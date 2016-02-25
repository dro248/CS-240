package server.database;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

import shared.model.*;


public class FieldDAOTest 
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		// Load database driver	
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
		return;
	}
		
	private Database db;
	private FieldDAO dbFields;

	@Before
	public void setUp() throws Exception 
	{
		// Delete all Fields from the database	
		db = new Database();		
		db.startTransaction();
		
		String query = "DROP Table IF EXISTS Field";
        String createStmt = "CREATE TABLE Field	(ID integer not null primary key autoincrement, title text, xcoord int, width int, helphtml text, knowndata text, parentProjectID int, columnNumber int );";
        
        db.getConnection().prepareStatement(query).execute();
        db.getConnection().prepareStatement(createStmt).execute();
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		dbFields = db.getFieldDAO();
	}

	@After
	public void tearDown() throws Exception 
	{
		// Roll back transaction so changes to database are undone
		db.endTransaction(true);
		
		db = null;
		dbFields = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException 
	{
		Field bob = new Field(-1, "bob title", 10, 10, "bob helpHTML", "bob knownData", 10, 10);
		Field amy = new Field(-1, "amy title", 20, 20, "amy helpHTML", "amy knownData", 20, 20);
		
		dbFields.add(bob);
		dbFields.add(amy);
		
		List<Field> fieldsList = null;
		fieldsList = dbFields.getAll();		
		assertTrue(fieldsList.size() == 2);
	}
	
	@Test
	public void testAdd() throws DatabaseException 
	{
		Field bob = new Field(-1, "bob title", 10, 10, "bob helpHTML", "bob knownData", 10, 10);
		Field amy = new Field(-1, "amy title", 20, 20, "amy helpHTML", "amy knownData", 20, 20);
		
		dbFields.add(bob);
		dbFields.add(amy);
		
		List<Field> fieldsList = dbFields.getAll();
		assertTrue(fieldsList.size() == 2);
	}
	
	@Test
	public void testInvalidAdd() throws DatabaseException 
	{
		Field invalidField = new Field(-1, null, -1, 0, null, null, 0, 0);
		dbFields.add(invalidField);
		
		List<Field> fieldsList = null;
		try
		{
			fieldsList = dbFields.getAll();
		}
		catch(Exception e)
		{
			System.out.println("Exception caught!");
		}
		
		assertTrue(fieldsList != null);
	}
}