package client.facade;

public class Coordinate
{
	int x_value;
	int y_value;
	
	public Coordinate(int _x, int _y)
	{
		x_value = _x;
		y_value = _y;
	}
	
	// getters & setters
	public int get_X() { return x_value; }
	public int get_Y() { return y_value; }
	
	public void set_X(int _x) { x_value = _x; }
	public void set_Y(int _y) { y_value = _y; }
}
