package com.dalonedrow.pooled;

/**
 * @author drau
 */
public final class PooledException extends Exception {
	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates a new instance of {@link PooledException}.
	 * @param message the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public PooledException(final String message) {
		super(message);
	}
}
