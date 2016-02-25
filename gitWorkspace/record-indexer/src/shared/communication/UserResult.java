package shared.communication;

import shared.model.*;

public class UserResult
{
	private User user;
	
	public UserResult(User _user)
	{
		user = _user;
	}

	public User getUser() 	
	{ 
		return user; 
	}
	
	public String toString()
	{
		if(user == null)
		{
			return "FALSE\n";
		}
		return 	"TRUE\n"
				+ user.getFirstname()		+ "\n"
				+ user.getLastname()		+ "\n"
				+ user.getRecordsIndexed() 	+ "\n";
	}
}
