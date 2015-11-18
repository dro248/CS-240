package shared.model;

public class Cell 
{
	private int parentRecordID;
	private int parentFieldID;
	private String cellValue;
	
	public Cell(int _parentRecordID, int _fieldID, String _value)
	{
		cellValue 		= _value;
		parentRecordID 	= _parentRecordID;
		parentFieldID 	= _fieldID;
	}
	
	public String getValue() 		{ return cellValue; 		}
	public void setValue(String val){ cellValue = val;			}
	public int getParentRecordID()	{ return parentRecordID; 	}
	public int getParentFieldID()	{ return parentFieldID; 	}
}
