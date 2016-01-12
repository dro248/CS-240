package client.views.mainWindow.bottom.left.form;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import shared.model.Field;
import client.facade.BatchState;
import client.facade.BatchStateListener;
import client.facade.ClientFacade;
import client.facade.Coordinate;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;

@SuppressWarnings("serial")
public class DataForm extends JSplitPane implements BatchStateListener, FocusListener
{
	private BatchState batchState;
	private JPanel rightPanel 				= new JPanel();
	private DefaultListModel<String> model 	= new DefaultListModel<>();
	private JList<String> recordsList 		= new JList<String>( model );
	private List<JTextField> textFields		= new ArrayList<JTextField>();
	
	public DataForm()
	{
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
		createComponents();
		
		
		// Listeners
		recordsList.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int row = recordsList.getSelectedIndex();
				
				// Refresh all values in textFields
				for(int i = 0; i < textFields.size(); i++)
				{
					textFields.get(i).setText("");
					textFields.get(i).setText(batchState.getCell(row, i));
				}
				
				// update batchState
				batchState.selectedCellChanged(row, batchState.getCellColumn());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
	}
	
	private void createComponents()
	{
		populateRecordsList();
		recordsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		// set recordsList values
		createRightPanel();

		this.setRightComponent(rightPanel);
		this.setLeftComponent(recordsList);
		this.setDividerSize(0);
		recordsList.setPreferredSize(new Dimension(50, 100));
	}
	
	private void createRightPanel()
	{	
		for(Field f : batchState.getFields())
		{
			// create current Panel
			JPanel currentPanel = new JPanel();
			currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.X_AXIS));
			
			// create label and textField
			JLabel currentLabel = new JLabel(f.getTitle());
			JTextField currentField = new JTextField("");
			currentField.addFocusListener(this);
			
			// set preferences
			currentLabel.setPreferredSize(new Dimension(100, 15));
			currentField.setPreferredSize(new Dimension(200, 15));
			
			// add components to CurrentPanel
			currentPanel.add(currentLabel);
			currentPanel.add(currentField);
			
			// add currentPanel to rightPanel
			rightPanel.add(currentPanel);
			
			// add currentTextField to list of TextFields
			textFields.add(currentField);
		}
	}
	
	private void populateRecordsList()
	{
		for(int i = 0; i < batchState.getProject().getRecordsPerImage(); i++)
		{
			model.addElement("" + (i+1));
		}
	}
	
	public void refreshTabSelection()
	{		
		int column = batchState.getCellColumn();
		int row = batchState.getCellRow();
//		int row = recordsList.getSelectedIndex();
		
		recordsList.setSelectedIndex(row);
		
		if(column >= 0)
		{
			// Refresh all values in textFields
			for(int i = 0; i < textFields.size(); i++)
			{
				textFields.get(i).setText("");
				System.out.println("column: " + i);
				textFields.get(i).setText(batchState.getCell(row, i));
			}
		}
		textFields.get(column).requestFocus();
	}
	
	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	@Override
	public void valueChanged(int row, int column, String newValue) 
	{
		textFields.get(column).setText(newValue);
		batchState.setCell(row, column, newValue);
		
		System.out.println("Value: " + newValue);
		System.out.println("bsValue:" + batchState.getCell(row, column));
	}

	@Override
	public void selectedCellChanged(int row, int column)
	{
		// ROW
		recordsList.setSelectedIndex(row);
		
		// COLUMN
		if(column >= 0)
		{
			textFields.get(column).requestFocus();
		}
	}

	@Override
	public Coordinate getWindowPosition() 			{ return null; 	}
	@Override
	public Dimension getWindowSize() 				{ return null; 	}
	@Override
	public int getVPaneDivPosition() 				{ return 0; 	}
	@Override
	public int getHPaneDivPosition() 				{ return 0; 	}
	@Override
	public void setButtonAvailability() 	{}
	@Override
	public void focusGained(FocusEvent e) 
	{
		int index = 0;

		for(int i = 0; i < textFields.size(); i++)
		{
			if(e.getSource() == textFields.get(i))
			{
				index = i;
				break;
			}
		}
		
		batchState.selectedCellChanged(batchState.getCellRow(), index);
	}

	@Override
	public void focusLost(FocusEvent e) {}
	@Override
	public DataTable getDataTable()					{ return null; }
	@Override
	public DataForm getDataForm()					{ return null; }
	@Override
	public FieldHelpPanel getFieldHelp()			{ return null; }
	@Override
	public ImageNavigationPanel getImageNavigation(){ return null; }

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
