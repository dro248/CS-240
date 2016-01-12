package client.facade;

import java.awt.Dimension;
import java.io.*;
import java.net.URL;
import java.util.*;

import shared.model.Batch;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class BatchState implements BatchStateListener
{	
	// Batch State
	private User 			user;
	private Project			project;
	private Batch 			batch;
	private URL				url;
	private List<Field>		fields;
	private List<String> 	recordFieldValues;
	private String[][] 		cellValues;
	private int				ARRAY_ROWS;
	private int				ARRAY_COLUMNS;
	private Cell 			selectedCell;
	
	// Window State
	Coordinate 				windowPosition;
	Dimension 				windowSize;
	int 					vPanePosition;	// Position of the vertical split-panel divider
	int						hPanePosition;	// Position of the horizontal split-panel divider
	
	// Image State
	private double 			zoomLevel;
//	private int 			scrollPosition;
	private int 			w_translateX;
	private int 			w_translateY;
	private boolean			highlightsVisible;
	private boolean 		imageInverted;
	
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
			url					= savedState.getURL();
			project				= savedState.getProject();
			recordFieldValues 	= savedState.getRecordFieldValues();
			cellValues			= savedState.getCellValues();
			selectedCell		= savedState.getSelectedCell();
			windowPosition 		= savedState.getWindowPosition();
			windowSize			= savedState.getWindowSize();
			vPanePosition		= savedState.getVPaneDivPosition();
			hPanePosition		= savedState.getHPaneDivPosition();
			zoomLevel			= savedState.getZoomLevel();
			highlightsVisible	= savedState.isHighlightsVisible();
			imageInverted		= savedState.isImageInverted();
			
			// window position normalizing
			windowPosition.set_X(windowPosition.get_X()/2);
			windowPosition.set_Y(windowPosition.get_Y()/2);
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
			selectedCell 		= new Cell(0, 1, "");
			
			// Window State
			windowPosition		= new Coordinate(10, 10);
			windowSize 			= new Dimension(1000, 700);
			vPanePosition		= 365;
			hPanePosition		= 450;
			
			// Image State
			zoomLevel 			= 0;
			highlightsVisible	= false;
			imageInverted 		= false;
		}
	}
	
	
	public void translate()
	{
		
	}
	
	
	public void addListener(BatchStateListener l) { listeners.add(l); }
	
	public void empty()
	{
		batch 				= null;
		url					= null;
		project 			= null;
		recordFieldValues 	= null;
		cellValues			= null;
		selectedCell 		= new Cell(0, 1, "");
		zoomLevel 			= 1.0;		// Default
		highlightsVisible	= false;
		imageInverted 		= false;
	}
	
	public void initCellValues()
	{
		final int ROWS = this.project.getRecordsPerImage();
		final int COLS = this.fields.size();
		
		ARRAY_ROWS 		= ROWS;
		ARRAY_COLUMNS 	= COLS;
		
		cellValues = new String[ROWS][COLS];
		
		// initialize cellValues with Strings (rather than null)
		for(int r = 0; r < ROWS; r++)
		{
			for(int c = 0; c < COLS; c++)
			{
				cellValues[r][c] = new String();
			}
		}
	}
	
	// WORKS!
	public void save()
	{
		updateValues();
		
		if(batch == null || user == null)
		{
			return;
		}
		
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
	@SuppressWarnings("resource")
	private BatchState deserializeFile(String _username)
	{
		BatchState output = null;
		File in_file = new File("./SavedStates/" + _username + ".xml");
		
		if(in_file.exists())
		{
			try 
			{
				XStream xmlStream = new XStream(new DomDriver());
				new BufferedInputStream(new FileInputStream(in_file));
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
		
		// get zoomValues from imageViewer
		zoomLevel		= listeners.get(2).getZoom();
	}
	
	//////////////////////
	//		Getters		//
	//////////////////////
	public User 		getUser() 										{ return user; 									}
	public Project 		getProject()									{ return project;								}
	public Batch 		getBatch()										{ return batch;									}
	public URL 			getURL()										{ return url;									}
	public List<Field> 	getFields()										{ return fields;								}
	public List<String> getRecordFieldValues()							{ return recordFieldValues; 					}
	public String[][]	getCellValues()									{ return cellValues;							}
	public Coordinate 	getWindowPosition()								{ return windowPosition; 						}
	public Dimension 	getWindowSize() 								{ return windowSize; 							}
	public double 		getZoomLevel()									{ return zoomLevel;								}
	public boolean 		isHighlightsVisible()							{ return highlightsVisible;						}
	public boolean 		isImageInverted()								{ return imageInverted;							}
	public Cell 		getSelectedCell() 								{ return selectedCell; 							}
	public int			getCellRow()									{ return selectedCell.getParentRecordID(); 		}
	public int			getCellColumn()									{ return selectedCell.getParentFieldID();		}
	public String 		getCell(int row, int column) 					{ return cellValues[row][column]; 				}
	public int					getRows()								{ return ARRAY_ROWS;							}
	public int 					getColumns()							{ return ARRAY_COLUMNS;							}
	
	//////////////////////
	//		Setters		//
	//////////////////////
	public void setUser(User user)										{ this.user 			 = user; 				}
	public void setProject(Project project)								{ this.project			 = project;				}
	public void setBatch(Batch batch)									{ this.batch 			 = batch; 				}
	public void setURL(URL url)											{ this.url				 = url;					}
	public void setFields(List<Field> fieldsList)						{ this.fields 			 = fieldsList;			}
	public void setRecordFieldValues(List<String> recordFieldValues) 	{ this.recordFieldValues = recordFieldValues; 	}
	public void setCellValues(String[][] cellValues)					{ this.cellValues 		 = cellValues; 			}
	public void setWindowPosition(Coordinate windowPosition)			{ this.windowPosition 	 = windowPosition; 		}
	public void setWindowSize(Dimension windowSize)						{ this.windowSize 		 = windowSize; 			}
	public void setvPanePosition(int vPanePosition) 					{ this.vPanePosition 	 = vPanePosition; 		}
	public void sethPanePosition(int hPanePosition)						{ this.hPanePosition 	 = hPanePosition; 		}
	public void setZoomLevel(double zoomLevel)							{ this.zoomLevel 		 = zoomLevel; 			}
	public void setHighlightsVisible(boolean highlightsVisible)	 		{ this.highlightsVisible = highlightsVisible; 	}
	public void setImageInverted(boolean imageInverted)					{ this.imageInverted 	 = imageInverted; 		}	

	
	public void setCell(int row, int column, String value) 
	{
		String oldValue = cellValues[row][column];
        
		cellValues[row][column] = value;
		
		if (!value.equals(oldValue))
		{
			for (BatchStateListener l : listeners) 
			{
				l.valueChanged(row, column, value);
			}
		}
	}
	public void setSelectedCell(Cell selCell) 
	{
		Cell oldValue = selectedCell;
        
		selectedCell = selCell;
		
		// this is supposed to prevent an Event Cycle
		if (selCell.getParentRecordID() != oldValue.getParentRecordID() || 
			selCell.getParentFieldID() != oldValue.getParentFieldID()) 
		{
			for (BatchStateListener l : listeners) 
			{
				l.selectedCellChanged(selCell.getParentRecordID(), selCell.getParentFieldID());
			}
		}
	}
	
	public String cellValues_toString()
	{
		StringBuilder output = new StringBuilder();
		
		for(int r = 0; r < ARRAY_ROWS; r++)
		{
			for(int c = 0; c < ARRAY_COLUMNS; c++)
			{
				output.append(cellValues[r][c]);
				if(c < ARRAY_COLUMNS-1)
				{
					output.append(",");
				}
			}
			
			if(r < ARRAY_ROWS-1)
			{
				output.append(";");
			}
		}
		
		return output.toString();
	}

	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	@Override
	public void valueChanged(int row, int column, String newValue) 
	{}
	@Override
	public void selectedCellChanged(int row, int column)
	{
		// make sure this works
		if(column >= 0 && !cellValues[row][column].equals(""))
		{
			setSelectedCell(new Cell(row, column, cellValues[row][column]));
		}
		else
		{
			setSelectedCell(new Cell(row, column, ""));
		}
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

	@Override
	public DataTable getDataTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataForm getDataForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldHelpPanel getFieldHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageNavigationPanel getImageNavigation() {
		// TODO Auto-generated method stub
		return null;
	}


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