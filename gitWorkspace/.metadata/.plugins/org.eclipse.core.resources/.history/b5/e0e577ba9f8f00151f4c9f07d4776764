package client.views;

import java.awt.Dimension;
import javax.swing.JFrame;

public class RecordIndexer extends JFrame
{
	public RecordIndexer(String title)
	{
		super(title);
		createComponents();
	}
	
	public void createComponents()
	{
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(100, 100));
	}
	
	
	
	
	
	public static void main(String[] args) 
	{
//		HOST = args[0];
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
            	RecordIndexer indexerFrame 	= new RecordIndexer("Record Indexer");
            	indexerFrame.createComponents();
            	
            	LoginGUI loginFrame 		= new LoginGUI("Login to GUI", indexerFrame);
            	indexerFrame.pack();
                indexerFrame.setVisible(false);
                
                loginFrame.pack();
                loginFrame.setVisible(true);
            }
        });
    }
}
