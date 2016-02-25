package client.views.mainWindow.bottom.left.table;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

import client.facade.*;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;

@SuppressWarnings("serial")
public class DataTable extends JPanel implements BatchStateListener
{
	private BatchState		batchState;
	private DataTableModel 	tableModel;
	private JTable table;
	
	public DataTable() throws HeadlessException 
	{
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
		
		tableModel = new DataTableModel();
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		
		
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < tableModel.getColumnCount(); ++i) 
		{
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(100);
		}
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		rootPanel.add(table, BorderLayout.CENTER);
		
		this.add(rootPanel);
	
		table.setFocusTraversalKeysEnabled(false);
		
		
		
		
		DefaultListModel<String> model 	= new DefaultListModel<>();
		JList<String> suggestionList	= new JList<String>( model );
		suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;
		
		for(int i = 0; i < 25; i++)
		{
			model.addElement("Suggestion");
		}
		
		// Create SampleImage dialog
		JOptionPane suggestionDialog = new JOptionPane();
		suggestionDialog.setLayout(new BoxLayout(suggestionDialog, BoxLayout.Y_AXIS));
//		JList<String> suggestionList = new JList<String>();
		JButton suggestionButton = new JButton("Use Suggestion");
		suggestionButton.setSize(100, 50);
		suggestionButton.setEnabled(false);
		suggestionList.setSize(100, 100);
		
		
		Object[] myObjects = new Object[] {new JScrollPane(suggestionList), suggestionButton};
		suggestionDialog.setSize(100, 100);
		
		
		suggestionList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
		
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {

				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("List Clicked!");
				suggestionButton.setEnabled(true);
			}
		});
		
		suggestionButton.addMouseListener(new MouseListener() 
		{	
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {	}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Trying to close suggestionDialog...");
//				suggestionDialog.hide();
			}
		});
		

		// Listeners
		table.addMouseListener(new MouseListener() 
		{	
			@SuppressWarnings("static-access")
			@Override
			public void mouseReleased(MouseEvent e) 
			{
//				if(e.isPopupTrigger())
				if(e.getButton() == 3)
				{
					System.out.println("See Suggestions");
					
					suggestionDialog.showMessageDialog(null, myObjects, "Suggestions", JOptionPane.PLAIN_MESSAGE);
				}
				else
				{
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					
					if(column > 0)
					{
						batchState.selectedCellChanged(row, column-1); 	// don't change this!
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) 	{}
			@Override
			public void mouseExited(MouseEvent e) 	{}
			@Override
			public void mouseEntered(MouseEvent e) 	{}
			@Override
			public void mouseClicked(MouseEvent e) 	
			{
				if(e.isPopupTrigger())
				{
					System.out.println("See Suggestions");
				}
				
				
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();

				if(column > 0)
				{
					batchState.selectedCellChanged(row, column-1);	// don't change this!
				}
			}
		});
		
		table.addKeyListener(new KeyListener() 
		{	
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if(e.getKeyChar() == '\t')
				{
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					
					batchState.selectedCellChanged(row, column-1);	// Don't change this!
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
	
	}	
	
	
	class ColorCellRenderer extends JLabel implements TableCellRenderer 
	{
		private Border unselectedBorder = BorderFactory.createLineBorder(Color.BLACK, 0);
		private Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

		public ColorCellRenderer() 
		{
			setOpaque(true);
			setFont(getFont().deriveFont(16.0f));
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
		{			
			if (isSelected) 
			{
				this.setBorder(selectedBorder);
				batchState.selectedCellChanged(row, column);
			}
			else 
			{
				this.setBorder(unselectedBorder);
			}
			
			this.setText((String)value);
			
			return this;
		}
	}
	
	public void refreshTabSelection()
	{
		if(batchState == null)
		{
			return;
		}
		// getValues from batchState
		int column = batchState.getCellColumn();
		int row = batchState.getCellRow();
		
		// Update selected Cell from batchState		
		table.changeSelection(row, column+1, false, false);
		
		if(column > 0)
		{
			String value = batchState.getCell(row, column);
			table.setValueAt(value, row, column+1);
		}
	}
	
	
	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	@Override
	public void valueChanged(int row, int column, String newValue) 
	{
		if(column >= 0)
		{
			table.setValueAt(newValue, row, column+1);
		}
	}

	@Override
	public void selectedCellChanged(int row, int column)
	{
		if(column < 0 || row < 0)
		{
			return;
		}
		
		// update position		
		if(table.getSelectedColumn() != column-1 || table.getSelectedRow() != row-1)
		{
			table.changeSelection(row, column+1, false, false);
		}
		
		
		// if values are different, save new value (This is supposed to prevent Event Cycle)
		if(!batchState.getCellValues()[row][column].equals(table.getValueAt(row, column)))
		{
			// set selectedCell in batchState
			batchState.setCell(row, column, (String)table.getValueAt(row, column+1));	// Don't Change this!
		}
	}

	@Override
	public Coordinate getWindowPosition()			{ return null; 	}
	@Override
	public Dimension getWindowSize()				{ return null; 	}
	@Override
	public int getVPaneDivPosition()				{ return 0; 	}
	@Override
	public int getHPaneDivPosition()				{ return 0; 	}
	@Override
	public void setButtonAvailability() 			{}
	@Override
	public DataTable getDataTable()					{ return null; 	}
	@Override
	public DataForm getDataForm()					{ return null;	}
	@Override
	public FieldHelpPanel getFieldHelp()			{ return null; 	}
	@Override
	public ImageNavigationPanel getImageNavigation(){ return null;	}



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
