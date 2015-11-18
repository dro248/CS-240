package shared.communication;

import java.util.*;

import shared.model.*;

public class ProjectResult
{
	private List<Project> projects;
	
	public ProjectResult(List<Project> _projects)
	{
		projects = _projects;
	}
	
	public List<Project> getProjects() 	{ return projects; 	}
	public String toString()
	{		
		StringBuilder output = new StringBuilder();
		for(Project p : projects)
		{
			output.append( p.getID() 	+ "\n"
						 + p.getTitle()	+ "\n");
		}
		return output.toString();
	}
}
