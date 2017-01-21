package Database;

/**
 * Nutrient object
 * Holds data and references related to a nutrient
 * @author Eric Chee
 * @version 11/22/2015
 */
public class NutrientData implements Comparable<NutrientData>
{
	private int ndbNo, nutrNo;
	private double nutrValue ;

	private NutrientDefinition nutrientDefinition;// Point to corresponding
													// nutrient definition

	/**
	 * Creates nutrient data object
	 * @param items
	 */
	public NutrientData(String[] items)
	{
		this.ndbNo = Integer.parseInt(items[0]);
		this.nutrNo = Integer.parseInt(items[1]);
		this.nutrValue = Double.parseDouble(items[2]);
}
	@Override
	public int compareTo(NutrientData v)
	{
		return this.nutrNo - v.getNutrNo();
	}

	/**
	 * @return the ndbNo
	 */
	public int getNdbNo() {
		return ndbNo;
	}
	/**
	 * @param ndbNo the ndbNo to set
	 */
	public void setNdbNo(int ndbNo) {
		this.ndbNo = ndbNo;
	}
	/**
	 * @return the nutrNo
	 */
	public int getNutrNo() {
		return nutrNo;
	}
	/**
	 * @return the nutrValue
	 */
	public double getNutrValue() {
		return nutrValue;
	}
	/**
	 * @return the nutrientDefinition
	 */
	public NutrientDefinition getNutrientDefinition() {
		return nutrientDefinition;
	}
	/**
	 * @param nutrientDefinition the nutrientDefinition to set
	 */
	public void setNutrientDefinition(NutrientDefinition nutrientDefinition) {
		this.nutrientDefinition = nutrientDefinition;
	}
}
