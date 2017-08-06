import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.*;
import javax.swing.JDialog;


public class BitMapGUI extends JFrame implements ActionListener, ChangeListener, SwingConstants
{
	private JPanel bitmapPanel;
	private JPanel sliderPanel;
	private JPanel previewPanel;
	private JPanel previousColorsPanel;
	private JPanel advancedPanel;
	private int rows;
	private int cols;

	private ArrayList<JButton> pixelButtons;
	private JSlider[] sliders;
	private JButton[] previousColors;



	public BitMapGUI(int r, int c)
	{
		super("World's Best Looking Bitmap... thing");

		rows = r;
		cols = c;
		sliders = new JSlider[3];
		previousColors = new JButton[5];
		pixelButtons = new ArrayList<JButton>(rows*cols);

	
		// Creates the pixel buttons (JPanel #1)
		createPixels(rows, cols);
		createSlides();
		createPreview();
		createPreviousColors();
		createAdvancedSettings();
		createBitmapButton();

		// Sets the layout type to Border (Allows placement in NORTH, SOUTH, CENTER, etc)
		setLayout(new BorderLayout());
		
		add(bitmapPanel, BorderLayout.CENTER);
		JPanel eastGrid = new JPanel(new GridLayout(3, 1));
		eastGrid.add(advancedPanel);
		eastGrid.add(sliderPanel);
		eastGrid.add(previewPanel);
		add(eastGrid, BorderLayout.EAST);
		add(previousColorsPanel,BorderLayout.SOUTH);

		// Set size of the window
		setSize(1000,500);

		// Set the X button to kill the process
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set location on monitor
		setLocation(100, 100);

		// Make sure the window is visible
		setVisible(true);



	}

	private void createPixels(int r, int c)
	{
		bitmapPanel = new JPanel();
		bitmapPanel.setLayout(new GridLayout(r, c));
		bitmapPanel.setPreferredSize(new Dimension(600, 600));
		JButton b;


		// Create the pixel buttons
		for(int i = 0; i < (r * c); i++)
		{
			// Used for label
			b = new JButton(Integer.toString(i));
			b.setPreferredSize(new Dimension(25,25));
			b.addActionListener(this);
			pixelButtons.add(b);
			bitmapPanel.add(b);

		}
	}

	private void createSlides()
	{
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(3,2));
		sliderPanel.setPreferredSize(new Dimension(400, 200));
		JSlider r;
		JLabel l;

		String[] s = new String[3];
		s[0] = "red";
		s[1] = "green";
		s[2] = "blue";

		for(int i = 0; i < 3; i++)
		{
			l = new JLabel(s[i]);
			r = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);

			// Set slider and label settings
			r.addChangeListener(this);
			r.setMajorTickSpacing(25);
			r.setPaintTicks(true);
			l.setHorizontalAlignment(LEFT);
			l.setHorizontalTextPosition(LEFT);
			l.setLabelFor(r);

