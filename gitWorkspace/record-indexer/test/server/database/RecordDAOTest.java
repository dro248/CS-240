package server.database;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

import shared.model.*;


public class RecordDAOTest 
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
	private RecordDAO dbRecords;

	@Before
	public void setUp() throws Exception 
	{
		// Delete all records from the database	
		db = new Database();		
		db.startTransaction();
		
		String query = "drop table if exists Record;";
        String createStmt = "CREATE TABLE Record (ID integer not null primary key autoincrement, parentBatchID int);";
        
        db.getConnection().prepareStatement(query).execute();
        db.getConnection().prepareStatement(createStmt).execute();
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		dbRecords = db.getRecordDAO();
	}

	@After
	public void tearDown() throws Exception 
	{
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbRecords = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException 
	{
		List<Record> all = dbRecords.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException 
	{
		Record bob = new Record(1,1);
		Record amy = new Record(2,2);
		
		dbRecords.add(bob);
		dbRecords.add(amy);
		
		List<Record> all = dbRecords.getAll();
		assertEquals(2, all.size());
		
		boolean foundBob = false;
		boolean foundAmy = false;
		
		for (Record r : all) 
		{
			assertFalse(r.getID() == -1);
			
			if (!foundBob) 
			{
				foundBob = areEqual(r, bob, false);
			}		
			if (!foundAmy) 
			{
				foundAmy = areEqual(r, amy, false);
			}
		}
		
		assertTrue(foundBob && foundAmy);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException 
	{
		Record invalidRecord = new Record(1,1);
		dbRecords.add(invalidRecord);
	}
	
	private boolean areEqual(Record a, Record b, boolean compareIDs) 
	{
		if (compareIDs) 
		{
			if (a.getID() != b.getID()) 
			{
				return false;
			}
		}	
		return (safeEquals(a.getParentBatchID(), b.getParentBatchID()));
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