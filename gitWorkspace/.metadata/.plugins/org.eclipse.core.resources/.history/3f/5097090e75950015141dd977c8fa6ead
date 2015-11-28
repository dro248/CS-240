package client.controllers;

import java.util.*;
import client.communication.*;
import client.views.LoginGUI;
import servertester.views.*;
import shared.communication.*;

public class LoginController
{
//	private ClientCommunicator cc;
	private LoginGUI login;
	
	public LoginController() 
	{
		return;
	}
	
	public LoginGUI getGUI() 
	{
		return login;
	}
	
	public void setGUI(LoginGUI value) 
	{
		login = value;
	}
	
	public void initialize() 
	{
//		operationSelected();
	}

	
	
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
	

	public void executeOperation() 
	{
		validateUser();
	}

	private void validateUser() 
	{
		// CREATE A ClientCommunicator object
		// in IView... there are getters and setters for the GUI
		ClientCommunicator cc 	= new ClientCommunicator(getGUI().getHost(), Integer.parseInt(getGUI().getPort()));
		String[] values			= getGUI().getParameterValues();
		UserParams params		= new UserParams(values[0], values[1]);
		
		try
		{
			UserResult results	= cc.validateUser(params);
			getGUI().setResponse(results.toString());
		}
		catch(Exception e)
		{
			getGUI().setResponse("FAILED\n");
		}
	}
/*
	private void getProjects() 
	{
		ClientCommunicator cc 	= new ClientCommunicator(getGUI().getHost(), Integer.parseInt(getGUI().getPort()));
		String[] values			= getGUI().getParameterValues();
		ProjectParams params	= new ProjectParams(values[0], values[1]);
		
		try
		{
			ProjectResult results = cc.getProjects(params);
			getGUI().setResponse(results.toString());
		}
		catch(Exception e)
		{
			getGUI().setResponse("FAILED\n");
		}
	}
*/
	
}

