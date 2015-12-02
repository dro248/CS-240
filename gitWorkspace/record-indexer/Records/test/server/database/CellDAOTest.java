package server.database;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

import shared.model.*;


public class CellDAOTest 
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
	private CellDAO dbCells;

	@Before
	public void setUp() throws Exception 
	{
		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();

//		Cell(ID integer not null primary key autoincrement, parentRecordCoord int, parentFieldCoord int, cellValue text )
		String query = "drop table if exists Cell;";
        String createStmt = "CREATE TABLE Cell (ID integer not null primary key autoincrement, parentRecordCoord int, parentFieldCoord int, cellValue text);";
        
        db.getConnection().prepareStatement(query).execute();
        db.getConnection().prepareStatement(createStmt).execute();
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		dbCells = db.getCellDAO();
	}

	@After
	public void tearDown() throws Exception 
	{
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbCells = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException 
	{	
		Cell bob = new Cell(1, 1, "bob value");
		Cell amy = new Cell(1, 1, "amy value");
		
		dbCells.add(bob);
		dbCells.add(amy);
		
		List<Cell> all = dbCells.getAll();
		assertEquals(2, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException 
	{
		Cell bob = new Cell(1, 1, "bob value");
		Cell amy = new Cell(1, 1, "amy value");
		
		dbCells.add(bob);
		dbCells.add(amy);
		
		List<Cell> all = dbCells.getAll();
		assertEquals(2, all.size());
		
		boolean foundBob = false;
		boolean foundAmy = false;
		
		for (Cell c : all) 
		{	
			assertFalse(c.getValue() == null);
			
			if (!foundBob) 
			{
				foundBob = areEqual(c, bob, false);
			}		
			if (!foundAmy) 
			{
				foundAmy = areEqual(c, amy, false);
			}
		}
		
		assertTrue(foundBob && foundAmy);
	}
	
	private boolean areEqual(Cell a, Cell b, boolean compareIDs) 
	{
		if (compareIDs) 
		{
			if (a.getParentFieldID() != b.getParentFieldID() &&
				a.getParentRecordID()!= b.getParentRecordID()) 
			{
				return false;
			}
		}	
		return (safeEquals(a.getValue(), b.getValue()));
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