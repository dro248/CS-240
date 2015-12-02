package client.views.mainWindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.*;

import client.facade.BatchState;
import client.facade.ClientFacade;
import shared.communication.ImgParams;
import shared.communication.ProjectParams;
import shared.communication.ProjectResult;
import shared.model.Project;

@SuppressWarnings("serial")
public class DownloadBatchGUI extends JDialog
{
	private JPanel 				rootPanel;
	private JPanel 				topPanel;
	private JPanel 				bottomPanel;
	private JLabel 				projectLabel;
	private JComboBox<String> 	projectsComboBox;		// This might not be of type String
	private JButton 			viewSampleButton;
	private JButton 			cancelButton;
	private JButton 			downloadButton;
	private BatchState 			batchState;				// this references the batchState in the ClientFacade
	private List<Project> 		projectsList;
//	private SampleImageViewer 	sampleImageDialog;
	private DownloadBatchGUI	thisDialog = this;
	private ImageIcon 			myImage;
	
	public DownloadBatchGUI()
	{
		initialize();
		createComponents();		
	}
	
	public void initialize()
	{
		batchState 			= ClientFacade.get().getBatchState();
		rootPanel			= new JPanel();
		topPanel 			= new JPanel();
		bottomPanel 		= new JPanel();
		projectLabel 		= new JLabel("Project: ");
		projectsComboBox	= new JComboBox<String>();
		viewSampleButton 	= new JButton("View Sample");
		cancelButton 		= new JButton("Cancel");
		downloadButton 		= new JButton("Download");	
	}
	
	public void createComponents()
	{
		this.setTitle("Download Batch");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setModal(true);
		
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		
		// Populate projectsComboBox with Projects
		populateComboBox();
		
		
		topPanel.add(projectLabel);
		topPanel.add(projectsComboBox);
		topPanel.add(viewSampleButton);
		bottomPanel.add(cancelButton);
		bottomPanel.add(downloadButton);
		rootPanel.add(topPanel);
		rootPanel.add(bottomPanel);
		
		this.add(rootPanel);
		
		
		// Listeners
		cancelButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				setVisible(false);
				setModal(false);
			}
		});
		
		viewSampleButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				String HOST = ClientFacade.get().getHost();
				int PORT	= ClientFacade.get().getPort();
				JPanel rootPanel = new JPanel();
				
				String projectName 	= (String) projectsComboBox.getSelectedItem();
				int projectID		= getIDFromProjectName(projectName);
				ImgParams params 	= new ImgParams(batchState.getUser().getUsername(),	batchState.getUser().getPassword(), projectID);
				String imageURL_str	= ClientFacade.get().getSampleImage(params).getUrl();
//				imageURL_str		= /*"http://" + HOST + ":" + PORT + "/" + imageURL_str;*/
				
//				System.out.println("http://" + HOST + ":" + PORT + "/" + imageURL_str);
				
				try 
				{ 
					URL imageURL = new URL("http://" + HOST + ":" + PORT + "/demo/indexer_data/Records/" + imageURL_str);
					
					myImage = new ImageIcon(imageURL);
					
					JOptionPane sampleImageDialog = new JOptionPane("Sample image from " + projectName);
					sampleImageDialog.showMessageDialog(null, myImage, "title txt", JOptionPane.PLAIN_MESSAGE);
//					sampleImageDialog.setPreferredSize(new Dimension(500,500));
//					sampleImageDialog.add(rootPanel);
				} 
				catch (MalformedURLException e) 
				{
					System.out.println("Could not convert String -> url");
				}
//				
				
//				sampleImageDialog	= new SampleImageViewer(imageURL, thisDialog);
				
				
			}
		});
		
		this.pack();
	}
	
	
	private void populateComboBox()
	{
		ProjectResult myProjects = ClientFacade.get().getProjects(new ProjectParams(
				batchState.getUser().getUsername(), batchState.getUser().getPassword()));
		
		if(myProjects.getProjects().isEmpty()) { return; }

		projectsList = myProjects.getProjects();
		
		// Now that we have a list of all of our projects... let's populate the comboBox
		
		for(Project p : projectsList)
		{
			projectsComboBox.addItem(p.getTitle());
		}
	}
	
	private int getIDFromProjectName(String projectName)
	{
		if(projectsList.isEmpty() || projectsList == null)
			{ return -2; }
		
		for(Project p : projectsList)
		{
			if(p.getTitle().equals(projectName))
			{ 
				return p.getID();
			}	
		}
		
		return -1;
	}
	
}