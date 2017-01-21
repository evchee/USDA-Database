package Database;

/**
 * Simple Linked List
 * Various methods and behaviors for a Simple Linked List
 * @author Eric Chee
 * @version 11/22/2015
 * @param <E>
 */
public class SimpleLinkedList<E extends Comparable<E>>
{
	private Node<E> head, tail, tempNode, tempNodeTwo;

	/**
	 * Creates new linked list
	 */
	public SimpleLinkedList()
	{
	}

	/**
	 * Add item to list
	 * @param value value to add
	 */
	public void add(E value)
	{
		Node<E> newNode = new Node<E>(value);
		tail = newNode;
		// if empty list
		if (head == null)
		{
			head = newNode;
		}
		// add to end
		else
		{
			tempNode = head;
			while (tempNode.getNext() != null)
			{
				tempNode = tempNode.getNext();
			}
			tempNode.setNext(newNode);
			tail = newNode;
		}
	}

	/**
	 * Remove node
	 * @param node node to remove
	 */
	public void remove(Node<E> node)
	{
		// if head set next node to head
		if (node == head)
		{
			Node<E> removeNode = head;
			head = head.getNext();
			removeNode.setNext(null);
		}
		else
		// find node and point prevoius to next
		{
			tempNode = head;
			while (tempNode != node)
			{
				tempNodeTwo = tempNode;
				tempNode = tempNode.getNext();
			}
			if (tempNode == tail)
			{
				tempNodeTwo.setNext(null);
				tail = tempNodeTwo;
			}
			else
				tempNodeTwo.setNext(tempNode.getNext());
		}

	}

	/**
	 * Get size of linked list
	 * @return size
	 */
	public int getSize()
	{
		// count the size
		int size = 0;
		tempNode = head;
		while (tempNode != null)
		{
			tempNode = tempNode.getNext();
			size++;
		}
		return size;
	}

	/**
	 * @return list head
	 */
	public Node<E> getHead()
	{
		return head;
	}

	/**
	 * @return list tail
	 */
	public Node<E> getTail()
	{
		return tail;
	}

	/**
	 * sets head to null
	 */
	void clear()
	{
		head = null;
	}
}
