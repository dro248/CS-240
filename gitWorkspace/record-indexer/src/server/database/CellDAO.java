package server.database;

import java.sql.*;
import java.util.*;
import shared.model.*;

public class CellDAO
{
	Database database;
	
	public CellDAO(Database db)
	{
		database = db;
	}
	
	public void add(Cell cell) throws DatabaseException 
	{		
		PreparedStatement stmt = null;	
		try 
		{
			database.startTransaction();
			
			String query = "INSERT INTO Cell (parentRecordCoord, parentFieldCoord, cellValue) values (?, ?, ?);";
			stmt = database.getConnection().prepareStatement(query);
			
			stmt.setInt		(1, cell.getParentRecordID());
			stmt.setInt		(2, cell.getParentFieldID());
			stmt.setString	(3, cell.getValue());
			
			stmt.execute();
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			throw new DatabaseException("ERROR: Could not add new cell!");
		}
		finally 
		{
			database.endTransaction(true);
			Database.safeClose(stmt);
		}
	}
	
	public Cell get(int fieldCoord, int recordCoord) throws DatabaseException
	{ 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Cell cell = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Cell WHERE parentRecordCoord = ? AND parentFieldCoord = ?";

			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, fieldCoord);
			stmt.setInt(2, recordCoord);
			rs = stmt.executeQuery();
					
			if (rs.next())
			{
				cell = new Cell(recordCoord, fieldCoord, rs.getString(2)); 
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
		
		return cell;
	}
	
	public List<Cell> getAll() throws DatabaseException
	{ 
		List<Cell> result = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Cell;";
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int parentRecordID 	= rs.getInt(2);
				int parentFieldID 	= rs.getInt(3);
				String cellValue	= rs.getString(4);

				result.add(new Cell( parentRecordID, parentFieldID, cellValue));
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
	
	public List<Cell> getAll(int fieldID) throws DatabaseException
	{ 
		List<Cell> result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			database.startTransaction();

			String query = "SELECT * FROM Cell WHERE parentFieldCoord = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, fieldID);
			rs = stmt.executeQuery();
			
			result = new ArrayList<Cell>();
			
			while (rs.next()) 
			{
				int parentRecordID 	= rs.getInt(2);
				String cellValue	= rs.getString(4);

				result.add(new Cell( parentRecordID, fieldID, cellValue));
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
