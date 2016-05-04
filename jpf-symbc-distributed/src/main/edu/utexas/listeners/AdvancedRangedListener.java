package edu.utexas.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import edu.utexas.cert.SimpleCert;
import edu.utexas.util.PathConditionOps;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.DoubleCompareInstruction;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.bytecode.BytecodeUtils;
import gov.nasa.jpf.symbc.bytecode.INVOKESTATIC;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Types;
import gov.nasa.jpf.vm.VM;

public class AdvancedRangedListener extends ListenerAdapter {

	Logger log;
	SimpleCert cert;
	List<Object> ranges = new ArrayList<>();
	int oldDepth;
	int newDepth;
	int curDepth = 0;

	Stack<Integer> depthStack = new Stack<>();

	List<String> theSymValues = new ArrayList<>();
	Object startPath;
	Object endPath;
	boolean inRange = false;
	boolean enterLastPath = false;

	List<Object> finalPaths = new ArrayList<>();

	public AdvancedRangedListener(Logger log, SimpleCert cert,
			List<Object> ranges, int oldDepth, int newDepth) {
		this.log = log;
		this.cert = cert;
		this.ranges = ranges;
		this.oldDepth = oldDepth;
		this.newDepth = newDepth;
		assert ranges != null;
		assert ranges.size() == 2;
		startPath = ranges.get(0);
		endPath = ranges.get(1);
	}

	@Override
	public void searchStarted(Search search) {
		assert log != null;
		assert cert != null;
		assert ranges != null;
		assert startPath != null;
		assert endPath != null;
		PathConditionOps.UnsatPCCleanup();
		PathConditionOps.turnOffConstraintSolver();
	}

	@Override
	public void searchFinished(Search search) {
		PathConditionOps.UnsatPCCleanup();
	}

