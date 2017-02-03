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
public final class SimpleVector2 {
	/** the x-coordinate. */
	private double	x;
	/** the y-coordinate. */
	private double	y;

	/**
	 * Creates a new instance of SimpleVector2.java.
	 */
	public SimpleVector2() {
		x = 0;
		y = 0;
	}

	/**
	 * Creates a new instance of SimpleVector2.java.
	 * @param x1 the x-coordinate
	 * @param y1 the y-coordinate
	 */
	public SimpleVector2(final double x1, final double y1) {
		set(x1, y1);
	}

	/**
	 * Creates a new instance of SimpleVector2.java.
	 * @param x1 the x-coordinate
	 * @param y1 the y-coordinate
	 */
	public SimpleVector2(final int x1, final int y1) {
		set(x1, y1);
	}
    /**
     * Creates a new instance of SimpleVector2.java.
     * @param v the <code>SimpleVector2</code> to copy
     * @throws RPGException if the supplied vector is null
     */
    public SimpleVector2(final SimplePoint pt) throws RPGException {
        set(pt);
    }
    /**
     * Creates a new instance of SimpleVector2.java.
     * @param v the <code>SimpleVector2</code> to copy
     * @throws RPGException if the supplied vector is null
     */
    public SimpleVector2(final SimpleVector2 v) throws RPGException {
        set(v);
    }

	/**
	 * Creates a new instance of {@link SimpleVector2}.
	 * @param v3 a {@link SimpleVector3}
	 * @throws RPGException if the supplied {@link SimpleVector3} is null
	 */
	public SimpleVector2(final SimpleVector3 v3) throws RPGException {
		if (v3 == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector2 from null");
			throw ex;
		}
		x = v3.getX();
		y = v3.getY();
	}

