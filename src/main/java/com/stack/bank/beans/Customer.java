package com.stack.bank.beans;

import java.util.Arrays;

public class Customer {
	protected int accountNumber;
	protected int balance;
	protected String type;
	protected String name;
	protected Transaction[] transactions;

	public Customer() {}
	
	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Transaction[] getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Customer(int accountNumber, int balance, String type, String name, Transaction[] transactions) {
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.type = type;
		this.name = name;
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "Customer{" +
			"accountNumber=" + accountNumber +
			", balance=" + balance +
			", type='" + type + '\'' +
			", name='" + name + '\'' +
			", transactions=" + Arrays.toString(transactions) +
			'}';
	}

	public void calculateInterest () {}

	public void calculatePenalty () {}

}
