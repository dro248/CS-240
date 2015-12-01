package client.facade;

import java.awt.Dimension;
import shared.model.Cell;

public interface BatchStateListener 
{	
	public void valueChanged(Cell cell, String newValue);
	
	public void selectedCellChanged(Cell newSelectedCell);
	
	public Coordinate getWindowPosition();
	public Dimension getWindowSize();
}
