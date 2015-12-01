package client.views.mainWindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import shared.communication.ProjectParams;
import shared.communication.ProjectResult;
import shared.model.Cell;
import shared.model.Project;
import client.facade.BatchState;
import client.facade.BatchStateListener;
import client.facade.ClientFacade;
import client.facade.Coordinate;
import client.views.LoginGUI;


@SuppressWarnings("serial")
public class WindowMenuBar extends JMenuBar implements BatchStateListener
{
	private JMenu 		fileMenu;
	private JMenuItem 	downloadBatchButton;
	private JMenuItem 	logoutButton;
	private JMenuItem 	exitButton;
	private BatchState 	batchState;	// this references the batchState in the ClientFacade
	private Indexer 	indexerWindow;
	private LoginGUI 	loginWindow;
	
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
				ProjectResult myProjects = ClientFacade.get().getProjects(new ProjectParams(
						batchState.getUser().getUsername(), batchState.getUser().getPassword()));
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