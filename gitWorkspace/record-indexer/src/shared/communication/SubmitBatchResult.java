package shared.communication;

public class SubmitBatchResult
{
	private boolean state;
	
	public SubmitBatchResult(boolean status)
	{
		state = status;
	}
	
	public boolean getState() { return state; }
	public String toString()
	{		
		if(!state)
		{
			return "FAILED\n";
		}
		
		return "TRUE\n";
	}
}
