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
	 * @return Returns whether or not user is successfully added to database.
	 */
	public boolean addUser(User newUser) 
	{ 
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try 
		{
			String query = "insert into User (username, password, firstname, lastname, email, recordsIndexed, currentBatchID)"
							+ " values (?, ?, ?, ?, ?, ?, ?)";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setString(1, newUser.getUsername());
			stmt.setString(2, newUser.getPassword());
			stmt.setString(3, newUser.getFirstname();
			stmt.setString(4, newUser.getLastname());
			stmt.setString(5, newUser.getEmail());
			stmt.setInt(5, newUser.getRecordsIndexed());
			stmt.setInt(5, newUser.getCurrentBatchID());
			
			if (stmt.executeUpdate() == 1) 
			{
				Statement keyStmt = database.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				newUser.setId(id);
			}
			else 
			{
				throw new DatabaseException("Could not insert contact");
			}
		}
		catch (SQLException e) 
		{
			throw new DatabaseException("Could not insert contact", e);
		}
		finally 
		{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}

		
		return false; 
	}
	
	/**
	 * Check if username and password are contained in the database
	 * @param username
	 * @param password
	 * @return Returns user with specified username & password is returned.
	 */
	public User getUser(String username, String password) { return null; }
	
	/**
	 * Updates the specified user with updated fields.
	 * @param updateUser
	 */
	public void updateUser(User updateUser) throws DatabaseException
	{
		PreparedStatement stmt = null;
		try
		{     
			String sql = "update User " 
						+ "set username = ?, password = ?, firstname = ?, lastname = ?, email = ?, recordsIndexed = ?, currentBatch = ?"
						+ "where id = ?";
			
			stmt = database.getConnection().prepareStatement(sql);
			stmt.setString(1, updateUser.getUsername());     
			stmt.setString(2, updateUser.getPassword());     
			stmt.setString(3, updateUser.getFirstname());
			stmt.setString(4, updateUser.getLastname());
			stmt.setString(5, updateUser.getEmail());
			stmt.setInt(6, updateUser.getRecordsIndexed());
			stmt.setInt(7, updateUser.getCurrentBatchID());

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