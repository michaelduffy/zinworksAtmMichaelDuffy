package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Account;
import model.Atm;
import model.TransactionTracker;
import service.AtmServices;

public class JunitAtmServiceTests {
	
		
	@Test
	public void testUpdateAccountAfterTransaction1(){
		
		//test with valid transaction info
		Account account = new Account(123456789,1234,800,200);
		
		//simulate 85 withdrawl
		
		TransactionTracker tracker = new TransactionTracker();
		tracker.setValidTransaction(true);
		tracker.setNum50s(1);
		tracker.setNum20s(1);
		tracker.setNum10s(1);
		tracker.setNum5s(1);
		
		//should deduct 85 from account
		
		AtmServices.updateAccountAfterTransaction(tracker, account);
		
		assertTrue(account.getOpeningBalance() == 715);
		
	}
	
		
	@Test
	public void testUpdateAccountAfterTransaction2(){
		
		//test with invalid transaction
		Account account = new Account(123456789,1234,800,200);
			
		
		TransactionTracker tracker = new TransactionTracker();
		tracker.setValidTransaction(false);
		tracker.setNum50s(1);
		tracker.setNum20s(1);
		tracker.setNum10s(1);
		tracker.setNum5s(1);
		
		//should deduct 85 from account
		
		AtmServices.updateAccountAfterTransaction(tracker, account);
		
		assertTrue(account.getOpeningBalance() == 800);
		
	}
	
	@Test
	public void testUpdateAccountAfterTransaction3(){
		
		//test with valid transaction info
		Account account = new Account(123456789,1234,800,200);
		
		//simulate 900 withdrawl - into overdraft amount
		
		TransactionTracker tracker = new TransactionTracker();
		tracker.setValidTransaction(true);
		tracker.setNum50s(16);
		tracker.setNum20s(3);
		tracker.setNum10s(3);
		tracker.setNum5s(2);
		
		//should deduct 85 from account
		
		AtmServices.updateAccountAfterTransaction(tracker, account);
		
		assertTrue(account.getOpeningBalance() == -100);
		assertTrue(account.getOverdraft() == 100);
		
	}
	
	@Test
	/*
	 * testing with amount that will require going into overdraft
	 */
	public void testRequireOverdraftCheck1() {
		
		Account ac1 = new Account(123456789, 1234, 800, 200);
		int withdrawlAmount = 900;
		
		boolean requireOverdraft = AtmServices.requireOverdraftCheck(ac1, withdrawlAmount);
		assertTrue(requireOverdraft);
	}
	
	@Test
	/*
	 * testing with amount that will not require going into overdraft
	 */
	public void testRequireOverdraftCheck2() {
		
		Account ac1 = new Account(123456789, 1234, 800, 200);
		int withdrawlAmount = 800;
		
		boolean requireOverdraft = AtmServices.requireOverdraftCheck(ac1, withdrawlAmount);
		assertFalse(requireOverdraft);
	}
	
	@Test
	/*
	 * testing going into overdraft when balance is zero
	 */
	public void testRequireOverdraftCheck3() {
		
		Account ac1 = new Account(123456789, 1234, 0, 200);
		int withdrawlAmount = 150;
		
		boolean requireOverdraft = AtmServices.requireOverdraftCheck(ac1, withdrawlAmount);
		assertTrue(requireOverdraft);
	}

	@Test
	public void testVerifyPin() {
		Account ac1 = new Account(123456789, 1234, 800, 200);
		
		int validPin = 1234;
		int invalidPin = 1233;
						
		boolean isValidPin = AtmServices.verifyPin(ac1, validPin );
		assertTrue(isValidPin);
			
		isValidPin = AtmServices.verifyPin(ac1, invalidPin );
		assertFalse(isValidPin);
				
	}
	
