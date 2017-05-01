package com.dalonedrow.rpg.base.flyweights;

import java.lang.reflect.Method;

/**
 * Wrapper class for the information needed to create a new {@link ScriptTime}.
 * @author drau
 * @param <IO> an interactive object class
 */
@SuppressWarnings("rawtypes")
public final class ScriptTimerInitializationParameters
<IO extends BaseInteractiveObject, SCRIPT extends Scriptable<IO>> {
	/**
	 * the argument list supplied to the {@link Method} being invoked when the
	 * timer completes. can be null.
	 */
	private Object[]		args;
	/** the flags set on the timer. */
	private long			flagValues;
	/** the {@link IO} associated with the timer. */
	private IO				io;
	/** the {@link Method} invoked on the associated {@link Object}. */
	private Method			method;
	/** the number of milliseconds in the timer's cycle. */
	private int				milliseconds;
	/** the timer's name. */
	private String			name;
	/** the {@link Object} having an action applied when the timer completes. */
	private Object			obj;
	/** the number of times the timer repeats. */
	private int				repeatTimes;
	/** the {@link Scriptable} associated with the timer. */
	private SCRIPT	script;
	/** the time when the timer starts. */
	private long			startTime;
	/** Clears all parameters. */
	public void clear() {
		args = null;
		flagValues = 0;
		io = null;
		method = null;
		milliseconds = 0;
		name = null;
		obj = null;
		repeatTimes = 0;
		script = null;
		startTime = 0;
	}
	/**
	 * Gets the argument list supplied to the {@link Method} being invoked when
	 * the timer completes. can be null.
	 * @return {@link Object}
	 */
	public Object[] getArgs() {
		return args;
	}
	/**
	 * Gets the flags to set on the timer.
	 * @return {@link long}
	 */
	public long getFlagValues() {
		return flagValues;
	}
	/**
	 * Gets the {@link IO} associated with the timer.
	 * @return {@link IO}
	 */
	public IO getIo() {
		return io;
	}
	/**
	 * Gets the {@link Method} invoked on the associated {@link Object}.
	 * @return {@link Method}
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * Gets the number of milliseconds in the timer's cycle.
	 * @return {@link int}
	 */
	public int getMilliseconds() {
		return milliseconds;
	}
	/**
	 * Gets the timer's name.
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}
	/**
	 * Gets the {@link Object} having an action applied when the timer
	 * completes.
	 * @return {@link Object}
	 */
	public Object getObj() {
		return obj;
	}
	/**
	 * Gets the number of times the timer repeats.
	 * @return {@link int}
	 */
	public int getRepeatTimes() {
		return repeatTimes;
	}
	/**
	 * Gets the {@link Scriptable} associated with the timer.
	 * @return {@link Scriptable<IO>}
	 */
	public SCRIPT getScript() {
		return script;
	}
	/**
	 * Gets the time when the timer starts.
	 * @return {@link long}
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * Sets the argument list supplied to the {@link Method} being invoked when
	 * the timer completes. can be null.
	 * @param val the new value to set
	 */
	public void setArgs(final Object[] val) {
		args = val;
	}
	/**
	 * Sets the flags to set on the timer.
	 * @param val the new value to set
	 */
	public void setFlagValues(final long val) {
		flagValues = val;
	}
	/**
	 * Sets the {@link IO} associated with the timer.
	 * @param val the new value to set
	 */
	public void setIo(final IO val) {
		io = val;
	}
	/**
	 * Sets the {@link Method} invoked on the associated {@link Object}.
	 * @param val the new value to set
	 */
	public void setMethod(final Method val) {
		method = val;
	}
	/**
	 * Sets the number of milliseconds in the timer's cycle.
	 * @param val the new value to set
	 */
	public void setMilliseconds(final int val) {
		milliseconds = val;
	}
	/**
	 * Sets the timer's name.
	 * @param val the new value to set
	 */
	public void setName(final String val) {
		name = val;
	}
	/**
	 * Sets the {@link Object} having an action applied when the timer
	 * completes.
	 * @param val the new value to set
	 */
	public void setObj(final Object val) {
		obj = val;
	}
	/**
	 * Sets the number of times the timer repeats.
	 * @param val the new value to set
	 */
	public void setRepeatTimes(final int val) {
		repeatTimes = val;
	}
	/**
	 * Sets the {@link Scriptable} associated with the timer.
	 * @param val the new value to set
	 */
	public void setScript(final SCRIPT val) {
		script = val;
	}
	/**
	 * Sets the time when the timer starts.
	 * @param val the new value to set
	 */
	public void setStartTime(final long val) {
		startTime = val;
	}
}
