package Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Food Database object
 * Contains methods, data and references for a database object
 * @author Eric Chee
 * @verion 11/22/2015
 */
public class FoodDatabase
{
	// Support data
	SimpleLinkedList<FoodGroup> foodGroups = new SimpleLinkedList<FoodGroup>();
	BalancedBinaryTree<NutrientDefinition> nutrientDefinitions = new BalancedBinaryTree<NutrientDefinition>();
	static SimpleLinkedList<NutrientDefinition> nutrientDef = new SimpleLinkedList<NutrientDefinition>();
	BalancedBinaryTree<Food> foods = new BalancedBinaryTree<Food>();
	int[] foodGroupNumbers;
	private static int highestNDB = 0;

	/**
	 * Creates database
	 */
	public FoodDatabase()
	{
		try
		{
			FoodDatabase.importData(foods, foodGroups, nutrientDefinitions);
			foodGroupNumbers = new int[foodGroups.getSize()];
			Node<FoodGroup> tempNode = foodGroups.getHead();
			for (int i = 0; i < foodGroupNumbers.length; i++)
			{
				foodGroupNumbers[i] = tempNode.getItem().getFoodGroupNo();
				tempNode = tempNode.getNext();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Read in all relational data info database
	 * 
	 * @param foods Tree of food items
	 * @param foodGroups Tree of food groups
	 * @param nutrientDefinitions Tree of Nutrient definitions
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void importData(BalancedBinaryTree<Food> foods,
			SimpleLinkedList<FoodGroup> foodGroups,
			BalancedBinaryTree<NutrientDefinition> nutrientDefinitions)
			throws IOException
	{
		// Temp trees to be placed into food object
		SimpleLinkedList<NutrientData> nutrientDataTemp = new SimpleLinkedList<NutrientData>();
		SimpleLinkedList<FootNote> footNotesTemp = new SimpleLinkedList<FootNote>();
		SimpleLinkedList<Weight> weightsTemp = new SimpleLinkedList<Weight>();

		// Read in food groups
		BufferedReader file = new BufferedReader(new FileReader("FD_GROUP.txt"));
		String tempStr;
		String[] items;
		// Add each entry into linked list;
		while ((tempStr = file.readLine()) != null)
		{
			items = split(tempStr, 2);
			foodGroups.add(new FoodGroup(items));
		}

		// Read in nutrient definitions
		file = new BufferedReader(new FileReader("NUTR_DEF.txt"));
		// Add each entry into linked list;
		while ((tempStr = file.readLine()) != null)
		{
			items = split(tempStr, 6);
			nutrientDef.add(new NutrientDefinition(items));
			nutrientDefinitions.insert(new NutrientDefinition(items));
		}

		// Read in food data
		file = new BufferedReader(new FileReader("FOOD_DES.txt"));
		// Add each entry into linked list;
		while ((tempStr = file.readLine()) != null)
		{
			items = split(tempStr, 14);
			Food tempFood = new Food(items);
			// Reference appropriate food group for each food
			tempFood.setFoodGroup(findFoodGroup(tempFood.getFoodGroupNo(),
					foodGroups.getHead()));
			foods.insert(tempFood);

			if (tempFood.getNDB() > highestNDB)
				highestNDB = tempFood.getNDB();

		}

		// Temporary variables for adding lists into food objects
		Food tempFood;
		int currentNDB = 0;

		// Read in footnotes
		file = new BufferedReader(new FileReader("FOOTNOTE.txt"));
		FootNote tempFootData;
		// Add each entry into linked list;
		while ((tempStr = file.readLine()) != null)
		{
			items = split(tempStr, 5);
			tempFootData = new FootNote(items);
			// If first entry
			if (currentNDB == 0)
				currentNDB = tempFootData.getNdbNo();
			// If footnote for the same food
			if (tempFootData.getNdbNo() == currentNDB)
			{
				footNotesTemp.add(tempFootData);
			}
			// Places list of footnote into correct object
			else
			{
				tempFood = findFood(currentNDB, foods.getHead());
				tempFood.setFootNotes(footNotesTemp);
				// Reset list
				footNotesTemp = new SimpleLinkedList<FootNote>();
				currentNDB = Integer.parseInt(items[0]);
				footNotesTemp.add(tempFootData);

			}

		}
		tempFood = findFood(currentNDB, foods.getHead());
		tempFood.setFootNotes(footNotesTemp);

		// Read in weight
		file = new BufferedReader(new FileReader("WEIGHT.txt"));
		currentNDB = 0;
		Weight tempWeightData;
		// Add each entry into linked list;
		while ((tempStr = file.readLine()) != null)
		{
			items = split(tempStr, 7);
			tempWeightData = new Weight(items);
			// If first entry
			if (currentNDB == 0)
				currentNDB = tempWeightData.getNdbNo();
			// If part of same food
			if (tempWeightData.getNdbNo() == currentNDB)
			{
				weightsTemp.add(tempWeightData);
			}
			// Places list of weight data into correct object
			else
			{
				tempFood = findFood(currentNDB, foods.getHead());
				tempFood.setWeights(weightsTemp);
				// reset list
				weightsTemp = new SimpleLinkedList<Weight>();
				currentNDB = Integer.parseInt(items[0]);
				weightsTemp.add(tempWeightData);

			}

		}
		tempFood = findFood(currentNDB, foods.getHead());
		tempFood.setWeights(weightsTemp);

		// Read in nutrient data
		file = new BufferedReader(new FileReader("NUT_DATA.txt"));
		currentNDB = 0;
		NutrientData tempNutData;
		// Add each entry into linked list;
		while ((tempStr = file.readLine()) != null)
		{
			items = split(tempStr, 18);
			tempNutData = new NutrientData(items);
			tempNutData.setNutrientDefinition(findDefinition(
					tempNutData.getNutrNo(), nutrientDefinitions.getHead()));
			// If first entry
			if (currentNDB == 0)
				currentNDB = tempNutData.getNdbNo();
			// If part of same food
			if (tempNutData.getNdbNo() == currentNDB)
			{
				nutrientDataTemp.add(tempNutData);
			}
			// Places list of nutrient data into correct object
			else
			{
				tempFood = findFood(currentNDB, foods.getHead());
				tempFood.setNutrientData(nutrientDataTemp);
				// Rest list
				nutrientDataTemp = new SimpleLinkedList<NutrientData>();
				currentNDB = Integer.parseInt(items[0]);
				nutrientDataTemp.add(tempNutData);

			}

		}
		tempFood = findFood(currentNDB, foods.getHead());
		tempFood.setNutrientData(nutrientDataTemp);

		file.close();
	}

	/**
	 * Adds food
	 * 
	 * @param foodDescription food description
	 * @param footnotes footnotes
	 * @param nutrientData nutrient data
	 * @param weights weights
	 * @return new database with food added
	 * @throws IOException
	 */
	public void add(String foodDescription, String[] footnotes,
			String[] nutrientData) throws IOException
	{
		// Writes food description
		BufferedWriter output = new BufferedWriter(new FileWriter(
				"FOOD_DES.txt", true));
		output.append(foodDescription);
		output.newLine();
		String[] items = split(foodDescription, 14);
		Food tempFood = new Food(items);
		// Reference appropriate food group for each food
		tempFood.setFoodGroup(findFoodGroup(tempFood.getFoodGroupNo(),
				foodGroups.getHead()));
		foods.insert(tempFood);
		output.close();

		// Writes buffered nutrient data
		output = new BufferedWriter(new FileWriter("NUT_DATA.txt", true));
		SimpleLinkedList<NutrientData> nutrientDataTemp = new SimpleLinkedList<NutrientData>();
		for (int i = 0; i < nutrientData.length; i++)
		{
			if (nutrientData[i].length() > 0)
			{
				output.append(nutrientData[i]);
				output.newLine();
				NutrientData tempNutData;
				// Add each entry into linked list;
				items = split(nutrientData[i], 18);
				tempNutData = new NutrientData(items);
				tempNutData
						.setNutrientDefinition(findDefinition(
								tempNutData.getNutrNo(),
								nutrientDefinitions.getHead()));
				nutrientDataTemp.add(tempNutData);
			}
		}
		tempFood.setNutrientData(nutrientDataTemp);

		output.close();
		// Writes footnotes
		output = new BufferedWriter(new FileWriter("FOOTNOTE.txt", true));
		SimpleLinkedList<FootNote> footNotesTemp = new SimpleLinkedList<FootNote>();
		for (int i = 0; i < footnotes.length; i++)
		{
			if (footnotes[i].length() > 0)
			{
				output.append(footnotes[i]);
				output.newLine();
				FootNote tempFootData;
				// Add each entry into linked list;
				items = split(footnotes[i], 5);
				tempFootData = new FootNote(items);
				footNotesTemp.add(tempFootData);
			}
		}
		tempFood.setFootNotes(footNotesTemp);
		output.close();
	}

	/**
	 * @return food groups as an array
	 */
	public int[] getFoodGroupArray()
	{
		return foodGroupNumbers;
	}

	/**
	 * @return tree of foods
	 */
	public BalancedBinaryTree<Food> getFoods()
	{
		return foods;
	}

	/**
	 * @return food groups as a linked list
	 */
	public SimpleLinkedList<FoodGroup> getFoodGroups()
	{
		return foodGroups;
	}

	/**
	 * Searches database by string and food groups
	 * 
	 * @param query string query to search
	 * @param foodGroups boolean array corresponding to food groups
	 * @return array of matching foods
	 */
	public Food[] search(String query, boolean[] foodGroups,
			boolean[] searchfields)
	{
		PriorityQueue<Food> matchingFoods = new PriorityQueue<Food>();
		// splits search into individual words
		String[] splitQuery = query.toLowerCase().replaceAll("[^a-zA-Z ]", "")
				.replaceAll("^[,\\s]+", "").split("[,\\s]+");
		internalSearch(splitQuery, foods.getHead(), matchingFoods, foodGroups,
				searchfields);
		Node<Food> tempNode = matchingFoods.getHead();
		// Writes to an array
		Food[] matches = new Food[matchingFoods.getSize()];
		tempNode = matchingFoods.getHead();
		for (int i = 0; i < matchingFoods.getSize(); i++)
		{
			matches[i] = tempNode.getItem();
			tempNode = (tempNode.getNext());
		}
		return matches;

	}

	/**
	 * Checks if one of active food groups
	 * 
	 * @param groupNumber food group number to check
	 * @param foodGroups active food groups
	 * @return if one of active food groups
	 */
	private boolean matchesFoodGroup(int groupNumber, boolean[] foodGroups)
	{
		// if active check if that food group
		for (int i = 0; i < foodGroups.length; i++)
		{
			if (foodGroups[i])
				if (foodGroupNumbers[i] == groupNumber)
					return true;
		}
		return false;
	}

	/**
	 * Internal search method
	 * 
	 * @param query string to search for
	 * @param start current node
	 * @return tree of foods
	 */
	private void internalSearch(String[] query, BinaryTreeNode<Food> start,
			PriorityQueue<Food> list, boolean[] foodGroups,
			boolean[] searchfields)
	{

		{
			// if food is not null
			if (start != null)
			{
				int noMatches = 0;
				for (int i = 0; i < query.length; i++)
				{
					// 1 point if part of long description
					if (searchfields[0])
						if (start.getItem().getLongDesc().toLowerCase()
								.contains(query[i]))
						{
							noMatches++;
						}
					// 1 point if part of manufacturer's name
					if (searchfields[1])
						if (start.getItem().getManufacName().toLowerCase()
								.contains(query[i]))
						{
							noMatches++;
						}
					// 2 points if part of scientific name
					if (searchfields[2])

						if (start.getItem().getSciName().toLowerCase()
								.contains(query[i]))
						{
							noMatches += 2;
						}
					// 3 points if common name
					if (searchfields[3])

						if (start.getItem().getComName().toLowerCase()
								.contains(query[i]))
						{
							noMatches += 3;
						}
				}
				// if an active food group and matches search query put in
				// priority queue
				if (noMatches > 0)
					if (matchesFoodGroup(start.getItem().getFoodGroup()
							.getFoodGroupNo(), foodGroups))
						list.enqueue(start.getItem(), noMatches);
				// Do for left and right child
				internalSearch(query, start.getLeftChild(), list, foodGroups,
						searchfields);
				internalSearch(query, start.getRightChild(), list, foodGroups,
						searchfields);
			}

		}

	}

	/**
	 * Finds food group based on food group number
	 * @param foodGroupNo food group number to search by
	 * @param start current node
	 * @return food group object
	 */
	private static FoodGroup findFoodGroup(int foodGroupNo,
			Node<FoodGroup> start)
	{
		// goes through each node in food group list
		if (start == null)
			return null;
		while (start.getItem().getFoodGroupNo() != foodGroupNo)
		{
			start = start.getNext();
		}
		return start.getItem();
	}

	/**
	 * Find corresponding food by ndbNo
	 * @param ndbNo ndb Number
	 * @param start current node
	 * @return Correct food
	 */
	private static Food findFood(int ndbNo, BinaryTreeNode<Food> start)
	{
		// goes through each node in food tree
		if (start == null)
			return null;
		if (ndbNo > start.getItem().getNDB())
		{
			return findFood(ndbNo, start.getRightChild());
		}
		else if (ndbNo < start.getItem().getNDB())
		{
			return findFood(ndbNo, start.getLeftChild());
		}
		return start.getItem();
	}

	/**
	 * Finds correct nutrient definition
	 * @param nutNo nutrient number
	 * @param start current node
	 * @return nutrient definition
	 */
	private static NutrientDefinition findDefinition(int nutNo,
			BinaryTreeNode<NutrientDefinition> start)
	{
		// goes through each node in nutrient definition tree
		if (start == null)
			return null;
		if (nutNo > start.getItem().getNutrNo())
		{
			return findDefinition(nutNo, start.getRightChild());
		}
		else if (nutNo < start.getItem().getNutrNo())
		{
			return findDefinition(nutNo, start.getLeftChild());
		}
		return start.getItem();
	}

	/**
	 * Returns next ndb number for add
	 * @return Next ndb number
	 */
	public static String nextFreeNdb()
	{
		String nextNDB = Integer.toString(highestNDB + 1);
		// makes it correct number of digits
		while (nextNDB.length() < 5)
			nextNDB = "0" + nextNDB;
		return nextNDB;
	}

	/**
	 * Split line into data pieces
	 * @param str string to be split
	 * @param noItems no items to split into
	 * @return string with items
	 */
	private static String[] split(String str, int noItems)
	{
		str = str + "^";
		String[] items = new String[noItems];
		int pos = 0;
		int end = 0;
		// for each character
		for (int i = 0; i < str.length(); i++)
		{
			// if delimiter break string
			if (str.charAt(i) == '^')
			{
				items[pos] = str.substring(end, i);
				if (items[pos].length() > 0)
				{// remove ~
					if (items[pos].charAt(0) == '~')
					{
						if (items[pos].length() > 2)
							items[pos] = items[pos].substring(1,
									items[pos].length() - 1);
						else
							// in case null
							items[pos] = "";
					}
				}
				else
					items[pos] = "";

				end = (i + 1);
				pos++;
			}
		}
		return items;
	}
}
