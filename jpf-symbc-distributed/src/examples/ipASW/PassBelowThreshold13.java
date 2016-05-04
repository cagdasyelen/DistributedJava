package ipASW;

public class PassBelowThreshold13 {
  private double X02429;
  private double X02438;

  public void Main25( double Altitude_2, double Threshold_3, boolean[] PowerOn_4 )
  {
    boolean sig_0;
    boolean sig_1;
    double sig_2;
    double sig_3;

    sig_2 = X02429;
    sig_3 = X02438;
    X02438 = Threshold_3;
    X02429 = Altitude_2;
	sig_1 = Altitude_2 < Threshold_3;
	sig_0 = sig_2 >= sig_3;
	PowerOn_4[ 0 ] = sig_0 && sig_1;
  }
  public void Init26(  )
  {
    X02429 = 0;
    X02438 = 0;
  }
}
