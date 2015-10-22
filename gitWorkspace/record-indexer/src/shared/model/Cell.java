package shared.model;

public class Cell 
{
	int parentRecordID;
	int parentFieldID;
	String cellValue;
	
	public Cell(String _value, int _parentRecordID, int _fieldID)
	{
		cellValue = _value;
		parentRecordID = _parentRecordID;
		parentFieldID = _fieldID;
	}
	
	
	public String getValue() { return cellValue; 		}
	public int getParentID() { return parentRecordID; 	}
	public int getFieldID()	 { return parentFieldID; 	}
}
