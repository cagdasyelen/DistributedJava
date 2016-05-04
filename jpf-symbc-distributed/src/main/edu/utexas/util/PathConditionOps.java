package edu.utexas.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utexas.cert.UnusedVar;
import gov.nasa.jpf.symbc.numeric.BinaryLinearIntegerExpression;
import gov.nasa.jpf.symbc.numeric.BinaryNonLinearIntegerExpression;
import gov.nasa.jpf.symbc.numeric.BinaryRealExpression;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.LinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.LinearIntegerExpression;
import gov.nasa.jpf.symbc.numeric.NonLinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.Operator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.numeric.RealConstraint;
import gov.nasa.jpf.symbc.numeric.RealExpression;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.numeric.UnsatPathCondition;

public class PathConditionOps {

	public static void turnOnConstraintSolver() {
		PathCondition.setReplay(false);
	}

	public static void turnOffConstraintSolver() {
		PathCondition.setReplay(true);
	}

	public static boolean h5(String pcString, Set<String> unsatPcs) {
		for (String upc : unsatPcs) {
			if (PathConditionOps.pc1ContainsPc2(pcString, upc)) {
				return true;
			}
		}
		return false;
	}

	public static Set<String> parsePathCondition(PathCondition pc) {
		String[] tokens = pc.stringPC().split("[\\n\\&\\&]+");
		Set<String> result = new HashSet<String>();
		for (String s : tokens) {
			if (!s.startsWith("constraint # =")) {
				result.add(s.replaceAll("\\&\\&", "").trim());
			}
		}
		return result;
	}

	public static Set<String> parsePathCondition(String pc) {
		String[] tokens = pc.split("[\\n\\&\\&]+");
		Set<String> result = new HashSet<String>();
		for (String s : tokens) {
			if (!s.startsWith("constraint # =")) {
				result.add(s.replaceAll("\\&\\&", "").trim());
			}
		}
		return result;
	}

	public static boolean isEquivalent(PathCondition pc1, PathCondition pc2) {
		if (pc1 == null || pc2 == null) {
			return false;
		}
		if (pc1.count() != pc2.count()) {
			return false;
		}
		Set<String> set1 = parsePathCondition(pc1);
		Set<String> set2 = parsePathCondition(pc2);
		if (set1.containsAll(set2) && set2.containsAll(set1)) {
			return true;
		}
		return false;
	}

	public static boolean isEquivalent(String pc1, String pc2) {
		if (pc1 == null || pc2 == null) {
			return false;
		}
		Set<String> set1 = parsePathCondition(pc1);
		Set<String> set2 = parsePathCondition(pc2);
		if (set1.containsAll(set2) && set2.containsAll(set1)) {
			return true;
		}
		return false;
	}

	public static boolean pc1ContainsPc2(PathCondition pc1, PathCondition pc2) {
		if (pc1 == null || pc2 == null) {
			return false;
		}
		if (pc1.count() < pc2.count()) {
			return false;
		}
		Set<String> set1 = parsePathCondition(pc1);
		Set<String> set2 = parsePathCondition(pc2);
		if (set1.containsAll(set2)) {
			return true;
		}
		return false;
	}

	public static boolean pc1ExtendsFromPc2(PathCondition pc1, PathCondition pc2) {
		if (pc1.equals(pc2)) {
			return true;
		}
		if (pc1.count() - 1 != pc2.count()) {
			return false;
		}
		return pc1.hasConstraint(pc2.header);
	}

	public static boolean pc1ContainsPc2(String pc1, String pc2) {
		if (pc1 == null || pc2 == null) {
			return false;
		}
		Set<String> set1 = parsePathCondition(pc1);
		Set<String> set2 = parsePathCondition(pc2);
		if (set1.containsAll(set2)) {
			return true;
		}
		return false;
	}

