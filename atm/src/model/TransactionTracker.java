package model;

public class TransactionTracker {
	
	private int num50s;
	private int num20s;
	private int num10s;
	private int num5s;
	
	private int amount;
	
	private Atm atm;
	
	private boolean validTransaction;
	
	private String errorMsg = "No Error";
					
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getNum50s() {
		return num50s;
	}

	public void setNum50s(int num50s) {
		this.num50s = num50s;
	}

	public int getNum20s() {
		return num20s;
	}

	public void setNum20s(int num20s) {
		this.num20s = num20s;
	}

	public int getNum10s() {
		return num10s;
	}

	public void setNum10s(int num10s) {
		this.num10s = num10s;
	}

	public int getNum5s() {
		return num5s;
	}

	public void setNum5s(int num5s) {
		this.num5s = num5s;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public Atm getAtm() {
		return atm;
	}

	public void setAtm(Atm atm) {
		this.atm = atm;
	}
	
	public boolean isValidTransaction() {
		return validTransaction;
	}

	public void setValidTransaction(boolean validTransaction) {
		this.validTransaction = validTransaction;
	}

	@Override
	public String toString() {
		return "TransactionTracker [num50s=" + num50s + ", num20s=" + num20s + ", num10s=" + num10s + ", num5s=" + num5s
				+ ", amount=" + amount + "\n atm=" + atm + "\n errorMsg=" + errorMsg + "\n valid transaction="+validTransaction+"]";
	}

	
	
	
	
	

}
