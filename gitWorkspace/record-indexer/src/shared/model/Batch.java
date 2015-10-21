package shared.model;

import java.util.*;

public class Batch 
{	
	int ID;
	boolean isAvailable;
	String filename;
	List<Record> myRecords;
	
	
	public Batch(int _ID, boolean _isAvailable, String _filename, List<Record> _records)
	{
		ID = _ID;
		isAvailable = _isAvailable;
		filename = _filename;
		myRecords = _records;
	}
	
	
	public int getID()				{ return ID; 			}
	public boolean isAvailable()	{ return isAvailable; 	}
	public String getFilename()		{ return filename; 		}
	public List<Record> getRecords(){ return myRecords; 	}

}