	public static Object evaluateExpression(Expression e,
			List<String> symValues, List<Object> cases) {
		assert symValues.size() == cases.size();
		if ((e instanceof IntegerConstant)) {
			return ((IntegerConstant) e).value;
		} else if (e instanceof RealConstant) {
			return ((RealConstant) e).value;
		} else if ((e instanceof SymbolicInteger)
				|| (e instanceof SymbolicReal)) {
			String s = e.stringPC().replaceAll("\\[.*?\\]", "");
			if (!symValues.contains(s)) {
				return null;
			}
			assert symValues.contains(s);
			int i = symValues.indexOf(s);
			return cases.get(i);
		} else if (e instanceof BinaryLinearIntegerExpression) {
			BinaryLinearIntegerExpression ble = (BinaryLinearIntegerExpression) e;
			return calculateExpression(
					evaluateExpression(ble.getLeft(), symValues, cases),
					ble.getOp(),
					evaluateExpression(ble.getRight(), symValues, cases));
		} else if (e instanceof BinaryNonLinearIntegerExpression) {
			BinaryNonLinearIntegerExpression bnle = (BinaryNonLinearIntegerExpression) e;
			return calculateExpression(
					evaluateExpression(bnle.left, symValues, cases), bnle.op,
					evaluateExpression(bnle.right, symValues, cases));
		} else if (e instanceof BinaryRealExpression) {
			BinaryRealExpression bre = (BinaryRealExpression) e;
			return calculateExpression(
					evaluateExpression(bre.getLeft(), symValues, cases),
					bre.getOp(),
					evaluateExpression(bre.getRight(), symValues, cases));
		} else {
			throw new RuntimeException("Unsupported expression type: "
					+ e.toString());
		}
	}

	public static Object calculateExpression(Object e1, Operator op, Object e2) {
		assert e1.getClass().equals(e2.getClass());
		if (e1 instanceof Integer && e2 instanceof Integer) {
			int i1 = ((Integer) e1).intValue();
			int i2 = ((Integer) e2).intValue();
			return calculateIntValue(i1, op, i2);
		} else if (e1 instanceof Double && e2 instanceof Double) {
			double d1 = ((Double) e1).doubleValue();
			double d2 = ((Double) e2).doubleValue();
			return calculateDoubleValue(d1, op, d2);
		} else if (e1 instanceof Float && e2 instanceof Float) {
			float f1 = ((Float) e1).floatValue();
			float f2 = ((Float) e2).floatValue();
			return calculateFloatValue(f1, op, f2);
		} else {
			throw new RuntimeException("Unsupported type in calculating "
					+ "path condition");
		}
	}

	private static int calculateIntValue(int o1, Operator op, int o2) {
		if (op.equals(Operator.DIV)) {
			return o1 / o2;
		} else if (op.equals(Operator.MUL)) {
			return o1 * o2;
		} else if (op.equals(Operator.PLUS)) {
			return o1 + o2;
		} else if (op.equals(Operator.MINUS)) {
			return o1 - o2;
		} else {
			throw new RuntimeException("Unsupported operation: "
					+ op.toString());
		}
	}

	private static double calculateDoubleValue(double o1, Operator op, double o2) {
		if (op.equals(Operator.DIV)) {
			return o1 / o2;
		} else if (op.equals(Operator.MUL)) {
			return o1 * o2;
		} else if (op.equals(Operator.PLUS)) {
			return o1 + o2;
		} else if (op.equals(Operator.MINUS)) {
			return o1 - o2;
		} else {
			throw new RuntimeException("Unsupported operation: "
					+ op.toString());
		}
	}

	private static float calculateFloatValue(float o1, Operator op, float o2) {
		if (op.equals(Operator.DIV)) {
			return o1 / o2;
		} else if (op.equals(Operator.MUL)) {
			return o1 * o2;
		} else if (op.equals(Operator.PLUS)) {
			return o1 + o2;
		} else if (op.equals(Operator.MINUS)) {
			return o1 - o2;
		} else {
			throw new RuntimeException("Unsupported operation: "
					+ op.toString());
		}
	}

