package shared.model;

public class Cell 
{
	String value;
	int parentRecordID;
	int fieldID;
	
	
	public Cell(String _value, int _parentRecordID, int _fieldID)
	{
		value = _value;
		parentRecordID = _parentRecordID;
		fieldID = _fieldID;
	}
	
	
	public String getValue() { return value; 			}
	public int getParentID() { return parentRecordID; 	}
	public int getFieldID()	 { return fieldID; 			}
}