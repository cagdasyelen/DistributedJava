package ipASW;

public class Alt_Calc14 {
  private byte Value2057;
  private double Value2123;
  private int Threshold2205;
  private byte Value2298;
  private int Threshold2365;

  public void Main7( double Altitude_In_2, boolean Altitude_Ok_3, double[] Altitude_Out_4, byte[] Altitude_Div_5 )
  {
    byte sig_0;
    double sig_1;
    byte sig_2;

    sig_2 = Value2057;
    sig_1 = Value2123;
	if (((Altitude_Ok_3) ? 1 : 0) >= Threshold2205) {
		Altitude_Out_4[0] = Altitude_In_2;
	} else {
		if (((1) != 0)) {
			Altitude_Out_4[0] = sig_1;
		}
	}

    sig_0 = Value2298;
	if (((Altitude_Ok_3) ? 1 : 0) >= Threshold2365) {
		Altitude_Div_5[0] = sig_0;
	} else {
		if (((1) != 0)) {
			Altitude_Div_5[0] = sig_2;
		}
	}

  }
  public void Init10(  )
  {
    Value2057 = (byte)( 0 );
    Value2123 = 0;
    Threshold2205 = (int)( 0 );
    Value2298 = (byte)( 1 );
    Threshold2365 = (int)( 0 );
  }
}
