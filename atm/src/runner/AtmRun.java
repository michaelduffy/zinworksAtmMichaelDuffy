package runner;

import java.util.Scanner;

import model.Account;
import model.Atm;
import model.TransactionTracker;
import service.AtmServices;

public class AtmRun {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		//initialize accounts and ATM
		Account ac1 = new Account(123456789, 1234, 800, 200);
		Account ac2 = new Account(987654321, 4321, 1230, 150);
		Atm atm = new Atm(10,30,30,20);
		final int MAX_PIN_ATTEMPTS = 3;
		
		
		boolean continueApplication = true;
		
		//applicationStartPoint:
		do {
			continueApplication = true;
			String userInput = "";
			Account selectedAccount = new Account();
			boolean correctOptionSelected = true;
			
			System.out.println(" //////////////////////////////////////////");
			System.out.println(" /////////////  WELCOME :-) ///////////////");
			System.out.println(" //////////////////////////////////////////");
			
											
			do{
				
				//user account selection - crude simulation of card input
				correctOptionSelected = true;
				
				displayAccountSelection();
				System.out.print("Enter option: ");
				userInput = sc.next();
				
				if( (userInput.equalsIgnoreCase("A") || userInput.equalsIgnoreCase("B"))) {
					if(userInput.equalsIgnoreCase("A")) {
						selectedAccount = ac1;
					}else {
						selectedAccount = ac2;
					}
				}else {				
					correctOptionSelected = false;
					System.out.println("INCORRECT OPTION SELECTED!! PLEASE SELECT A or B!");					
				}
			}while(!correctOptionSelected);
			
			/// user pin entry ///////////////////////////////
			
			boolean validPin = false;
			int pinAttempt = 0;
			int userPinEntry = 0;
			
			do {
				
				if(pinAttempt == MAX_PIN_ATTEMPTS) {
					System.out.println("You used 3 PIN attempts - Better Luck Next Time!!");
					break;
				}
				pinAttempt++;
				System.out.println("please enter your PIN (acc No. "+selectedAccount.getAccountNo()+") Attempt No."+pinAttempt);
				System.out.print("Enter Pin:");
				boolean isNumber = true;
				
				//ensure correct number format - not counting these toward pin attempts
				do {
					
					if(!isNumber) {
						
						System.out.println("Number Format Exception!! - PLEASE ENTER DIGITS ONLY!! - Attempt No."+pinAttempt);
						System.out.print("Enter Pin:");
					}
					
					userInput = sc.next();
					isNumber = AtmServices.isNumeric(userInput);
					
				}while(!isNumber);
												
				userPinEntry = Integer.parseInt(userInput); 
				
				validPin = AtmServices.verifyPin(selectedAccount, userPinEntry);
				
				
			}while((!validPin) && (pinAttempt <= MAX_PIN_ATTEMPTS));
			
			//// only past this point if valid pin entered.
			////////// account options /////////////////
			
			boolean continueOptions = false;
			boolean withdrawlSelected = false;
			
			//acocountOptionsPoint:
			do {
				continueOptions = false;
				withdrawlSelected = false;
				
				if(validPin) {
					
					
					//display account options
					displayAccountOptionsSelection();
					userInput = sc.next();
					
					if(userInput.equalsIgnoreCase("A")) {
						displayAccountInfo(selectedAccount);
						userInput = sc.next();
						
					}else if(userInput.equalsIgnoreCase("B")) {
						//withdraw options
						displayWithdrawlOption(selectedAccount);
						userInput = sc.next();
						withdrawlSelected = true;
						
					}else {	
						System.out.println("///////////GOODBYE ///////////");
						printNumOfBlankLines(5);
						//continue applicationStartPoint;
					}
					
				}else {
					//continue applicationStartPoint;
				}
				
				switch(userInput) {
								
					case "Y":
					case "y":
						continueOptions = true;
						break;									
					default:
						
						if(withdrawlSelected) {
							boolean isNumber = AtmServices.isNumeric(userInput);
							if(isNumber) {
								if(userInput.equalsIgnoreCase("0")) {
									continueOptions = true;																		
								}else {
									//begin withdrawl processing
									int withdrawlAmount = Integer.parseInt(userInput);
																																				
									//begin withdrawl process
									TransactionTracker tracker = new TransactionTracker();
									tracker = AtmServices.processWithdrawl(atm, withdrawlAmount, selectedAccount);
									
									if(tracker.isValidTransaction()) {
										//display notes transaction info etc.
										displaySuccessWithdrawlInfo(withdrawlAmount, tracker, selectedAccount);
										System.out.println("/////////// GOODBYE AND THANKS FOR USING ///////////");
										
									}else {
										System.out.println(tracker.getErrorMsg());
										printNumOfBlankLines(2);
										System.out.println("/////////// GOODBYE AND THANKS FOR USING ///////////");
									}
																			
								}
								
							}else {								
									System.out.println("'"+userInput+"' is not a valid number format - please try again");
									printNumOfBlankLines(2);
									continueOptions = true;
									break;
							}
						}else {	
							continueOptions = false;
							//System.out.println("entered withdrawlSelected else");
							System.out.println("/////////// GOODBYE AND THANKS FOR USING ///////////");
							printNumOfBlankLines(5);
							break;
						}
				
				}
			}while(continueOptions);
			
		}while(continueApplication);
		
