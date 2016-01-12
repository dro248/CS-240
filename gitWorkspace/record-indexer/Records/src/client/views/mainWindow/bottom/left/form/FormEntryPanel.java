package client.views.mainWindow.bottom.left.form;

import java.awt.Dimension;

import javax.swing.JPanel;

import client.facade.*;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;

@SuppressWarnings("serial")
public class FormEntryPanel extends JPanel implements BatchStateListener
{
	private BatchState batchState;
	
	public FormEntryPanel()
	{
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
	}
	
	
	
	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	@Override
	public void valueChanged(int row, int column, String newValue) 	{}
	@Override
	public void selectedCellChanged(int row, int column) 			{}
	@Override
	public Coordinate getWindowPosition() 							{ return null; }
	@Override
	public Dimension getWindowSize()								{ return null; }
	@Override
	public int getVPaneDivPosition()								{ return 0; }
	@Override
	public int getHPaneDivPosition()								{ return 0; }
	@Override
	public void setButtonAvailability()								{}
	@Override
	public DataTable getDataTable()									{ return null; }
	@Override
	public DataForm getDataForm()									{ return null; }
	@Override
	public FieldHelpPanel getFieldHelp()							{ return null; }
	@Override
	public ImageNavigationPanel getImageNavigation() 				{ return null; }




	@Override
	public void translate(int w_X, int w_Y) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void setScale(Double scale) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public double getZoom() {
		// TODO Auto-generated method stub
		return 0;
	}
}