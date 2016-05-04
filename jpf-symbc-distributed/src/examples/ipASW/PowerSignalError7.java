package ipASW;

public class PowerSignalError7 {
  private boolean X02033;

  public void Main13( boolean DOIOn_2, boolean PowerSignal_3, boolean[] PowerSignalError_4 )
  {
    boolean sig_0;
    boolean sig_1;

    sig_1 = X02033;
    X02033 = PowerSignal_3;
    sig_0 = !DOIOn_2;
	PowerSignalError_4[ 0 ] = sig_0 && sig_1;
  }
  public void Init21(  )
  {
    X02033 = false;
  }
}
