package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;

import util.NodeArray;
import util.RoadArray;
import util.TrieTree;

/**
 * A Map contains information on all objects that are used to construct and draw
 * a map
 *
 * @author Oliver Greenaway
 *
 */
public class Map {

	private double offsetX, offsetY;
	private double zoomLevel;
	private double diffX, diffY, minX, minY;
	private String directory;
	private NodeArray nodes = new NodeArray();
	private RoadArray roads = new RoadArray();
	private List<Polygon> polygons = new ArrayList<Polygon>();
	private boolean polygonsExist;
	private Road selectedRoad;
	private TrieTree roadNames = new TrieTree();

	/**
	 * Initializes all Nodes, Roads and Segments into a graphed map using the
	 * information contained in the directory
	 *
	 * @param dir
	 *            The directory containing the data files
	 */
	public Map(String dir) {
		directory = dir;
		zoomLevel = 2;
		polygonsExist = true;
		initNodes();
		initRoads();
		initSegments();
		initPolygons();
		buildGraph();
	}

	/**
	 * Reads all data from files and creates Node objects
	 */
	public void initNodes() {
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		try {
			File file = new File(directory + "nodeID-lat-lon.tab");
			if (file.exists()) {
				Scanner scan = new Scanner(file);
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					String[] columns = line.split("\t");
					int ID = Integer.parseInt(columns[0]);
					double y = latToY(Double.parseDouble(columns[1]));
					double x = lonToX(Double.parseDouble(columns[2]));
					if (x < minX) {
						minX = x;
					}
					if (x > maxX) {
						maxX = x;
					}
					if (y < minY) {
						minY = y;
					}
					if (y > maxY) {
						maxY = y;
					}
					nodes.add(new Node(ID, x, y));
				}
				nodes.sort();
				scan.close();
				diffX = maxX - minX;
				diffY = maxY - minY;
				this.minX = minX;
				this.minY = minY;
				offsetX = (0.0 - minX);
				offsetY = (0.0 - minY);
				zoomLevel = 800 / diffX;
			} else {
				Mapper.textArea.setText("File Read Error\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads all data from files and creates Road objects
	 */
	public void initRoads() {
		try {
			File file = new File(directory + "roadID-roadInfo.tab");
			if (file.exists()) {
				Scanner scan = new Scanner(file);
				scan.nextLine();
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					String[] columns = line.split("\t");
					roads.add(new Road(toInt(columns[0]), toInt(columns[1]),
							columns[2], columns[3], toInt(columns[4]),
							toInt(columns[5]), toInt(columns[6]),
							toInt(columns[7]), toInt(columns[8]),
							toInt(columns[9])));
				}
				roads.sort();
				scan.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Road r : roads) {
			roadNames.add(r);
		}
	}

	/**
	 * Reads all data from files and creates Segment objects linking them to
	 * roads
	 */
	public void initSegments() {
		try {
			File file = new File(directory
					+ "roadSeg-roadID-length-nodeID-nodeID-coords.tab");
			if (file.exists()) {
				Scanner scan = new Scanner(file);
				scan.nextLine();
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					String[] columns = line.split("\t");
					Road road = roads.get(Integer.parseInt(columns[0]));
					if (road != null) {
						Segment s = new Segment(Double.parseDouble(columns[1]),
								Integer.parseInt(columns[2]),
								Integer.parseInt(columns[3]));
						for (int i = 4; i < columns.length; i += 2) {
							double x = lonToX(Double
									.parseDouble(columns[i + 1]));
							double y = latToY(Double.parseDouble(columns[i]));
							s.addPoint(x, y);
						}
						road.addSegment(s);
					} else {
						System.err.println(columns[0] + " not present");
					}
				}
				scan.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads data from the polygon file if one exists, if one one does not exit
	 * then this is flagged so that polygons are marked as not present
	 */
	public void initPolygons() {
		try {
			File file = new File(directory + "polygon-shapes.mp");
			if (file.exists()) {
				Scanner scan = new Scanner(file);
				while (scan.hasNextLine()) {
					List<String> details = new ArrayList<String>();
					details.add(scan.nextLine());
					while (!details.get(details.size() - 1).equals("[END]")
							&& scan.hasNextLine()) {
						details.add(scan.nextLine());
					}
					polygons.add(new Polygon(details));
				}
				scan.close();
			} else {
				this.polygonsExist = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Links all nodes with roads and segments
	 */
	public void buildGraph() {
		for (Road r : roads) {
			r.connect(nodes);
		}
	}

	/**
	 * Draws all the map components to the graphics object
	 *
	 * @param g
	 *            The graphics object to be drawn to
	 */
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect((int) ((minX + offsetX) * zoomLevel),
				(int) ((minY + offsetY) * zoomLevel),
				(int) (diffX * zoomLevel), (int) (diffY * zoomLevel));
		if (polygonsExist) {
			for (Polygon p : polygons) {
				p.draw(g, offsetX, offsetY, zoomLevel);
			}
		}
		for (Road r : roads) {
			r.draw(g, offsetX, offsetY, zoomLevel);
		}

		for (Node n : nodes) {
			n.draw(g, offsetX, offsetY, zoomLevel);
		}
		if (selectedRoad != null) {
			Mapper.textArea.setText("Road Details:\n"
					+ selectedRoad.getDetails());
		} else {
			Mapper.textArea.setText("");
		}
	}

	/**
	 * Adjust the offset of the map by the given x and y positions
	 *
	 * @param x
	 *            The distance to move in the x direction
	 * @param y
	 *            The distance to move in the y direction
	 */
	public void moveMap(double x, double y) {
		offsetX -= x / zoomLevel;
		offsetY -= y / zoomLevel;
	}

	/**
	 * Adjust the zoom level by 0.5 per notch rotated
	 *
	 * @param notches
	 *            The number of mouse wheel notches moved
	 */
	public void zoom(int notches) {
		zoomLevel += (double) notches / 2;
		if (zoomLevel < 1) {
			zoomLevel = 1;
		}
	}

	/**
	 * Checks all roads to see if the click occured within the bondries of the
	 * road, if a road has been clicked then the road is marked as selected
	 *
	 * @param x
	 *            The mouseX coordinate
	 * @param y
	 *            The mouseY coordinate
	 */
	public void clickedRoad(int x, int y) {
		for (Road r : roads) {
			Road road = r.on(x, y, offsetX, offsetY, zoomLevel);
			if (road != null) {
				if (selectedRoad != null) {
					selectedRoad.setSelect(false);
				}
				selectedRoad = road;
				road.setSelect(true);
				return;
			}
		}
		if (selectedRoad != null) {
			selectedRoad.setSelect(false);
		}
		selectedRoad = null;
	}

	/**
	 * Returns the 10 closest road names to the given string
	 *
	 * @param text
	 *            the string to be searched for
	 * @return A list of at most 10 roads
	 */
	public List<Road> getTen(String text) {
		return roadNames.getTen(text);
	}

	/**
	 * Deselects any currently selected roads and selects the given road
	 *
	 * @param r
	 *            the road to be selected
	 */
	public void setSelectedRoad(Road r) {
		if (selectedRoad != null) {
			selectedRoad.setSelect(false);
		}
		selectedRoad = r;
		if (selectedRoad != null) {
			selectedRoad.setSelect(true);
		}
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

	/**
	 * converts a string to an int
	 *
	 * @param s
	 *            The string to be converted
	 * @return The int representation of the string, 0 is a conversion error is
	 *         encountered
	 */
	private int toInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

}
