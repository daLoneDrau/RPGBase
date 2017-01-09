package com.dalonedrow.rpg.graph;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * A graph Vertex or Node, that also represents a point in 2-dimensional space.
 * @author DaLoneDrow
 */
public class PhysicalGraphNode extends GraphNode {
    /** the integer representing the node's map location. */
    private int location;
	/** utility class for storing and retrieving 2-dimensional information. */
    private final TwoDimensional locationStore;
	/**
	 * Creates a new instance of {@link PhysicalGraphNode}.
     * @param ind the cell's index
	 * @param x the cell's x-coordinates
	 * @param y the cell's y-coordinates
     * @throws RPGException if an error occurs
	 */
    public PhysicalGraphNode(final int ind, final int x, final int y)
            throws RPGException {
        super(ind);
		locationStore = new TwoDimensional() { };
        location = locationStore.convertPointToInt(x, y);
    }
	/**
	 * Creates a new instance of {@link PhysicalGraphNode}.
     * @param name the {@link PhysicalGraphNode}'s name
     * @param ind the cell's index
	 * @param x the cell's x-coordinates
	 * @param y the cell's y-coordinates
     * @throws RPGException if an error occurs
	 */
    public PhysicalGraphNode(final String name, final int ind, final int x,
    		final int y) throws RPGException {
        super(name, ind);
		locationStore = new TwoDimensional() { };
        location = locationStore.convertPointToInt(x, y);
    }
	/**
	 * Creates a new instance of {@link PhysicalGraphNode}.
     * @param name the {@link PhysicalGraphNode}'s name
     * @param ind the cell's index
	 * @param x the cell's x-coordinates
	 * @param y the cell's y-coordinates
     * @throws RPGException if an error occurs
	 */
    public PhysicalGraphNode(final char[] name, final int ind, final int x,
    		final int y) throws RPGException {
        super(name, ind);
		locationStore = new TwoDimensional() { };
        location = locationStore.convertPointToInt(x, y);
    }
    /**
     * Creates a new instance of {@link PhysicalGraphNode}.
     * @param ind the cell's index
	 * @param v the cell's coordinates
     * @throws RPGException if an error occurs
     */
    public PhysicalGraphNode(final int ind, final SimplePoint v)
            throws RPGException {
        this(ind, (int) v.getX(), (int) v.getY());
    }
    /**
     * Creates a new instance of {@link PhysicalGraphNode}.
     * @param name the {@link PhysicalGraphNode}'s name
     * @param ind the cell's index
	 * @param v the cell's coordinates
     * @throws RPGException if an error occurs
     */
    public PhysicalGraphNode(final String name, final int ind, 
    		final SimplePoint v)  throws RPGException {
        this(name, ind, (int) v.getX(), (int) v.getY());
    }
    /**
     * Creates a new instance of {@link PhysicalGraphNode}.
     * @param name the {@link PhysicalGraphNode}'s name
     * @param ind the cell's index
	 * @param v the cell's coordinates
     * @throws RPGException if an error occurs
     */
    public PhysicalGraphNode(final char[] name, final int ind, 
    		final SimplePoint v)  throws RPGException {
        this(name, ind, (int) v.getX(), (int) v.getY());
    }
    /**
     * Determines if this {@link PhysicalGraphNode} equals a specific set of 
     * coordinates.
	 * @param x the x-coordinates
	 * @param y the y-coordinates
     * @return <tt>true</tt> if the {@link PhysicalGraphNode} equals the 
     * coordinates; <tt>false</tt> otherwise
     */
    public final boolean equals(final float x, final float y) {
        return location == locationStore.convertPointToInt(x, y);
    }
    /**
     * Determines if this {@link PhysicalGraphNode} equals a specific set of 
     * coordinates.
	 * @param v the coordinates
     * @return <tt>true</tt> if the {@link PhysicalGraphNode} equals the 
     * coordinates; <tt>false</tt> otherwise
     */
    public final boolean equals(final SimplePoint v) {
        boolean equals = false;
        if (v != null) {
            equals = location == locationStore.convertPointToInt(v);
        }
        return equals;
    }
    /**
     * Gets the node's coordinates.
     * @return {@link SimplePoint}
     */
    public final SimplePoint getLocation() {
        return locationStore.convertIntToPoint(location);
    }
    /**
	 * Sets the value of the location.
	 * @param pt the cell's coordinates
	 */
	public final void setLocation(final SimplePoint pt) {
        location = locationStore.convertPointToInt(pt);
	}
    /**
	 * Sets the value of the location.
	 * @param x the cell's x-coordinates
	 * @param y the cell's y-coordinates
	 */
	public final void setLocation(final int x, final int y) {
        location = locationStore.convertPointToInt(x, y);
	}
}
