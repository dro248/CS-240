package shared.communication;

public class FieldParams extends UserParams
{
	int projectID;
	
	public FieldParams(String _username, String _password, int _projectID) 
	{
		super(_username, _password);
		projectID = _projectID;
	}

	public int getProjectID() { return projectID; }
}