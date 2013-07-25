package util;

import java.util.ArrayList;
import java.util.List;

import core.Road;

/**
 * A tree containing nodes that is used to store road names and their
 * coresponding Road objects
 *
 * @author Oliver Greenaway
 *
 */
public class TrieTree {

	private TrieTreeNode[] children;

	/**
	 * Creates a new Trie Tree
	 */
	public TrieTree() {
		children = new TrieTreeNode[26];
	}

	/**
	 * Adds the given road to the Tree
	 *
	 * @param r
	 *            The road to be added
	 */
	public void add(Road r) {
		char first = r.getName().toLowerCase().charAt(0);
		if (first - 'a' >= 0 && first - 'a' < 26) {
			if (children[first - 'a'] != null) {
				children[first - 'a'].add(r);
			} else {
				children[first - 'a'] = new TrieTreeNode(r);
			}
		}
	}

	/**
	 * Returns a list of roads which start with the given string and are closest
	 * phonetically to it
	 *
	 * @param s
	 *            The string to be searched for
	 * @return A list of roads
	 */
	public List<Road> getTen(String s) {
		if (s.length() == 0) {
			for (TrieTreeNode t : children) {
				if (t != null) {
					return t.getTen();
				}
			}
		}
		char first = s.toLowerCase().charAt(0);
		if (first - 'a' >= 0 && first - 'a' < 26
				&& children[first - 'a'] != null) {
			return children[first - 'a'].getTen(s.substring(1));
		} else {
			return new ArrayList<Road>();
		}
	}
}
