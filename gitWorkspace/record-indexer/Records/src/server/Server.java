package server;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import server.facade.*;
import server.handler.*;

public class Server
{
	private static int SERVER_PORT_NUMBER = 45321;
	private static final int MAX_WAITING_CONNECTIONS = 10;
		
	private HttpServer server;
	
	public Server(int port)
	{
		SERVER_PORT_NUMBER = port;
		run(port);
	}
	
	private Server() 
	{
		return;
	}
	
	private void run(int port) 
	{		
		try 
		{
			ServerFacade.initialize();		
		}
		catch (ServerException e) 
		{
			return;
		}
		
		try 
		{
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) 
		{		
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/",                   	downloadFileHandler);
		server.createContext("/ValidateUser", 			validateUserHandler);
		server.createContext("/GetProjects", 			getProjectsHandler);
		server.createContext("/GetSampleImage", 		getSampleImageHandler);
		server.createContext("/DownloadBatch", 			downloadBatchHandler);
		server.createContext("/SubmitBatch", 			submitBatchHandler);
		server.createContext("/GetFields", 				getFieldsHandler);
		server.createContext("/Search", 				searchHandler);

		server.start();
	}
		
	private HttpHandler downloadFileHandler 	= new DownloadFileHandler();
	private HttpHandler validateUserHandler 	= new ValidateUserHandler();
	private HttpHandler getProjectsHandler 		= new GetProjectsHandler();
	private HttpHandler getSampleImageHandler 	= new GetSampleImageHandler();
	private HttpHandler downloadBatchHandler 	= new DownloadBatchHandler();
	private HttpHandler submitBatchHandler 		= new SubmitBatchHandler();
	private HttpHandler getFieldsHandler 		= new GetFieldsHandler();
	private HttpHandler searchHandler 			= new SearchHandler();
	
	
	public static void main(String[] args) 
	{
		int port = Integer.parseInt(args[0]);
		
		if(port <= 0)
		{
			port = SERVER_PORT_NUMBER;
		}
		else
		{
			SERVER_PORT_NUMBER = port;
		}
		
		
		new Server().run(SERVER_PORT_NUMBER);
	}
}