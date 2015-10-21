package shared.communication;

import java.io.*;

public class SearchResult
{
	int batchID;
	File imgURL;
	int recordNum;
	int fieldID;
	boolean fail;
	
	
	public SearchResult(int _batchID, File _imgURL, int _recordNum, int _fieldID, boolean _fail)
	{
		batchID = _batchID;
		imgURL = _imgURL;
		recordNum = _recordNum;
		fieldID = _fieldID;
		fail = _fail;
	}
	
	
	public int getBatchID()		{ return batchID; 	}
	public File getImgURL()		{ return imgURL; 	}
	public int getRecordNum()	{ return recordNum; }
	public int getFieldID()		{ return fieldID; 	}
	public boolean fail()		{ return fail;		}
}
