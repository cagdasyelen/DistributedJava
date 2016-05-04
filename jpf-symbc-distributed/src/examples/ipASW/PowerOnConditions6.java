package ipASW;

public class PowerOnConditions6 {
  private WithinHysteresis12 WithinHysteresis_1223_class_member0 = new WithinHysteresis12();
  private PassBelowThreshold13 PassBelowThreshold_1222_class_member1 = new PassBelowThreshold13();

  public void Main12( boolean AltitudeOk_2, double Altitude_3, double Threshold_4, boolean DOIPower_5, boolean[] PowerOn_6 )
  {
    boolean sig_0;
    boolean sig_1[] = new boolean[ 1 ];
    boolean sig_2[] = new boolean[ 1 ];

    WithinHysteresis_1223_class_member0.Main24( Altitude_3, Threshold_4, DOIPower_5, sig_2 );
    PassBelowThreshold_1222_class_member1.Main25( Altitude_3, Threshold_4, sig_1 );
	sig_0 = sig_1[ 0 ] || sig_2[ 0 ];
	PowerOn_6[ 0 ] = AltitudeOk_2 && sig_0;
  }
  public void Init20(  )
  {
    PassBelowThreshold_1222_class_member1.Init26(  );
    WithinHysteresis_1223_class_member0.Init27(  );
  }
}
