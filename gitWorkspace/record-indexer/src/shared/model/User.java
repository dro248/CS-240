package shared.model;


public class User 
{
	// FIELDS
	private String username;	// this is unique
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private int recordsIndexed;
	private int currentBatchID;
	
	public User(String _username, String _password, String _firstname, String _lastname, String _email, int _recordsIndexed, int _currentBatchID)
	{
		username 		= _username;
		password 		= _password;
		firstname 		= _firstname;
		lastname 		= _lastname;
		email 			= _email;
		recordsIndexed 	= _recordsIndexed;
		currentBatchID 	= _currentBatchID;
	}
	
	public String getUsername()		{ return username; 		}
	public String getPassword()		{ return password; 		}
	public String getFirstname()	{ return firstname; 	}
	public String getLastname()		{ return lastname;		}
	public String getEmail()		{ return email; 		}
	public int getRecordsIndexed()	{ return recordsIndexed;}
	public int getCurrentBatchID()	{ return currentBatchID;}

	public void setUsername			(String _username) 		{ username 			= _username;		}
	public void setPassword			(String _password) 		{ password 			= _password; 		}
	public void setFirstname		(String _firstname)		{ firstname 		= _firstname;		}
	public void setLastname			(String _lastname)		{ lastname 			= _lastname; 		}
	public void setEmail			(String _email)			{ email 			= _email; 			}
	public void setRecordsIndexed	(int _recordsIndexed)	{ recordsIndexed 	= _recordsIndexed;	}
	public void setCurrentBatchID	(int _currentBatchID)	{ currentBatchID 	= _currentBatchID;	}
}
