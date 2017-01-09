/**
 *
 */
package com.dalonedrow.engine.sprite.base;

import com.dalonedrow.rpg.graph.TwoDimensional;

/**
 * Substitute class for Point.
 * @author Donald
 */
public final class SimplePoint {
	/**
	 * The X coordinate of this <code>SimplePoint</code>. If no X coordinate is
	 * set it will default to 0.
	 */
	private int	x;
	/**
	 * The Y coordinate of this <code>SimplePoint</code>. If no Y coordinate is
	 * set it will default to 0.
	 */
	private int	y;
	/**
	 * Constructs and initializes a SimplePoint at the origin (0,&nbsp;0) of the
	 * coordinate space.
	 */
	public SimplePoint() {
		this(0, 0);
	}
	/**
	 * Constructs and initializes a SimplePoint at the specified {@code (x,y)}
	 * location in the coordinate space.
	 * @param x0 the X coordinate of the newly constructed
	 *            <code>SimplePoint</code>
	 * @param y0 the Y coordinate of the newly constructed
	 *            <code>SimplePoint</code>
	 */
	public SimplePoint(final double x0, final double y0) {
		super();
		x = (int) x0;
		y = (int) y0;
	}
	/**
	 * Constructs and initializes a SimplePoint at the specified {@code (x,y)}
	 * location in the coordinate space.
	 * @param x0 the X coordinate of the newly constructed
	 *            <code>SimplePoint</code>
	 * @param y0 the Y coordinate of the newly constructed
	 *            <code>SimplePoint</code>
	 */
	public SimplePoint(final int x0, final int y0) {
		x = x0;
		y = y0;
	}
	/**
	 * Constructs and initializes a SimplePoint with the same location as the
	 * specified <code>SimplePoint</code> object.
	 * @param p a SimplePoint
	 */
	public SimplePoint(final SimplePoint p) {
		this(p.x, p.y);
	}
	/**
	 * Constructs and initializes a SimplePoint with the same location as the
	 * specified <code>SimpleVector2</code> object.
	 * @param v2 a {@link SimpleVector2}
	 */
	public SimplePoint(final SimpleVector2 v2) {
		this(v2.getX(), v2.getY());
	}
	/**
	 * Determines whether or not two points are equal. Two instances of
	 * <code>SimplePoint</code> are equal if the values of their <code>x</code>
	 * and <code>y</code> member fields, representing their position in the
	 * coordinate space, are the same.
	 * @param x0 an x value to be compared with this <code>SimplePoint</code>
	 * @param y0 a y value to be compared with this <code>SimplePoint</code>
	 * @return <code>true</code> if the object to be compared is an instance of
	 *         <code>SimplePoint</code> and has the same values;
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(final int x0, final int y0) {
		boolean equals = false;
		if (x == x0 && y == y0) {
			equals = true;
		}
		return equals;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals()
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean equals = false;
		if (this == obj) {
			equals = true;
		} else if (obj != null) {
			if (obj instanceof SimplePoint) {
				if (((SimplePoint) obj).x == x
						&& ((SimplePoint) obj).y == y) {
					equals = true;
				}
			} else if (obj instanceof SimpleVector2) {
				if ((int) ((SimpleVector2) obj).getX() == x
						&& (int) ((SimpleVector2) obj).getY() == y) {
					equals = true;
				}
			}
		}
		return equals;
	}
	/**
	 * Returns the X coordinate of this <code>SimplePoint</code> in
	 * <code>double</code> precision.
	 * @return the X coordinate of this <code>SimplePoint</code>.
	 */
	public double getX() {
		return x;
	}
	/**
	 * Returns the Y coordinate of this <code>SimplePoint</code> in
	 * <code>double</code> precision.
	 * @return the Y coordinate of this <code>SimplePoint</code>.
	 */
	public double getY() {
		return y;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		TwoDimensional dd = new TwoDimensional() { };
		int hash = dd.convertPointToInt(this);
		dd = null;
		return hash;
	}
	/**
	 * Moves this point to the specified location in the {@code (x,y)}
	 * coordinate plane. This method is identical with
	 * <code>setLocation(int,&nbsp;int)</code>.
	 * @param newX the X coordinate of the new location
	 * @param newY the Y coordinate of the new location
	 */
	public void move(final double newX, final double newY) {
		move((int) newX, (int) newY);
	}
	/**
	 * Moves this point to the specified location in the {@code (x,y)}
	 * coordinate plane. This method is identical with
	 * <code>setLocation(int,&nbsp;int)</code>.
	 * @param newX the X coordinate of the new location
	 * @param newY the Y coordinate of the new location
	 */
	public void move(final int newX, final int newY) {
		x = newX;
		y = newY;
	}
	/**
	 * Moves this point to the specified location in the {@code (x,y)}
	 * coordinate plane. This method is identical with
	 * <code>setLocation(int,&nbsp;int)</code>.
	 * @param pt the new location
	 */
	public void move(final SimplePoint pt) {
		move(pt.getX(), pt.getY());
	}
	/**
	 * Sets the x.
	 * @param val the x to set
	 */
	public void setX(final double val) {
		x = (int) val;
	}
	/**
	 * Sets the x.
	 * @param val the x to set
	 */
	public void setX(final int val) {
		x = val;
	}
	/**
	 * Sets the y.
	 * @param val the y to set
	 */
	public void setY(final double val) {
		y = (int) val;
	}
	/**
	 * Sets the y.
	 * @param val the y to set
	 */
	public void setY(final int val) {
		y = val;
	}
	/**
	 * Returns a string representation of this point and its location in the
	 * {@code (x,y)} coordinate space. This method is intended to be used only
	 * for debugging purposes, and the content and format of the returned string
	 * may vary between implementations. The returned string may be empty but
	 * may not be <code>null</code>.
	 * @return a string representation of this point
	 */
	@Override
	public String toString() {
		return "[x=" + x + ",y=" + y + "]";
	}
}
