package shared.model;

public class Record 
{
	int ID;
	int parentBatchID;
	int rowNumber;
	
	
	public Record(int _ID, int _parentBatchID, int _rowNumber)
	{
		ID = _ID;
		parentBatchID = _parentBatchID;
		rowNumber = _rowNumber;
	}
	
	
	public int getID()				{ return ID; 			}
	public int getParentBatchID()	{ return parentBatchID; }
	public int getRowNumber()		{ return rowNumber; 	}
	
}