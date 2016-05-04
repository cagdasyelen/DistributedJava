package ipASW;

import java.lang.reflect.InvocationTargetException;

public class ASWMainClass {

	/**
	 * @param args
	 */
	public static void main_original(String[] args) {
		asw myAsw = new asw();
		myAsw.Init3();

		/*
		 * int counter = 0; while (counter < 2) { double Altitude_Input_2 =
		 * Debug.getSymbolicReal(0, 30000, "Altitude_Input_2"); boolean
		 * AltitudeOK_Input_3 = Debug.getSymbolicBoolean("AltitudeOK_Input_3");
		 * boolean DOIOn_Input_4 = Debug.getSymbolicBoolean("DOIOn_Input_4");
		 * boolean Inhibit_Input_5 =
		 * Debug.getSymbolicBoolean("Inhibit_Input_5"); boolean Reset_Input_6 =
		 * Debug.getSymbolicBoolean("Reset_Input_6"); double Threshold_Input_7 =
		 * Debug.getSymbolicReal(0, 30000, "Threshhold_Input_7"); double
		 * Altitude_Input1_8 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input1_8"); boolean AltitudeOK_Input1_9 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input1_9"); double
		 * Altitude_Input2_10 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input2_10"); boolean AltitudeOK_Input2_11 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input2_11"); boolean[]
		 * AlarmOn_12 = new boolean[1]; AlarmOn_12[0] =
		 * Debug.getSymbolicBoolean("AlarmOn_12"); boolean[] DOICommand_13 = new
		 * boolean[1]; DOICommand_13[0] =
		 * Debug.getSymbolicBoolean("DOICommand_13");
		 * myAsw.Main0(Altitude_Input_2, AltitudeOK_Input_3, DOIOn_Input_4,
		 * Inhibit_Input_5, Reset_Input_6, Threshold_Input_7, Altitude_Input1_8,
		 * AltitudeOK_Input1_9, Altitude_Input2_10, AltitudeOK_Input2_11,
		 * AlarmOn_12, DOICommand_13); counter++; }
		 */

		/*
		 * double Altitude_Input_2 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input_1"); boolean AltitudeOK_Input_3 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input_1"); boolean DOIOn_Input_4
		 * = Debug.getSymbolicBoolean("DOIOn_Input_1"); boolean Inhibit_Input_5
		 * = Debug.getSymbolicBoolean("Inhibit_Input_1"); boolean Reset_Input_6
		 * = Debug.getSymbolicBoolean("Reset_Input_1"); double Threshold_Input_7
		 * = Debug.getSymbolicReal(0, 30000, "Threshhold_Input_1"); double
		 * Altitude_Input1_8 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input1_1"); boolean AltitudeOK_Input1_9 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input1_1"); double
		 * Altitude_Input2_10 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input2_1"); boolean AltitudeOK_Input2_11 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input2_1"); boolean[] AlarmOn_12
		 * = new boolean[1]; AlarmOn_12[0] =
		 * Debug.getSymbolicBoolean("AlarmOn_1"); boolean[] DOICommand_13 = new
		 * boolean[1]; DOICommand_13[0] =
		 * Debug.getSymbolicBoolean("DOICommand_1");
		 */

		double Altitude_Input_2 = 0;
		boolean AltitudeOK_Input_3 = true;
		boolean DOIOn_Input_4 = true;
		boolean Inhibit_Input_5 = true;
		boolean Reset_Input_6 = true;
		double Threshold_Input_7 = 0;
		double Altitude_Input1_8 = 0;
		boolean AltitudeOK_Input1_9 = true;
		double Altitude_Input2_10 = 0;
		boolean AltitudeOK_Input2_11 = true;
		boolean AlarmOn_12 = true;
		boolean DOICommand_13 = true;

		myAsw.Main0(Altitude_Input_2, AltitudeOK_Input_3, DOIOn_Input_4,
				Inhibit_Input_5, Reset_Input_6, Threshold_Input_7,
				Altitude_Input1_8, AltitudeOK_Input1_9, Altitude_Input2_10,
				AltitudeOK_Input2_11, AlarmOn_12, DOICommand_13);

		/*
		 * Altitude_Input_2 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input_2"); AltitudeOK_Input_3 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input_2"); DOIOn_Input_4 =
		 * Debug.getSymbolicBoolean("DOIOn_Input_2"); Inhibit_Input_5 =
		 * Debug.getSymbolicBoolean("Inhibit_Input_2"); Reset_Input_6 =
		 * Debug.getSymbolicBoolean("Reset_Input_2"); Threshold_Input_7 =
		 * Debug.getSymbolicReal(0, 30000, "Threshhold_Input_2");
		 * Altitude_Input1_8 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input1_2"); AltitudeOK_Input1_9 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input1_2"); Altitude_Input2_10 =
		 * Debug.getSymbolicReal(0, 30000, "Altitude_Input2_2");
		 * AltitudeOK_Input2_11 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input2_2"); AlarmOn_12 = new
		 * boolean[1]; AlarmOn_12[0] = Debug.getSymbolicBoolean("AlarmOn_2");
		 * DOICommand_13 = new boolean[1]; DOICommand_13[0] =
		 * Debug.getSymbolicBoolean("DOICommand_2");
		 */

		DOICommand_13 = true;

		myAsw.Main0(Altitude_Input_2, AltitudeOK_Input_3, DOIOn_Input_4,
				Inhibit_Input_5, Reset_Input_6, Threshold_Input_7,
				Altitude_Input1_8, AltitudeOK_Input1_9, Altitude_Input2_10,
				AltitudeOK_Input2_11, AlarmOn_12, DOICommand_13);

		/*
		 * Altitude_Input_2 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input_3"); AltitudeOK_Input_3 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input_3"); DOIOn_Input_4 =
		 * Debug.getSymbolicBoolean("DOIOn_Input_3"); Inhibit_Input_5 =
		 * Debug.getSymbolicBoolean("Inhibit_Input_3"); Reset_Input_6 =
		 * Debug.getSymbolicBoolean("Reset_Input_3"); Threshold_Input_7 =
		 * Debug.getSymbolicReal(0, 30000, "Threshhold_Input_3");
		 * Altitude_Input1_8 = Debug.getSymbolicReal(0, 30000,
		 * "Altitude_Input1_3"); AltitudeOK_Input1_9 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input1_3"); Altitude_Input2_10 =
		 * Debug.getSymbolicReal(0, 30000, "Altitude_Input2_3");
		 * AltitudeOK_Input2_11 =
		 * Debug.getSymbolicBoolean("AltitudeOK_Input2_3"); AlarmOn_12 = new
		 * boolean[1]; AlarmOn_12[0] = Debug.getSymbolicBoolean("AlarmOn_3");
		 * DOICommand_13 = new boolean[1]; DOICommand_13[0] =
		 * Debug.getSymbolicBoolean("DOICommand_3");
		 * myAsw.Main0(Altitude_Input_2, AltitudeOK_Input_3, DOIOn_Input_4,
		 * Inhibit_Input_5, Reset_Input_6, Threshold_Input_7, Altitude_Input1_8,
		 * AltitudeOK_Input1_9, Altitude_Input2_10, AltitudeOK_Input2_11,
		 * AlarmOn_12, DOICommand_13);
		 */
	}

