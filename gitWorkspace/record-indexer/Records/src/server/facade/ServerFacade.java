package server.facade;

import java.util.*;

import server.database.*;
import shared.communication.*;
import shared.model.*;
import server.*;

public class ServerFacade 
{
	public static void initialize() throws ServerException
	{		
		try 
		{
			Database.initialize();		
		}
		catch (DatabaseException e) 
		{
			throw new ServerException(e.getMessage(), e);
		}
	}	

	public static UserResult validateUser(String _username, String _password) throws ServerException
	{
		Database db = new Database();
		
		try 
		{
			db.startTransaction();
			User user = db.getUserDAO().get(_username, _password);
			db.endTransaction(true);
			return new UserResult (user);
		}
		catch (Exception e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

	public static ProjectResult getProjects(String _username, String _password) throws ServerException
	{
		User user = validateUser(_username, _password).getUser();
		if(user == null)
		{
			throw new ServerException();
		}
		
		Database db = new Database();
		List<Project> projectList;
		ProjectResult result;
		
		try 
		{
			db.startTransaction();
			projectList = db.getProjectDAO().getAll();
			db.endTransaction(true);
			result = new ProjectResult(projectList);
			
			return result;
		}
		catch (DatabaseException e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

	public static ImgResult getSampleImage(String _username, String _password, int _parentProjectID) throws ServerException
	{
		User user = validateUser(_username, _password).getUser();
		if(user == null)
		{
			throw new ServerException();
		}
		
		Database db = new Database();
		
		try 
		{
			db.startTransaction();
			
			int maxProjectID = db.getProjectDAO().getAll().size();
			
			if(_parentProjectID > maxProjectID ||
			   _parentProjectID < 1 )
			{
				db.endTransaction(false);
				throw new ServerException();
			}
			
			Batch batch = db.getBatchDAO().getFirstAvailableBatch(_parentProjectID);
			String url = batch.getUrl();
			
			db.endTransaction(true);
			return new ImgResult(url);
		}
		catch (DatabaseException e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unused")
	public static DownloadBatchResult downloadBatch(String _username, String _password, int _parentProjectID) throws ServerException
	{
		User user = validateUser(_username, _password).getUser();
		
		if(user == null || user.getCurrentBatchID() < 0)
		{
			throw new ServerException();
		}		


		Database db = new Database();
		DownloadBatchResult result;
		
		try 
		{
			db.startTransaction();
			
			int maxProjectID = db.getProjectDAO().getAll().size();
			
			if(_parentProjectID > maxProjectID ||
			   _parentProjectID < 1 )
			{
				db.endTransaction(false);
				throw new ServerException();
			}
			
			Batch batch 			= db.getBatchDAO().getFirstAvailableBatch(_parentProjectID);
			Project project 		= db.getProjectDAO().get(_parentProjectID);
			List<Field> fieldsList 	= db.getFieldDAO().getFieldsByProjectID(_parentProjectID);
			
			batch.setAvailable(false);
			user.setCurrentBatchID(batch.getID());
			
			db.getUserDAO().update(user);
			db.getBatchDAO().update(batch);
			
			db.endTransaction(true);
			return new DownloadBatchResult(batch, project, fieldsList);
		}
		catch (DatabaseException e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unused")
	public static SubmitBatchResult submitBatch(String _username, String _password, int _batchID, String _recordValues) throws ServerException
	{
		User user = validateUser(_username, _password).getUser();
		if( user  == null ||
			user.getCurrentBatchID() != _batchID)
		{
			throw new ServerException();
		}
		
		Database db = new Database();
		SubmitBatchResult result;
		
		try 
		{
			db.startTransaction();
			int fieldID 		= db.getFieldDAO().getFieldsByProjectID(db.getBatchDAO().get(_batchID).getParentProjectID()).get(0).getID();
			int projectID 		= db.getBatchDAO().get(_batchID).getParentProjectID();
			int correct_num_cells 	= db.getFieldDAO().getFieldsByProjectID(projectID).size();
			int correct_num_records 	= db.getProjectDAO().get(projectID).getRecordsPerImage();

			String[] records 	= _recordValues.split(";",-1);
			String[] cells 		= null;
			
			for(int i = 0; i < records.length; i++)
			{
				cells 		= records[i].split(",", -1);
				Record r 	= new Record(-1, db.getBatchDAO().get(_batchID).getID());
				db.getRecordDAO().add(r);
				
				if(cells.length != correct_num_cells)
				{
					db.endTransaction(false);
					throw new ServerException();
				}
				
				for(int j = 0; j < cells.length; j++)
				{
					db.getCellDAO().add(new Cell(r.getID(), fieldID + j, cells[j]));
				}
			}
			
			user.setRecordsIndexed(user.getRecordsIndexed() + correct_num_records);
			user.setCurrentBatchID(0);
			
			db.getUserDAO().update(user);
			
			db.endTransaction(true);
			return new SubmitBatchResult(true);
			
		}
		catch (DatabaseException e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

	public static FieldResult getFields(String _username, String _password, int _parentProjectID) throws ServerException
	{
		User user = validateUser(_username, _password).getUser(); 
		if(user == null)
		{
			throw new ServerException();
		}
		
		Database db = new Database();
		
		try 
		{
			db.startTransaction();
			List<Field> fields = null;
			
			if(_parentProjectID == -1)
			{
				fields = db.getFieldDAO().getAll();
			}
			
			else
			{
				int maxProjectID = db.getProjectDAO().getAll().size();
				
				if(_parentProjectID > maxProjectID ||
				   _parentProjectID < 1 )
				{
					db.endTransaction(false);
					throw new ServerException();
				}
				
				fields = db.getFieldDAO().getFieldsByProjectID(_parentProjectID);
			}
			db.endTransaction(true);
			return new FieldResult(fields);
		}
		catch (DatabaseException e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

	public static SearchResult search(String _username, String _password, String _fieldIDs, String _searchValues) throws ServerException
	{
		User user = validateUser(_username, _password).getUser();
		if(user == null || _searchValues.isEmpty())
		{
			throw new ServerException();
		}

		Database db = new Database();	
		List<List<String>> resultList = new ArrayList<List<String>>();
		
		try
		{
			db.startTransaction();
			int maxFieldID = db.getFieldDAO().getAll().size();			
			
			String[] fieldIdList = _fieldIDs.split(",", 0);
			String[] wordList = _searchValues.split(",", 0);
			
			for(String f : fieldIdList)
			{
				if(Integer.parseInt(f) < 1 || Integer.parseInt(f) > maxFieldID)
				{
					db.endTransaction(false);
					return new SearchResult(null);
				}
				
				List<Cell> cellsList = db.getCellDAO().getAll(Integer.parseInt(f));
				
				for(String s : wordList)
				{
					List<String> subList = new ArrayList<String>();
					for(Cell cell : cellsList)
					{
						if(cell.getValue().toLowerCase().equals(s.toLowerCase()))
						{							
							int record_id 	= cell.getParentRecordID();
							Record record 	= db.getRecordDAO().get(record_id);
							int batch_id	= record.getParentBatchID();
							Batch batch		= db.getBatchDAO().get(batch_id);
							
							List<Record> recordsList = db.getRecordDAO().getAll(batch_id);
							
							int min = recordsList.get(0).getID();
							
							int recID = cell.getParentRecordID()-min+1;
							
							subList.add(batch.getID() 				+ "\n");
							subList.add(batch.getUrl() 				+ "\n");
							subList.add(recID						+ "\n"); 
							subList.add(cell.getParentFieldID() 	+ "\n");							
						}
					}
					resultList.add(subList);
				}
			}
			
			db.endTransaction(true);
		}
		catch (DatabaseException e) 
		{
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		
		return new SearchResult(resultList);
	}
}