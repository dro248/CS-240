package shared.communication;

public class SearchParams extends UserParams
{
	private String fields;
	private String searchValues;	// comma delimited
	
	public SearchParams(String _username, String _password, String _fields, String _searchValues)
	{
		super(_username, _password);
		fields = _fields;
		searchValues = _searchValues;
	}

	public String getFields() 		{ return fields; 		}
	public String getSearchValues()	{ return searchValues; 	}
}
