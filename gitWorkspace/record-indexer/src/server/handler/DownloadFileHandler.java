package server.handler;

import java.io.*;
import org.apache.commons.io.FileUtils;
import com.sun.net.httpserver.*;

public class DownloadFileHandler implements HttpHandler
{	
	public void handle(HttpExchange exchange) throws IOException
	{
		File f = new File("." + exchange.getRequestURI().getPath());
		exchange.sendResponseHeaders(200, 0);
		FileUtils.copyFile(f, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
}
