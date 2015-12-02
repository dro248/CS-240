package server.database;

import shared.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO 
{
	Database database;
	
	public UserDAO(Database db)
	{
		database = db;
	}
	
	// DONE
	public void add(User user) throws DatabaseException
	{	
		if (user.getUsername()	  	== null 		||
			user.getPassword()		== null 		||
			user.getFirstname()		== null 		||
			user.getLastname()		== null 		||
			user.getEmail()	  		== null 		||
			user.getRecordsIndexed() < 0 			||
			user.getCurrentBatchID() < 0) 
		{ 
			throw new DatabaseException("ERROR: Invalid input!");
		}
		
		PreparedStatement stmt = null;	
		try 
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: userDAO add()");
			
//			User ( ID integer not null primary key autoincrement, username text, password text, firstname text, lastname text, email text, indexedRecords int, currentBatchID int );
			String query = "INSERT INTO User (username, password, firstname, lastname, email, indexedRecords, currentBatchID) "
						 + "values (?, ?, ?, ?, ?, ?, ?)";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setString	(1, user.getUsername());
			stmt.setString	(2, user.getPassword());
			stmt.setString	(3, user.getFirstname());
			stmt.setString	(4, user.getLastname());
			stmt.setString	(5, user.getEmail());
			stmt.setInt		(6, user.getRecordsIndexed());
			stmt.setInt		(7, user.getCurrentBatchID());
			
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: userDAO add()");
			throw new DatabaseException("Could not add new user!");
		}
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: userDAO add()");
			Database.safeClose(stmt);
		}
	}
	
	// DONE
	public User get(String _username, String _password) throws DatabaseException
	{	
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User myUser = null;
		
		try 
		{
			database.startTransaction();
			
//			User (ID integer not null primary key autoincrement, username text, password text, firstname text, lastname text, email text, indexedRecords int, currentBatchID int );
			String query = "SELECT * FROM User WHERE username = ? AND password = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setString(1, _username);
			stmt.setString(2, _password);

			rs = stmt.executeQuery();
			
			if(rs.next())
			{
//				String username 	= rs.getString(2);
//				String password 	= rs.getString(3);
				String firstname 	= rs.getString(4);
				String lastname 	= rs.getString(5);
				String email 		= rs.getString(6);
				int indexedRecords 	= rs.getInt(7);
				int currentBatchID	= rs.getInt(8);

				myUser = new User(_username, _password, firstname, lastname, email, indexedRecords, currentBatchID);
			}
		}
		catch (Exception e) 
		{
			database.endTransaction(false);
//			System.out.println("END_TRANSACTION: userDAO get()");
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}		
		finally 
		{
			database.endTransaction(true);
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return myUser;	
	}

	// DONE
	public List<User> getAll() throws DatabaseException
	{
		List<User> result = new ArrayList<User>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
//			User (ID integer not null primary key autoincrement, username text, password text, firstname text, lastname text, email text, indexedRecords int, currentBatchID int );
			String query = "SELECT * FROM User";
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				String username =		rs.getString(2);
				String password =		rs.getString(3);
				String firstname =		rs.getString(4);
				String lastname = 		rs.getString(5);
				String email = 			rs.getString(6);
				int indexedrecords = 	rs.getInt(7);
				int currentBatchID = 	rs.getInt(8);

				result.add(new User(username, password, firstname, lastname, email, indexedrecords, currentBatchID));
			}
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: userDAO getAll()");
			throw new DatabaseException(e.getMessage(), e);
		} 
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: userDAO getAll()");
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return result;
	}
	
	//	DONE
	public void update(User user) throws DatabaseException
	{
		PreparedStatement stmt = null;
		try
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: userDAO update()");
			
//			User (ID integer not null primary key autoincrement, username text, password text, firstname text, lastname text, email text, indexedRecords int, currentBatchID int );
			String sql = "UPDATE User " 
						+ "SET password = ?, firstname = ?, lastname = ?, email = ?, indexedRecords = ?, currentBatchID = ?"
						+ "WHERE  username = ?";
			
			stmt = database.getConnection().prepareStatement(sql);  
			stmt.setString	(1, user.getPassword());     
			stmt.setString	(2, user.getFirstname());
			stmt.setString	(3, user.getLastname());
			stmt.setString	(4, user.getEmail());
			stmt.setInt		(5, user.getRecordsIndexed());
			stmt.setInt		(6, user.getCurrentBatchID());
			stmt.setString	(7, user.getUsername());   

			if (stmt.executeUpdate() != 1) 
			{
				throw new DatabaseException("Could not update user.");
			}
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: userDAO update()");
			throw new DatabaseException("Could not update user.", e);
		}
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: userDAO update()");
			Database.safeClose(stmt);
		}
	}
}
