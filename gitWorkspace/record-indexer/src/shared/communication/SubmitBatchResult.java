package shared.communication;

public class SubmitBatchResult
{

	boolean fail;
	
	public SubmitBatchResult(boolean _fail)
	{
		fail = _fail;
	}
	
	public boolean fail() { return fail; }
}
