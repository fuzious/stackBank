package com.stack.bank.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Current extends Customer {

	public Current(int accountNumber, int balance, String type, String name, Transaction[] transactions) {
		super(accountNumber, balance, type, name, transactions);
	}

	@Override
	public void calculateInterest() {
		// code to update transaction array and update Database.json
		// Take 8% interest added
		try {
			List<Transaction> tr = new ArrayList<>();
			Collections.addAll(tr, super.transactions);
			int bal = super.balance;
			int interest = 8 * bal / 1200;
			bal += interest;

			createTransaction(tr, bal, interest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTransaction(List<Transaction> tr, int bal, int interest) {
		super.balance = bal;
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Transaction ob = new Transaction(localDateTime.format(format), interest, bal);
		tr.add(ob);
		super.transactions = tr.toArray(new Transaction[0]);
	}

	@Override
	public void calculatePenalty() {
		// code to update transaction array and update Database.json
		// Take 4% penalty if balance less than 2000
		int bal = super.balance;
		if (bal < 2000) {
			List<Transaction> tr = new ArrayList<>();
			Collections.addAll(tr, super.transactions);
			int penalty = - 4 * bal / 1200;
			bal += penalty;
			createTransaction(tr, bal, penalty);
		}
	}
}
