package etg;

import java.lang.reflect.InvocationTargetException;

import gov.nasa.jpf.symbc.Symbolic;

public class WBS_EP {

	// Internal state
	@Symbolic("true")
	private int WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE;
	@Symbolic("true")
	private int WBS_Node_WBS_BSCU_rlt_PRE1;
	@Symbolic("true")
	private int WBS_Node_WBS_rlt_PRE2;

	// Outputs
	private int Nor_Pressure;
	@SuppressWarnings("unused")
	private int Alt_Pressure;
	private int Sys_Mode;

	public WBS_EP() {
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
	}

	public void myUpdate(int PedalPos, boolean AutoBrake, boolean Skid) {
		int WBS_Node_WBS_AS_MeterValve_Switch;
		int WBS_Node_WBS_AccumulatorValve_Switch;
		int WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch;
		boolean WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator;
		int WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1;
		int WBS_Node_WBS_BSCU_Command_Switch;
		boolean WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6;
		int WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay;
		int WBS_Node_WBS_BSCU_Switch2;
		int WBS_Node_WBS_BSCU_Switch3;
		int WBS_Node_WBS_BSCU_Unit_Delay1;
		int WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
		int WBS_Node_WBS_SelectorValve_Switch;
		int WBS_Node_WBS_SelectorValve_Switch1;
		int WBS_Node_WBS_Unit_Delay2;

		WBS_Node_WBS_Unit_Delay2 = WBS_Node_WBS_rlt_PRE2;
		WBS_Node_WBS_BSCU_Unit_Delay1 = WBS_Node_WBS_BSCU_rlt_PRE1;
		WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay = WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE;

		WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator = (WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay == 0);

		if ((PedalPos == 0)) {
			WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
		} else {
			if ((PedalPos == 1)) {
				WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 1;
			} else {
				if ((PedalPos == 2)) {
					WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 2;
				} else {
					if ((PedalPos == 3)) {
						WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 3;
					} else {
						if ((PedalPos == 4)) {
							WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 4;
						} else {
							WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
						}
					}
				}
			}
		}

		if ((AutoBrake && WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)) {
			WBS_Node_WBS_BSCU_Command_Switch = 1;
		} else {
			WBS_Node_WBS_BSCU_Command_Switch = 0;
		}

		WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6 = ((((!(WBS_Node_WBS_BSCU_Unit_Delay1 == 0)) && (WBS_Node_WBS_Unit_Delay2 <= 0)) && WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator) || (!WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator));

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			if (Skid)
				WBS_Node_WBS_BSCU_Switch3 = 0;
			else
				WBS_Node_WBS_BSCU_Switch3 = 4;
		} else {
			WBS_Node_WBS_BSCU_Switch3 = 4;
		}

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 0;
		} else {
			WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 5;
		}

		if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) {
			WBS_Node_WBS_SelectorValve_Switch1 = 0;
		} else {
			WBS_Node_WBS_SelectorValve_Switch1 = 5;
		}

		if ((!WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6)) {
			WBS_Node_WBS_AccumulatorValve_Switch = 0;
		} else {
			if ((WBS_Node_WBS_SelectorValve_Switch1 >= 1)) {
				WBS_Node_WBS_AccumulatorValve_Switch = WBS_Node_WBS_SelectorValve_Switch1;
			} else {
				WBS_Node_WBS_AccumulatorValve_Switch = 5;
			}
		}

		if ((WBS_Node_WBS_BSCU_Switch3 == 0)) {
			WBS_Node_WBS_AS_MeterValve_Switch = 0;
		} else {
			if ((WBS_Node_WBS_BSCU_Switch3 == 1)) {
				WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 4);
			} else {
				if ((WBS_Node_WBS_BSCU_Switch3 == 2)) {
					WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 2);
				} else {
					if ((WBS_Node_WBS_BSCU_Switch3 == 3)) {
						WBS_Node_WBS_AS_MeterValve_Switch = ((WBS_Node_WBS_AccumulatorValve_Switch / 4) * 3);
					} else {
						if ((WBS_Node_WBS_BSCU_Switch3 == 4)) {
							WBS_Node_WBS_AS_MeterValve_Switch = WBS_Node_WBS_AccumulatorValve_Switch;
						} else {
							WBS_Node_WBS_AS_MeterValve_Switch = 0;
						}
					}
				}
			}
		}

		if (Skid) {
			WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = 0;
		} else {
			WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = (WBS_Node_WBS_BSCU_Command_Switch + WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1);
		}

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			Sys_Mode = 1;
		} else {
			Sys_Mode = 0;
		}

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			WBS_Node_WBS_BSCU_Switch2 = 0;
		} else {
			if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 0) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 1))) {
				WBS_Node_WBS_BSCU_Switch2 = 0;
			} else {
				if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 1) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 2))) {
					WBS_Node_WBS_BSCU_Switch2 = 1;
				} else {
					if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 2) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 3))) {
						WBS_Node_WBS_BSCU_Switch2 = 2;
					} else {
						if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 3) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 4))) {
							WBS_Node_WBS_BSCU_Switch2 = 3;
						} else {
							WBS_Node_WBS_BSCU_Switch2 = 4;
						}
					}
				}
			}
		}

		if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) {
			WBS_Node_WBS_SelectorValve_Switch = WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
		} else {
			WBS_Node_WBS_SelectorValve_Switch = 0;
		}

		if ((WBS_Node_WBS_BSCU_Switch2 == 0)) {
			Nor_Pressure = 0;
		} else {
			if ((WBS_Node_WBS_BSCU_Switch2 == 1)) {
				Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 4);
			} else {
				if ((WBS_Node_WBS_BSCU_Switch2 == 2)) {
					Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 2);
				} else {
					if ((WBS_Node_WBS_BSCU_Switch2 == 3)) {
						Nor_Pressure = ((WBS_Node_WBS_SelectorValve_Switch / 4) * 3);
					} else {
						if ((WBS_Node_WBS_BSCU_Switch2 == 4)) {
							Nor_Pressure = WBS_Node_WBS_SelectorValve_Switch;
						} else {
							Nor_Pressure = 0;
						}
					}
				}
			}
		}

		if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 0)) {
			Alt_Pressure = 0;
		} else {
			if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 1)) {
				Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 4);
				assert WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE <= PedalPos;
			} else {
				if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 2)) {
					Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 2);
				} else {
					if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 3)) {
						Alt_Pressure = ((WBS_Node_WBS_AS_MeterValve_Switch / 4) * 3);
					} else {
						if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 4)) {
							Alt_Pressure = WBS_Node_WBS_AS_MeterValve_Switch;
						} else {
							Alt_Pressure = 0;
						}
					}
				}
			}
		}

		WBS_Node_WBS_rlt_PRE2 = Nor_Pressure;

		WBS_Node_WBS_BSCU_rlt_PRE1 = WBS_Node_WBS_BSCU_Switch2;

		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = Sys_Mode;

	}

	public void update(int PedalPos, boolean AutoBrake, boolean Skid) {
		int WBS_Node_WBS_AS_MeterValve_Switch;
		int WBS_Node_WBS_AccumulatorValve_Switch;
		int WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch;
		boolean WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator;
		int WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1;
		int WBS_Node_WBS_BSCU_Command_Switch;
		boolean WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6;
		int WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay;
		int WBS_Node_WBS_BSCU_Switch2;
		int WBS_Node_WBS_BSCU_Switch3;
		int WBS_Node_WBS_BSCU_Unit_Delay1;
		int WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
		int WBS_Node_WBS_SelectorValve_Switch;
		int WBS_Node_WBS_SelectorValve_Switch1;
		int WBS_Node_WBS_Unit_Delay2;

		WBS_Node_WBS_Unit_Delay2 = WBS_Node_WBS_rlt_PRE2;
		WBS_Node_WBS_BSCU_Unit_Delay1 = WBS_Node_WBS_BSCU_rlt_PRE1;
		WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay = WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE;

		WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator = (WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay == 0);

		if ((PedalPos == 0)) {
			WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
//			assert false;
		} else {
			if ((PedalPos == 1)) {
				WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 1;
			} else {
				if ((PedalPos == 2)) {
					WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 2;
				} else {
					if ((PedalPos == 3)) {
						WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 3;
					} else {
						if ((PedalPos == 4)) {
							WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 4;
						} else {
							WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
						}
					}
				}
			}
		}

		if ((AutoBrake && WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)) {
			WBS_Node_WBS_BSCU_Command_Switch = 1;
		} else {
			WBS_Node_WBS_BSCU_Command_Switch = 0;
		}

		WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6 = ((((!(WBS_Node_WBS_BSCU_Unit_Delay1 == 0)) && (WBS_Node_WBS_Unit_Delay2 <= 0)) && WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator) || (!WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator));

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			if (Skid)
				WBS_Node_WBS_BSCU_Switch3 = 0;
			else
				WBS_Node_WBS_BSCU_Switch3 = 4;
		} else {
			WBS_Node_WBS_BSCU_Switch3 = 4;
		}

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 0;
		} else {
			WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 5;
		}

		if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) {
			WBS_Node_WBS_SelectorValve_Switch1 = 0;
		} else {
			WBS_Node_WBS_SelectorValve_Switch1 = 5;
		}

		if ((!WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6)) {
			WBS_Node_WBS_AccumulatorValve_Switch = 0;
		} else {
			if ((WBS_Node_WBS_SelectorValve_Switch1 >= 1)) {
				WBS_Node_WBS_AccumulatorValve_Switch = WBS_Node_WBS_SelectorValve_Switch1;
			} else {
				WBS_Node_WBS_AccumulatorValve_Switch = 5;
			}
		}

		if ((WBS_Node_WBS_BSCU_Switch3 == 0)) {
			WBS_Node_WBS_AS_MeterValve_Switch = 0;
		} else {
			if ((WBS_Node_WBS_BSCU_Switch3 == 1)) {
				WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 4);
			} else {
				if ((WBS_Node_WBS_BSCU_Switch3 == 2)) {
					WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 2);
				} else {
					if ((WBS_Node_WBS_BSCU_Switch3 == 3)) {
						WBS_Node_WBS_AS_MeterValve_Switch = ((WBS_Node_WBS_AccumulatorValve_Switch / 4) * 3);
					} else {
						if ((WBS_Node_WBS_BSCU_Switch3 == 4)) {
							WBS_Node_WBS_AS_MeterValve_Switch = WBS_Node_WBS_AccumulatorValve_Switch;
						} else {
							WBS_Node_WBS_AS_MeterValve_Switch = 0;
						}
					}
				}
			}
		}

		if (Skid) {
			WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = 0;
		} else {
			WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = (WBS_Node_WBS_BSCU_Command_Switch + WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1);
		}

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			Sys_Mode = 1;
		} else {
			Sys_Mode = 0;
		}

		if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
			WBS_Node_WBS_BSCU_Switch2 = 0;
		} else {
			if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 0) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 1))) {
				WBS_Node_WBS_BSCU_Switch2 = 0;
			} else {
				if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 1) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 2))) {
					WBS_Node_WBS_BSCU_Switch2 = 1;
				} else {
					if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 2) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 3))) {
						WBS_Node_WBS_BSCU_Switch2 = 2;
					} else {
						if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 3) && (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 4))) {
							WBS_Node_WBS_BSCU_Switch2 = 3;
						} else {
							WBS_Node_WBS_BSCU_Switch2 = 4;
						}
					}
				}
			}
		}

		if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) {
			WBS_Node_WBS_SelectorValve_Switch = WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
		} else {
			WBS_Node_WBS_SelectorValve_Switch = 0;
		}

		if ((WBS_Node_WBS_BSCU_Switch2 == 0)) {
			Nor_Pressure = 0;
		} else {
			if ((WBS_Node_WBS_BSCU_Switch2 == 1)) {
				Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 4);
			} else {
				if ((WBS_Node_WBS_BSCU_Switch2 == 2)) {
					Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 2);
				} else {
					if ((WBS_Node_WBS_BSCU_Switch2 == 3)) {
						Nor_Pressure = ((WBS_Node_WBS_SelectorValve_Switch / 4) * 3);
					} else {
						if ((WBS_Node_WBS_BSCU_Switch2 == 4)) {
							Nor_Pressure = WBS_Node_WBS_SelectorValve_Switch;
						} else {
							Nor_Pressure = 0;
						}
					}
				}
			}
		}

		if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 0)) {
			Alt_Pressure = 0;
		} else {
			if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 1)) {
				Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 4);
