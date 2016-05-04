package ipASW;

import java.lang.reflect.InvocationTargetException;

public class asw {
  private AltitudeAvg1 AltitudeAvg_1039_class_member0 = new AltitudeAvg1();
  private ASW_REQ2 ASW_REQ_1040_class_member1 = new ASW_REQ2();
  
  public void Main0( double Altitude_Input_2, boolean AltitudeOK_Input_3, boolean DOIOn_Input_4, boolean Inhibit_Input_5, boolean Reset_Input_6, double Threshold_Input_7, double Altitude_Input1_8, boolean AltitudeOK_Input1_9, double Altitude_Input2_10, boolean AltitudeOK_Input2_11, boolean AlarmOn_12, boolean DOICommand_13 )
  {
    double sig_0 = 0;
    boolean sig_1 = false;
    
    AltitudeAvg_1039_class_member0.Main1( Altitude_Input_2, AltitudeOK_Input_3, Altitude_Input1_8, AltitudeOK_Input1_9, Altitude_Input2_10, AltitudeOK_Input2_11, sig_0, sig_1 );
//    Init3();
    ASW_REQ_1040_class_member1.Main2( sig_0, sig_1, DOIOn_Input_4, Inhibit_Input_5, Reset_Input_6, Threshold_Input_7, AlarmOn_12, DOICommand_13 );
//    assert false;
  }
  public void Init3(  )
  {
    AltitudeAvg_1039_class_member0.Init4(  );
    ASW_REQ_1040_class_member1.Init5(  );
  }
  
  public static void main(String[] args) {
		asw o = new asw();
		o.Init3();
//		try {
			if (args.length == 1) {
				if(args[0].equals("Main0")) {
					o.Main0(0.0, false, false, false, false, 0, 0, false, 0, false, false, false);
				}
//				RunUtil.runMethod(o, args[0]);
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
