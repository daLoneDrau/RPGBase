/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.JOGLErrorHandler;
import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.MathGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.systems.Script;
import com.dalonedrow.rpg.base.systems.SpellController;
import com.dalonedrow.utils.ArrayUtilities;
import com.dalonedrow.utils.Watchable;
import com.dalonedrow.utils.Watcher;

/**
 * @author drau
 * @param <IO>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class IoPcData<IO extends BaseInteractiveObject>
extends IOCharacter implements Combatant {
	/** the number of bags the player has. */
	private int bags;
	/** the {@link IoPcData}'s gender. */
	private int gender = -1;
	/** the character's gold. */
	private float gold;
	/** interface flags. */
	private int interfaceFlags;
	/** the IO associated with this {@link IoPcData}. */
	private IO io;
	/** the player's key ring. */
	private char[][] keyring;
	/** the {@link IoPcData}'s level. */
	private int level = 0;
	/** the {@link IoPcData}'s name. */
	private char[] name;
	/** the number of keys on the key ring. */
	private int numKeys;
	/** the {@link IoPcData}'s Profession. */
	private int profession = -1;
	/** the {@link IoPcData}'s Race. */
	private int race = -1;
	/** the {@link IoPcData}'s experience points. */
	private int xp;
	/**
	 * Creates a new instance of {@link IoPcData}.
	 * @throws RPGException if there is an error defining attributes
	 */
	protected IoPcData() throws RPGException {
	    super();
		name = new char[0];
	}
	/**
	 * Adds an interface flag.
	 * @param flag the flag
	 */
	public final void addInterfaceFlag(final long flag) {
		interfaceFlags |= flag;
	}
	/**
	 * Adds a key to the keyring.
	 * @param key the key
	 */
	public final void addKey(final char[] key) {
		if (keyring == null) {
			keyring = new char[0][];
		}
		char[] keyCopy = new char[key.length];
		System.arraycopy(key, 0, keyCopy, 0, key.length);
		for (int i = keyCopy.length - 1; i >= 0; i--) {
			keyCopy[i] = Character.toLowerCase(keyCopy[i]);
		}
		int index = -1;
		for (int i = keyring.length - 1; i >= 0; i--) {
			if (keyring[i] == null) {
				index = i;
				break;
			}
			char[] keyRingCopy = new char[keyring[i].length];
			System.arraycopy(keyring[i], 0, keyRingCopy, 0, keyring[i].length);
			for (int j = keyRingCopy.length - 1; j >= 0; j--) {
				keyRingCopy[j] = Character.toLowerCase(keyRingCopy[j]);
			}
			if (Arrays.equals(keyRingCopy, keyCopy)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			keyring = ArrayUtilities.getInstance().extendArray(key, keyring);
			numKeys++;
		}
		keyCopy = null;
	}
	/**
	 * Adds a key to the keyring.
	 * @param key the key
	 */
	public final void addKey(final String key) {
		addKey(key.toCharArray());
	}
	/**
	 * Adjusts the {@link IoPcData}'s gold.
	 * @param val the amount adjusted by
	 */
	public final void adjustGold(final float val) {
		gold += val;
		if (gold < 0) {
			gold = 0;
		}
		notifyWatchers();
	}
	/**
	 * Adjusts the player's life by a specific amount.
	 * @param dmg the amount
	 */
	private final void adjustLife(final float dmg) {
		String ls = getLifeAttribute();
		PooledStringBuilder sb =
		        StringBuilderPool.getInstance().getStringBuilder();
		try {
			sb.append("M");
			sb.append(ls);
		} catch (PooledException e) {
			JOGLErrorHandler.getInstance().fatalError(e);
		}
		String mls = sb.toString();
		sb.returnToPool();
		sb = null;
		setBaseAttributeScore(getLifeAttribute(), getBaseLife() + dmg);
		if (getBaseLife() > getFullAttributeScore(mls)) {
			// if Hit Points now > max
			setBaseAttributeScore(ls, getFullAttributeScore(mls));
		}
		if (getBaseLife() < 0f) {
			// if life now < 0
			setBaseAttributeScore(ls, 0f);
		}
		ls = null;
		mls = null;
	}
	/**
	 * Adjusts the player's mana by a specific amount.
	 * @param dmg the amount
	 */
	protected abstract void adjustMana(float dmg);
	/**
	 * Adjusts the {@link IoPcData}'s experience points.
	 * @param val the amount adjusted by
	 */
	public final void adjustXp(final int val) {
		xp += val;
		if (xp < 0) {
			xp = 0;
		}
		notifyWatchers();
	}
	/**
	 * Damages the player.
	 * @param dmg the damage amount
	 * @param type the type of damage
	 * @param source the source of the damage
	 * @return {@link float}
	 * @throws RPGException if an error occurs
	 */
	public final float ARX_DAMAGES_DamagePlayer(final float dmg,
	        final long type,
	        final int source) throws RPGException {
		float damagesdone = 0.f;
		computeFullStats();
		if (!io.hasIOFlag(IoGlobals.PLAYERFLAGS_INVULNERABILITY)
		        && getBaseLife() > 0) {
			if (dmg > getBaseLife()) {
				damagesdone = getBaseLife();
			} else {
				damagesdone = dmg;
			}
			io.setDamageSum(io.getDamageSum() + dmg);

			// TODO - add timer for ouch
			// if (ARXTime > inter.iobj[0]->ouch_time + 500) {
			IO oes = (IO) Script.getInstance().getEventSender();

			if (Interactive.getInstance().hasIO(source)) {
				Script.getInstance()
				        .setEventSender(Interactive
				                .getInstance().getIO(source));
			} else {
				Script.getInstance().setEventSender(null);
			}
			Script.getInstance().sendIOScriptEvent(io,
			        ScriptConsts.SM_045_OUCH,
			        new Object[] { "OUCH", io.getDamageSum(),
			                "SUMMONED_OUCH", 0f },
			        null);
			Script.getInstance().setEventSender(oes);
			io.setDamageSum(0);
			// }

			if (dmg > 0.f) {
				if (Interactive.getInstance().hasIO(source)) {
					IO poisonWeaponIO = null;
					IO sourceIO =
					        (IO) Interactive.getInstance()
					                .getIO(source);

					if (sourceIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
						poisonWeaponIO = (IO) sourceIO.getNPCData().getWeapon();
						if (poisonWeaponIO != null
						        && (poisonWeaponIO.getPoisonLevel() == 0
						                || poisonWeaponIO
						                        .getPoisonCharges() == 0)) {
							poisonWeaponIO = null;
						}
					}

					if (poisonWeaponIO == null) {
						poisonWeaponIO = sourceIO;
					}

					if (poisonWeaponIO != null
					        && poisonWeaponIO.getPoisonLevel() > 0
					        && poisonWeaponIO.getPoisonCharges() > 0) {
						// TODO - handle poisoning

						if (poisonWeaponIO.getPoisonCharges() > 0) {
							poisonWeaponIO.setPoisonCharges(
							        poisonWeaponIO.getPoisonCharges() - 1);
						}
					}
				}

				boolean alive;
				if (getBaseLife() > 0) {
					alive = true;
				} else {
					alive = false;
				}
				adjustLife(-dmg);

				if (getBaseLife() <= 0.f) {
					adjustLife(-getBaseLife());
					if (alive) {
						// TODO - what is this?
						// REFUSE_GAME_RETURN = true;
						becomesDead();

						// TODO - play fire sounds
						// if (type & DAMAGE_TYPE_FIRE
						// || type & DAMAGE_TYPE_FAKEFIRE) {
						// ARX_SOUND_PlayInterface(SND_PLAYER_DEATH_BY_FIRE);
						// }

						Script.getInstance().sendIOScriptEvent(io,
						        ScriptConsts.SM_017_DIE, null, null);

						int i = Interactive.getInstance().getMaxIORefId();
						for (; i >= 0; i--) {
							if (!Interactive.getInstance().hasIO(i)) {
								continue;
							}
							IO ioo = (IO) Interactive.getInstance().getIO(i);
							// tell all IOs not to target player anymore
							if (ioo != null
							        && ioo.hasIOFlag(IoGlobals.IO_03_NPC)) {
								if (ioo.getTargetinfo() == io.getRefId()
								        || ioo.getTargetinfo() == IoGlobals.TARGET_PLAYER) {
									Script.getInstance()
									        .setEventSender(io);
									String killer = "";
									if (source == io.getRefId()) {
										killer = "PLAYER";
									} else if (source <= -1) {
										killer = "NONE";
									} else if (Interactive.getInstance()
									        .hasIO(source)) {
										IO sourceIO =
										        (IO) Interactive
										                .getInstance().getIO(
										                        source);
										if (sourceIO.hasIOFlag(
										        IoGlobals.IO_03_NPC)) {
											killer = new String(
											        sourceIO.getNPCData()
											                .getName());
										}
									}
									Script.getInstance().sendIOScriptEvent(ioo,
									        0,
									        new Object[] { "tmp_int1", source },
									        "TargetDeath");
								}
							}
						}
					}
				}
			}
		}
		return damagesdone;
	}
	/**
	 * Drains mana from the NPC, returning the full amount drained.
	 * @param dmg the attempted amount of mana to be drained
	 * @return {@link float}
	 */
	public final float ARX_DAMAGES_DrainMana(final float dmg) {
		float manaDrained = 0;
		if (!io.hasIOFlag(IoGlobals.PLAYERFLAGS_NO_MANA_DRAIN)) {
			if (getBaseMana() >= dmg) {
				adjustMana(-dmg);
				manaDrained = dmg;
			} else {
				manaDrained = getBaseMana();
				adjustMana(-manaDrained);
			}
		}
		return manaDrained;
	}
	/**
	 * Heals the player's mana.
	 * @param dmg the amount of healing
	 */
	public final void ARX_DAMAGES_HealManaPlayer(final float dmg) {
		if (getBaseLife() > 0.f) {
			if (dmg > 0.f) {
				adjustMana(dmg);
			}
		}
	}
	/**
	 * Heals the player.
	 * @param dmg the amount of healing
	 */
	public final void ARX_DAMAGES_HealPlayer(final float dmg) {
		if (getBaseLife() > 0.f) {
			if (dmg > 0.f) {
				// if (!BLOCK_PLAYER_CONTROLS)
				adjustLife(dmg);
			}
		}
	}
	/**
	 * Gets the type of weapon the player is wielding.
	 * @return {@link long}
	 * @throws PooledException if an error occurs
	 * @throws RPGException if an error occurs
	 */
	public final long ARX_EQUIPMENT_GetPlayerWeaponType() throws RPGException {
		int type = EquipmentGlobals.WEAPON_BARE;
		int wpnId = getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON);
		if (wpnId >= 0
		        && Interactive.getInstance().hasIO(wpnId)) {
			IO weapon = (IO) Interactive.getInstance().getIO(wpnId);
			if (weapon.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER)) {
				type = EquipmentGlobals.WEAPON_DAGGER;
			}
			if (weapon.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_1H)) {
				type = EquipmentGlobals.WEAPON_1H;
			}
			if (weapon.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_2H)) {
				type = EquipmentGlobals.WEAPON_2H;
			}
			if (weapon.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW)) {
				type = EquipmentGlobals.WEAPON_BOW;
			}
			weapon = null;
		}
		return type;
	}
	/**
	 * Determines if the player has an item equipped.
	 * @param itemIO the item
	 * @return <tt>true</tt> if the player has the item equipped; <tt>false</tt>
	 *         otherwise
	 * @throws PooledException if an error occurs
	 * @throws RPGException if an error occurs
	 */
	public final boolean ARX_EQUIPMENT_IsPlayerEquip(final IO itemIO)
	        throws RPGException {
		boolean isEquipped = false;
		int i = ProjectConstants.getInstance().getMaxEquipped() - 1;
		for (; i >= 0; i--) {
			if (this.getEquippedItem(i) >= 0
			        && Interactive.getInstance().hasIO(getEquippedItem(i))) {
				IO toequip = (IO) Interactive.getInstance().getIO(
				        getEquippedItem(i));
				if (toequip.equals(itemIO)) {
					isEquipped = true;
					break;
				}
			}
		}
		return isEquipped;
	}
	/** Re-creates the player's appearance. */
	public abstract void ARX_EQUIPMENT_RecreatePlayerMesh();
	/**
	 * Unequips the player's weapon.
	 * @throws PooledException if an error occurs
	 * @throws RPGException if an error occurs
	 */
	public final void ARX_EQUIPMENT_UnEquipPlayerWeapon()
	        throws RPGException {
		int wpnId = getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON);
		if (wpnId >= 0
		        && Interactive.getInstance().hasIO(wpnId)) {
			IO weapon = (IO) Interactive.getInstance().getIO(wpnId);
			weapon.getItemData().ARX_EQUIPMENT_UnEquip(io, false);
		}
		setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, -1);
	}
	/**
	 * Called when a player dies.
	 * @throws RPGException
	 */
	public final void becomesDead() throws RPGException {
		int i = ProjectConstants.getInstance().getMaxSpells() - 1;
		for (; i >= 0; i--) {
			Spell spell = SpellController.getInstance().getSpell(i);
			if (spell.exists()
			        && spell.getCaster() == io.getRefId()) {
				spell.setTimeToLive(0);
				spell.setTurnsToLive(0);
			}
		}
	}
	/**
	 * Determines if a PC can identify a piece of equipment.
	 * @param equipitem
	 * @return
	 */
	public abstract boolean canIdentifyEquipment(IOEquipItem equipitem);
	/** Clears all interface flags that were set. */
	public final void clearInterfaceFlags() {
		interfaceFlags = 0;
	}
	/**
	 * Gets the player's base life value from the correct attribute.
	 * @return {@link float}
	 */
	protected abstract float getBaseLife();
	/**
	 * Gets the player's base mana value from the correct attribute.
	 * @return {@link float}
	 */
	protected abstract float getBaseMana();
	/**
	 * Gets the {@link IoPcData}'s gender.
	 * @return int
	 */
	public final int getGender() {
		return gender;
	}
	/**
	 * Gets the character's gold.
	 * @return <code>float</code>
	 */
	public final float getGold() {
		return gold;
	}
	/**
	 * Gets the IO associated with this {@link IoPcData}.
	 * @return {@link IO}
	 */
	public final IO getIo() {
		return io;
	}
	/**
	 * Gets a specific key from the keyring.
	 * @param index the key's index
	 * @return {@link String}
	 */
	public final char[] getKey(final int index) {
		char[] key = null;
		if (keyring != null
		        && index >= 0
		        && index < keyring.length) {
			key = keyring[index];
		}
		return key;
	}
	/**
	 * Gets the index of a specific key.
	 * @param key the key's id.
	 * @return {@link int}
	 */
	private int getKeyIndex(final char[] key) {
		int index = -1;
		if (keyring == null) {
			keyring = new char[0][];
		}
		char[] keyCopy = new char[key.length];
		System.arraycopy(key, 0, keyCopy, 0, key.length);
		for (int i = keyCopy.length - 1; i >= 0; i--) {
			keyCopy[i] = Character.toLowerCase(keyCopy[i]);
		}
		for (int i = keyring.length - 1; i >= 0; i--) {
			char[] arrCopy = new char[keyring[i].length];
			System.arraycopy(keyring[i], 0, arrCopy, 0, keyring[i].length);
			for (int j = arrCopy.length - 1; j >= 0; j--) {
				arrCopy[j] = Character.toLowerCase(arrCopy[j]);
			}
			if (Arrays.equals(keyCopy, arrCopy)) {
				index = i;
				break;
			}
			arrCopy = null;
		}
		keyCopy = null;
		return index;
	}
	/**
	 * Gets the {@link IoPcData}'s level.
	 * @return int
	 */
	public final int getLevel() {
		return level;
	}
	protected abstract String getLifeAttribute();
	/**
	 * Gets the {@link IoPcData}'s name.
	 * @return char[]
	 */
	public final char[] getName() {
		return name;
	}
	/**
	 * Gets the value for the bags.
	 * @return {@link int}
	 */
	public int getNumberOfBags() {
		return bags;
	}
	/**
	 * Gets the number of keys on the key ring.
	 * @return {@link int}
	 */
	public final int getNumKeys() {
		return numKeys;
	}
	/**
	 * Gets the {@link IoPcData}'s Profession.
	 * @return int
	 */
	public final int getProfession() {
		return profession;
	}
	/**
	 * Gets the {@link IoPcData}'s Race.
	 * @return int
	 */
	public final int getRace() {
		return race;
	}
	/**
	 * Gets the {@link IoPcData}'s experience points.
	 * @return int
	 */
	public final long getXp() {
		return xp;
	}
	/**
	 * Determines if the {@link IoPcData} has a specific flag.
	 * @param flag the flag
	 * @return true if the {@link IoPcData} has the flag; false otherwise
	 */
	public final boolean hasInterfaceFlag(final long flag) {
		return (interfaceFlags & flag) == flag;
	}
	/**
	 * Determines if the PC has a key in their keyring.
	 * @param key the key's name
	 * @return <tt>true</tt> if the PC has the key <tt>false></tt> otherwise
	 */
	public final boolean hasKey(final char[] key) {
		boolean hasKey = false;
		if (keyring == null) {
			keyring = new char[0][];
		}
		char[] keyCopy = new char[key.length];
		System.arraycopy(key, 0, keyCopy, 0, key.length);
		for (int i = keyCopy.length - 1; i >= 0; i--) {
			keyCopy[i] = Character.toLowerCase(keyCopy[i]);
		}
		for (int i = keyring.length - 1; i >= 0; i--) {
			char[] arrCopy = new char[keyring[i].length];
			System.arraycopy(keyring[i], 0, arrCopy, 0, keyring[i].length);
			for (int j = arrCopy.length - 1; j >= 0; j--) {
				arrCopy[j] = Character.toLowerCase(arrCopy[j]);
			}
			if (Arrays.equals(keyCopy, arrCopy)) {
				hasKey = true;
				break;
			}
		}
		keyCopy = null;
		return hasKey;
	}
	/**
	 * Determines if the PC has a key in their keyring.
	 * @param key the key's name
	 * @return <tt>true</tt> if the PC has the key <tt>false></tt> otherwise
	 */
	public final boolean hasKey(final String key) {
		return hasKey(key.toCharArray());
	}
	/**
	 * Removes an interface flag.
	 * @param flag the flag
	 */
	public final void removeInterfaceFlag(final long flag) {
		interfaceFlags &= ~flag;
	}
	/**
	 * Removes a key.
	 * @param key the key's id
	 */
	public final void removeKey(final char[] key) {
		int index = getKeyIndex(key);
		if (index >= 0) {
			keyring = ArrayUtilities.getInstance().removeIndex(index, keyring);
			numKeys--;
		}
	}
	/**
	 * Removes a key.
	 * @param key the key's id
	 */
	public final void removeKey(final String key) {
		removeKey(key.toCharArray());
	}
	/**
	 * Sets the {@link IoPcData}'s gender.
	 * @param val the gender to set
	 */
	public final void setGender(final int val) {
		gender = val;
		notifyWatchers();
	}
	/**
	 * Sets the IO associated with the pc data.
	 * @param newIO the IO to set
	 */
	public final void setIo(final IO newIO) {
		io = newIO;
		if (newIO != null
		        && newIO.getPCData() == null) {
			newIO.setPCData(this);
		}
	}
	/**
	 * Sets the {@link IoPcData}'s level.
	 * @param val the level to set
	 */
	public final void setLevel(final int val) {
		level = val;
		notifyWatchers();
	}
	/**
	 * Sets the {@link IoPcData}'s name.
	 * @param val the name to set
	 */
	public final void setName(final char[] val) {
		name = val;
		notifyWatchers();
	}
	/**
	 * Sets the {@link IoPcData}'s name.
	 * @param val the name to set
	 */
	public final void setName(final String val) {
		name = val.toCharArray();
		notifyWatchers();
	}
	/**
	 * Sets the {@link IoPcData}'s Profession.
	 * @param val the profession to set
	 */
	public final void setProfession(final int val) {
		profession = val;
		notifyWatchers();
	}
	/**
	 * Sets the {@link IoPcData}'s Race.
	 * @param val the race to set
	 */
	public final void setRace(final int val) {
		race = val;
		notifyWatchers();
	}
}
