package ipASW;

public class RisinEdge11 {
  private boolean X02420;

  public void Main22( boolean In1_2, boolean[] Out1_3 )
  {
    boolean sig_0;
    boolean sig_1;

    sig_1 = X02420;
    sig_0 = !sig_1;
	Out1_3[ 0 ] = In1_2 && sig_0;
    X02420 = Out1_3[ 0 ];
  }
  public void Init23(  )
  {
    X02420 = false;
  }
}
