package edu.utexas.cert;

import gov.nasa.jpf.symbc.numeric.UnsatPC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Tree {

	private TreeNode root;
	private TreeNode cur;

	public Tree(TreeNode root) {
		this.setRoot(root);
		this.setCur(root);
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getCur() {
		return cur;
	}

	public void setCur(TreeNode cur) {
		this.cur = cur;
	}

	public void addChildren(List<TreeNode> nodes) {
		assert cur != null;
		cur.setChildren(nodes);
		for (TreeNode n : nodes) {
			if (!(n.getPc() instanceof UnsatPC)) {
				cur = n;
				return;
			}
		}
		assert false;
	}

	public void next() {
		assert cur != null;
		if (cur == root) {
			return;
		}
		List<TreeNode> sameLevel = getUnprocessedSameLevelNodes();
		if (sameLevel.isEmpty()) {
			cur = cur.getParent();
			cur.setProcessed();
			next();
		} else {
			cur = sameLevel.get(0);
			return;
		}
	}

	public void getSameLevel() {
		assert cur != null;
		if (cur == root) {
			return;
		}
		cur = cur.getParent().getChildren().get(0);
	}

	public List<Object> getCert(TreeNode startNode) {
		assert startNode != null;
		List<Object> result = new ArrayList<>();
		for (TreeNode n : startNode.getChildren()) {
			if (n.getPc() instanceof UnsatPC) {
				result.add(false);
			} else if (n.getTestCase() == null) {
				result.addAll(getCert(n));
			} else {
				assert n.getTestCase() != null;
				result.add(n.getTestCase());
				assert n.getChildren().isEmpty();
			}
		}
		return result;
	}

	public List<Object> getCert() {
		assert root != null;
		return getCert(root);
	}

	public List<TreeNode> getUnprocessedSameLevelNodes() {
		assert cur != root;
		List<TreeNode> result = new ArrayList<>();
		for (TreeNode n : cur.getParent().getChildren()) {
			if (!n.isProcessed()) {
				result.add(n);
			}
		}
		return result;
	}

	public void printTreeNodeToDot(String fileName) {
		Writer output = null;
		File file = new File(fileName);
		try {
			output = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			System.err.printf("Error while creating the file to write: ", e);
		}
		try {
			output.write("digraph \"\" { \n");
			if (null != root) {
				printNodes(root, output);
				printEdges(root, output);
			}
			output.write("}");
			output.close();
		} catch (IOException e) {
			System.err.printf("Error while writing to the dot file", e);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.printf("Exception in reading nodes in the trie", e);
			e.printStackTrace();
		}
	}

	private void printNodes(TreeNode node, Writer output) throws IOException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		assert node != null;
		assert node.isProcessed();
		if (node.getTestCase() == null) {
			String label = "";
			if (node.getPc() instanceof UnsatPC) {
				label = "Unsat PC";
			} else {
				label = node.toString();
			}
			output.write(node.nodeString("grey", "filled", label));
		} else {
			String label = node.toString() + "\n" + node.getTestCase();
			output.write(node.nodeString("lightblue", "filled", label));
		}
		for (TreeNode n : node.getChildren()) {
			printNodes(n, output);
		}
	}

	private void printEdges(TreeNode node, Writer output) throws IOException {
		for (TreeNode child : node.getChildren()) {
			output.write(node.hashCode() + "->" + child.hashCode()
					+ "[ color=\"grey\"];\n");
			printEdges(child, output);
		}
	}

}
