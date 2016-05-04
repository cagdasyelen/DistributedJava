package ipASW;

public class Alt_Calc5 {
  private byte Value2045;
  private double Value2111;
  private int Threshold2187;
  private byte Value2286;
  private int Threshold2347;

  public void Main8( double Altitude_In_2, boolean Altitude_Ok_3, double[] Altitude_Out_4, byte[] Altitude_Div_5 )
  {
    byte sig_0;
    double sig_1;
    byte sig_2;

    sig_2 = Value2045;
    sig_1 = Value2111;
	if (((Altitude_Ok_3) ? 1 : 0) >= Threshold2187) {
		Altitude_Out_4[0] = Altitude_In_2;
	} else {
		if (((1) != 0)) {
			Altitude_Out_4[0] = sig_1;
		}
	}

    sig_0 = Value2286;
	if (((Altitude_Ok_3) ? 1 : 0) >= Threshold2347) {
		Altitude_Div_5[0] = sig_0;
	} else {
		if (((1) != 0)) {
			Altitude_Div_5[0] = sig_2;
		}
	}

  }
  public void Init9(  )
  {
    Value2045 = (byte)( 0 );
    Value2111 = 0;
    Threshold2187 = (int)( 0 );
    Value2286 = (byte)( 1 );
    Threshold2347 = (int)( 0 );
  }
}
