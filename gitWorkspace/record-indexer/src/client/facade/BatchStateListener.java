package client.facade;

import shared.model.Cell;

public interface BatchStateListener 
{	
	public void valueChanged(Cell cell, String newValue);
	
	public void selectedCellChanged(Cell newSelectedCell);
}