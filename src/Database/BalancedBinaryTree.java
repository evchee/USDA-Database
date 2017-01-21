package Database;

/**
 * Balanced Binary Tree
 * Various methods and behaviors for a balanced binary tree
 * @author Eric Chee
 *@version 11/22/2015
 * @param <E>
 */
public class BalancedBinaryTree<E extends Comparable<E>> extends BinaryTree<E>
{
	/**
	 * Creates new balanced binary tree
	 */
	public BalancedBinaryTree()
	{
		super();
	}

	/**
	 * Insert value into binary tree
	 * @param value
	 */
	public void insert(E value)
	{
		// create new node
		BinaryTreeNode<E> n = new BinaryTreeNode<E>(value);
		// start recursive procedure for inserting the node
		internalInsert(this.head, n);
		updateHeight(head);
	}

	/**
	 * Recursive method to insert a node into a tree.
	 * @param start The node currently compared, usually you start with the root.
	 * @param newNode The node to be inserted.
	 */
	public void internalInsert(BinaryTreeNode<E> start, BinaryTreeNode<E> newNode)
	{
		// If node to compare is null, the node is inserted. If the root is
		// null, it is the root of the tree.
		if (start == null)
		{
			this.head = newNode;
		}
		//Finds the correct place to put the node
		else
		{
			// If smaller, continue with the left node
			if (newNode.getItem().compareTo(start.getItem()) < 0)
			{
				if (start.getLeftChild() == null)
				{
					start.setLeftChild(newNode);
					newNode.setParent(start);
					recursiveBalance(start);
				}
				else
				{
					internalInsert(start.getLeftChild(), newNode);
				}
			}
			//If larger, continue with the Right node
			else if (newNode.getItem().compareTo(start.getItem()) > 0)
			{
				if (start.getRightChild() == null)
				{
					start.setRightChild(newNode);
					newNode.setParent(start);
					recursiveBalance(start);
				}
				else
				{
					internalInsert(start.getRightChild(), newNode);
				}
			}
			//Node already exists
			else
			{
			}
		}
	}


	/**
	 * Balances binary tree after node is inserted;
	 * @param start current node
	 */
	public void recursiveBalance(BinaryTreeNode<E> start)
	{

		setBalance(start);
		int balance = start.getBalance();
		//If difference is 2 to the left must rotate to the right
		if (balance == -2)
		{
			//Left Right case
			if (height(start.getLeftChild().getLeftChild()) >= height(start
					.getLeftChild().getRightChild()))
			{
				start = rotateRight(start);
			}
			//Left Left case
			else
			{
				start = doubleRotateLeftRight(start);
			}
		}
		else if (balance == 2)
		{
			//Right Left case
			if (height(start.getRightChild().getRightChild()) >= height(start
					.getRightChild().getLeftChild()))
			{
				start = rotateLeft(start);
			}
			//Right Right case
			else
			{
				start = doubleRotateRightLeft(start);
			}
		}
		//Updates balance
		if (start.getParent() != null)
		{
			recursiveBalance(start.getParent());
		}
		//If head
		else
		{
			this.head = start;
		}
	}

	/**
	 * Sets balance
	 * @param cur node to set balance
	 */
	private void setBalance(BinaryTreeNode<E> cur)
	{
		cur.setBalance(height(cur.getRightChild()) - height(cur.getLeftChild()));
	}

	/**
	 * Double left rotate
	 * @param node The node for the rotation.
	 * @return The root after the double rotation.
	 */
	public BinaryTreeNode<E> doubleRotateLeftRight(BinaryTreeNode<E> node)
	{
		node.setLeftChild(rotateLeft(node.getLeftChild()));
		return rotateRight(node);
	}

	/**
	 * Double Right rotate
	 * @param root The node for the rotation.
	 * @return The root after the double rotation.
	 */
	public BinaryTreeNode<E> doubleRotateRightLeft(BinaryTreeNode<E> root)
	{
		root.setRightChild(rotateRight(root.getRightChild()));
		return rotateLeft(root);
	}

	/**
	 * Rotates tree to the left
	 * @param root root node to rotate from
	 * @return pivot node
	 */
	public BinaryTreeNode<E> rotateLeft(BinaryTreeNode<E> root)
	{

		//Set pivot to right child
		BinaryTreeNode<E> pivot = root.getRightChild();
		//Make pivot have the same parent as root
		pivot.setParent(root.getParent());
		//Move pivot left child to root right child
		root.setRightChild(pivot.getLeftChild());
		if (root.getRightChild() != null)
		{
			root.getRightChild().setParent(root);
		}
		//Make root child of parent
		pivot.setLeftChild(root);
		root.setParent(pivot);
		//If root had a parent determine if it was a left or right child and set it to pivot
		if (pivot.getParent() != null)
		{
			//If was right child
			if (pivot.getParent().getRightChild() == root)
			{
				pivot.getParent().setRightChild(pivot);
			}
			//If was left child
			else if (pivot.getParent().getLeftChild() == root)
			{
				pivot.getParent().setLeftChild(pivot);
			}
		}
		setBalance(root);
		setBalance(pivot);
		return pivot;
	}

	/**
	 * Right rotation using the given node.
	 * @param root The node for the rotation
	 * @return The root of the new rotated tree.
	 */
	public BinaryTreeNode<E> rotateRight(BinaryTreeNode<E> root)
	{
		//Set pivot to left child
		BinaryTreeNode<E> pivot = root.getLeftChild();
		//Make pivot have the same parent as root
		pivot.setParent(root.getParent());
		//Move pivot right child to root left child
		root.setLeftChild(pivot.getRightChild());
		if (root.getLeftChild() != null)
		{
			root.getLeftChild().setParent(root);
		}
		//Make root child of parent
		pivot.setRightChild(root);
		root.setParent(pivot);
		//If root had a parent determine if it was a left or right child and set it to pivot
		if (pivot.getParent() != null)
		{
			if (pivot.getParent().getRightChild() == root)
			{
				pivot.getParent().setRightChild(pivot);
			}
			else if (pivot.getParent().getLeftChild() == root)
			{
				pivot.getParent().setLeftChild(pivot);
			}
		}
		setBalance(root);
		setBalance(pivot);
		return pivot;
	}

}
