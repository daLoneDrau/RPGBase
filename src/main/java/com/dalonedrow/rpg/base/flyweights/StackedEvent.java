/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

/**
 * @author Donald
 * @param <IO>
 */
@SuppressWarnings("rawtypes")
public class StackedEvent<IO extends BaseInteractiveObject> {
	/** the event name. */
	private String		eventname;
	/** flag indicating whether the event still exists. */
	private boolean		exist;
	/** the IO associated with the event. */
	private IO			io;
	/** the event message. */
	private int			msg;
	/** the event parameters. */
	private Object[]	params;
	/** the event sender. */
	private IO			sender;
	/**
	 * Gets the flag indicating whether the event still exists.
	 * @return <code>boolean</code>
	 */
	public final boolean exists() {
		return exist;
	}
	/**
	 * Gets the event name.
	 * @return {@link String}
	 */
	public final String getEventname() {
		return eventname;
	}
	/**
	 * Gets the IO associated with the event.
	 * @return {@link IO}
	 */
	public final IO getIo() {
		return io;
	}
	/**
	 * Gets the event message.
	 * @return {@link int}
	 */
	public final int getMsg() {
		return msg;
	}
	/**
	 * Gets the event parameters.
	 * @return {@link Object}[]
	 */
	public final Object[] getParams() {
		return params;
	}
	/**
	 * Gets the event sender.
	 * @return {@link IO}
	 */
	public final IO getSender() {
		return sender;
	}
	/**
	 * Sets the event name.
	 * @param val the eventname to set
	 */
	public final void setEventname(final String val) {
		this.eventname = val;
	}
	/**
	 * Sets the flag indicating whether the event still exists.
	 * @param val the exist to set
	 */
	public final void setExist(final boolean val) {
		this.exist = val;
	}
	/**
	 * Sets the IO associated with the event.
	 * @param val the io to set
	 */
	public final void setIo(final IO val) {
		this.io = val;
	}
	/**
	 * Sets the event message.
	 * @param val the msg to set
	 */
	public final void setMsg(final int val) {
		this.msg = val;
	}
	/**
	 * Sets the event parameters.
	 * @param val the params to set
	 */
	public final void setParams(final Object[] val) {
		this.params = val;
	}
	/**
	 * Sets the event sender.
	 * @param val the sender to set
	 */
	public final void setSender(final IO val) {
		this.sender = val;
	}
}
