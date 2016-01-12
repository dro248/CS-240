package client.views.mainWindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.facade.BatchState;
import client.facade.BatchStateListener;
import client.facade.ClientFacade;
import client.facade.Coordinate;
import client.views.LoginGUI;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;


@SuppressWarnings("serial")
public class WindowMenuBar extends JMenuBar implements BatchStateListener
{
	private JMenu 				fileMenu;
	private JMenuItem 			downloadBatchButton;
	private JMenuItem 			logoutButton;
	private JMenuItem 			exitButton;
	private BatchState 			batchState;		// this references the batchState in the ClientFacade
	private Indexer 			indexerWindow;	// reference to indexer
	private LoginGUI	 		loginWindow;
	private DownloadBatchGUI 	downloadDialog;
	
	public WindowMenuBar(Indexer _indexer, LoginGUI _login)
	{	
		
		batchState 		= ClientFacade.get().getBatchState();
		batchState.addListener(this);
		indexerWindow	= _indexer;
		loginWindow 	= _login;
		initialize();
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
		downloadDialog		= new DownloadBatchGUI(indexerWindow);
		downloadDialog.setVisible(false);
		
		fileMenu 			= new JMenu("File");
		downloadBatchButton = new JMenuItem("Download Batch");
		logoutButton 		= new JMenuItem("Logout");
		exitButton 			= new JMenuItem("Exit");
	}
	
	
	
	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	
	@Override
	public void valueChanged(int row, int column, String newValue) 	{}
	@Override
	public void selectedCellChanged(int row, int column)			{}
	@Override
	public Coordinate getWindowPosition()				{ return null; }
	@Override
	public Dimension getWindowSize()					{ return null; }
	@Override
	public int getVPaneDivPosition() 					{ return 0;    }
	@Override
	public int getHPaneDivPosition() 					{ return 0;    }
	@Override
	public void setButtonAvailability()
	{
		if(batchState.getBatch() == null)
			{ downloadBatchButton.setEnabled(true); }
		else
			{ downloadBatchButton.setEnabled(false); }
	}
	@Override
	public DataTable getDataTable()						{ return null; }
	@Override
	public DataForm getDataForm()						{ return null; }
	@Override
	public FieldHelpPanel getFieldHelp()				{ return null; }
	@Override
	public ImageNavigationPanel getImageNavigation()	{ return null; }

	@Override
	public void translate(int w_X, int w_Y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScale(Double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getZoom() {
		// TODO Auto-generated method stub
		return 0;
	}
}