/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

/**
 * @author Donald
 */
public final class IOSpellCastData {
	/** the reference id of the spell being cast. */
	private long	castingspell;
	/** the spell's duration. */
	private long	duration;
	// unsigned char symb[4]; // symbols to draw before casting...
	/** flags applied to the spell. */
	private int		spellFlags;
	/** the spell's level. */
	private int		spellLevel;
	/** the reference id of the target. */
	private long	target;
	/**
	 * Adds a spell flag.
	 * @param flag the flag
	 */
	public void addSpellFlag(final long flag) {
		spellFlags |= flag;
	}
	/** Clears all spell flags that were set. */
	public void clearSpellFlags() {
		spellFlags = 0;
	}
	/**
	 * Gets the reference id of the spell being cast.
	 * @return {@link long}
	 */
	public long getCastingspell() {
		return castingspell;
	}
	/**
	 * Gets the spell's duration.
	 * @return {@link long}
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * Gets the spell's level.
	 * @return {@link short}
	 */
	public int getSpellLevel() {
		return spellLevel;
	}
	/**
	 * Gets the target's reference id.
	 * @return {@link long}
	 */
	public long getTarget() {
		return target;
	}
	/**
	 * Determines if the {@link IOSpellCastData} has a specific flag.
	 * @param flag the flag
	 * @return true if the {@link IOSpellCastData} has the flag; false otherwise
	 */
	public boolean hasSpellFlag(final long flag) {
		return (spellFlags & flag) == flag;
	}
	/**
	 * Removes a spell flag.
	 * @param flag the flag
	 */
	public void removeSpellFlag(final long flag) {
		spellFlags &= ~flag;
	}
	/**
	 * Sets the reference id of the spell being cast.
	 * @param refId the reference id to set
	 */
	public void setCastingspell(final long refId) {
		castingspell = refId;
	}
	/**
	 * Sets the duration of the spell.
	 * @param val the duration to set
	 */
	public void setDuration(final long val) {
		duration = val;
	}
	/**
	 * Sets the spell's level.
	 * @param i the spell's level to set
	 */
	public void setSpellLevel(final int i) {
		spellLevel = i;
	}
	/**
	 * Sets the spell's target IO.
	 * @param refId the target's reference id
	 */
	public void setTarget(final long refId) {
		target = refId;
	}
}
