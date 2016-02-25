import java.io.FileNotFoundException;


public class ImageEditor {

	public static void main(String[] args) //throws FileNotFoundException 
	{
		
//		System.out.println("TESTING: This is my ImageEditor!!");
//		System.out.println("TESTING: This is my ImageEditor!!");
//		System.out.println("TESTING: This is my ImageEditor!!");
//		System.out.println("TESTING: This is my ImageEditor!!");
		
		try {
			// Data Members
			Image img = new Image(args[0]);
			
			switch(args[2]) 
			{
			case "invert"	 : img.invert();			break;
			case "grayscale" : img.grayscale();			break; 
			case "emboss"	 : img.emboss();			break;
			case "motionblur": img.motionblur(args[3]);	break;
			}
			
			img.output(args[1]);
			System.out.println("Edit Complete!");
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
		}
	}
}