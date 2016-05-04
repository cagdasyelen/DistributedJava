package tcas;

import java.lang.reflect.InvocationTargetException;

import gov.nasa.jpf.symbc.Symbolic;

public class TCAS_modified {

	private static final int OLEV = 600;/* in feets/minute */
	private static final int MAXALTDIFF = 600; /*
												 * max altitude difference in
												 * feet
												 */
	private static final int MINSEP = 300; /* min separation in feet */
	private static final int NOZCROSS = 100; /* in feet */

//	@Symbolic("true")
	int Cur_Vertical_Sep;
//	@Symbolic("true")
	boolean High_Confidence;
//	@Symbolic("true")
	boolean Two_of_Three_Reports_Valid;

//	@Symbolic("true")
	int Own_Tracked_Alt;
//	@Symbolic("true")
	int Own_Tracked_Alt_Rate;
//	@Symbolic("true")
	int Other_Tracked_Alt;

//	@Symbolic("true")
	int Alt_Layer_Value; /* 0, 1, 2, 3 */
	int[] Positive_RA_Alt_Thresh = new int[4];

//	@Symbolic("true")
	int Up_Separation;
//	@Symbolic("true")
	int Down_Separation;

	/* state variables */
//	@Symbolic("true")
	int Other_RAC; /* NO_INTENT, DO_NOT_CLIMB, DO_NOT_DESCEND */
	private static final int NO_INTENT = 0;
	private static final int DO_NOT_CLIMB = 1;
	private static final int DO_NOT_DESCEND = 2;

//	@Symbolic("true")
	int Other_Capability; /* TCAS_TA, OTHER */
	private static final int TCAS_TA = 1;
	private static final int OTHER = 2;

//	@Symbolic("true")
	boolean Climb_Inhibit; /* true/false */

	private static final int UNRESOLVED = 0;
	private static final int UPWARD_RA = 1;
	private static final int DOWNWARD_RA = 2;

	void initialize() {
		Positive_RA_Alt_Thresh[0] = 400;
		Positive_RA_Alt_Thresh[1] = 500;
		Positive_RA_Alt_Thresh[2] = 640;
		Positive_RA_Alt_Thresh[3] = 740;
	}

	public int ALIM() {
		return Positive_RA_Alt_Thresh[Alt_Layer_Value];
	}

	public int Inhibit_Biased_Climb() {
		return (Climb_Inhibit ? Up_Separation + NOZCROSS : Up_Separation);
	}

	public boolean Non_Crossing_Biased_Climb() {
		boolean upward_preferred;
		int upward_crossing_situation;
		boolean result;

		upward_preferred = Inhibit_Biased_Climb() > Down_Separation;
		if (upward_preferred) {
			result = !(Own_Below_Threat())
					|| ((Own_Below_Threat()) && (!(Down_Separation >= ALIM())));
		} else {
			result = Own_Above_Threat() && (Cur_Vertical_Sep >= MINSEP)
					&& (Up_Separation >= ALIM());
		}
		return result;
	}

	public boolean Non_Crossing_Biased_Descend() {
		boolean upward_preferred;
		int upward_crossing_situation;
		boolean result;

		upward_preferred = Inhibit_Biased_Climb() > Down_Separation;
		if (upward_preferred) {
			result = Own_Below_Threat() && (Cur_Vertical_Sep >= MINSEP)
					&& (Down_Separation >= ALIM());
		} else {
			result = !(Own_Above_Threat())
					|| ((Own_Above_Threat()) && (Up_Separation >= ALIM()));
		}
		return result;
	}

	public boolean Own_Below_Threat() {
		return (Own_Tracked_Alt < Other_Tracked_Alt);
	}

	public boolean Own_Above_Threat() {
		return (Other_Tracked_Alt < Own_Tracked_Alt);
	}

