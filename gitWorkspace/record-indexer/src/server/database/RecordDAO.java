package server.database;

import java.sql.*;
import java.util.*;

import shared.model.*;

public class RecordDAO 
{
	Database database;
	
	public RecordDAO(Database db)
	{
		database = db;
	}

	// DONE
	public void add(Record record) throws DatabaseException
	{
		if(record.getParentBatchID() < 0)
		{
			// record not valid
			throw new DatabaseException();
		}
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try 
		{
			database.startTransaction();
			
//			Record	( ID integer not null primary key autoincrement, parentBatchID int);	
			String query = "INSERT INTO Record (parentBatchID) values (?)";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt		(1, record.getParentBatchID());

			if (stmt.executeUpdate() == 1) 
			{
				Statement keyStmt = database.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("SELECT last_insert_rowid()");
				keyRS.next();
				record.setID(keyRS.getInt(1));
			} 
			else 
			{ 
				database.endTransaction(false); 
				throw new DatabaseException("ERROR: Record not added!"); 
			}
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			throw new DatabaseException("ERROR: Could not add record to database!", e);
		} 
		finally 
		{
			database.endTransaction(true);
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}	
	}
	
	// DONE
	public Record get(int recordID) throws DatabaseException
	{ 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Record record = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Record WHERE ID = ?";
			
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, recordID);
			rs = stmt.executeQuery();
			
			stmt.setInt	(1, recordID);
			
			if (rs.next()){
				int parentBatchID = 	rs.getInt(2);
			
				record = new Record(recordID, parentBatchID);
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
		
		return record; 
	}
	
	// DONE
	public List<Record> getAll() throws DatabaseException
	{
		List<Record> result = new ArrayList<Record>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Record";
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int ID 				=  rs.getInt(1);
				int parentBatchID	=  rs.getInt(2);

				result.add(new Record(ID, parentBatchID));
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
	public List<Record> getAll(int _batchID) throws DatabaseException
	{
		List<Record> result = new ArrayList<Record>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Record WHERE parentBatchID = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, _batchID);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int ID 				=  rs.getInt(1);
				
				result.add(new Record(ID, _batchID));
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
}