	public static boolean constraintSatisfiable(Constraint c, Object left,
			Object right) {
		Comparator comp = c.getComparator();
		if (comp.equals(Comparator.EQ) && left instanceof Boolean
				&& right instanceof Integer) {
			if (((Boolean) left) && ((Integer) right).intValue() != 1) {
				return false;
			} else if (!((Boolean) left) && ((Integer) right).intValue() != 0) {
				return false;
			}
		} else if (comp.equals(Comparator.EQ) && left instanceof Integer
				&& right instanceof Boolean) {
			if (((Boolean) right) && ((Integer) left).intValue() != 1) {
				return false;
			} else if (!((Boolean) right) && ((Integer) left).intValue() != 0) {
				return false;
			}
		} else if (comp.equals(Comparator.NE) && left instanceof Boolean
				&& right instanceof Integer) {
			if (((Boolean) left) && ((Integer) right).intValue() == 1) {
				return false;
			} else if (!((Boolean) left) && ((Integer) right).intValue() == 0) {
				return false;
			}
		} else if (comp.equals(Comparator.NE) && left instanceof Integer
				&& right instanceof Boolean) {
			if (((Boolean) right) && ((Integer) left).intValue() == 1) {
				return false;
			} else if (!((Boolean) right) && ((Integer) left).intValue() == 0) {
				return false;
			}
		} else if (comp.equals(Comparator.EQ) && !left.equals(right)) {
			return false;
		} else if (comp.equals(Comparator.NE) && left.equals(right)) {
			return false;
		} else if (comp.equals(Comparator.GE)) {
			if (left instanceof Integer && right instanceof Integer) {
				if (((Integer) left).intValue() < ((Integer) right).intValue()) {
					return false;
				}
			} else if (left instanceof Double && right instanceof Double) {
				if (((Double) left).doubleValue() < ((Double) right)
						.doubleValue()) {
					return false;
				}
			} else if (left instanceof Float && right instanceof Float) {
				if (((Float) left).floatValue() < ((Float) right).floatValue()) {
					return false;
				}
			}
		} else if (comp.equals(Comparator.LE)) {
			if (left instanceof Integer && right instanceof Integer) {
				if (((Integer) left).intValue() > ((Integer) right).intValue()) {
					return false;
				}
			} else if (left instanceof Double && right instanceof Double) {
				if (((Double) left).doubleValue() > ((Double) right)
						.doubleValue()) {
					return false;
				}
			} else if (left instanceof Float && right instanceof Float) {
				if (((Float) left).floatValue() > ((Float) right).floatValue()) {
					return false;
				}
			}
		} else if (comp.equals(Comparator.GT)) {
			if (left instanceof Integer && right instanceof Integer) {
				if (((Integer) left).intValue() < ((Integer) right).intValue()
						|| ((Integer) left).intValue() == ((Integer) right)
								.intValue()) {
					return false;
				}
			} else if (left instanceof Double && right instanceof Double) {
				if (((Double) left).doubleValue() < ((Double) right)
						.doubleValue()
						|| ((Double) left).doubleValue() == ((Double) right)
								.doubleValue()) {
					return false;
				}
			} else if (left instanceof Float && right instanceof Float) {
				if (((Float) left).floatValue() < ((Float) right).floatValue()
						&& ((Float) left).floatValue() == ((Float) right)
								.floatValue()) {
					return false;
				}
			}
		} else if (comp.equals(Comparator.LT)) {
			if (left instanceof Integer && right instanceof Integer) {
				if (((Integer) left).intValue() > ((Integer) right).intValue()
						|| ((Integer) left).intValue() == ((Integer) right)
								.intValue()) {
					return false;
				}
			} else if (left instanceof Double && right instanceof Double) {
				if (((Double) left).doubleValue() > ((Double) right)
						.doubleValue()
						|| ((Double) left).doubleValue() == ((Double) right)
								.doubleValue()) {
					return false;
				}
			} else if (left instanceof Float && right instanceof Float) {
				if (((Float) left).floatValue() > ((Float) right).floatValue()
						&& ((Float) left).floatValue() == ((Float) right)
								.floatValue()) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean pathSatisfiable(PathCondition pc,
			List<String> symValues, List<Object> cases) {
		assert symValues.size() == cases.size();
		Constraint c = pc.header;
		while (c != null) {
			Object left = evaluateExpression(c.getLeft(), symValues, cases);
			Object right = evaluateExpression(c.getRight(), symValues, cases);
			assert left != null;
			assert right != null;
			if (!constraintSatisfiable(c, left, right)) {
				return false;
			}
			c = c.getTail();
		}
		return true;
	}

	public static boolean pathSatisfiableForStart(PathCondition pc,
			List<String> symValues, List<Object> cases) {
		if (cases == null) {
			return true;
		}
		assert symValues.size() == cases.size();
		Constraint c = pc.header;
		while (c != null) {
			Object left = evaluateExpression(c.getLeft(), symValues, cases);
			Object right = evaluateExpression(c.getRight(), symValues, cases);
			assert left != null;
			assert right != null;
			if (!constraintSatisfiable(c, left, right)) {
				return false;
			}
			c = c.getTail();
		}
		return true;
	}

	public static boolean pathSatisfiableForEnd(PathCondition pc,
			List<String> symValues, List<Object> cases) {
		if (cases == null) {
			return false;
		}
		assert symValues.size() == cases.size();
		Constraint c = pc.header;
		while (c != null) {
			Object left = evaluateExpression(c.getLeft(), symValues, cases);
			Object right = evaluateExpression(c.getRight(), symValues, cases);
			assert left != null;
			assert right != null;
			if (!constraintSatisfiable(c, left, right)) {
				return false;
			}
			c = c.getTail();
		}
		return true;
	}

	public static boolean advancedPathSatisfiable(PathCondition pc,
			List<String> symValues, List<Object> cases) {
		assert symValues.size() == cases.size();
		Constraint c = pc.header;
		while (c != null) {
			if (!constraintHasUnusedVar(c, symValues, cases)) {
				Object left = evaluateExpression(c.getLeft(), symValues, cases);
				Object right = evaluateExpression(c.getRight(), symValues,
						cases);
				assert left != null;
				assert right != null;
				if (!constraintSatisfiable(c, left, right)) {
					return false;
				}
			} else {

			}
			c = c.getTail();
		}
		return true;
	}

	public static boolean constraintHasUnusedVar(Constraint c,
			List<String> symValues, List<Object> cases) {
		assert c != null;
		Expression left = c.getLeft();
		Expression right = c.getRight();
		assert left != null;
		assert right != null;
		return expressionHasUnusedVar(left, symValues, cases)
				|| expressionHasUnusedVar(right, symValues, cases);
	}

	public static boolean expressionHasUnusedVar(Expression e,
			List<String> symValues, List<Object> cases) {
		assert e != null;
		if ((e instanceof IntegerConstant)) {
			return false;
		} else if (e instanceof RealConstant) {
			return false;
		} else if ((e instanceof SymbolicInteger)
				|| (e instanceof SymbolicReal)) {
			String s = e.stringPC().replaceAll("\\[.*?\\]", "");
			if (!symValues.contains(s)) {
				return false;
			}
			assert symValues.contains(s);
			int i = symValues.indexOf(s);
			return cases.get(i) instanceof UnusedVar;
		} else if (e instanceof BinaryLinearIntegerExpression) {
			BinaryLinearIntegerExpression ble = (BinaryLinearIntegerExpression) e;
			return expressionHasUnusedVar(ble.getLeft(), symValues, cases)
					|| expressionHasUnusedVar(ble.getLeft(), symValues, cases);
		} else if (e instanceof BinaryNonLinearIntegerExpression) {
			BinaryNonLinearIntegerExpression bnle = (BinaryNonLinearIntegerExpression) e;
			return expressionHasUnusedVar(bnle.left, symValues, cases)
					|| expressionHasUnusedVar(bnle.right, symValues, cases);
		} else if (e instanceof BinaryRealExpression) {
			BinaryRealExpression bre = (BinaryRealExpression) e;
			return expressionHasUnusedVar(bre.getLeft(), symValues, cases)
					|| expressionHasUnusedVar(bre.getLeft(), symValues, cases);
		} else {
			throw new RuntimeException("Unsupported expression type: "
					+ e.toString());
		}
	}

	public static String processPCString(String pc) {
		return pc.replaceAll("\\[.*?\\]", "").replaceAll("_\\d*?_SYM", "_SYM")
				.replaceAll("REAL_\\d+", "REAL").replaceAll("INT_\\d+", "INT");
	}

	public static void UnsatPCCleanup() {
		UnsatPathCondition.isUnsat = false;
		UnsatPathCondition.unsatPC = new ArrayList<>();
		UnsatPathCondition.unknownPC = new ArrayList<>();
	}

	/**
	 * Returns false if unsat is unsure; returns true if pc is unsat.
	 * 
	 * @param pc
	 * @return
	 */
	public static boolean checkUnsatWhenLastConstraintIsSimpleEqual(
			PathCondition pc) {
		Constraint c = pc.header;
		Constraint c_copy = pc.header.getTail();
		if (c_copy == null) {
			return false;
		}
		PathCondition nextPC = new PathCondition();
		nextPC.prependAllConjuncts(c_copy);
		Expression left = c.getLeft();
		Expression right = c.getRight();
		if (c.getComparator().equals(Comparator.EQ)) {
			Expression e = null;
			Object value = null;
			if (left instanceof SymbolicInteger
					&& right instanceof IntegerConstant) {
				e = left;
				value = ((IntegerConstant) right).value;
			} else if (left instanceof SymbolicReal
					&& right instanceof RealConstant) {
				e = left;
				value = ((RealConstant) right).value;
			} else if (left instanceof IntegerConstant
					&& right instanceof SymbolicInteger) {
				e = right;
				value = ((IntegerConstant) left).value;
			} else if (left instanceof RealConstant
					&& right instanceof SymbolicReal) {
				e = right;
				value = ((RealConstant) left).value;
			} else {
				return false;
			}
			List<String> symValue = new ArrayList<>();
			symValue.add(e.stringPC().replaceAll("\\[.*?\\]", ""));
			List<Object> cases = new ArrayList<>();
			cases.add(value);
			c = c.getTail();
			while (c != null) {
				Object leftO = evaluateExpression(c.getLeft(), symValue, cases);
				Object rightO = evaluateExpression(c.getRight(), symValue,
						cases);
				if (leftO == null || rightO == null) {
					c = c.getTail();
					continue;
				}
				if (!constraintSatisfiable(c, leftO, rightO)) {
					return true;
				}
				c = c.getTail();
			}
		}
		return checkUnsatWhenLastConstraintIsSimpleEqual(nextPC);
	}

	public static boolean determineRangeInfeasibility(PathCondition pc) {
		Constraint c = pc.header;
		Expression left = c.getLeft();
		Expression right = c.getRight();
		if (left instanceof SymbolicInteger && right instanceof IntegerConstant) {
			if (c.getComparator().equals(Comparator.GT)) {
				Constraint other = c.getTail();
				while (other != null) {
					if (other.getLeft().equals(left)
							&& other.getComparator().equals(Comparator.LT)
							&& other.getRight() instanceof IntegerConstant) {
						int value1 = ((IntegerConstant) right).value;
						int value2 = ((IntegerConstant) other.getRight()).value;
						if (value1 > value2) {
							return true;
						}
					}
					other = other.getTail();
				}
			}
			if (c.getComparator().equals(Comparator.LT)) {
				Constraint other = c.getTail();
				while (other != null) {
					if (other.getLeft().equals(left)
							&& other.getComparator().equals(Comparator.GT)
							&& other.getRight() instanceof IntegerConstant) {
						int value1 = ((IntegerConstant) right).value;
						int value2 = ((IntegerConstant) other.getRight()).value;
						if (value1 < value2) {
							return true;
						}
					}
					other = other.getTail();
				}
			}
		}
		return false;
	}

	public static boolean determineReorderInfeasiblity(PathCondition pc) {
		assert pc != null;
		Constraint c = pc.header;
		while (c != null) {
			Constraint reorder = reOrder(c);
			if (pcContainsOppositeOrNot(pc, reorder)) {
				return true;
			}

			Constraint general = toGeneralForm(c);
			if (general != null && pcContainsOppositeOrNot(pc, general)) {
				return true;
			}

			Constraint reOrderGeneral = toGeneralForm(reorder);
			if (reOrderGeneral != null
					&& pcContainsOppositeOrNot(pc, reOrderGeneral)) {
				return true;
			}

			c = c.getTail();
		}
		return false;
	}

	public static boolean pcContainsOppositeOrNot(PathCondition pc, Constraint c) {
		Expression left = c.getLeft();
		Expression right = c.getRight();
		Constraint oppC = formOppositeConstriant(c.getComparator(), left, right);
		Constraint notC1 = formNotConstraint1(c.getComparator(), left, right);
		Constraint notC2 = formNotConstraint2(c.getComparator(), left, right);
		if (pc.hasConstraint(oppC) || pc.hasConstraint(notC1)
				|| pc.hasConstraint(notC2)) {
			return true;
		}
		return false;
	}

	public static Constraint toGeneralForm(Constraint c) {
		Expression left = c.getLeft();
		Expression right = c.getRight();
		if (right instanceof LinearIntegerExpression
				&& left instanceof LinearIntegerExpression) {
			BinaryLinearIntegerExpression l = new BinaryLinearIntegerExpression(
					(IntegerExpression) left, Operator.MINUS,
					(IntegerExpression) right);
			IntegerConstant zero = new IntegerConstant(0);
			LinearIntegerConstraint lic = new LinearIntegerConstraint(l,
					c.getComparator(), zero);
			return lic;
		}
		return null;
	}

	public static Constraint reOrder(Constraint c) {
		assert c != null;
		Expression left = c.getLeft();
		Expression right = c.getRight();
		Comparator com = getDifferentOrderComparator(c.getComparator());
		return formAConstraint(com, right, left);
	}

	public static PathCondition slicePC(PathCondition pc) {
		if (pc == null) {
			return null;
		}
		PathCondition pcCopy = pc.make_copy();
		PathCondition result = new PathCondition();
		Constraint c = pcCopy.header;
		// result._addDet(c.getComparator(), c.getLeft(), c.getRight());
		Set<Expression> vars = new HashSet<>();
		Expression left = c.getLeft();
		Expression right = c.getRight();
		vars.addAll(getVarsFromExpression(left));
		vars.addAll(getVarsFromExpression(right));
		int size = result.count();
		assert size == 0;
		int temp = size;
		while (true) {
			addAllVar(vars, result, pcCopy);
			size = result.count();
			if (size == temp) {
				break;
			}
			temp = size;
		}
		result._addDet(c.getComparator(), c.getLeft(), c.getRight());

		// c = c.getTail();
		// while (c != null) {
		// left = c.getLeft();
		// right = c.getRight();
		// Set<Expression> tempVars = new HashSet<>();
		// for (Expression var : vars) {
		// if (expContainsVar(left, var) || expContainsVar(right, var)) {
		// result._addDet(c.getComparator(), left, right);
		// tempVars.addAll(getVarsFromExpression(left));
		// tempVars.addAll(getVarsFromExpression(right));
		// }
		// }
		// vars.addAll(tempVars);
		// c = c.getTail();
		// }
		return result;
	}

	public static void addAllVar(Set<Expression> vars, PathCondition result,
			PathCondition pc) {
		Constraint c = pc.header;
		c = c.getTail();
		Expression left = c.getLeft();
		Expression right = c.getRight();
		while (c != null) {
			left = c.getLeft();
			right = c.getRight();
			Set<Expression> tempVars = new HashSet<>();
			for (Expression var : vars) {
				if (expContainsVar(left, var) || expContainsVar(right, var)) {
					result._addDet(c.getComparator(), left, right);
					tempVars.addAll(getVarsFromExpression(left));
					tempVars.addAll(getVarsFromExpression(right));
				}
			}
			vars.addAll(tempVars);
			c = c.getTail();
		}
	}

	public static boolean containsNotOrOpposite(PathCondition pc) {
		Constraint c = pc.header;
		Expression left = c.getLeft();
		Expression right = c.getRight();
		Constraint oppC = formOppositeConstriant(c.getComparator(), left, right);
		Constraint notC1 = formNotConstraint1(c.getComparator(), left, right);
		Constraint notC2 = formNotConstraint2(c.getComparator(), left, right);
		if (pc.hasConstraint(oppC) || pc.hasConstraint(notC1)
				|| pc.hasConstraint(notC2)) {
			return true;
		}
		return false;
	}

	public static Constraint formOppositeConstriant(Comparator c, Expression l,
			Expression r) {
		Comparator newC = getOppositeComparator(c);
		if (c != null) {
			return formAConstraint(newC, l, r);
		} else {
			return null;
		}
	}

	public static Constraint formNotConstraint2(Comparator c, Expression l,
			Expression r) {
		Comparator newC = getNotComparator2(c);
		if (c != null) {
			return formAConstraint(newC, l, r);
		} else {
			return null;
		}
	}

	public static Constraint formNotConstraint1(Comparator c, Expression l,
			Expression r) {
		Comparator newC = getNotComparator1(c);
		if (c != null) {
			return formAConstraint(newC, l, r);
		} else {
			return null;
		}
	}

	public static Constraint formAConstraint(Comparator c, Expression l,
			Expression r) {
		if (l instanceof IntegerExpression && r instanceof IntegerExpression) {
			Constraint t;
			if ((l instanceof LinearIntegerExpression)
					&& (r instanceof LinearIntegerExpression)) {
				t = new LinearIntegerConstraint((LinearIntegerExpression) l, c,
						(LinearIntegerExpression) r);
			} else {
				t = new NonLinearIntegerConstraint((IntegerExpression) l, c,
						(IntegerExpression) r);
			}
			return t;
		} else if (l instanceof RealExpression && r instanceof RealExpression) {
			Constraint t = new RealConstraint((RealExpression) l, c,
					(RealExpression) r);
			return t;
		} else {
			throw new RuntimeException(
					"## Error: _addDet (type incompatibility real/integer) "
							+ c + " " + l + " " + r);
		}
	}

	public static Comparator getOppositeComparator(Comparator c) {
		if (c == Comparator.EQ) {
			return Comparator.NE;
		} else if (c == Comparator.GE) {
			return Comparator.LT;
		} else if (c == Comparator.GT) {
			return Comparator.LE;
		} else if (c == Comparator.LE) {
			return Comparator.GT;
		} else if (c == Comparator.LT) {
			return Comparator.GE;
		} else if (c == Comparator.NE) {
			return Comparator.EQ;
		} else {
			return null;
		}
	}

	public static Comparator getNotComparator1(Comparator c) {
		if (c == Comparator.EQ) {
			return Comparator.GT;
		} else if (c == Comparator.GE) {
			return Comparator.LT;
		} else if (c == Comparator.GT) {
			return Comparator.EQ;
		} else if (c == Comparator.LE) {
			return Comparator.GT;
		} else if (c == Comparator.LT) {
			return Comparator.EQ;
		} else if (c == Comparator.NE) {
			return Comparator.EQ;
		} else {
			return null;
		}
	}

	public static Comparator getNotComparator2(Comparator c) {
		if (c == Comparator.EQ) {
			return Comparator.LT;
		} else if (c == Comparator.GE) {
			return Comparator.LT;
		} else if (c == Comparator.GT) {
			return Comparator.LT;
		} else if (c == Comparator.LE) {
			return Comparator.GT;
		} else if (c == Comparator.LT) {
			return Comparator.GT;
		} else if (c == Comparator.NE) {
			return Comparator.EQ;
		} else {
			return null;
		}
	}

	public static Comparator getDifferentOrderComparator(Comparator c) {
		if (c == Comparator.EQ) {
			return Comparator.EQ;
		} else if (c == Comparator.GE) {
			return Comparator.LE;
		} else if (c == Comparator.GT) {
			return Comparator.LT;
		} else if (c == Comparator.LE) {
			return Comparator.GE;
		} else if (c == Comparator.LT) {
			return Comparator.GT;
		} else if (c == Comparator.NE) {
			return Comparator.NE;
		} else {
			return null;
		}
	}

	public static Set<Expression> getVarsFromExpression(Expression e) {
		Set<Expression> result = new HashSet<>();
		if ((e instanceof SymbolicInteger) || (e instanceof SymbolicReal)) {
			result.add(e);
		} else if (e instanceof BinaryLinearIntegerExpression) {
			BinaryLinearIntegerExpression ble = (BinaryLinearIntegerExpression) e;
			result.addAll(getVarsFromExpression(ble.getLeft()));
			result.addAll(getVarsFromExpression(ble.getRight()));
		} else if (e instanceof BinaryNonLinearIntegerExpression) {
			BinaryNonLinearIntegerExpression bnle = (BinaryNonLinearIntegerExpression) e;
			result.addAll(getVarsFromExpression(bnle.left));
			result.addAll(getVarsFromExpression(bnle.right));
		} else if (e instanceof BinaryRealExpression) {
			BinaryRealExpression bre = (BinaryRealExpression) e;
			result.addAll(getVarsFromExpression(bre.getLeft()));
			result.addAll(getVarsFromExpression(bre.getRight()));
		} else if ((e instanceof IntegerConstant)) {
		} else if (e instanceof RealConstant) {
		} else {
			throw new RuntimeException("Unsupported expression type: "
					+ e.toString());
		}
		return result;
	}

	public static boolean expContainsVar(Expression e, Expression var) {
		if ((e instanceof SymbolicInteger) || (e instanceof SymbolicReal)) {
			if (e.equals(var)) {
				return true;
			}
		} else if (e instanceof BinaryLinearIntegerExpression) {
			BinaryLinearIntegerExpression ble = (BinaryLinearIntegerExpression) e;
			return expContainsVar(ble.getLeft(), var)
					|| expContainsVar(ble.getRight(), var);
		} else if (e instanceof BinaryNonLinearIntegerExpression) {
			BinaryNonLinearIntegerExpression bnle = (BinaryNonLinearIntegerExpression) e;
			return expContainsVar(bnle.left, var)
					|| expContainsVar(bnle.right, var);
		} else if (e instanceof BinaryRealExpression) {
			BinaryRealExpression bre = (BinaryRealExpression) e;
			return expContainsVar(bre.getLeft(), var)
					|| expContainsVar(bre.getRight(), var);
		} else if ((e instanceof IntegerConstant)) {
			return false;
		} else if (e instanceof RealConstant) {
			return false;
		} else {
			throw new RuntimeException("Unsupported expression type: "
					+ e.toString());
		}
		return false;
	}

}
