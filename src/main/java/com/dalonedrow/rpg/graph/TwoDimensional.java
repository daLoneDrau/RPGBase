package com.dalonedrow.rpg.graph;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.engine.sprite.base.SimpleVector2;

/**
 * A class representing a 2-dimensional point that is stored as an int.
 * @author drau
 *
 */
public abstract class TwoDimensional {
	/**
	 * Converts an integer value to a {@link SimplePoint}.
	 * @param val the integer
	 * @return {@link SimplePoint}
	 */
	public final SimplePoint convertIntToPoint(final int val) {
		final int sixteen = 16, shift = 0xffff;
		return new SimplePoint(val >>> sixteen, val & shift);
	}
	/**
	 * Converts an integer value to a {@link SimplePoint}.
	 * @param val the integer
	 * @return {@link SimpleVector2}
	 */
	public final SimpleVector2 convertIntToVector(final int val) {
		final int sixteen = 16, shift = 0xffff;
		return new SimpleVector2(val >>> sixteen, val & shift);
	}
	/**
	 * Converts a coordinate to an integer.
	 * @param x the x-coordinate value
	 * @param y the y-coordinate value
	 * @return <code>int</code>
	 */
	public final int convertPointToInt(final double x, final double y) {
		return convertPointToInt((float) x, (float) y);
	}
	/**
	 * Converts a coordinate to an integer.
	 * @param x the x-coordinate value
	 * @param y the y-coordinate value
	 * @return <code>int</code>
	 */
	public final int convertPointToInt(final float x, final float y) {
		final int sixteen = 16;
		int val = (int) x << sixteen;
		val += (int) y;
		return val;
	}
	/**
	 * Converts a coordinate to an integer.
	 * @param pt the coordinates
	 * @return <code>int</code>
	 */
	public final int convertPointToInt(final SimplePoint pt) {
		return this.convertPointToInt((float) pt.getX(), (float) pt.getY());
	}
	/**
	 * Converts a coordinate to an integer.
	 * @param pt the coordinates
	 * @return <code>int</code>
	 */
	public final int convertPointToInt(final SimpleVector2 pt) {
		return this.convertPointToInt((float) pt.getX(), (float) pt.getY());
	}
}
