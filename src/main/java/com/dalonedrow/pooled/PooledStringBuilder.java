/**
 *
 */
package com.dalonedrow.pooled;

/**
 * Wrapper class for {@link StringBuilder}.
 * @author DaLoneDrow
 */
public final class PooledStringBuilder implements PoolableObject {
	/** the initial capacity. */
	private final int			capacity		= 1000;
	/** the pooled object's index. */
	private final int			poolIndex;
	/** the internal {@link StringBuffer} instance. */
	private final StringBuffer	stringBuilder	= new StringBuffer(capacity);
	/**
	 * Creates a new instance of {@link PooledStringBuilder}.
	 * @param index the object's index
	 */
	public PooledStringBuilder(final int index) {
		poolIndex = index;
	}
	/**
	 * Appends the string representation of the {@code char} argument to this
	 * sequence.
	 * <p>
	 * The argument is appended to the contents of this sequence. The length of
	 * this sequence increases by {@code 1}.
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a
	 * string by the method {@link String#valueOf(char)}, and the character in
	 * that string were then {@link #append(String) appended} to this character
	 * sequence. *
	 * @param c a {@code char}.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final char c) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			stringBuilder.append(c);
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the string representation of the {@code char} array argument to
	 * this sequence.
	 * <p>
	 * The characters of the array argument are appended, in order, to the
	 * contents of this sequence. The length of this sequence increases by the
	 * length of the argument.
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a
	 * string by the method {@link String#valueOf(char[])}, and the characters
	 * of that string were then {@link #append(String) appended} to this
	 * character sequence.
	 * @param str the characters to be appended.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final char[] str) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			stringBuilder.append(new String(str));
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the string representation of the {@code float} argument to this
	 * sequence.
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a
	 * string by the method {@link String#valueOf(float)}, and the characters of
	 * that string were then {@link #append(String) appended} to this character
	 * sequence.
	 * @param f a {@code float}.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final float f) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			stringBuilder.append(f);
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the string representation of the {@code int} argument to this
	 * sequence.
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a
	 * string by the method {@link String#valueOf(int)}, and the characters of
	 * that string were then {@link #append(String) appended} to this character
	 * sequence.
	 * @param i an {@code int}.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final int i) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			stringBuilder.append(i);
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the string representation of the {@code long} argument to this
	 * sequence.
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a
	 * string by the method {@link String#valueOf(long)}, and the characters of
	 * that string were then {@link #append(String) appended} to this character
	 * sequence.
	 * @param l a {@code long}.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final long l) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			stringBuilder.append(l);
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the string representation of the {@code Object} argument.
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a
	 * string by the method {@link String#valueOf(Object)}, and the characters
	 * of that string were then {@link #append(String) appended} to this
	 * character sequence.
	 * @param obj an {@code Object}.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final Object obj) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			if (obj != null) {
				stringBuilder.append(obj.toString());
			} else {
				stringBuilder.append(obj);
			}
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the specified string to this character sequence.
	 * <p>
	 * The characters of the {@code String} argument are appended, in order,
	 * increasing the length of this sequence by the length of the argument. If
	 * {@code str} is {@code null}, then the four characters {@code "null"} are
	 * appended.
	 * <p>
	 * Let <i>n</i> be the length of this character sequence just prior to
	 * execution of the {@code append} method. Then the character at index
	 * <i>k</i> in the new character sequence is equal to the character at index
	 * <i>k</i> in the old character sequence, if <i>k</i> is less than <i>n</i>
	 * ; otherwise, it is equal to the character at index <i>k-n</i> in the
	 * argument {@code str}.
	 * @param str a string.
	 * @throws PooledException if the item was not locked
	 */
	public void append(final String str) throws PooledException {
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			stringBuilder.append(str);
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Gets the value for the object's index.
	 * @return {@link int}
	 */
	public int getPoolIndex() {
		return poolIndex;
	}
	/*
	 * (non-Javadoc)
	 * @see com.dalonedrow.pooled.PoolableObject#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}
	/**
	 * Returns the length (character count). *
	 * @return the length of the sequence of characters currently represented by
	 *         this object
	 */
	public int length() {
		return stringBuilder.length();
	}
	/*
	 * (non-Javadoc)
	 * @see com.dalonedrow.pooled.PoolableObject#returnToPool()
	 */
	@Override
	public void returnToPool() {
		stringBuilder.setLength(0);
		if (StringBuilderPool.getInstance().isItemLocked(this)) {
			StringBuilderPool.getInstance().unlockItem(this);
		}
	}
	/**
	 * Sets the length of the character sequence. The sequence is changed to a
	 * new character sequence whose length is specified by the argument. For
	 * every nonnegative index <i>k</i> less than {@code newLength}, the
	 * character at index <i>k</i> in the new character sequence is the same as
	 * the character at index <i>k</i> in the old sequence if <i>k</i> is less
	 * than the length of the old character sequence; otherwise, it is the null
	 * character {@code '\u005Cu0000'}. In other words, if the {@code newLength}
	 * argument is less than the current length, the length is changed to the
	 * specified length.
	 * <p>
	 * If the {@code newLength} argument is greater than or equal to the current
	 * length, sufficient null characters ({@code '\u005Cu0000'}) are appended
	 * so that length becomes the {@code newLength} argument.
	 * <p>
	 * The {@code newLength} argument must be greater than or equal to {@code 0}
	 * .
	 * @param newLength the new length
	 * @throws IndexOutOfBoundsException if the {@code newLength} argument is
	 *             negative.
	 */
	public void setLength(final int newLength) {
		stringBuilder.setLength(newLength);
	}
	/**
	 * Returns a string representing the data in this sequence. A new
	 * {@code String} object is allocated and initialized to contain the
	 * character sequence currently represented by this object. This
	 * {@code String} is then returned. Subsequent changes to this sequence do
	 * not affect the contents of the {@code String}.
	 * @return a string representation of this sequence of characters.
	 */
	@Override
	public String toString() {
		return stringBuilder.toString();
	}
}
