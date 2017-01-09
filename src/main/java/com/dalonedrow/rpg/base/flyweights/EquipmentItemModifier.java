/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

/**
 * Equipped item element modifiers. An equipped item can have 0 or more of these
 * modifiers applied; an element that modifies the wielder's strength, an
 * element that modifies the wielder's to-hit score, etc.
 * @author Donald
 */
public final class EquipmentItemModifier {
	/** the flag indicating whether the modifier is a percentage modifier. */
	private boolean	percent;
	/** not used. yet. */
	private int		special;
	/** the value of modifier to be applied. */
	private float	value;
	/** Clears all data. */
	public void clearData() {
		percent = false;
		special = 0;
		value = 0f;
	}
	/**
	 * Gets the special.
	 * @return int
	 */
	public int getSpecial() {
		return special;
	}
	/**
	 * Gets the value of modifier to be applied.
	 * @return float
	 */
	public float getValue() {
		return value;
	}
	/**
	 * Determines if the {@link EquipmentItemModifier} is a percentage modifier.
	 * @return <tt>true</tt> if the {@link EquipmentItemModifier} is a
	 * percentage modifier; <tt>false</tt> otherwise
	 */
	public boolean isPercentage() {
		return percent;
	}
	/**
	 * Sets the modifier values.
	 * @param other the values being cloned
	 */
	public void set(final EquipmentItemModifier other) {
	    this.percent = other.percent;
	    this.special = other.special;
	    this.value = other.value;
	}
	/**
	 * Sets the flag indicating whether the modifier is a percentage modifier.
	 * @param flag the flag
	 */
	public void setPercentage(final boolean flag) {
		percent = flag;
	}
	/**
	 * Sets the special.
	 * @param val the special to set
	 */
	public void setSpecial(final int val) {
		special = val;
	}
	/**
	 * Sets the value of modifier to be applied.
	 * @param val the value to set
	 */
	public void setValue(final float val) {
		value = val;
	}
}
