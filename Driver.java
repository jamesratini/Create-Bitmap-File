import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class Driver
{
	public static void main(String args[])
	{
		System.out.println("Bitmap thing");
		// User selected splatterFill option
		JTextField rows = new JTextField(5);
		JTextField cols = new JTextField(5);

		Object[] fields = {
			"rows: ", rows,
			"cols: ", cols,
		};
		int option = JOptionPane.showConfirmDialog(null, fields, null, JOptionPane.OK_CANCEL_OPTION);

		BitMapGUI gui = new BitMapGUI(Integer.parseInt(rows.getText()), Integer.parseInt(cols.getText()));
	}
}
