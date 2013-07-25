package util;

import java.util.ArrayList;
import java.util.List;

import core.Road;

/**
 * A tree collection for storing words
 *
 * @author Oliver Greenaway
 *
 */
public class TrieTreeNode {

	// Current path, letter and whether the word is complete
	private String path;
	private char letter;
	private boolean isFinal;

	// An array of the next level of letters
	private TrieTreeNode[] children;

	// The collection of roads that the word represents should this be a
	// complete word
	private List<Road> roads = new ArrayList<Road>();

	/**
	 * Constructs a new TrieTree by taking the given road and splitting it into
	 * the components needed and calling another constructor
	 *
	 * @param road
	 *            The road to be stored
	 */
	public TrieTreeNode(Road road) {
		this(road.getName().charAt(0), "", road.getName().substring(1), road);
	}

	/**
	 * Constructs a new TrieTreenode with the given information
	 *
	 * @param c
	 *            The current letter of the word being stored
	 * @param path
	 *            The path so far through the tree
	 * @param roadLine
	 *            The String being stored
	 * @param r
	 *            The Road being represented
	 */
	public TrieTreeNode(char c, String path, String roadLine, Road r) {
		roadLine = processRoadName(roadLine);
		this.letter = c;
		this.path = path + c;
		children = new TrieTreeNode[26];
		if (roadLine.length() == 0) {
			roads.add(r);
			isFinal = true;
		} else {
			char next = roadLine.charAt(0);
			String newRoadLine = roadLine.substring(1);
			int index = next - 'a';
			children[index] = new TrieTreeNode(next, path, newRoadLine, r);
		}
	}

	/**
	 * Removes all special characters and spaces from the given name
	 *
	 * @param name
	 *            The name to be processed
	 * @return The formatted string
	 */
	private String processRoadName(String name) {
		char[] array = name.toLowerCase().toCharArray();
		String correctName = "";
		for (Character c : array) {
			if (c - 'a' >= 0 && c - 'a' < 26) {
				correctName += c;
			}
		}
		return correctName;
	}

	/**
	 * gets the ten closed related roads starting from this node
	 *
	 * @return An array list containing the most relevant Roads
	 */
	public List<Road> getTen() {
		return getTen(new ArrayList<Road>());
	}

	/**
	 * Gets the ten closed related roads starting from the end of the given path
	 *
	 * @param path
	 *            The path defining the start of the search
	 * @return An array list containing the most relevant Roads
	 */
	public List<Road> getTen(String path) {
		path = processRoadName(path);
		if (path.length() == 0) {
			return getTen();
		}
		if (children[path.charAt(0) - 'a'] != null) {
			return children[path.charAt(0) - 'a'].getTen(path.substring(1));
		} else {
			return new ArrayList<Road>();
		}
	}

	/**
	 * Gets the ten closed related roads adding them to the given array list
	 *
	 * @param roads
	 *            The Array List of roads so far found
	 * @return The Array List with any appended roads found
	 */
	private List<Road> getTen(List<Road> roads) {
		if (roads.size() == 10) {
			return roads;
		} else {
			if (isFinal) {
				roads.addAll(this.roads);
			}
			if (roads.size() < 10) {
				for (TrieTreeNode t : children) {
					if (t != null) {
						roads = t.getTen(roads);
						if (roads.size() == 10) {
							return roads;
						}
					}
				}
			}
			return roads;
		}
	}

	/**
	 * Returns the nodes assigned character
	 *
	 * @return Assigned character
	 */
	public char getChar() {
		return letter;
	}

	/**
	 * Adds the given road to the TrieTree
	 *
	 * @param road
	 *            The road to be added
	 */
	public void add(Road road) {
		add(road.getName().charAt(0), "", road.getName().substring(1), road);
	}

	/**
	 * Adds the road with the given details to the trie tree
	 *
	 * @param c
	 *            The character to be stored as this node
	 * @param path
	 *            The path for the string being added
	 * @param roadLine
	 *            The remaining characters to be added
	 * @param r
	 *            The Road being added
	 */
	public void add(char c, String path, String roadLine, Road r) {
		roadLine = processRoadName(roadLine);
		if (roadLine.length() == 0) {
			roads.add(r);
			isFinal = true;
		} else {
			char next = roadLine.charAt(0);
			String newRoadLine = roadLine.substring(1);
			int index = next - 'a';
			if (children[index] == null) {
				children[index] = new TrieTreeNode(next, path, newRoadLine, r);
			} else {
				children[index].add(next, path, newRoadLine, r);
			}
		}
	}

}
