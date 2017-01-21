package Database;

/**
 * Priority Queue
 * Various methods and behaviors for a priority queue
 * @author Eric Chee
 * @version 11/22/2015
 * @param <E>
 */
public class PriorityQueue<E>
{
	private Node<E> head, tempNode;
	private int length;

	/**
	 * Constructs new priority queue
	 */
	public PriorityQueue()
	{
		length = 0;
	}

	/**
	 * adds item based on priority
	 * @param value 
	 * @param priority
	 */
	void enqueue(E value, int priority)
	{
		Node<E> temp = new Node<E>(value, priority);
		//if empty queue
		if (head == null)
		{
			head = temp;
		}
		else
		{
			tempNode = head;
			//if new head
			if (temp.compareTo(tempNode) > 0)
			{
				tempNode = temp;
				tempNode.setNext(head);
				head = tempNode;
			}

			else
			{
				//finds place in queue to insert(highest first)
				Node<E> prev = tempNode;
				tempNode = tempNode.getNext();
				for (int i = 0; i < length; i++)
				{
					if (tempNode == null)
					{
						prev.setNext(temp);
						break;
					}
					else if (temp.compareTo(tempNode) > 0)
					{
						temp.setNext(prev.getNext());
						prev.setNext(temp);
						break;
					}
					prev = tempNode;
					tempNode = tempNode.getNext();
				}
			}

		}
		length++;//add one to length
	}

	/**
	 * Removes one from the queue
	 * @return
	 */
	E dequeue()
	{
		if (head!=null)
		{
		//set head to next return old head's value
		Node<E> temp = head;
		head = head.getNext();
		length--;
		return temp.getItem();
		}
		return null;

	}
	
	/**
	 * @return queue size
	 */
	public int getSize()
	{
		return length;
	}
	
	/**
	 * @return queue head
	 */
	public Node<E> getHead()
	{
		return head;
	}

	/**
	 * @return if empty
	 */
	boolean isEmpty()
	{
		return (head == null);
	}
}
