package shared.communication;

import java.util.*;
import shared.model.*;

public class FieldResult 
{
	private List<Field> fieldsList;
	
	public FieldResult(List<Field> _fields)
	{
		fieldsList 	= _fields;
	}
	
	public List<Field> getFields()	{ return fieldsList;}
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		
		if(fieldsList == null)
		{
			output.append("FAILED\n");
			return output.toString();
		}
		
		if(fieldsList.size() > 0)
		{
			for(int i = 0; i < fieldsList.size(); i++)
			{
				output.append(fieldsList.get(i).getParentProjectID() 	+ "\n");
				output.append(fieldsList.get(i).getID()					+ "\n");
				output.append(fieldsList.get(i).getTitle()				+ "\n");
			}
		}
		
		return output.toString();
	}
}
