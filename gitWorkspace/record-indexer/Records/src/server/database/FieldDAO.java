package server.database;

import java.sql.*;
import java.util.*;

import shared.model.*;


public class FieldDAO 
{
	Database database;
	
	public FieldDAO(Database db)
	{
		database = db;
	}
	
	// DONE
	public void add(Field field) throws DatabaseException 
	{ 
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try 
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: fieldDAO add()");
			
//			Field( ID integer not null primary key autoincrement, title text, xcoord int, width int, helphtml text, knowndata text, parentProjectID int, columnNumber int );
			String query = "INSERT INTO Field (title, xcoord, width, helphtml, knowndata, parentProjectID, columnNumber) values (?, ?, ?, ?, ?, ?, ?)";
			
			stmt = database.getConnection().prepareStatement(query);
			stmt.setString	(1, field.getTitle());
			stmt.setInt		(2, field.getXCoordinate());
			stmt.setInt		(3, field.getWidth());
			stmt.setString	(4, field.getHelpHTML());
			stmt.setString	(5, field.getKnownData());
			stmt.setInt		(6, field.getParentProjectID());
			stmt.setInt		(7, field.getColumnNumber());
			
			if(stmt.executeUpdate() == 1)
			{
				Statement keyStmt = database.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("SELECT last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				field.setID(id);
			}	
			else 
			{
				database.endTransaction(false);
				//System.out.println("END_TRANSACTION: fieldDAO add()");
				throw new DatabaseException("ERROR: Could not add new Field to database");
			}
		}
		catch (SQLException e) 
		{
//			e.printStackTrace();
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: fieldDAO add()");
			throw new DatabaseException("ERROR: Could not add new Field to database", e);
		}
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: fieldDAO add()");
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}

	// DONE
	public List<Field> getAll() throws DatabaseException
	{
		List<Field> result = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
//			Field(ID integer not null primary key autoincrement, title text, xcoord int, width int, helphtml text, knowndata text, parentProjectID int, columnNumber int );
			String query = "SELECT * FROM Field";
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int ID 				= rs.getInt(1);
				String title 		= rs.getString(2);
				int xcoord 			= rs.getInt(3);
				int width 			= rs.getInt(4);
				String helphtml 	= rs.getString(5);
				String knowndata 	= rs.getString(6);
				int parentProjectID = rs.getInt(7);
				int column	 		= rs.getInt(8);
				
				result.add(new Field(ID, title, xcoord, width, helphtml, knowndata, parentProjectID, column));
			}
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			throw new DatabaseException(e.getMessage(), e);
		}
		finally 
		{
			database.endTransaction(true);
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return result;
	}

	// DONE
	public List<Field> getFieldsByProjectID(int _parentProjectID) throws DatabaseException 
	{
		List<Field> result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Field WHERE parentProjectID = " + _parentProjectID;
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			result = new ArrayList<Field>();
			
			while (rs.next()) 
			{
				int id 					= rs.getInt(1);
				String title 			= rs.getString(2);
				int xcoord 				= rs.getInt(3);
				int width 				= rs.getInt(4);
				String helphtml			= rs.getString(5);
				String knowndata		= rs.getString(6);
				int parentProjectID		= rs.getInt(7);
				int columnNumber		= rs.getInt(8);

				result.add(new Field(id, title, xcoord, width, helphtml, knowndata, parentProjectID, columnNumber));
			}
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}		
		finally 
		{
			database.endTransaction(true);
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return result;	
	}
}
