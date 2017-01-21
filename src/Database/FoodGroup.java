package Database;

/**
 * FoodGroup object
 * Holds data and references related to a food group
 * @author Eric Chee
 * @version 11/22/2015
 */
public class FoodGroup implements Comparable<FoodGroup>
{
	private int foodGroupNo;
	private String foodGroupDes;
	
	/**
	 * creates food group object
	 * @param items
	 */
	public FoodGroup(String[] items)
	{
		this.foodGroupNo=Integer.parseInt(items[0]);
		this.foodGroupDes=items[1];
	}
	
	@Override
	public int compareTo(FoodGroup e)
	{
		return (this.foodGroupNo-e.getFoodGroupNo());
	}
	
	/**
	 * @return the foodGroup
	 */
	public int getFoodGroupNo()
	{
		return foodGroupNo;
	}
	/**
	 * @return the foodGroupDes
	 */
	public String getFoodGroupDes()
	{
		return foodGroupDes;
	}
}
