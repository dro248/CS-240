package shared.model;

public class Batch 
{	
	private int ID;
	private String url;
	private boolean isAvailable;
	private int parentProjectID;
	
	
	public Batch(int _ID, int _parentProjectID, boolean _isAvailable, String _url)
	{
		ID 				= _ID;
		parentProjectID = _parentProjectID;
		isAvailable 	= _isAvailable;
		url 			= _url;
	}
	
	
	public int getID()				{ return ID; 				}
	public void setID(int _ID)		{ ID = _ID;					} 
	public int getParentProjectID()	{ return parentProjectID; 	}
	public String getUrl()			{ return url; 				}
	public boolean isAvailable()	{ return isAvailable; 		}
	public void setAvailable(boolean b) { isAvailable = b;		}
}