		sc.close();
		
		//System.out.println("exit 1 point");
	}
	
	public static void displayWithdrawlOption(Account account) {
		
		double accountBalance = account.getOpeningBalance();
		double accountOverdraft = account.getOverdraft();
		
		System.out.println("");
		System.out.println("////////////// WITHDRAWL //////////////////");
		System.out.println("||  Current Savings: "+ account.getOpeningBalance());
		System.out.println("||  Available Overdraft: "+account.getOverdraft());		
		displayMaxBalanceWithdrawl(accountBalance);
		displayMaxWithdrawl(accountBalance, accountOverdraft);
		System.out.println("////////////////////////////////////////////");
		System.out.print("ENTER WITHDRAWL AMOUNT (0 to return): ");
		
		
	}
	
	public static void displayAccountOptionsSelection() {
		System.out.println("");
		System.out.println("/////// ACCOUNT OPTIONS ////////");
		System.out.println("||   A. Display Balance       ||");
		System.out.println("||   B. Withdraw Cash         ||");
		System.out.println("||   C. Exit                  ||");
		System.out.println("////////////////////////////////");
		System.out.print("Enter Option (A|B|C):");
	}
	
	public static void displayAccountSelection() {
		System.out.println("");
		System.out.println("Please select account A or B");
		System.out.println("");
		System.out.println("A. account number - 123456789");
		System.out.println("B. account number - 987654321");
		System.out.println("");
	}
	
	public static void displayAccountInfo(Account account) {
		double accountBalance = account.getOpeningBalance();
		double accountOverdraft = account.getOverdraft();
		System.out.println("");
		System.out.println("///////// ACCOUNT INFO ////////////////");
		System.out.println("||  Account number: "+account.getAccountNo());
		System.out.println("||  Opening Balance: "+accountBalance);
		System.out.println("||  Overdraft: "+accountOverdraft);
		displayMaxWithdrawl(accountBalance, accountOverdraft);
		System.out.println("////////////////////////////////////////");
		System.out.print("Continue Y/N (Enter 'Y' to continue): ");
	}
	
	public static void printNumOfBlankLines(int numLines) {
		
		for(int i=0; i <= numLines; i++) {
			System.out.println();
		}				
	}
	
	public static void displaySuccessWithdrawlInfo(int withdrawlAmount, TransactionTracker tracker, Account account) {
		
		double accountBalance = account.getOpeningBalance();
		double accountOverdraft = account.getOverdraft();
		System.out.println("");
		System.out.println("///////// successfull withdrawl ////////////////");
		System.out.println("||  Withdrwal amount: "+withdrawlAmount);
		System.out.println("||  number of 50s: "+tracker.getNum50s());
		System.out.println("||  number of 20s: "+tracker.getNum20s());
		System.out.println("||  number of 10s: "+tracker.getNum10s());
		System.out.println("||  number of 5s: "+tracker.getNum5s());
		System.out.println("||  Updated Balance: "+accountBalance);
		System.out.println("||  Updated Overdraft: "+accountOverdraft);
		displayMaxWithdrawl(accountBalance, accountOverdraft);
		System.out.println("//////////////////////////////////////////");
		//System.out.print("Continue Y/N (Enter 'Y' to continue): ");
	}
	
	public static void displayMaxWithdrawl(Double accountBalance, double accountOverdraft) {
		if(accountBalance > 0) {
			System.out.println("||  Max Withdrawl: "+ (accountBalance + accountOverdraft));
		}else {
			System.out.println("||  Max Withdrawl: "+ (accountOverdraft));
		}
	}
	
	public static void displayMaxBalanceWithdrawl(Double accountBalance) {
		if(accountBalance <= 0) {
			System.out.println("||  Max withdrawl without overdraft: 0");
		}else {
			System.out.println("||  Max withdrawl without overdraft: "+ (accountBalance));
		}
	}

}
