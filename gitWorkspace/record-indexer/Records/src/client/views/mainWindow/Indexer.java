package client.views.mainWindow;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import shared.communication.SubmitBatchParams;
import client.views.LoginGUI;
import client.views.mainWindow.bottom.left.form.*;
import client.views.mainWindow.bottom.left.table.*;
import client.views.mainWindow.bottom.right.*;
import client.views.mainWindow.top.ImageViewer;
import client.facade.*;

@SuppressWarnings("serial")
public class Indexer extends JFrame implements BatchStateListener
{
	private JMenuBar 				menuBar;
	private JPanel 					buttonBar;
	private JButton 				zoomInButton;
	private JButton					zoomOutButton;
	private JButton 				invertButton;
	private JButton 				toggleButton;
	private JButton 				saveButton;
	private JButton 				submitButton;
	private JSplitPane 				v_SplitPane;
	private JSplitPane 				h_SplitPane;
	private ImageViewer				imageViewer;
	private JTabbedPane 			leftTabbedPane;
	private JTabbedPane 			rightTabbedPane;
	private TableEntryPanel			tableEntry;
	private FormEntryPanel			formEntry;
	private DataTable				dataTable;
	private DataForm				dataForm;
	private JScrollPane				fieldScrollPane;
	private FieldHelpPanel			fieldHelp;
	private ImageNavigationPanel 	imageNavigation;
	private BatchState 				batchState;			// this is the reference to the batchState within the clientFacade
	private LoginGUI 				loginWindow;		// this is the reference to the LoginGUI
//	private	Indexer					thisDialog = this;
	
	public Indexer(String title)
	{
		super(title);
	}
	
	public void initialize()
	{
		// Set batchState
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
		
		menuBar 		= new WindowMenuBar(this, loginWindow);
		buttonBar 		= new JPanel();
		zoomInButton	= new JButton("Zoom In");
		zoomOutButton	= new JButton("Zoom Out");
		invertButton	= new JButton("Invert Image");
		toggleButton	= new JButton("Toggle Highlights");
		saveButton		= new JButton("Save");
		submitButton	= new JButton("Submit");
		v_SplitPane 	= new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		h_SplitPane		= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		imageViewer		= new ImageViewer(null);
		leftTabbedPane 	= new JTabbedPane();
		rightTabbedPane	= new JTabbedPane();
		fieldHelp		= new FieldHelpPanel();
		fieldScrollPane = new JScrollPane(fieldHelp);
		
		createComponents();
		setButtonAvailability();
	}
	
	private void createComponents()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(batchState.getWindowPosition().get_X(), batchState.getWindowPosition().get_X());
		this.setPreferredSize(batchState.getWindowSize());
		this.setLayout(new BorderLayout());
		
		// Set MenuBar
		setJMenuBar(menuBar);
		
		// Create buttonBar
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));		
		buttonBar.add(zoomInButton);
		buttonBar.add(zoomOutButton);
		buttonBar.add(invertButton);
		buttonBar.add(toggleButton);
		buttonBar.add(saveButton);
		buttonBar.add(submitButton);
		this.add(buttonBar, BorderLayout.NORTH);
		
		
		// setup TabbedPanes
		leftTabbedPane.addTab("Table Entry", tableEntry);
		leftTabbedPane.addTab("Form Entry", formEntry);
		
		rightTabbedPane.addTab("Field Help", fieldScrollPane);
		rightTabbedPane.addTab("Image Navigation", imageNavigation);
		
		// setup Horizontal splitPane
		h_SplitPane.setLeftComponent(leftTabbedPane);
		h_SplitPane.setRightComponent(rightTabbedPane);
		h_SplitPane.setDividerLocation(batchState.getHPaneDivPosition());
		h_SplitPane.setResizeWeight(.5);
		
		// setup Vertical splitPane
		v_SplitPane.setBackground(Color.GRAY);
		v_SplitPane.setTopComponent(imageViewer);
		v_SplitPane.setBottomComponent(h_SplitPane);
		v_SplitPane.setDividerLocation(batchState.getVPaneDivPosition());
		v_SplitPane.setResizeWeight(.7);
		
		this.add(v_SplitPane, BorderLayout.CENTER);
		
		
		
		
		
		
		
		// LISTENERS		
		zoomInButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				imageViewer.scaleImage(1.25);
			}
		});
		
		zoomOutButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				imageViewer.scaleImage(.75);
			}
		});
		
		invertButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				imageViewer.invertImage();
			}
		});
		
		toggleButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				imageViewer.toggleRectColor();
			}
		});
		
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				batchState.save();
			}
		});
		
		submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// 1. Properly submit information to server
				SubmitBatchParams params = new SubmitBatchParams(
						batchState.getUser().getUsername(),
						batchState.getUser().getPassword(), 
						batchState.getBatch().getID(), 
						batchState.cellValues_toString());

				ClientFacade.get().submitBatch(params);
				
				// empty batchState
				batchState.empty();
