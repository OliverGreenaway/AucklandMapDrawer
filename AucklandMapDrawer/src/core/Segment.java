package core;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import util.DoublePoint;
import util.NodeArray;

/**
 * A single section of road between two intersectons
 *
 * @author Oliver Greenaway
 *
 */
public class Segment {

	// Segment properties
	private double length;
	private String roadName = "";

	// The end nodes of the segment
	private int node1ID, node2ID;
	private Node node1, node2;

	// The list of coordinates for the segment
	private List<DoublePoint> points = new ArrayList<DoublePoint>();

	/**
	 * Constructs a new segment with the given length and the ID's of the end
	 * nodes
	 *
	 * @param length
	 *            The length of the segment of road
	 * @param nodeID1
	 *            The ID of a end node
	 * @param nodeID2
	 *            The ID of a end node
	 */
	public Segment(double length, int nodeID1, int nodeID2) {
		this.length = length;
		this.node1ID = nodeID1;
		this.node2ID = nodeID2;
	}

	/**
	 * Adds a coordinate to the list of coordinates
	 *
	 * @param x
	 *            The x position of the point
	 * @param y
	 *            The y position of the point
	 */
	public void addPoint(double x, double y) {
		points.add(new DoublePoint(x, y));
	}
	
	/**
	 * Sets the name of the road the segment belongs to
	 * @param name The name to be set
	 */
	public void addRoadName(String name){
		roadName = name;
	}
	
	/**
	 * Returns the RoadName the segment belongs to
	 * @return The name of the road
	 */
	public String getName(){
		return roadName;
	}

	/**
	 * Draws the segment by linking points with lines
	 *
	 * @param g
	 *            The Graphics object to be drawn to
	 * @param offsetX
	 *            The current offset on the x-axis
	 * @param offsetY
	 *            The current offset on the y-axis
	 * @param zoom
	 *            The current zoom factor of the map
	 */
	public void draw(Graphics2D g, double offsetX, double offsetY, double zoom) {
		DoublePoint prevPoint = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			g.drawLine((int) ((points.get(i).getX() + offsetX) * zoom),
					(int) ((points.get(i).getY() + offsetY) * zoom),
					(int) ((prevPoint.getX() + offsetX) * zoom),
					(int) ((prevPoint.getY() + offsetY) * zoom));
			prevPoint = points.get(i);
		}
	}

	/**
	 * Searches the Collection of nodes for the stored node ID's and connects
	 * the segment with the Node both at node level and in the segment
	 *
	 * @param nodes
	 *            The Collection of nodes
	 */
	public void connect(NodeArray nodes) {
		node1 = nodes.get(node1ID);
		node2 = nodes.get(node2ID);
		if (node1 != null) {
			node1.connect(this);
		} else {
			System.err.println(node1ID + " does not exist");
		}
		if (node2 != null) {
			node2.connect(this);
		} else {
			System.err.println(node2ID + " does not exist");
		}
	}

	/**
	 * Checks if the segment has been clicked on
	 *
	 * @param x
	 *            MouseX coordinate
	 * @param y
	 *            MouseY coordinate
	 * @param offsetX
	 *            The offset on the x-axis
	 * @param offsetY
	 *            The offset on the y-axis
	 * @param zoom
	 *            The current zoom level
	 * @return True if on the segment, false is not
	 */
	public boolean on(int x, int y, double offsetX, double offsetY, double zoom) {
		Path2D.Double path = new Path2D.Double();
		path.moveTo((points.get(0).getX() + offsetX) * zoom, (points.get(0)
				.getY() + offsetY) * zoom);
		for (DoublePoint i : points) {
			path.lineTo((i.getX() + offsetX) * zoom, (i.getY() + offsetY)
					* zoom);
			path.moveTo((i.getX() + offsetX) * zoom, (i.getY() + offsetY)
					* zoom);
		}
		return path.getBounds().contains(x, y);
	}
}
