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
	private boolean selected = false;
	private int depth = Integer.MAX_VALUE; //for articulation

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
	 * Returns a list of connecting segments
	 *
	 * @return Segments connected to the node
	 */
	public List<Segment> getNeighbours() {
		return connections;
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
		if (selected) {
			g.setColor(Color.red);
		}
		if (selected) {
			g.drawOval((int) ((this.x + offsetX) * zoom) - 3,
					(int) ((this.y + offsetY) * zoom) - 3, 6, 6);
		}
		g.fillRect((int) ((this.x + offsetX) * zoom) - 1,
				(int) ((this.y + offsetY) * zoom) - 1, 2, 2);
	}

	public void drawArticulation(Graphics2D g, double offsetX, double offsetY,
			double zoom) {
		g.setColor(Color.green);
		g.drawOval((int) ((this.x + offsetX) * zoom) - 4,
				(int) ((this.y + offsetY) * zoom) - 4, 8, 8);
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

	/**
	 * Sets whether or not the node is selected on the map
	 *
	 * @param s
	 *            True is selected, false if not
	 */
	public void setSelect(boolean s) {
		selected = s;
	}

	/**
	 * Returns details on roads branching off of the node
	 *
	 * @return A string representation of the intersection
	 */
	public String getDetails() {
		String toReturn = getID() + "\n";
		for (Segment s : connections) {
			toReturn += s.getName() + "\n";
		}
		return toReturn;
	}

	/**
	 * Returns the distance from the x,y coordinate to the nodes location
	 *
	 * @param x
	 *            The x coordinate to compare to
	 * @param y
	 *            The y coordinate to compare to
	 * @param offsetX
	 *            The current offset of the map on the x-axis
	 * @param offsetY
	 *            The current offset of the map on the y-axis
	 * @param zoom
	 *            The current zoom level of the map
	 * @return The distance between points
	 */
	public double getDist(int x, int y, double offsetX, double offsetY,
			double zoom) {
		return Point.distance(x, y, (this.x + offsetX) * zoom,
				(this.y + offsetY) * zoom);
	}

	public void setDepth(int depth){
		this.depth = depth;
	}

	public int getDepth(){
		return this.depth;
	}

}
