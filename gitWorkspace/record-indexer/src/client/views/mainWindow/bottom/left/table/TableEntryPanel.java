package client.views.mainWindow.bottom.left.table;

import javax.swing.*;
import client.facade.BatchState;
import client.facade.ClientFacade;

@SuppressWarnings("serial")
public class TableEntryPanel extends JPanel
{
	@SuppressWarnings("unused")
	private BatchState	batchState;
	private JScrollPane	scrollPane;
	private DataTable 	dataTable;
	

	public TableEntryPanel()
	{
		batchState = ClientFacade.get().getBatchState();
		dataTable 	= new DataTable();
		scrollPane	= new JScrollPane(dataTable);
		
		this.add(scrollPane);
		System.out.println("Table Created!");
	}
	
}
