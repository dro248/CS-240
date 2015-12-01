package client.views.mainWindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import shared.communication.*;
import shared.model.*;
import client.facade.*;
import client.views.LoginGUI;


@SuppressWarnings("serial")
public class WindowMenuBar extends JMenuBar implements BatchStateListener
{
	private JMenu 				fileMenu;
	private JMenuItem 			downloadBatchButton;
	private JMenuItem 			logoutButton;
	private JMenuItem 			exitButton;
	private BatchState 			batchState;	// this references the batchState in the ClientFacade
	private Indexer 			indexerWindow;
	private LoginGUI 			loginWindow;
	private downloadBatchGUI	downloadDialog;
	
	public WindowMenuBar(Indexer _indexer, LoginGUI _login)
	{	
		initialize();
		batchState 		= ClientFacade.get().getBatchState();
		batchState.addListener(this);
		indexerWindow	= _indexer;
		loginWindow 	= _login;
		
		fileMenu.add(downloadBatchButton);
		fileMenu.add(logoutButton);
		fileMenu.add(exitButton);
		add(fileMenu);
		
		setButtonAvailability();
		
		// LISTENERS
		downloadBatchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				downloadDialog.setVisible(true);
				
				
				// this doesn't go here! It goes in "downloadBatchGUI.java"
				/*ProjectResult myProjects = ClientFacade.get().getProjects(
					new ProjectParams(batchState.getUser().getUsername(), batchState.getUser().getPassword()));*/
			}
		});
		
		
		logoutButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				batchState.save();
				indexerWindow.setVisible(false);
				loginWindow.clear();
				loginWindow.setVisible(true);
			}
		});
		
		exitButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				batchState.save();
				System.exit(0);
			}
		});
	}
	
	void initialize()
	{
		downloadDialog		= new downloadBatchGUI();
		downloadDialog.setVisible(false);
		
		fileMenu 			= new JMenu("File");
		downloadBatchButton = new JMenuItem("Download Batch");
		logoutButton 		= new JMenuItem("Logout");
		exitButton 			= new JMenuItem("Exit");
	}
	
	@Override
	public void valueChanged(Cell cell, String newValue)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selectedCellChanged(Cell newSelectedCell)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public Coordinate getWindowPosition()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Dimension getWindowSize()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getVPaneDivPosition() { return 0; }
	@Override
	public int getHPaneDivPosition() { return 0; }

	@Override
	public void setButtonAvailability()
	{
		if(batchState.getBatch() == null)
			{ downloadBatchButton.setEnabled(true); }
		else
			{ downloadBatchButton.setEnabled(false); }
	}

}