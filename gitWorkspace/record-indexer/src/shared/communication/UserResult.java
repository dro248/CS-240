package shared.communication;

public class UserResult
{
	String firstname;
	String lastname;
	int recordsIndexed;
	boolean validUser;
	boolean fail;
	
	public UserResult(String _firstname, String _lastname, int _recordsIndexed, boolean _validUser, boolean _fail)
	{
		firstname = _firstname;
		lastname = _lastname;
		recordsIndexed = _recordsIndexed;
		validUser = _validUser;
		fail = _fail;
	}

	public String getFirstname() 	{ return firstname; 	}
	public String getLastname() 	{  return lastname;		}
	public int getRecordsIndexed() 	{ return recordsIndexed;}
	public boolean isValidUser() 	{ return validUser; 	}
	public boolean fail()			{ return fail; 			}
}
