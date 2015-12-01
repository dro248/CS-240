package client.views.mainWindow;

import javax.swing.*;

@SuppressWarnings("serial")
public class downloadBatchGUI extends JDialog
{
	private JPanel rootPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JLabel projectLabel;
	private JComboBox<String> projectsComboBox;		// This might not be of type String
	private JButton viewSampleButton;
	private JButton cancelButton;
	private JButton downloadButton;
	
	
	public downloadBatchGUI()
	{
		initialize();
		createComponents();		
	}
	
	public void initialize()
	{
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
		this.pack();
	}
	
	
	private void populateComboBox()
	{
		
		// TODO: these are Test items... populate with real items
		projectsComboBox.addItem("test1");
		projectsComboBox.addItem("test2");
		projectsComboBox.addItem("test3");
		projectsComboBox.addItem("test4");
	}
	
}