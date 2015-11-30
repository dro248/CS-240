package client.facade;

import shared.communication.*;
import client.communication.ClientCommunicator;

public class ClientFacade 
{
	private String HOST;
	private int PORT;
	private BatchState batchState;
	
	
	// GETTERS & SETTERS
	public void 	setHost(String host){ HOST = host; }
	public void		setPort(int port)	{ PORT = port; }
	public String 	getHost() 			{ return HOST; }
	public int		getPort()			{ return PORT; }
	
	
	private static ClientFacade facade_SINGLETON = new ClientFacade();
	
	public static ClientFacade get()
	{	
		return facade_SINGLETON;
	}
	
	public UserResult validateUser(UserParams params) 
	{
		// CREATE A ClientCommunicator object
		ClientCommunicator cc 	= new ClientCommunicator(HOST, PORT);
		
		try
		{
			UserResult results	= cc.validateUser(params);
			if(results != null) { batchState = new BatchState(results.getUser()); }

			return results;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	
	
}