package com.stack.bank.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stack.bank.beans.Customer;
import com.stack.bank.beans.Database;
import com.stack.bank.beans.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WithdrawMoney extends JInternalFrame implements ActionListener {
	
	private LocalDateTime localDateTime;
	private JTextField txtNo, txtName, txtWithdraw, date;
	private JButton btnSave, btnCancel;
	private Database dbs;
	
	public WithdrawMoney () {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Withdraw Money", false, true, false, true);
		setSize (335, 235);

		dbs=new Database();
		localDateTime = LocalDateTime.now();
		fillDbs();
		
		JPanel jpWith = new JPanel();
		jpWith.setLayout (null);

		JLabel lbNo = new JLabel("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
		JLabel lbName = new JLabel("Person Name:");
		lbName.setForeground (Color.black);
		lbName.setBounds (15, 55, 80, 25);
		JLabel lbDate = new JLabel("With. Date:");
		lbDate.setForeground (Color.black);
		lbDate.setBounds (15, 90, 80, 25);
		JLabel lbWithdraw = new JLabel("With. Amount:");
		lbWithdraw.setForeground (Color.black);
		lbWithdraw.setBounds (15, 125, 80, 25);

		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		//Checking the Account No. Provided By User on Lost Focus of the TextBox.
		txtNo.addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) { }
			public void focusLost (FocusEvent fe) {
				if (txtNo.getText().equals ("")) { }
				else {
					checkAccount (Integer.parseInt(txtNo.getText()));		//Finding if Account No. Already Exist or Not.
				}
			}
		}
		);
		txtNo.setBounds (105, 20, 205, 25);

		txtName = new JTextField ();
		txtName.setEnabled (false);
		txtName.setBounds (105, 55, 205, 25);
		txtWithdraw = new JTextField ();
		txtWithdraw.setHorizontalAlignment (JTextField.RIGHT);
		txtWithdraw.setBounds (105, 125, 205, 25);
		localDateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		date = new JTextField(localDateTime.format(format));
		date.setEditable(false);
		date.setBounds(105,90,205,25);

		//Restricting The User Input to only Numerics in Numeric TextBoxes.
		txtNo.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);
		txtWithdraw.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);


		//Aligning The Buttons.
		btnSave = new JButton ("Save");
		btnSave.setBounds (20, 165, 120, 25);
		btnSave.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (185, 165, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpWith.add (lbNo);
		jpWith.add (txtNo);
		jpWith.add (lbName);
		jpWith.add (txtName);
		jpWith.add (lbDate);
		jpWith.add (date);
		jpWith.add (lbWithdraw);
		jpWith.add (txtWithdraw);
		jpWith.add (btnSave);
		jpWith.add (btnCancel);

		//Adding Panel to Window.
		getContentPane().add (jpWith);
		
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
			else if (txtWithdraw.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Withdraw Amount.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtWithdraw.requestFocus ();
			}
			else {
				int validAccountNumber=Integer.parseInt(txtNo.getText());
				List<Customer> customers=dbs.getCustomers();
				Customer x = new Customer() ;
				for ( Customer y: customers) {
					if (y.getAccountNumber() == validAccountNumber) {
						x=y;
						break;
					}
				}
				if (x.getBalance() == 0) {
					JOptionPane.showMessageDialog (this, txtName.getText () + " doesn't have any Amount in Balance.",
							"BankSystem - EmptyAccount", JOptionPane.PLAIN_MESSAGE);
					txtClear ();
				}
				else if (Integer.parseInt(txtWithdraw.getText()) > x.getBalance()) {
					JOptionPane.showMessageDialog (this, "Withdraw Amount can't greater than Actual Balance.",
							"BankSystem - Large Amount", JOptionPane.PLAIN_MESSAGE);
					txtWithdraw.setText ("");
					txtWithdraw.requestFocus ();
				}
				else {
					editDbs ();	//Update the Contents of Array.
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
	private void checkAccount(int validAccountNumber)  {
		boolean found =false;
		List<Customer> customers=dbs.getCustomers();
		for (Customer x: customers) {
			if (x.getAccountNumber() == validAccountNumber) {
				found = true;
				showRec(x);
				break;
			}
		}
		if (!found) {
			String str = txtNo.getText ();
			txtClear ();
			JOptionPane.showMessageDialog (this, "Account No. " + str + " doesn't Exist.",
				"Punjab and Maharashtra Cooperative Bank - WrongNo", JOptionPane.PLAIN_MESSAGE);
		}
	}

	//Function which display Record from Array onto the Form.
	private void showRec(Customer x) {

		txtNo.setText (""+x.getAccountNumber());
		txtName.setText (x.getName());

	}

	//Function use to Clear all TextFields of Window.
	private void txtClear() {

		txtNo.setText ("");
		txtName.setText ("");
		txtWithdraw.setText ("");
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		localDateTime = LocalDateTime.now();
		date.setText(""+localDateTime.format(format));
		txtNo.requestFocus ();
	}

	//Function use to Edit an Element's Value of the Array.
	private void editDbs () {
		int validAccountNumber=Integer.parseInt(txtNo.getText());
		List<Customer> customers=dbs.getCustomers();
		for (Customer x: customers) {
			if (x.getAccountNumber() == validAccountNumber) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				localDateTime = LocalDateTime.now();
				Transaction transaction = new Transaction(localDateTime.format(format),-Integer.parseInt(txtWithdraw.getText()),x.getBalance()-Integer.parseInt(txtWithdraw.getText()));
				x.setBalance(x.getBalance()-Integer.parseInt(txtWithdraw.getText()));
				List<Transaction> AL=new ArrayList<>();
				Collections.addAll(AL,x.getTransactions());
				AL.add(transaction);
				x.setTransactions(AL.toArray(new Transaction[0]));
				break;
			}
		}
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
