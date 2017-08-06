public class Pixel
{
	private int rgb; // rgb is a 32 bit integer. 4 Bytes of 8 Bits. 3 of 4 groups
					 // will be used to represent an 8 bit integer (Red, Green, and Blue)


	// Constructor
	public Pixel(int r, int g, int b)
	{
		// r, g, and b must be between 0 and 255
		rgb = 0;

		setRed(r);
		setGreen(g);
		setBlue(b);
	}

	// Methods
	public String printHex()
	{
		return Integer.toHexString(rgb);
	}
	// Getters Setters
	public int getBlue()
	{
		int rgbCopy = rgb;
		
		rgbCopy = rgbCopy & 0xFF;
		
		return rgbCopy;
	}
	public int getGreen()
	{
		// Set green to rgb shifted right 8 bits, aka the GREEN in rgb
		int rgbCopy = rgb;

		//Push blue out and green to last byte
		rgbCopy = rgbCopy >> 8; 

		rgbCopy = rgbCopy & 0xFF;
		
		return rgbCopy;
	}
	public int getRed()
	{
		// Set red to rgb shift 16 bits right, aka the RED in RGB
		int rgbCopy = rgb;

		// Push green and blue out
		rgbCopy = rgbCopy >> 16;

		// Take content of Least Significant Byte
		rgbCopy = rgbCopy & 0xFF;

		return rgbCopy;
	}
	public void setBlue(int b)
	{
		if(b >= 0 && b <= 255)
		{
			b = b << 0;
			//System.out.println(b);
			rgb = rgb | b;	
		}
		else
		{
			System.out.println("Invalid value. 1 - 255");
		}
		
	}
	public void setGreen(int g)
	{
		if(g >= 0 && g <= 255)
		{
			g = g << 8;
			//System.out.println(g);
			rgb = rgb | g;	
		}
		else
		{
			System.out.println("Invalid value. 1 - 255");
		}
		
	}
	public void setRed(int r)
	{
		if(r >= 0 && r <= 255)
		{
			// Shift r 16 bits to the left
			r = r << 16;
			//System.out.println(r);
			// 2nd set of 8 bits in rgb is now set to r
			rgb = rgb | r;	
		}
		else
		{
			System.out.println("Invalid value. 1 - 255");
		}
		
	}


}