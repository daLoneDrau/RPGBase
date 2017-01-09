package com.dalonedrow.engine.systems.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class mimics the act of rolling one or more dice.
 * @author DaLoneDrau
 */
public final class Diceroller {
	/** the singleton instance. */
	private static Diceroller instance;
	/**
	 * Gets the one and only instance of {@link Diceroller}.
	 * @return {@link Diceroller}
	 */
	public static Diceroller getInstance() {
		if (Diceroller.instance == null) {
			Diceroller.instance = new Diceroller();
		}
		return Diceroller.instance;
	}
	/** the seed value. */
	private long			mySeed;
	/** the random number generator. */
	private final Random	random	= new Random(mySeed);
	/** A hidden constructor for this utility class. */
	private Diceroller() {
		super();
	}
	/** Checks to see if a new seed needs to be generated. */
	private void check() {
		if (mySeed != System.nanoTime()) {
			mySeed = System.nanoTime();
			random.setSeed(mySeed);
		}
	}
	/**
	 * Gets a random index from an array of chars.
	 * @param array and array of chars
	 * @return {@link char}
	 */
	public char getRandomIndex(final char[] array) {
		check();
		return array[Math.abs(random.nextInt() % array.length)];
	}
	/**
	 * Gets a random index from an array of ints.
	 * @param array an array of ints
	 * @return {@link int}
	 */
	public int getRandomIndex(final int[] array) {
		check();
		return array[Math.abs(random.nextInt() % array.length)];
	}
	/**
	 * Gets a random long value.
	 * @return {@link long}
	 */
	public long getRandomLong() {
		check();
		return random.nextLong();
	}
	/**
	 * Gets a random object from an {@link List} of {@link Object}s.
	 * @param list the {@link List} of {@link Object}s
	 * @return {@link Object}
	 */
	public Object getRandomObject(final List<Object> list) {
		check();
		return list.get(Math.abs(random.nextInt() % list.size()));
	}
	/**
	 * Gets a random object from a {@link Map} of {@link Object}s.
	 * @param map the {@link Map of {@link Object}s
	 * @return {@link Object}
	 */
	public Object getRandomObject(final Map<Object, Object> map) {
		check();
		Iterator<Object> iter = map.keySet().iterator();
		ArrayList<Object> list = new ArrayList<Object>();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return map.get(getRandomObject(list));
	}
	/**
	 * Gets a random object from an array of {@link Object}s.
	 * @param array the array of {@link Object}s
	 * @return {@link Object}
	 */
	public Object getRandomObject(final Object[] array) {
		check();
		return array[Math.abs(random.nextInt() % array.length)];
	}
	/**
	 * Removes a random object from a {@link List} of {@link Object}s.
	 * @param list the {@link List} of {@link Object}s
	 * @return {@link Object}
	 */
	public Object removeRandomObject(final List<Object> list) {
		check();
		Object o = null;
		if (!list.isEmpty()) {
			o = list.remove(Math.abs(random.nextInt() % list.size()));
		}
		return o;
	}
	/**
	 * Removes a random object from a {@link Map} of {@link Object}s.
	 * @param map the {@link Map} of {@link Object}s
	 * @return {@link Object}
	 */
	public Object removeRandomObject(final Map<Object, Object> map) {
		check();
		Object o = null;
		if (!map.isEmpty()) {
			Iterator<Object> iter = map.keySet().iterator();
			ArrayList<Object> list = new ArrayList<Object>();
			while (iter.hasNext()) {
				list.add(iter.next());
			}
			o = map.remove(getRandomObject(list));
		}
		return o;
	}
	/**
	 * Rolls an x-sided die.
	 * @param x the # of faces on the die
	 * @return {@link int}
	 */
	public int rolldX(final int x) {
		check();
		return Math.abs(random.nextInt() % x) + 1;
	}
	/**
	 * Rolls an x-sided die plus y offset. to roll from 3-8 (d6 + 2) x must be
	 * 6, y must be 2.
	 * @param x the # of faces on the die
	 * @param y the offset
	 * @return {@link int}
	 */
	public int rolldXPlusY(final int x, final int y) {
		check();
		return rolldX(x) + y;
	}
	/**
	 * Gets a positive number between 0.0 and 1.0.
	 * @return {@link float}
	 */
	public float rollPercent() {
		check();
		return random.nextFloat();
	}
	/**
	 * Gets a y-die roll x number of times. To get a 6d6 roll, x and y must be
	 * 6. To get a 3d6 roll, x must be 3, y must be 6.
	 * @param x the number of time to roll the die
	 * @param y the # of faces on the die
	 * @return {@link int}
	 */
	public int rollXdY(final int x, final int y) {
		int result = 0;
		for (int i = 0; i < x; i++) {
			result += rolldX(y);
		}
		return result;
	}
}
