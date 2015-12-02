package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import shared.communication.*;
import server.facade.*;

public class ValidateUserHandler implements HttpHandler 
{	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		UserParams params = (UserParams)xmlStream.fromXML(exchange.getRequestBody());
		UserResult result = null;
		
		String username = params.getUsername();
		String password = params.getPassword();
		
		try 
		{
			result = ServerFacade.validateUser(username, password);
			System.out.println("[validateUserHandler]: result returns " + result.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
}