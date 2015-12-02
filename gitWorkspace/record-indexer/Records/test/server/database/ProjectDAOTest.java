package server.database;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

import shared.model.*;


public class ProjectDAOTest 
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
	private ProjectDAO dbProject;

	@Before
	public void setUp() throws Exception 
	{
		// Delete all projects from the database	
		db = new Database();		
		db.startTransaction();
		
		String query = "drop table if exists Project;";
        String createStmt = "CREATE TABLE Project(ID integer not null primary key autoincrement, title text, recordsperimage int, firstycoord int, recordheight int);";
        
        db.getConnection().prepareStatement(query).execute();
        db.getConnection().prepareStatement(createStmt).execute();
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		dbProject = db.getProjectDAO();
	}

	@After
	public void tearDown() throws Exception
	{
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbProject = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException 
	{
		List<Project> all = dbProject.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException 
	{
		Project bob = new Project(0, null, 0, 0, 0);
		Project amy = new Project(0, null, 0, 0, 0);
		
		dbProject.add(bob);
		dbProject.add(amy);
		
		List<Project> all = dbProject.getAll();
		assertEquals(2, all.size());
		
		boolean foundBob = false;
		boolean foundAmy = false;
		
		for (Project project : all) 
		{	
			assertFalse(project.getID() == -1);
		}
		
		assertTrue(foundBob && foundAmy);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException 
	{	
		Project invalidProject = new Project(0, null, 0, 0, 0);
		dbProject.add(invalidProject);
	}
}