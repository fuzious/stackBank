package com.stack.bank.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Savings extends Customer {

	public Savings(int accountNumber, int balance, String type, String name, Transaction[] transactions) {
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
			int interest = 4 * bal / 1200;
			bal += interest;
			createTransaction(bal, tr, interest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void calculatePenalty() {
		// code to update transaction array and update Database.json
		// Take 2% penalty if balance less than 2000

		int bal = super.balance;
		if (bal < 2000) {
			List<Transaction> tr = new ArrayList<>();
			Collections.addAll(tr, super.transactions);
			int penalty = -2 * bal / 1200;
			bal += penalty;
			createTransaction(bal, tr, penalty);
		}
	}

	private void createTransaction(int bal, List<Transaction> tr, int penalty) {
		super.balance = bal;
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Transaction ob = new Transaction(localDateTime.format(format), penalty, bal);
		tr.add(ob);
		super.transactions = tr.toArray(new Transaction[0]);
	}
}
