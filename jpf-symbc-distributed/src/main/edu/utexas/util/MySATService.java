package edu.utexas.util;

import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.Instance;
import za.ac.sun.cs.green.service.SATService;

public class MySATService extends SATService {

	public static boolean isSat = false;
	
	public MySATService(Green solver) {
		super(solver);
	}

	@Override
	protected Boolean solve(Instance instance) {
		return isSat;
	}

}
