package client.communication;

import shared.communication.*;

public class ClientCommunicator {

	/**
	 * Returns the UserResult object based on the parameters in UserParams.
	 * @param UserParams (username, password)
	 * @return UserResult
	 */
	public UserResult validateUser(UserParams params) 					{ return null; }
	
	/**
	 * Returns the ProjectResult based on the parameters in ProjectParams.
	 * @param ProjectParams (username, password, projectID)
	 * @return ProjectResult
	 */
	public ProjectResult getProjects(ProjectParams params) 				{ return null; }
	
	/**
	 * Returns ImgResult based on the parameters in ImgParams.
	 * @param ImgParams (username, password, projectID)
	 * @return ImgResult
	 */
	public ImgResult getSampleImage(ImgParams params) 					{ return null; }
	
	/**
	 * Returns DownloadBatchResult based on the parameters in DownloadBatchParams.
	 * @param DownloadBatchParams (username, password, projectID)
	 * @return DownloadBatchResult
	 */
	public DownloadBatchResult downloadBatch(DownloadBatchParams params){ return null; }
	
	/**
	 * Returns SubmitBatchResult based on the parameters in SubmitBatchParams.
	 * @param SubmitBatchParams  (username, password, projectID, batchID, field)
	 * @return SubmitBatchResult
	 */
	public SubmitBatchResult submitBatch(SubmitBatchParams params)		{ return null; }
	
	/**
	 * Returns FieldResult based on the parameters in FieldParams.
	 * @param FieldParams  (username, password, projectID)
	 * @return FieldResult
	 */
	public FieldResult getFields(FieldParams params) 					{ return null; }
	
	/**
	 * Returns SearchResult based on the searchValues in SearchParams.
	 * @param SearchParams  (username, password, projectID, fields, searchValues)
	 * @return SearchResult
	 */
	public SearchResult search(SearchParams params) 					{ return null; }
	
	
	// Based on Rodham's ContactManager example
	/**
	 * Returns an object taken from ClientCommunicator file
	 * @return Object
	 */
	//private Object doGet( /*WHAT GOES HERE?*/ )	{ return null; }
	
	//private void doPost() {}
}
