package Database;

/**
 * Binary Tree Node 
 * Various methods and behaviors for a node
 * @author Eric Chee
 * @version 11/22/2015
 * @param <E>
 */
public class Node<E> implements Comparable<Node<E>>
{
	private E item;
	private Node<E> next;
	private int priority;

	/**
	 * creates new unweighed node
	 * @param item item to store in data
	 */
	public Node(E item)
	{
		this.item = item;
	}
	
	/**
	 * Creates weighted node
	 * @param item item in node
	 * @param priority priority
	 */
	public Node(E item, int priority)
	{
		this.item = item;
		this.priority=priority;
	}
	/**
	 * @return the item
	 */
	public E getItem()
	{
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(E item)
	{
		this.item = item;
	}

	/**
	 * @return the next
	 */
	public Node<E> getNext()
	{
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(Node<E> next)
	{
		this.next = next;
	}

	/**
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	@Override
	public int compareTo(Node<E> tempNode)
	{
		// TODO Auto-generated method stub
		return this.priority - tempNode.getPriority();
	}
}
