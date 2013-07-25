package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Road;

/**
 * A storage class for map roads
 * 
 * @author Oliver Greenaway
 * 
 */
public class RoadArray implements Iterable<Road> {

	// Data variables
	private List<Road> roads;

	/**
	 * Constructs an empty Collection of roads
	 */
	public RoadArray() {
		this.roads = new ArrayList<Road>();
	}

	/**
	 * Adds the given road to the collection of roads
	 * 
	 * @param object
	 *            The Map Road to be added
	 */
	public void add(Road object) {
		roads.add(object);
	}

	/**
	 * Check if the given road is contained within the collection
	 * 
	 * @param object
	 *            The Map Road being checked for
	 * @return True if the Road is contained, otherwise returns False
	 */
	public boolean contains(Road object) {
		if (object == null) {
			return false;
		} else {
			int i = 0;
			int j = roads.size() - 1;
			while (i <= j) {
				int mid = (int) Math.ceil((double) (i + j) / 2);
				if (roads.get(mid).compareTo(object) == 0) {
					return true;
				} else if (roads.get(mid).compareTo(object) > 0) {
					j = mid - 1;
				} else {
					i = mid + 1;
				}
			}
			return false;
		}
	}

	/**
	 * Checks the collection for a road that has the matching ID
	 * 
	 * @param ID
	 *            The ID of the Road being searched for
	 * @return Returns the Road with the matching ID if one exists, else returns
	 *         null
	 */
	public Road get(int ID) {
		int i = 0;
		int j = roads.size() - 1;
		while (i <= j) {
			int mid = (int) Math.ceil((double) (i + j) / 2);
			if (roads.get(mid).getID() == ID) {
				return roads.get(mid);
			} else if (roads.get(mid).getID() > ID) {
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
		quicksort(0, roads.size() - 1);
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
		int pivot = roads.get(low + (high - low) / 2).getID();

		while (i <= j) {
			while (roads.get(i).getID() < pivot) {
				i++;
			}
			while (roads.get(j).getID() > pivot) {
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
		Road temp = roads.get(i);
		roads.set(i, roads.get(j));
		roads.set(j, temp);
	}

	@Override
	public Iterator<Road> iterator() {
		return new TreeIterator();
	}

	/**
	 * Iterator for the NodeArray Collection
	 * 
	 * @author Oliver Greenaway
	 * 
	 */
	private class TreeIterator implements Iterator<Road> {

		// The current index the iterator is reading
		private int count;

		/**
		 * Constructs a new iterator starting from index 0
		 */
		public TreeIterator() {
			count = 0;
		}

		@Override
		public boolean hasNext() {
			return count < roads.size();
		}

		@Override
		public void remove() {
		}

		@Override
		public Road next() {
			return roads.get(count++);
		}

	}

}
