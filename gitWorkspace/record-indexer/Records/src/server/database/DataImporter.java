package server.database;

import java.io.*;
import java.sql.*;
import org.apache.commons.io.*;
import shared.model.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class DataImporter 
{
	Database database = new Database();
	
	private static final String dropUsers		= "DROP TABLE IF EXISTS User;";
	private static final String dropProjects	= "DROP TABLE IF EXISTS Project;";
	private static final String dropFields		= "DROP TABLE IF EXISTS Field;";
	private static final String dropBatches		= "DROP TABLE IF EXISTS Batch;";
	private static final String dropRecords		= "DROP TABLE IF EXISTS Record;";
	private static final String dropCells		= "DROP TABLE IF EXISTS Cell;";
	
	private static final String createUsers		= "CREATE TABLE User 	( ID integer not null primary key autoincrement, username text, 		password text, 			firstname text, 			lastname text, 		email text, 		indexedRecords int, 	currentBatchID int );";
	private static final String createProjects	= "CREATE TABLE Project	( ID integer not null primary key autoincrement, title text, 			recordsperimage int, 	firstycoord int, 			recordheight int);";
	private static final String createFields	= "CREATE TABLE Field	( ID integer not null primary key autoincrement, title text, 			xcoord int, 			width int, 					helphtml text, 		knowndata text, 	parentProjectID int,	columnNumber int );";
	private static final String createBatches	= "CREATE TABLE Batch	( ID integer not null primary key autoincrement, file text, 			isAvailable boolean, 	parentProjectID int );";
	private static final String createRecords	= "CREATE TABLE Record	( ID integer not null primary key autoincrement, parentBatchID int);";
	private static final String createCells		= "CREATE TABLE Cell 	( ID integer not null primary key autoincrement, parentRecordCoord int, parentFieldCoord int, 	cellValue text );";
	
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
		
		try
		{
			if(!xmlFile.getAbsoluteFile().getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			FileUtils.copyDirectory(xmlFile.getAbsoluteFile().getParentFile(), dest);
			
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
		
		NodeList users = ((Element) doc.getElementsByTagName("users").item(0)).getElementsByTagName("user");
		parseUsers(users);
		
		NodeList projects = ((Element) doc.getElementsByTagName("projects").item(0)).getElementsByTagName("project");
		parseProjects(projects);
		
		System.out.println("Import PASSED!!");
	}

	private void parseUsers(NodeList usersList)
	{
		for (int i = 0; i < usersList.getLength(); ++i) 
		{
			User user;
			UserDAO userDAO = new UserDAO(database);
			Element userElem = (Element)usersList.item(i);
			
			String username 	= userElem.getElementsByTagName("username").item(0).getTextContent();
			String password 	= userElem.getElementsByTagName("password").item(0).getTextContent();
			String firstname 	= userElem.getElementsByTagName("firstname").item(0).getTextContent();
			String lastname 	= userElem.getElementsByTagName("lastname").item(0).getTextContent();
			String email 		= userElem.getElementsByTagName("email").item(0).getTextContent();
			int indexedRecords 	= Integer.parseInt(userElem.getElementsByTagName("indexedrecords").item(0).getTextContent());
			
//			User(String _username, String _password, String _firstname, String _lastname, String _email, int _recordsIndexed, int _currentBatchID)
			user = new User(username, password, firstname, lastname, email, indexedRecords, 0);
			
			try 						
			{ 
				userDAO.add(user); 
			}
			catch(DatabaseException e)	{ System.out.println("UserDAO Database Exception"); }
		}
	}

	private void parseProjects(NodeList projectsList)
	{
		int fieldID = 0;
		ProjectDAO projectDAO = new ProjectDAO(database);
		
		for(int i = 0; i < projectsList.getLength(); i++)
		{
			Project myProject;
			Element projectElem = (Element) projectsList.item(i);
			
			String title 		= projectElem.getElementsByTagName("title").item(0).getTextContent();
			int recordsPerImage = Integer.parseInt(projectElem.getElementsByTagName("recordsperimage").item(0).getTextContent());
			int firstYCoord 	= Integer.parseInt(projectElem.getElementsByTagName("firstycoord").item(0).getTextContent());
			int recordHeight 	= Integer.parseInt(projectElem.getElementsByTagName("recordheight").item(0).getTextContent());
			
			myProject = new Project(-1, title, recordsPerImage, firstYCoord, recordHeight);
			
			try 						{ projectDAO.add(myProject);							}
			catch (DatabaseException e) { System.out.println("ProjectDAO Database Exception");	}
			
			// Field parsing...
			NodeList fieldsList = ((Element) projectElem.getElementsByTagName("fields").item(0)).getElementsByTagName("field");
			fieldID = parseFields(fieldsList, myProject.getID(), fieldID);
			
			// Batch parsing...
			NodeList batchesList = ((Element) projectElem.getElementsByTagName("images").item(0)).getElementsByTagName("image");
			parseBatches(batchesList, myProject.getID(), fieldID);
		}
	}
	
	private int parseFields(NodeList fieldsList, int parentProjectID, int fieldID)
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
			String knownData = "";
			if (fieldElem.getElementsByTagName("knowndata").getLength() > 0)
			{
				knownData = fieldElem.getElementsByTagName("knowndata").item(0).getTextContent();
			}
			myField = new Field(-1, title, xCoord, width, helpHTML, knownData, parentProjectID, i+1);
			
			try 						{ fieldDAO.add(myField); 								}
			catch(DatabaseException e)	{ System.out.println("FieldDAO Database Exception"); 	}
			
			fieldID = myField.getID();
		}
		
		return fieldID;
	}
	
	private void parseBatches(NodeList batchesList, int parentProjectID, int fieldID)
	{
		for(int i = 0; i < batchesList.getLength(); i++)
		{
			Batch myBatch;
			BatchDAO batchDAO = new BatchDAO(database);
			Element batchElem = (Element) batchesList.item(i);
			
			String filename = batchElem.getElementsByTagName("file").item(0).getTextContent();
			
			myBatch = new Batch(-1, parentProjectID, true, filename);
			try 						
			{ 
				batchDAO.add(myBatch); 
			}
			catch(DatabaseException e)	{ System.out.println("BatchDAO Database Exception"); }
			
			if(batchElem.getElementsByTagName("records").getLength() > 0)
			{
				NodeList recordsList = ((Element) batchElem.getElementsByTagName("records").item(0)).getElementsByTagName("record");
				parseRecords(recordsList, myBatch.getID(), fieldID);
			}
		}
	}
	
	private void parseRecords(NodeList recordsList, int parentBatchID, int fieldID)
	{
		RecordDAO recordDAO = new RecordDAO(database);
		
		for(int i = 0; i < recordsList.getLength(); i++)
		{
			Record myRecord;
			
			Element recordElem = (Element) recordsList.item(i);
			
			myRecord = new Record(-1, parentBatchID);
			try								{ recordDAO.add(myRecord); 								}
			catch(DatabaseException e)		{ System.out.println("RecordDAO Database Exception"); 	}
			
			NodeList cellsList = null;
			
//			if(recordElem.getElementsByTagName("values").getLength() > 0)
//			{
			cellsList = ((Element) recordElem.getElementsByTagName("values").item(0)).getElementsByTagName("value");
			parseCells(cellsList, myRecord.getID(), fieldID);
//			}
		}
	}
	
	private void parseCells(NodeList cellsList, int parentRecordID, int fieldID)
	{
		Cell myCell;
		CellDAO cellDAO = new CellDAO(database);
		int size = cellsList.getLength();
		
		for(int i = 0; i < size; i++)
		{
			Element cellElem = (Element) cellsList.item(i);
			
//			System.out.println (cellElem.getElementsByTagName("value").item(0).getTextContent());
			String value = cellElem.getTextContent().toLowerCase();
			
			myCell = new Cell(parentRecordID, i+1+fieldID-size, value);
			try								{ cellDAO.add(myCell); }
			catch(DatabaseException e)		{ System.out.println("CellDAO Database Exception"); }
		}
	}
	
	@SuppressWarnings("static-access")
	private void refreshDatabase() throws SQLException, IOException, DatabaseException
	{
		database.initialize();
		database.startTransaction();
//		System.out.println("START_TRANSACTION: refreshDatabase()");
		
		database.getConnection().prepareStatement(dropUsers).execute();
		database.getConnection().prepareStatement(createUsers).execute();
		
		database.getConnection().prepareStatement(dropProjects).execute();
		database.getConnection().prepareStatement(createProjects).execute();
		
		database.getConnection().prepareStatement(dropFields).execute();
		database.getConnection().prepareStatement(createFields).execute();
		
		database.getConnection().prepareStatement(dropBatches).execute();
		database.getConnection().prepareStatement(createBatches).execute();
		
		database.getConnection().prepareStatement(dropRecords).execute();
		database.getConnection().prepareStatement(createRecords).execute();
		
		database.getConnection().prepareStatement(dropCells).execute();
		database.getConnection().prepareStatement(createCells).execute();
		
		database.endTransaction(true);
//		System.out.println("END_TRANSACTION: refreshDatabase()");
	}
}