package com.stack.bank.beans;

public class Transaction {
	private String d;
	private int amount;
	private int newBalance;

	public Transaction() {}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(int newBalance) {
		this.newBalance = newBalance;
	}

	@Override
	public String toString() {
		return "Transaction{" +
			"d='" + d + '\'' +
			", amount=" + amount +
			", newBalance=" + newBalance +
			'}';
	}

	public Transaction(String d, int amount, int newBalance) {
		this.d = d;
		this.amount = amount;
		this.newBalance = newBalance;
	}
}