			// Add the slider to array of sliders and the JPanel
			sliders[i] = r;
			sliderPanel.add(r);
			sliderPanel.add(l);
		}

	}

	private void createPreview()
	{
		previewPanel = new JPanel();
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(300, 300));
		Color c = new Color(0, 0, 0);

		pane.setBackground(c);
		previewPanel.add(pane);
		

	}
	private void createPreviousColors()
	{
		previousColorsPanel = new JPanel();
		previousColorsPanel.setLayout(new FlowLayout());
		previousColorsPanel.setPreferredSize(new Dimension(1000, 250));

		Color c = new Color(0, 0, 0);

		for(int i = 0; i < 5; i++)
		{
			JButton p = new JButton();

			// Anon function
			p.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// Set slider values to this buttons background
					JButton source = (JButton)e.getSource();

					Color c = source.getBackground();

					sliders[0].setValue(c.getRed());
					sliders[1].setValue(c.getGreen());
					sliders[2].setValue(c.getBlue());
				}
			});
			p.setBackground(c);	
			p.setPreferredSize(new Dimension(175, 50));

			previousColors[i] = p;
			previousColorsPanel.add(p);
		}
		

	}
	private void createBitmapButton()
	{
		JButton createBitmap = new JButton("Create");
		createBitmap.setPreferredSize(new Dimension(200, 100));
		createBitmap.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Open File Chooser and ask user for directory
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retVal = fc.showOpenDialog(bitmapPanel);
				String chosenDirectory = fc.getSelectedFile().getAbsolutePath();

				// Get the name of the file
				JOptionPane.showMessageDialog(null, "Enter the file name");
				String bmpName = JOptionPane.showInputDialog("Enter the file name");

				// Create Icon
				// Icon has a Pixel array. Convert buttons into Pixels and set them
				Icon userBMP = new Icon(rows, cols);

				// Convert button to pixel and place in corresponding row/col of Icon
				for(int r = 0; r < rows; r++)
				{
					for(int c = 0; c < cols; c++)
					{
						Color current = bitmapPanel.getComponent((r * cols) + c).getBackground();
						userBMP.setPixel(r, c, new Pixel(current.getRed(), current.getGreen(), current.getBlue()));
					}
				}
			
				// createBitmap
				
				userBMP.createBMP(chosenDirectory + "/" + bmpName);
			}
		});
		previousColorsPanel.add(createBitmap);
	}
	private void createAdvancedSettings()
	{
		advancedPanel = new JPanel();

		JCheckBox settings = new JCheckBox("Advanced Options");

		advancedPanel.add(settings);
	}

	private void splatterFill(int r, int c, JButton origin)
	{
		// pixelPanel is 1D array
		JButton temp = new JButton();
		int index = pixelButtons.indexOf(origin) - cols;

		// Start at origin point and fill col
		// Add one col and fill
		for(int i = 0; i <= r ; i++)
		{
			// Increment index by 1 row
			index = index + cols;
			// Iterate over cols and setBackground
			for(int j = 0; j <= c; j++)
			{
				temp = pixelButtons.get(index + j);
				temp.setBackground(new Color(sliders[0].getValue(), sliders[1].getValue(), sliders[2].getValue()));
			}

		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JCheckBox temp = (JCheckBox)advancedPanel.getComponent(0);
		JButton source = (JButton)e.getSource();
		if(temp.isSelected() == false)
		{
			// Set button background to preview background
			
			source.setBackground(previewPanel.getComponent(0).getBackground());

			//Set oldest previousColor to new color
			previousColors[4].setBackground(previousColors[3].getBackground());
			previousColors[3].setBackground(previousColors[2].getBackground());
			previousColors[2].setBackground(previousColors[1].getBackground());
			previousColors[1].setBackground(previousColors[0].getBackground());
			previousColors[0].setBackground(source.getBackground());
		}
		else
		{
			// Advanced settings is toggled

			JDialog advancedOptionDialog = new JDialog();
			advancedOptionDialog.setLayout(new GridLayout(2, 1));
			
			// Option 1, splatterFill
			JPanel splatterPanel = new JPanel();
			JButton splatterSelect = new JButton("Splatter Fill");
			splatterPanel.add(splatterSelect);
			splatterSelect.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// User selected splatterFill option
					JTextField rows = new JTextField(5);
					JTextField cols = new JTextField(5);

					JPanel inputFields = new JPanel();
					inputFields.add(new JLabel("rows:"));
					inputFields.add(rows);
					inputFields.add(Box.createHorizontalStrut(15));
					inputFields.add(new JLabel("cols:"));
					inputFields.add(cols);

					int result = JOptionPane.showConfirmDialog(null, inputFields, "Please enter rows and cols", JOptionPane.OK_CANCEL_OPTION);
					int numRow = Integer.parseInt(rows.getText());
					int numCol = Integer.parseInt(cols.getText());

					splatterFill(numRow, numCol, source);

				}
			});

			// Option 2, bitmapFill
			JPanel bitmapLoadPanel = new JPanel();
			JButton bitmapSelect = new JButton("Load Bitmap");
			bitmapLoadPanel.add(bitmapSelect);
			bitmapSelect.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// This will be complex, will make this into a seperate function

					// Prompt user to select their own bitmap file
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("BMP FILES", "bmp", "bitmap");
					fc.setFileFilter(bmpFilter);
					int retVal = fc.showOpenDialog(bitmapPanel);
					String chosenDirectory = fc.getSelectedFile().getAbsolutePath();

					// Open the bitmap file
					try
					{
						File userMap = new File(chosenDirectory);
						BufferedImage image = ImageIO.read(userMap);
						JButton confirm = new JButton("Confirm");
						JButton wrongImage = new JButton("Wrong BMP");

						// Dialog and Panel will be used to display the image
						JDialog imageDisplayDialog = new JDialog();
						JPanel imageDisplayPanel = new JPanel() {
							@Override
							protected void paintComponent(Graphics g)
							{
								super.paintComponent(g);
								g.drawImage(image, 0, 0, super.getWidth(), super.getHeight(), null);
							}
						};

						// User confirm buttons
						JPanel buttonPanel = new JPanel();
						buttonPanel.add(confirm);
						buttonPanel.add(wrongImage);
						confirm.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								// Call loadBitmap
								imageDisplayDialog.dispose();
								loadBitmap(image, source);
							}
						});
						wrongImage.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								imageDisplayDialog.dispose();
							}
						});


						imageDisplayPanel.setSize(200, 200);
						imageDisplayDialog.getContentPane().add(imageDisplayPanel);
						imageDisplayDialog.getContentPane().add(buttonPanel);
						imageDisplayDialog.getContentPane().setPreferredSize(new Dimension(700,300));
						imageDisplayDialog.setSize(500,300);
						imageDisplayDialog.pack();
						imageDisplayDialog.setLocation(200,200);
						imageDisplayDialog.setVisible(true);

					}
					catch(IOException q)
					{
						System.out.println(q);
					}
					
				}
			});

			advancedOptionDialog.setSize(300,300);
			advancedOptionDialog.getContentPane().add(splatterPanel);
			advancedOptionDialog.getContentPane().add(bitmapLoadPanel);
			advancedOptionDialog.getContentPane().setPreferredSize(new Dimension(200, 100));
			advancedOptionDialog.pack();
			advancedOptionDialog.setLocation(100, 100);
			advancedOptionDialog.setVisible(true);
		}
	}

	private void loadBitmap(BufferedImage image, JButton origin)
	{
		// Iterate through images height and width
		// Take pixel at iterator
		// Take RGB and add to button
		int buttonIndex = pixelButtons.indexOf(origin);
		for(int bufR = 0; bufR < image.getHeight(); bufR++)
		{
			for(int bufC = 0; bufC < image.getWidth(); bufC++)
			{
				pixelButtons.get(buttonIndex + (bufR * cols) + bufC).setBackground(new Color(image.getRGB(bufR, bufC)));
			}
		}
	}
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		// Retrieve RGB values from the sliders
		int r = sliders[0].getValue();
		int g = sliders[1].getValue();
		int b = sliders[2].getValue();

		// Change previewPanel's background color
		previewPanel.getComponent(0).setBackground(new Color(r, g, b));

	}
}
