package shared.communication;

public class ImgParams extends UserParams
{
	private int parentProjectID;
	
	public ImgParams(String _username, String _password, int _parentProjectID)
	{
		super(_username, _password);
		parentProjectID = _parentProjectID;
	}

	public int getParentProjectID() { return parentProjectID; }
}
