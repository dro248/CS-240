package shared.model;

public class Field 
{
	// FIELDS
	int ID;
	int parent_project_id;
	String title;
	int xCoordinate;
	int width;
	String helpHTML;
	String knownData;
	
	
	// CONSTRUCTOR
	public Field(String _title, int _xCoordinate, int _width, String _helpHTML, String _knownData)
	{
		title = _title;
		xCoordinate = _xCoordinate;
		width = _width;
		helpHTML = _helpHTML;
		knownData = _knownData;
	}
	
	
	// GETTERS
	public String get_title()			{ return title; 		}
	public int get_xCoordinate()		{ return xCoordinate; 	}
	public int get_width()				{ return width; 		}
	public String get_helpHTML()		{ return helpHTML; 		}
	public String get_knownData()		{ return knownData; 	}	
}