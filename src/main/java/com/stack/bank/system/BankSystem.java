package com.stack.bank.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stack.bank.actions.DepositMoney;
import com.stack.bank.actions.JavaMailUtil;
import com.stack.bank.actions.NewAccount;
import com.stack.bank.actions.UpdateRecords;
import com.stack.bank.actions.WithdrawMoney;
import com.stack.bank.beans.Customer;
import com.stack.bank.beans.Database;
import com.stack.bank.beans.Transaction;
import com.stack.bank.UI.AquaTheme;
import com.stack.bank.UI.GrayTheme;
import com.stack.bank.UI.GreenTheme;
import com.stack.bank.UI.MetalThemeMenu;
import com.stack.bank.UI.MilkyTheme;
import com.stack.bank.UI.SandTheme;
import com.stack.bank.UI.SolidTheme;
import com.stack.bank.UI.UISwitchListener;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class BankSystem extends JFrame implements ActionListener, ItemListener {

	// Main Place on Form where All Child Forms will Shown.
	private JDesktopPane desktop = new JDesktopPane ();

	private JMenuItem addNew, printRec, end;				//File Menu Options.
	private	JMenuItem  deposit, withdraw;	//Edit Menu Options.
	private	JMenuItem oneByOne, allCustomer;				//View Menu Options.
	private	JMenuItem change;

	private JMenuItem close, closeAll;					//Window Menu Options.
	private	JMenuItem keyHelp, about;				//Help Menu Options.

	//PopupMenu of Program.
	private JPopupMenu popMenu = new JPopupMenu ();

	// MenuItems for PopupMenu of the Program.
	private JMenuItem open, report, dep, with, del, find, all;

	//For ToolBar's Button.
	private	JButton btnNew, btnDep, btnWith, btnRec, btnDel, btnSrch, btnHelp, btnKey;

	//Making the LookAndFeel Menu.
	private String[] strings = {"1. Metal", "2. Motif", "3. Windows"};
	private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels ();

	private JRadioButtonMenuItem[] radio = new JRadioButtonMenuItem[strings.length];

	Database dbs;

	//Following all Variables are use in BankSystem's IO's.

	//Variable use in Reading the BankSystem Records File & Store it in an Array.

	//String Type Array use to Load Records From File.

	//Variable for Reading the BankSystem Records File.
	private FileInputStream fis;
	private DataInputStream dis;

	//Constructor of The Bank Program to Initialize all Variables of Program.

	public BankSystem () {

		//Setting Program's Title.
		super ("Punjab and Maharashtra Cooperative Bank");

		dbs=new Database();
		UIManager.addPropertyChangeListener (new UISwitchListener(getRootPane()));

		//Creating the MenuBar.
		//For Program's MenuBar.
		JMenuBar bar = new JMenuBar();

		//Setting the Main Window of Program.
		setIconImage (getToolkit().getImage ("Images/Bank.gif"));
		setSize (700, 550);
		setJMenuBar (bar);

		//Closing Code of Main Window.
		addWindowListener (new WindowAdapter () {
			public void windowClosing (WindowEvent we) {
				quitApp ();
			}
		}
		);

		//Setting the Location of Application on Screen.
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getWidth()) / 2,
			(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

		//Creating the MenuBar Items.
		//All the Main Menu of the Program.
		JMenu mnuFile = new JMenu("File");
		mnuFile.setMnemonic ((int)'F');
		JMenu mnuEdit = new JMenu("Edit");
		mnuEdit.setMnemonic ((int)'E');
		JMenu mnuView = new JMenu("View");
		mnuView.setMnemonic ((int)'V');
		JMenu mnuOpt = new JMenu("Options");
		mnuOpt.setMnemonic ((int)'O');
		JMenu mnuWin = new JMenu("Window");
		mnuWin.setMnemonic ((int)'W');
		JMenu mnuHelp = new JMenu("Help");
		mnuHelp.setMnemonic ((int)'H');

		//Creating the MenuItems of Program.
		//MenuItems for FileMenu.
		addNew = new JMenuItem ("Open New Account", new ImageIcon ("Images/Open.gif"));
		addNew.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		addNew.setMnemonic ((int)'N');
		addNew.addActionListener (this);
		printRec = new JMenuItem ("Print Customer Balance", new ImageIcon ("Images/New.gif"));
		printRec.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		printRec.setMnemonic ((int)'R');
		printRec.addActionListener (this);
		end = new JMenuItem ("Quit BankSystem ?", new ImageIcon ("Images/export.gif"));
		end.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		end.setMnemonic ((int)'Q');	
		end.addActionListener (this);

		//MenuItems for EditMenu.
		deposit = new JMenuItem ("Deposit Money");
		deposit.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		deposit.setMnemonic ((int)'T');
		deposit.addActionListener (this);
		withdraw = new JMenuItem ("Withdraw Money");
		withdraw.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		withdraw.setMnemonic ((int)'W');	
		withdraw.addActionListener (this);

		//MenuItems for ViewMenu.
		oneByOne = new JMenuItem ("View One By One");
		oneByOne.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		oneByOne.setMnemonic ((int)'O');	
		oneByOne.addActionListener (this);
		allCustomer = new JMenuItem ("View All Customer", new ImageIcon ("Images/refresh.gif"));
		allCustomer.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		allCustomer.setMnemonic ((int)'A');
		allCustomer.addActionListener (this);

		//MenuItems for OptionMenu.
		change = new JMenuItem ("Change Background Color");
		change.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		change.setMnemonic ((int)'B');
		change.addActionListener (this);
		//Menu For Changing the Program's Layout.
		JMenuItem style = new JMenu("Change Layout Style");
		style.setMnemonic ((int)'L');
		for( int i = 0; i < radio.length ; i++ ) {			//Creating the subMenus of Style Menu.
			radio[i] = new JRadioButtonMenuItem (strings[i]);	//Build an Array of Layouts to Apply.
			radio[i].addItemListener (this);			//Setting their Actions.
			ButtonGroup group = new ButtonGroup();
			group.add (radio[i]);					//Making them Grouped.
			style.add (radio[i]);					//Adding to Style MenuOption.
		}
		//SubMenu of Theme For Applying different Themes to Program By Building an Array of Themes to Apply.
		MetalTheme[] themes = { new DefaultMetalTheme(), new GreenTheme(), new AquaTheme(),
					new SandTheme(), new SolidTheme(), new MilkyTheme(), new GrayTheme() };
		//Option Menu Options.
		JMenuItem theme = new MetalThemeMenu("Apply Theme", themes);        //Putting the Themes in ThemeMenu.
		theme.setMnemonic ((int)'M');

		//MenuItems for WindowMenu.
		close = new JMenuItem ("Close Active Window");
		close.setMnemonic ((int)'C');
		close.addActionListener (this);
		closeAll = new JMenuItem ("Close All Windows...");
		closeAll.setMnemonic ((int)'A');
		closeAll.addActionListener (this);

		//MenuItems for HelpMenu.
		keyHelp = new JMenuItem ("Interest");
		keyHelp.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
		keyHelp.setMnemonic ((int)'K');
		keyHelp.addActionListener (this);
		about = new JMenuItem ("About BankSystem", new ImageIcon ("Images/Save.gif"));
		about.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		about.setMnemonic ((int)'C');
		about.addActionListener (this);

		//Adding MenuItems to Menu.
	
		//File Menu Items.
		mnuFile.add (addNew);
		mnuFile.addSeparator ();
		mnuFile.add (printRec);
		mnuFile.addSeparator ();
		mnuFile.add (end);

		//Edit Menu Items.
		mnuEdit.add (deposit);
		mnuEdit.addSeparator ();
		mnuEdit.add (withdraw);

		//View Menu Items.
		mnuView.add (oneByOne);
		mnuView.addSeparator ();
		mnuView.add (allCustomer);

		//Options Menu Items.
		mnuOpt.add (change);
		mnuOpt.addSeparator ();
		mnuOpt.add (style);
		mnuOpt.addSeparator ();
		mnuOpt.add (theme);

		//Window Menu Items.
		mnuWin.add (close);
		mnuWin.add (closeAll);

		//Help Menu Items.
		mnuHelp.add (keyHelp);
		mnuHelp.addSeparator ();
		mnuHelp.add (about);

		//Adding Menues to Bar.
		bar.add (mnuFile);
		bar.add (mnuEdit);
		bar.add (mnuView);
		bar.add (mnuOpt);
		bar.add (mnuWin);
		bar.add (mnuHelp);

		//MenuItems for PopupMenu.
		open = new JMenuItem ("Open New Account", new ImageIcon ("Images/Open.gif"));
		open.addActionListener (this);
		report = new JMenuItem ("Print BankSystem Report", new ImageIcon ("Images/New.gif"));
		report.addActionListener (this);
		dep = new JMenuItem ("Deposit Money");
		dep.addActionListener (this);
		with = new JMenuItem ("Withdraw Money");
		with.addActionListener (this);


		//Adding Menues to PopupMenu.
		popMenu.add (open);
		popMenu.add (report);
		popMenu.add (dep);
		popMenu.add (with);

		//Following Procedure display the PopupMenu of Program.
		addMouseListener (new MouseAdapter () {
			public void mousePressed (MouseEvent me) { checkMouseTrigger (me); }
			public void mouseReleased (MouseEvent me) { checkMouseTrigger (me); }
			private void checkMouseTrigger (MouseEvent me) {
				if (me.isPopupTrigger ())
					popMenu.show (me.getComponent (), me.getX (), me.getY ());
			}
		}
		);
		
		// Creating the ToolBar's Buttons of Program.
		btnNew = new JButton (new ImageIcon ("Images/note1.png"));
		btnNew.setToolTipText ("Create New Account");
		btnNew.addActionListener (this);
		btnDep = new JButton (new ImageIcon ("Images/deposit1.png"));
		btnDep.setToolTipText ("Deposit Money");
		btnDep.addActionListener (this);
		btnWith = new JButton (new ImageIcon ("Images/withdraw1.png"));
		btnWith.setToolTipText ("Withdraw Money");
		btnWith.addActionListener (this);
		btnRec = new JButton (new ImageIcon ("Images/print1.png"));
		btnRec.setToolTipText ("Print Customer Balance");
		btnRec.addActionListener (this);
		btnKey = new JButton (new ImageIcon ("Images/interest.png"));
		btnKey.setToolTipText ("Shortcut Keys of BankSystem");
		btnKey.addActionListener (this);

		//Creating the ToolBar of Program.
		//For Program's ToolBar.
		JToolBar toolBar = new JToolBar();
		toolBar.add (btnNew);
		toolBar.addSeparator ();
		toolBar.add (btnDep);
		toolBar.add (btnWith);
		toolBar.addSeparator ();
		toolBar.add (btnRec);
		toolBar.addSeparator ();
		toolBar.add (btnKey);

		//Creating the StatusBar of Program.
		JLabel author = new JLabel(" " + "BankStack Pvt Limited", SwingConstants.CENTER);
		author.setForeground (Color.black);
		author.setToolTipText ("Program's Title");
		//Labels for Displaying Program's Name & saying Welcome to Current User on StatusBar.
		//Getting the Current System Date.
		Date currDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
		String d = sdf.format(currDate);
		JLabel welcome = new JLabel("Welcome Today is " + d + " ", JLabel.RIGHT);
		welcome.setForeground (Color.black);
		welcome.setToolTipText ("Welcoming the User & System Current Date");
		//Main Form StatusBar where Program's Name & Welcome Message Display.
		JPanel statusBar = new JPanel();
		statusBar.setLayout (new BorderLayout());
		statusBar.add (author, BorderLayout.WEST);
		statusBar.add (welcome, BorderLayout.EAST);

		//For Making the Dragging Speed of Internal Frames Faster.
		desktop.putClientProperty ("JDesktopPane.dragMode", "outline");

		//Setting the Contents of Programs.
		getContentPane().add (toolBar, BorderLayout.NORTH);
		getContentPane().add (desktop, BorderLayout.CENTER);
		getContentPane().add (statusBar, BorderLayout.SOUTH);

		//Showing The Main Form of Application.
		setVisible (true);

	}

	//Function For Performing different Actions By Menus of Program.

	public void actionPerformed (ActionEvent ae) {
	
		Object obj = ae.getSource();

		if (obj == addNew || obj == open || obj == btnNew) {
			boolean b = openChildWindow ("Create New Account");
			if (!b) {
				NewAccount newAcc = null;
				newAcc = new NewAccount();
				desktop.add (newAcc);
				newAcc.show ();
			}
		}
		else if (obj == printRec || obj == btnRec || obj == report) {
			getAccountNo ();
		}
		else if (obj == end) {
			quitApp ();
		}
		else if (obj == deposit || obj == dep || obj == btnDep) {
			boolean b = openChildWindow ("Deposit Money");
			if (!b) {
				DepositMoney depMon = new DepositMoney ();
				desktop.add (depMon);
				depMon.show ();
			}
		}
		else if (obj == withdraw || obj == with || obj == btnWith) {
			boolean b = openChildWindow ("Withdraw Money");
			if (!b) {
				WithdrawMoney withMon = new WithdrawMoney ();
				desktop.add (withMon);
				withMon.show ();
			}
		}
		else if (obj == change) {
			Color cl = new Color (153, 153, 204);
			cl = JColorChooser.showDialog (this, "Choose Background Color", cl);
			if (cl == null) { }
			else {
				desktop.setBackground (cl);
				desktop.repaint ();
			}

		}
		else if (obj == close) {
			try {
				desktop.getSelectedFrame().setClosed(true);
			}
			catch (Exception ignored) { }

		}
		else if (obj == closeAll) {
			JInternalFrame[] Frames = desktop.getAllFrames (); //Getting all Open Frames.
			for (JInternalFrame frame : Frames) {
				try {
					frame.setClosed(true); //Close the frame.
				}
				catch (Exception ignored) {
				}    //if we can't close it then we have a problem.
			}

		}

		else if (obj == keyHelp || obj == btnKey) {

			String msg = "records updated\n\n" + "\n";
			JOptionPane.showMessageDialog (this, msg, "About BankSystem", JOptionPane.PLAIN_MESSAGE);
			UpdateRecords ob = new UpdateRecords();
			ob.update();
		}

		else if (obj == about) {

			String msg = "Punjab and Maharashtra Cooperative Bank\n\n" + "\n";
			JOptionPane.showMessageDialog (this, msg, "About BankSystem", JOptionPane.PLAIN_MESSAGE);
		}
	}

	// Function Perform By LookAndFeel Menu.
	public void itemStateChanged (ItemEvent e) {
		for( int i = 0; i < radio.length; i++ )
			if(radio[i].isSelected()) {
				changeLookAndFeel (i);
			}

	}	

	// Function For Closing the Program.

	private void quitApp () {

		try {
			//Show a Confirmation Dialog.
		    	int reply = JOptionPane.showConfirmDialog (this,
					"Are you really want to exit\nFrom BankSystem?",
					"BankSystem - Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			//Check the User Selection.
			if (reply == JOptionPane.YES_OPTION) {
				setVisible (false);	//Hide the Frame.
				dispose();            	//Free the System Resources.
				System.exit (0);        //Close the Application.
			}
			else if (reply == JOptionPane.NO_OPTION) {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		} 

		catch (Exception ignored) {
		}

	}

	//Function for Changing the Program's Look.

	private void changeLookAndFeel(int val) {

		try {
			UIManager.setLookAndFeel (looks[val].getClassName());
			SwingUtilities.updateComponentTreeUI (this);
		}
		catch (Exception ignored) { }

	}

	//Loop Through All the Opened JInternalFrame to Perform the Task.

	private boolean openChildWindow (String title) {

		JInternalFrame[] childs = desktop.getAllFrames ();
		for (JInternalFrame child : childs) {
			if (child.getTitle().equalsIgnoreCase(title)) {
				child.show();
				return true;
			}
		}
		return false;

	}

	// Following Functions use for Printing Records & Report of BankSystem.

	private void getAccountNo() {

		fillDbs();
		String printing;
		String email;
		try {
			printing = JOptionPane.showInputDialog (this, "Enter Account No. to Print Customer Balance.\n" +
			"(Tip: Account No. Contains only Digits)", "BankSystem - PrintRecord", JOptionPane.PLAIN_MESSAGE);
			if (printing.equals ("")) {
				JOptionPane.showMessageDialog (this, "Provide Account No. to Print.",
					 "BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				getAccountNo ();
			}
			else {
				email = JOptionPane.showInputDialog (this, "Enter email.\n", "BankSystem - PrintRecord", JOptionPane.PLAIN_MESSAGE);
				makeRecordPrint (Integer.parseInt(printing),email);
			}
		}
		catch (Exception ignored) { }

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

	//Function use to make Current Record ready for Print.

	private void makeRecordPrint(int rec ,String email) {

		Customer X=null;

		List<Customer> customers=dbs.getCustomers();
		for (Customer x: customers) {
			if (x.getAccountNumber() == rec) {
				X=x;
				break;
			}
		}

		String data;
		String data0 = "               Punjab and Maharashtra Cooperative Bank.               \n";	
		String data1 = "               Customer Balance Report.              \n\n";	
		String data2 = "  Account No.:       " + X.getAccountNumber() + "\n";
		String data3 = "  Customer Name:     " + X.getName() + "\n";
		Transaction[] transactions= X.getTransactions();
		String data4="\t \t Transaction list \n";
		data4 += " -----------------------------------------------------------\n";
		data4 += " -----------------------------------------------------------\n";

		for (int i = 0; i < transactions.length; i++) {
			// data4+=transactions[i]+"\n";
			data4 += "Date: " + transactions[i].getD() + "\nTransaction Amount: " +  transactions[i].getAmount() + "\nUpdated Balance " + transactions[i].getNewBalance() + "\n";
			data4 += " -----------------------------------------------------------\n";
		}
		String data5 = "  Current Balance:   " + X.getBalance() + "\n\n";
		String data6 = "\n";	// Page Footer.
		String sep0 = " -----------------------------------------------------------\n";
		String sep1 = " -----------------------------------------------------------\n";
		String sep2 = " -----------------------------------------------------------\n";
		String sep3 = " -----------------------------------------------------------\n";
		String sep4 = " -----------------------------------------------------------\n\n";

		data = data0 + sep0 + data1 + data2 + sep1 + data3 + sep2 + sep2 + data4 + sep3 + data5 + sep4 + data6;
		printRecord (data);
		JavaMailUtil.sendEmail (email,data);
	}

	// Function use to Print the Current Record.

	private void printRecord(String rec) {

		StringReader sr = new StringReader (rec);
		LineNumberReader lnr = new LineNumberReader (sr);
		Font typeface = new Font ("Times New Roman", Font.PLAIN, 12);
		Properties p = new Properties ();
		PrintJob pJob = getToolkit().getPrintJob (this, "Print Customer Balance Report", p);

		if (pJob != null) {
			Graphics gr = pJob.getGraphics ();
			if (gr != null) {
				FontMetrics fm = gr.getFontMetrics (typeface);
				int margin = 20;
				int pageHeight = pJob.getPageDimension().height - margin;
    				int fontHeight = fm.getHeight();
	    			int fontDescent = fm.getDescent();
    				int curHeight = margin;
				String nextLine;
				gr.setFont (typeface);

				try {
					do {
						nextLine = lnr.readLine ();
						if (nextLine != null) {         
							if ((curHeight + fontHeight) > pageHeight) {	//New Page.
								assert gr != null;
								gr.dispose();
								gr = pJob.getGraphics ();
								curHeight = margin;
							}							
							curHeight += fontHeight;
							if (gr != null) {
								gr.setFont (typeface);
								gr.drawString (nextLine, margin, curHeight - fontDescent);
							}
						}
					}
					while (nextLine != null);					
				}
				catch (Throwable ignored) { }
			}
			assert gr != null;
			gr.dispose();
		}
		if (pJob != null)
			pJob.end ();
	}

}
