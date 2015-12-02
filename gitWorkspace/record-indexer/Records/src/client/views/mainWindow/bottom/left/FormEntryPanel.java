package client.views.mainWindow.bottom.left;

import java.awt.Dimension;
import javax.swing.JPanel;
import shared.model.Cell;
import client.facade.*;

@SuppressWarnings("serial")
public class FormEntryPanel extends JPanel implements BatchStateListener
{
	private BatchState batchState;
	
	public FormEntryPanel()
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

	@Override
	public int getVPaneDivPosition()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHPaneDivPosition()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setButtonAvailability()
	{
		// TODO Auto-generated method stub
		
	}

}