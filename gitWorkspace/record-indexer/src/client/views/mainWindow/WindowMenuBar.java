package client.views.mainWindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import shared.model.Cell;
import client.facade.BatchState;
import client.facade.BatchStateListener;
import client.facade.ClientFacade;
import client.facade.Coordinate;


@SuppressWarnings("serial")
public class WindowMenuBar extends JMenuBar implements BatchStateListener
{
	private JMenu fileMenu;
	private JMenuItem downloadBatch;
	private JMenuItem logoutButton;
	private JMenuItem exit;
	private BatchState batchState;
	
	public WindowMenuBar()
	{	
		initialize();
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
		
		fileMenu.add(downloadBatch);
		fileMenu.add(logoutButton);
		fileMenu.add(exit);
		add(fileMenu);
		
		
		logoutButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	void initialize()
	{
		fileMenu 		= new JMenu("File");
		downloadBatch 	= new JMenuItem("Download Batch");
		logoutButton 	= new JMenuItem("Logout");
		exit 			= new JMenuItem("Exit");
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
	
}