package shared.communication;

import java.util.*;
import shared.model.*;

public class DownloadBatchResult 
{
	private Batch batch;
	private Project project;
	private List<Field> fieldsList;
	
	public DownloadBatchResult(Batch _batch, Project _project, List<Field> _fieldsList)
	{
		batch 		= _batch;
		project 	= _project;
		fieldsList	= _fieldsList;
	}
	
	// GETTERS
	public Batch getBatch() 		{ return batch; 	}
	public List<Field> getFields() 	{ return fieldsList;}
	public Project getProject() 	{ return project; 	}
	
	public String toString(String url_prefix)
	{	
		if(	batch.getID() 				 < 1	|| batch.getParentProjectID() 	 < 1	|| batch.getUrl() 		== null	||
			project.getID()				 < 1 	|| project.getRecordHeight()	 < 1	|| project.getTitle() 	== null	||
			project.getRecordsPerImage() < 1 	|| project.getFirstYCoordinate() < 1	|| fieldsList 			== null	||
			batch == null						|| project == null)
		{
			return "FAILED\n";
		}
		
		StringBuilder output = new StringBuilder();		
		
		output.append(batch.getID() 									+ 	"\n");
		output.append(project.getID()			 						+ 	"\n");
		output.append(url_prefix + "/Records/" + batch.getUrl() 		+ 	"\n");
		output.append(project.getFirstYCoordinate()						+	"\n");
		output.append(project.getRecordHeight()							+	"\n");
		output.append(project.getRecordsPerImage()						+	"\n");
		output.append(fieldsList.size()									+	"\n");

		for(Field f : fieldsList)
		{				
			output.append(f.getID() 									+ 	"\n");
			output.append(f.getColumnNumber() 							+ 	"\n");
			output.append(f.getTitle()									+ 	"\n");
			output.append(url_prefix + "/Records/" + f.getHelpHTML()	+ 	"\n");
			output.append(f.getXCoordinate()							+ 	"\n");
			output.append(f.getWidth()									+ 	"\n");
			
			if(!f.getKnownData().equals(""))
				output.append(url_prefix +"/Records/" +f.getKnownData()	+ 	"\n");
		}

		return output.toString();
	}
}


