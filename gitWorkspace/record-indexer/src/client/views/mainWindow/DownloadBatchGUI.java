package client.views.mainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.facade.BatchState;
import client.facade.ClientFacade;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;
import client.views.mainWindow.top.ImageViewer;
import shared.communication.*;
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
	private List<Project> 		projectsList;
	private DownloadBatchGUI	thisDialog = this;
	private ImageIcon 			myImage;
	private Indexer				indexerWindow;			// reference to indexer window
	private ImageViewer			imageViewer;
	private String				HOST;
	private int					PORT;
	private BatchState 			batchState;				// this references the batchState in the ClientFacade
	
	public DownloadBatchGUI(Indexer indexer)
	{
		this.indexerWindow = indexer;
		initialize();
		createComponents();		
	}
	
	public void initialize()
	{
		HOST 				= ClientFacade.get().getHost();
		PORT				= ClientFacade.get().getPort();
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
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{				
				String projectName 	= (String) projectsComboBox.getSelectedItem();
				int projectID		= getIDFromProjectName(projectName);
				ImgParams params 	= new ImgParams(batchState.getUser().getUsername(),
													batchState.getUser().getPassword(), 
													projectID);
				String imageURL_str	= ClientFacade.get().getSampleImage(params).getUrl();
				
				try 
				{
					// Create URL
					URL imageURL = new URL("http://" + HOST + ":" + PORT + "/Records/" +  imageURL_str);
					
					// Create ImageIcon
					myImage = new ImageIcon(imageURL);
					
					// Create SampleImage dialog
					JOptionPane sampleImageDialog = new JOptionPane("Sample image from " + projectName);
					sampleImageDialog.showMessageDialog(null, myImage, "Sample Image", JOptionPane.PLAIN_MESSAGE);
				} 
				catch (MalformedURLException e) 
				{
					System.out.println("ERROR[DownloadBatchGUI.viewSampleButton.addActionListener]: \nCould not convert String to url");
				}		
			}
		});
		
		downloadButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				// 1. Download Batch
				String projectName 	= (String) projectsComboBox.getSelectedItem();
				int projectID		= getIDFromProjectName(projectName);
				
				DownloadBatchParams d_params = new DownloadBatchParams(
												batchState.getUser().getUsername(),
												batchState.getUser().getPassword(), 
												projectID);
				
				DownloadBatchResult results = ClientFacade.get().downloadBatch(d_params);
				
				
				// 2. Make downloadBatchGUI disappear
				thisDialog.setVisible(false);
				thisDialog.setModal(false);
				
				
				// 3. get image URL
				String imageURL_str	= results.getBatch().getUrl();
				
				try 
				{
					// Create URL
					URL imageURL = new URL("http://" + HOST + ":" + PORT + "/Records/" +  imageURL_str);

					// 4. Pass Batch values to BatchState
					batchState.setProject(results.getProject());
					batchState.setBatch(results.getBatch());
					batchState.setFields(results.getFields());
					batchState.setURL(imageURL);		
					
					// 5. Create necessary components
					{
						// Create ImageViewer
						indexerWindow.getVSplitPane().remove(1);
						imageViewer = new ImageViewer(imageURL);
						indexerWindow.getVSplitPane().setTopComponent(imageViewer);
						indexerWindow.setImageViewer(imageViewer);
						
						// Initialize cellValues in batchState
						batchState.initCellValues();
												
						// Scrap the Table and Form tabs so that we can recreate them
						if(indexerWindow.getHSplitPane().getRightComponent() != null &&
							indexerWindow.getHSplitPane().getLeftComponent() != null)
						{
//							indexerWindow.getHSplitPane().remove(0);
							indexerWindow.getHSplitPane().remove(1);
						}
						
						// create JTabbedPanes with table & form in it
						{
							JTabbedPane tabbedPanel = new JTabbedPane();
							
							// Table
							DataTable dataTable = new DataTable();
							indexerWindow.setDataTable(dataTable);
							
							// Form							
							DataForm dataForm	= new DataForm();
							indexerWindow.setDataForm(dataForm);
							
							tabbedPanel.addTab("Table Entry", new JScrollPane(dataTable));
							tabbedPanel.addTab("Form Entry", new JScrollPane(dataForm));
							indexerWindow.getHSplitPane().setLeftComponent(tabbedPanel);
							
							// Listener for TabbedPanel
							tabbedPanel.addChangeListener(new ChangeListener()
							{
								@Override
								public void stateChanged(ChangeEvent e) 
								{									
									if(indexerWindow.getDataForm() != null && indexerWindow.getDataTable() != null)
									{
										indexerWindow.getDataForm().refreshTabSelection();
										indexerWindow.getDataTable().refreshTabSelection();
									}
								}
								
							});
						}
						
						// set FieldHelp
						JTabbedPane 		 rightTabbedPanel 	= new JTabbedPane();
						FieldHelpPanel 		 fieldHelp 			= new FieldHelpPanel();
						ImageNavigationPanel imageNavigation 	= new ImageNavigationPanel(batchState.getURL());
						
						rightTabbedPanel.addTab("Field Help", new JScrollPane(fieldHelp));
//						rightTabbedPanel.addTab("Image Navigation", new JScrollPane(imageNavigation));
						rightTabbedPanel.addTab("Image Navigation", imageNavigation);
						
						indexerWindow.getHSplitPane().setRightComponent(rightTabbedPanel);
						indexerWindow.getVSplitPane().setBottomComponent(indexerWindow.getHSplitPane());
						
						URL helpURL = new URL("http://" + HOST + ":" + PORT + "/Records/" + batchState.getFields().get(0).getHelpHTML());
						try { indexerWindow.getFieldHelpContent().setPage(helpURL); } 
						catch (Exception e) {}
					}
					// 6. Set buttons Enabled/Disabled
					batchState.setButtonAvailability();
				} 
				catch (MalformedURLException e) 
				{
					System.out.println("ERROR[DownloadBatchGUI.downloadBatchButton.addActionListener]: \nCould not convert String to url");
				}		
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