package client.views.mainWindow.bottom.left.table;

import javax.swing.table.AbstractTableModel;
import client.facade.BatchState;
import client.facade.ClientFacade;

@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel
{
	private BatchState batchState;

	public DataTableModel() 
	{
		batchState = ClientFacade.get().getBatchState();
				
	}

	@Override
	public int getColumnCount() { return batchState.getFields().size() + 1; }
	
	@Override
	public int getRowCount() { return batchState.getProject().getRecordsPerImage(); }
	
	@Override
	public String getColumnName(int columnNumber) 
	{
		if(columnNumber == 0 )
		{
			return "Record Number";
		}
		
		if (columnNumber >= 0 && columnNumber < getColumnCount()) 
		{
			return batchState.getFields().get(columnNumber-1).getTitle();
		} 
		else 
		{
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) 
	{
		if(column == 0)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public String getValueAt(int row, int column) 
	{
//		System.out.println("Starting getValueAt");
		
		if(column == 0)
		{
			return "" + (row + 1);
		}
		
		if (row >= 0 && row < getRowCount() && column >= 0 && column < getColumnCount()) 
		{
			// Get values directly from the "cellValues" array in the batchState
			return batchState.getCell(row, column-1);
		} 
		else { throw new IndexOutOfBoundsException(); }
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
//		System.out.println("Starting setValueAt");
		if (row >= 0 && row < getRowCount() && column >= 0 && column < getColumnCount()) 
		{
			System.out.println("Set Cell: " + row + "," + (column));
			
//			batchState.setCell(row, column, (String)value); 
			if(column > 0)
			{
				batchState.setCell(row, column-1, (String)value); 
			}
			
			this.fireTableCellUpdated(row, column);
			
		} 
		else { throw new IndexOutOfBoundsException(); }		
//		System.out.println("Ending setValueAt");
	}
}