	@Test
	public void testVerifyWithdrawlAmount() {
		
		Account ac1 = new Account(123456789, 1234, 800, 200);
		
		int validAmount = 300;
		int validAmount2 = 800;
		int validAmount3 = 900;
		int validAmount4 = 1000;
		int invalidAmount = 1001;
								
		boolean isValidWithrawlAmount =AtmServices.verifyWithdrawlAmount(ac1, validAmount);
		assertTrue(isValidWithrawlAmount);
				
		isValidWithrawlAmount =AtmServices.verifyWithdrawlAmount(ac1, validAmount2);
		assertTrue(isValidWithrawlAmount);
				
		isValidWithrawlAmount =AtmServices.verifyWithdrawlAmount(ac1, validAmount3);
		assertTrue(isValidWithrawlAmount);
				
		isValidWithrawlAmount =AtmServices.verifyWithdrawlAmount(ac1, validAmount4);
		assertTrue(isValidWithrawlAmount);
				
		isValidWithrawlAmount =AtmServices.verifyWithdrawlAmount(ac1, invalidAmount);
		assertFalse(isValidWithrawlAmount);
	
	}
	
	@Test
	public void testProcessWithdrawlWithValidAmounts() {
		Atm atm = new Atm(10,30,30,20);
		Account account = new Account(123456789,1234,800,200);
		int waithdrawlAmount = 55;
		
		TransactionTracker tracker = AtmServices.processWithdrawl(atm, waithdrawlAmount,account);
		
		assertEquals(tracker.getNum50s(),1);
		assertEquals(tracker.getNum20s(),0);
		assertEquals(tracker.getNum10s(),0);
		assertEquals(tracker.getNum5s(),1);
	}
	
	@Test
	public void testProcessWithdrawlWithInvalidAmounts() {
		Atm atm = new Atm(10,30,30,20);
		Account account = new Account(123456789,1234,800,200);
		
		int waithdrawlAmount = 54;		
		TransactionTracker tracker = AtmServices.processWithdrawl(atm, waithdrawlAmount,account);
		
		assertEquals(tracker.getNum50s(),0);
		assertEquals(tracker.getNum20s(),0);
		assertEquals(tracker.getNum10s(),0);
		assertEquals(tracker.getNum5s(),0);
		assertEquals(tracker.getErrorMsg()," ERROR -- Withdrawl amount is not a multiple of 5 -- \n");
		
		waithdrawlAmount = 1200;		
		tracker = AtmServices.processWithdrawl(atm, waithdrawlAmount,account);
		
		assertEquals(tracker.getNum50s(),0);
		assertEquals(tracker.getNum20s(),0);
		assertEquals(tracker.getNum10s(),0);
		assertEquals(tracker.getNum5s(),0);
		assertEquals(tracker.getErrorMsg()," ERROR -- User has insufficient funds -- \n");
		
		waithdrawlAmount = 1510;		
		tracker = AtmServices.processWithdrawl(atm, waithdrawlAmount,account);
		
		assertEquals(tracker.getNum50s(),0);
		assertEquals(tracker.getNum20s(),0);
		assertEquals(tracker.getNum10s(),0);
		assertEquals(tracker.getNum5s(),0);
		assertEquals(tracker.getErrorMsg()," ERROR -- User has insufficient funds -- \n ERROR -- Atm does not have enough cash -- \n");
	}
	
	@Test
	/*
	 * testing with numeric string
	 */
	public void testIsNumeric(){
		String s = "1234";
		boolean b = AtmServices.isNumeric(s);
		assertTrue(b);				
	}
	
	@Test
	/*
	 * testing with non-numeric string
	 */
	public void testIsNumeric2(){
		String s = "2fft";
		boolean b = AtmServices.isNumeric(s);
		assertFalse(b);				
	}
	
	@Test
	/*
	 * testing with null string
	 */
	public void testIsNumeric3(){
		
		String s = null;
		boolean b = AtmServices.isNumeric(s);
		assertFalse(b);				
	}
	
	

}
