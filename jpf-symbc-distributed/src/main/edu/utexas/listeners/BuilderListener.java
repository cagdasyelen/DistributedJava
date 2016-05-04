package edu.utexas.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.utexas.cert.SimpleCert;
import edu.utexas.util.PathConditionOps;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.bytecode.BytecodeUtils;
import gov.nasa.jpf.symbc.bytecode.INVOKESTATIC;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.UnsatPathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Types;
import gov.nasa.jpf.vm.VM;

public class BuilderListener extends ListenerAdapter {

	SimpleCert cert;
	Logger log;
	boolean DEBUG = false;

	List<Object> paths = new ArrayList<>();

	List<PathCondition> pcs = new ArrayList<>();

	int maxDepth = 0;

	public BuilderListener(Logger log) {
		this.log = log;
	}

	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread,
			Instruction nextInstruction, Instruction executedInstruction) {
		Config conf = vm.getConfig();
		MethodInfo mi = executedInstruction.getMethodInfo();

		// Add unsta PC
		if (UnsatPathCondition.isUnsat && cert != null
				&& UnsatPathCondition.unsatPC != null
				&& !UnsatPathCondition.unsatPC.isEmpty()) {
			pcs.addAll(UnsatPathCondition.unsatPC);
			if (paths.isEmpty()
					|| !(paths.get(paths.size() - 1) instanceof Boolean)) {
				paths.add(false);
			}
			PathConditionOps.UnsatPCCleanup();
		}

		// add parameters and arguments
		if (executedInstruction instanceof InvokeInstruction) {
			InvokeInstruction md = (InvokeInstruction) executedInstruction;

			int numberOfArgs = md.getArgumentValues(currentThread).length;
			StackFrame sf = currentThread.getTopFrame();
			MethodInfo invokedMethod = md.getInvokedMethod();
			String methodName = invokedMethod.getFullName();

			if (BytecodeUtils.isMethodSymbolic(conf,
					invokedMethod.getFullName(), numberOfArgs, null)) {
				cert = new SimpleCert(methodName);

				Object[] argValues = md.getArgumentValues(currentThread);
				for (int i = 0; i < argValues.length; i++) {
					cert.addArgValue(argValues[i]);
				}
				byte[] argTypes = invokedMethod.getArgumentTypes();
				for (int i = 0; i < argTypes.length; i++) {
					cert.addArgType(argTypes[i]);
				}
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
					cert.addSymValue(expLocal.stringPC().replaceAll(
							"\\[.*?\\]", ""));
					sfIndex++;
					if (argTypes[i] == Types.T_LONG
							|| argTypes[i] == Types.T_DOUBLE)
						sfIndex++;
				}
			}
			return;
		}

		// add test case for feasible paths
		if (executedInstruction instanceof ReturnInstruction) {
			if (cert != null && cert.getName().equals(mi.getFullName())) {
				ChoiceGenerator<?> cg = vm
						.getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
				if (cg instanceof PCChoiceGenerator) {
					PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
					if (pc != null) {
						assert pc.solve();
						pcs.add(pc);
						processCert(cert, pc);
					}
				}
			}
			return;
		}
	}

	private void processCert(SimpleCert cert, PathCondition pc) {
		assert cert.repOk();
		List<Object> onePath = new ArrayList<>();
		for (int i = 0; i < cert.getArgValues().size(); i++) {
			String e = cert.getSymValues().get(i);
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
		if (!paths.contains(onePath)) {
			paths.add(onePath);
		}
	}

	@Override
	public void searchFinished(Search search) {
		assert !paths.isEmpty();

		List<Object> subList = new ArrayList<>();
		if (paths.get(0) instanceof Boolean) {
			subList = paths.subList(1, paths.size());
		} else {
			subList = paths;
		}
		List<Object> finalList = new ArrayList<>();
		if (subList.get(subList.size() - 1) instanceof Boolean) {
			finalList = subList.subList(0, paths.size() - 1);
		} else {
			finalList = subList;
		}
		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < finalList.size(); i++) {
			if (finalList.get(i) instanceof Boolean) {
				indices.add(i);
			}
		}

		List<Integer> begins = new ArrayList<>();
		List<Integer> ends = new ArrayList<>();
		begins.add(0);
		for (int i : indices) {
			ends.add(i - 1);
			begins.add(i + 1);
		}
		ends.add(finalList.size() - 1);
		assert begins.size() == ends.size();
		for (int i = 0; i < begins.size(); i++) {
			List<Object> range = new ArrayList<>();
			assert !(finalList.get(begins.get(i)) instanceof Boolean);
			assert !(finalList.get(ends.get(i)) instanceof Boolean);
			if (begins.get(i) != ends.get(i)) {
				range.add(finalList.get(begins.get(i)));
				range.add(finalList.get(ends.get(i)));
			} else {
				range.add(finalList.get(begins.get(i)));
			}
			cert.addOnePath(range);
		}

		assert cert != null;
		assert cert.repOk();
		PathConditionOps.turnOffConstraintSolver();
	}

	@Override
	public void searchStarted(Search search) {
		PathConditionOps.turnOnConstraintSolver();
	}

	public SimpleCert getCert() {
		return cert;
	}

	@Override
	public void searchConstraintHit(Search search) {
		ChoiceGenerator<?> cg = search.getVM().getLastChoiceGeneratorOfType(
				PCChoiceGenerator.class);
		if (cg instanceof PCChoiceGenerator) {
			PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
			if (pc != null) {
				pcs.add(pc);
				assert pc.solve();
				processCert(cert, pc);
			}
		}
	}

	@Override
	public void stateAdvanced(Search search) {
		int depth = search.getDepth();
		if (maxDepth < depth) {
			maxDepth = depth;
		}
	}

	public List<Object> getPathsFromListener() {
		return paths;
	}

	public int getMaxDepth() {
		return maxDepth;
	}
}
