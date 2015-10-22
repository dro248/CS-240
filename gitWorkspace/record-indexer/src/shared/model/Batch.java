package shared.model;

import java.util.*;

public class Batch 
{	
	int ID;
	String file;
	boolean isAvailable;
	int parentProjectID;
	
	
	public Batch(int _ID, int _parentProjectID, boolean _isAvailable, String _filename)
	{
		ID = _ID;
		parentProjectID = _parentProjectID;
		isAvailable = _isAvailable;
		file = _filename;
	}
	
	
	public int getID()				{ return ID; 			}
	public String getFilename()		{ return file; 			}
	public boolean isAvailable()	{ return isAvailable; 	}
}
