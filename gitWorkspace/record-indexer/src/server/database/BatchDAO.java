package server.database;

import shared.model.*;

public class BatchDAO 
{
	Database database;
	
	public BatchDAO(Database db)
	{
		database = db;
	}
	
	// Create
	/**
	 * Adds a batch to the database.
	 * @param batch
	 * @return whether or not the batch was successfully added to database.
	 */
	public boolean addBatch(Batch batch) { return false; }
	
	// Read
	/**
	 * Returns the URL of a given batch.
	 * @return URL
	 */
	public String getURL() { return ""; }
	
	/**
	 * Returns the Batch of a given batch.
	 * @return BatchID
	 */
	public Batch getBatch(int projectID) { return null;}
	
	/**
	 * Gets the first available batch by projectID.
	 * @param projectID
	 * @return Batch
	 */
	public Batch getFirstAvailableBatch(int projectID) { return null; }
	
	// Update
	/**
	 * Updates a batch with the necessary items
	 * @param batch
	 * @return
	 */
	public boolean updateBatch(Batch batch) { return false; } 
}
