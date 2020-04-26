package com.stack.bank.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stack.bank.beans.Customer;
import com.stack.bank.beans.Database;
import com.stack.bank.beans.Savings;
import com.stack.bank.beans.Transaction;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Init extends JWindow {

	public Init() {

		try {
			File tempFile = new File("Database.json");
			boolean exists = tempFile.exists();
			if (!exists) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				Transaction transaction = new Transaction(localDateTime.format(format), -100, -100);
				//			List<Transaction> transactions=new ArrayList<>();
				Transaction[] transactions = {transaction};
				//			transactions.add(transaction);
				Customer tmpCus = new Savings(-100, -100, "temp", "temp", transactions);
				List<Customer> tmpList = new ArrayList<>();
				tmpList.add(tmpCus);
				Database dbs = new Database();
				dbs.setCustomers(tmpList);
				objectMapper.writeValue(new File("Database.json"), dbs);
			}


			JLabel lbImage = new JLabel(new ImageIcon("Images/init.jpg"));
			Color cl = new Color(0, 0, 0);
			lbImage.setBorder(new LineBorder(cl, 1));

			getContentPane().add(lbImage, BorderLayout.CENTER);
			pack();

			setSize(getSize().width, getSize().height);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);

			new BankSystem();

			toFront();
			dispose();
			setVisible(false);
		}
		catch (Exception ignored) {}
	}

}
