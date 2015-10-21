package listem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LineCounter extends FileProcessor implements ILineCounter {

	// Fields
	Map<File, Integer> countLines = new HashMap<File, Integer>();
	
	
	// Empty Constructor
	
	// Method
	
	@Override
	public Map<File, Integer> countLines(File directory, String fileSelectionPattern, boolean recursive) 
	{		
		countLines = new HashMap<File, Integer>();
		processDirectory(directory, fileSelectionPattern, recursive);

		return countLines;
	}

	@Override
	public void processLine(File f, String line) 
	{
		int lineCount = 1;
		
		// IF our file is already a key within our map...
		if(countLines.containsKey(f))
		{
			lineCount = countLines.get(f);	
			lineCount++;
		}
		
		countLines.put(f, lineCount);
	}

	@Override
	public void addEmptyFile(File f) 
	{
		countLines.put(f, 0);
	}
}
