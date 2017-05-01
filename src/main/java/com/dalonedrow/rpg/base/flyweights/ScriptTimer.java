/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

/**
 * Script timer class.
 * @author drau
 * @param <IO> the interactive object class associated with the timer
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScriptTimer<IO extends BaseInteractiveObject,
SCRIPT extends Scriptable<IO>> {
	/** the action taken when the script timer completes. */
	private ScriptTimerAction	action;
	/** the flag indicating whether the timer exists. */
	private boolean				exists;
	/** any flags set on the timer. */
	private int					flags;
	/** the {@link IO} associated with this timer. */
	private IO					io;
	/** the index of any array the timer is associated with. */
	private long				longinfo;
	/** the timer's length in milliseconds. */
	private long				msecs;
	/** the timer's name. */
	private String				name;
	/** the script associated with the timer. */
	private SCRIPT		script;
	/** the amount of time passed since the timer was started. */
	private long				tim;
	/** the number of times the timer repeats. */
	private long				times;
	/** if true, the timer is turn-based, otherwise it is millisecond based. */
	private boolean turnBased;
	/**
	 * Adds a flag set on the timer..
	 * @param flag the flag
	 */
	public final void addFlag(final long flag) {
		flags |= flag;
	}
	/** Clears all flags that were set. */
	public final void clearFlags() {
		flags = 0;
	}
	/**
	 * Gets the flag indicating whether the timer exists.
	 * @return <code>boolean</code>
	 */
	public final boolean exists() {
		return exists;
	}
	/**
	 * Gets the value for the action.
	 * @return {@link ScriptTimerAction}
	 */
	public final ScriptTimerAction getAction() {
		return action;
	}
	/**
	 * Gets the {@link IO} associated with this timer.
	 * @return {@link IO}
	 */
	public final IO getIo() {
		return io;
	}
	/**
	 * Gets the index of any array the timer is associated with.
	 * @return {@link long}
	 */
	public final long getLonginfo() {
		return longinfo;
	}
	/**
	 * Gets the timer's length in milliseconds.
	 * @return {@link long}
	 */
	public final long getCycleLength() {
		return msecs;
	}
	/**
	 * Gets the timer's name.
	 * @return {@link String}
	 */
	public final String getName() {
		return name;
	}
	/**
	 * Gets the script associated with the timer.
	 * @return {@link Scriptable}<{@link IO}>
	 */
	public final Scriptable<IO> getScript() {
		return script;
	}
	/**
	 * Gets the amount of time passed since the timer was started.
	 * @return {@link long}
	 */
	public final long getLastTimeCheck() {
		return tim;
	}
	/**
	 * Gets the number of times the timer repeats.
	 * @return {@link long}
	 */
	public final long getRepeatTimes() {
		return times;
	}
	/**
	 * Determines if the {@link ScriptTimer} has a specific flag.
	 * @param flag the flag
	 * @return true if the {@link ScriptTimer} has the flag; false otherwise
	 */
	public final boolean hasFlag(final long flag) {
		return (flags & flag) == flag;
	}
    /**
     * Determines whether the timer is turn-based, or millisecond based.
     * @return {@link boolean}
     */
    public boolean isTurnBased() {
        return turnBased;
    }
	/**
	 * Removes a flag.
	 * @param flag the flag
	 */
	public final void removeFlag(final long flag) {
		flags &= ~flag;
	}
	/**
	 * Sets the timer.
	 * @param params the parameters used to set the timer.
	 */
	public final void set(final ScriptTimerInitializationParameters params) {
		script = (SCRIPT) params.getScript();
		exists = true;
		io = (IO) params.getIo();
		msecs = params.getMilliseconds();
		name = params.getName();
		action = new ScriptTimerAction(
				params.getObj(),
				params.getMethod(),
				params.getArgs());
		tim = params.getStartTime();
		times = params.getRepeatTimes();
		clearFlags();
		addFlag(params.getFlagValues());
	}
	/**
	 * Sets the action taken when the script timer completes.
	 * @param sta the {@link ScriptTimerAction}
	 */
	public final void setAction(final ScriptTimerAction sta) {
		action = sta;
	}
    /**
     * Sets the timer's length in milliseconds.
     * @param val the value to set
     */
    public final void setCycleLength(final long val) {
        msecs = val;
    }
	/**
	 * Sets the flag indicating whether the timer exists.
	 * @param flag the flag to set
	 */
	public final void setExists(final boolean flag) {
		exists = flag;
	}
	/**
	 * Sets the {@link IO} associated with this timer.
	 * @param val the value to set
	 */
	public final void setIo(final IO val) {
		io = val;
	}
	/**
	 * Sets the index of any array the timer is associated with.
	 * @param val the value to set
	 */
	public final void setLonginfo(final long val) {
		longinfo = val;
	}
	/**
	 * Sets the timer's name.
	 * @param val the value to set
	 */
	public final void setName(final String val) {
		name = val;
	}
	/**
	 * Sets the script associated with the timer.
	 * @param val the {@link Scriptable}<{@link IO}> to set
	 */
	public final void setScript(final SCRIPT val) {
		script = val;
	}
	/**
	 * Sets the amount of time passed since the timer was started.
	 * @param val the value to set
	 */
	public final void setLastTimeCheck(final long val) {
		tim = val;
	}
	/**
	 * Sets the number of times the timer repeats.
	 * @param val the value to set
	 */
	public final void setRepeatTimes(final long val) {
		times = val;
	}
    /**
     * Sets whether the timer is turn-based, or millisecond based.
     * @param isTurnBased the new value to set
     */
    public void setTurnBased(final boolean flag) {
        this.turnBased = flag;
    }
}
