package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Node;

/**
 * A storage class for map intersections
 * 
 * @author Oliver Greenaway
 * 
 */
public class NodeArray implements Iterable<Node> {

	// Data variables
	private List<Node> nodes;

	/**
	 * Constructs an empty Collection of nodes
	 */
	public NodeArray() {
		this.nodes = new ArrayList<Node>();
	}

	/**
	 * Adds the given node to the collection of nodes
	 * 
	 * @param object
	 *            The Map Node to be added
	 */
	public void add(Node object) {
		nodes.add(object);
	}

	/**
	 * Check if the given node is contained within the collection
	 * 
	 * @param object
	 *            The Map Node being checked for
	 * @return True if the Node is contained, otherwise returns False
	 */
	public boolean contains(Node object) {
		if (object == null) {
			return false;
		} else {
			int i = 0;
			int j = nodes.size() - 1;
			while (i <= j) {
				int mid = (int) Math.ceil((double) (i + j) / 2);
				if (nodes.get(mid).compareTo(object) == 0) {
					return true;
				} else if (nodes.get(mid).compareTo(object) > 0) {
					j = mid - 1;
				} else {
					i = mid + 1;
				}
			}
			return false;
		}
	}

	/**
	 * Checks the collection for a node that has the matching ID
	 * 
	 * @param ID
	 *            The ID of the Node being searched for
	 * @return Returns the Node with the matching ID if one exists, else returns
	 *         null
	 */
	public Node get(int ID) {
		int i = 0;
		int j = nodes.size() - 1;
		while (i <= j) {
			int mid = (int) Math.ceil((double) (i + j) / 2);
			if (nodes.get(mid).getID() == ID) {
				return nodes.get(mid);
			} else if (nodes.get(mid).getID() > ID) {
				j = mid - 1;
			} else {
				i = mid + 1;
			}
		}
		return null;
	}

	/**
	 * Sorts the data using the quick sort Algorithm
	 */
	public void sort() {
		quicksort(0, nodes.size() - 1);
	}

	/**
	 * The quick sort Algorithm
	 * 
	 * @param low
	 *            The lower bound of the area to be sorted
	 * @param high
	 *            The upper bound of the area being sorted
	 */
	private void quicksort(int low, int high) {
		int i = low;
		int j = high;
		int pivot = nodes.get(low + (high - low) / 2).getID();

		while (i <= j) {
			while (nodes.get(i).getID() < pivot) {
				i++;
			}
			while (nodes.get(j).getID() > pivot) {
				j--;
			}
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	/**
	 * Swaps the ith index with the jth index of the storage class
	 * 
	 * @param i
	 *            Index to be switched
	 * @param j
	 *            Index to be switched
	 */
	private void exchange(int i, int j) {
		Node temp = nodes.get(i);
		nodes.set(i, nodes.get(j));
		nodes.set(j, temp);
	}

	@Override
	public Iterator<Node> iterator() {
		return new ArrayIterator();
	}

	/**
	 * Iterator for the NodeArray Collection
	 * 
	 * @author Oliver Greenaway
	 * 
	 */
	private class ArrayIterator implements Iterator<Node> {

		// The current index the iterator is reading
		private int count;

		/**
		 * Constructs a new iterator starting from index 0
		 */
		public ArrayIterator() {
			count = 0;
		}

		@Override
		public boolean hasNext() {
			return count < nodes.size();
		}

		@Override
		public void remove() {
		}

		@Override
		public Node next() {
			return nodes.get(count++);
		}

	}
}