	@Override
	public void executeInstruction(VM vm, ThreadInfo currentThread,
			Instruction instructionToExecute) {
		if (curDepth >= oldDepth) {
			PathConditionOps.turnOnConstraintSolver();
		} else {
			PathConditionOps.turnOffConstraintSolver();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread,
			Instruction nextInstruction, Instruction executedInstruction) {
		Config conf = vm.getConfig();
		MethodInfo mi = executedInstruction.getMethodInfo();
		StackFrame sf = currentThread.getTopFrame();

		if (executedInstruction instanceof InvokeInstruction) {
			InvokeInstruction md = (InvokeInstruction) executedInstruction;
			int numberOfArgs = md.getArgumentValues(currentThread).length;
			MethodInfo invokedMethod = md.getInvokedMethod();
			String methodName = invokedMethod.getFullName();

			if (BytecodeUtils.isMethodSymbolic(conf,
					invokedMethod.getFullName(), numberOfArgs, null)) {
				assert methodName.equals(cert.getName());

				byte[] argTypes = invokedMethod.getArgumentTypes();

				// get the symbolic values
				LocalVarInfo[] argsInfo = invokedMethod.getArgumentLocalVars();

				if (argsInfo == null)
					throw new RuntimeException(
							"ERROR: you need to turn debug option on");

				int sfIndex = 1; // do not consider implicit param "this"
				if (md instanceof INVOKESTATIC) {
					sfIndex = 0; // no "this" for static
				}

				for (int i = 0; i < numberOfArgs; i++) {
					Expression expLocal = (Expression) sf.getLocalAttr(sfIndex);
					theSymValues.add(expLocal.stringPC().replaceAll(
							"\\[.*?\\]", ""));
					sfIndex++;
					if (argTypes[i] == Types.T_LONG
							|| argTypes[i] == Types.T_DOUBLE)
						sfIndex++;
				}
			}
			return;
		}

		if (executedInstruction instanceof IfInstruction
				|| executedInstruction instanceof DoubleCompareInstruction) {
			if (!theSymValues.isEmpty()
					&& executedInstruction != nextInstruction
					&& !(executedInstruction instanceof DoubleCompareInstruction)) {
				curDepth++;
			}

			if (nextInstruction != executedInstruction
					&& !(executedInstruction instanceof DoubleCompareInstruction)) {
				ChoiceGenerator<?> cg = vm.getChoiceGenerator();
				if (cg instanceof PCChoiceGenerator) {
					PCChoiceGenerator pccg = (PCChoiceGenerator) cg;
					PathCondition pc = pccg.getCurrentPC();
					// if (cg.getInsn() != executedInstruction
					// && !(cg.getInsn() instanceof DoubleCompareInstruction)) {
					// return;
					// }
					if (pc.toString().contains("[")) {
						return;
					}
					if (curDepth <= oldDepth && !inRange) {
						if (!PathConditionOps.pathSatisfiable(pc, theSymValues,
								(List<Object>) startPath)) {
							vm.getSearch().setIgnoredState(true);
							return;
						} else if (curDepth == oldDepth) {
							inRange = true;
						}
					}
					if (inRange && curDepth == oldDepth) {
						if (!enterLastPath
								&& PathConditionOps.pathSatisfiable(pc,
										theSymValues, (List<Object>) endPath)) {
							enterLastPath = true;
						}
					}
					if (inRange
							&& enterLastPath
							&& curDepth <= oldDepth
							&& !PathConditionOps.pathSatisfiable(pc,
									theSymValues, (List<Object>) endPath)) {
						inRange = false;
					}
					if (!inRange && enterLastPath) {
						vm.getSearch().setIgnoredState(true);
						return;
					}
					if (curDepth > oldDepth) {
						assert inRange;
					}
				}
			}

			if (curDepth == newDepth) {
				vm.getSystemState().setIgnored(true);
				handleSearchConstraintHit(vm.getSearch());

				return;
			}

			assert !(curDepth > newDepth);
		}

		// add test case for feasible paths
		if (executedInstruction instanceof ReturnInstruction) {
			if (cert != null && cert.getName().equals(mi.getFullName())) {
				ChoiceGenerator<?> cg = vm
						.getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
				if (cg instanceof PCChoiceGenerator) {
					PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
					if (PathConditionOps.pathSatisfiable(pc, theSymValues,
							(List<Object>) startPath)) {
						inRange = true;
					}
					if (curDepth <= oldDepth
							&& PathConditionOps.pathSatisfiable(pc,
									theSymValues, (List<Object>) endPath)) {
						inRange = false;
						enterLastPath = true;
					}
					if (pc != null) {
						if (pc.solve()) {
							processCert(cert, pc);
						} else {
							assert false;
						}
					}
				}
			}
			return;
		}

	}

	private void handleSearchConstraintHit(Search search) {
		ChoiceGenerator<?> cg = search.getVM().getLastChoiceGeneratorOfType(
				PCChoiceGenerator.class);
		if (cg instanceof PCChoiceGenerator) {
			PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
			assert pc.solve();
			processCert(cert, pc);
		}
	}

	private void processCert(SimpleCert cert, PathCondition pc) {
		assert cert.repOk();
		List<Object> onePath = new ArrayList<>();
		for (int i = 0; i < cert.getArgValues().size(); i++) {
			String e = theSymValues.get(i);
			if (e != null) {
				String token = e;
				byte actualType = cert.getArgTypes().get(i);
				Object actualValue = cert.getArgValues().get(i);
				String pcString = pc.toString();
				if (pcString.contains(token)) {
					String temp = pcString.substring(pcString.indexOf(token));
					String val = temp.substring(temp.indexOf("[") + 1,
							temp.indexOf("]"));
					if (val == null) {
						onePath.add(actualValue);
					}
					if (actualType == Types.T_INT) {
						onePath.add(Integer.parseInt(val));
					} else if (actualType == Types.T_FLOAT) {
						onePath.add(Float.parseFloat(val));
					} else if (actualType == Types.T_LONG) {
						onePath.add(Long.parseLong(val));
					} else if (actualType == Types.T_DOUBLE) {
						onePath.add(Double.parseDouble(val));
					} else if (actualType == Types.T_BOOLEAN) {
						if (val.equals("1")) {
							onePath.add(true);
						} else if (val.equals("0")) {
							onePath.add(false);
						} else {
							onePath.add(Boolean.parseBoolean(val));
						}
					} else {
						throw new RuntimeException(
								"## Error: listener does not support type "
										+ "other than int, long, float, double and "
										+ "boolean");
					}
				} else {
					onePath.add(actualValue);
				}
			}
		}
		assert onePath != null;
		if (!finalPaths.contains(onePath)) {
			finalPaths.add(onePath);
			if (onePath.contains(-2147483648)) {
				System.out.println("Error");
				assert false;
			}
		}
	}

	@Override
	public void stateBacktracked(Search search) {
		if (depthStack.isEmpty()) {
			return;
		}
		depthStack.pop();
		if (depthStack.isEmpty()) {
			curDepth = 0;
		} else {
			curDepth = depthStack.peek();
		}
	}

	@Override
	public void stateAdvanced(Search search) {
		// if (depthStack.isEmpty() || depthStack.peek() != curDepth) {
		depthStack.push(curDepth);
		// }
	}

	public List<Object> getFinalPaths() {
		return finalPaths;
	}

}
