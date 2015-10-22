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
	
	/**
	 * Adds a project to the database.
	 * @param project
	 * @return (boolean) whether or not the project is successfully added to the database.
	 */
	public void addProject(Project project) throws DatabaseException
	{ 
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try
		{     
			String sql = "INSERT INTO Project (title, recordsPerImage, firstYCoord, recordHeight) values (?,?,?,?)";
			
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
	}
	
	/**
	 * Gets a list of all projects.
	 * @return List of projects
	 */
	public List<Project> getAllProjects()	{ return null; }
	
	/**
	 * Get project by projectID
	 * @param project_id
	 * @return Project
	 */
	public Project getProject(int project_id) { return null; }

}
