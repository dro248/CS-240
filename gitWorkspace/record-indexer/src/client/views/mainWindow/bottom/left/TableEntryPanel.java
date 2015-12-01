package client.views.mainWindow.bottom.left;

import java.awt.Dimension;

import javax.swing.JPanel;

import shared.model.Cell;
import client.facade.BatchState;
import client.facade.BatchStateListener;
import client.facade.ClientFacade;
import client.facade.Coordinate;

@SuppressWarnings("serial")
public class TableEntryPanel extends JPanel implements BatchStateListener
{
	private BatchState batchState;

	public TableEntryPanel()
	{
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
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

	@Override
	public Coordinate getWindowPosition()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getWindowSize()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
