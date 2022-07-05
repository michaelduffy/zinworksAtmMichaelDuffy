package model;

public class Atm {
	
	private int numOf50s;
	private int numOf20s;
	private int numOf10s;
	private int numOf5s;
	
	private final int WITHDRAWL_MULTIPLE = 5;
	
						
	public int getWITHDRAWL_MULTIPLE() {
		return WITHDRAWL_MULTIPLE;
	}


	public Atm(int numOf50s, int numOf20s, int numOf10, int noOf5s) {
		super();
		this.numOf50s = numOf50s;
		this.numOf20s = numOf20s;
		this.numOf10s = numOf10;
		this.numOf5s = noOf5s;
	}


	public int getTotalAmount() {
		
		int total = 0;
		total += numOf50s * 50;
		total += numOf20s * 20;
		total += numOf10s * 10;
		total += numOf5s * 5;
		
		return total;
	}
	

	public int getNumOf50s() {
		return numOf50s;
	}


	public void setNumOf50s(int numOf50s) {
		this.numOf50s = numOf50s;
	}


	public int getNumOf20s() {
		return numOf20s;
	}


	public void setNumOf20s(int numOf20s) {
		this.numOf20s = numOf20s;
	}


	public int getNumOf10s() {
		return numOf10s;
	}


	public void setNumOf10s(int numOf10) {
		this.numOf10s = numOf10;
	}


	public int getNumOf5s() {
		return numOf5s;
	}


	public void setNumOf5s(int noOf5s) {
		this.numOf5s = noOf5s;
	}

		
	@Override
	public String toString() {
		return "Atm [numOf50s=" + numOf50s + ", numOf20s=" + numOf20s + ", numOf10s=" + numOf10s + ", numOf5s="
				+ numOf5s + " total= "+ getTotalAmount()+", withdrawlMultiple="+"WITHDRAWLMULTIPLE]";
	}
	
	
	
}
