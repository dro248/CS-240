package shared.communication;

public class SubmitBatchParams extends UserParams
{
	private int batchID;
	private String fieldValues;
	
	public SubmitBatchParams(String _username, String _password, int _batchID, String _fieldValues)
	{
		super(_username, _password);
		batchID = _batchID;
		fieldValues = _fieldValues;
	}
	
	public int getBatchID()			{ return batchID; 		}
	public String getFieldValues()	{ return fieldValues; 	}
}
