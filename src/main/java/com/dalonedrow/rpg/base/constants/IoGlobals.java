package com.dalonedrow.rpg.base.constants;

/**
 * @author drau
 */
public final class IoGlobals {
	public static final int	GFLAG_DOOR					= 1 << 5;
	public static final int	GFLAG_ELEVATOR				= 1 << 10;
	public static final int	GFLAG_GOREEXPLODE			= 1 << 14;
	public static final int	GFLAG_HIDEWEAPON			= 1 << 12;
	public static final int	GFLAG_INTERACTIVITY			= 1;
	public static final int	GFLAG_INTERACTIVITYHIDE		= 1 << 4;
	public static final int	GFLAG_INVISIBILITY			= 1 << 6;
	public static final int	GFLAG_ISINTREATZONE			= 1 << 1;
	public static final int	GFLAG_MEGAHIDE				= 1 << 11;
	public static final int	GFLAG_NEEDINIT				= 1 << 3;
	public static final int	GFLAG_NO_PHYS_IO_COL		= 1 << 7;
	public static final int	GFLAG_NOCOMPUTATION			= 1 << 15;
	public static final int	GFLAG_NOGORE				= 1 << 13;
	public static final int	GFLAG_PLATFORM				= 1 << 9;
	public static final int	GFLAG_VIEW_BLOCKER			= 1 << 8;
	public static final int	GFLAG_WASINTREATZONE		= 1 << 2;
	/** flag indicating the interactive object is a PC. */
	public static final int	IO_01_PC					= 1;
	/** flag indicating the interactive object is an item. */
	public static final int	IO_02_ITEM					= 2;
	/** flag indicating the interactive object is an NPC. */
	public static final int	IO_03_NPC					= 4;
	/** flag indicating the interactive object is a horse object. */
	public static final int	IO_04_HORSE					= 8;
	/** flag indicating the interactive object is under water. */
	public static final int	IO_05_UNDERWATER			= 16;
	/** flag indicating the interactive object is a fixable object. */
	public static final int	IO_06_FREEZESCRIPT			= 32;
	/** flag indicating the interactive object is a fixable object. */
	public static final int	IO_07_NO_COLLISIONS			= 64;
	/** flag indicating the interactive object is a fixable object. */
	public static final int	IO_08_INVULNERABILITY		= 128;
	/** flag indicating the interactive object is a dwelling. */
	public static final int	IO_09_DWELLING				= 256;
	/** flag indicating the interactive object is gold. */
	public static final int	IO_10_GOLD					= 512;
	/** flag indicating the interactive object has interactivity. */
	public static final int	IO_11_INTERACTIVITY			= 1024;
	/** flag indicating the interactive object is a treasure. */
	public static final int	IO_12_TREASURE				= 2048;
	/** flag indicating the interactive object is unique. */
	public static final int	IO_13_UNIQUE				= 4096;
	/** flag indicating the interactive object is a party. */
	public static final int	IO_14_PARTY					= 8192;
	/** flag indicating the interactive object is a winged mount. */
	public static final int	IO_15_MOVABLE				= 16384;
	public static final int	PLAYER_CROUCH				= 1 << 7;
	public static final int	PLAYER_LEAN_LEFT			= 1 << 8;
	public static final int	PLAYER_LEAN_RIGHT			= 1 << 9;
	public static final int	PLAYER_MOVE_JUMP			= 1 << 4;
	public static final int	PLAYER_MOVE_STEALTH			= 1 << 5;
	public static final int	PLAYER_MOVE_STRAFE_LEFT		= 1 << 2;
	public static final int	PLAYER_MOVE_STRAFE_RIGHT	= 1 << 3;
	public static final int	PLAYER_MOVE_WALK_BACKWARD	= 1 << 1;
	public static final int	PLAYER_MOVE_WALK_FORWARD	= 1;
	public static final int	PLAYER_ROTATE				= 1 << 6;
	public static final int	PLAYERFLAGS_INVULNERABILITY	= 2;
	public static final int	PLAYERFLAGS_NO_MANA_DRAIN	= 1;
	public static final int	SHOW_FLAG_DESTROYED			= 255;
	public static final int	SHOW_FLAG_HIDDEN			= 5;		// In =
																	// Inventory;
	public static final int	SHOW_FLAG_IN_INVENTORY		= 4;		// In =
																	// Inventory;
	public static final int	SHOW_FLAG_IN_SCENE			= 1;
	public static final int	SHOW_FLAG_KILLED			= 7;		// Not Used
																	// = Yet;
	public static final int	SHOW_FLAG_LINKED			= 2;
	public static final int	SHOW_FLAG_MEGAHIDE			= 8;
	public static final int	SHOW_FLAG_NOT_DRAWN			= 0;

	public static final int	SHOW_FLAG_ON_PLAYER			= 9;
	public static final int	SHOW_FLAG_TELEPORTING		= 6;
	public static final int TARGET_PATH = -3;
	public static final int TARGET_NONE = -2;
	public static final int TARGET_PLAYER = 0;
	public static final int WALKMODE=	0;
	public static final int RUNMODE	=	1;
	public static final int NOMOVEMODE=	2;
	public static final int SNEAKMODE=	3;
	public static final int NO_IDENT = 1;
	public static final int NO_MESH = 2;
	public static final int NO_ON_LOAD = 4;
	public static final int IO_IMMEDIATELOAD = 8;
	/** Hidden constructor. */
	private IoGlobals() {
		super();
	}
}
