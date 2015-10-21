package listem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grep extends FileProcessor implements IGrep {

	// Fields
	Map<File, List<String>> matches = new HashMap<File, List<String>>();
	protected String mySubstringSelectionPattern = "";
	
	
	// Empty Constructor
	
	// Method
	@Override
	public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) 
	{
		matches = new HashMap<File, List<String>>();
		mySubstringSelectionPattern = substringSelectionPattern;
		
		processDirectory(directory, fileSelectionPattern, recursive);
		
		return matches;
	}
	
	@Override
	public void processLine(File f, String line) 
	{
		Pattern p = Pattern.compile(mySubstringSelectionPattern);
		Matcher m = p.matcher(line);
		List<String> list_of_lines = new ArrayList<String>();

		
		// IF pattern is found within the 'line' that we pass in...
		if(m.find())
		{
			// IF our file is already a key within our map...
			if(matches.containsKey(f))
			{
				list_of_lines = matches.get(f);				
			}
			
			// add line to list
			list_of_lines.add(line);
			
			// add entry to map
			matches.put(f, list_of_lines);
		}
	}

	@Override
	public void addEmptyFile(File f) {}
}
