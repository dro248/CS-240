package shared.model;

public class Record 
{
	int ID;
	int parentBatchID;
	
	
	public Record(int _ID, int _parentBatchID)
	{
		ID = _ID;
		parentBatchID = _parentBatchID;
	}
	
	
	public int getID()				{ return ID; 			}
	public int getParentBatchID()	{ return parentBatchID; }	
}
