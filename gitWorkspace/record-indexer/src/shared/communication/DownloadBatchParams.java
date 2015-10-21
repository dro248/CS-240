package shared.communication;

public class DownloadBatchParams extends UserParams
{ 
	int projectID;
	
	public DownloadBatchParams(String _username, String _password, int _projectID) 
	{
		super(_username, _password);
		projectID = _projectID;
	}
	
	public int getProjectID() { return projectID; }
}
