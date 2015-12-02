package shared.communication;

public class ImgResult
{
	private String url;
	
	public ImgResult(String _url)
	{
		url = _url;
	}
	public String getUrl()		{ return url;	}
	public String toString(String url_prefix)
	{		
		if(url == null)
		{
			return "FAILED\n";
		}
		
		return url_prefix + "/Records/" + url + '\n';
	}
}
