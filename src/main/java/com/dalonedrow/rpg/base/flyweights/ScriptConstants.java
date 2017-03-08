package com.dalonedrow.rpg.base.flyweights;

/**
 * @author Donald
 */
public final class ScriptConstants {
    public static final int PATHFIND_ALWAYS                          = 1;
    public static final int PATHFIND_ONCE                          = 2;
    public static final int PATHFIND_NO_UPDATE                          = 4;
    public static final int TARGET_PATH = -3;
    public static final int TARGET_NONE = -2;
    public static final int TARGET_PLAYER = 0;
    public static final int ACCEPT                          = 1;
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
	public static final int	SM_009_RESET						= 9;
	public static final int	SM_010_CHAT						= 10;
	public static final int	SM_011_ACTION					= 11;
	public static final int	SM_012_DEAD						= 12;
	public static final int	SM_013_REACHEDTARGET				= 13;
	public static final int	SM_014_FIGHT						= 14;
	public static final int	SM_015_FLEE						= 15;
	public static final int	SM_016_HIT						= 16;
	public static final int	SM_017_DIE						= 17;
	public static final int	SM_018_LOSTTARGET				= 18;
	public static final int	SM_019_TREATIN					= 19;
	public static final int	SM_020_TREATOUT					= 20;
	/** script message to move to a travel location. */
	public static final int	SM_021_MOVE						= 21;
	public static final int	SM_022_DETECTPLAYER				= 22;
	public static final int	SM_023_UNDETECTPLAYER			= 23;
	public static final int	SM_024_COMBINE					= 24;
	public static final int	SM_025_NPC_FOLLOW				= 25;
	public static final int	SM_255_EXECUTELINE				= 255;
	public static final int	SM_256_DUMMY					= 256;
	public static final int	SM_026_NPC_FIGHT					= 26;
	public static final int	SM_027_NPC_STAY					= 27;
	public static final int	SM_028_INVENTORY2_OPEN			= 28;
	public static final int	SM_029_INVENTORY2_CLOSE			= 29;
	public static final int	SM_030_CUSTOM					= 30;
	public static final int	SM_031_ENTER_ZONE				= 31;
	public static final int	SM_032_LEAVE_ZONE				= 32;
	public static final int	SM_033_INITEND					= 33;
	public static final int	SM_034_CLICKED					= 34;
	public static final int	SM_035_INSIDEZONE				= 35;
	public static final int	SM_036_CONTROLLEDZONE_INSIDE		= 36;
	public static final int	SM_037_LEAVEZONE					= 37;
	public static final int	SM_038_CONTROLLEDZONE_LEAVE		= 38;
	public static final int	SM_039_ENTERZONE					= 39;
	public static final int	SM_040_CONTROLLEDZONE_ENTER		= 40;
	public static final int	SM_041_LOAD						= 41;
	public static final int	SM_042_SPELLCAST					= 42;
	public static final int	SM_043_RELOAD					= 43;
	public static final int	SM_044_COLLIDE_DOOR				= 44;
	public static final int	SM_045_OUCH						= 45;
	public static final int	SM_046_HEAR						= 46;
	public static final int	SM_047_SUMMONED					= 47;
	public static final int	SM_048_SPELLEND					= 48;
	public static final int	SM_049_SPELLDECISION				= 49;
	public static final int	SM_050_STRIKE					= 50;
	public static final int	SM_051_COLLISION_ERROR			= 51;
	public static final int	SM_052_WAYPOINT					= 52;
	public static final int	SM_053_PATHEND					= 53;
	public static final int	SM_054_CRITICAL					= 54;
	public static final int	SM_055_COLLIDE_NPC				= 55;
	public static final int	SM_056_BACKSTAB					= 56;
	public static final int	SM_057_AGGRESSION				= 57;
	public static final int	SM_058_COLLISION_ERROR_DETAIL	= 58;
	public static final int	SM_059_GAME_READY				= 59;
	public static final int	SM_060_CINE_END					= 60;
	public static final int	SM_061_KEY_PRESSED				= 61;
	public static final int	SM_062_CONTROLS_ON				= 62;
	public static final int	SM_063_CONTROLS_OFF				= 63;
	public static final int	SM_064_PATHFINDER_FAILURE		= 64;
	public static final int	SM_065_PATHFINDER_SUCCESS		= 65;
	public static final int	SM_066_TRAP_DISARMED				= 66;
	public static final int	SM_067_BOOK_OPEN					= 67;
	public static final int	SM_068_BOOK_CLOSE				= 68;
	public static final int	SM_069_IDENTIFY					= 69;
	public static final int	SM_070_BREAK						= 70;
	public static final int	SM_071_STEAL						= 71;
	public static final int	SM_072_COLLIDE_FIELD				= 72;
	public static final int	SM_073_CURSORMODE				= 73;
	public static final int	SM_074_EXPLORATIONMODE			= 74;
	public static final int	SM_075_MAXCMD					= 75;
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
