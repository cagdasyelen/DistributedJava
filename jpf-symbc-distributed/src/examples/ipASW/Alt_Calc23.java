package ipASW;

public class Alt_Calc23 {
	private byte Value2069;
	private double Value2135;
	private int Threshold2223;
	private byte Value2310;
	private int Threshold2383;

	public void Main6(double Altitude_In_2, boolean Altitude_Ok_3,
			double[] Altitude_Out_4, byte[] Altitude_Div_5) {
		byte sig_0;
		double sig_1;
		byte sig_2;

		sig_2 = Value2069;
		sig_1 = Value2135;
		if (((Altitude_Ok_3) ? 1 : 0) >= Threshold2223) {
			Altitude_Out_4[0] = Altitude_In_2;
		} else {
			if (((1) != 0)) {
				Altitude_Out_4[0] = sig_1;
			}
		}

		sig_0 = Value2310;
		if (((Altitude_Ok_3) ? 1 : 0) >= Threshold2383) {
			Altitude_Div_5[0] = sig_0;
		} else {
			if (((1) != 0)) {
				Altitude_Div_5[0] = sig_2;
			}
		}

	}

	public void Init11() {
		Value2069 = (byte) (0);
		Value2135 = 0;
		Threshold2223 = (int) (0);
		Value2310 = (byte) (1);
		Threshold2383 = (int) (0);
	}

}
