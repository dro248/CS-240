package client.views.mainWindow;

import javax.swing.*;


@SuppressWarnings("serial")
public class WindowMenuBar extends JMenuBar
{
	private JMenu fileMenu;
	private JMenuItem downloadBatch;
	private JMenuItem logout;
	private JMenuItem exit;
	
	public WindowMenuBar()
	{	
		initialize();
		
		fileMenu.add(downloadBatch);
		fileMenu.add(logout);
		fileMenu.add(exit);
		add(fileMenu);
	}
	
	void initialize()
	{
		fileMenu 		= new JMenu("File");
		downloadBatch 	= new JMenuItem("Download Batch");
		logout 			= new JMenuItem("Logout");
		exit 			= new JMenuItem("Exit");
	}
	
}