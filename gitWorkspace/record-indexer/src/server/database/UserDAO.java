package server.database;

import shared.model.User;
import java.sql.*;

public class UserDAO 
{
	Database database;
	
	public UserDAO(Database db)
	{
		database = db;
	}
	
	/**
	 * Adds a user to the database.
	 * @param newUser
	 */
	public void add(User newUser) throws DatabaseException
	{
		// TEST that Username DOES NOT EXIST. Duplicate usernames not allowed!
		User tmpUser = get(newUser.getUsername(), newUser.getPassword());
		if(tmpUser.getUsername().equals(newUser.getUsername()))
		{
			System.out.println("error USERNAME_EXISTS: Username \"" + newUser.getUsername() + "\" already taken. Choose different username.");
			return;
		}
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try 
		{
			String query = "INSERT INTO User (username, password, firstname, lastname, email, indexedRecords, currentBatchID)"
							+ " values (?, ?, ?, ?, ?, ?, ?)";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setString(1, newUser.getUsername());
			stmt.setString(2, newUser.getPassword());
			stmt.setString(3, newUser.getFirstname());
			stmt.setString(4, newUser.getLastname());
			stmt.setString(5, newUser.getEmail());
			stmt.setInt(5, newUser.getRecordsIndexed());
			stmt.setInt(5, newUser.getCurrentBatchID());
		}
		catch (SQLException e) 
		{
			throw new DatabaseException("Could not ADD new User to Database", e);
		}
		finally 
		{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * Gets a User from the database based on username & password.
	 * @param username
	 * @param password
	 * @return Returns User with specified username & password.
	 */
	public User get(String _username, String _password) throws DatabaseException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User myUser;
		try 
		{
			String query = "SELECT * FROM User WHERE username = ? AND password = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setString(1, _username);
			stmt.setString(2, _password);

			rs = stmt.executeQuery();
			
			String username 	= rs.getString(2);
			String password 	= rs.getString(3);
			String firstname 	= rs.getString(4);
			String lastname 	= rs.getString(5);
			String email 		= rs.getString(6);
			int indexedRecords 	= rs.getInt(7);
			int currentBatchID	= rs.getInt(8);

			myUser = new User(username, password, firstname, lastname, email, indexedRecords, currentBatchID);
		}
		catch (SQLException e) 
		{
			System.out.println("[server.database.UserDAO]: get() not working...");
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}		
		finally 
		{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return myUser;	
	}
	
	/**
	 * Updates the specified user with updated fields.
	 * @param updateUser
	 */
	public void update(User updateUser) throws DatabaseException
	{
		PreparedStatement stmt = null;
		try
		{     
			String sql = "UPDATE User " 
						+ "SET password = ?, firstname = ?, lastname = ?, email = ?, recordsindexed = ?, currentbatch = ?"
						+ "WHERE  username = ?";
			
			stmt = database.getConnection().prepareStatement(sql);  
			stmt.setString(1, updateUser.getPassword());     
			stmt.setString(2, updateUser.getFirstname());
			stmt.setString(3, updateUser.getLastname());
			stmt.setString(4, updateUser.getEmail());
			stmt.setInt(5, updateUser.getRecordsIndexed());
			stmt.setInt(6, updateUser.getCurrentBatchID());
			stmt.setString(7, updateUser.getUsername());   

			if (stmt.executeUpdate() != 1) 
			{
				throw new DatabaseException("Could not update contact");
			}
		}
		catch (SQLException e) 
		{
			throw new DatabaseException("Could not update contact", e);
		}
		finally 
		{
			Database.safeClose(stmt);
		}
	}
}
