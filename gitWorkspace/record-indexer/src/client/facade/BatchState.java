package client.facade;

import java.awt.Dimension;
import java.io.*;
import java.util.*;
import java.beans.*;

import shared.model.*;

public class BatchState 
{	
	// Batch State
	private User user;
	private Batch batch;
	private List<String> recordFieldValues;
	private String[][] cellValues;
	private Cell selectedCell;
	
	// Window State
	private Coordinate windowPosition;
	private Dimension windowSize;
	private Coordinate vPanePosition;	// Position of the vertical split-panel divider
	private	Coordinate hPanePosition;	// Position of the horizontal split-panel divider
	
	// Image State
	private double zoomLevel;
	private int scrollPosition;
	private boolean highlightsVisible;
	private boolean imageInverted;
	
	// Contains all the important items that need to be kept synchronized
	private transient List<BatchStateListener> listeners;
	
	public BatchState(User _user)
	{
		BatchState savedState = deserializeFile(_user.getUsername());
		
		if(savedState != null)
		{
			// This executes when a saved BatchState is found
			// SET VALUES from _SavedState
			user 				= savedState.getUser();
			batch 				= savedState.getBatch();
			recordFieldValues 	= savedState.getRecordFieldValues();
			cellValues			= savedState.getCellValues();
			selectedCell		= savedState.getSelectedCell();
			windowPosition 		= savedState.getWindowPosition();
			windowSize			= savedState.getWindowSize();
			vPanePosition		= savedState.getvPanePosition();
			hPanePosition		= savedState.gethPanePosition();
			zoomLevel			= savedState.getZoomLevel();
			scrollPosition		= savedState.getScrollPosition();
			highlightsVisible	= savedState.isHighlightsVisible();
			imageInverted		= savedState.isImageInverted();			
		}
		else
		{
			// This will be called if no saved BatchState is found
			// SET DEFAULT VALUES
			
			// Batch State
			user 				= _user;
			batch 				= null;
			recordFieldValues 	= null;
			cellValues 			= null;
			selectedCell 		= null;
			
			// Window State
			windowPosition		= null;
			windowSize 			= null;
			vPanePosition		= null;
			hPanePosition		= null;
			
			// Image State
			zoomLevel 			= 0;
			scrollPosition 		= 0;
			highlightsVisible	= false;
			imageInverted 		= false;
		}
	}
	
	public void addListener(BatchStateListener l) 
	{
		listeners.add(l);
	}
	
	public BatchState deserializeFile(String _username)
	{
		File in_file = new File("./SavedStates/" + _username + ".xml");
		if(in_file.exists())
		{
			XMLDecoder decoder;
			try
			{
				decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(in_file)));
				BatchState output = (BatchState)decoder.readObject();
				decoder.close();
				return output;
			}
			catch (FileNotFoundException e) 
			{ return null; }
		}
		else
		{
			return null;
		}
	}
	
	public void saveStateToDisk(String _username)
	{
		// Serialize BatchState to xml and save it to disk as {{_username}}.xml
		File out_file = new File(_username + ".xml");
		
		XMLEncoder encoder;
		try
		{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(out_file)));
			encoder.writeObject(this);
			encoder.close();
		}
		catch (FileNotFoundException e) {}
	}	
	
	
	//////////////////////
	//		Getters		//
	//////////////////////
	public User getUser() 						{ return user; 				}
	public Batch getBatch()						{ return batch;				}
	public List<String> getRecordFieldValues()	{ return recordFieldValues; }
	public String[][] getCellValues()			{ return cellValues; 		}
	public Coordinate getWindowPosition()		{ return windowPosition; 	}
	public Dimension getWindowSize() 			{ return windowSize; 		}
	public Coordinate getvPanePosition()		{ return vPanePosition; 	}
	public Coordinate gethPanePosition()		{ return hPanePosition;		}
	public double getZoomLevel()				{ return zoomLevel;			}
	public int getScrollPosition()				{ return scrollPosition;	}
	public boolean isHighlightsVisible()		{ return highlightsVisible; }
	public boolean isImageInverted()			{ return imageInverted;		}
	public Cell getSelectedCell() 				{ return selectedCell; 		}
	public String getValue(Cell cell) 			{ return cellValues[cell.getParentRecordID()][cell.getParentFieldID()]; }
	
	//////////////////////
	//		Setters		//
	//////////////////////
	public void setUser(User user)										{ this.user = user; 							}
	public void setBatch(Batch batch)									{ this.batch = batch; 							}
	public void setRecordFieldValues(List<String> recordFieldValues) 	{ this.recordFieldValues = recordFieldValues; 	}
	public void setCellValues(String[][] cellValues)					{ this.cellValues = cellValues; 				}
	public void setWindowPosition(Coordinate windowPosition)			{ this.windowPosition = windowPosition; 		}
	public void setWindowSize(Dimension windowSize)						{ this.windowSize = windowSize; 				}
	public void setvPanePosition(Coordinate vPanePosition) 				{ this.vPanePosition = vPanePosition; 			}
	public void sethPanePosition(Coordinate hPanePosition)				{ this.hPanePosition = hPanePosition; 			}
	public void setZoomLevel(double zoomLevel)							{ this.zoomLevel = zoomLevel; 					}
	public void setScrollPosition(int scrollPosition)					{ this.scrollPosition = scrollPosition; 		}
	public void setHighlightsVisible(boolean highlightsVisible)	 		{ this.highlightsVisible = highlightsVisible; 	}
	public void setImageInverted(boolean imageInverted)					{ this.imageInverted = imageInverted; 			}
	
	public void setValue(Cell cell, String value) 
	{
		String oldValue = cellValues[cell.getParentRecordID()][cell.getParentFieldID()];
        
		cellValues[cell.getParentRecordID()][cell.getParentFieldID()] = value;
		
		if (!value.equals(oldValue)) {
        
			for (BatchStateListener l : listeners) 
			{
				l.valueChanged(cell, value);
			}
		}
	}
	
	public void setSelectedCell(Cell selCell) 
	{
		Cell oldValue = selectedCell;
        
		selectedCell = selCell;
		
		if (selCell.getParentRecordID() != oldValue.getParentRecordID() || 
			selCell.getParentFieldID() != oldValue.getParentFieldID()) 
		{
        
			for (BatchStateListener l : listeners) 
			{
				l.selectedCellChanged(selCell);
			}
		}
	}
}