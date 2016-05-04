package ipASW;

import java.lang.reflect.InvocationTargetException;

public class AltitudeAvg1 {
  private int Value1953;
  private int Value1973;
  private int Threshold1984;
  private Alt_Calc23 Alt_Calc2_1082_class_member3 = new Alt_Calc23();
  private Alt_Calc14 Alt_Calc1_1081_class_member4 = new Alt_Calc14();
  private Alt_Calc5 Alt_Calc_1080_class_member5 = new Alt_Calc5();

  public void Main1( double Altitude1_2, boolean Altitude_OK1_3, double Altitude2_4, boolean Altitude_OK2_5, double Altitude3_6, boolean Altitude_OK3_7, double Altitude_8, boolean Altitude_OK_9 )
  {
    int sig_0;
    int sig_1;
    double sig_2;
    int sig_3;
    int sig_4;
    double sig_5[] = new double[ 1 ];
    byte sig_6[] = new byte[ 1 ];
    double sig_7[] = new double[ 1 ];
    byte sig_8[] = new byte[ 1 ];
    double sig_9[] = new double[ 1 ];
    byte sig_10[] = new byte[ 1 ];

    Alt_Calc2_1082_class_member3.Main6( Altitude3_6, Altitude_OK3_7, sig_9, sig_10 );
    Alt_Calc1_1081_class_member4.Main7( Altitude2_4, Altitude_OK2_5, sig_7, sig_8 );
    Alt_Calc_1080_class_member5.Main8( Altitude1_2, Altitude_OK1_3, sig_5, sig_6 );
    sig_3 = sig_6[ 0 ] + sig_8[ 0 ] + sig_10[ 0 ];
    sig_2 = sig_5[ 0 ] + sig_7[ 0 ] + sig_9[ 0 ];
    sig_1 = Value1953;
	Altitude_OK_9 = sig_3 >= sig_1;
    sig_0 = Value1973;
	if (((Altitude_OK_9) ? 1 : 0) >= Threshold1984) {
		sig_4 = sig_3;
	} else {
		if (((1) != 0)) {
			sig_4 = sig_0;
		}
	}

    Altitude_8 = sig_2 / sig_4;
  }
  public void Init4(  )
  {
    Value1953 = (int)( 2 );
    Value1973 = (int)( 1 );
    Threshold1984 = (int)( 0 );
    Alt_Calc_1080_class_member5.Init9(  );
    Alt_Calc1_1081_class_member4.Init10(  );
    Alt_Calc2_1082_class_member3.Init11(  );
  }
  
  public static void main(String[] args) {
		AltitudeAvg1 o = new AltitudeAvg1();
		o.Init4();
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
}
