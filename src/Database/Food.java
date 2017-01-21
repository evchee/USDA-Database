package Database;

/**
 * Food Object
 * Holds data and references related to a food
 * @author Eric Chee
 * @version 11/22/2015
 */
public class Food implements Comparable<Food>
{
	//Data fields
	private int ndbNo, foodGroupNo;
	private String longDesc, shortDesc, comName, manufacName, refDesc, sciName, survey,refuse;
	private FoodGroup foodGroup;// Point to corresponding food group
	private SimpleLinkedList<FootNote> footNotes;// Point to corresponding footnote
	private SimpleLinkedList<Weight> weights;// Point to corresponding weight
	private SimpleLinkedList<NutrientData> nutrientData;

	/**
	 * Creates Food Object
	 * @param items values for the data fields
	 */
	public Food(String[] items)
	{
		this.ndbNo = Integer.parseInt(items[0]);
		this.foodGroupNo = Integer.parseInt(items[1]);
		this.longDesc = items[2];
		this.shortDesc = items[3];
		this.comName = items[4];
		this.manufacName = items[5];
		this.survey=(items[6]);
		this.refDesc = items[7];
		this.refuse = items[8];
		this.sciName = items[9];
	
	}
	@Override
	public int compareTo(Food v)
	{
		return this.ndbNo - v.getNDB();
	}

	/**
	 * @return the ndbNo
	 */
	public int getNDB() {
		return ndbNo;
	}
	/**
	 * @param ndbNo the ndbNo to set
	 */
	public void getNDB(int ndbNo) {
		this.ndbNo = ndbNo;
	}
	/**
	 * @return the foodGroupNo
	 */
	public int getFoodGroupNo() {
		return foodGroupNo;
	}
	/**
	 * @param foodGroupNo the foodGroupNo to set
	 */
	public void setFoodGroupNo(int foodGroupNo) {
		this.foodGroupNo = foodGroupNo;
	}
	/**
	 * @return the longDesc
	 */
	public String getLongDesc() {
		return longDesc;
	}

	/**
	 * @return the shortDesc
	 */
	public String getShortDesc() {
		return shortDesc;
	}
	/**
	 * @return the comName
	 */
	public String getComName() {
		return comName;
	}
	/**
	 * @return the manufacName
	 */
	public String getManufacName() {
		return manufacName;
	}
	/**
	 * @return the survey
	 */
	public String getSurvey() {
		return survey;
	}
	/**
	 * @return the refDesc
	 */
	public String getRefDesc() {
		return refDesc;
	}
	/**
	 * @return the refuse
	 */
	public String getRefuse() {
		return refuse;
	}
	/**
	 * @return the sciName
	 */
	public String getSciName() {
		return sciName;
	}
	/**
	 * @param sciName the sciName to set
	 */
	public void setSciName(String sciName) {
		this.sciName = sciName;
	}
	/**
	 * @return the foodGroup
	 */
	public FoodGroup getFoodGroup() {
		return foodGroup;
	}
	/**
	 * @param foodGroup the foodGroup to set
	 */
	public void setFoodGroup(FoodGroup foodGroup) {
		this.foodGroup = foodGroup;
	}
	/**
	 * @return the footNotes
	 */
	public SimpleLinkedList<FootNote> getFootNotes() {
		return footNotes;
	}
	/**
	 * @param footNotes the footNotes to set
	 */
	public void setFootNotes(SimpleLinkedList<FootNote> footNotes) {
		this.footNotes = footNotes;
	}
	/**
	 * @return the weights
	 */
	public SimpleLinkedList<Weight> getWeights() {
		return weights;
	}
	/**
	 * @param weights the weights to set
	 */
	public void setWeights(SimpleLinkedList<Weight> weights) {
		this.weights = weights;
	}
	/**
	 * @return the nutrientData
	 */
	public SimpleLinkedList<NutrientData> getNutrientData() {
		return nutrientData;
	}
	/**
	 * @param nutrientData the nutrientData to set
	 */
	public void setNutrientData(SimpleLinkedList<NutrientData> nutrientData) {
		this.nutrientData = nutrientData;
	}
}