	/**
	 * Creates a new instance of SimpleVector2.java.
	 * @param string the string representing the <code>SimpleVector2</code>
	 * @throws RPGException if the string is not in the expected format. format
	 *             should be "x,y,z" (for example, "1.3,2,4");
	 * @throws PooledException if there is an issue with the stringbuilder
	 */
	public SimpleVector2(final String string)
			throws RPGException, PooledException {
		if (string == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot create SimpleVector2 from null");
			throw ex;
		}
		String[] split = string.split(",");
		if (split.length != 2) {
			PooledStringBuilder msg =
					StringBuilderPool.getInstance().getStringBuilder();
			msg.append("Unable to create SimpleVector2 from text: ");
			msg.append(string);
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					msg.toString());
			msg.returnToPool();
			throw ex;
		}
		x = Double.parseDouble(split[0]);
		y = Double.parseDouble(split[1]);
	}

	/**
	 * Decrements the <code>SimpleVector2</code>.
	 * @param v the other <code>SimpleVector2</code> used to calculate the
	 *            decrement
	 * @throws RPGException if the supplied vector is null
	 */
	public void decrement(final SimpleVector2 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot decrement SimpleVector2 from null");
			throw ex;
		}
		x -= v.x;
		y -= v.y;
	}

	/**
	 * Calculates the distance between two <code>SimpleVector2</code>s.
	 * @param v the other <code>SimpleVector2</code> used to calculate the
	 *            distance
	 * @return double
	 * @throws RPGException if the supplied vector is null
	 */
	public double distance(final SimpleVector2 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot distance SimpleVector2 from null");
			throw ex;
		}
		return Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
	}

	/**
	 * Divides one vector by another.
	 * @param v the other <code>SimpleVector2</code> used to divide
	 * @throws RPGException if the supplied vector is null
	 */
	public void divide(final SimpleVector2 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot divide SimpleVector2 by null");
			throw ex;
		}
		x /= v.x;
		y /= v.y;
	}

	/**
	 * Gets the dot/scalar product: the difference between two directions.
	 * @param v the other <code>SimpleVector2</code>
	 * @return double
	 * @throws RPGException if the supplied vector is null
	 */
	public double dotProduct(final SimpleVector2 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot dot product SimpleVector2 with null");
			throw ex;
		}
		return x * v.x + y * v.y;
	}

	/**
	 * Indicates whether the supplied values are equal to this
	 * {@link SimpleVector2}'s location.
	 * @param x0 the x-coordinate
	 * @param y0 the y-coordinate
	 * @return true if the supplied coordinates are equal to the
	 *         {@link SimpleVector2}'s location; false otherwise
	 */
	public boolean equals(final double x0, final double y0) {
		boolean equals = false;
		final double variation = 0.0001f;
		equals =
				x0 - variation < x && x < x0 + variation && y0 - variation < y
						&& y < y0 + variation;
		return equals;
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
			if (obj instanceof SimpleVector2) {
				equals =
						((SimpleVector2) obj).x - variation < x
								&& x < ((SimpleVector2) obj).x + variation
								&& ((SimpleVector2) obj).y - variation < y
								&& y < ((SimpleVector2) obj).y + variation;
			} else if (obj instanceof SimplePoint) {
				equals =
						((SimplePoint) obj).getX() - variation < x
								&& x < ((SimplePoint) obj).getX() + variation
								&& ((SimplePoint) obj).getY() - variation < y
								&& y < ((SimplePoint) obj).getY() + variation;
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
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/**
	 * Increments the <code>SimpleVector2</code>.
	 * @param v the other <code>SimpleVector2</code> used to calculate the
	 *            increment
	 * @throws RPGException if the supplied vector is null
	 */
	public void increment(final SimpleVector2 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot increment SimpleVector2 with null");
			throw ex;
		}
		x += v.x;
		y += v.y;
	}

	/**
	 * Gets the distance from the origin.
	 * @return double
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Moves the <code>SimpleVector2</code> to by a certain amount.
	 * @param mx the distance moved along the x-axis
	 * @param my the distance moved along the y-axis
	 */
	public void move(final double mx, final double my) {
		x += mx;
		y += my;
	}
	/**
	 * Multiplies one vector by another.
	 * @param v the other <code>SimpleVector2</code> used to multipy
	 * @throws RPGException if the supplied vector is null
	 */
	public void multiply(final SimpleVector2 v) throws RPGException {
		if (v == null) {
			RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Cannot increment SimpleVector2 with null");
			throw ex;
		}
		x *= v.x;
		y *= v.y;
	}
	/**
	 * Calculates the normal angle of the <code>SimpleVector2</code>.
	 * @return {@link SimpleVector2}
	 */
	public SimpleVector2 normal() {
		double length;
		if (length() == 0) {
			length = 0;
		} else {
			length = 1 / length();
		}
		double nx = x * length;
		double ny = y * length;
		return new SimpleVector2(nx, ny);
	}
	/**
	 * Sets the <code>SimpleVector2</code> position.
	 * @param x1 the new position along the x-axis
	 * @param y1 the new position along the y-axis
	 */
	public void set(final double x1, final double y1) {
		x = x1;
		y = y1;
	}
    /**
     * Sets the <code>SimpleVector2</code> position to match another
     * <code>SimpleVector2</code>.
     * @param vector the <code>SimpleVector2</code> whose position is being
     *            matched
     * @throws RPGException
     */
    public void set(final SimpleVector2 vector) throws RPGException {
        if (vector == null) {
            RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Cannot increment SimpleVector2 with null");
            throw ex;
        }
        x = vector.x;
        y = vector.y;
    }
    /**
     * Sets the <code>SimpleVector2</code> position to match another
     * <code>SimpleVector2</code>.
     * @param vector the <code>SimpleVector2</code> whose position is being
     *            matched
     * @throws RPGException
     */
    public void set(final SimplePoint point) throws RPGException {
        if (point == null) {
            RPGException ex = new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Cannot set SimpleVector2 with null");
            throw ex;
        }
        x = point.getX();
        y = point.getY();
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = null;
		PooledStringBuilder text =
				StringBuilderPool.getInstance().getStringBuilder();
		try {
			text.append(getClass().getName());
			text.append("[x=");
			text.append(x);
			text.append(", y=");
			text.append(y);
			text.append("]");
		} catch (PooledException e) {
			e.printStackTrace();
		}
		s = text.toString();
		text.returnToPool();
		return s;
	}
}