	public int alt_sep_test() {
		boolean enabled, tcas_equipped, intent_not_known;
		boolean need_upward_RA, need_downward_RA;
		int alt_sep;

		enabled = High_Confidence && (Own_Tracked_Alt_Rate <= OLEV)
				&& (Cur_Vertical_Sep > MAXALTDIFF);
		tcas_equipped = Other_Capability == TCAS_TA;
		intent_not_known = Two_of_Three_Reports_Valid && Other_RAC == NO_INTENT;

		alt_sep = UNRESOLVED;

		if (enabled && ((tcas_equipped && intent_not_known) || !tcas_equipped)) {
			need_upward_RA = Non_Crossing_Biased_Climb() && Own_Below_Threat();
			need_downward_RA = Non_Crossing_Biased_Descend()
					&& Own_Above_Threat();
			if (need_upward_RA && need_downward_RA)
				/*
				 * unreachable: requires Own_Below_Threat and Own_Above_Threat
				 * to both be true - that requires Own_Tracked_Alt <
				 * Other_Tracked_Alt and Other_Tracked_Alt < Own_Tracked_Alt,
				 * which isn't possible
				 */
				alt_sep = UNRESOLVED;
			else if (need_upward_RA)
				alt_sep = UPWARD_RA;
			else if (need_downward_RA)
				alt_sep = DOWNWARD_RA;
			else
				alt_sep = UNRESOLVED;
		}

//		assert NO_INTENT < Other_Capability;

		return alt_sep;
	}

	// alternate entry point for test purposes
	public int startTcas(int cvs, boolean hc, boolean ttrv, int ota, int otar,
			int otTa, int alv, int upS, int dS, int oRAC, int oc, boolean ci) {
		Cur_Vertical_Sep = cvs;
		High_Confidence = hc;
		Two_of_Three_Reports_Valid = ttrv;
		Own_Tracked_Alt = ota;
		Own_Tracked_Alt_Rate = otar;
		Other_Tracked_Alt = otTa;
		Alt_Layer_Value = alv;
		Up_Separation = upS;
		Down_Separation = dS;
		Other_RAC = oRAC;
		Other_Capability = oc;
		Climb_Inhibit = ci;
		initialize();
		alt_sep_test();
		Cur_Vertical_Sep = cvs;
		High_Confidence = hc;
		Two_of_Three_Reports_Valid = ttrv;
		Own_Tracked_Alt = ota;
		Own_Tracked_Alt_Rate = otar;
		Other_Tracked_Alt = otTa;
		Alt_Layer_Value = alv;
		Up_Separation = upS;
		Down_Separation = dS;
		Other_RAC = oRAC;
		Other_Capability = oc;
		Climb_Inhibit = ci;
		initialize();
		return alt_sep_test();
	}

	public void tcasTwice(int cvs1, boolean hc1, boolean ttrv1, int ota1, int otar1,
			int otTa1, int alv1, int upS1, int dS1, int oRAC1, int oc1, boolean ci1,
			int cvs2, boolean hc2, boolean ttrv2, int ota2, int otar2,
			int otTa2, int alv2, int upS2, int dS2, int oRAC2, int oc2, boolean ci2) {
		Cur_Vertical_Sep = cvs1;
		High_Confidence = hc1;
		Two_of_Three_Reports_Valid = ttrv1;
		Own_Tracked_Alt = ota1;
		Own_Tracked_Alt_Rate = otar1;
		Other_Tracked_Alt = otTa1;
		Alt_Layer_Value = alv1;
		Up_Separation = upS1;
		Down_Separation = dS1;
		Other_RAC = oRAC1;
		Other_Capability = oc1;
		Climb_Inhibit = ci1;
		initialize();
		alt_sep_test();
		Cur_Vertical_Sep = cvs2;
		High_Confidence = hc2;
		Two_of_Three_Reports_Valid = ttrv2;
		Own_Tracked_Alt = ota2;
		Own_Tracked_Alt_Rate = otar2;
		Other_Tracked_Alt = otTa2;
		Alt_Layer_Value = alv2;
		Up_Separation = upS2;
		Down_Separation = dS2;
		Other_RAC = oRAC2;
		Other_Capability = oc2;
		Climb_Inhibit = ci2;
		initialize();
		alt_sep_test();
	}

