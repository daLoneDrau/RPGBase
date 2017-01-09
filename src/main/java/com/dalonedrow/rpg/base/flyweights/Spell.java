/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

/**
 * @author Donald
 */
public class Spell {
	/** the caster's reference id. */
	private int		caster;
	/** the caster's level. */
	private float	casterLevel;
	private boolean	exists;
	/** any flags that have been set. */
	private long	flags	= 0;
	private int		lastTurnUpdated;
	private long	lastUpdated;
	private int		longinfo;
	private int		longinfo2;
	private Object	misc;
	private int		target;
	private long	timeCreated;
	private long	timeToLive;
	private int		turnCreated;
	private int		turnsToLive;
	private int		type;
	/**
	 * Adds a flag.
	 * @param flag the flag
	 */
	public void addFlag(final long flag) {
		flags |= flag;
	}
	/** Clears all flags that were set. */
	public void clearFlags() {
		flags = 0;
	}
	/**
	 * Gets the exists
	 * @return <code>boolean</code>
	 */
	public boolean exists() {
		return exists;
	}
	/**
	 * Gets the caster
	 * @return {@link int}
	 */
	public int getCaster() {
		return caster;
	}
	/**
	 * Gets the caster level.
	 * @return {@link float}
	 */
	public final float getCasterLevel() {
		return casterLevel;
	}
	/**
	 * Gets the lastTurnUpdated
	 * @return {@link int}
	 */
	public int getLastTurnUpdated() {
		return lastTurnUpdated;
	}
	/**
	 * Gets the lastUpdated
	 * @return {@link long}
	 */
	public long getLastUpdated() {
		return lastUpdated;
	}
	/**
	 * Gets the value of the longinfo.
	 * @return {@link int}
	 */
	public int getLonginfo() {
		return longinfo;
	}
	/**
	 * Gets the value of the longinfo2.
	 * @return {@link int}
	 */
	public int getLonginfo2() {
		return longinfo2;
	}
	/**
	 * Gets the value of the misc.
	 * @return {@link Object}
	 */
	public Object getMisc() {
		return misc;
	}
	/**
	 * Gets the target
	 * @return {@link int}
	 */
	public int getTarget() {
		return target;
	}
	/**
	 * Gets the timeCreated
	 * @return {@link long}
	 */
	public long getTimeCreated() {
		return timeCreated;
	}
	/**
	 * Gets the timeToLive
	 * @return {@link long}
	 */
	public long getTimeToLive() {
		return timeToLive;
	}
	/**
	 * Gets the turnCreated
	 * @return {@link int}
	 */
	public int getTurnCreated() {
		return turnCreated;
	}
	/**
	 * Gets the turnsToLive
	 * @return {@link int}
	 */
	public int getTurnsToLive() {
		return turnsToLive;
	}
	/**
	 * Gets the type
	 * @return {@link int}
	 */
	public int getType() {
		return type;
	}
	/**
	 * Determines if the {@link Spell} has a specific flag.
	 * @param flag the flag
	 * @return true if the {@link Spell} has the flag; false otherwise
	 */
	public boolean hasFlag(final long flag) {
		return (flags & flag) == flag;
	}
	/**
	 * Removes a flag.
	 * @param flag the flag
	 */
	public void removeFlag(final long flag) {
		flags &= ~flag;
	}
	/**
	 * Sets the caster
	 * @param caster the caster to set
	 */
	public void setCaster(final int caster) {
		this.caster = caster;
	}
	/**
	 * Sets the caster level.
	 * @param val the level to set
	 */
	public final void setCasterLevel(final float val) {
		casterLevel = val;
	}
	/**
	 * Sets the exists
	 * @param exists the exists to set
	 */
	public void setExists(final boolean exists) {
		this.exists = exists;
	}
	/**
	 * Sets the lastTurnUpdated
	 * @param lastTurnUpdated the lastTurnUpdated to set
	 */
	public void setLastTurnUpdated(final int lastTurnUpdated) {
		this.lastTurnUpdated = lastTurnUpdated;
	}
	/**
	 * Sets the lastUpdated
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(final long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	/**
	 * Sets the value of the longinfo.
	 * @param longinfo the value to set
	 */
	public void setLonginfo(final int longinfo) {
		this.longinfo = longinfo;
	}
	/**
	 * Sets the value of the longinfo2.
	 * @param longinfo2 the value to set
	 */
	public void setLonginfo2(final int longinfo2) {
		this.longinfo2 = longinfo2;
	}
	/**
	 * Sets the value of the misc.
	 * @param misc the value to set
	 */
	public void setMisc(final Object misc) {
		this.misc = misc;
	}
	/**
	 * Sets the target
	 * @param target the target to set
	 */
	public void setTarget(final int target) {
		this.target = target;
	}
	/**
	 * Sets the timeCreated
	 * @param timeCreated the timeCreated to set
	 */
	public void setTimeCreated(final long timeCreated) {
		this.timeCreated = timeCreated;
	}
	/**
	 * Sets the timeToLive
	 * @param timeToLive the timeToLive to set
	 */
	public void setTimeToLive(final long timeToLive) {
		this.timeToLive = timeToLive;
	}
	/**
	 * Sets the turnCreated
	 * @param turnCreated the turnCreated to set
	 */
	public void setTurnCreated(final int turnCreated) {
		this.turnCreated = turnCreated;
	}
	/**
	 * Sets the turnsToLive
	 * @param turnsToLive the turnsToLive to set
	 */
	public void setTurnsToLive(final int turnsToLive) {
		this.turnsToLive = turnsToLive;
	}
	/**
	 * Sets the type
	 * @param type the type to set
	 */
	public void setType(final int type) {
		this.type = type;
	}
}