//				v_SplitPane.setTopComponent(new ImageViewer(null));
				
				
				// do what you did in downloadBatchGUI
				v_SplitPane.remove(0);
				v_SplitPane.remove(1);
				imageViewer = new ImageViewer(null);
				v_SplitPane.setTopComponent(imageViewer);
				
				// Scrap bottom components
//				if(h_SplitPane != null)
//				{
//					h_SplitPane.remove(0);
//					h_SplitPane.remove(1);
//				}
				
				JTabbedPane leftTabbedPanel = new JTabbedPane();
				JTabbedPane rightTabbedPanel = new JTabbedPane();
				
				leftTabbedPanel.addTab("Table Entry", new JScrollPane());
				leftTabbedPanel.addTab("Form Entry", new JScrollPane());
				
				rightTabbedPanel.addTab("Field Help", new JScrollPane());
				rightTabbedPanel.addTab("Image Navigation", new JScrollPane());
				
				h_SplitPane.setRightComponent(rightTabbedPanel);
				h_SplitPane.setLeftComponent(leftTabbedPanel);
				
				v_SplitPane.setBottomComponent(h_SplitPane);
				
				batchState.setButtonAvailability();				
			}
		});

		this.pack();
	}

	
	
	
	// Getters
	public ImageViewer  	getImageViewer()		{ return imageViewer;	}
	public JSplitPane		getVSplitPane()			{ return v_SplitPane;	}
	public JSplitPane		getHSplitPane()			{ return h_SplitPane;	}
	public TableEntryPanel	getTableEntryPanel()	{ return tableEntry;	}
	public FieldHelpPanel	getFieldHelpContent()	{ return fieldHelp;		}
	public DataForm			getDataForm()			{ return dataForm;		}
	public DataTable		getDataTable()			{ return dataTable;		}	
	
	
	// Setters
	public void	setImageViewer(ImageViewer viewer)	{ this.imageViewer = viewer; 	}
	public void	setLeftTabbedPane(JTabbedPane pane)	{ this.leftTabbedPane = pane;	}
	public void	setRightTabbedPane(JTabbedPane pane){ this.rightTabbedPane = pane;	}
	public void setDataForm(DataForm _dataForm)		{ this.dataForm = _dataForm;	}
	public void setDataTable(DataTable _dataTable)	{ this.dataTable = _dataTable;	}
	
	
	
	
	
	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	
	@Override
	public void valueChanged(int row, int column, String newValue) 
	{		
	}

	@Override
	public void selectedCellChanged(int row, int column) 
	{
	}

	public Coordinate getWindowPosition() 	
	{ 
		return new Coordinate(this.getX(), this.getY());
	}
	public Dimension getWindowSize() { return this.getSize(); }
	public int getVPaneDivPosition() { return this.v_SplitPane.getDividerLocation(); }
	public int getHPaneDivPosition() { return this.h_SplitPane.getDividerLocation(); }

	public void setLoginWindow(LoginGUI _login)	{ loginWindow = _login; }

	@Override
	public void setButtonAvailability() 
	{
		if(batchState.getBatch() == null)
		{
			zoomInButton.setEnabled(false);
			zoomOutButton.setEnabled(false);
			invertButton.setEnabled(false);
			toggleButton.setEnabled(false);
			saveButton.setEnabled(false);
			submitButton.setEnabled(false);
		}
		else
		{
			zoomInButton.setEnabled(true);
			zoomOutButton.setEnabled(true);
			invertButton.setEnabled(true);
			toggleButton.setEnabled(true);
			saveButton.setEnabled(true);
			submitButton.setEnabled(true);
		}
	}

	@Override
	public FieldHelpPanel getFieldHelp()
	{
		return getFieldHelpContent();
	}

	@Override
	public ImageNavigationPanel getImageNavigation()
	{
		return null;
	}

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
