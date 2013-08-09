package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import util.NodeArray;

/**
 * A Road Object containing information on the Map Road aswell as segments of
 * the road to be drawn
 *
 * @author Oliver Greenaway
 *
 */
public class Road implements Comparable<Road> {

	// Road information
	private int ID, type, speed;
	private String roadName, city;
	private boolean oneway, roadclass, notforcar, notforpede, notforbicy;

	// Segments that make up the Road
	private List<Segment> segments = new ArrayList<Segment>();

	// Selection status
	private boolean selected = false;

	/**
	 * Construct a new road with the given information
	 *
	 * @param ID
	 *            The Roads ID
	 * @param type
	 *            The integer representing the type of road
	 * @param label
	 *            The name of the road
	 * @param city
	 *            The City that the road is contained in
	 * @param oneway
	 *            Whether the road is one way or not
	 * @param speed
	 *            The max speed of the road
	 * @param roadclass
	 *            The class of the road
	 * @param notforcar
	 *            Whether the road is for cars or not
	 * @param notforpede
	 *            Whether the road is for pedestrians of not
	 * @param notforbicy
	 *            Whether the road is for bikes or not
	 */
	public Road(int ID, int type, String label, String city, int oneway,
			int speed, int roadclass, int notforcar, int notforpede,
			int notforbicy) {
		this.ID = ID;
		this.type = type;
		this.roadName = label;
		this.city = city;
		this.oneway = oneway == 1;
		this.speed = speed;
		this.roadclass = roadclass == 1;
		this.notforcar = notforcar == 1;
		this.notforpede = notforpede == 1;
		this.notforbicy = notforbicy == 1;
	}

	@Override
	public int compareTo(Road road) {
		return this.getID() - road.getID();
	}

	/**
	 * Returns the ID of the road
	 *
	 * @return Road ID
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Adds a segment to the roads list of segments
	 *
	 * @param s
	 *            The segment to be added
	 */
	public void addSegment(Segment s) {
		s.addRoadName(this.getName());
		segments.add(s);
	}

	/**
	 * When called draws the road by iterating over the stored segments drawing
	 * each one. If the road is currently selected then the road will be drawn
	 * in red, else will be blue.
	 *
	 * @param g
	 *            The Graphics object to be drawn to
	 * @param offsetX
	 *            The current offset on the x-axis
	 * @param offsetY
	 *            The current offset on the y-axis
	 * @param zoom
	 *            The current zoom level of the map
	 */
	public void draw(Graphics2D g, double offsetX, double offsetY, double zoom) {
		g.setColor(Color.blue);
		if (this.selected) {
			g.setColor(Color.red);
		}
		for (Segment s : segments) {
			s.draw(g, offsetX, offsetY, zoom);
		}
	}

	/**
	 * Passes the collection of nodes so that the segments can identify the
	 * attached nodes and connect with them
	 *
	 * @param nodes
	 *            The collection of nodes
	 */
	public void connect(NodeArray nodes) {
		for (Segment s : segments) {
			s.connect(nodes);
		}
	}

	/**
	 * Returns a string representation of the road with the street name and the
	 * city
	 *
	 * @return The address of the street
	 */
	public String getName() {
		return this.roadName + ", " + this.city;
	}

	/**
	 * checks the road segments to see if the mouse click was on that segment
	 *
	 * @param x
	 *            MouseX coordinate
	 * @param y
	 *            MouseY coordinate
	 * @param offsetX
	 *            The current offset on the x-axis
	 * @param offsetY
	 *            the current offset on the y-axis
	 * @param zoom
	 *            The current zoom level
	 * @return The Road if it has been clicked on
	 */
	public Road on(int x, int y, double offsetX, double offsetY, double zoom) {
		for (Segment s : segments) {
			if (s.on(x, y, offsetX, offsetY, zoom)) {
				return this;
			}
		}
		return null;
	}

	/**
	 * Sets the selected value for the Road
	 *
	 * @param set
	 *            True if selected, false if not selected
	 */
	public void setSelect(boolean set) {
		selected = set;
	}

	/**
	 * Returns a string detailing the road
	 *
	 * @return Road details String
	 */
	public String getDetails() {
		String details = this.roadName + ", " + city;
		if (oneway) {
			details += "\nOneway Road";
		}
		if (notforcar) {
			details += "\nNo Car Access";
		}
		if (notforbicy) {
			details += "\nNo Bike Access";
		}
		if (notforpede) {
			details += "\nNo Pedestrian Access";
		}
		return details;
	}

	public boolean isOneWay(){
		return oneway;
	}

	public int getSpeed(){
		switch(speed){
		case 1:
			return 20;
		case 2:
			return 40;
		case 3:
			return 60;
		case 4:
			return 80;
		case 5:
			return 100;
		case 6:
			return 110;
		case 7:
			return 120;
		default:
			return 0;
		}
	}

}
