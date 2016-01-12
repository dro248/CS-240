package server.database;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

import shared.model.*;


public class BatchDAOTest 
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
	private BatchDAO dbBatches;

	@Before
	public void setUp() throws Exception 
	{
		// Delete all batches from the database	
		db = new Database();		
		db.startTransaction();
		
		String query = "drop table if exists Batch";
        String createStmt = "CREATE TABLE Batch	(ID integer not null primary key autoincrement, file text, isAvailable boolean, parentProjectID int)";
        
        db.getConnection().prepareStatement(query).execute();
        db.getConnection().prepareStatement(createStmt).execute();
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		dbBatches = db.getBatchDAO();
	}

	@After
	public void tearDown() throws Exception 
	{
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbBatches = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException 
	{
		List<Batch> all = dbBatches.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException 
	{
//		Batch(int _ID, int _parentProjectID, boolean _isAvailable, String _url)
		Batch bob = new Batch(1,1, true, "url");
		Batch amy = new Batch(2,2, true, "url");
		
		dbBatches.add(bob);
		dbBatches.add(amy);
		
		List<Batch> batchesList = dbBatches.getAll();
		
		assertTrue(batchesList.size() >= 2);
	}
	
	@Test
	public void testInvalidAdd() throws DatabaseException 
	{
		int before = dbBatches.getAll().size();
		
		Batch invalidBatch = new Batch(-1,-1, false, null);
		dbBatches.add(invalidBatch);
		
		int after  = dbBatches.getAll().size();
		
		System.out.println("Before: " + before);
		System.out.println("After: " + after);
		
		assertTrue(before == after);
	}

	@Test
	public void testUpdate() throws DatabaseException
	{
//		Batch(int _ID, int _parentProjectID, boolean _isAvailable, String _url)
		Batch bob = new Batch(0, 0, true, "bob url");
		Batch amy = new Batch(0, 0, true, "amy url");
		
		dbBatches.add(bob);
		dbBatches.add(amy);
		
		bob.setAvailable(false);
		amy.setAvailable(false);
		
		dbBatches.update(bob);
		dbBatches.update(amy);
		
		List<Batch> all = dbBatches.getAll();
		assertEquals(2, all.size());
		
		boolean foundBob = false;
		boolean foundAmy = false;
		
		for (Batch b : all) 
		{
			if (!foundBob) 
			{
				foundBob = areEqual(b, bob, false);
			}		
			if (!foundAmy) 
			{
				foundAmy = areEqual(b, amy, false);
			}
		}
		
		assertTrue(foundBob && foundAmy);
	}
	
	@Test
	public void testInvalidUpdate() throws DatabaseException
	{
		
	}

	private boolean areEqual(Batch a, Batch b, boolean compareIDs) 
	{
		if (compareIDs) 
		{
			if (a.getID() != b.getID()) 
			{
				return false;
			}
		}	
		return (safeEquals(a.getID(), b.getID()));
	}
	
	private boolean safeEquals(Object a, Object b) 
	{
		if (a == null || b == null) 
		{
			return (a == null && b == null);
		}
		else 
		{
			return a.equals(b);
		}
	}

}