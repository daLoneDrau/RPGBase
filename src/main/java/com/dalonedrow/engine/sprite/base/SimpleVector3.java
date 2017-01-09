/**
 *
 */
package com.dalonedrow.engine.sprite.base;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * Simple Vector class, implemented before the switch to libGDX.
 * @author DaLoneDrow
 */
public final class SimpleVector3 {
	/** the number of coordinates needed to create a new vector. */
	private static final int	NUM_COORDINATES	= 3;
	/** the x-coordinate. */
	private double				x;
	/** the y-coordinate. */
	private double				y;
	/** the x-coordinate. */
	private double				z;
	/**
	 * Creates a new instance of Vector3.java.
	 */
	public SimpleVector3() {
		x = 0;
		y = 0;
		z = 0;
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param x1 the x-coordinate
	 * @param y1 the y-coordinate
	 * @param z1 the z-coordinate
	 */
	public SimpleVector3(final double x1, final double y1, final double z1) {
		set(x1, y1, z1);
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param x1 the x-coordinate
	 * @param y1 the y-coordinate
	 * @param z1 the z-coordinate
	 */
	public SimpleVector3(final int x1, final int y1, final int z1) {
		set(x1, y1, z1);
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param v the <code>Vector2</code> to copy
	 */
	public SimpleVector3(final SimpleVector2 v) {
		set(v.getX(), v.getY(), 0);
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param v the <code>Vector2</code> to copy
	 * @param z1 the z-coordinate
	 */
	public SimpleVector3(final SimpleVector2 v, final double z1) {
		set(v.getX(), v.getY(), z1);
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param v the <code>Vector2</code> to copy
	 * @param z1 the z-coordinate
	 */
	public SimpleVector3(final SimpleVector2 v, final int z1) {
		set(v.getX(), v.getY(), z1);
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param v the <code>Vector3</code> to copy
	 * @throws RPGException
	 */
	public SimpleVector3(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		set(v);
	}
	/**
	 * Creates a new instance of Vector3.java.
	 * @param string the string representing the <code>Vector3</code>
	 * @throws PooledException
	 * @throws Exception if the string is not in the expected format. format
	 *             should be "x,y,z" (for example, "1.3,2,4");
	 */
	public SimpleVector3(final String string)
			throws RPGException, PooledException {
		if (string == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		String[] split = string.split(",");
		if (split.length != NUM_COORDINATES) {
			PooledStringBuilder msg =
					StringBuilderPool.getInstance().getStringBuilder();
			msg.append("Unable to create Vector3 from text: ");
			msg.append(string);
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					msg.toString());
			msg.returnToPool();
			throw ex;
		}
		x = Double.parseDouble(split[0]);
		y = Double.parseDouble(split[1]);
		z = Double.parseDouble(split[2]);
	}
	/**
	 * Cross product - used to calculate the normal.
	 * @param v the other <code>Vector3</code>
	 * @return {@link SimpleVector3}
	 */
	public SimpleVector3 crossProduct(final SimpleVector3 v) {
		double nx = y * v.z - z * v.y;
		double ny = z * v.y - x * v.z;
		double nz = x * v.y - y * v.x;
		return new SimpleVector3(nx, ny, nz);
	}
	/**
	 * Decrements the <code>Vector3</code>.
	 * @param v the other <code>Vector3</code> used to calculate the decrement
	 * @throws RPGException
	 */
	public void decrement(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot decrement SimpleVector3 from null");
			throw ex;
		}
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	/**
	 * Calculates the distance between two <code>Vector3</code>s.
	 * @param v the other <code>Vector3</code> used to calculate the distance
	 * @return double
	 * @throws RPGException
	 */
	public double distance(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		return Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
	}
	/**
	 * Divides one vector by another.
	 * @param v the other <code>Vector3</code> used to divide
	 * @throws RPGException
	 */
	public void divide(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		x /= v.x;
		y /= v.y;
		z /= v.z;
	}
	/**
	 * Gets the dot/scalar product: the difference between two directions.
	 * @param v the other <code>Vector3</code>
	 * @return double
	 * @throws RPGException
	 */
	public double dotProduct(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		return x * v.x + y * v.y + z * v.z;
	}
	/**
	 * Indicates whether the supplied values are equal to this
	 * {@link SimpleVector3}'s location.
	 * @param x0 the x-coordinate
	 * @param y0 the y-coordinate
	 * @param z0 the z-coordinate
	 * @return true if the supplied coordinates are equal to the
	 *         {@link SimpleVector3}'s location; false otherwise
	 */
	public boolean equals(final double x0, final double y0, final double z0) {
		boolean isEqual = false;
		final double variation = 0.0001f;
		isEqual = x0 - variation < x
				&& x < x0 + variation
				&& y0 - variation < y
				&& y < y0 + variation
				&& z0 - variation < z
				&& z < z0 + variation;
		return isEqual;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean equals = false;
		if (this == obj) {
			equals = true;
		} else if (obj != null) {
			final double variation = 0.0001f;
			if (obj instanceof SimpleVector3) {
				equals = ((SimpleVector3) obj).x - variation < x
						&& x < ((SimpleVector3) obj).x + variation
						&& ((SimpleVector3) obj).y - variation < y
						&& y < ((SimpleVector3) obj).y + variation
						&& ((SimpleVector3) obj).z - variation < z
						&& z < ((SimpleVector3) obj).z + variation;
			}
		}
		return equals;
	}
	/**
	 * Gets the x.
	 * @return double
	 */
	public double getX() {
		return x;
	}
	/**
	 * Gets the y.
	 * @return double
	 */
	public double getY() {
		return y;
	}
	/**
	 * Gets the z.
	 * @return double
	 */
	public double getZ() {
		return z;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	/**
	 * Increments the <code>Vector3</code>.
	 * @param v the other <code>Vector3</code> used to calculate the increment
	 * @throws RPGException
	 */
	public void increment(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot increment SimpleVector3 from null");
			throw ex;
		}
		x += v.x;
		y += v.y;
		z += v.z;
	}
	/**
	 * Gets the distance from the origin.
	 * @return double
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	/**
	 * Moves the <code>Vector3</code> to by a certain amount.
	 * @param mx the distance moved along the x-axis
	 * @param my the distance moved along the y-axis
	 * @param mz the distance moved along the z-axis
	 */
	public void move(final double mx, final double my, final double mz) {
		x += mx;
		y += my;
		z += mz;
	}
	/**
	 * Multiplies one vector by another.
	 * @param v the other <code>Vector3</code> used to multipy
	 * @throws RPGException
	 */
	public void multiply(final SimpleVector3 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}
	/**
	 * Calculates the normal angle of the <code>Vector3</code>.
	 * @return {@link SimpleVector3}
	 */
	public SimpleVector3 normal() {
		double length;
		if (length() == 0) {
			length = 0;
		} else {
			length = 1 / length();
		}
		double nx = x * length;
		double ny = y * length;
		double nz = z * length;
		return new SimpleVector3(nx, ny, nz);
	}
	/**
	 * Sets the <code>Vector3</code> position.
	 * @param x1 the new position along the x-axis
	 * @param y1 the new position along the y-axis
	 * @param z1 the new position along the z-axis
	 */
	public void set(final double x1, final double y1, final double z1) {
		x = x1;
		y = y1;
		z = z1;
	}
	/**
	 * Sets the <code>Vector3</code> position to match another
	 * <code>Vector3</code>.
	 * @param vector the <code>Vector3</code> whose position is being matched.
	 * @throws RPGException
	 */
	public void set(final SimpleVector3 vector) throws RPGException {
		if (vector == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector3 from null");
			throw ex;
		}
		x = vector.x;
		y = vector.y;
		z = vector.z;
	}
	/**
	 * Sets the position along the x-axis.
	 * @param v the double to set
	 */
	public void setX(final double v) {
		x = v;
	}
	/**
	 * Sets the position along the y-axis.
	 * @param v the double to set
	 */
	public void setY(final double v) {
		y = v;
	}
	/**
	 * Sets the position along the z-axis.
	 * @param v the double to set
	 */
	public void setZ(final double v) {
		z = v;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = null;
		PooledStringBuilder text =
				StringBuilderPool.getInstance().getStringBuilder();
		try {
			text.append("[x=");
			text.append(x);
			text.append(", y=");
			text.append(y);
			text.append(", z=");
			text.append(z);
			text.append("]");
		} catch (PooledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s = text.toString();
		text.returnToPool();
		return s;
	}
}
