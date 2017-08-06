import java.util.ArrayList;
import java.util.Collections;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Icon
{
	//2D Array of Pixels
	private ArrayList<ArrayList<Pixel>> pixelList = new ArrayList<ArrayList<Pixel>>();
	private ArrayList<Byte> bytes = new ArrayList<Byte>();
	int imgWidth = 0;
	int imgHeight = 0;

	//Constructors
	public Icon()
	{
		// NOTE: Make setters and getters for this
		imgWidth = 40;
		imgHeight = 40;

		Pixel newPixel = new Pixel(255, 255, 255);
		

		for( int i = 0; i < imgWidth; i++)
		{
			// Make sure there are arrays in the empty arrayList
			pixelList.add(new ArrayList<Pixel>());

			for(int j = 0; j < imgHeight; j++)
			{
				// Get the current row array, then add the new pixel to it
				pixelList.get(i).add(new Pixel(255, 255, 255));
			}
		}
		
	}
	public Icon(int width, int height)
	{
		
		imgWidth = width;
		imgHeight = height;
		
		Pixel newPixel = new Pixel(255, 255, 255);
		

		for( int i = 0; i < width; i++)
		{
			// Make sure there are arrays in the empty arrayList
			pixelList.add(new ArrayList<Pixel>());

			for(int j = 0; j < height; j++)
			{
				// Get the current row array, then add the new pixel to it
				pixelList.get(i).add(new Pixel(255,255,255));
			}
		}
		
	}
	//Getters Setters
	public
	 Pixel getPixel(int r, int c)
	{
		// Use given row and column to return proper pixel
		return pixelList.get(r).get(c);
	}
	public void setPixel(int r, int c, Pixel newPixel)
	{
		pixelList.get(r).add(c, newPixel);
	}

	private void setupBMP()
	{

		int padding = 4 - ((imgWidth * 3) % 4);
		// Amount of data the image contains
		int dataSize = ((imgWidth * 3) + padding) * imgHeight;
		// Total size of the .bmp file
		int fileSize = 54 + dataSize;

		// File Header
		bytes.add((byte)0x42);
		bytes.add((byte)0x4D);

		// Add the file size to the header
		bytes.add((byte)fileSize);
		bytes.add((byte)(fileSize >> 8));
		bytes.add((byte)(fileSize >> 16));
		bytes.add((byte)(fileSize >> 24));
		
		// Empty bytes in place of reserved bytes
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);

		// Pixel data Offset
		// offset by 54 bytes
		bytes.add((byte)0x36);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		

		//Image Header
		// Image Header size = 40 bytes
		bytes.add((byte)0x28);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		

		// Width of the image
		bytes.add((byte)imgWidth);
		bytes.add((byte)(imgWidth>>8));
		bytes.add((byte)(imgWidth>>16));
		bytes.add((byte)(imgWidth>>24));

		// Height of the image
		bytes.add((byte)imgHeight);
		bytes.add((byte)(imgHeight>>8));
		bytes.add((byte)(imgHeight>>16));
		bytes.add((byte)(imgHeight>>24));

		// Color Planes
		bytes.add((byte)0x01);
		bytes.add((byte)0x00);

		// bits per pixel
		bytes.add((byte)0x18); //24 bits per pixel (3 8 bit bytes)
		bytes.add((byte)0x00);

		// No compression, empty bytes
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);

		// Size of data
		bytes.add((byte)dataSize);
		bytes.add((byte)(dataSize>>8));
		bytes.add((byte)(dataSize>>16));
		bytes.add((byte)(dataSize>>24));

		// Horizontal resolution
		bytes.add((byte)0x13);
		bytes.add((byte)0x0B);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);

		// Vertical resolution
		bytes.add((byte)0x13);
		bytes.add((byte)0x0B);
		bytes.add((byte)0);
		bytes.add((byte)0);

		//Number of colors
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);

		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);
		bytes.add((byte)0x00);



		// Adding the Pixels
		// Iterate each row
		for(int i = imgHeight - 1; i >= 0; i-- )
		{
			// Iterate each col
			for(int j = 0; j < imgWidth; j++)
			{
				// Add the pixel into the data map
				bytes.add((byte)(pixelList.get(i).get(j).getBlue()));
				bytes.add((byte)(pixelList.get(i).get(j).getGreen()));
				bytes.add((byte)(pixelList.get(i).get(j).getRed()));
			}

			// Add the padding
			for(int x =0; x < padding; x++)
			{
				bytes.add((byte)0x00);
			}
		}

	}

	public void createBMP(String name)
	{
		// Create byte array to push onto file
		setupBMP();

		try
		{
	
			FileOutputStream outputStream;
			File bmpFile;
			// make a byte array the size of how many bytes there are
			byte[] byteArray = new byte[bytes.size()];

			// create the file and the output stream
			bmpFile = new File(name + ".bmp");
			outputStream = new FileOutputStream(bmpFile);

			if(!bmpFile.exists())
			{
				System.out.println("File didnt exist");
				bmpFile.createNewFile();
			}

			for(int i = 0; i < bytes.size(); i++)
			{
				// Fill the array
				byteArray[i] = bytes.get(i);
			}
			
			// Write only likes an array of bytes, not individual bytes
			outputStream.write(byteArray);
			outputStream.flush();
			outputStream.close();

			System.out.println(" Successfully wrote file");	
		}
		catch(IOException x)
		{
			System.out.println("exception thrown");
			x.printStackTrace();
		}

	}

	public void printHexString()
	{
		String temp = new String();

		// Iterate each pixel
		for(int i = 0; i < imgHeight; i++)
		{
			for(int j = 0; j < imgWidth; j++)
			{
				temp = temp + pixelList.get(i).get(j).printHex() + " ";
			}

			System.out.println(temp);
			temp = "";
		}
	}
		
}