package server.database;

import java.sql.*;
import java.util.*;

import shared.model.*;

public class BatchDAO 
{
	Database database;
	
	public BatchDAO(Database db)
	{
		database = db;
	}
	
	// DONE
	public void add(Batch batch) throws DatabaseException 
	{ 
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try 
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: batchDAO add()");
			
			String query = "INSERT INTO Batch (file, isAvailable, parentProjectID) values (?, ?, ?)";
			stmt = database.getConnection().prepareStatement(query);
			
			stmt.setString	(1, batch.getUrl());
			stmt.setBoolean	(2, batch.isAvailable());
			stmt.setInt		(3, batch.getParentProjectID());
			
			if (stmt.executeUpdate() == 1) 
			{
				Statement keyStmt = database.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("SELECT last_insert_rowid()");
				keyRS.next();
				batch.setID(keyRS.getInt(1));
			} 
			else 
			{
				database.endTransaction(false);
				//System.out.println("END_TRANSACTION: batchDAO add()");
				throw new DatabaseException("Could not insert batch");
			}
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: batchDAO add()");
			throw new DatabaseException("Could not add new batch!",e);
		}
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: batchDAO add()");
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	// DONE
	public Batch get(int batchID) throws DatabaseException 
	{ 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Batch batch = null;
		
		try
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: batchDAO get()");
			
			String query = "SELECT * FROM Batch WHERE ID = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, batchID);
			rs = stmt.executeQuery();
			
			if (rs.next())
			{
//				Batch (ID integer not null primary key autoincrement, file text, isAvailable boolean, parentProjectID int );
				
//				int ID				= rs.getInt(1);
				String url			= rs.getString(2);
				boolean isAvailable	= rs.getBoolean(3);
				int parentProjectID	= rs.getInt(4);
			
				batch = new Batch(batchID, parentProjectID, isAvailable, url);
			}
			
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("START_TRANSACTION: batchDAO get()");
			throw new DatabaseException(e.getMessage(), e);
		} 
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: batchDAO get()");
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return batch;
	}
	
	// DONE
	public Batch getFirstAvailableBatch(int projectID) throws DatabaseException 
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Batch batch = null;
		
		try
		{
			database.startTransaction();
			String query = "SELECT * FROM Batch WHERE isAvailable = 1 AND parentProjectID = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			
			if (rs.next())
			{
//				Batch (ID integer not null primary key autoincrement, file text, isAvailable boolean, parentProjectID int );
				
				int ID				= rs.getInt(1);
				String url			= rs.getString(2);
//				boolean isAvailable	= rs.getBoolean(3);
//				int parentProjectID	= rs.getInt(4);
			
				batch = new Batch(ID, projectID, true, url);
			}
			
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: batchDAO getFirstAvailableBatch()");
			throw new DatabaseException(e.getMessage(), e);
		} 
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: batchDAO getFirstAvailableBatch()");
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return batch;
	}
	
	// DONE
	public List<Batch> getAll() throws DatabaseException
	{
		List<Batch> result = new ArrayList<Batch>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: batchDAO getAll()");
			
			String query = "SELECT * FROM Batch";
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
//				Batch (ID integer not null primary key autoincrement, file text, isAvailable boolean, parentProjectID int );
				
				int ID				= rs.getInt(1);
				String url			= rs.getString(2);
				boolean isAvailable	= rs.getBoolean(3);
				int parentProjectID	= rs.getInt(4);
			
				result.add(new Batch(ID, parentProjectID, isAvailable, url));
			}
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: batchDAO getAll()");
			throw new DatabaseException(e.getMessage(), e);
		} 
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: batchDAO getAll()");
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return result;
	}
	
	// DONE
	public void update(Batch batch) throws DatabaseException
	{
		PreparedStatement stmt = null;

		try 
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: batchDAO update()");
			
			String query = "UPDATE Batch SET isAvailable = ? WHERE ID = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setBoolean	(1, batch.isAvailable());
			stmt.setInt		(2, batch.getID());	
			
			if (stmt.executeUpdate() != 1) 
			{
				database.endTransaction(false);
				//System.out.println("END_TRANSACTION: batchDAO update()");
				throw new DatabaseException("ERROR: Batch not updated!");
			}
		} 
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: batchDAO update()");
			throw new DatabaseException("ERROR: Batch not updated!", e);
		} 
		finally 
		{ 
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: batchDAO update()");
			Database.safeClose(stmt); 
		}
	} 
}
