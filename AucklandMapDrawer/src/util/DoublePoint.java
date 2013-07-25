package util;

/**
 * An object that stores an (x,y) coordinate with the double number format
 * 
 * @author Oliver Greenaway
 * 
 */
public class DoublePoint {
	private double x, y;

	/**
	 * Construct a point with the given coordinates
	 * 
	 * @param x
	 *            The X coordinate
	 * @param y
	 *            The Y coordinate
	 */
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the stored X coordinate
	 * 
	 * @return The X coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Returns the stored Y coordinate
	 * 
	 * @return The Y Coordinate
	 */
	public double getY() {
		return this.y;
	}
}
