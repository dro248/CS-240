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
            	
            	Indexer indexerWindow = new Indexer("Indexer");
            	LoginGUI loginWindow = new LoginGUI("Login to GUI", indexerWindow);
            	
            	indexerWindow.setLoginWindow(loginWindow);
            	indexerWindow.pack();
                indexerWindow.setVisible(false);
                
                loginWindow.pack();
                loginWindow.setVisible(true);
            }
        });
    }
}
