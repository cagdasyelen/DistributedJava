package bankaccount;

import gov.nasa.jpf.symbc.Symbolic;
import gov.nasa.jpf.vm.Verify;

/**
 * 
 * @author Mithun Acharya Taken from Inkumsah, Xie's ASE08 paper
 */
public class BankAccount_EP {
	@Symbolic("true")
	private int balance;
	@Symbolic("true")
	private int numberOfWithdrawals = 0;

	public BankAccount_EP(int amount) {
		balance = amount;
	}

	// @Preconditions("amount<1000&&amount>-1000")
	public void deposit(int amount) {
		if (amount > 0)
			System.out.println("I am easily reachable in deposit");
		balance = balance + amount;
	}

	// @Preconditions("amount<1000&&amount>-1000")
	public void withdraw(int amount) {
		if (amount > balance) {
//			assert false;
			System.out.println("I am easily reachable in withdraw");
			return;
		}
		if (numberOfWithdrawals >= 10) {// was 10
//			assert (false);
			System.out.println("I am very hard to reach in withdraw");
			return;
		}
		balance = balance - amount;
		numberOfWithdrawals++;
		assert false;
	}

	public void testDriver0(int amount1, int amount2) {
		withdraw(amount1);
		withdraw(amount2);
	}

	public void testDriver(int amount1, int amount2, int amount3, int amount4,
			int amount5, int amount6) {
		// BankAccount b = new BankAccount(0);
		deposit(amount1);
		withdraw(amount2);
		deposit(amount3);
		withdraw(amount4);
		deposit(amount5);
		withdraw(amount6);
	}

	public void myDriver(int amount1, int amount2, int amount3, int amount4,
			int amount5, int amount6) {
		if (numberOfWithdrawals < 10) {
			deposit(amount6);
			withdraw(amount2);
			deposit(amount5);
			withdraw(amount3);
			deposit(amount4);
			withdraw(amount4);
			deposit(amount3);
			withdraw(amount5);
			deposit(amount1);
			withdraw(amount6);
			deposit(amount6);
			withdraw(amount2);
			deposit(amount5);
			withdraw(amount3);
			deposit(amount4);
			deposit(amount6);
			withdraw(amount2);
			deposit(amount5);
			withdraw(amount3);
			deposit(amount4);
			withdraw(amount4);
//			deposit(amount3);
//			withdraw(amount5);
//			deposit(amount1);
		}
		if (numberOfWithdrawals >= 10) {
			withdraw(amount1);
		}
		// if(numberOfWithdrawals < 5) {
		// withdraw(amount6);
		// }
		// deposit(amount1);
		// deposit(amount2);
		// deposit(amount3);
		// deposit(amount4);
		// deposit(amount5);

	}

	public static void main(String[] args) {
		BankAccount_EP ba = new BankAccount_EP(0);
		if (args.length == 1) {
			if (args[0].equals("withdraw")) {
				ba.withdraw(0);
			} else if (args[0].equals("deposit")) {
				ba.deposit(0);
			} else if (args[0].equals("myDriver")) {
				ba.myDriver(0, 0, 0, 0, 0, 0);
			}
		}
		// ba.myDriver(0,0,0,0,0,0);
		// try {
		// if (args.length == 1) {
		// RunUtil.runMethod(ba, args[0]);
		// }
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// e.printStackTrace();
		// }
	}
}
