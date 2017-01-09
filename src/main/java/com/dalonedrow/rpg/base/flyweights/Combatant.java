package com.dalonedrow.rpg.base.flyweights;

/**
 * 
 * @author drau
 *
 */
public interface Combatant {
	/**
	 * Determines if the {@link Combatant} is in combat.
	 * @return <tt>true</tt> if the combatant is in combat; <tt>false</tt>
	 * otherwise
	 */
	boolean isInCombat();
}
