package client.communication;

import static org.junit.Assert.*;
import org.junit.*;
import server.Server;
import server.database.DataImporter;
import shared.communication.*;
import client.ClientException;

public class ClientCommunicatorTest 
{
	private static ClientCommunicator cc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		String[] args = new String[1];
		args[0] = "45321";
		Server.main(args);
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
		cc = null;
	}
	@Before
	public void setUp() throws Exception 
	{
		String[] args = new String[1];
		args[0] = "demo/indexer_data/Records/Records.xml";
		DataImporter.main(args);
		
		args[0] = "45321";
		
		cc = new ClientCommunicator("localhost", Integer.parseInt(args[0]));
	}
	@After
	public void tearDown() throws Exception 
	{
		return;
	}
	
	@Test
	public void validtest_validateUser()
	{
		String test = "TRUE\nSheila\nParker\n0\n";
		
		UserParams params = new UserParams("sheila", "parker");
		UserResult result = null; 
				
		try
		{
			result = cc.validateUser(params);
			System.out.println("validateUser: " + result.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		assertTrue(result.toString().equals(test));
	}
	@SuppressWarnings("unused")
	@Test
	public void invalidtest_validateUser()
	{	
		String test = null;
		
		UserParams params = new UserParams("username", "password");
		UserResult result = null;
		
		try
		{
			result = cc.validateUser(params);
			System.out.println("result: " + result.toString());
		}
		catch(Exception e)
		{
			test = "FALSE\n";
		}
		assertTrue(result.toString().equals("FALSE\n"));
	}

	@Test
	public void validtest_getProjects()
	{
		String test = "1\n1890 Census\n2\n1900 Census\n3\nDraft Records\n";
		
		ProjectParams params = new ProjectParams("sheila", "parker");
		ProjectResult result = null;
				
		try
		{
			result = cc.getProjects(params);
			System.out.println(result.toString());
		}
		catch(Exception e)
		{
			System.out.println(result.toString());
		}
		assertTrue(result.toString().equals(test));
	}
	@SuppressWarnings("unused")
	@Test
	public void invalidtest_getProjects()
	{	
		String fail = null;
		
		ProjectParams params = new ProjectParams("username", "password");
		ProjectResult result = null;
		
		try
		{
			result = cc.getProjects(params);
		}
		catch(Exception e)
		{
			fail = "FAILED\n";
		}
		
		assert fail.equals("FAILED\n");
	}

	@Test
	public void validtest_getSampleImage()
	{
		String test = "http://localhost:45321/Records/images/1890_image0.png\n";
		
		ImgParams params = new ImgParams("test1", "test1", 1);
		ImgResult result = null;
		
		try
		{
			result = cc.getSampleImage(params);
			System.out.println(result.toString(cc.getURLPrefix()));
		}
		catch(Exception e)
		{
			System.out.println(result.toString(cc.getURLPrefix()));
		}
		
		assertTrue(result.toString(cc.getURLPrefix()).equals(test));
	}
	@SuppressWarnings("unused")
	@Test
	public void invalidtest_getSampleImage()
	{	
		String fail = null;
		
		ImgParams params = new ImgParams("username", "password", 1);
		ImgResult result = null; 
			
		try
		{
			result = cc.getSampleImage(params);
		}
		catch(Exception e)
		{
			fail = "FAILED\n";
		}
		
		assertTrue(fail.equals("FAILED\n"));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void validtest_downloadBatch_submitBatch()
	{	
		String test = "1\n1\nhttp://localhost:45321/Records/images/1890_image0.png\n199\n60\n8\n4\n" 
					+ "1\n1\nLast Name\nhttp://localhost:45321/Records/fieldhelp/last_name.html\n60\n300\n"
					+ "http://localhost:45321/Records/knowndata/1890_last_names.txt\n"
					+ "2\n2\nFirst Name\nhttp://localhost:45321/Records/fieldhelp/first_name.html\n360\n280\n"
					+ "http://localhost:45321/Records/knowndata/1890_first_names.txt\n"
					+ "3\n3\nGender\nhttp://localhost:45321/Records/fieldhelp/gender.html\n640\n205\n"
					+ "http://localhost:45321/Records/knowndata/genders.txt\n"
					+ "4\n4\nAge\nhttp://localhost:45321/Records/fieldhelp/age.html\n845\n120\n";
		
		DownloadBatchParams params = new DownloadBatchParams("test1", "test1", 1);
		DownloadBatchResult result = null;
		
		try
		{
			result = cc.downloadBatch(params);
			System.out.println(result.toString(cc.getURLPrefix()));
		}
		catch(Exception e)
		{
			System.out.println(result.toString(cc.getURLPrefix()));
		}
		
		assertTrue(result.toString(cc.getURLPrefix()).equals(test));
		
		
		String test2 = "TRUE\n";
		
		SubmitBatchParams s_params = new SubmitBatchParams("test1", "test1", 1, "Ostler,David,M,24;Ostler,Rachel,F,23");
		SubmitBatchResult s_result = null;
		
		try
		{
			s_result = cc.submitBatch(s_params);
			System.out.println(result.toString().equals(test2));
		}
		catch(Exception e)
		{
			System.out.println(result.toString().equals(test2));
		}
		
		if(result.toString().equals(test))
			System.out.println("good2");
		assertTrue(result.toString().equals(test));		
	}
	@Test
	public void invalidtest_downloadBatch_submitBatch()
	{
		String fail = null;
		
		DownloadBatchParams params = new DownloadBatchParams("username", "password", 1);
		DownloadBatchResult result = null;
				
		try
		{
			result = cc.downloadBatch(params);	
			System.out.println(result.toString(cc.getURLPrefix()));
		}
		catch(Exception e)
		{
			fail = "FAILED\n";
		}
		
		assertTrue(fail.equals("FAILED\n"));
	}

	@Test
	public void validtest_getFields()
	{
		String test = "1\n1\nLast Name\n1\n2\nFirst Name\n1\n3\nGender\n1\n4\nAge\n";
		
		FieldParams params = new FieldParams("sheila", "parker", 1);
		FieldResult result = null;
		
		try
		{
			result = cc.getFields(params);
			System.out.println(result.toString());
		}
		catch(Exception e)
		{
			System.out.println(result.toString());
		}
		
		assertTrue(result.toString().equals(test));
	}
	@SuppressWarnings("unused")
	@Test
	public void invalidtest_getFields()
	{
		String fail = null;
		
		FieldParams params = new FieldParams("username", "password", 1);
		FieldResult result = null;
		
		try
		{
			result = cc.getFields(params);
		}
		catch(Exception e)
		{
			fail = "FAILED\n";
		}
		assertTrue(fail.equals("FAILED\n"));
	}
	
	@Test
	public void validtest_search() throws ClientException
	{
		String test = "41\nhttp://localhost:45321/images/draftimage0.png\n1\n10\n"
				  +   "41\nhttp://localhost:45321/images/draftimage1.png\n11\n";
		
		SearchParams params = new SearchParams("sheila", "parker", "10,11", "fox,russell");
		SearchResult result = cc.search(params);
		
		assertTrue(result.toString().equals(test));
	}
	@SuppressWarnings("unused")
	@Test
	public void invalidtest_search() throws ClientException
	{
		String fail = null;
		
		SearchParams params = new SearchParams("username", "password", "-1", "-1");
		SearchResult result = null;
		try
		{
			result = cc.search(params);
		}
		catch(Exception e)
		{
			fail = "FAILED\n";
		}
		assertTrue(fail.equals("FAILED\n"));
	}
}