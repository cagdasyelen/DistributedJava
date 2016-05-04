package ipASW;

public class AlarmDetermination10 {
  private boolean X02024;

  public void Main16( boolean AltimiterErrorStatus_2, boolean PowerErrorStatus_3, boolean Inhibit_4, boolean Reset_5, boolean AlarmOn_6 )
  {
    boolean sig_0;
    boolean sig_1;
    boolean sig_2;
    boolean sig_3;
    boolean sig_4;
    boolean sig_5;

    sig_3 = X02024;
    sig_2 = !Reset_5;
	sig_4 = sig_2 && sig_3;
	sig_1 = AltimiterErrorStatus_2 || PowerErrorStatus_3;
    sig_0 = !Inhibit_4;
	sig_5 = sig_1 && sig_0;
	AlarmOn_6 = sig_5 || sig_4;
    X02024 = AlarmOn_6;
  }
  public void Init18(  )
  {
    X02024 = false;
  }
}
