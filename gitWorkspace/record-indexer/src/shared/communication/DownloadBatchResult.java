package shared.communication;

import java.util.*;
import shared.model.*;

public class DownloadBatchResult 
{
	Batch batch;
	List<Field> fields;
	Project project;
	boolean fail;
	
	public DownloadBatchResult(boolean _fail)
	{
		fail = _fail;
	}
	
	// SETTERS
	public void setBatch(Batch newBatch)			{ batch = newBatch;		}
	public void setFields(List<Field> newFields) 	{ fields = newFields;	}
	public void setProject(Project newProject)		{ project = newProject;	}
	
	// GETTERS
	public Batch getBatch() 		{ return batch; 	}
	public List<Field> getField() 	{ return fields; 	}
	public Project getProject() 	{ return project; 	}
	public boolean fail()			{ return fail;		}
}


