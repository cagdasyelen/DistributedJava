package MerArbiter;

import java.lang.reflect.InvocationTargetException;

import edu.vanderbilt.isis.sm.RhapsodyInterpreter;
import edu.vanderbilt.isis.sm.StateflowInterpreter;
import edu.vanderbilt.isis.sm.SymbolicDataProvider;
import edu.vanderbilt.isis.sm.Event;
import edu.vanderbilt.isis.sm.UMLInterpreter;

public class MyMerArbiterSym {

	private TopLevelArbiter arbiter;
	private TopLevelUser1 user1;
	private TopLevelUser2 user2;

	public MyMerArbiterSym() {
		this.arbiter = new TopLevelArbiter();
		this.user1 = new TopLevelUser1();
		this.user2 = new TopLevelUser2();

		User1Reader u1reader = new User1Reader(this.user1.r1.User122,
				new SymbolicDataProvider(1), null);
		User2Reader u2reader = new User2Reader(this.user2.r1.User228,
				new SymbolicDataProvider(1), null);
		ArbiterReader areader = new ArbiterReader(this.arbiter.r1.Arbiter3,
				new SymbolicDataProvider(1), null);

		this.arbiter.sim = new StateflowInterpreter(this.arbiter.sm, u1reader);
		this.user1.sim = new StateflowInterpreter(this.user1.sm, u2reader);
		// this.user1.sim = new UMLInterpreter(this.user1.sm, u2reader);
		// this.user1.sim = new RhapsodyInterpreter(this.user1.sm, u2reader);
		this.user2.sim = new StateflowInterpreter(this.user2.sm, areader);

		this.arbiter.sim.initialize();
		this.user1.sim.initialize();
		this.user2.sim.initialize();
	}

	private void setArbiterInput() {
		this.arbiter.r1.Arbiter3.u1resource = this.user1.r1.User122.resourceOut;
		this.arbiter.r1.Arbiter3.u1request = this.user1.r1.User122.request;
		this.arbiter.r1.Arbiter3.u1cancel = this.user1.r1.User122.cancel;

		this.arbiter.r1.Arbiter3.u2resource = this.user2.r1.User228.resourceOut;
		this.arbiter.r1.Arbiter3.u2request = this.user2.r1.User228.request;
		this.arbiter.r1.Arbiter3.u2cancel = this.user2.r1.User228.cancel;
	}

	private void setUser1Input(int resource, boolean reset) {
		this.user1.r1.User122.resourceIn = resource;
		this.user1.r1.User122.grant = this.arbiter.r1.Arbiter3.u1grant;
		this.user1.r1.User122.deny = this.arbiter.r1.Arbiter3.u1deny;
		this.user1.r1.User122.rescind = this.arbiter.r1.Arbiter3.u1rescind;
		this.user1.r1.User122.reset = reset;
	}

	private void setUser2Input(int resource, boolean reset) {
		this.user2.r1.User228.resourceIn = resource;
		this.user2.r1.User228.grant = this.arbiter.r1.Arbiter3.u2grant;
		this.user2.r1.User228.deny = this.arbiter.r1.Arbiter3.u2deny;
		this.user2.r1.User228.rescind = this.arbiter.r1.Arbiter3.u2rescind;
		this.user2.r1.User228.reset = reset;
	}

	public void runMachines(int i00, int i01, boolean b01, int i02,
			boolean b02, int i10, int i11, boolean b11, int i12, boolean b12,
			int i20, int i21, boolean b21, int i22, boolean b22) {
//		this.user1.r1.User122.resourceIn = 1;
//		this.user1.r1.User122.grant = false;
//		this.user1.r1.User122.deny = false;
//		this.user1.r1.User122.rescind = false;
//		this.user1.r1.User122.reset = false;
//
//		this.user2.r1.User228.resourceIn = 0;
//		this.user2.r1.User228.grant = false;
//		this.user2.r1.User228.deny = false;
//		this.user2.r1.User228.rescind = false;
//		this.user2.r1.User228.reset = false;

		Event e = new Event("");

		this.user1.sim.addEvent(e);
		this.user1.sim.step();
		this.user2.sim.addEvent(e);
		this.user2.sim.step();
		this.setArbiterInput();
		this.arbiter.sim.addEvent(e);
		this.arbiter.sim.step();

		innerRun(e, i00, i01, b01, i02, b02);
		innerRun(e, i10, i11, b11, i12, b12);
		innerRun(e, i20, i21, b21, i22, b22);
//		innerRun(e, i30, i31, b31, i32, b32);
//		innerRun(e, i40, i41, b41, i42, b42);
//		innerRun(e, i50, i51, b51, i52, b52);
	}
	
