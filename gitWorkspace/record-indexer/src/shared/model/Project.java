package shared.model;

import java.util.*;

public class Project 
{
	// FIELDS
	private int ID;
	private String title = null;
	private int recordsPerImage = -1;
	private int firstYCoord = -1;
	private int recordHeight = -1;

	List<Field> myFields = new ArrayList<Field>();
	List<Batch> myImages = new ArrayList<Batch>();
	
	
	// CONSTRUCTOR
	public Project(int _ID, String _title, int _recordsPerImage, int _firstYCoordinate,
					int _recordHeight, List<Field> _myFields, List<Batch> _myImages)
	{
		ID = _ID;
		title = _title;
		recordsPerImage = _recordsPerImage;
		firstYCoord = _firstYCoordinate;
		recordHeight = _recordHeight;
		myFields = _myFields;
		myImages = _myImages;
	}
	
	
	
	// GETTERS
	public String getTitle()			{ return title; 			}
	public int getRecordsPerImage()		{ return recordsPerImage; 	}
	public int getFirstYCoordinate()	{ return firstYCoord; 		}
	public int getRecordHeight()		{ return recordHeight; 		}
	public List<Field> getFields()		{ return myFields; 			}
	public List<Batch> getImages()		{ return myImages; 			}

}
