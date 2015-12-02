package servertester.controllers;

import java.util.*;

import client.communication.*;
import servertester.views.*;
import shared.communication.*;

public class Controller implements IController 
{
	private IView _view;
	
	public Controller() 
	{
		return;
	}
	
	public IView getView() 
	{
		return _view;
	}
	
	public void setView(IView value) 
	{
		_view = value;
	}
	
	@Override
	public void initialize() 
	{
		getView().setHost("localhost");
		getView().setPort("45321");
		operationSelected();
	}

	@Override
	public void operationSelected() 
	{
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) 
		{
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() 
	{
		switch (getView().getOperation()) 
		{
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}

	private void validateUser() 
	{
		// CREATE A ClientCommunicator object
		// in IView... there are getters and setters for the GUI
		ClientCommunicator cc 	= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values			= getView().getParameterValues();
		UserParams params		= new UserParams(values[0], values[1]);
		
		try
		{
			UserResult results	= cc.validateUser(params);
			getView().setResponse(results.toString());
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}

	private void getProjects() 
	{
		ClientCommunicator cc 	= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values			= getView().getParameterValues();
		ProjectParams params	= new ProjectParams(values[0], values[1]);
		
		try
		{
			ProjectResult results = cc.getProjects(params);
			getView().setResponse(results.toString());
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}

	private void getSampleImage() 
	{
		ClientCommunicator cc 	= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values			= getView().getParameterValues();
		ImgParams params		= new ImgParams(values[0], values[1], Integer.parseInt(values[2]));
		
		try
		{
			ImgResult results	= cc.getSampleImage(params);
			getView().setResponse(results.toString(cc.getURLPrefix()));
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}
	
	private void downloadBatch() 
	{
		ClientCommunicator cc 		= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values				= getView().getParameterValues();
		DownloadBatchParams params	= new DownloadBatchParams(values[0], values[1], Integer.parseInt(values[2]));
		
		try
		{
			DownloadBatchResult results	= cc.downloadBatch(params);
			getView().setResponse(results.toString(cc.getURLPrefix()));
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getFields() 
	{
		ClientCommunicator cc 	= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values			= getView().getParameterValues();
		FieldParams params 		= null;
		int projectID			= -1;
		
		if(!values[2].equals(""))
		{
			if(Integer.parseInt(values[2]) == -1)
			{
				values[2] = "0";
			}
			else
			{
				projectID = Integer.parseInt(values[2]);
			}
		}
		
		params = new FieldParams(values[0], values[1], projectID);
		
		try
		{
			FieldResult results	= cc.getFields(params);
			getView().setResponse(results.toString());
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}

	private void submitBatch() 
	{
		ClientCommunicator cc 		= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values				= getView().getParameterValues();
		SubmitBatchParams params 	= new SubmitBatchParams(values[0], values[1], Integer.parseInt(values[2]), values[3]);
		
		try
		{
			SubmitBatchResult results = cc.submitBatch(params);
			getView().setResponse(results.toString());
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}

	private void search() 
	{
		ClientCommunicator cc	= new ClientCommunicator(getView().getHost(), Integer.parseInt(getView().getPort()));
		String[] values			= getView().getParameterValues();
		SearchParams params	= new SearchParams(values[0], values[1], values[2], values[3]);
		
		try
		{
			SearchResult results	= cc.search(params);
			getView().setResponse(results.toString(cc.getURLPrefix()));
		}
		catch(Exception e)
		{
			getView().setResponse("FAILED\n");
		}
	}
}