//				assert WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE <= PedalPos;
			} else {
				if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 2)) {
					Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 2);
				} else {
					if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 3)) {
						Alt_Pressure = ((WBS_Node_WBS_AS_MeterValve_Switch / 4) * 3);
					} else {
						if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 4)) {
							Alt_Pressure = WBS_Node_WBS_AS_MeterValve_Switch;
						} else {
							Alt_Pressure = 0;
						}
					}
				}
			}
		}

		WBS_Node_WBS_rlt_PRE2 = Nor_Pressure;

		WBS_Node_WBS_BSCU_rlt_PRE1 = WBS_Node_WBS_BSCU_Switch2;

		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = Sys_Mode;
		
		if(!Skid && WBS_Node_WBS_BSCU_rlt_PRE1 == 0 && !AutoBrake && PedalPos != 4 && PedalPos != 3 && PedalPos != 2 && PedalPos != 1 && PedalPos != 0 && WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE != 0) {
			assert false;
		}

	}

	public void myMain1(int PedalPos, boolean AutoBrake, boolean Skid) {
		myUpdate(PedalPos, AutoBrake, Skid);
		myUpdate(PedalPos, !AutoBrake, !Skid);
	}

	public void main1(int PedalPos, boolean AutoBrake, boolean Skid) {
		update(PedalPos, AutoBrake, Skid);
		update(PedalPos, AutoBrake, Skid);
	}

	public void main2(int PedalPos, boolean AutoBrake, boolean Skid) {
		update(PedalPos, AutoBrake, Skid);
		update(PedalPos, AutoBrake, !Skid);
		update(PedalPos, !AutoBrake, Skid);
	}

	public void main3(int PedalPos, boolean AutoBrake, boolean Skid) {
		update(PedalPos, AutoBrake, Skid);
		update(PedalPos, !AutoBrake, Skid);
		update(PedalPos, AutoBrake, !Skid);
		update(PedalPos, false, true);
	}

	public void launch(int pedal1, boolean auto1, boolean skid1, int pedal2,
			boolean auto2, boolean skid2, int pedal3, boolean auto3,
			boolean skid3) {
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
		update(pedal1, auto1, skid1);
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
		update(pedal2, auto2, skid2);
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
		update(pedal3, auto3, skid3);
	}

	public void mainBody(int PedalPos, boolean AutoBrake, boolean Skid) {
		update(PedalPos, AutoBrake, Skid);
		update(PedalPos, AutoBrake, Skid);
	}

	public static void main(String[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException {
		WBS_EP wBS = new WBS_EP();
		// if (args.length == 1) {
		// if (args[0].equals("myUpdate")) {
		// wBS.myUpdate(0, false, false);
		// } else if (args[0].equals("myMain1")) {
		// wBS.myMain1(0, false, false);
		// } else if(args[0].equals("launch")) {
		// wBS.launch(0, false, false, 0, false, false, 0, false, false);
		// }
		if (args.length == 1) {
			// try {

			RunUtil.runMethod(wBS, args[0]);
			// } catch (IllegalAccessException e) {
			// e.printStackTrace();
			// } catch (IllegalArgumentException e) {
			// e.printStackTrace();
			// } catch (InvocationTargetException e) {
			// e.printStackTrace();
			// } catch (InstantiationException e) {
			// e.printStackTrace();
			// }
			// wBS.myMain1(0, true, true);
		} else if (args.length == 0) {

			// int PedalPos = Debug.getSymbolicInteger(0, 4, "PedalPos_1");
			// boolean AutoBrake = Debug.getSymbolicBoolean("AutoBrake_1");
			// boolean Skid = Debug.getSymbolicBoolean("Skid_1");

			// new code
			int PedalPos = 0;
			boolean AutoBrake = true;
			boolean Skid = true;

			wBS.update(PedalPos, AutoBrake, Skid);

			// PedalPos = Debug.getSymbolicInteger(0, 4, "PedalPos_2");
			// AutoBrake = Debug.getSymbolicBoolean("AutoBrake_2");
			// Skid = Debug.getSymbolicBoolean("Skid_2");

			// new code
			PedalPos = 0;
			AutoBrake = true;
			Skid = true;

			wBS.update(PedalPos, AutoBrake, Skid);

			// PedalPos = Debug.getSymbolicInteger(0, 4, "PedalPos_3");
			// AutoBrake = Debug.getSymbolicBoolean("AutoBrake_3");
			// Skid = Debug.getSymbolicBoolean("Skid_3");

			// new code
			PedalPos = 0;
			AutoBrake = true;
			Skid = true;

			wBS.update(PedalPos, AutoBrake, Skid);

			// PedalPos = Debug.getSymbolicInteger(0, 4, "PedalPos_4");
			// AutoBrake = Debug.getSymbolicBoolean("AutoBrake_4");
			// Skid = Debug.getSymbolicBoolean("Skid_4");

			// new code
			PedalPos = 0;
			AutoBrake = true;
			Skid = true;

			wBS.update(PedalPos, AutoBrake, Skid);

			// PedalPos = Debug.getSymbolicInteger(0, 4, "PedalPos_5");
			// AutoBrake = Debug.getSymbolicBoolean("AutoBrake_5");
			// Skid = Debug.getSymbolicBoolean("Skid_5");

			// //new code
			// PedalPos = 0;
			// AutoBrake = true;
			// Skid = true;
			// wbs.update(PedalPos, AutoBrake, Skid);
			//
			// /*PedalPos = Debug.getSymbolicInteger(0, 4, "PedalPos_6");
			// AutoBrake = Debug.getSymbolicBoolean("AutoBrake_6");
			// Skid = Debug.getSymbolicBoolean("Skid_6");
			//
			// wbs.update(PedalPos, AutoBrake, Skid);*/
		}
	}
}
