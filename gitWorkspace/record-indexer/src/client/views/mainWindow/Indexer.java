package client.views.mainWindow;

import java.awt.*;

import javax.swing.*;

import shared.model.Cell;

import com.sun.org.apache.bcel.internal.classfile.Field;

import client.views.mainWindow.bottom.left.*;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;
import client.views.mainWindow.top.ImageViewer;
import client.facade.*;

@SuppressWarnings("serial")
public class Indexer extends JFrame implements BatchStateListener
{
	JMenuBar menuBar;
	JPanel buttonBar;
	JButton zoomIn;
	JButton zoomOut;
	JButton invert;
	JButton toggle;
	JButton save;
	JButton submit;
	JSplitPane v_SplitPane;
	JSplitPane h_SplitPane;
	JComponent imageViewer;
	JTabbedPane leftTabbedPane;
	JTabbedPane rightTabbedPane;
	JPanel tableEntry;
	JPanel formEntry;
	JPanel fieldHelp;
	JPanel imageNavigation;
	BatchState batchState;
	
	public Indexer(String title, BatchState _batchState)
	{
		super(title);
		
		batchState = _batchState;
		initialize();
		createComponents();
	}
	
	void initialize()
	{
		menuBar 		= new WindowMenuBar();
		buttonBar 		= new JPanel();
		zoomIn 			= new JButton("Zoom In");
		zoomOut			= new JButton("Zoom Out");
		invert 			= new JButton("Invert Image");
		toggle			= new JButton("Toggle Highlights");
		save			= new JButton("Save");
		submit			= new JButton("Submit");
		v_SplitPane 	= new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		h_SplitPane		= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		imageViewer		= new ImageViewer();
		leftTabbedPane 	= new JTabbedPane();
		rightTabbedPane	= new JTabbedPane();
		tableEntry		= new TableEntryPanel();
		formEntry		= new FormEntryPanel();
		fieldHelp		= new FieldHelpPanel();
		imageNavigation = new ImageNavigationPanel();
	}
	
	public void createComponents()
	{
		this.setLocationRelativeTo(null);
		this.setPreferredSize(new Dimension(1000, 700));
		this.setLayout(new BorderLayout());
		
		// Set MenuBar
		setJMenuBar(menuBar);
		
		// Create buttonBar
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));		
		buttonBar.add(zoomIn);
		buttonBar.add(zoomOut);
		buttonBar.add(invert);
		buttonBar.add(toggle);
		buttonBar.add(save);
		buttonBar.add(submit);
		this.add(buttonBar, BorderLayout.NORTH);
		
		// setup TabbedPanes
		leftTabbedPane.addTab("Table Entry", tableEntry);
		leftTabbedPane.addTab("Form Entry", formEntry);
		
		rightTabbedPane.addTab("Field Help", fieldHelp);
		rightTabbedPane.addTab("Image Navigation", imageNavigation);
		
		
		// setup Horizontal splitPane
		h_SplitPane.setLeftComponent(leftTabbedPane);
		h_SplitPane.setRightComponent(rightTabbedPane);
		
		// setup Vertical splitPane
		v_SplitPane.setTopComponent(imageViewer);
		v_SplitPane.setBottomComponent(h_SplitPane);
		
		v_SplitPane.setOneTouchExpandable(true);
		v_SplitPane.setDividerLocation(150);
		
		this.add(v_SplitPane, BorderLayout.CENTER);
		
		
		
		this.pack();
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
}