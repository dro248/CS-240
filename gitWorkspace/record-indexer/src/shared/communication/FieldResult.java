package shared.communication;

import java.util.*;
import shared.model.*;

public class FieldResult 
{
	List<Field> fields;
	boolean fail;
	
	public FieldResult(List<Field> _fields, boolean _fail)
	{
		fields = _fields;
		fail = _fail;
	}
	
	public List<Field> getFields()	{ return fields;	}
	public boolean fail() 			{ return fail;		}
}
