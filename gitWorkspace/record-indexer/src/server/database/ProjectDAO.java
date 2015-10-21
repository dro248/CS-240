package server.database;

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
	public boolean addProject(Project project) { return false; }
	
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
