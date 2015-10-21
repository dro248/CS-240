package shared.communication;

public class ImgResult extends UserParams
{
	int projectID;
	boolean fail;
	
	
	public ImgResult(String _username, String _password, int _projectID, boolean _fail)
	{
		super(_username, _password);
		projectID = _projectID;
		fail = _fail;
	}
	
	
	public String getUsername() { return username; 	}
	public String getPassword()	{ return password; 	}
	public int getProjectID()	{ return projectID;	}
	public boolean fail()		{ return fail;		}
}
