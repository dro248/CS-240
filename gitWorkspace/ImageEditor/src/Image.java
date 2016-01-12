import java.io.*;
import java.util.*;
import java.lang.Math;

public class Image {
	// Data Members
	private Pixel[][] myImg;
	private Pixel[][] newImage;
	
	private String in_filename;
	private int WIDTH;
	private int HEIGHT;
	private int MAX_VALUE;

	// Constructor: attempts to automatically load image into memory
	public Image(String fn)
	{	
		System.out.println(fn);		
		in_filename = fn;
		
		try {
			// Attempt to load the .PPM file into memory...
			// Scan in the image
			scan(in_filename);
		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
		}
	}
	
	private void scan(String filename) throws FileNotFoundException 
	{
		Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File(filename))));
		scanner.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#^\\n]*\\n)(\\s*)");
		
		// Toss the first line
		scanner.next();

		// Important PPM image values
		WIDTH = Integer.parseInt(scanner.next());
		HEIGHT = Integer.parseInt(scanner.next());
		MAX_VALUE = Integer.parseInt(scanner.next());
	
		//myImg = new Pixel[WIDTH][HEIGHT];
		myImg = new Pixel[HEIGHT][WIDTH];
		newImage = new Pixel[HEIGHT][WIDTH];
		
		// Sets RGB values of each Pixel within myImg
		for(int h = 0; h < HEIGHT; h++)
		{
			for(int w = 0; w < WIDTH; w++)
			{
				myImg[h][w] = new Pixel(Integer.parseInt(scanner.next()),Integer.parseInt(scanner.next()),Integer.parseInt(scanner.next()));
				newImage[h][w] = new Pixel(0,0,0);
			}
		}
		scanner.close();
	}
	
	public void invert()
	{
		// call Pixel invert
		for(int h = 0; h < HEIGHT; h++)
		{
			for(int w = 0; w < WIDTH; w++)
				myImg[h][w].invert();
		}
	}
	
	public void grayscale()
	{
		// call Pixel grayscale
		for(int h = 0; h < HEIGHT; h++)
		{
			for(int w = 0; w < WIDTH; w++)
				myImg[h][w].grayscale();
		}
	}
	
	public void emboss()
	{
		for(int h = HEIGHT-1; h >= 0; h--)
		{
			for(int w = WIDTH-1; w >= 0; w--)
			{		
				// Edge case...
				if(w < 1 || h < 1)
				{
					myImg[h][w].setPixelValues(128,128,128);
				}
				// General case...
				else
				{
					// Calculate 
					int redDiff = myImg[h][w].getRedValue() - myImg[h-1][w-1].getRedValue();
					int greenDiff = myImg[h][w].getGreenValue() - myImg[h-1][w-1].getGreenValue();
					int blueDiff = myImg[h][w].getBlueValue() - myImg[h-1][w-1].getBlueValue();
					int maxDiff = redDiff;
					
					if(Math.abs(greenDiff) > Math.abs(maxDiff))
					{
						maxDiff = greenDiff;
					}
						
					if(Math.abs(blueDiff) > Math.abs(maxDiff))
					{
						maxDiff = blueDiff;
					}
					
					maxDiff += 128;
					
					if(maxDiff < 0)
					{
						maxDiff = 0;
					}
					
					if(maxDiff > 255)
					{
						maxDiff = 255;
					}
					
					myImg[h][w].setPixelValues(maxDiff, maxDiff, maxDiff);
				}
			}
		}
	}

	/*
	public void motionblur(String blur_length)
	{		
		if(blur_length.equals(""))
		{
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
			System.exit(0);
		}
		
		int length = Integer.parseInt(blur_length);
		
		if(length < 1)
		{
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
			System.exit(0);
		}
		
		if(length == 1)
		{
			return;
		}
		
		// image is [HEIGHT][WIDTH]
		for(int h = 0; h < HEIGHT; h++)
		{
			for(int w = 0; w < WIDTH; w++)
			{			
				int redAvg = 0;
				int greenAvg = 0;
				int blueAvg = 0;
				int counter = 0;
				
				for(int n = 0; n < length; n++)
				{
					// general case
					if(w + n < WIDTH)
					{
						counter++;
						// find average for R, G, B
						redAvg += myImg[h][w+n].getRedValue();
						greenAvg += myImg[h][w+n].getGreenValue();
						blueAvg += myImg[h][w+n].getBlueValue();
					}
					else
					{
						counter++;
						int r = (WIDTH-1) - w;
						redAvg += myImg[h][w+r].getRedValue();
						greenAvg += myImg[h][w+r].getGreenValue();
						blueAvg += myImg[h][w+r].getBlueValue();
					}
				}
				
				redAvg = redAvg / counter;
				greenAvg = greenAvg / counter;
				blueAvg = blueAvg / counter;
				
				//newImage[h][w].setPixelValues(redAvg, greenAvg, blueAvg);
				newImage[h][w].setPixelValues(50, 50, 50);
			}
		}
		
		myImg = newImage;
	}
	*/
	
	public void motionblur(String blur_length)
	{
		if(blur_length.equals(""))
		{
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
			System.exit(0);
		}
		
		int n = Integer.parseInt(blur_length);
		
		if(n < 1)
		{
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
			System.exit(0);
		}
		
		if(n == 1)
		{
			return;
		}
		
		for(int h = 0; h < HEIGHT; h++)
		{
			for(int w = 0; w < WIDTH; w++)
			{
				int redAvg = 0;
				int greenAvg = 0;
				int blueAvg = 0;
				int counter = 0;
				
				for(int blur = 0; blur < n; blur++)
				{
					if(w + blur < WIDTH)
					{
						redAvg += myImg[h][w+blur].getRedValue();
						greenAvg += myImg[h][w+blur].getGreenValue();
						blueAvg += myImg[h][w+blur].getBlueValue();
						counter++;
					}
				}
				redAvg /= counter;
				greenAvg /= counter;
				blueAvg /= counter;
				newImage[h][w].setPixelValues(redAvg, greenAvg, blueAvg);
			}
		}
		
		System.out.println("Original Image[0][0]: " + myImg[0][0].getRedValue() + "," + myImg[0][0].getGreenValue() + ","+ myImg[0][0].getBlueValue());
		System.out.println("New Image[0][0]: " + newImage[0][0].getRedValue() + "," + newImage[0][0].getGreenValue() + ","+ newImage[0][0].getBlueValue());
		
		myImg = newImage;
	}
	
	public void output(String filename) throws FileNotFoundException
	{
		PrintWriter writer = new PrintWriter(filename);
		StringBuilder concat = new StringBuilder();
		
		concat.append("P3\n" + WIDTH + " " + HEIGHT + "\n" + MAX_VALUE + "\n");
		
		//System.out.println("myImg.length: " + myImg.length);
		
		for(int h = 0; h < HEIGHT; h++)
		{
			for(int w = 0; w < WIDTH; w++)
				concat.append(myImg[h][w].toString());
		}
		
		writer.print(concat.toString());
		writer.close();
	}
}