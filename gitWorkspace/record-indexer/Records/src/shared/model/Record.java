package shared.model;

public class Record 
{
	private int ID;
	private int parentBatchID;
	
	
	public Record(int _ID, int _parentBatchID)
	{
		ID 				= _ID;
		parentBatchID 	= _parentBatchID;
	}
	
	public int getID()				{ return ID; 			}
	public void setID(int id)		{ ID = id;				}
	public int getParentBatchID()	{ return parentBatchID; }	
}
