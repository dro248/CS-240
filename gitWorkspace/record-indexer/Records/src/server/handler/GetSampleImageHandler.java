package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import shared.communication.*;
import server.facade.*;

public class GetSampleImageHandler implements HttpHandler 
{	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		ImgParams params = (ImgParams)xmlStream.fromXML(exchange.getRequestBody());
		ImgResult result = null;
		
		String username		= params.getUsername(); 
		String password		= params.getPassword(); 
		int parentProjectID = params.getParentProjectID();
		
		try 
		{
			result = ServerFacade.getSampleImage(username, password, parentProjectID);
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