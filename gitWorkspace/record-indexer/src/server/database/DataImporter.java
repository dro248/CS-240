package server.database;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;

/*
 *	This is the import you need to use the Apache Commons IO library.
 *	Look at the code marked {/*(**APACHE**)*/
 //*/
import org.apache.commons.io.*;

import shared.model.*;
import server.database.*;
import server.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

public class DataImporter 
{
	Database database = new Database();
	
	public static void main(String[] args)
	{
		DataImporter dataImporter = new DataImporter();
		
		try { dataImporter.importData(args[0]); } 
		catch (Exception e) 
		{
			System.out.println("Import failed!");
			e.printStackTrace();
		}
	}
	
	public void importData(String xmlFilename) throws Exception
	{
		// VARIABLES FOR PARSING XML
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File xmlFile = new File(xmlFilename);
		File dest = new File("Records");
		
		
		// COPY FILE TO LOCAL
		// (**APACHE**)
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!xmlFile.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(xmlFile.getParentFile(), dest);
			
			/* Empties Database. Now, my database is completely
			 *  empty and ready to load with data. */
			refreshDatabase();
		}
		catch (IOException e)
		{
			System.out.println("ERROR in [DataImporter.importData]:");
			System.out.println("   -> Unable to deal with the IOException thrown.");
		}
		
		
		// PARSE XML
		File parsefile = new File(dest.getPath() + File.separator + xmlFile.getName());
		Document doc = builder.parse(parsefile);
		
		NodeList users = doc.getElementsByTagName("users");
		parseUsers(users);
		
