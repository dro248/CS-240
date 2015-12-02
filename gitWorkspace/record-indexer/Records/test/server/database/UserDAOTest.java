package server.database;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

import shared.model.*;


public class UserDAOTest 
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
	private UserDAO dbUsers;

	@Before
	public void setUp() throws Exception
	{

		// Delete all Users from the database	
		db = new Database();		
		db.startTransaction();
		
		String query = "drop table if exists User;";
        String createStmt = "CREATE TABLE User ( ID integer not null primary key autoincrement, username text, password text, firstname text, lastname text, email text, indexedRecords int, currentBatchID int );";
        
        db.getConnection().prepareStatement(query).execute();
        db.getConnection().prepareStatement(createStmt).execute();
		
		
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		dbUsers = db.getUserDAO();
	}

	@After
	public void tearDown() throws Exception 
	{
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbUsers = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException 
	{
		List<User> all = dbUsers.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException 
	{
//		User(String _username, String _password, String _firstname, String _lastname, String _email, int _recordsIndexed, int _currentBatchID)
		
		User bob = new User("bob1", "password", "Bob", "White", "bob1@email.com", 1, -1);
		User amy = new User("amy1", "password", "Amy", "Black", "amy1@email.com", 2, -1);
		
		dbUsers.add(bob);
		dbUsers.add(amy);
		
		List<User> all = dbUsers.getAll();
		assertEquals(2, all.size());
		
		boolean foundBob = false;
		boolean foundAmy = false;
		
		for (User  user : all) 
		{
			assertFalse(user == null);
			
			if (!foundBob) 
			{
				foundBob = areEqual(user, bob, false);
			}		
			if (!foundAmy) 
			{
				foundAmy = areEqual(user, amy, false);
			}
		}
		
		assertTrue(foundBob && foundAmy);
	}

	@Test
	public void testUpdate() throws DatabaseException 
	{
		User bob = new User("bob1", "password", "Bob", "White", "bob1@email.com", 1, -1);
		User amy = new User("amy1", "password", "Amy", "Black", "amy1@email.com", 2, -1);
		
		dbUsers.add(bob);
		dbUsers.add(amy);
		
		bob.setUsername		("robert1");
		bob.setPassword		("newPassword");
		bob.setFirstname	("Robert");
		bob.setLastname		("Whitechick");
		bob.setEmail		("robert1@email.com");
		bob.setRecordsIndexed(10);
		bob.setCurrentBatchID(1);
		
		amy.setUsername		("amelia1");
		amy.setPassword		("newPassword");
		amy.setFirstname	("Amelia");
		amy.setLastname		("Blackwood");
		amy.setEmail		("amelia1@email.com");
		amy.setRecordsIndexed(20);
		amy.setCurrentBatchID(2);
		
		dbUsers.update(bob);
		dbUsers.update(amy);
		
		List<User> all = dbUsers.getAll();
		assertEquals(2, all.size());
		
		boolean foundBob = false;
		boolean foundAmy = false;
		
		for (User user : all) 
		{
			if (!foundBob) 
			{
				foundBob = areEqual(user, bob, false);
			}		
			if (!foundAmy)
			{
				foundAmy = areEqual(user, amy, false);
			}
		}
		
		assertTrue(foundBob && foundAmy);
	}

	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException 
	{
		User invalidUser = new User(null, null, null, null, null, 0, 0);
		dbUsers.add(invalidUser);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException 
	{
		User invalidUser = new User(null, null, null, null, null, 0, 0);
		dbUsers.update(invalidUser);
	}
	
	private boolean areEqual(User a, User b, boolean compareIDs) 
	{
		if (compareIDs) 
		{
			if (a.getUsername() != b.getUsername()) 
			{
				return false;
			}
		}	
		return (safeEquals(a.getPassword()		, b.getPassword()) 			&&
				safeEquals(a.getFirstname()		, b.getFirstname()) 		&&
				safeEquals(a.getLastname()		, b.getLastname())			&&
				safeEquals(a.getEmail()			, b.getEmail()) 			&&
				safeEquals(a.getRecordsIndexed(), b.getRecordsIndexed())	&&
				safeEquals(a.getCurrentBatchID(), b.getCurrentBatchID()));
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