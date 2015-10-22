package server.database;

import shared.model.Cell;

public class CellDAO
{

	Database database;
	
	public CellDAO(Database db)
	{
		database = db;
	}
	
	/**
	 * Adds a cell to the database.
	 * @param cell
	 * @return whether or not cell was successfully added.
	 */
	public boolean addCell(Cell cell) throws DatabaseException 
	{ return false; }
	
	/**
	 * Gets a cell based on Field and Record coordinates.
	 * @param fieldCoord
	 * @param recordCoord
	 * @return Cell
	 */
	public Cell getCell(int fieldCoord, int recordCoord) { return null; }
	
	/**
	 * Updates a given cell.
	 * @param cell
	 * @return Whether cell successfully updated database or not.
	 */
	public boolean updateCell(Cell cell) { return false; }
}
