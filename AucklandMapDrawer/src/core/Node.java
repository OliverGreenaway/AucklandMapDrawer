package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an intersection on a map
 *
 * @author Oliver Greenaway
 *
 */
public class Node implements Comparable<Node> {

	private int id;
	private double x, y;
	private List<Segment> connections;

	/**
	 * Constructs a Node with the given ID, x position and y position
	 *
	 * @param id
	 *            The ID of the node
	 * @param x
	 *            The x position of the node
	 * @param y
	 *            The y position of the node
	 */
	public Node(int id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
		connections = new ArrayList<Segment>();
	}

	@Override
	public int compareTo(Node node) {
		return this.getID() - node.getID();
	}

	/**
	 * Returns the ID of the node
	 *
	 * @return The Node ID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Returns the coordinates of the Node as a Point object
	 *
	 * @return The coordinates of the Node
	 */
	public Point getPoint() {
		return new Point((int) this.x, (int) this.y);
	}

	/**
	 * Draws a Node as a blue 1px X 1px dot
	 *
	 * @param g
	 *            The graphics object to be drawn to
	 * @param offsetX
	 *            The current maps X offset
	 * @param offsetY
	 *            The current maps Y offset
	 * @param zoom
	 *            The current maps zoom level
	 */
	public void draw(Graphics2D g, double offsetX, double offsetY, double zoom) {
		g.setColor(Color.blue);
		g.fillRect((int) ((this.x + offsetX) * zoom),
				(int) ((this.y + offsetY) * zoom), 1, 1);
	}

	/**
	 * Connects the node with the given Segment
	 *
	 * @param segment
	 *            The segment to connect with
	 */
	public void connect(Segment segment) {
		connections.add(segment);
	}

}
