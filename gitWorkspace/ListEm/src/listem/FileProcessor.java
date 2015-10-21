package listem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FileProcessor {

	public String fileSelectionPattern = "";
	public boolean recursive;
	
	
	
	public void processDirectory(File directory, String fsPattern, boolean r)
	{
		// Fields
		fileSelectionPattern = fsPattern;
		recursive = r;
		Pattern p = Pattern.compile(fileSelectionPattern);
		Matcher m;
		
		// Empty Constructor
		
		
		// Methods
//		System.out.println("My Directory: " + directory);
//		System.out.println("List Files: " + directory.listFiles());
		
		for(File myFile : directory.listFiles())
		{
			//System.out.println("File is: "+myFile.getName());
			if(myFile.isDirectory())
			{
				if(recursive)
				{
					processDirectory(myFile, fsPattern, r);
				}
			}
			else
			{
//				System.out.println(myFile);
//				System.out.println(fileSelectionPattern);
				
				if(myFile.canRead())
				{
					m = p.matcher(myFile.getName());
					if(m.matches())
					{
						//System.out.println("About to process "+myFile.getName());
						processFile(myFile);
					}
				}
			}
		}
	}
	
	public void processFile(File file)
	{
		try
		{
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
			
			if(!scanner.hasNextLine())
			{
				addEmptyFile(file);
			}
			
			while(scanner.hasNextLine())
			{
				processLine(file, scanner.nextLine());
			}
			scanner.close();
		}
		
		catch (Exception e)
		{
			System.out.println("ERROR: Something went wrong when trying to process File...");
			e.printStackTrace();
		}
	}
	
	// Implemented within "Grep" and "LineCounter" inner classes
	public abstract void processLine(File f, String line);
	
	public abstract void addEmptyFile(File f);
}
