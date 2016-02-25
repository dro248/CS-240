package client.views.mainWindow.bottom.right;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.JEditorPane;

import client.facade.*;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.left.table.DataTable;

@SuppressWarnings("serial")
public class FieldHelpPanel extends JEditorPane implements BatchStateListener
{
	private BatchState 	batchState;
	@SuppressWarnings("unused")
	private URL			currentURL;

	
	public FieldHelpPanel()
	{
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
		this.setEditable(false);

		
		createComponents();
	}
	
	
	public void createComponents()
	{
		this.setContentType("text/html");
		
		// testing
		System.out.println("Fieldhelp was called!!");
		
		
		
		
	}
	
	
	
	
	
	//////////////////////////////////////
	//									//
	//		BatchStateListener Stuff	//
	//									//
	//////////////////////////////////////
	@Override
	public void valueChanged(int row, int column, String newValue) 
	{}

	@Override
	public void selectedCellChanged(int row, int column)
	{
		// Get helpHTML from given column number
		URL url;
		try 
		{
			String HOST = ClientFacade.get().getHost();
			int PORT	= ClientFacade.get().getPort();
			
			url = new URL("http://" + HOST + ":" + PORT + "/Records/" + batchState.getFields().get(column).getHelpHTML());
//			url = new URL("http://" + HOST + ":" + PORT + "/Records/" + batchState.getFields().get(column-1).getHelpHTML());			
			this.setPage(url);
		} catch (Exception e) 
		{}
		
	}

	@Override
	public Coordinate getWindowPosition()
	{
		return null;
	}

	@Override
	public Dimension getWindowSize()
	{
		return null;
	}

	@Override
	public int getVPaneDivPosition()
	{
		return 0;
	}

	@Override
	public int getHPaneDivPosition()
	{
		return 0;
	}

	@Override
	public void setButtonAvailability()
	{}


	@Override
	public DataTable getDataTable()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public DataForm getDataForm()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FieldHelpPanel getFieldHelp()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ImageNavigationPanel getImageNavigation()
	{
		// TODO Auto-generated method stub
		return null;
	}


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
