package com.dalonedrow.rpg.base.flyweights;

/**
 * @author Donald
 */
public final class ScriptConstants {
	public static final int	ACCEPT							= 1;
	public static final int	BIGERROR						= -2;
	public static final int	DISABLE_AGGRESSION				= 32;
	public static final int	DISABLE_CHAT					= 2;
	public static final int	DISABLE_COLLIDE_NPC				= 128;
	public static final int	DISABLE_CURSORMODE				= 256;
	public static final int	DISABLE_DETECT					= 16;
	public static final int	DISABLE_EXPLORATIONMODE			= 512;
	public static final int	DISABLE_HEAR					= 8;
	public static final int	DISABLE_HIT						= 1;
	public static final int	DISABLE_INVENTORY2_OPEN			= 4;
	public static final int	DISABLE_MAIN					= 64;
	public static final int	KILLBOTH						= 2;
	public static final int	KILLCOMBINEDWITH				= 4;
	public static final int	KILLCOMBINER					= 3;
	public static final int	MAX_EVENT_STACK					= 800;
	public static final int	MAX_SCRIPTTIMERS				= 5;
	public static final int	REFUSE							= -1;
	public static final int	SM_000_NULL						= 0;
	/** script messages to initialize an IO. */
	public static final int	SM_001_INIT						= 1;
	public static final int	SM_002_INVENTORYIN				= 2;
	public static final int	SM_003_INVENTORYOUT				= 3;
	public static final int	SM_004_INVENTORYUSE				= 4;
	public static final int	SM_005_SCENEUSE					= 5;
	public static final int	SM_006_EQUIPIN					= 6;
	public static final int	SM_007_EQUIPOUT					= 7;
	public static final int	SM_008_MAIN						= 8;
	public static final int	SM_09_RESET						= 9;
	public static final int	SM_10_CHAT						= 10;
	public static final int	SM_11_ACTION					= 11;
	public static final int	SM_12_DEAD						= 12;
	public static final int	SM_13_REACHEDTARGET				= 13;
	public static final int	SM_14_FIGHT						= 14;
	public static final int	SM_15_FLEE						= 15;
	public static final int	SM_16_HIT						= 16;
	public static final int	SM_17_DIE						= 17;
	public static final int	SM_18_LOSTTARGET				= 18;
	public static final int	SM_19_TREATIN					= 19;
	public static final int	SM_20_TREATOUT					= 20;
	/** script message to move to a travel location. */
	public static final int	SM_21_MOVE						= 21;
	public static final int	SM_22_DETECTPLAYER				= 22;
	public static final int	SM_23_UNDETECTPLAYER			= 23;
	public static final int	SM_24_COMBINE					= 24;
	public static final int	SM_25_NPC_FOLLOW				= 25;
	public static final int	SM_255_EXECUTELINE				= 255;
	public static final int	SM_256_DUMMY					= 256;
	public static final int	SM_26_NPC_FIGHT					= 26;
	public static final int	SM_27_NPC_STAY					= 27;
	public static final int	SM_28_INVENTORY2_OPEN			= 28;
	public static final int	SM_29_INVENTORY2_CLOSE			= 29;
	public static final int	SM_30_CUSTOM					= 30;
	public static final int	SM_31_ENTER_ZONE				= 31;
	public static final int	SM_32_LEAVE_ZONE				= 32;
	public static final int	SM_33_INITEND					= 33;
	public static final int	SM_34_CLICKED					= 34;
	public static final int	SM_35_INSIDEZONE				= 35;
	public static final int	SM_36_CONTROLLEDZONE_INSIDE		= 36;
	public static final int	SM_37_LEAVEZONE					= 37;
	public static final int	SM_38_CONTROLLEDZONE_LEAVE		= 38;
	public static final int	SM_39_ENTERZONE					= 39;
	public static final int	SM_40_CONTROLLEDZONE_ENTER		= 40;
	public static final int	SM_41_LOAD						= 41;
	public static final int	SM_42_SPELLCAST					= 42;
	public static final int	SM_43_RELOAD					= 43;
	public static final int	SM_44_COLLIDE_DOOR				= 44;
	public static final int	SM_45_OUCH						= 45;
	public static final int	SM_46_HEAR						= 46;
	public static final int	SM_47_SUMMONED					= 47;
	public static final int	SM_48_SPELLEND					= 48;
	public static final int	SM_49_SPELLDECISION				= 49;
	public static final int	SM_50_STRIKE					= 50;
	public static final int	SM_51_COLLISION_ERROR			= 51;
	public static final int	SM_52_WAYPOINT					= 52;
	public static final int	SM_53_PATHEND					= 53;
	public static final int	SM_54_CRITICAL					= 54;
	public static final int	SM_55_COLLIDE_NPC				= 55;
	public static final int	SM_56_BACKSTAB					= 56;
	public static final int	SM_57_AGGRESSION				= 57;
	public static final int	SM_58_COLLISION_ERROR_DETAIL	= 58;
	public static final int	SM_59_GAME_READY				= 59;
	public static final int	SM_60_CINE_END					= 60;
	public static final int	SM_61_KEY_PRESSED				= 61;
	public static final int	SM_62_CONTROLS_ON				= 62;
	public static final int	SM_63_CONTROLS_OFF				= 63;
	public static final int	SM_64_PATHFINDER_FAILURE		= 64;
	public static final int	SM_65_PATHFINDER_SUCCESS		= 65;
	public static final int	SM_66_TRAP_DISARMED				= 66;
	public static final int	SM_67_BOOK_OPEN					= 67;
	public static final int	SM_68_BOOK_CLOSE				= 68;
	public static final int	SM_69_IDENTIFY					= 69;
	public static final int	SM_70_BREAK						= 70;
	public static final int	SM_71_STEAL						= 71;
	public static final int	SM_72_COLLIDE_FIELD				= 72;
	public static final int	SM_73_CURSORMODE				= 73;
	public static final int	SM_74_EXPLORATIONMODE			= 74;
	public static final int	SM_75_MAXCMD					= 75;
	/** flag indicating the script variable is a global string. */
	public static final int	TYPE_G_00_TEXT					= 0;
	/** flag indicating the script variable is a global string. */
	public static final int	TYPE_G_01_TEXT_ARR				= 1;
	/** flag indicating the script variable is a global floating-point type. */
	public static final int	TYPE_G_02_FLOAT					= 2;
	/** flag indicating the script variable is a global floating-point array. */
	public static final int	TYPE_G_03_FLOAT_ARR				= 3;
	/** flag indicating the script variable is a global integer. */
	public static final int	TYPE_G_04_INT					= 4;
	/** flag indicating the script variable is a global integer array. */
	public static final int	TYPE_G_05_INT_ARR				= 5;
	/** flag indicating the script variable is a global integer. */
	public static final int	TYPE_G_06_LONG					= 6;
	/** flag indicating the script variable is a global long array. */
	public static final int	TYPE_G_07_LONG_ARR				= 7;
	/** flag indicating the script variable is a local string. */
	public static final int	TYPE_L_08_TEXT					= 8;
	/** flag indicating the script variable is a local string array. */
	public static final int	TYPE_L_09_TEXT_ARR				= 9;
	/** flag indicating the script variable is a local floating-point type. */
	public static final int	TYPE_L_10_FLOAT					= 10;
	/** flag indicating the script variable is a local floating-point array. */
	public static final int	TYPE_L_11_FLOAT_ARR				= 11;
	/** flag indicating the script variable is a local integer. */
	public static final int	TYPE_L_12_INT					= 12;
	/** flag indicating the script variable is a local integer array. */
	public static final int	TYPE_L_13_INT_ARR				= 13;
	/** flag indicating the script variable is a local integer. */
	public static final int	TYPE_L_14_LONG					= 14;
	/** flag indicating the script variable is a local long array. */
	public static final int	TYPE_L_15_LONG_ARR				= 15;
	/** Hidden constructor. */
	private ScriptConstants() {
		super();
	}
}
