package com.stack.bank.beans;

import java.util.List;

public class Database {
	private List<Customer> customers;

	public Database() {
	}

	@Override
	public String toString() {
		return "Database{" +
			"customers=" + customers +
			'}';
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

}
