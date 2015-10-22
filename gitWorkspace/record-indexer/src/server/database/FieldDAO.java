package server.database;

import java.util.*;

import shared.model.*;

public class FieldDAO 
{

	Database database;
	
	public FieldDAO(Database db)
	{
		database = db;
	}
	
	// Create
	/**
	 * Adds a field to the database.
	 * @param field
	 * @return whether or not the field was successfully added to database.
	 */
	public boolean addField(Field field) throws DatabaseException 
	{ return false; }
	
	// Read
	/**
	 * Gets a list of all of the fields in the database.
	 * @return List of Fields
	 */
	public List<Field> getAllFields() { return null; }
	
	/**
	 * Given searchValues, returns 
	 * @param searchValues
	 * @return
	 */
	public List<Field> getFields(String searchValues) { return null; }
	
	/**
	 * Get feid based off of fieldID
	 * @param fieldID
	 * @return
	 */
	public Field getField(int fieldID)	{ return null; }

}
