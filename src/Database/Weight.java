package Database;

/**
 * Weight object
 * Holds data and references related to a weight
 * @author Eric Chee
 * @version 11/22/2015
 */
public class Weight implements Comparable<Weight>
{
	private int ndbNo, seq, key;
	private double ammount, gmWeight;
	private String msreDesc;
	/**
	 * Creates weight object
	 * @param items data to put in object
	 */
	public Weight(String[] items)
	{
		this.ndbNo=Integer.parseInt(items[0]);
		this.seq=Integer.parseInt(items[1]);
		this.key=ndbNo*100+seq;
		this.ammount=Double.parseDouble(items[2]);
		this.msreDesc=items[3];
		this.gmWeight=Double.parseDouble(items[4]);

	}
	@Override
	public int compareTo(Weight w)
	{
		return this.getKey()-w.getKey();
	}
	
	/**
	 * @return the ndbNo
	 */
	public int getNdbNo()
	{
		return ndbNo;
	}
	/**
	 * @return the seq
	 */
	public int getSeq()
	{
		return seq;
	}
	/**
	 * @return the key
	 */
	public int getKey()
	{
		return key;
	}
	/**
	 * @return the ammount
	 */
	public double getAmmount()
	{
		return ammount;
	}
	/**
	 * @return the gmWeight
	 */
	public double getGmWeight()
	{
		return gmWeight;
	}
	/**
	 * @return the msreDesc
	 */
	public String getMsreDesc()
	{
		return msreDesc;
	}
}
