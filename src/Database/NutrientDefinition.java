package Database;

/**
 * Nutrient Definition object
 * Holds data and references related to a nutrient definition
 * @author Eric Chee
 * @version 11/22/2015
 */
public class NutrientDefinition implements Comparable<NutrientDefinition>
{
	private int nutrNo, numDec;
	private String units, nutrDesc;
	/**
	 * Creates nutrient data object
	 * @param items data to place in object
	 */
	public NutrientDefinition(String[] items)
	{
		this.nutrNo=Integer.parseInt(items[0]);
		this.units=items[1];
		this.nutrDesc=items[3];
		this.numDec= Integer.parseInt(items[4]);
	}
	@Override
	public int compareTo(NutrientDefinition nutDef)
	{
		return this.nutrNo-nutDef.getNutrNo();
	}
	/**
	 * @return the nutrNo
	 */
	public int getNutrNo() {
		return nutrNo;
	}
	/**
	 * @param nutrNo the nutrNo to set
	 */
	public void setNutrNo(int nutrNo) {
		this.nutrNo = nutrNo;
	}
	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(String units) {
		this.units = units;
	}
	/**
	 * @return the nutrDesc
	 */
	public String getNutrDesc() {
		return nutrDesc;
	}
	/**
	 * @return the numDec
	 */
	public int getNumDec() {
		return numDec;
	}
}
