
public class Pixel {
	// (RED,GREEN,BLUE) value
	private int red;
	private int green;
	private int blue;
	
	// Constructor
	public Pixel (int RED, int GREEN, int BLUE)
	{
		red = RED;
		green = GREEN;
		blue = BLUE;
	}
	
	public void invert()
	{
		red = 255 - red;
		green = 255 - green;
		blue = 255 - blue;
	}
	
	public void grayscale()
	{
		red = green = blue = ((red + green + blue) / 3);
	}
	
	public void setPixelValues(int r, int g, int b)
	{
		red = r;
		green = g;
		blue = b;
	}
	
	public int getRedValue()
	{
		return red;
	}
	
	public int getGreenValue()
	{
		return green;
	}
	
	public int getBlueValue()
	{
		return blue;
	}
	
	public String toString()
	{
		StringBuilder concat = new StringBuilder(red + "\n" + green + "\n" + blue + "\n");
		return concat.toString();
	}
}