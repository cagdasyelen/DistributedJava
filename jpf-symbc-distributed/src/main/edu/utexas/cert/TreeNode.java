package edu.utexas.cert;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.jpf.symbc.numeric.PathCondition;

public class TreeNode {

	private PathCondition pc;
	private List<TreeNode> children;
	private TreeNode parent;
	private Object testCase = null;
	private boolean processed = false;

	public TreeNode(TreeNode parent, PathCondition pc) {
		this.setParent(parent);
		this.setPc(pc);
		setChildren(new ArrayList<TreeNode>());
	}

	public PathCondition getPc() {
		return pc;
	}

	public void setPc(PathCondition pc) {
		this.pc = pc;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Object getTestCase() {
		return testCase;
	}

	public void setTestCase(Object testCase) {
		this.testCase = testCase;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		if (this.pc == null) {
			return "Root Tree Node";
		}
		return this.pc.stringPC();
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed() {
		processed = true;
	}

	public String nodeString(String color, String style, String label) {
		return hashCode() + "[ color=\"" + color + "\" style=\"" + style
				+ "\" label=\"" + label + "\"];\n";
	}

}
