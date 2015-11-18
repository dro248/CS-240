package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import shared.communication.*;
import server.ServerException;
import server.facade.*;

public class GetFieldsHandler implements HttpHandler 
{
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		FieldParams params = (FieldParams)xmlStream.fromXML(exchange.getRequestBody());
		FieldResult result = null;
		
		String username		= params.getUsername();
		String password		= params.getPassword(); 
		int parentProjectID	= params.getParentProjectID();
		
		try 
		{
			result = ServerFacade.getFields(username, password, parentProjectID);
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