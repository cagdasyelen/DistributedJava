package edu.utexas.util;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunUtil {

	public static String SM = "symbolic.method";
	public static String TA = "target.args";
	public static String DIR = System.getProperty("user.dir");
	public static String sep = System.getProperty("file.separator");
	public static String TEST_CLASS_PATH = DIR + sep + "build" + sep + "tests";
	public static String TEST_SOURCE_PATH = DIR + sep + "src" + sep + "tests";
	public static String EXAMPLE_CLASS_PATH = DIR + sep + "build" + sep
			+ "examples";


	public static void runMethod(Object o, String methodName)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException {
		Class<?> c = o.getClass();
		Method[] allMethods = c.getDeclaredMethods();
		for (Method m : allMethods) {
			String mName = m.getName();
			// can not handle overloading yet
			if (mName.equals(methodName)) {
				Class<?>[] pType = m.getParameterTypes();
				Object[] paras = new Object[pType.length];
				int i = 0;
				for (Class<?> p : pType) {
					if (!p.isPrimitive()) {
						if (p.getName().equals("[D")) {
							paras[i] = new double[] { 1.0 };
						} else if (p.getName().equals("[B")) {
							paras[i] = new byte[] { 1 };
						} else if (p.getName().equals("[I")) {
							paras[i] = new int[] { 1 };
						} else {
							paras[i] = p.newInstance();
						}
					} else {
						if (p.getName().equals("int")) {
							paras[i] = 0;
						} else if (p.getName().equals("boolean")) {
							paras[i] = true;
						} else if (p.getName().equals("double")) {
							paras[i] = 1.0;
						}
					}
					i++;
				}
				m.invoke(o, paras);
			}
		}
	}

	public static void runJPF(Config conf, ListenerAdapter... listeners) {
		JPF jpf = new JPF(conf);
		for (ListenerAdapter listener : listeners) {
			jpf.addListener(listener);
		}
		jpf.run();
	}

}
