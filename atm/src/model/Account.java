package model;

public class Account {
	
	private int accountNo;
	private int pin;
	private double openingBalance;
	private double overdraft;
	
	
	public Account(int accountNo, int pin, double openingBalance, double overdraft) {
		this.accountNo = accountNo;
		this.pin = pin;
		this.openingBalance = openingBalance;
		this.overdraft = overdraft;
	}
	
	public Account() {
		
	}

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public double getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(double overdraft) {
		this.overdraft = overdraft;
	}

	@Override
	public String toString() {
		return "Account [accountNo=" + accountNo + ", pin=" + pin + ", openingBalance=" + openingBalance
				+ ", overdraft=" + overdraft + "]";
	}
	
	

}
