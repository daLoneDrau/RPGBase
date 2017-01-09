package com.dalonedrow.rpg.graph;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.engine.sprite.base.SimpleVector3;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author drau
 */
public class Hexagon {
	/** hexagons have 6 corners; each corner is shared by two other hexagons. */
	private final int[][]	corners;
	/** hexagons have 6 edges; each edge is shared by another hexagon. */
	private final int[]		edges;
	/** the hexagon's orientation; flat or pointed on top. */
	private final boolean	flat;
	/** the hexagon's height. */
	private float			height;
	/** the horizontal distance between adjacent hexes. */
	private float			horizontalDistance;
	/** each hexagon has a unique id. */
	private final int		id;
	/** constants. */
	private final int		sixty	= 60, oneEighty = 180;
	/** the distance between a hexagon's center point and a corner. */
	private float			size;
	/** constants. */
	private final int		three	= 3, four = 4, six = 6, thirty = 30;
	/** the vertical distance between adjacent hexes. */
	private float			verticalDistance;
	/** the hexagon's width. */
	private float			width;
	/** cube coordinates. */
	private int				x, y, z;
	/**
	 * Creates a new instance of {@link Hexagon}.
	 * @param isFlat flag indicating whether the hexagon is flat on top or 
	 * pointy
	 * @param refId the hexagon's reference id
	 */
	public Hexagon(final boolean isFlat, final int refId) {
		this(isFlat, refId, 0);
	}
	/**
	 * Creates a new instance of {@link Hexagon}.
	 * @param isFlat flag indicating whether the hexagon is flat on top or
	 * pointy
	 * @param refId the hexagon's reference id
	 * @param newSize the hexagon's new size
	 */
	public Hexagon(final boolean isFlat, final int refId, final float newSize) {
		id = refId;
		edges = new int[] { -1, -1, -1, -1, -1, -1 };
		corners =
				new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 },
						{ -1, -1 }, { -1, -1 } };
		flat = isFlat;
		if (newSize > 0) {
			setSize(newSize);
		}
	}
	/**
	 * Creates a new instance of {@link Hexagon}.
	 * @param refId the hexagon's reference id
	 */
	public Hexagon(final int refId) {
		this(true, refId, 0);
	}
	/**
	 * Creates a new instance of {@link Hexagon}.
	 * @param refId the hexagon's reference id
	 * @param newSize the hexagon's new size
	 */
	public Hexagon(final int refId, final float newSize) {
		this(true, refId, newSize);
	}
	/**
	 * Makes this {@link Hexagon} a copy of a specific {@link Hexagon}.
	 * @param hex the {@link Hexagon} being copied
	 */
	public void copyOf(final Hexagon hex) {
		size = hex.size;
		x = hex.x;
		y = hex.y;
		z = hex.z;
	}
	/**
	 * Determines if this {@link Hexagon} is equal to the supplied coordinates.
	 * @param x1 the x-coordinate
	 * @param y1 the y-coordinate
	 * @param z1 the z-coordinate
	 * @return <tt>true</tt> if the coordinates match this instance;
	 *         <tt>false</tt> otherwise
	 */
	public final boolean equals(final int x1, final int y1, final int z1) {
		boolean equals = false;
		if (x1 == x && y1 == y && z1 == z) {
			equals = true;
		}
		return equals;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean equals = false;
		if (obj instanceof Hexagon && ((Hexagon) obj).x == x
				&& ((Hexagon) obj).y == y && ((Hexagon) obj).z == z) {
			equals = true;
		} else if (obj instanceof SimpleVector3
				&& (int) ((SimpleVector3) obj).getX() == x
				&& (int) ((SimpleVector3) obj).getY() == y
				&& (int) ((SimpleVector3) obj).getZ() == z) {
			equals = true;
		}
		return equals;
	}
	public final String getCubeCoordinatesArt() {
		String s = null;
		try {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			// sb.append(" _ _ ");
			sb.append("/");
			sb.append(y);
			int len = Integer.toString(y).length();
			for (int i = "     ".length() - len; i > 0; i--) {
				sb.append(' ');
			}
			sb.append("\\");
			sb.append('\n');
			sb.append("/");
			len = Integer.toString(x).length();
			for (int i = "       ".length() - len; i > 0; i--) {
				sb.append(' ');
			}
			sb.append(x);
			sb.append("\\");
			sb.append('\n');
			sb.append("\\");
			sb.append(z);
			len = Integer.toString(z).length();
			for (int i = "       ".length() - len; i > 0; i--) {
				sb.append(' ');
			}
			sb.append("/");
			sb.append('\n');
			sb.append("\\ _ _ /");
			s = sb.toString();
			sb.returnToPool();
			sb = null;
		} catch (PooledException ex) {
			ex.printStackTrace();
		}
		return s;
	}
	public final int getCubeCoordinatesArtHeight() {
		return 4;
	}
	public final int getCubeCoordinatesArtWidth() {
		return 8;
	}
	/**
	 * Gets the hexagon's height.
	 * @return <tt>float</tt>
	 */
	public final float getHeight() {
		return height;
	}
	/**
	 * Gets the position (vertex) of a specific corner of a hexagon.
	 * @param center the hexagon's center point.
	 * @param cornerId the corner id
	 * @param newSize the hexagon's new size
	 * @return {@link SimplePoint}
	 * @throws RPGException if the size was never declared
	 */
	public final SimplePoint getHexCornerVertex(final SimplePoint center,
			final float newSize, final int cornerId) throws RPGException {
		setSize(newSize);
		return getHexCornerVertex(center, cornerId);
	}
	/**
	 * Gets the position (vertex) of a specific corner of a hexagon.
	 * @param center the hexagon's center point.
	 * @param cornerId the corner id
	 * @return {@link SimplePoint}
	 * @throws RPGException if the size was never declared
	 */
	public final SimplePoint getHexCornerVertex(final SimplePoint center,
			final int cornerId) throws RPGException {
		if (size <= 0) {
			throw new RPGException(
					ErrorMessage.INTERNAL_ERROR, "Size was never set!");
		}
		int offset = 0;
		if (!flat) {
			offset = thirty;
		}
		float angleDeg = sixty * cornerId + offset;
		float angleRad = (float) (Math.PI / oneEighty * angleDeg);
		return new SimplePoint(center.getX() + size * Math.cos(angleRad),
				center.getY() + size * Math.sin(angleRad));
	}
	/**
	 * Gets the horizontal distance between adjacent hexes.
	 * @return <tt>float</tt>
	 */
	public final float getHorizontalDistance() {
		return horizontalDistance;
	}
	public int getId() {
		return id;
	}
	public final String getOffsetCoordinatesArt(final HexCoordinateSystem grid)
			throws PooledException, RPGException {
		SimplePoint pt = grid.getOffsetCoordinates(this);
		int x1 = (int) pt.getX();
		int y1 = (int) pt.getY();
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		// sb.append(" _ _ ");
		sb.append("/     \\");
		sb.append('\n');
		sb.append("/");
		int len = Integer.toString(x1).length();
		len++;
		len += Integer.toString(y1).length();
		int off = "       ".length() - len;
		int lef = off / 2;
		for (int i = lef; i > 0; i--) {
			sb.append(' ');
		}
		sb.append(x1);
		sb.append(",");
		sb.append(y1);
		for (int i = off - lef; i > 0; i--) {
			sb.append(' ');
		}
		sb.append("\\");
		sb.append('\n');
		sb.append("\\       /");
		sb.append('\n');
		sb.append("\\ _ _ /");
		String s = sb.toString();
		sb.returnToPool();
		sb = null;
		return s;
	}
	/**
	 * Gets the distance between a hexagon's center point and a corner.
	 * @return <tt>float</tt>
	 */
	public final float getSize() {
		return size;
	}
	/**
	 * Gets the {@link Hexagon}'s position.
	 * @return {@link SimpleVector3}
	 */
	public SimpleVector3 getVector() {
		return new SimpleVector3(x, y, z);
	}
	/**
	 * Gets the vertical distance between adjacent hexes.
	 * @return <tt>float</tt>
	 */
	public final float getVerticalDistance() {
		return verticalDistance;
	}
	/**
	 * Gets the hexagon's width.
	 * @return <tt>float</tt>
	 */
	public final float getWidth() {
		return width;
	}
	/**
	 * Gets the value of the x coordinate.
	 * @return {@link int}
	 */
	public final int getX() {
		return x;
	}
	/**
	 * Gets the value of the y coordinate.
	 * @return {@link int}
	 */
	public final int getY() {
		return y;
	}
	/**
	 * Gets the value of the z coordinate.
	 * @return {@link int}
	 */
	public final int getZ() {
		return z;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}
	/**
	 * Gets the value of the flat.
	 * @return {@link boolean}
	 */
	public boolean isFlat() {
		return flat;
	}
	/**
	 * Rotates the hexagon 60 degrees to the right.
	 * @throws RPGException if an error occurs
	 */
	public void rotate() throws RPGException {
		int oldx = x, oldy = y, oldz = z;
		x = -oldz;
		y = -oldx;
		z = -oldy;
	}
	/**
	 * Sets the {@link Hexagon}'s cube coordinates.
	 * @param newX the x-coordinate
	 * @param newY the y-coordinate
	 * @param newZ the z-coordinate
	 */
	public final void setCoordinates(final int newX, final int newY,
			final int newZ) {
		x = newX;
		y = newY;
		z = newZ;
	}
	/**
	 * Sets the {@link Hexagon}'s cube coordinates.
	 * @param v3 the new coordinates
	 */
	public final void setCoordinates(final SimpleVector3 v3) {
		x = (int) v3.getX();
		y = (int) v3.getY();
		z = (int) v3.getZ();
	}
	/**
	 * Sets the distance between a hexagon's center point and a corner.
	 * @param newSize the hexagon's new size
	 */
	public final void setSize(final float newSize) {
		size = newSize;
		if (flat) {
			width = size * 2;
			horizontalDistance = width * three / four;
			height = (float) (Math.sqrt(three) / 2 * width);
			verticalDistance = height;
		} else {
			height = size * 2;
			verticalDistance = height * three / four;
			width = (float) (Math.sqrt(three) / 2 * height);
			horizontalDistance = width;
		}
	}
	public final String toCubeCoordinateString() {
		return new SimpleVector3(x, y, z).toString();
	}
}
