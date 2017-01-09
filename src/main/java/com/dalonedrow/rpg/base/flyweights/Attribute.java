/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

/**
 * An <tt>Attribute</tt> is defined as a base value, its modifiers, and a full
 * value, along with a display name, abbreviation, and description.
 * @author drau
 */
public final class Attribute {
	/** the {@link Attribute}'s name abbreviation. */
	private char[]	abbr;
	/** the {@link Attribute}'s base value. */
	private float	base;
	/** the {@link Attribute}'s description. */
	private char[]	description;
	/** the {@link Attribute}'s display name. */
	private char[]	displayName;
	/** the value of any modifiers to the attribute. */
	private float	modifier;
	/**
	 * Creates a new instance of {@link Attribute}.
	 * @param a the {@link Attribute}'s name abbreviation
	 * @param n the {@link Attribute}'s display name
	 */
	public Attribute(final char[] a, final char[] n) {
		this(a, n, null);
	}
	/**
	 * Creates a new instance of {@link Attribute}.
	 * @param a the {@link Attribute}'s name abbreviation
	 * @param n the {@link Attribute}'s display name
	 * @param desc the {@link Attribute}'s description
	 */
	public Attribute(final char[] a, final char[] n, final char[] desc) {
		abbr = a;
		displayName = n;
		description = desc;
	}
	/**
	 * Creates a new instance of {@link Attribute}.
	 * @param a the {@link Attribute}'s name abbreviation
	 * @param n the {@link Attribute}'s display name
	 * @throws RPGException if either parameter is null
	 */
	public Attribute(final String a, final String n) throws RPGException {
		if (a == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Name abbreviation cannot be null");
		}
		if (n == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Display name cannot be null");
		}
		abbr = a.toCharArray();
		displayName = n.toCharArray();
	}
	/**
	 * Creates a new instance of {@link Attribute}.
	 * @param a the {@link Attribute}'s name abbreviation
	 * @param n the {@link Attribute}'s display name
	 * @param desc the {@link Attribute}'s description
	 * @throws RPGException if any parameter is null
	 */
	public Attribute(final String a, final String n, final String desc)
			throws RPGException {
		if (a == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Name abbreviation cannot be null");
		}
		if (n == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Display name cannot be null");
		}
		if (desc == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Description cannot be null");
		}
		abbr = a.toCharArray();
		displayName = n.toCharArray();
		description = desc.toCharArray();
	}
	/**
	 * Sets the value for the modifier.
	 * @param val the value to set
	 */
	public void adjustModifier(final float val) {
		modifier += val;
	}
	/** Resets the {@link Attribute}'s modifier value to 0. */
	public void clearModifier() {
		modifier = 0;
	}
	/**
	 * Gets the {@link Attribute}'s name abbreviation.
	 * @return <code>char</code>[]
	 */
	public char[] getAbbr() {
		return abbr;
	}
	/**
	 * Gets the base {@link Attribute} value.
	 * @return {@link float}
	 */
	public float getBase() {
		return base;
	}
	/**
	 * Gets the {@link Attribute}'s description.
	 * @return <code>char</code>[]
	 */
	public char[] getDescription() {
		return description;
	}
	/**
	 * Gets the {@link Attribute}'s display name.
	 * @return <code>char</code>[]
	 */
	public char[] getDisplayName() {
		return displayName;
	}
	/**
	 * Gets the full {@link Attribute} value.
	 * @return {@link float}
	 */
	public float getFull() {
		return base + modifier;
	}
	/**
	 * Gets the value of all modifiers to the {@link Attribute}.
	 * @return {@link float}
	 */
	public float getModifier() {
		return modifier;
	}
	/**
	 * Sets the {@link Attribute}'s name abbreviation.
	 * @param val the name to set
	 * @throws RPGException if the parameter is null
	 */
	public void setAbbr(final char[] val) throws RPGException {
		if (val == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Name abbreviation cannot be null");
		}
		abbr = val;
	}
	/**
	 * Sets the {@link Attribute}'s name abbreviation.
	 * @param val the name to set
	 * @throws RPGException if the parameter is null
	 */
	public void setAbbr(final String val) throws RPGException {
		if (val == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Name abbreviation cannot be null");
		}
		abbr = val.toCharArray();
	}
	/**
	 * Sets the base {@link Attribute} value.
	 * @param val the value to set
	 */
	public void setBase(final float val) {
		base = val;
	}
	/**
	 * Sets the {@link Attribute}'s description.
	 * @param val the name to set
	 * @throws RPGException if the parameter is null
	 */
	public void setDescription(final char[] val) throws RPGException {
		if (val == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Description cannot be null");
		}
		description = val;
	}
	/**
	 * Sets the {@link Attribute}'s description.
	 * @param val the name to set
	 * @throws RPGException if the parameter is null
	 */
	public void setDescription(final String val) throws RPGException {
		if (val == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Description cannot be null");
		}
		description = val.toCharArray();
	}
	/**
	 * Sets the {@link Attribute}'s display name.
	 * @param val the name to set
	 * @throws RPGException if the parameter is null
	 */
	public void setDisplayName(final char[] val) throws RPGException {
		if (val == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Dispaly name cannot be null");
		}
		displayName = val;
	}
	/**
	 * Sets the {@link Attribute}'s display name.
	 * @param val the name to set
	 * @throws RPGException if the parameter is null
	 */
	public void setDisplayName(final String val) throws RPGException {
		if (val == null) {
			throw new RPGException(ErrorMessage.BAD_PARAMETERS,
					"Dispaly name cannot be null");
		}
		displayName = val.toCharArray();
	}
}
