package shared.communication;

import java.util.*;
import shared.model.*;

public class ProjectResult
{
	List<Project> projects;
	boolean fail;
	
	public ProjectResult(List<Project> _projects, boolean _fail)
	{
		projects = _projects;
		fail = _fail;
	}
	
	public List<Project> getProjects() 	{ return projects; 	}
	public boolean fail()				{ return fail; 		}
}
