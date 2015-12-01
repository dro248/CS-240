package client.facade;

import java.awt.Dimension;
import java.io.*;
import java.util.*;
import java.beans.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.model.*;

public class BatchState implements BatchStateListener
{	
	// Batch State
	private User 			user;
	private Batch 			batch;
	private List<String> 	recordFieldValues;
	private String[][] 		cellValues;
	private Cell 			selectedCell;
	
	// Window State
	Coordinate 	windowPosition;
	Dimension 	windowSize;
	int 		vPanePosition;	// Position of the vertical split-panel divider
	int			hPanePosition;	// Position of the horizontal split-panel divider
	
	// Image State
	private double 	zoomLevel;
	private int 	scrollPosition;
	private boolean highlightsVisible;
	private boolean imageInverted;
	
	// Contains all the important items that need to be kept synchronized
	private transient List<BatchStateListener> listeners;
	
	public BatchState(User _user)
	{
		BatchState savedState = deserializeFile(_user.getUsername());
		listeners = new ArrayList<BatchStateListener>();
		
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
			vPanePosition		= savedState.getVPaneDivPosition();
			hPanePosition		= savedState.getHPaneDivPosition();
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
			windowPosition		= new Coordinate(10, 10);
			windowSize 			= new Dimension(1000, 700);
			vPanePosition		= 365;
			hPanePosition		= 450;
			
			// Image State
			zoomLevel 			= 0;
			scrollPosition 		= 0;
			highlightsVisible	= false;
			imageInverted 		= false;
		}
	}
	
	public void addListener(BatchStateListener l) { listeners.add(l); }
	
	// WORKS!
	public void save()
	{
		updateValues();
		
		// Serialize BatchState to xml and save it to disk as {{_username}}.xml
		File out_file = new File("./SavedStates/" + user.getUsername() + ".xml");
		
		try 
		{ 
			XStream xmlStream = new XStream(new DomDriver());
			OutputStream outStream;
			outStream = new BufferedOutputStream(new FileOutputStream(out_file)); 
			xmlStream.toXML(this, outStream);
		}
		catch (FileNotFoundException e) {}
	}
	
	// WORKS!
	private BatchState deserializeFile(String _username)
	{
		BatchState output = null;
		File in_file = new File("./SavedStates/" + _username + ".xml");
		
		if(in_file.exists())
		{
			try 
			{
				XStream xmlStream = new XStream(new DomDriver());
				InputStream inStream = new BufferedInputStream(new FileInputStream(in_file));
				output = (BatchState) xmlStream.fromXML(in_file);
			}
			catch (FileNotFoundException e) {}
			
			return output;
		}
		else { return null; }
	}
	
	private void updateValues()
	{
		windowPosition 	= listeners.get(0).getWindowPosition();
		windowSize		= listeners.get(0).getWindowSize();
		vPanePosition	= listeners.get(0).getVPaneDivPosition();
		hPanePosition	= listeners.get(0).getHPaneDivPosition();
		
		// TODO ... update all values!
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
	public void setvPanePosition(int vPanePosition) 					{ this.vPanePosition = vPanePosition; 			}
	public void sethPanePosition(int hPanePosition)						{ this.hPanePosition = hPanePosition; 			}
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
	public int getVPaneDivPosition() { return vPanePosition; }

	@Override
	public int getHPaneDivPosition() { return hPanePosition; }

	@Override
	public void setButtonAvailability() 
	{
		for(BatchStateListener l : listeners)
		{
			l.setButtonAvailability();
		}
	}
}