	public void runMachines2(int i00, int i01, boolean b01, int i02,
			boolean b02, int i10, int i11, boolean b11, int i12, boolean b12,
			int i20, int i21, boolean b21, int i22, boolean b22, int i32,
			boolean b32, boolean b31, int i31, int i30) {
//		this.user1.r1.User122.resourceIn = 1;
//		this.user1.r1.User122.grant = false;
//		this.user1.r1.User122.deny = false;
//		this.user1.r1.User122.rescind = false;
//		this.user1.r1.User122.reset = false;
//
//		this.user2.r1.User228.resourceIn = 0;
//		this.user2.r1.User228.grant = false;
//		this.user2.r1.User228.deny = false;
//		this.user2.r1.User228.rescind = false;
//		this.user2.r1.User228.reset = false;

		Event e = new Event("");

		this.user1.sim.addEvent(e);
		this.user1.sim.step();
		this.user2.sim.addEvent(e);
		this.user2.sim.step();
		this.setArbiterInput();
		this.arbiter.sim.addEvent(e);
		this.arbiter.sim.step();

		innerRun(e, i00, i01, b01, i02, b02);
		innerRun(e, i10, i11, b11, i12, b12);
		innerRun(e, i20, i21, b21, i22, b22);
		innerRun(e, i30, i31, b31, i32, b32);
//		innerRun(e, i40, i41, b41, i42, b42);
//		innerRun(e, i50, i51, b51, i52, b52);
	}

	private void innerRun(Event e, int i0, int i1, boolean b1, int i2,
			boolean b2) {
		switch (flag(i0)) {
		case 0:
			System.out.println("user1");
			this.setUser1Input(i1, b1);
			this.user1.sim.addEvent(e);
			this.user1.sim.step();
			break;
		case 1:
			System.out.println("user2");
			this.setUser2Input(i2, b2);
			this.user2.sim.addEvent(e);
			this.user2.sim.step();
			break;
		case 2:
			System.out.println("arbiter");
			this.setArbiterInput();
			this.arbiter.sim.addEvent(e);
			this.arbiter.sim.step();
			break;
		}
	}

	public void run(int resource1, boolean reset1, int resource2,
			boolean reset2, int i00, int i01, boolean b01, int i02,
			boolean b02, int i10, int i11, boolean b11, int i12, boolean b12,
			int i20, int i21, boolean b21, int i22, boolean b22, int i32,
			boolean b32, boolean b31, int i31, int i30, int i40, int i41,
			boolean b41, int i42, boolean b42, int i50, int i51, boolean b51,
			int i52, boolean b52) {
		setUser1Input(resource1, reset1);
		setUser2Input(resource2, reset2);
		runMachines(i00, i01, b01, i02, b02, i10, i11, b11, i12, b12, i20, i21,
				b21, i22, b22);
	}
	
	public void run2(int resource1, boolean reset1, int resource2,
			boolean reset2, int i00, int i01, boolean b01, int i02,
			boolean b02, int i10, int i11, boolean b11, int i12, boolean b12,
			int i20, int i21, boolean b21, int i22, boolean b22, int i32,
			boolean b32, boolean b31, int i31, int i30) {
		setUser1Input(resource1, reset1);
		setUser2Input(resource2, reset2);
		runMachines2(i00, i01, b01, i02, b02, i10, i11, b11, i12, b12, i20, i21,
				b21, i22, b22, i32, b32, b31, i31, i30);
	}

	public static int flag(int x) {
		if (x == 0)
			return 0;
		else if (x == 1)
			return 1;
		else
			return 2;
	}

	public static int flag(boolean x) {
		if (x)
			return 1;
		else
			return 0;
	}

	public static void main(String[] args) {
		System.out.println("********************");
		MyMerArbiterSym mer = new MyMerArbiterSym();
		// mer.runMachines();
		try {
			if (args.length == 1) {
				RunUtil.runMethod(mer, args[0]);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
//		mer.run(0, false, 0, false, 0, 0, false, 0, false, 0, 0, false, 0,
//				false, 0, 0, false, 0, false, 0, false, false, 0, 0, 0, 0,
//				false, 0, false, 0, 0, false, 0, false);
	}

}
