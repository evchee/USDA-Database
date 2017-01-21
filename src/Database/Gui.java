
package Database;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class Gui extends JPanel implements ActionListener, ItemListener, ListSelectionListener {

	private JPanel mainPane, mainAddPane, mainAddLeftPane, mainAddRightPane, resultsPane, descriptionPane, nutrientsPane, footnotesPane,
			statsPane;
	private JTabbedPane weightsPane = new JTabbedPane();
	private DefaultListModel resultsModel = new DefaultListModel();
	private DefaultListModel footnotesModel = new DefaultListModel();
	private JList resultsList = new JList(resultsModel);
	private JList footnotesList = new JList(footnotesModel);
	private JTable addNutTable = new JTable();
	private JScrollPane listScroller, addNutScroller, footnotesScroller;
	private JButton searchButton, addButton, addFootnote, removeFootnote, addNutrient, removeNutrient;
	private JComboBox nutrientsBox, foodGroupBox;
	private JLabel descriptionLabel, manufactLabel, commonLabel, foodgroupLabel;
	private boolean[] searchQueryState = new boolean[25];
	private boolean[] searchOptionsState = new boolean[4];
	private Food[] foods;
	private int foodGroupSize = Main.database.getFoodGroups().getSize();
	private String[] nutrientsDesc = new String[150];
	private String[] nutrientsUnits = new String[150];
	private String[] nutrientColumns = {"Nutrient", "Amount" }, nutrientAddColumns = {"Nutrient", "Amount" };
	private JTextField[] nutrientsFields = new JTextField[150];
	private JTextField searchField, descriptionField, footnotesField, currentTextField, manufactField, commonField;
	private JRadioButtonMenuItem[] searchQuery = new JRadioButtonMenuItem[foodGroupSize + 1];
	private JRadioButtonMenuItem[] searchOptions = new JRadioButtonMenuItem[4];
	private Object[][] nutrientData, nutrientAddData = new Object[0][2];
	private GroupLayout groupLayoutLeft, groupLayoutRight;
	
	private int[] foodGroupNums = new int[foodGroupSize];

	/**
	 * Creates the GUI
	 */
	public Gui() {
		this.setLayout(new GridLayout());

		//Initializes the main panels
		mainPane = new JPanel(new BorderLayout());
		mainAddPane = new JPanel(new GridLayout(0,2));
		mainAddLeftPane = new JPanel();
		mainAddRightPane = new JPanel();
		
		//Creates the group layouts
		groupLayoutLeft = new GroupLayout(mainAddLeftPane);
		groupLayoutLeft.setAutoCreateGaps(true);
		groupLayoutLeft.setAutoCreateContainerGaps(true);
		groupLayoutRight = new GroupLayout(mainAddRightPane);
		groupLayoutRight.setAutoCreateGaps(true);
		groupLayoutRight.setAutoCreateContainerGaps(true);
		
		//Initializes the text fields
		descriptionField = new JTextField();
		manufactField = new JTextField();
		footnotesField = new JTextField();
		commonField = new JTextField();
		
		//Initializes the labels
		descriptionLabel = new JLabel("Long Description *");
		new JLabel("Footnotes");
		manufactLabel = new JLabel("Manufacturer");
		commonLabel = new JLabel("Common Name");
		foodgroupLabel = new JLabel("Foodgroup");
		
		//Initializes the scrollers
		footnotesList.setBorder(BorderFactory.createLoweredBevelBorder());
		addNutTable.setBorder(BorderFactory.createLoweredBevelBorder());
		addNutScroller = new JScrollPane(addNutTable);
		footnotesScroller = new JScrollPane(footnotesList);
		
		//Creates the combo boxes
		nutrientsBox = new JComboBox();
		foodGroupBox = new JComboBox();
		
		//Initializes the buttons and adds listeners to them
		addButton = new JButton("ADD");
		addButton.addActionListener(this);
		removeFootnote = new JButton("Remove");
		removeFootnote.addActionListener(this);
		addFootnote = new JButton("Add");
		addFootnote.setPreferredSize(removeFootnote.getPreferredSize());
		addFootnote.addActionListener(this);
		addNutrient = new JButton("Add");
		addNutrient.addActionListener(this);
		removeNutrient = new JButton("Remove");
		removeNutrient.addActionListener(this);
		
		//Acquires the nutrient descriptions and units
		Node<NutrientDefinition> tempDefNode = FoodDatabase.nutrientDef.getHead();
		for (int i = 0; i < nutrientsDesc.length; i++) {
			nutrientsDesc[i] = tempDefNode.getItem().getNutrDesc();
			nutrientsUnits[i] = tempDefNode.getItem().getUnits();
			nutrientsBox.addItem(nutrientsDesc[i] + " (" + nutrientsUnits[i] + ")");
			nutrientsFields[i] = new JTextField("0");
			tempDefNode = tempDefNode.getNext();
		}
		currentTextField = nutrientsFields[0];
		nutrientsBox.addActionListener(this);

		mainAddPane.add(mainAddLeftPane);
		mainAddPane.add(mainAddRightPane);
		
		// Creates the Tab Panel
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Search", mainPane);
		tabPane.addTab("Add", mainAddPane);

		// Creates the Search Panel
		JPanel searchPane = new JPanel();
		searchPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = .3;
		searchPane.add(searchButton = new JButton("SEARCH"), c);
		searchButton.setMnemonic(KeyEvent.VK_ENTER);
		searchButton.addActionListener(this);
		JMenuBar searchMenuBar = new JMenuBar();
		JMenu searchMenu = new JMenu("Foodgroups");
		JMenu optionsMenu = new JMenu("Search In");
		
		//Creates the Search Options
		searchOptions[0] = new JRadioButtonMenuItem("Long Description");
		searchOptions[0].addItemListener(this);
		searchOptions[1] = new JRadioButtonMenuItem("Manufacturer");
		searchOptions[1].addItemListener(this);
		searchOptions[2] = new JRadioButtonMenuItem("Scientific Name");
		searchOptions[2].addItemListener(this);
		searchOptions[3] = new JRadioButtonMenuItem("Common Name");
		searchOptions[3].addItemListener(this);
		for (int i = 0; i < 4; i++){
			searchOptions[i].setSelected(true);
			optionsMenu.add(searchOptions[i]);
			searchOptionsState[i] = true;
		}
		
		// Creating all the Filter buttons
		searchQuery[0] = new JRadioButtonMenuItem("All");
		Node<FoodGroup> tempNode = Main.database.getFoodGroups().getHead();
		for (int i = 1; i < searchQuery.length; i++) {
			searchQuery[i] = new JRadioButtonMenuItem(tempNode.getItem()
					.getFoodGroupDes());
			foodGroupBox.addItem(tempNode.getItem()
					.getFoodGroupDes());
			foodGroupNums[i-1] = tempNode.getItem().getFoodGroupNo();
			tempNode = tempNode.getNext();
		}
		searchQuery[0].addItemListener(this);
		searchQuery[0].setSelected(true);
		searchMenu.add(searchQuery[0]);
		
		
		//Sets up the default state of the search query
		for (int i = 1; i < searchQuery.length; i++) {
			searchQuery[i].addItemListener(this);
			searchQuery[i].setSelected(true);
			searchQueryState[i - 1] = true;
			searchMenu.add(searchQuery[i]);
		}
		
		//Finalizes the search panel
		searchMenuBar.add(optionsMenu);
		searchMenuBar.add(searchMenu);
		searchPane.add(searchMenuBar);
		c.weightx = 1;
		searchPane.add(searchField = new JTextField(), c);
		searchField.addActionListener(this);

		foodGroupBox.addActionListener(this);
		updateAddTab();
		mainAddLeftPane.setLayout(groupLayoutLeft);
		mainAddRightPane.setLayout(groupLayoutRight);
		
		// Creates the Results Panel
		resultsPane = new JPanel();
		resultsPane.setLayout(new GridLayout(0, 2));
		// Shows Results of the search
		resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsList.setLayoutOrientation(JList.VERTICAL);
		resultsList.setVisibleRowCount(-1);
		resultsList.addListSelectionListener(this);
		listScroller = new JScrollPane(resultsList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		resultsPane.add(listScroller);
		
		// Creates the statistics Panel
		statsPane = new JPanel();
		statsPane.setLayout(new FlowLayout());
		statsPane.add(new JLabel("# of Results: 0"));
		
		// Shows Data of the selected Item
		descriptionPane = new JPanel();
		descriptionPane.setLayout(new GridLayout());
		nutrientsPane = new JPanel();
		nutrientsPane.setLayout(new GridLayout(2, 0));
		footnotesPane = new JPanel();
		footnotesPane.setLayout(new GridLayout(10, 0));
		JTabbedPane dataSelectPane = new JTabbedPane();
		dataSelectPane.addTab("Description", descriptionPane);
		dataSelectPane.addTab("Nutrients", nutrientsPane);
		resultsPane.add(dataSelectPane);
		mainPane.add(searchPane, BorderLayout.PAGE_START);
		mainPane.add(resultsPane, BorderLayout.CENTER);
		mainPane.add(statsPane, BorderLayout.PAGE_END);

		this.add(tabPane);
	}
	
	/**
	 * Creates a text area with specified parameters
	 * @param s The text to go in the area
	 * @param c The colour of the background
	 * @return The created text area
	 */
	public JTextArea createText(String s, Color c)
	{
		JTextArea text = new JTextArea(s);
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setBackground(c);

		return text;
	}
	
	/**
	 * Updates the add tab of the GUI
	 */
	public void updateAddTab()
	{
		//Clears the add panels
		mainAddLeftPane.removeAll();
		mainAddRightPane.removeAll();
		
		//Creates the labels for the footnotes and nutrients
		JLabel nutrientLabel = new JLabel("Nutrients (Per 100 grams) *");
		JLabel footnotesLabel = new JLabel("Footnotes");
		JLabel requiredFields = new JLabel("* Indicates Required Fields");
		
		//Creates a panel containing the add and remove buttons for the footnotes
		JPanel footnoteButtons = new JPanel(new FlowLayout(FlowLayout.LEADING));
		footnoteButtons.add(addFootnote);
		footnoteButtons.add(removeFootnote);
		
		//Creates a panel containing the add and remove buttons for the nutrients
		JPanel nutrientButtons = new JPanel(new FlowLayout(FlowLayout.LEADING));
		nutrientButtons.add(addNutrient);
		nutrientButtons.add(removeNutrient);
		
		//Creates the horizontal group of components for the left panel
		groupLayoutLeft.setHorizontalGroup(
				groupLayoutLeft.createSequentialGroup()
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(requiredFields)
				.addComponent(descriptionLabel)
				.addComponent(descriptionField)
				.addComponent(foodgroupLabel)
				.addComponent(footnotesLabel)
				.addComponent(footnotesField)	
				.addComponent(footnotesScroller, 475,475,475)
				.addComponent(footnoteButtons)
				.addComponent(foodGroupBox)
				.addComponent(addButton,200,200,200)));	
		//Creates the vertical group of components for the left panel
		groupLayoutLeft.setVerticalGroup(
				groupLayoutLeft.createSequentialGroup()
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(requiredFields))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(descriptionLabel))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(75)
						.addComponent(descriptionField))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(foodgroupLabel))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(50)
						.addComponent(foodGroupBox))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(50)
						.addComponent(footnotesLabel))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(32)
						.addComponent(footnotesField))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(footnotesScroller, 100, 100, 100))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(250)
						.addComponent(footnoteButtons, 100, 100, 100))
				.addGroup(groupLayoutLeft.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(addButton)));
		
		//Creates the horizontal group of components for the right panel
		groupLayoutRight.setHorizontalGroup(
				groupLayoutRight.createSequentialGroup()
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(manufactLabel)
						.addComponent(manufactField)
						.addComponent(commonLabel)
						.addComponent(commonField)
						.addComponent(nutrientLabel)
						.addComponent(nutrientsBox)
						.addComponent(currentTextField)
						.addComponent(addNutScroller, 475,475,475)
						.addComponent(nutrientButtons)));
		//Creates the vertical group of components for the right panel
		groupLayoutRight.setVerticalGroup(
				groupLayoutRight.createSequentialGroup()
				.addGap(38)
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(manufactLabel))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(75)
						.addComponent(manufactField))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(commonLabel))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(50)
						.addComponent(commonField))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(nutrientLabel))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(nutrientsBox))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(currentTextField))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(25)
						.addComponent(addNutScroller, 100, 100, 100))
				.addGroup(groupLayoutRight.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(nutrientButtons)));
	}
	
	/**
	 * Updates the panel containing the food data
	 * @param index The index of the selected food
	 */
	public void updateDescriptionPane(int index)
	{	
		//Creates all of the labels and text fields for the description panel
		JPanel tempPane = new JPanel(new GridBagLayout());
		JLabel descLabel = new JLabel("Description:");
		JTextArea desc = createText(foods[index].getLongDesc(), Color.WHITE);
		desc.setBorder(BorderFactory.createLoweredBevelBorder());
		JLabel foodGroupLabel = new JLabel("Food Group:");
		JTextArea foodGroup = createText(foods[index].getFoodGroup().getFoodGroupDes(), Color.WHITE);
		foodGroup.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Creates the Common Name field and label
		JLabel commonLabel = new JLabel("Common Name:");
		JTextArea commonName;
		if (!foods[index].getComName().isEmpty())
			commonName = createText(foods[index].getComName(), Color.WHITE);
		else
			commonName = createText("Not Specified", Color.WHITE);
		commonName.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Creates the Scientific Name field and label
		JLabel sciNameLabel = new JLabel("Scientific Name:");
		JTextArea sciName;
		if (!foods[index].getSciName().isEmpty())
			sciName = createText(foods[index].getSciName(), Color.WHITE);
		else
			sciName = createText("Not Specified", Color.WHITE);
		sciName.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Creates the manufacturer field and label
		JLabel manufactLabel = new JLabel("Manufacturer:");
		JTextArea manufact; 
		if (!foods[index].getManufacName().isEmpty())
			manufact = createText(foods[index].getManufacName(), Color.WHITE);
		else
			manufact = createText("Not Specified", Color.WHITE);
		manufact.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Creates the refuse field and label
		JLabel extraDetailsLabel= new JLabel("Inedible Parts:");
		JTextArea extraDetails;
		if (!foods[index].getRefDesc().isEmpty())
			extraDetails = createText(foods[index].getRefDesc(), Color.WHITE);
		else
			extraDetails = createText("Not Specified",Color.WHITE);
		extraDetails.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Displays the description label
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(25,25,0,0);
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		tempPane.add(descLabel,c);
		
		//Displays the description field
		c.insets = new Insets(25,25,0,25);
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 0;
		tempPane.add(desc, c);
		
		//Displays the food group label
		c.insets = new Insets(50,25,0,0);
		c.weightx = .5;
		c.gridx = 0;
		c.gridy = 1;
		tempPane.add(foodGroupLabel, c);
		
		//Displays the food group field
		c.insets = new Insets(50,25,0,25);
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 1;
		tempPane.add(foodGroup, c);
		c.insets = new Insets(50,25,0,0);
		
		//Displays common label
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		tempPane.add(commonLabel, c);
		
		//Displays the common name
		c.insets = new Insets(50,25,0,25);
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 2;
		tempPane.add(commonName, c);
		c.insets = new Insets(50,25,0,0);
		
		//Displays the scientific label
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		tempPane.add(sciNameLabel, c);
		
		//Displays the scientific name
		c.insets = new Insets(50,25,0,25);
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 3;
		tempPane.add(sciName, c);
		c.insets = new Insets(50,25,0,0);
		
		//Displays the manufacturer label
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		tempPane.add(manufactLabel, c);
		
		//Displays the manufacturer
		c.insets = new Insets(50,25,0,25);
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 4;
		tempPane.add(manufact, c);
		c.insets = new Insets(50,25,0,0);
		
		//Displays the refuse label
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		tempPane.add(extraDetailsLabel, c);
		
		//Displays the refuse details
		c.insets = new Insets(50,25,0,25);
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 5;
		tempPane.add(extraDetails, c);
		c.insets = new Insets(50,25,0,0);
		
		descriptionPane.add(tempPane);
		
	}

	/**
	 * Updates the display data
	 * @param index The index of the selected item
	 */
	public void updateDisplayData(int index) {
		//Clears all of the previous data
		descriptionPane.removeAll();
		nutrientsPane.removeAll();
		weightsPane.removeAll();
		
		JPanel footnotesPane = new JPanel(new GridLayout(10, 0));
		
		// Fill up the description pane
		if (index != -1) {
			updateDescriptionPane(index);
			// Acquiring the nutrient data
			SimpleLinkedList<NutrientData> nutrients = foods[index]
					.getNutrientData();
			//Remove zero value nutrients
			Node<NutrientData> tempNutrient = nutrients.getHead();
			while (tempNutrient != null) {

				if ((tempNutrient.getItem().getNutrValue()== 0)) {
					Node<NutrientData> toRemove = tempNutrient;
					tempNutrient = tempNutrient.getNext();
					nutrients.remove(toRemove);
				} else
					tempNutrient = tempNutrient.getNext();
			}
			
			// Getting all of the weights categories
			SimpleLinkedList<Weight> weights = foods[index].getWeights();
			
			//Creates a table for 100g
			Object[][] tempData = new Object[nutrients.getSize()][3];
			Node<NutrientData> tempNode2 = nutrients.getHead();
			int counter = 0;
			JPanel weightTab = new JPanel();
			weightTab.setLayout(new GridLayout());
			while (tempNode2 != null) {
				tempData[counter][0] = tempNode2.getItem()
						.getNutrientDefinition().getNutrDesc();
				tempData[counter][1] = tempNode2.getItem().getNutrValue() +" "+  tempNode2.getItem()
						.getNutrientDefinition().getUnits();
				tempNode2 = tempNode2.getNext();
				counter++;
			}
			JTable tempTable = new JTable(tempData, nutrientColumns);
			JScrollPane tempScroller = new JScrollPane(tempTable);
			tempTable.setPreferredScrollableViewportSize(new Dimension(500,70));
			tempTable.setEnabled(false);
			weightTab.add(tempScroller);
			weightsPane.addTab("100g", weightTab);
			
			//Creates tables for all extra weight categories
			if (weights != null) {
				Node<Weight> tempWeight = weights.getHead();
				while (tempWeight != null) {

					weightTab = new JPanel();
					weightTab.setLayout(new GridLayout());
					tempData = new Object[nutrients.getSize()][3];
					 tempNutrient = nutrients.getHead();
					counter = 0;

					while (tempNutrient != null) {
						int numOfDec = getNumDec(tempNutrient.getItem().getNutrNo());
						
						tempData[counter][0] = tempNutrient.getItem()
								.getNutrientDefinition().getNutrDesc();
						tempData[counter][1] = (Math.round(((tempNutrient.getItem()
								.getNutrValue() * tempWeight.getItem()
								.getGmWeight())
								/ 100)*Math.pow(10, numOfDec)))/Math.pow(10, numOfDec)
								+" "+ tempNutrient.getItem()
										.getNutrientDefinition().getUnits();
						tempNutrient = tempNutrient.getNext();
						counter++;
					}
					tempTable = new JTable(tempData, nutrientColumns);
					tempTable.setEnabled(false);
					tempScroller = new JScrollPane(tempTable);
					tempTable.setPreferredScrollableViewportSize(new Dimension(
							500, 70));
					weightTab.add(tempScroller);
					weightsPane.addTab(tempWeight.getItem().getMsreDesc(),
							weightTab);
					tempWeight = tempWeight.getNext();
				}
			}
				nutrientsPane.add(weightsPane);
			footnotesPane.add(new JLabel("Footnotes:"));

			//Creates all of the footnote components
			SimpleLinkedList<FootNote> footNotes = foods[index].getFootNotes();
			if (footNotes != null) {
				Node<FootNote> tempNode = footNotes.getHead();
				while (tempNode != null) {
					footnotesPane.add(createText(tempNode.getItem().getFootNotetxt(), new Color(0,0,0,0)));
					tempNode = tempNode.getNext();
				}
			} 
			else
				footnotesPane.add(new JLabel("None Specified"));
			nutrientsPane.add(footnotesPane);
		}

		repaint();
	}
	
	/**
	 * Gets the number of a nutrient based on its description
	 * @param desc The description of the nutrient
	 * @return The nutrient number
	 */
	public int getNutrNumber(String desc){
		//Cycles through the nutrients looking for a matching description
		Node<NutrientDefinition> tempDefNode = FoodDatabase.nutrientDef.getHead();
		for (int i = 0; i < nutrientsDesc.length; i++) {
			if (tempDefNode.getItem().getNutrDesc() == desc)
				return tempDefNode.getItem().getNutrNo();
			tempDefNode = tempDefNode.getNext();
		}
		return -1;
	}
	
	/**
	 * Gets the number of decimals required to round the nutrient to
	 * @param nutrNo The number of the nutrient
	 * @return The number of decimals
	 */
	public int getNumDec(int nutrNo){
		//Cycles through the nutrients looking for the matching nutrient
		Node<NutrientDefinition> tempDefNode = FoodDatabase.nutrientDef.getHead();
		for (int i = 0; i < nutrientsDesc.length; i++) {
			if (tempDefNode.getItem().getNutrNo() == nutrNo)
				return tempDefNode.getItem().getNumDec();
			tempDefNode = tempDefNode.getNext();
		}
		return -1;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		//Checks to see if a search should be made
		if (source == searchButton || source == searchField) {
			//Clears all of the results
			resultsModel.clear();

			//Preforms the search
			foods = Main.database.search(searchField.getText(),
					searchQueryState, searchOptionsState);

			//Updates the number of results
			statsPane.removeAll();
			statsPane.add(new JLabel("# of Results: " + foods.length));

			//Adds all of the search results to the Jlist
			for (Food f : foods)
				resultsModel.addElement(f.getLongDesc());
			resultsList = new JList(resultsModel);
		}
		//Checks to see if a food should be added
		else if (source == addButton){
			if (!descriptionField.getText().isEmpty() && addNutTable.getRowCount() > 0)
			{
			//Creates the NDB number
			String ndbNum = FoodDatabase.nextFreeNdb();
			System.out.println(ndbNum);
			
			//Creates the description String
			String foodDesc;
			//Creates the short description
			String shortDesc;
			if (descriptionField.getText().length() > 60)
				shortDesc = descriptionField.getText().substring(0, 61).toUpperCase();
			else
				shortDesc = descriptionField.getText().toUpperCase();
			
			//Creates the first part of the food description string
			if (foodGroupNums[foodGroupBox.getSelectedIndex()] < 1000){
				foodDesc = new String("~"+ndbNum +"~^~0"+foodGroupNums[foodGroupBox.getSelectedIndex()]+"~^~");
			}
			else{
				foodDesc = new String("~"+ndbNum +"~^~"+foodGroupNums[foodGroupBox.getSelectedIndex()]+"~^~");
			}
			//Creates the second part of the food description string
			if (descriptionField.getText().length() > 200)
				foodDesc += descriptionField.getText().substring(0, 200)+"~^~"+shortDesc+"~^~~^~" + manufactField.getText() + "~^~~^~~^0^~~^^^^";
			else
				foodDesc += descriptionField.getText()+"~^~"+shortDesc+"~^~~^~" + manufactField.getText() + "~^~~^~~^0^~~^^^^";
			
			//Creates the array of nutrient strings
			String[] nutrientsArray = new String[addNutTable.getRowCount()];
			for (int i = 0; i < addNutTable.getRowCount(); i++){
					nutrientsArray[i] = "~"+ndbNum+"~^~"+getNutrNumber((String)nutrientAddData[i][0])+"~^"+nutrientAddData[i][1]+"^1^^~4~^~~^~~^~~^^^^^^^~~^11/2015^";
			}
			
			//Creates the array of all entered footnotes
			String[] footnotesArray = new String[footnotesModel.size()];
			for (int i = 0; i < footnotesModel.size(); i++)
				footnotesArray[i] = "~"+ndbNum +"~^~"+"0"+(i+1)+"~^~D~^~~^~"+footnotesModel.getElementAt(i)+"~";
			
			//Adds the food with the required parameters
			try {
				Main.database.add(foodDesc, footnotesArray, nutrientsArray);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//Resets all of the fields to default
			descriptionField.setText("");
			manufactField.setText("");
			commonField.setText("");
			footnotesField.setText("");
			nutrientsBox.setSelectedItem(nutrientsBox.getItemAt(0));
			foodGroupBox.setSelectedItem(foodGroupBox.getItemAt(0));
			
			//Resets the nutrient table
			nutrientAddData = new Object[0][2];
			addNutTable = new JTable(nutrientAddData, nutrientAddColumns);
			addNutScroller = new JScrollPane(addNutTable);
			
			//Resets the footnotes list
			footnotesModel.clear();
			footnotesList = new JList(footnotesModel);
			footnotesList.setBorder(BorderFactory.createLoweredBevelBorder());
			footnotesScroller = new JScrollPane(footnotesList);
			
			currentTextField.setText("0");;

			updateAddTab();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "You must fill out all required fields!");
			}
		}
		else if (source == addNutrient){
			//Checks to see if the entered value is valid
			try{
				if (currentTextField.getText().isEmpty() || Double.parseDouble(currentTextField.getText()) <= 0)
					return;
			}
			catch (Exception NumberFormatException){
				JOptionPane.showMessageDialog(this, "The value contains invalid characters!");
				return;
			}
			//Checks to see if the nutrient was already added
			for (int i = 0; i < addNutTable.getRowCount(); i++)
			{
				if (nutrientsDesc[nutrientsBox.getSelectedIndex()] == nutrientAddData[i][0])
				{
					nutrientAddData[i][1] = Double.parseDouble(nutrientAddData[i][1].toString()) + Double.parseDouble(currentTextField.getText());
					
					addNutTable = new JTable(nutrientAddData, nutrientAddColumns);
					addNutScroller = new JScrollPane(addNutTable);
					
					updateAddTab();
					
					return;
				}
			}
			//Recreates the table data array
			Object[][] tempAddData = nutrientAddData;
			nutrientAddData = new Object[addNutTable.getRowCount()+1][2];
			for (int i = 0; i < addNutTable.getRowCount(); i++)
			{
				nutrientAddData[i][0] = tempAddData[i][0];
				nutrientAddData[i][1] = tempAddData[i][1];
			}
			
			//Adds the new entry to the table data array
			nutrientAddData[nutrientAddData.length-1][0] = nutrientsDesc[nutrientsBox.getSelectedIndex()];
			nutrientAddData[nutrientAddData.length-1][1] = currentTextField.getText();
			
			//Makes the Table non editable
			addNutTable = new JTable(nutrientAddData, nutrientAddColumns){  
				  public boolean isCellEditable(int row, int column){  
					    return false;  
					  } };
			addNutScroller = new JScrollPane(addNutTable);
			
			//Updates the GUI
			updateAddTab();
		}
		//Removes an added nutrient
		else if (source == removeNutrient)
		{
			//Finds the selected nutrient and removes it
			if (addNutTable.getRowCount() != 0 && addNutTable.getSelectedRow() != -1){
				Object[][] tempAddData = nutrientAddData;
				nutrientAddData = new Object[addNutTable.getRowCount()-1][2];
				
				for (int i = 0; i < addNutTable.getSelectedRow(); i++)
				{
					nutrientAddData[i][0] = tempAddData[i][0];
					nutrientAddData[i][1] = tempAddData[i][1];
				}
				
				for (int i = addNutTable.getSelectedRow(); i < addNutTable.getRowCount()-1; i++)
				{
					nutrientAddData[i][0] = tempAddData[i+1][0];
					nutrientAddData[i][1] = tempAddData[i+1][1];
				}
				
				//Makes the table non editable
				addNutTable = new JTable(nutrientAddData, nutrientAddColumns){  
					  public boolean isCellEditable(int row, int column){  
						    return false;  
						  } };
				addNutScroller = new JScrollPane(addNutTable);
				
				updateAddTab();
			}
		}
		//Adds a footnote
		else if (source == addFootnote){
			if (!footnotesField.getText().isEmpty()){
				footnotesModel.addElement(footnotesField.getText());
				footnotesList = new JList(footnotesModel);
				footnotesList.setBorder(BorderFactory.createLoweredBevelBorder());
				footnotesScroller = new JScrollPane(footnotesList);
				updateAddTab();
			}
			
		}
		//Removes a footnote
		else if (source == removeFootnote){
			if (footnotesList.getSelectedIndex() != -1)
			{
				footnotesModel.remove(footnotesList.getSelectedIndex());
				footnotesList = new JList(footnotesModel);
				footnotesList.setBorder(BorderFactory.createLoweredBevelBorder());
				
				updateAddTab();
			}
		}
		repaint();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		//Updates the state of the search query
		Object source = e.getItemSelectable();
		int index = -1;
		for (int i = 0; i < 25; i++)
		{
			if (source == searchQuery[i])
			{
				index = i;
				break;
			}
		}
		
		if (index != -1)
		{		
			if (index == 0) {
				if (searchQuery[0].isSelected()) {
					for (int i = 1; i < searchQuery.length; i++) {
						searchQuery[i].setSelected(true);
						searchQueryState[i - 1] = true;
					}
				} else {
					for (int i = 1; i < searchQuery.length; i++) {
						searchQuery[i].setSelected(false);
						searchQueryState[i - 1] = false;
					}
				}
			} 
			else
			{
				searchQueryState[index-1] = !searchQueryState[index-1];
			}
			
		}
		else
		{
			for (int i = 0; i < 4; i++)
			{
				if (source == searchOptions[i])
					searchOptionsState[i] = !searchOptionsState[i];
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		//Updates the displayed Data whenever a new food is selected
		if (e.getValueIsAdjusting() == false) {
			updateDisplayData(((JList) e.getSource()).getSelectedIndex());
		}
	}
}