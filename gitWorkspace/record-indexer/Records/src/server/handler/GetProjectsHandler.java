package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import shared.communication.*;
import server.ServerException;
import server.facade.*;

public class GetProjectsHandler implements HttpHandler 
{
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{

		ProjectParams params = (ProjectParams)xmlStream.fromXML(exchange.getRequestBody());
		ProjectResult result = null;
		
		String username = params.getUsername();
		String password = params.getPassword();
		
		try 
		{
			result = ServerFacade.getProjects(username, password);
		}
		catch (ServerException e) 
		{
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
}