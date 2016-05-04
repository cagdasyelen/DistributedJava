package rjc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MyDriver {

	public static String SM = "symbolic.method";
	public static String TA = "target.args";
	public static String DIR = System.getProperty("user.dir");
	public static String sep = System.getProperty("file.separator");
	public static String CLASSPATH = DIR + sep + "build" + sep + "examples";
	public static String SOURCEPATH = DIR + sep + "src" + sep + "examples";

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Wrong number of arguments. Expecting 2 arguments for class name and method name.");
			System.exit(1);
		}
		assert args.length == 2;
		String className = args[0];
		String methodName = args[1];
		Object o = null;
		o = createObject(className);
		assert o != null;
		System.out.println("Object " + o.getClass().getName() + " created!");
		System.out.println("Target method name: " + methodName);
		try {
			runMethod(o, methodName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

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
				break;
			}
		}
	}

	public static Object createObject(String className) {
		Object object = null;
		try {
			Class<?> classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		return object;
	}

	public static Config jpfConfigSetup(String className, String methodName,
			int depth) {
		Object o = createObject(className);
		if (o == null) {
			System.err.println("Cannot create the target object.");
			return null;
		}
		assert o != null;
		String[] args = {};
		Config conf = JPF.createConfig(args);
		conf.setProperty("symbolic.dp", "cvc3");
		conf.setProperty("symbolic.lazy", "true");
		System.out.println("Set target to " + MyDriver.class.getName());
		conf.setProperty("target", MyDriver.class.getName());
		System.out.println("Set classpath to " + CLASSPATH);
		conf.setProperty("classpath", CLASSPATH);
		System.out.println("Set sourcepath to " + SOURCEPATH);
		conf.setProperty("sourcepath", SOURCEPATH);
		Class<?> c = o.getClass();
		Method[] allMethods = c.getDeclaredMethods();
		String longName = c.getName() + ".";
		for (Method m : allMethods) {
			String mName = m.getName();
			if (mName.equals(methodName)) {
				Type[] pType = m.getGenericParameterTypes();
				StringBuilder s = new StringBuilder();
				s.append(mName + "(");
				for (int i = 0; i < pType.length; i++) {
					if (i == 0) {
						s.append("sym");
					} else {
						s.append("#sym");
					}
				}
				s.append(")");
				System.out.println("Set " + SM + " to " + longName
						+ s.toString());
				conf.setProperty(SM, longName + s.toString());
				System.out.println("Set " + TA + " to "
						+ o.getClass().getName() + ", " + mName);
				conf.setProperty(TA, o.getClass().getName() + "," + mName);
				break;
			}
		}
		if (depth != -1) {
			conf.setProperty("search.depth_limit", Integer.toString(depth));
		}
		return conf;
	}

	public static void runJPF(Config conf, ListenerAdapter... listeners) {
		JPF jpf = new JPF(conf);
		for (ListenerAdapter listener : listeners) {
			jpf.addListener(listener);
		}
		jpf.run();
	}

}
