package com.stack.bank.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stack.bank.beans.Current;
import com.stack.bank.beans.Customer;
import com.stack.bank.beans.Database;
import com.stack.bank.beans.Savings;
import com.stack.bank.beans.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewAccount extends JInternalFrame implements ActionListener {

	private JTextField txtNo, txtName, date;
	private JButton btnSave, btnCancel;
	private JComboBox<String> accountType;
	private LocalDateTime localDateTime;
	private Database dbs;
	
	public NewAccount () {
		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Create New Account", false, true, false, true);
		setSize (335, 235);
		JPanel jpInfo = new JPanel();
		jpInfo.setBounds (0, 0, 500, 115);
		jpInfo.setLayout (null);

		JLabel lbNo = new JLabel("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
		JLabel lbName = new JLabel("Person Name:");
		lbName.setForeground (Color.black);
	    lbName.setBounds (15, 55, 80, 25);
		JLabel lbDate = new JLabel("Date:");
		lbDate.setForeground (Color.black);
		lbDate.setBounds (15, 90, 80, 25);
		JLabel lbAccountType = new JLabel("Account Type");
		lbAccountType.setForeground (Color.black);
		lbAccountType.setBounds (15, 125, 80, 25);
		
		dbs=new Database();
		fillDbs();
		
		int validAccountNumber=(int)(Math.random()*1000000000);
		while (true) {
			boolean chk=checkAccount(validAccountNumber);
			if(chk)
				break;
			validAccountNumber=(int)(Math.random()*1000000000);
		}
		txtNo = new JTextField (""+validAccountNumber);
		txtNo.setEditable(false);
		txtNo.setHorizontalAlignment (JTextField.RIGHT);    
		txtNo.setBounds (105, 20, 205, 25);
		txtName = new JTextField ();
		txtName.setBounds (105, 55, 205, 25);
		localDateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		date = new JTextField(localDateTime.format(format));
		date.setEditable(false);
		date.setBounds(105,90,205,25);
		String[] types={"Savings","Current"};
		accountType=new JComboBox<>(types);
		accountType.setBounds(105,125,205,25);
		btnSave = new JButton ("Save");
		btnSave.setBounds (20, 165, 120, 25);
		btnSave.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (185, 165, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpInfo.add (lbNo);
		jpInfo.add (txtNo);
		jpInfo.add (lbName);
		jpInfo.add (txtName);
		jpInfo.add (lbDate);
		jpInfo.add (date);
		jpInfo.add (lbAccountType);
		jpInfo.add (accountType);
		jpInfo.add (btnSave);
		jpInfo.add (btnCancel);
		
		//Adding Panel to Window.
		getContentPane().add (jpInfo);

		//In the End Showing the New Account Window.
		setVisible (true);

	}

	//Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed (ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnSave) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			}
			else if (txtName.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Name of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtName.requestFocus ();
			}
			else {
				int depAmount;
				if (accountType.getSelectedItem().equals("Savings"))
					depAmount = 2000;
				else 
					depAmount = 10000;
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				localDateTime = LocalDateTime.now();
				Transaction transaction = new Transaction(localDateTime.format(format),depAmount,depAmount);
				Transaction[] transactions = {transaction};
				Customer customer;
				if (accountType.getSelectedItem().equals("Savings"))
					customer = new Savings (Integer.parseInt(txtNo.getText()),depAmount,""+accountType.getSelectedItem().equals("Savings"),txtName.getText(),transactions);
				else
					customer = new Current (Integer.parseInt(txtNo.getText()),depAmount,""+accountType.getSelectedItem().equals("Savings"),txtName.getText(),transactions);
				List<Customer> customers=dbs.getCustomers();
				customers.add(customer);
				dbs.setCustomers(customers);
				ObjectMapper objectMapper=new ObjectMapper();
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				try {
					objectMapper.writeValue(new File("Database.json"),dbs);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					txtClear();
				}
			}
		}
		if (obj == btnCancel) {
			txtClear ();
			setVisible (false);
			dispose();
		}

	}
	
	//Function use to load all Records from File when Application Execute.

	private void fillDbs(){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File("Database.json");
			dbs = objectMapper.readValue(file, Database.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Function use to Find Record by Matching the Contents of Records Array with ID TextBox.
	
	private boolean checkAccount(int validAccountNumber)  {
		List<Customer> customers=dbs.getCustomers();
		for (Customer x: customers) {
			if(x.getAccountNumber()==validAccountNumber)
				return false;
		}
		return true;
		
	}

	//Function use to Clear all TextFields of Window.
	private void txtClear() {
		int validAccountNumber=(int)(Math.random()*1000000000);
		while (true) {
			boolean chk=checkAccount(validAccountNumber);
			if(chk)
				break;
			validAccountNumber=(int)(Math.random()*1000000000);
		}
		txtNo.setText (""+validAccountNumber);
		txtName.setText ("");
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		localDateTime = LocalDateTime.now();
		date.setText(""+localDateTime.format(format));
		txtNo.requestFocus ();
		
	}
}	
