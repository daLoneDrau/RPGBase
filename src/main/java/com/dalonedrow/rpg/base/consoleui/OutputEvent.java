package com.dalonedrow.rpg.base.consoleui;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author drau
 */
public final class OutputEvent {
	/** the singLeton instance of {@link OutputEvent}. */
	private static OutputEvent instance;
	/**
	 * Gets the one and only instance of {@link OutputEvent}.
	 * @return {@link OutputEvent}
	 */
	public static OutputEvent getInstance() {
		if (OutputEvent.instance == null) {
			OutputEvent.instance = new OutputEvent();
		}
		return OutputEvent.instance;
	}
	/** the output buffer. */
	private final StringBuffer	buffer;
	/** flag indicating the output is locked. */
	private boolean				locked;
	/** the lock id. */
	private String				lockid;
	/** Hidden constructor. */
	private OutputEvent() {
		buffer = new StringBuffer();
	}
	/**
	 * Prints text to output and appends a line break.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void errprintln(final String text, final Object caller)
			throws RPGException {
		if (!locked) {
			buffer.append("** ");
			buffer.append(text);
			buffer.append(" **");
			buffer.append('\n');
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append("** ");
				buffer.append(text);
				buffer.append(" **");
				buffer.append('\n');
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/**
	 * Locks the output thread.
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void lock(final Object caller) throws RPGException {
		if (!locked) {
			locked = true;
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			lockid = sb.toString();
			sb.returnToPool();
			sb = null;
		} else {
			throw new RPGException(
					ErrorMessage.INTERNAL_ERROR, "Invalid lock attempt!");
		}
	}
	/**
	 * Prints text to output.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void print(final char text, final Object caller)
			throws RPGException {
		if (!locked) {
			buffer.append(text);
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append(text);
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/**
	 * Prints text to output.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void print(final char[] text, final Object caller)
			throws RPGException {
		if (!locked) {
			buffer.append(text);
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append(text);
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/**
	 * Prints text to output.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void print(final int text, final Object caller) throws RPGException {
		if (!locked) {
			buffer.append(text);
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append(text);
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/**
	 * Prints text to output.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void print(final String text, final Object caller)
			throws RPGException {
		if (!locked) {
			buffer.append(text);
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append(text);
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/**
	 * Prints text to output and appends a line break.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void println(final char[] text, final Object caller)
			throws RPGException {
		if (!locked) {
			buffer.append(text);
			buffer.append('\n');
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append(text);
				buffer.append('\n');
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/**
	 * Prints text to output and appends a line break.
	 * @param text the text
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void println(final String text, final Object caller)
			throws RPGException {
		if (!locked) {
			buffer.append(text);
			buffer.append('\n');
		} else {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			try {
				sb.append(caller.getClass().getName());
				sb.append('@');
				sb.append(Integer.toHexString(hashCode()));
			} catch (PooledException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
			if (lockid.equals(sb.toString())) {
				buffer.append(text);
				buffer.append('\n');
			}
			sb.returnToPool();
			sb = null;
		}
	}
	/** Renders the output and empties the buffer. */
	public void render() {
		System.out.print(buffer.toString());
		buffer.setLength(0);
	}
	/**
	 * Unlocks the output thread.
	 * @param caller the caller
	 * @throws RPGException if an error occurs
	 */
	public void unlock(final Object caller) throws RPGException {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		try {
			sb.append(caller.getClass().getName());
			sb.append('@');
			sb.append(Integer.toHexString(hashCode()));
		} catch (PooledException e) {
			throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
		}
		if (lockid.equals(sb.toString())) {
			locked = false;
		}
		sb.returnToPool();
		sb = null;
	}
}
