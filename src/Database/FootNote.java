package Database;

/**
 * FootNote object
 * Holds data and references related to a footnote
 * @author Eric Chee
 * @version 11/22/2015
 */
public class FootNote implements Comparable<FootNote>
{
	private int ndbNo, footNoteNo,nutrNo;
	private String FootNotetxt;
	private int key;
	
	/**
	 * Creates Footnote object
	 * @param items string with object data
	 */
	public FootNote(String[] items)

	{
		this.ndbNo=Integer.parseInt(items[0]);
		this.footNoteNo=Integer.parseInt(items[1]);
		if(items[3].length()>0)
		this.nutrNo=Integer.parseInt(items[3]);
		else
			this.nutrNo=0;
		this.FootNotetxt=items[4];
		this.key=(this.ndbNo*10000+this.footNoteNo+nutrNo*10);
	}
	
	@Override
	public int compareTo(FootNote v)
	{
		return (this.key)-(v.getKey());

	}

	/**
	 * @return the ndbNo
	 */
	public int getNdbNo()
	{
		return ndbNo;
	}
	/**
	 * @return the footNoteNo
	 */
	public int getFootNoteNo()
	{
		return footNoteNo;
	}
	/**
	 * @return the nutrNo
	 */
	public int getNutrNo()
	{
		return nutrNo;
	}
	/**
	 * @return the footNotetxt
	 */
	public String getFootNotetxt()
	{
		return FootNotetxt;
	}
	/**
	 * @return the sort key
	 */
	public int getKey()
	{
		return key;
	}
}
