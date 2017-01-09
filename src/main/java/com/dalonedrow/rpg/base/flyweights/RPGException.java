package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;

/**
 * @author drau
 */
public final class RPGException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Gets the formatted message string.
	 * @param message the error message
	 * @param devMsg the developer's message
	 * @return {@link String}
	 */
	private static String getMessageString(final ErrorMessage message,
			final String devMsg) {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		try {
			sb.append("ErrorMessage [");
			sb.append(message);
			sb.append("user_message = ");
			sb.append(message.getUserMessage());
			sb.append(", developer_message = ");
			sb.append(devMsg);
			sb.append("]");
		} catch (PooledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = sb.toString();
		sb.returnToPool();
		return s;
	}
	/** the developer message. */
	private final String		developerMessage;
	/** the error message. */
	private final ErrorMessage	errorMessage;
	/**
	 * Creates a new instance of {@link RPGException}.
	 * @param message the {@link ErrorMessage}
	 * @param cause the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public RPGException(final ErrorMessage message, final Exception ex) {
		super(getMessageString(message, ex.getMessage()), ex);
		errorMessage = message;
		developerMessage = ex.getMessage();
	}
	/**
	 * Creates a new instance of {@link RPGException}.
	 * @param message the {@link ErrorMessage}
	 * @param devMsg the developer message
	 */
	public RPGException(final ErrorMessage message, final String devMsg) {
		super(getMessageString(message, devMsg));
		errorMessage = message;
		developerMessage = devMsg;
	}
	/**
	 * Creates a new instance of {@link RPGException}.
	 * @param message the {@link ErrorMessage}
	 * @param devMsg the developer message
	 */
	public RPGException(final ErrorMessage message, final String devMsg,
			final Exception ex) {
		super(getMessageString(message, devMsg), ex);
		errorMessage = message;
		developerMessage = ex.getMessage();
	}
	/**
	 * Gets the message from the developer.
	 * @return {@link String}
	 */
	public String getDeveloperMessage() {
		return developerMessage;
	}
	/**
	 * Gets the value for the message.
	 * @return {@link ErrorMessage}
	 */
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
}
