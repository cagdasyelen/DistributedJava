package edu.utexas.cert;

public class UnusedVar {

	private String varName;

	public UnusedVar(String varName) {
		this.varName = varName;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	@Override
	public String toString() {
		return getVarName();
	}

}
