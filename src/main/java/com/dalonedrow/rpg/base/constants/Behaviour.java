package com.dalonedrow.rpg.base.constants;

/**
 * @author drau
 */
public enum Behaviour {
	/** */
	BEHAVIOUR_DISTANT(1 << 9),
	/** */
	BEHAVIOUR_FIGHT(1 << 8),
	/** */
	BEHAVIOUR_FLEE(1 << 4),
	/** */
	BEHAVIOUR_FRIENDLY(1 << 1),
	/** */
	BEHAVIOUR_GO_HOME(1 << 12),
	/** */
	BEHAVIOUR_GUARD(1 << 11),
	/** */
	BEHAVIOUR_HIDE(1 << 5),
	/** */
	BEHAVIOUR_LOOK_AROUND(1 << 13),
	/** */
	BEHAVIOUR_LOOK_FOR(1 << 6),
	/** */
	BEHAVIOUR_MAGIC(1 << 10),
	/** */
	BEHAVIOUR_MOVE_TO(1 << 2),
	/** */
	BEHAVIOUR_NONE(1),
	/** */
	BEHAVIOUR_SNEAK(1 << 7),
	/** */
	BEHAVIOUR_STARE_AT(1 << 14),
	/** */
	BEHAVIOUR_WANDER_AROUND(1 << 3);
	/** the flag. */
	private final int flag;
	/**
	 * Creates a new instance of {@link Behaviour}.
	 * @param f the flag
	 */
	Behaviour(final int f) {
		flag = f;
	}
	/**
	 * Gets the flag.
	 * @return {@link long}
	 */
	public int getFlag() {
		return flag;
	}
}
