package server.database;

import java.sql.*;
import java.util.*;

import shared.model.*;

public class ProjectDAO
{

	Database database;
	
	public ProjectDAO(Database db)
	{
		database = db;
	}
	
	// DONE
	public void add(Project project) throws DatabaseException
	{ 
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try
		{
			database.startTransaction();
			//System.out.println("START_TRANSACTION: projectDAO add()");
			
			String sql = "INSERT INTO Project (title, recordsperimage, firstycoord, recordheight) values (?,?,?,?)";
			
			stmt = database.getConnection().prepareStatement(sql);
			stmt.setString(1, project.getTitle());     
			stmt.setInt(2, project.getRecordsPerImage());     
			stmt.setInt(3, project.getFirstYCoordinate());
			stmt.setInt(4, project.getRecordHeight());

			if (stmt.executeUpdate() == 1) 
			{
				Statement keyStmt = database.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				project.setID(id);
			}
			else 
			{
				database.endTransaction(false);
				//System.out.println("END_TRANSACTION: projectDAO add()");
				throw new DatabaseException("ERROR: Could not insert project.");
			}
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("END_TRANSACTION: projectDAO add()");
			throw new DatabaseException("ERROR: Could not insert project.", e);
		}
		finally 
		{
			database.endTransaction(true);
			//System.out.println("END_TRANSACTION: projectDAO add()");
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	// DONE
	public Project get(int projectID) throws DatabaseException
	{ 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Project project = null;
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Project WHERE ID = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, projectID);

			rs = stmt.executeQuery();
			
			if(rs.next())
			{
//				Project	(ID integer not null primary key autoincrement, title text, recordsperimage int, firstycoord int, recordheight int);
				
				int ID 				= rs.getInt(1);
				String title	 	= rs.getString(2);
				int recordsPerImage	= rs.getInt(3);
				int firstYCoord 	= rs.getInt(4);
				int recordHeight	= rs.getInt(5);

				project = new Project(ID, title, recordsPerImage, firstYCoord, recordHeight);
			}
		}
		catch (SQLException e) 
		{
			database.endTransaction(false);
			//System.out.println("[server.database.UserDAO]: get() not working...");
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}		
		finally 
		{
			database.endTransaction(true);
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return project;	
		
		
	}
	
	// DONE
	public List<Project> getAll(int projectID) throws DatabaseException
	{ 
		List<Project> result = new ArrayList<Project>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Project WHERE ID = ?";
			stmt = database.getConnection().prepareStatement(query);
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int ID 				= rs.getInt(1);
				String title	 	= rs.getString(2);
				int recordsPerImage	= rs.getInt(3);
				int firstYCoord 	= rs.getInt(4);
				int recordHeight	= rs.getInt(5);

				result.add(new Project(ID, title, recordsPerImage, firstYCoord, recordHeight));
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
	public List<Project> getAll() throws DatabaseException
	{ 
		List<Project> result = new ArrayList<Project>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try 
		{
			database.startTransaction();
			
			String query = "SELECT * FROM Project";
			stmt = database.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int ID 				= rs.getInt(1);
				String title	 	= rs.getString(2);
				int recordsPerImage	= rs.getInt(3);
				int firstYCoord 	= rs.getInt(4);
				int recordHeight	= rs.getInt(5);

				result.add(new Project(ID, title, recordsPerImage, firstYCoord, recordHeight));
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
