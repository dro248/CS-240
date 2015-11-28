package client.views;

import client.facade.ClientFacade;
import client.views.mainWindow.Indexer;

public class Main 
{
	private static String HOST;
	private static int PORT;
	
	public static void main(String[] args) 
	{
		HOST = args[0];
		PORT = Integer.parseInt(args[1]);
		
		
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
            	// setup clientFacade
            	ClientFacade.get().setHost(HOST);
            	ClientFacade.get().setPort(PORT);
            	
            	Indexer indexerFrame 	= new Indexer("Indexer", null);
            	indexerFrame.createComponents();
            	
            	LoginGUI loginFrame = new LoginGUI("Login to GUI", indexerFrame);
            	indexerFrame.pack();
                indexerFrame.setVisible(false);
                
                loginFrame.pack();
                loginFrame.setVisible(true);
            }
        });
    }
}