		NodeList projects = doc.getElementsByTagName("projects");
		parseProjects(projects);
	}

	private void parseUsers(NodeList usersList)
	{
		for (int i = 0; i < usersList.getLength(); ++i) 
		{
			User myUser;
			UserDAO userDAO = new UserDAO(database);
			Element userElem = (Element)usersList.item(i);
			
			String username 	= userElem.getElementsByTagName("username").item(0).getTextContent();
			String password 	= userElem.getElementsByTagName("password").item(0).getTextContent();
			String firstname 	= userElem.getElementsByTagName("firstname").item(0).getTextContent();
			String lastname 	= userElem.getElementsByTagName("lastname").item(0).getTextContent();
			String email 		= userElem.getElementsByTagName("emails").item(0).getTextContent();
			int indexedRecords 	= Integer.parseInt(userElem.getElementsByTagName("indexedrecords").item(0).getTextContent());
			
			myUser = new User(username, password, firstname, lastname, email, indexedRecords, -1);
			
			try 						{ userDAO.addUser(myUser); }
			catch(DatabaseException e)	{ System.out.println("UserDAO Database Exception"); }
		}
	}

	private void parseProjects(NodeList projectsList)
	{
		for(int i = 0; i < projectsList.getLength(); i++)
		{
			Project myProject;
			ProjectDAO projectDAO = new ProjectDAO(database);
			Element projectElem = (Element) projectsList.item(i);
			
			String title 		= projectElem.getElementsByTagName("title").item(0).getTextContent();
			int recordsPerImage = Integer.parseInt(projectElem.getElementsByTagName("recordsperimage").item(0).getTextContent());
			int firstYCoord 	= Integer.parseInt(projectElem.getElementsByTagName("firstycoord").item(0).getTextContent());
			int recordHeight 	= Integer.parseInt(projectElem.getElementsByTagName("recordheight").item(0).getTextContent());
			
			myProject = new Project(-1, title, recordsPerImage, firstYCoord, recordHeight);
			
			try 						{ projectDAO.addProject(myProject);						}
			catch (DatabaseException e) { System.out.println("ProjectDAO Database Exception");	}
			
			// Field parsing...
			NodeList fieldsList = projectElem.getElementsByTagName("fields");
			parseFields(fieldsList, myProject.getID());
			
			// Batch parsing...
			NodeList batchesList = projectElem.getElementsByTagName("images");
			parseBatches(batchesList, myProject.getID());
		}
	}
	
	private void parseFields(NodeList fieldsList, int parentProjectID)
	{
		for(int i = 0; i < fieldsList.getLength(); i++)
		{
			Field myField;
			FieldDAO fieldDAO = new FieldDAO(database);
			Element fieldElem = (Element) fieldsList.item(i);
			
			String title 	 = fieldElem.getElementsByTagName("title").item(0).getTextContent();
			int xCoord 		 = Integer.parseInt(fieldElem.getElementsByTagName("xcoord").item(0).getTextContent());
			int width 		 = Integer.parseInt(fieldElem.getElementsByTagName("width").item(0).getTextContent());
			String helpHTML  = fieldElem.getElementsByTagName("helphtml").item(0).getTextContent();
			String knownData = fieldElem.getElementsByTagName("knowndata").item(0).getTextContent();
			
			myField = new Field(title, xCoord, width, helpHTML, knownData);
			try 						{ fieldDAO.addField(myField); }
			catch(DatabaseException e)	{ System.out.println("FieldDAO Database Exception"); }
		}
	}
	
	private void parseBatches(NodeList batchesList, int parentProjectID)
	{
		for(int i = 0; i < batchesList.getLength(); i++)
		{
			Batch myBatch;
			BatchDAO batchDAO = new BatchDAO(database);
			Element batchElem = (Element) batchesList.item(i);
			
			String filename = batchElem.getElementsByTagName("file").item(0).getTextContent();
			
			myBatch = new Batch(-1, parentProjectID, true, filename);
			try 						{ batchDAO.addBatch(myBatch); }
			catch(DatabaseException e)	{ System.out.println("BatchDAO Database Exception"); }

			NodeList recordsList = batchElem.getElementsByTagName("record");
			parseRecords(recordsList, myBatch.getID());
		}
	}
	
	private void parseRecords(NodeList recordsList, int parentBatchID)
	{
		for(int i = 0; i < recordsList.getLength(); i++)
		{
			Record myRecord;
			RecordDAO recordDAO = new RecordDAO(database);
			Element recordElem = (Element) recordsList.item(0);
			
			myRecord = new Record(-1, parentBatchID);
			try								{ recordDAO.addRecord(myRecord); }
			catch(DatabaseException e)		{ System.out.println("RecordDAO Database Exception"); }
			NodeList cellsList = recordElem.getElementsByTagName("values");
			parseCells(cellsList, myRecord.getID());
		}
	}
	
	private void parseCells(NodeList cellsList, int parentRecordID)
	{
		for(int i = 1; i <= cellsList.getLength(); i++)
		{
			Cell myCell;
			CellDAO cellDAO = new CellDAO(database);
			Element cellElem = (Element) cellsList.item(0);
			
			String value = cellElem.getElementsByTagName("value").item(0).getTextContent();
			
			myCell = new Cell(value, parentRecordID, i);
			try								{ cellDAO.addCell(myCell); }
			catch(DatabaseException e)		{ System.out.println("CellDAO Database Exception"); }
		}
	}
	
	private void refreshDatabase() throws SQLException
	{
		String dropStmt = "DROP TABLE IF EXISTS Batch;"
						+ "DROP TABLE IF EXISTS Cell;"
						+ "DROP TABLE IF EXISTS Field;"
						+ "DROP TABLE IF EXISTS Project;"
						+ "DROP TABLE IF EXISTS Record;"
						+ "DROP TABLE IF EXISTS User;";
		database.getConnection().prepareStatement(dropStmt).execute();
		
		String createStmt = "CREATE TABLE User 		( ID integer not null primary key autoincrement, username text, 		password text, 			firstname text, 			lastname text, 		email text, 		indexedRecords int, 	currentBatchID int );" 
						  + "CREATE TABLE Project	( ID integer not null primary key autoincrement, title text, 			recordsperimage int, 	firstycoord int, 			recordheight int);"
						  + "CREATE TABLE Field		( ID integer not null primary key autoincrement, title text, 			xcoord int, 			width int, 					helphtml text, 		knowndata text, 	parentProjectID int );"
						  + "CREATE TABLE Batch		( ID integer not null primary key autoincrement, file text, 			isAvailable boolean, 	parentProjectID int );"
						  + "CREATE TABLE Record	( ID integer not null primary key autoincrement, parentBatchID int);"
						  + "CREATE TABLE Cell 		( ID integer not null primary key autoincrement, parentRecordCoord int, parentFieldCoord int, 	cellValue text );";
		database.getConnection().prepareStatement(createStmt).execute();
	}
}