	public static void main(String[] args) {
		TCAS_modified o = new TCAS_modified();
		o.initialize();
//		if (args.length == 1) {
//			if (args[0].equals("Non_Crossing_Biased_Climb")) {
//				o.Non_Crossing_Biased_Climb();
//			} else if (args[0].equals("Non_Crossing_Biased_Descend")) {
//				o.Non_Crossing_Biased_Descend();
//			} else if (args[0].equals("alt_sep_test")) {
//				o.alt_sep_test();
//			} else if (args[0].equals("startTcas")) {
//				o.startTcas(0, true, true, 0, 0, 0, 0, 0, 0, 0, 0, false);
//			}
//		}
		try {
			if (args.length == 1) {
				RunUtil.runMethod(o, args[0]);
			}
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

	// public static void main (String[] args) {
	//
	// TCAS tcas = new TCAS();
	// if (args.length == 12){
	// tcas.Cur_Vertical_Sep = Integer.parseInt(args[0]);
	// if (args[1].equalsIgnoreCase("0"))
	// tcas.High_Confidence = false;
	// else
	// tcas.High_Confidence = true;
	// if (args[2].equalsIgnoreCase("0"))
	// tcas.Two_of_Three_Reports_Valid = false;
	// else
	// tcas.Two_of_Three_Reports_Valid = true;
	// tcas.Own_Tracked_Alt = Integer.parseInt((args[3]));
	// tcas.Own_Tracked_Alt_Rate = Integer.parseInt(args[4]);
	// tcas.Other_Tracked_Alt = Integer.parseInt(args[5]);
	// tcas.Alt_Layer_Value = Integer.parseInt(args[6]);
	// tcas.Up_Separation = Integer.parseInt(args[7]);
	// tcas.Down_Separation = Integer.parseInt(args[8]);
	// tcas.Other_RAC = Integer.parseInt(args[9]);
	// tcas.Other_Capability = Integer.parseInt(args[10]);
	// if (args[11].equalsIgnoreCase("0"))
	// tcas.Climb_Inhibit = false;
	// else
	// tcas.Climb_Inhibit = true;
	// }else if (args.length == 0){
	//
	// }else{
	// System.out.println("Invalid number of args");
	// }
	// tcas.initialize();
	// int res = tcas.alt_sep_test();
	// System.out.println(">>>>>>results: " + res);
	// }
	/*
	 * main(argc, argv) int argc; char *argv[]; { if(argc < 13) {
	 * fprintf(stdout, "Error: Command line arguments are\n"); fprintf(stdout,
	 * "Cur_Vertical_Sep, High_Confidence, Two_of_Three_Reports_Valid\n");
	 * fprintf(stdout,
	 * "Own_Tracked_Alt, Own_Tracked_Alt_Rate, Other_Tracked_Alt\n");
	 * fprintf(stdout, "Alt_Layer_Value, Up_Separation, Down_Separation\n");
	 * fprintf(stdout, "Other_RAC, Other_Capability, Climb_Inhibit\n"); exit(1);
	 * } initialize(); Cur_Vertical_Sep = atoi(argv[1]); High_Confidence =
	 * atoi(argv[2]); Two_of_Three_Reports_Valid = atoi(argv[3]);
	 * Own_Tracked_Alt = atoi(argv[4]); Own_Tracked_Alt_Rate = atoi(argv[5]);
	 * Other_Tracked_Alt = atoi(argv[6]); Alt_Layer_Value = atoi(argv[7]);
	 * Up_Separation = atoi(argv[8]); Down_Separation = atoi(argv[9]); Other_RAC
	 * = atoi(argv[10]); Other_Capability = atoi(argv[11]); Climb_Inhibit =
	 * atoi(argv[12]);
	 * 
	 * fprintf(stdout, "%d\n", alt_sep_test()); exit(0); }
	 */

}
