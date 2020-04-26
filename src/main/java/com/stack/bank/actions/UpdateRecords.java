package com.stack.bank.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stack.bank.beans.Current;
import com.stack.bank.beans.Customer;
import com.stack.bank.beans.Database;
import com.stack.bank.beans.Savings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateRecords {
	Database dbs;

	private void fillDbs() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File("Database.json");
			dbs = objectMapper.readValue(file, Database.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		List<Customer> AL=dbs.getCustomers();
		List<Customer> Set=new ArrayList<>();
		for (Customer X: AL) {
			Customer temp;
			if (X.getType().equals("true")) {
				temp = new Savings(X.getAccountNumber(),X.getBalance(),X.getType(),X.getName(),X.getTransactions());
			}
			else {
				temp = new Current(X.getAccountNumber(),X.getBalance(),X.getType(),X.getName(),X.getTransactions());
			}
			Set.add(temp);
		}
		dbs.setCustomers(Set);
	}

	public void update() {
		fillDbs();
		List<Customer> customerList = dbs.getCustomers();
		for (Customer x: customerList) {
			if(x.getBalance() < 2000) {


			}
			x.calculateInterest();
			x.calculatePenalty();
		}
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			objectMapper.writeValue(new File("Database.json"),dbs);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
