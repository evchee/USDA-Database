package Database;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * USDA Database repository
 * Allows users to search and view the USDA open data in a user friendly matter
 * @author Eric Chee @ Alosha Reymer
 * @version 11/22/2015
 */
public class Main
{
	static FoodDatabase database;
	static ImageIcon icon = new ImageIcon("chicken_icon.png");
	
	/**
	 * Creates and sets up the frame containing the GUI
	 */
	public static void createMainframe()
	{
		//Creates the frame
		JFrame foodFrame = new JFrame("USDA Food Database");
		//Sets up the frame
		foodFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		foodFrame.setSize(1000,750);
		foodFrame.setResizable(false);
		foodFrame.setIconImage(icon.getImage());
		
		Gui gui = new Gui();
		
		gui.setOpaque(true);
		foodFrame.setContentPane(gui);
		
		foodFrame.setVisible(true);
	}
	
	
	public static void main(String[] args) throws IOException
	{
		//create new database
		database = new FoodDatabase();
		//create gui
		createMainframe();
	}
}
