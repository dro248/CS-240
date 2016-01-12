package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import shared.communication.*;
import server.ServerException;
import server.facade.*;

public class SubmitBatchHandler implements HttpHandler 
{
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		SubmitBatchParams params = (SubmitBatchParams)xmlStream.fromXML(exchange.getRequestBody());
		SubmitBatchResult result = null;
		
		String username 	= params.getUsername();
		String password 	= params.getPassword();
		int batchID 		= params.getBatchID();
		String fieldValues	= params.getFieldValues();
		
		try 
		{
			result = ServerFacade.submitBatch(username, password, batchID, fieldValues);
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