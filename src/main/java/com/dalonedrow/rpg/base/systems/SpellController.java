package com.dalonedrow.rpg.base.systems;

import com.dalonedrow.rpg.base.flyweights.Spell;

public abstract class SpellController {
	/** the one and only instance of the <tt>Script</tt> class. */
	private static SpellController instance;
	/**
	 * Gives access to the singleton instance of {@link Script}.
	 * @return {@link Script}
	 */
	public static SpellController getInstance() {
		return SpellController.instance;
	}
	/**
	 * Sets the singleton instance.
	 * @param i the instance to set
	 */
	protected static void setInstance(final SpellController i) {
		SpellController.instance = i;
	}
	public abstract Spell getSpell(int index);
}
