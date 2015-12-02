package shared.communication;

public class UserParams
{
	String username;
	String password;
	
	public UserParams(String _username, String _password)
	{
		username = _username;
		password = _password;
	}
	
	public String getUsername() { return username; }
	public String getPassword() { return password; }
}
