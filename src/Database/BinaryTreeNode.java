package Database;

/**
 * Binary Tree Node 
 * Various methods and behaviors for a balanced binary tree node
 * @author Eric Chee
 * @version 11/22/2015
 * @param <E>
 */
public class BinaryTreeNode<E>
{
	private BinaryTreeNode<E> parent, leftChild, rightChild;
	private E item;
	private int height, balance;

	/**
	 * Creates new node
	 * @param data data to put in node
	 */
	public BinaryTreeNode(E data)
	{
		setItem(data);
	}

	/**
	 * Checks if node is leaf or not
	 * @return if node is leaf or not
	 */
	public boolean isLeaf()
	{
		// if has no children
		if (this.leftChild == null && this.rightChild == null)
			return true;
		else
			return false;
	}

	/**
	 * @return the parent
	 */
	public BinaryTreeNode<E> getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(BinaryTreeNode<E> parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the leftChild
	 */
	public BinaryTreeNode<E> getLeftChild()
	{
		return leftChild;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public void setLeftChild(BinaryTreeNode<E> leftChild)
	{
		this.leftChild = leftChild;
	}

	/**
	 * @return the rightChild
	 */
	public BinaryTreeNode<E> getRightChild()
	{
		return rightChild;
	}

	/**
	 * @param rightChild the rightChild to set
	 */
	public void setRightChild(BinaryTreeNode<E> rightChild)
	{
		this.rightChild = rightChild;
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
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return the balance
	 */
	public int getBalance()
	{
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(int balance)
	{
		this.balance = balance;
	}
}
