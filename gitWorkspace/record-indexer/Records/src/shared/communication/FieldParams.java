package shared.communication;

public class FieldParams extends UserParams
{
	private int parentProjectID;
	
	public FieldParams(String _username, String _password, int _parentProjectID) 
	{
		super(_username, _password);
		parentProjectID = _parentProjectID;
	}

	public int getParentProjectID() { return parentProjectID; }
}
