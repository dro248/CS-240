package client.views;

import static servertester.views.Constants.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import server.handler.ValidateUserHandler;
import shared.communication.UserParams;
import shared.communication.UserResult;
import shared.model.User;
import client.communication.ClientCommunicator;
import client.facade.ClientFacade;
import client.views.mainWindow.Indexer;


@SuppressWarnings("serial")
public class LoginGUI extends JFrame
{
	private JPanel 			rootPanel;
	private JPanel 			usernamePanel;
	private JPanel 			passwordPanel;
	private JPanel 			buttonPanel;
	private JLabel 			usernameLabel;
	private JLabel 			passwordLabel;
	private JTextField 		usernameField;
	private JPasswordField 	passwordField;
	private JButton 		loginButton;
	private JButton 		exitButton;
	private Indexer 		indexerFrame;
	
	
	
	public LoginGUI(String title, Indexer _indexerFrame)
	{
		super(title);
		
		indexerFrame = _indexerFrame;
		createComponents();
	}
	
	private void createComponents()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(300, 100));
		setLocationRelativeTo(null);
		setResizable(false);
		
		rootPanel 		= new JPanel();
		usernamePanel 	= new JPanel(); 
		passwordPanel 	= new JPanel(); 
		buttonPanel 	= new JPanel(); 
		
		usernameLabel 	= new JLabel("username: ");
		usernameField 	= new JTextField();
		usernameField.setMaximumSize(new Dimension(getWidth(), 25));
		
		passwordLabel 	= new JLabel("password: ");
		passwordField 	= new JPasswordField();
		passwordField.setMaximumSize(new Dimension(getWidth(), 25));
		
		loginButton 	= new JButton("Login");
		exitButton		= new JButton("Exit");
		
		
		// SET LAYOUT
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameField);
		
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		
		buttonPanel.add(loginButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,5)));
		buttonPanel.add(exitButton);
		
		
		rootPanel.add(new JPanel());
		rootPanel.add(usernamePanel);
		rootPanel.add(new JPanel());
		rootPanel.add(passwordPanel);
		rootPanel.add(new JPanel());
		rootPanel.add(buttonPanel);
		rootPanel.add(new JPanel());
		
		this.add(rootPanel);

		loginButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					String password = new String(passwordField.getPassword());
					UserParams params = new UserParams(usernameField.getText(), password);
					UserResult result = ClientFacade.get().validateUser(params);
					User user = result.getUser();
					
					createGoodDialog(user);
				}
				catch(Exception e) { createBadDialog(); }
			}
		});
		
		exitButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		

        pack();
		setVisible(true);
	}
	
	private void createGoodDialog(User user)
	{
		// Create Good Dialog
		final JDialog goodDialog = new JDialog();
		goodDialog.setTitle("Welcome to Indexer");
		goodDialog.setLocationRelativeTo(null);
		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		
		JLabel topLabel 	= new JLabel("Welcome, " + user.getFirstname() + " " + user.getLastname() + ".");
		JLabel bottomLabel 	= new JLabel("You have indexed " + user.getRecordsIndexed() + " records.");
		
		JPanel buttonPanel	= new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton okButton 	= new JButton("OK");
		
		rootPanel.add(topLabel);
		rootPanel.add(bottomLabel);
		rootPanel.add(new JPanel());
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(okButton);
		buttonPanel.add(new JPanel());
		
		rootPanel.add(buttonPanel);
		
		
		
		goodDialog.add(rootPanel);
		
		goodDialog.pack();
		goodDialog.setVisible(true);
		
		this.setVisible(false);
		
		okButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				goodDialog.setVisible(false);
				
				indexerFrame.initialize();
				indexerFrame.setVisible(true);
			}
		});
	}
	
	private void createBadDialog()
	{
		// Create Good Dialog
		final JDialog badDialog = new JDialog();
		badDialog.setTitle("Login Failed");
		badDialog.setLocationRelativeTo(null);
		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		
		JLabel topLabel 	= new JLabel("Invalid username and/or password");
		
		JPanel buttonPanel	= new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton okButton 	= new JButton("OK");
		
		rootPanel.add(topLabel);
		rootPanel.add(new JPanel());
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(okButton);
		buttonPanel.add(new JPanel());
		
		rootPanel.add(buttonPanel);

		badDialog.add(rootPanel);
		badDialog.pack();
		badDialog.setVisible(true);
		
		this.setVisible(false);
		
		final LoginGUI mainframe = this;
		
		okButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				badDialog.setVisible(false);
				
				mainframe.setVisible(true);
			}
		});	
	}

	public void clear()
	{
		usernameField.setText("");
		passwordField.setText("");
	}
}
