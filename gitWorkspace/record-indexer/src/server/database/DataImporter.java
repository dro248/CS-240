package server.database;

import java.io.*;
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
	public static void main(String[] args)
	{
		DataImporter dataImporter = new DataImporter();
		
		try 
		{
			dataImporter.importData(args[0]);
		} 
		catch (Exception e) 
		{
			System.out.println("Import failed!");
			e.printStackTrace();
		}
	}
	
	public void importData(String xmlFilename) throws Exception
	{
		// 1) COPY FILE INTO LOCAL PROJECT
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File xmlFile = new File(xmlFilename);
		File dest = new File("Records");
		
		// CREATE STATEMENTS TO DROP DATABASE THEN CREATE TABLES
		refreshDatabase();
		
//		File emptydb = new File("database" + File.separator + "empty" + File.separator + "recordindexer.sqlite");
//		File currentdb = new File("database" + File.separator + "recordindexer.sqlite");

		/*
		 * (**APACHE**)
		 */
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!xmlFile.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(xmlFile.getParentFile(), dest);
			
			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completely empty and ready to load with data.
//			FileUtils.copyFile(emptydb, currentdb);
		}
		catch (IOException e)
		{
//			logger.log(Level.SEVERE, "Unable to deal with the IOException thrown", e);
			System.out.println("ERROR in [DataImporter.importData]:");
			System.out.println("   -> Unable to deal with the IOException thrown.");
		}
		/*
		 * (**APACHE**)
		 */
		
		File parsefile = new File(dest.getPath() + File.separator + xmlFile.getName());
		Document doc = builder.parse(parsefile);
		
		
		NodeList users = doc.getElementsByTagName("user");
		parseUsers(users);
		
//		logger.info("I'm getting to here");
//		System.out.println("[DataImporter.importData()]: Method called...");
		NodeList projects = doc.getElementsByTagName("project");
		parseProjects(projects);
	}


	private void parseUsers(NodeList usersList)
	{
		/*
		 *	This is where you will iterate through all of the users in the
		 *	usersList NodeList and extract the information to create and insert
		 *	a user into your database.
		 */
		
		// SEE DOMExample.java

		for (int i = 0; i < usersList.getLength(); ++i) 
		{
			Element user = (Element)usersList.item(i);
			/*
			Element elem_firstname = (Element)usersList.getElementsByTagName("firstname").item(0);
			Element elem_lastname = (Element)usersList.getElementsByTagName("lastname").item(0);
			Element elem_username = (Element)usersList.getElementsByTagName("username").item(0);
			Element elem_password = (Element)usersList.getElementsByTagName("password").item(0);
			Element elem_email = (Element)usersList.getElementsByTagName("email").item(0);
			Element elem_currentBatchID = (Element)usersList.getElementsByTagName("currentBatchID").item(0);
			Element elem_recordsIndexed = (Element)usersList.getElementsByTagName("recordsIndexed").item(0);
			Element elem_ID = (Element)usersList.getElementByTagName("ID").item(0);
			
			String firstname = elem_firstname.getTextContent();
			String lastname = elem_lastname.getTextContent();
			String username = elem_username.getTextContent();
			String password = elem_password.getTextContent();
			String email = elem_email.getTextContent();
			int currentBatchID = elem_currentBatchID.getTextContent();
			int ID = elem_ID.getTextContent();
			*/
			
			
//			System.out.println(title + ", " + artist);
		}	
		
		
	}


	private void parseProjects(NodeList projectsList)
	{
		/*
		 *	This is where you will iterate through all of the users in the
		 *	projectsList NodeList and extract the information to create and
		 *	insert a project into your database.
		 */
		
		
		
	}
	
	
	private void refreshDatabase()
	{
		PreparedStatement stmt = null;
		
		String dropStmt = "DROP TABLE IF EXISTS Batch;"
						+ "DROP TABLE IF EXISTS Cell;"
						+ "DROP TABLE IF EXISTS Field;"
						+ "DROP TABLE IF EXISTS Project;"
						+ "DROP TABLE IF EXISTS Record;"
						+ "DROP TABLE IF EXISTS User;";
		
		stmt = database.getConnection().preparedStatement(dropStmt);
		
	}
}
