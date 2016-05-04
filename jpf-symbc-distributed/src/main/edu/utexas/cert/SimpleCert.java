package edu.utexas.cert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleCert implements Serializable {

	private static final long serialVersionUID = 4865737532961267531L;
	private String name;
	private List<String> symValues;
	private List<Object> argValues;
	private List<Byte> argTypes;
	private List<Object> pathsGropus;
	private int maxDepth;
	
	// special storage for traditional ranged analysis
	private List<Object> traditionalRanges;

	public SimpleCert() {
		symValues = new ArrayList<>();
		argValues = new ArrayList<>();
		argTypes = new ArrayList<>();
		pathsGropus = new ArrayList<>();
	}

	public SimpleCert(String name) {
		this();
		this.name = name;
	}

	public void addOnePath(Object p) {
		assert p != null;
		pathsGropus.add(p);
	}

	public boolean containsPath(Object p) {
		return pathsGropus.contains(p);
	}

	public void addArgValue(Object value) {
		this.argValues.add(value);
	}

	public void addArgType(Byte type) {
		this.argTypes.add(type);
	}

	public void addSymValue(String value) {
		this.symValues.add(value);
	}

	public boolean repOk() {
		return argTypes.size() == argValues.size()
				&& argValues.size() == symValues.size();
	}

	public List<Object> getPathsGroups() {
		return pathsGropus;
	}

	public void setPaths(List<Object> paths) {
		this.pathsGropus = paths;
	}

	public String getName() {
		return name;
	}

	public List<String> getSymValues() {
		return symValues;
	}

	public List<Object> getArgValues() {
		return argValues;
	}

	public List<Byte> getArgTypes() {
		return argTypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSymValues(List<String> symValues) {
		this.symValues = symValues;
	}

	public void setArgValues(List<Object> argValues) {
		this.argValues = argValues;
	}

	public void setArgTypes(List<Byte> argTypes) {
		this.argTypes = argTypes;
	}

	public void writeToFile(String file) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(this);
		out.close();
		fileOut.close();
	}

	public static SimpleCert readFromFile(String file) throws IOException,
			ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		SimpleCert sc = (SimpleCert) in.readObject();
		in.close();
		fileIn.close();
		return sc;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public List<Object> getTraditionalRanges() {
		return traditionalRanges;
	}

	public void setTraditionalRanges(List<Object> traditionalRanges) {
		this.traditionalRanges = traditionalRanges;
	}

}
