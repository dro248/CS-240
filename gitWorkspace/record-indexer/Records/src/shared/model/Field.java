package shared.model;

public class Field
{
	private int ID;
	private String title;
	private int xCoordinate;
	private int width;
	private String helpHTML;
	private String knownData;
	private int parentProjectID;
	private int column;
	
	public Field(int _ID, String _title, int _xCoordinate, int _width, String _helpHTML, String _knownData, int _parentProjectID, int _column)
	{
		ID				= _ID;
		title 			= _title;
		xCoordinate 	= _xCoordinate;
		width 			= _width;
		helpHTML 		= _helpHTML;
		knownData 		= _knownData;
		parentProjectID = _parentProjectID;
		column			= _column;
	}

	public int 		getID()									{ return ID;							}
	public String 	getTitle()								{ return title; 						}
	public int 		getXCoordinate()						{ return xCoordinate; 					}
	public int 		getWidth()								{ return width; 						}
	public String 	getHelpHTML()							{ return helpHTML; 						}
	public String 	getKnownData()							{ return knownData; 					}
	public int 		getParentProjectID()					{ return parentProjectID;				}	
	public int 		getColumnNumber()						{ return column;						}
	public void 	setParentProjectID(int _parentProjectID){ parentProjectID = _parentProjectID; 	}
	public void 	setID(int id)							{ ID = id;								}
}