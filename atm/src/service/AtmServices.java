package service;

import model.Account;
import model.Atm;
import model.TransactionTracker;

public class AtmServices {
	
	//private constructor
	private AtmServices() {
		
	}
		
	public static boolean verifyPin(Account account, int pin) {
		
		if(account.getPin() == pin) {
			return true;
		}else {
			return false;
		}
	}
	
   public static boolean verifyWithdrawlAmount(Account account, int withdrawlAmount) {
	   
	   double totalAvailableFunds = account.getOpeningBalance() + account.getOverdraft();
		
		if(withdrawlAmount <= totalAvailableFunds) {
			return true;
		}else {
			return false;
		}
	}
		
	//method to process a withdrawl
	public static TransactionTracker processWithdrawl(Atm atm, int withdrawlAmount, Account account) {
		
		TransactionTracker tracker = new TransactionTracker();
		String errorMsg = "";
		boolean validTransaction = true;
		
		if(!verifyWithdrawlAmount(account,withdrawlAmount)) {
			errorMsg += " ERROR -- User has insufficient funds -- \n";
			tracker.setErrorMsg(errorMsg);
			tracker.setValidTransaction(false);
			validTransaction = false;
		}
		
		if(!amountIsMultipleOf(withdrawlAmount, atm)) {
			errorMsg += " ERROR -- Withdrawl amount is not a multiple of "+atm.getWITHDRAWL_MULTIPLE()+" -- \n";
			tracker.setErrorMsg(errorMsg);
			tracker.setValidTransaction(false);
			validTransaction = false;
		}
		if(!checkEnoughMoney(atm, withdrawlAmount)) {
			errorMsg += " ERROR -- Atm does not have enough cash -- \n";
			tracker.setErrorMsg(errorMsg);
			tracker.setValidTransaction(false);
			validTransaction = false;
		}
		
		if(validTransaction) {
			tracker.setValidTransaction(true);
			tracker = process50s(atm, withdrawlAmount, tracker);			
			process20s(atm, tracker);			
			process10s(atm, tracker);			
			process5s(atm, tracker);
			
			//update account info
			updateAccountAfterTransaction(tracker, account);
			
			
		}
		tracker.setAtm(atm);
					
		return tracker;
	}
	
	public static boolean requireOverdraftCheck(Account account, int withdrawlAmount) {
		
		double accountBalance = account.getOpeningBalance();
		//double overDraft = account.getOverdraft();
		boolean isOverdraftRequired = false;
		
		if(accountBalance <= 0) {
			isOverdraftRequired = true;
		}else if(withdrawlAmount > accountBalance) {
			isOverdraftRequired = true;
		}
		
		return isOverdraftRequired;
	}
	
	
	public static void updateAccountAfterTransaction(TransactionTracker tracker, Account account ) {
		
		if(tracker.isValidTransaction()) {
			int totalOf50s = tracker.getNum50s() * 50;
			int totalOf20s = tracker.getNum20s() * 20;
			int totalOf10s = tracker.getNum10s() * 10;
			int totalOf5s = tracker.getNum5s() * 5;
			
			double transactionTotal = totalOf50s + totalOf20s + totalOf10s + totalOf5s;
			
			double userBalance = account.getOpeningBalance();
			double userOverdraft = account.getOverdraft();
			double maxUserWithdrawl = userBalance + userOverdraft;
			
			if(transactionTotal <= userBalance) {
				account.setOpeningBalance(userBalance - transactionTotal);
			}else {
				account.setOpeningBalance(userBalance - transactionTotal);
				double requiredOverdraft = maxUserWithdrawl - transactionTotal;
				account.setOverdraft(userOverdraft - requiredOverdraft);
			}
		}
	}
	
	//method to check if withdrawl amount is not greater than amount in atm
		public static boolean checkEnoughMoney(Atm atm, int withdrawlAmount) {
			if(atm.getTotalAmount() >= withdrawlAmount) {
				// add more code here to check correct denominations
				// i.e atm may have 100 in 2*50 but user may only want to withdraw 20
				// in that case the atm does not have enough cash
				return true;
			}else {
				return false;
			}
		}
		
