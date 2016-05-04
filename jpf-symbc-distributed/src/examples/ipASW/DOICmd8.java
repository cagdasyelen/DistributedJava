package ipASW;

public class DOICmd8 {
  private RisinEdge11 RisinEdge_1221_class_member0 = new RisinEdge11();

  public void Main14( boolean PowerOn_2, boolean Inhibit_3, boolean DOICommand_4 )
  {
    boolean sig_0;
    boolean sig_1[] = new boolean[ 1 ];

    RisinEdge_1221_class_member0.Main22( PowerOn_2, sig_1 );
    sig_0 = !Inhibit_3;
	DOICommand_4 = sig_0 && sig_1[ 0 ];
  }
  public void Init19(  )
  {
    RisinEdge_1221_class_member0.Init23(  );
  }
}
