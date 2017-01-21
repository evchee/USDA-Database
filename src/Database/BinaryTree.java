package Database;

/**
 * Binary Tree
 * Various methods and behaviors for a binary tree
 * @author Eric Chee
 * @version 11/22/2015
 * @param <E>
 */
public class BinaryTree<E extends Comparable<E>>
{
	protected BinaryTreeNode<E> head, tempNode, tempNode2;

	/**
	 * creates binary tree
	 */
	public BinaryTree()
	{
	}
	/**
	 * @return tree head
	 */
	public BinaryTreeNode<E> getHead()
	{
		return head;
	}
	/**
	 * Adds binary tree node
	 * @param value
	 */
	public void add(E value)
	{
		BinaryTreeNode<E> child = new BinaryTreeNode<E>(value);
		//If tree is empty
		if (head == null)
		{
			head = child;
			head.setHeight(0);
		}
		else
		{
			BinaryTreeNode<E> parent = internalAdd(child, head);
			if (parent.getItem().compareTo(child.getItem()) > 0)
			{
				parent.setLeftChild(child);
			}
			else
			{
				parent.setRightChild(child);
			}
			child.setParent(parent);
			updateHeight(head);
		}
	}
	/**
	 * Height of tree
	 * @param height of node
	 * @return height of node
	 */
	protected int height(BinaryTreeNode<E> node)
	{
		if (node == null)
			return -2;
		else
			return node.getHeight();
	}

	/**
	 * @return if empty
	 */
	public boolean isEmpty()
	{
		return head == null;
	}

	/**
	 * @return tree size
	 */
	public int size()
	{
		return internalSize(head);
	}

	/**
	 * Internal size finder
	 * @param start current node
	 * @return size
	 */
	protected int internalSize(BinaryTreeNode<E> start)
	{
		if (start == null)
			return 0;
		return 1 + internalSize(start.getLeftChild())
				+ internalSize(start.getRightChild());

	}

	/**
	 * checks if value exists in tree
	 * @param value
	 * @return if it exists
	 */
	public boolean contains(E value)
	{
		return privateContains(value, head);

	}

	/**
	 * Get node that contains value
	 * @param value value to search for
	 * @return node that contains value
	 */
	public BinaryTreeNode<E> getNode(E value)
	{
		return (privateGetNode(value, head));
	}
	/**
	 * Find node that contains value
	 * @param value value to remove
	 * @param start starting point to search
	 * @return node that contains value
	 */
	public BinaryTreeNode<E> getNode(E value, BinaryTreeNode<E> start)
	{
		if (start == null)
		{
			return null;
		}
		else if (start.getItem().equals(value))
		{
			return start;
		}
		else
		{
			BinaryTreeNode<E> left = getNode(value, start.getLeftChild());
			BinaryTreeNode<E> right = getNode(value, start.getRightChild());
			if (left == null)
				return right;
			else
				return left;

		}
	}
	/**
	 * Recursively find node that contains value
	 * @param value value to search for
	 * @param start current node
	 * @return node that contains value
	 */
	protected BinaryTreeNode<E> privateGetNode(E value, BinaryTreeNode<E> start)
	{
		//Base case
		if (start == null)
		{
			return null;
		}
		//Value found return value
		else if (start.getItem().equals(value))
		{
			return start;
		}
		//Recursively call left and right node
		else
		{
			BinaryTreeNode<E> left = privateGetNode(value, start.getLeftChild());
			if (left != null)
				return left;
			else
				return privateGetNode(value, start.getRightChild());
		}
	}

	/**
	 * Recursively check tree contains value
	 * @param value value to search for
	 * @param start current Node
	 * @return if tree contains node
	 */
	protected boolean privateContains(E value, BinaryTreeNode<E> start)
	{
		//Base case
		if (start == null)
		{
			return false;
		}
		//Value found return true
		else if (start.getItem().equals(value))
		{
			return true;
		}
		//Call left and right
		else
		{
			return privateContains(value, start.getLeftChild())
					|| privateContains(value, start.getRightChild());
		}

	}

	/**
	 * Recursively add method
	 * @param newNode node to add
	 * @param startcurrent node
	 * @return
	 */
	protected BinaryTreeNode<E> internalAdd(BinaryTreeNode<E> newNode,
			BinaryTreeNode<E> start)
	{
		//base case
		if (start.isLeaf())
		{
			return start;
		}
		//Continue left if larger
		else
		{
			if (start.getItem().compareTo(newNode.getItem()) > 0)
			{
				if (start.getLeftChild() == null)
				{
					return start;
				}
				return internalAdd(newNode, start.getLeftChild());
			}
			
			//Continue right if smaller
			else
			{
				if (start.getRightChild() == null)
				{
					return start;
				}
				return internalAdd(newNode, start.getRightChild());
			}

		}
	}

	/**
	 * remove a node with the value
	 * @param value value to remove
	 */
	public void remove(E value)
	{
		if (this.contains(value))//if value is in tree
		{
			BinaryTreeNode<E> toBeRemoved = getNode(value, head);
		//if leaf remove it
			if (toBeRemoved.isLeaf())
			{
				if (toBeRemoved.getParent().getLeftChild() == toBeRemoved)
					toBeRemoved.getParent().setLeftChild(null);
				else
					toBeRemoved.getParent().setRightChild(null);
			}
			else
			{
				//re attach children in the correct place
				BinaryTreeNode<E> reattach = toBeRemoved.getRightChild();
				if (toBeRemoved.getLeftChild() != null)
				{
					BinaryTreeNode<E> toBeMoved = toBeRemoved.getLeftChild();
					BinaryTreeNode<E> moveTo = toBeRemoved.getRightChild();
					while (moveTo.getLeftChild() != null)
					{
						moveTo = moveTo.getLeftChild();
					}
					toBeMoved.setParent(moveTo);
					moveTo.setLeftChild(toBeMoved);
				}
				reattach.setParent(toBeRemoved.getParent());
				toBeRemoved.getParent().setLeftChild(reattach);
				updateHeight(reattach);
			}
		}
	}

	/**
	 * Updates height
	 * @param start current node
	 * @return the start nodes height
	 */
	protected int updateHeight(BinaryTreeNode<E> start)
	{
		//base cases
		if (start == null)
		{
			return -1;
		}
		if (start.isLeaf())
		{
			start.setHeight(0);
			return 0;
		}
		//Recursively updates height (difference between left and right sides)
		else
		{
			start.setHeight(1 + Math.max(updateHeight(start.getLeftChild()),(updateHeight(start.getRightChild()))));
			return start.getHeight();
		}
	}

	/**
	 * Empties tree
	 */
	public void clear()
	{
		head=null;
	}
}
