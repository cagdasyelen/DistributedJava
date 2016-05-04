package edu.utexas.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import edu.utexas.cert.SimpleCert;
import edu.utexas.cert.Tree;
import edu.utexas.cert.TreeNode;
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
import gov.nasa.jpf.symbc.numeric.UnsatPC;
import gov.nasa.jpf.symbc.numeric.UnsatPathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Types;
import gov.nasa.jpf.vm.VM;

public class AdvancedBuilderListener extends ListenerAdapter {

	SimpleCert cert;
	Logger log;
	boolean DEBUG = false;
	Tree tree;
	int depth = 0;

	PathCondition lastPc = null;

	int actualDepth = 0;
	int depthCount = 0;

	int maxDepth = 0;

	Stack<Integer> depthStack = new Stack<>();

	public AdvancedBuilderListener(Logger log, int depth) {
		this.log = log;
		this.depth = depth;
	}

	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread,
			Instruction nextInstruction, Instruction executedInstruction) {
		Config conf = vm.getConfig();
		MethodInfo mi = executedInstruction.getMethodInfo();

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

				TreeNode root = new TreeNode(null, null);
				tree = new Tree(root);

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
				// processDepthCount();
				ChoiceGenerator<?> cg = vm
						.getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
				if (cg instanceof PCChoiceGenerator) {
					PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
					if (pc != null) {
						assert tree.getCur() != tree.getRoot();
						if (!tree.getCur().getPc().equals(pc)) {
							for (TreeNode n : tree
									.getUnprocessedSameLevelNodes()) {
								if (n.getPc().equals(pc)) {
									tree.setCur(n);
									break;
								}
							}
						}
						assert tree.getCur().getPc().equals(pc);
						lastPc = pc;
						assert pc.solve();
						processCert(cert, pc);
					}
				}
			}
			return;
		}

		if (executedInstruction instanceof IfInstruction
				|| executedInstruction instanceof DoubleCompareInstruction) {
			if (tree != null && !UnsatPathCondition.unsatPC.isEmpty()) {
				List<TreeNode> children = new ArrayList<>();
				for (PathCondition pc : UnsatPathCondition.unsatPC) {
					if (!(pc instanceof UnsatPC)
							&& tree.getCur() != tree.getRoot()) {
						if (!PathConditionOps.pc1ExtendsFromPc2(pc, tree
								.getCur().getPc())) {
							for (TreeNode pp : tree
									.getUnprocessedSameLevelNodes()) {
								PathCondition ppc = pp.getPc();
								if (PathConditionOps.pc1ExtendsFromPc2(pc, ppc)) {
									tree.setCur(pp);
									break;
								}
							}
						}
						if (tree.getCur() != tree.getRoot()) {
							if (!PathConditionOps.pc1ExtendsFromPc2(pc, tree
									.getCur().getPc())) {
								assert lastPc != null;
								assert PathConditionOps.pc1ExtendsFromPc2(pc,
										lastPc);
								PathConditionOps.UnsatPCCleanup();
								return;
							}
						}
					}
					TreeNode tn = new TreeNode(tree.getCur(), pc);
					if (pc instanceof UnsatPC) {
						tn.setProcessed();
					}
					children.add(tn);
				}
				tree.addChildren(children);
				PathConditionOps.UnsatPCCleanup();
			}

			if (cert != null
					&& executedInstruction != nextInstruction
					&& !(executedInstruction instanceof DoubleCompareInstruction)) {
				depthCount++;

				if (depthCount > maxDepth) {
					maxDepth = depthCount;
				}

				assert !(depthCount > depth);

				if (depthCount == depth) {
					vm.getSystemState().setIgnored(true);
					handleSearchConstraintHit(vm.getSearch());

					return;
				}
			}
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
				String pcString = pc.toString();
				if (pcString.indexOf("[") == -1) {
					pc.solveOld();
					pcString = pc.toString();
				}
				Object actualValue = cert.getArgValues().get(i);
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
						if (val.equals("0")) {
							onePath.add(false);
						} else if (!val.equals("0")) {
							onePath.add(true);
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
		assert tree != null;
		TreeNode cur = tree.getCur();
		assert cur != null;
		assert cur.getPc().equals(pc);
		cur.setTestCase(onePath);
		cur.setProcessed();
		tree.next();
	}

	@Override
	public void searchFinished(Search search) {
		System.out.println("Search finished!");
		assert tree != null;
		assert tree.getCur() == tree.getRoot();
		List<Object> paths = tree.getCert();

		assert !paths.isEmpty();

		// remove duplicate false
		List<Object> pathsNew = new ArrayList<>();
		for (Object o : paths) {
			if (o instanceof Boolean) {
				if (!pathsNew.isEmpty()
						&& !(pathsNew.get(pathsNew.size() - 1) instanceof Boolean)) {
					pathsNew.add(o);
				}
			} else {
				pathsNew.add(o);
			}
		}

		List<Object> subList = new ArrayList<>();
		if (pathsNew.get(0) instanceof Boolean) {
			subList = pathsNew.subList(1, pathsNew.size());
		} else {
			subList = pathsNew;
		}
		List<Object> finalList = new ArrayList<>();
		if (subList.get(subList.size() - 1) instanceof Boolean) {
			finalList = subList.subList(0, pathsNew.size() - 1);
		} else {
			finalList = subList;
		}
		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < finalList.size(); i++) {
			if (finalList.get(i) instanceof Boolean) {
				indices.add(i);
			}
		}

		if (indices.isEmpty()) {
			processRange(finalList);
		} else {
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
		}

		assert cert != null;
		assert cert.repOk();
		PathConditionOps.UnsatPCCleanup();
		PathConditionOps.turnOffConstraintSolver();
	}

	@Override
	public void searchStarted(Search search) {
		PathConditionOps.turnOnConstraintSolver();
	}

	public SimpleCert getCert() {
		return cert;
	}

	private void handleSearchConstraintHit(Search search) {
		ChoiceGenerator<?> cg = search.getVM().getLastChoiceGeneratorOfType(
				PCChoiceGenerator.class);
		if (cg instanceof PCChoiceGenerator) {
			PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
			if (pc != null) {
				if (tree.getCur() == tree.getRoot()) {
					return;
				}
				if (lastPc != null && pc.equals(lastPc)) {
					return;
				}
				if (!tree.getCur().getPc().equals(pc)) {
					for (TreeNode n : tree.getUnprocessedSameLevelNodes()) {
						if (n.getPc().equals(pc)) {
							tree.setCur(n);
							break;
						}
					}
				}
				if (!tree.getCur().getPc().equals(pc)) {
					// tree.setCur(tree.getCur().getParent());
					// assert tree.getCur().getPc().equals(pc);
					// tree.getCur().setChildren(new ArrayList<TreeNode>());
					for (TreeNode tn : tree.getCur().getParent().getChildren()) {
						if (!(tn.getPc() instanceof UnsatPC)) {
							assert tn.getPc().solve();
							lastPc = tn.getPc();
							processCert(cert, tn.getPc());
						}
					}
					// depthCount++;
					// if (realDepth < depthCount) {
					// realDepth = depthCount;
					// }
					return;
				}
				assert pc.solve();
				lastPc = pc;
				processCert(cert, pc);
			}
		}
	}

	@Override
	public void stateAdvanced(Search search) {
		int d = search.getDepth();
		if (actualDepth < d) {
			actualDepth = d;
		}
		depthStack.push(depthCount);
	}

	@Override
	public void stateBacktracked(Search search) {
		if (depthStack.isEmpty()) {
			return;
		}
		depthStack.pop();
		if (depthStack.isEmpty()) {
			depthCount = 0;
		} else {
			depthCount = depthStack.peek();
		}
	}

	public int getActualDepth() {
		return actualDepth;
	}

	public Tree getTree() {
		return tree;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	private void processRange(List<Object> finalList) {
		assert !finalList.isEmpty();
		int size = finalList.size() / 2;
		int i = 0;
		for (; i < size; i++) {
			List<Object> range = new ArrayList<>();
			range.add(finalList.get(i * 2));
			if (i * 2 + 1 < finalList.size()) {
				range.add(finalList.get(i * 2 + 1));
			}
			cert.addOnePath(range);
		}
		if (i * 2 == finalList.size() - 1) {
			List<Object> finalRange = new ArrayList<>();
			finalRange.add(finalList.get(i * 2));
			cert.addOnePath(finalRange);
		}
	}

	public List<Object> generateAlternateRanges(int rangeSize) {
		assert rangeSize != 0;
		List<Object> mypaths = tree.getCert();
		// remove false
		List<Object> pathsNew = new ArrayList<>();
		for (Object o : mypaths) {
			if (!(o instanceof Boolean)) {
				pathsNew.add(o);
			}
		}
		List<Object> result = new ArrayList<>();
		int space = pathsNew.size() / rangeSize;
		if (space >= 1) {
			for (int i = 0; i < rangeSize; i++) {
				List<Object> range = new ArrayList<Object>();
				if (i == 0) {
					range.add(null);
				} else {
					range.add(pathsNew.get(i * space));
				}
				if (i != 0 && i == rangeSize - 1) {
					range.add(null);
				} else {
					range.add(pathsNew.get((i + 1) * space));
				}
				result.add(range);
			}

			this.cert.setTraditionalRanges(result);
			return result;
		} else {
			throw new IllegalArgumentException("Number of ranges is too small!");
		}
	}
}
