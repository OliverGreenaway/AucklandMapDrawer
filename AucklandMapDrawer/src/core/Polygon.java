package core;

//TODO
//Add priority based on level from highest to lowest
//fix regex
//draw polygons
//add colours

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import util.DoublePoint;

/**
 * An object representing a polygon shape on the map
 *
 * @author Oliver Greenaway
 *
 */
public class Polygon {

	private Color texture;
	private List<DoublePoint> coordinates = new ArrayList<DoublePoint>();
	private List<List<DoublePoint>> cutoutPolygons = new ArrayList<List<DoublePoint>>();
	private int level = 1;

	/**
	 * Constructs a new polygon using the given list of details
	 *
	 * @param details
	 *            All details required to construct the polygon
	 */
	public Polygon(List<String> details) {
		boolean firstData = true;
		for (String s : details) {
			if (s.startsWith("Type")) {
				texture = getType(s.substring(5));
			} else if (s.startsWith("Data") && firstData) {
				coordinates = getCoordinates(s.substring(6));
				firstData = false;
			} else if (s.startsWith("Data")) {
				cutoutPolygons.add(getCoordinates(s.substring(6)));
			} else if (s.startsWith("EndLevel")) {
				level = Integer.parseInt(s.substring(9));
			}
		}
	}

	/**
	 * Draws the polgon to the graphics object
	 *
	 * @param g
	 *            The graphics object to be drawn to
	 * @param offsetX
	 *            The current offest on the x-axis
	 * @param offsetY
	 *            The current offest on the y-axis
	 * @param zoom
	 *            The current zoom level
	 */
	public void draw(Graphics2D g, double offsetX, double offsetY, double zoom) {
		java.awt.Polygon p = new java.awt.Polygon();
		for (DoublePoint c : coordinates) {
			p.addPoint((int) ((c.getX() + offsetX) * zoom),
					(int) ((c.getY() + offsetY) * zoom));
		}
		g.setColor(texture);
		g.fillPolygon(p);
	}

	/**
	 * Takes the hex code for the type of polygon and assigns it a colour.
	 *
	 * @param hex
	 *            The type on polygon
	 * @return A colour representation of the type
	 */
	private Color getType(String hex) {
		int type = Integer.decode(hex);
		if (type >= 1 && type <= 3) {
			return Color.lightGray;
		}
		if (type == 0xa) {
			return Color.orange;
		}
		if (type == 0xb) {
			return Color.orange;
		}
		if (type >= 7 && type <= 0xd) {
			return Color.gray;
		}
		if (type == 0xe || type == 0x13) {
			return Color.darkGray;
		}
		if (type == 0x1a) {
			return Color.gray;
		}
		if (type >= 0x14 && type <= 0x1f) {
			return new Color(188, 228, 181);
		}
		if (type >= 0x28 && type <= 0x49) {
			return new Color(170, 204, 255);
		}
		if (type == 0x50) {
			return new Color(188, 228, 181);
		}
		return null;
	}

	/**
	 * Takes a string of coordinates and converts them into a list of points
	 *
	 * @param coordinates
	 *            The string containing all coordinates
	 * @return A list of points
	 */
	private List<DoublePoint> getCoordinates(String coordinates) {
		List<DoublePoint> points = new ArrayList<DoublePoint>();
		coordinates = coordinates.replace(')', ' ');
		coordinates = coordinates.replace('(', ' ');
		String[] c = coordinates.split(",");
		for (int i = 0; i < c.length; i += 2) {
			double y = latToY(Double.parseDouble(c[i].trim()));
			double x = lonToX(Double.parseDouble(c[i + 1].trim()));

			points.add(new DoublePoint(x, y));
		}
		return points;
	}

	/**
	 * converts latitude into Y coordinates
	 *
	 * @param latitude
	 *            The degree of latitude
	 * @return The Y coordinate
	 */
	private double latToY(double latitude) {
		return Math.abs(latitude) * 111.0;
	}

	/**
	 * converts longitude into X coordinates
	 *
	 * @param longitude
	 *            The degree of longitude
	 * @return The X coordinate
	 */
	private double lonToX(double longitude) {
		return Math.abs(longitude) * 88.649;
	}
}
