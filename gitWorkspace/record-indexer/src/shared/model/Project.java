package shared.model;

import java.util.*;

public class Project 
{
	// FIELDS
	private int ID;
	private String title;
	private int recordsPerImage;
	private int firstYCoord;
	private int recordHeight;
	
	
	// CONSTRUCTOR
	public Project(int _ID, String _title, int _recordsPerImage, int _firstYCoordinate, int _recordHeight)
	{
		ID = _ID;
		title = _title;
		recordsPerImage = _recordsPerImage;
		firstYCoord = _firstYCoordinate;
		recordHeight = _recordHeight;
	}
	
	
	public String getTitle()			{ return title; 			}
	public int getRecordsPerImage()		{ return recordsPerImage; 	}
	public int getFirstYCoordinate()	{ return firstYCoord; 		}
	public int getRecordHeight()		{ return recordHeight; 		}
	public int getID()					{ return ID;				}
	public void setID(int _ID)			{ ID = _ID;					}
}