		//method to check if withdrawl amount is multiple of particular number
		public static boolean amountIsMultipleOf(int amount, Atm atm) {
			
			boolean vaildMultipleAmount = false;
			
			if(amount % atm.getWITHDRAWL_MULTIPLE() == 0) {
				vaildMultipleAmount = true;
			}
			
			return vaildMultipleAmount;
		}
	
	
	//static method to process how many 50s needed for transaction
	public static TransactionTracker process50s(Atm atm, int withdrawlAmount, TransactionTracker transactionTracker) {
		
						
		int num50sInAtm = atm.getNumOf50s();
		
		//check first if any remaining 50s in atm
		if(num50sInAtm == 0) {
			transactionTracker.setAmount(withdrawlAmount);
			transactionTracker.setNum50s(0);
		}else {
				
			int num50sToReturn = 0;
									
			int num50sInWithdrawlAmount = withdrawlAmount/50;
			
			if(num50sInAtm >= num50sInWithdrawlAmount) {
				num50sToReturn = num50sInWithdrawlAmount;
			}
			else {
				num50sToReturn = num50sInAtm;
			}
			
			//update num of 50s notes in atm
			if(num50sToReturn > 0) {
				atm.setNumOf50s(atm.getNumOf50s() - num50sToReturn);			
			}
			
			//calculate the remaining withdrawl amount
			int remainingWithdrawlAmount = withdrawlAmount - (num50sToReturn * 50);
			
			
			
			transactionTracker.setAmount(remainingWithdrawlAmount);
			transactionTracker.setNum50s(num50sToReturn);
		}
		
		return transactionTracker;
		
	}
	
	//static method to process how many 20s needed for transaction
	public static TransactionTracker process20s(Atm atm, TransactionTracker transactionTracker) {
		
		int num20sInAtm = atm.getNumOf20s();
		int remainingWithdrawl = transactionTracker.getAmount();
		
		//check first if any remaining 20s in atm
		if(num20sInAtm == 0) {
			//transactionTracker.setAmount(remainingWithdrawl);
			transactionTracker.setNum20s(0);
		}else {
				
			int num20sToReturn = 0;
									
			int num20sInWithdrawlAmount = remainingWithdrawl/20;
			
			if(num20sInAtm >= num20sInWithdrawlAmount) {
				num20sToReturn = num20sInWithdrawlAmount;
			}
			else {
				num20sToReturn = num20sInAtm;
			}
			
			//update num of 20s notes in atm
			if(num20sToReturn > 0) {
				atm.setNumOf20s(atm.getNumOf20s() - num20sToReturn);			
			}
			
			//calculate the remaining withdrawl amount
			remainingWithdrawl = remainingWithdrawl - (num20sToReturn * 20);
			
			
			
			transactionTracker.setAmount(remainingWithdrawl);
			transactionTracker.setNum20s(num20sToReturn);
		}
		
		return transactionTracker;
		
	}
	
	//static method to process how many 10s needed for transaction
	public static TransactionTracker process10s(Atm atm, TransactionTracker transactionTracker) {
		
		int num10sInAtm = atm.getNumOf10s();
		int remainingWithdrawl = transactionTracker.getAmount();
		
		//check first if any remaining 10s in atm
		if(num10sInAtm == 0) {
			//transactionTracker.setAmount(remainingWithdrawl);
			transactionTracker.setNum10s(0);
		}else {
				
			int num10sToReturn = 0;
									
			int num10sInWithdrawlAmount = remainingWithdrawl/10;
			
			if(num10sInAtm >= num10sInWithdrawlAmount) {
				num10sToReturn = num10sInWithdrawlAmount;
			}
			else {
				num10sToReturn = num10sInAtm;
			}
			
			//update num of 10s notes in atm
			if(num10sToReturn > 0) {
				atm.setNumOf10s(atm.getNumOf10s() - num10sToReturn);			
			}
			
			//calculate the remaining withdrawl amount
			remainingWithdrawl = remainingWithdrawl - (num10sToReturn * 10);
			
			
			
			transactionTracker.setAmount(remainingWithdrawl);
			transactionTracker.setNum10s(num10sToReturn);
		}
		
		return transactionTracker;
		
	}
	
	//static method to process how many 5s needed for transaction
	public static TransactionTracker process5s(Atm atm, TransactionTracker transactionTracker) {
		
		int num5sInAtm = atm.getNumOf5s();
		int remainingWithdrawl = transactionTracker.getAmount();
		
		//check first if any remaining 10s in atm
		if(num5sInAtm == 0) {
			//transactionTracker.setAmount(remainingWithdrawl);
			transactionTracker.setNum5s(0);
		}else {
				
			int num5sToReturn = 0;
									
			int num5sInWithdrawlAmount = remainingWithdrawl/5;
			
			if(num5sInAtm >= num5sInWithdrawlAmount) {
				num5sToReturn = num5sInWithdrawlAmount;
			}
			else {
				num5sToReturn = num5sInAtm;
			}
			
			//update num of 5s notes in atm
			if(num5sToReturn > 0) {
				atm.setNumOf5s(atm.getNumOf5s() - num5sToReturn);			
			}
			
			//calculate the remaining withdrawl amount
			remainingWithdrawl = remainingWithdrawl - (num5sToReturn * 5);
			
			
			
			transactionTracker.setAmount(remainingWithdrawl);
			transactionTracker.setNum5s(num5sToReturn);
		}
		
		return transactionTracker;
		
	}
	
	//https://www.baeldung.com/java-check-string-number
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	

}
