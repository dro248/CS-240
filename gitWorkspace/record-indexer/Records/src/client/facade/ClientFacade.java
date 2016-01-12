package client.facade;

import shared.communication.*;
import client.communication.ClientCommunicator;

public class ClientFacade 
{
	private String HOST;
	private int PORT;
	private BatchState batchState;
	
	
	// GETTERS & SETTERS
	public void 		setHost(String host){ HOST = host; }
	public void			setPort(int port)	{ PORT = port; }
	public String 		getHost() 			{ return HOST; }
	public int			getPort()			{ return PORT; }
	public BatchState 	getBatchState() 	{ return batchState; }
	
	private static ClientFacade facade_SINGLETON = new ClientFacade();
	
	public static ClientFacade get()
	{	
		return facade_SINGLETON;
	}
	
	public UserResult validateUser(UserParams params) 
	{
		// CREATE A ClientCommunicator object
		ClientCommunicator cc = new ClientCommunicator(HOST, PORT);
		
		try
		{
			UserResult results = cc.validateUser(params);
			if(results != null) { batchState = new BatchState(results.getUser()); }

			return results;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public DownloadBatchResult downloadBatch(DownloadBatchParams params)
	{
		ClientCommunicator cc = new ClientCommunicator(HOST, PORT);
		
		try
		{
			DownloadBatchResult results = cc.downloadBatch(params);
			if(results != null)	
			{ 
//				System.out.println("DownloadBatch entered IF (NOT null)");
				batchState.setBatch(results.getBatch()); 
			}
			
//			System.out.println("DownloadBatchResult is: " + results);
			return results;			
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public SubmitBatchResult submitBatch(SubmitBatchParams params)
	{
		ClientCommunicator cc = new ClientCommunicator(HOST, PORT);
		
		try
		{
			SubmitBatchResult results = cc.submitBatch(params);
			if(results != null)	
			{ 
				System.out.println("SubmitBatch Succeeded!");
//				batchState.getBatch(results.getBatch()); 
			}
			
//			System.out.println("DownloadBatchResult is: " + results);
			return results;			
		}
		catch(Exception e)
		{
			System.out.println("SubmitBatch Failed!");
			return null;
		}
	}
	
	public ProjectResult getProjects(ProjectParams params)
	{
		ClientCommunicator cc = new ClientCommunicator(HOST, PORT);
		
		try
		{
			ProjectResult results = cc.getProjects(params);
			return results;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public ImgResult getSampleImage(ImgParams params)
	{
		ClientCommunicator cc = new ClientCommunicator(HOST, PORT);
		
		try
		{
			ImgResult results = cc.getSampleImage(params);
			return results;
		}
		catch(Exception e)
		{
			return null;
		}
	}


}