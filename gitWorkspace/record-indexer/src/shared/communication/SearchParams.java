package shared.communication;

import shared.model.*;
import java.util.*;

public class SearchParams extends UserParams
{

	List<Field> fields;
	String searchValues;	// comma delimited
	
	public SearchParams(String _username, String _password, List<Field> _fields, String _searchValues)
	{
		super(_username, _password);
		fields = _fields;
		searchValues = _searchValues;
	}

	
	public List<Field> getFields() { return fields; 		}
	public String getSearchValues(){ return searchValues; 	}
}
