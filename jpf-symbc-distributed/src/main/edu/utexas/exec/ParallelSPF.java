package edu.utexas.exec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import edu.utexas.cert.SimpleCert;
import edu.utexas.listeners.AdvancedBuilderListener;
import edu.utexas.listeners.AdvancedRangedListener;
import edu.utexas.listeners.AdvancedSingleRangedListener;
import edu.utexas.listeners.NaiveRangedListener;
import edu.utexas.listeners.VerifyListener;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.symbc.numeric.PathCondition;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

public class ParallelSPF {

	static String SOURCE_DIR = System.getProperty("user.dir");

	public static void main(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption("M", "master", false,
				"This is a master node running.");
		options.addOption("r", "range", true, "The range for this worker.");
		options.addOption("d", "depth", true, "The initial depth.");
		options.addOption("D", "Depth", true, "The final depth.");
		options.addOption("c", "class", true, "Target class full name.");
		options.addOption("m", "method", true, "Method full name.");
		options.addOption("s", "solver", true, "Solver name.");
		options.addOption("C", "cert", true, "Cert file.");
		options.addOption("a", "target-args", true, "Target program args.");
		options.addOption("T", "traditional", false,
				"Traditional non-distributed SPF.");
		options.addOption("N", "naive", false,
				"Generate test cases for naive ranged analysis.");
		options.addOption("n", "range-number", true,
				"The number of ranges for original ranged analysis.");

		String[] empty = new String[] { "" };
		Config conf = JPF.createConfig(empty);

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			// set common configs
			settings(conf, line);
			if (line.hasOption("master")) {
				if (line.hasOption("naive")) {
					// naive ranged analysis
					System.out
							.println("Run original ranged analysis master node...\n");
					runOriginalRangedMasterAnalysis(conf, line);
				} else {
					// master node work here
					System.out
							.println("Master node doing an initial symbolic execution...\n");
					runMasterNode(conf, line);
				}
			} else if (line.hasOption("traditional")) {
				System.out.println("Run traditional non-distributed SPF...\n");
				runTraditionalSPF(conf, line);
			} else {
				if (line.hasOption("naive")) {
					runOriginalRangedSlaveAnalysis(conf, line);
				} else {
					// slave node work here
					runSlaveNode(conf, line);
				}
			}

		} catch (ParseException exp) {
			System.out.println("Missing or wrong args:" + exp.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static private void runOriginalRangedMasterAnalysis(Config conf,
			CommandLine line) throws IOException {
		int rangeSize = Integer.parseInt(line.getOptionValue("range-number"));
		PathCondition.totalNum = 0;
		JPF jpf = new JPF(conf);
		Logger log = Logger.getLogger(ParallelSPF.class);
		String depth = line.getOptionValue("Depth");
		String certFile = line.getOptionValue("cert");
		AdvancedBuilderListener abl = new AdvancedBuilderListener(log,
				Integer.parseInt(depth));
		jpf.addListener(abl);
		jpf.run();
		// generate random alternate ranges
		List<Object> alternateRanges = abl.generateAlternateRanges(rangeSize);
		FileOutputStream fout = new FileOutputStream(certFile, false);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(abl.getCert());
		oos.close();
		System.out.println("Number of ranges generated: "
				+ alternateRanges.size());
		File rangeSizeFile = new File(getRootDir(), "rangeSize.txt");
		PrintWriter writer = new PrintWriter(rangeSizeFile, "UTF-8");
		writer.println(alternateRanges.size());
		writer.close();
		String time = Publisher.formatHMS(jpf.getReporter().getElapsedTime());
		// File resultFile = createNaiveResultFile(line.getOptionValue("class"),
		// true, -1);
		// writer = new PrintWriter(resultFile, "UTF-8");
		// writer.println("Master_Node");
		// writer.println(time);
		// writer.println(numberOfSolverCalls);
		// writer.close();
		printOutToConsole("Original Ranged DistributedSPF", -1,
				PathCondition.totalNum, time);
		System.out.println("Master node work done!");
	}

	@SuppressWarnings("unchecked")
	static private void runOriginalRangedSlaveAnalysis(Config conf,
			CommandLine line) throws Exception {
		PathCondition.totalNum = 0;
		JPF jpf = new JPF(conf);
		Logger log = Logger.getLogger(ParallelSPF.class);
		String newDepth = line.getOptionValue("Depth");
		String certFile = line.getOptionValue("cert");
		int rangeIndex = Integer.parseInt(line.getOptionValue("range"));
		FileInputStream streamIn = new FileInputStream(certFile);
		ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
		SimpleCert cert = (SimpleCert) objectinputstream.readObject();
		objectinputstream.close();
		List<Object> ranges = cert.getTraditionalRanges();
		if (rangeIndex >= ranges.size()) {
			throw new Exception("The range is out of bound!");
		}
		System.out.println("------------");
		System.out.println("Symbolic execution on range No. " + rangeIndex);
		System.out.println("------------");
		List<Object> therange = (List<Object>) ranges.get(rangeIndex);
		assert therange instanceof List<?>;
		NaiveRangedListener srl = new NaiveRangedListener(log, cert, therange,
				Integer.parseInt(newDepth));
		jpf.addListener(srl);
		jpf.run();
		String time = Publisher.formatHMS(jpf.getReporter().getElapsedTime());
		// File resultFile = createNaiveResultFile(line.getOptionValue("class"),
		// false, rangeIndex);
		// PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
		// writer.println(rangeIndex);
		// writer.println(time);
		// writer.println(numberOfSolverCalls);
		// writer.close();
		printOutToConsole("Original Ranged DistributedSPF", rangeIndex,
				PathCondition.totalNum, time);
	}

	static private void runTraditionalSPF(Config conf, CommandLine line)
			throws IOException {
		PathCondition.totalNum = 0;
		JPF jpf = new JPF(conf);
		int finalDepth = Integer.parseInt(line.getOptionValue("Depth"));
		VerifyListener vl = new VerifyListener(finalDepth);
		jpf.addListener(vl);
		jpf.run();
		String time = Publisher.formatHMS(jpf.getReporter().getElapsedTime());
		// File resultFile = createResultFile(line.getOptionValue("class"),
		// true,
		// 0);
		// PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
		// writer.println("Traditional SPF");
		// writer.println(time);
		// writer.println(numberOfSolverCalls);
		// writer.close();
		printOutToConsole("Traditional SPF", -2, PathCondition.totalNum, time);
	}

	static private void settings(Config conf, CommandLine line) {
		conf.setProperty("classpath", SOURCE_DIR);
		conf.setProperty("target", line.getOptionValue("class"));
		conf.setProperty("symbolic.method", line.getOptionValue("method"));
		if (line.hasOption("target-args")) {
			conf.setProperty("target.args", line.getOptionValue("target-args"));
		}
		if (line.hasOption("solver")) {
			conf.setProperty("symbolic.dp", line.getOptionValue("solver"));
		} else {
			conf.setProperty("symbolic.dp", "z3");
		}
	}

	@SuppressWarnings("unchecked")
	static private void runSlaveNode(Config conf, CommandLine line)
			throws Exception {
		PathCondition.totalNum = 0;
		JPF jpf = new JPF(conf);
		Logger log = Logger.getLogger(ParallelSPF.class);
		String oldDepth = line.getOptionValue("depth");
		String newDepth = line.getOptionValue("Depth");
		String certFile = line.getOptionValue("cert");
		int rangeIndex = Integer.parseInt(line.getOptionValue("range"));
		FileInputStream streamIn = new FileInputStream(certFile);
		ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
		SimpleCert cert = (SimpleCert) objectinputstream.readObject();
		objectinputstream.close();
		if (rangeIndex >= cert.getPathsGroups().size()) {
			throw new Exception("The range is out of bound!");
		}
		System.out.println("------------");
		System.out.println("Symbolic execution on range No. " + rangeIndex);
		System.out.println("------------");
		Object therange = cert.getPathsGroups().get(rangeIndex);
		assert therange instanceof List<?>;
		List<Object> ranges = (List<Object>) therange;
		if (ranges.size() == 2) {
			AdvancedRangedListener arl = new AdvancedRangedListener(log, cert,
					ranges, Integer.parseInt(oldDepth),
					Integer.parseInt(newDepth));
			jpf.addListener(arl);
			jpf.run();
		} else {
			assert ranges.size() == 1;
			AdvancedSingleRangedListener asrl = new AdvancedSingleRangedListener(
					log, cert, ranges.get(0), Integer.parseInt(oldDepth),
					Integer.parseInt(newDepth));
			jpf.addListener(asrl);
			jpf.run();
		}
		String time = Publisher.formatHMS(jpf.getReporter().getElapsedTime());
		// File resultFile = createResultFile(line.getOptionValue("class"),
		// false,
		// rangeIndex);
		// PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
		// writer.println(rangeIndex);
		// writer.println(time);
		// writer.println(numberOfSolverCalls);
		// writer.close();
		printOutToConsole("New Ranged DistributedSPF", rangeIndex,
				PathCondition.totalNum, time);
	}

	static private void runMasterNode(Config conf, CommandLine line)
			throws IOException {
		PathCondition.totalNum = 0;
		JPF jpf = new JPF(conf);
		Logger log = Logger.getLogger(ParallelSPF.class);
		String depth = line.getOptionValue("Depth");
		String certFile = line.getOptionValue("cert");
		AdvancedBuilderListener abl = new AdvancedBuilderListener(log,
				Integer.parseInt(depth));
		jpf.addListener(abl);
		jpf.run();
		FileOutputStream fout = new FileOutputStream(certFile, false);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(abl.getCert());
		oos.close();
		System.out.println("Number of ranges generated: "
				+ abl.getCert().getPathsGroups().size());
		File rangeSizeFile = new File(getRootDir(), "rangeSize.txt");
		PrintWriter writer = new PrintWriter(rangeSizeFile, "UTF-8");
		writer.println(abl.getCert().getPathsGroups().size());
		writer.close();
		String time = Publisher.formatHMS(jpf.getReporter().getElapsedTime());
		// File resultFile = createResultFile(line.getOptionValue("class"),
		// true,
		// -1);
		// writer = new PrintWriter(resultFile, "UTF-8");
		// writer.println("Master_Node");
		// writer.println(time);
		// writer.println(numberOfSolverCalls);
		// writer.close();
		printOutToConsole("New Ranged DistributedSPF", -1,
				PathCondition.totalNum, time);
		System.out.println("Master node work done!");
	}

	static private String getRootDir() throws IOException {
		File currentFile = new File(SOURCE_DIR);
		if (currentFile.isDirectory()) {
			while (currentFile != null
					&& !currentFile.getName()
							.equalsIgnoreCase("distributedjava")) {
				currentFile = currentFile.getParentFile();
			}
			return currentFile.getAbsolutePath();
		} else {
			throw new IOException(
					"Cannot find root dir for jpf-symbc-distributed!");
		}
	}

	// static private File getResultDir() throws IOException {
	// String rootDir = getRootDir();
	// File resultDir = new File(rootDir, "results");
	// if (!resultDir.exists()) {
	// resultDir.mkdir();
	// }
	// return resultDir;
	// }

	// static private File createResultFile(String targetName, boolean isMaster,
	// int nodeIndex) throws IOException {
	// File resultDir = getResultDir();
	// String fileName = null;
	// if (isMaster) {
	// if (nodeIndex == -1) {
	// fileName = targetName + "_Master.txt";
	// } else {
	// fileName = targetName + "_Traditional.txt";
	// }
	// } else {
	// fileName = targetName + "_" + nodeIndex + ".txt";
	// }
	// File result = new File(resultDir, fileName);
	// if (result.exists()) {
	// result.delete();
	// }
	// result.createNewFile();
	// return result;
	// }
	//
	// static private File createNaiveResultFile(String targetName,
	// boolean isMaster, int nodeIndex) throws IOException {
	// File resultDir = getResultDir();
	// String fileName = null;
	// if (isMaster) {
	// if (nodeIndex == -1) {
	// fileName = targetName + "_Orig_Master.txt";
	// } else {
	// fileName = targetName + "_Traditional.txt";
	// }
	// } else {
	// fileName = targetName + "_Orig_" + nodeIndex + ".txt";
	// }
	// File result = new File(resultDir, fileName);
	// if (result.exists()) {
	// result.delete();
	// }
	// result.createNewFile();
	// return result;
	// }

	/**
	 * Print out results to console
	 * 
	 * @param nodeNumber
	 *            -1 for master node -2 for traditional SPF
	 */
	static private void printOutToConsole(String note, int nodeNumber,
			int csNo, String time) {
		String prefix = note + " Output Node " + nodeNumber + " : ";
		if (nodeNumber == -2) {
			prefix = note + " Output : ";
		}
		System.out.println(prefix + "CS Calls " + csNo);
		System.out.println(prefix + "Time " + time);
	}

}
