package shared.model;


public class User 
{
	// FIELDS
//	int ID;
	String username;	// this is unique
	String password;
	String firstname;
	String lastname;
	String email;
	int recordsIndexed;
	int currentBatchID;
	
	
	// CONSTRUCTOR
	public User(String _username, String _password, String _firstname, 
			String _lastname, String _email, int _recordsIndexed, int _currentBatchID)
	{
//		ID = _id;
		username = _username;
		password = _password;
		firstname = _firstname;
		lastname = _lastname;
		email = _email;
		recordsIndexed = _recordsIndexed;
		currentBatchID = _currentBatchID;
	}
	
	
	// GETTERS
	public String getUsername()		{ return username; 		}
	public String getPassword()		{ return password; 		}
	public String getFirstname()	{ return firstname; 	}
	public String getLastname()		{ return lastname;		}
	public String getEmail()		{ return email; 		}
	public int getRecordsIndexed()	{ return recordsIndexed;}
	public int getCurrentBatchID()	{ return currentBatchID;}
	
}
