package client.facade;

import java.awt.Dimension;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;


public interface BatchStateListener 
{	
	public void valueChanged(int row, int column, String newValue);
	public void selectedCellChanged(int row, int column);
	public Coordinate getWindowPosition();
	public Dimension getWindowSize();
	public int getVPaneDivPosition();
	public int getHPaneDivPosition();
	public void setButtonAvailability();
	public DataTable getDataTable();
	public DataForm	getDataForm();
	public FieldHelpPanel getFieldHelp();
	public ImageNavigationPanel getImageNavigation();
	public void translate(int w_X, int w_Y);
	public void setScale(Double scale);
	public double getZoom();
}
