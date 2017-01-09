package com.dalonedrow.utils;

import java.util.Arrays;

/**
 * Array utilities class.
 * @author drau
 */
public final class ArrayUtilities {
	/** the singLeton instance of {@link ArrayUtilities}. */
	private static ArrayUtilities instance;
	/**
	 * Gets the one and only instance of {@link ArrayUtilities}.
	 * @return {@link ArrayUtilities}
	 */
	public static ArrayUtilities getInstance() {
		if (ArrayUtilities.instance == null) {
			ArrayUtilities.instance = new ArrayUtilities();
		}
		return ArrayUtilities.instance;
	}
	/** Hidden constructor. */
	private ArrayUtilities() {
	}
	/**
	 * Extends an array, adding a new element to the last index.
	 * @param element the new element
	 * @param src the source array
	 * @return <code>boolean</code>[]
	 */
	public boolean[] extendArray(final boolean element, final boolean[] src) {
		boolean[] dest = new boolean[src.length + 1];
		System.arraycopy(src, 0, dest, 0, src.length);
		dest[src.length] = element;
		return dest;
	}
	/**
	 * Extends an array, adding a new element to the last index.
	 * @param element the new element
	 * @param src the source array
	 * @return <code>float</code>[]
	 */
	public float[] extendArray(final float element, final float[] src) {
		float[] dest = new float[src.length + 1];
		System.arraycopy(src, 0, dest, 0, src.length);
		dest[src.length] = element;
		return dest;
	}
	/**
	 * Extends an array, adding a new element to the last index.
	 * @param element the new element
	 * @param src the source array
	 * @return <code>int</code>[]
	 */
	public int[] extendArray(final int element, final int[] src) {
		int[] dest = new int[src.length + 1];
		System.arraycopy(src, 0, dest, 0, src.length);
		dest[src.length] = element;
		return dest;
	}
	/**
	 * Extends an array, adding a new element to the last index.
	 * @param element the new element
	 * @param src the source array
	 * @return <code>long</code>[]
	 */
	public long[] extendArray(final long element, final long[] src) {
		long[] dest = new long[src.length + 1];
		System.arraycopy(src, 0, dest, 0, src.length);
		dest[src.length] = element;
		return dest;
	}
	/**
	 * Extends an array, adding a new element to the last index.
	 * @param <T> the parameterized type
	 * @param element the new element
	 * @param src the source array
	 * @return {@link T}[]
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] extendArray(final T element, final T[] src) {
		T[] dest = (T[]) Arrays.copyOf(src, src.length + 1, src.getClass());
		dest[src.length] = element;
		return dest;
	}
	/**
	 * Prepends an array, adding a new element to the first index.
	 * @param element the new element
	 * @param src the source array
	 * @return {@link boolean}[]
	 */
	public boolean[] prependArray(final boolean element, final boolean[] src) {
		boolean[] dest = Arrays.copyOf(src, src.length + 1);
		System.arraycopy(src, 0, dest, 1, src.length);
		dest[0] = element;
		return dest;
	}
	/**
	 * Prepends an array, adding a new element to the first index.
	 * @param element the new element
	 * @param src the source array
	 * @return {@link int}[]
	 */
	public int[] prependArray(final int element, final int[] src) {
		int[] dest = Arrays.copyOf(src, src.length + 1);
		System.arraycopy(src, 0, dest, 1, src.length);
		dest[0] = element;
		return dest;
	}
	/**
	 * Prepends an array, adding a new element to the first index.
	 * @param <T> the parameterized type
	 * @param element the new element
	 * @param src the source array
	 * @return {@link T}[]
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] prependArray(final T element, final T[] src) {
		T[] dest = (T[]) Arrays.copyOf(src, src.length + 1, src.getClass());
		System.arraycopy(src, 0, dest, 1, src.length);
		dest[0] = element;
		return dest;
	}
	/**
	 * Removes an element from an array.
	 * @param index the element's index
	 * @param src the source array
	 * @return <code>boolean</code>[]
	 * @throws NullPointerException if the supplied source array is null
	 * @throws ArrayIndexOutOfBoundsException if the supplied index is outside
	 *             of the array's bounds
	 */
	public boolean[] removeIndex(final int index, final boolean[] src) {
		boolean[] dest = new boolean[src.length - 1];
		if (index == 0) {
			System.arraycopy(src, 1, dest, 0, dest.length);
		} else if (index == dest.length) {
			System.arraycopy(src, 0, dest, 0, src.length - 1);
		} else {
			System.arraycopy(src, 0, dest, 0, index);
			System.arraycopy(src, index + 1, dest, index, src.length - 1
					- index);
		}
		return dest;
	}
	/**
	 * Removes an element from an array.
	 * @param index the element's index
	 * @param src the source array
	 * @return <code>int</code>[]
	 * @throws NullPointerException if the supplied source array is null
	 * @throws ArrayIndexOutOfBoundsException if the supplied index is outside
	 *             of the array's bounds
	 */
	public int[] removeIndex(final int index, final int[] src) {
		int[] dest = new int[src.length - 1];
		if (index == 0) {
			System.arraycopy(src, 1, dest, 0, dest.length);
		} else if (index == dest.length) {
			System.arraycopy(src, 0, dest, 0, src.length - 1);
		} else {
			System.arraycopy(src, 0, dest, 0, index);
			System.arraycopy(src, index + 1, dest, index, src.length - 1
					- index);
		}
		return dest;
	}
	/**
	 * Removes an element from an array.
	 * @param <T> the parameterized type
	 * @param index the element's index
	 * @param src the source array
	 * @return {@link T}[]
	 * @throws NullPointerException if the supplied source array is null
	 * @throws ArrayIndexOutOfBoundsException if the supplied index is outside
	 *             of the array's bounds
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] removeIndex(final int index, final T[] src) {
		T[] dest = (T[]) Arrays.copyOf(src, src.length - 1, src.getClass());
		if (index == 0) {
			System.arraycopy(src, 1, dest, 0, dest.length);
		} else if (index == dest.length) {
			System.arraycopy(src, 0, dest, 0, src.length - 1);
		} else {
			System.arraycopy(src, 0, dest, 0, index);
			System.arraycopy(src, index + 1, dest, index, src.length - 1
					- index);
		}
		return dest;
	}
}
