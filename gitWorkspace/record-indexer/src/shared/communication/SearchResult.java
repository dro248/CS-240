package shared.communication;

import java.util.*;

public class SearchResult
{
	private List<List<String>> results;
	
	public SearchResult(List<List<String>> _results)
	{
		results = _results;
	}
	
	public List<List<String>> getResults() { return results; } 
	public String toString(String url_prefix)
	{		
		StringBuilder output = new StringBuilder();
		for(List<String> myList : results)
		{
			for(int i = 0; i < myList.size(); i++)
			{
				if(i % 4 == 1)
				{
					output.append(url_prefix + "/Records/");
				}
				output.append(myList.get(i));
			}
		}
		return output.toString();
	}
}
