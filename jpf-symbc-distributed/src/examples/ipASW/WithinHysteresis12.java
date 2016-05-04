package ipASW;

public class WithinHysteresis12 {
  private boolean X02447;
  private double Value2485;

  public void Main24( double Altitude_2, double Threshold_3, boolean DOIPower_4, boolean[] PowerOn_5 )
  {
    double sig_0;
    double sig_1;
    boolean sig_2;
    double sig_3;
    boolean sig_4;

    sig_4 = X02447;
    X02447 = DOIPower_4;
    sig_0 = Value2485;
    sig_1 = Threshold_3 * sig_0;
    sig_3 = Threshold_3 + sig_1;
	sig_2 = Altitude_2 < sig_3;
	PowerOn_5[ 0 ] = sig_2 && sig_4;
  }
  public void Init27(  )
  {
    X02447 = false;
    Value2485 = 0.1;
  }
}
