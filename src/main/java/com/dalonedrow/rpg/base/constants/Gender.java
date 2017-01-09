package com.dalonedrow.rpg.base.constants;

/**
 * @author drau
 */
public final class Gender {
	/** child relationships by gender. */
	public static final char[][]	GENDER_CHILD_RELATION		= {
			"son".toCharArray(), "daughter".toCharArray() };
	/** the female gender. */
	public static final int			GENDER_FEMALE				= 1;
	/** the male gender. */
	public static final int			GENDER_MALE					= 0;
	/** objective nouns by gender. */
	public static final char[][]	GENDER_OBJECTIVE			= {
			"him".toCharArray(), "her".toCharArray() };
	/** possessive adjectives by gender. */
	public static final char[][]	GENDER_POSSESSIVE			= {
			"his".toCharArray(), "her".toCharArray() };
	/** possessive objective nouns by gender. */
	public static final char[][]	GENDER_POSSESSIVE_OBJECTIVE	= {
			"his".toCharArray(), "hers".toCharArray() };
	/** pronouns by gender. */
	public static final char[][]	GENDER_PRONOUN				= {
			"he".toCharArray(), "she".toCharArray() };
	/** a list of player gender names. */
	public static final char[][]	GENDERS						= {
			"Male".toCharArray(), "Female".toCharArray() };
	/** Hidden constructor. */
	private Gender() {
		super();
	}
}