	public void mainBody(double Altitude_Input_2, boolean AltitudeOK_Input_3,
			boolean DOIOn_Input_4, boolean Inhibit_Input_5,
			boolean Reset_Input_6, double Threshold_Input_7,
			double Altitude_Input1_8, boolean AltitudeOK_Input1_9,
			double Altitude_Input2_10, boolean AltitudeOK_Input2_11,
			boolean AlarmOn_12, boolean DOICommand_13) {
		asw myAsw = new asw();
		myAsw.Init3();
		myAsw.Main0(Altitude_Input_2, AltitudeOK_Input_3, DOIOn_Input_4,
				Inhibit_Input_5, Reset_Input_6, Threshold_Input_7,
				Altitude_Input1_8, AltitudeOK_Input1_9, Altitude_Input2_10,
				AltitudeOK_Input2_11, AlarmOn_12, DOICommand_13);
//		DOICommand_13 = true;
		asw myAsw2 = new asw();
		myAsw2.Init3();
		myAsw2.Main0(Altitude_Input_2, AltitudeOK_Input_3, DOIOn_Input_4,
				Inhibit_Input_5, Reset_Input_6, Threshold_Input_7,
				Altitude_Input1_8, AltitudeOK_Input1_9, Altitude_Input2_10,
				AltitudeOK_Input2_11, AlarmOn_12, DOICommand_13);
	}

	public static void main(String[] args) {
		ASWMainClass o = new ASWMainClass();
//		try {
			if (args.length == 1) {
//				RunUtil.runMethod(o, args[0]);
				if(args[0].equals("mainBody")) {
					o.mainBody(0, false, false, false, false, 0, 0, false, 0, false, false, false);
				}
			}
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		}
	}

}
