package client.communication;

import java.io.*;
import java.net.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communication.*;
import client.*;

public class ClientCommunicator 
{
/*	private static ClientCommunicator singleton = null;
	public static ClientCommunicator getSingleton()
	{
		if (singleton == null)
		{
			singleton = new ClientCommunicator("localhost", 45321);
		}
		
		return singleton;
	}*/
	
	
	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 8080;
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static final String HTTP_POST = "POST";
	
	private XStream xmlStream;
	
	public ClientCommunicator(String host, int port)
	{
		SERVER_HOST = host;
		SERVER_PORT = port;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
		xmlStream = new XStream(new DomDriver());
	}
	

	public UserResult validateUser(UserParams params) 					 throws ClientException 
		{ return (UserResult)doPost("/ValidateUser", params); }
	
	public ProjectResult getProjects(ProjectParams params) 				 throws ClientException 
		{ return (ProjectResult)doPost("/GetProjects", params); }
	
	public ImgResult getSampleImage(ImgParams params) 					 throws ClientException 
		{ return (ImgResult)doPost("/GetSampleImage", params); }
	
	public DownloadBatchResult downloadBatch(DownloadBatchParams params) throws ClientException 
		{ return (DownloadBatchResult)doPost("/DownloadBatch", params); }
	
	public SubmitBatchResult submitBatch(SubmitBatchParams params) 		 throws ClientException 
		{ return (SubmitBatchResult)doPost("/SubmitBatch", params); }
	
	public FieldResult getFields(FieldParams params) 					 throws ClientException 
		{ return (FieldResult)doPost("/GetFields", params); }
	
	public SearchResult search(SearchParams params) 					 throws ClientException 
		{ return (SearchResult)doPost("/Search", params); }
		
	private Object doPost(String urlPath, Object postData) 				 throws ClientException 
	{
		try 
		{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			
			// Get part
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}
			else 
			{
//				System.out.println("ResponseCode NOT 200. Server Sent something else.");
				throw new ClientException(String.format("doPost failed: %s (http code %d)",
											urlPath, connection.getResponseCode()));
			}
		}
		catch (IOException e) 
		{
//			System.out.println("doPost COMPLETELY failed.");
			throw new ClientException(String.format("doPost failed: %s", e.getMessage()), e);
		}
	}
	
	public String getURLPrefix()	{ return URL_PREFIX; }
}
