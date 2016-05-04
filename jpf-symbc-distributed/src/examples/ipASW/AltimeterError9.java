package ipASW;

public class AltimeterError9 {
  private boolean X02015;

  public void Main15( boolean AltimeterOk_2, boolean[] ErrorStatus_3 )
  {
    boolean sig_0;
    boolean sig_1;
    boolean sig_2;

    sig_2 = X02015;
    X02015 = AltimeterOk_2;
    sig_1 = !sig_2;
    sig_0 = !AltimeterOk_2;
	ErrorStatus_3[ 0 ] = sig_1 && sig_0;
  }
  public void Init17(  )
  {
    X02015 = false;
  }
}
