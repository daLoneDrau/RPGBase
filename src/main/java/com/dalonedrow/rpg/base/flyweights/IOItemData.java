/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.systems.Script;

/**
 * @author drau
 * @param <EQUIP> {@link IOEquipItem}
 * @param <IO> {@link BaseInteractiveObject}
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class IOItemData<IO extends BaseInteractiveObject> {
    /** the current number in an inventory slot. */
    private int count;
    /** the item's description. */
    private char[] description;
    /** modifier data for the item. */
    private IOEquipItem equipitem;
    /** dunno? */
    private char foodValue;
    /** the IO associated with this data. */
    private IO io;
    /** the item's name. */
    private char[] itemName;
    /** the item's light value. */
    private int lightValue;
    /** the maximum number of the item the player can own. */
    private int maxOwned;
    /** the item's price. */
    private float price;
    /** the type of ring the item is. */
    private int ringType;
    /** the amount of the item that can be stacked in one inventory slot. */
    private int stackSize;
    /** dunno? */
    private char stealvalue;
    /** the item's weight. */
    private float weight;
    private char[] title;
    /**
     * Creates a new instance of {@link IOItemData}.
     * @throws RPGException
     */
    public IOItemData() {
        equipitem = new IOEquipItem();
    }
    /**
     * Adjusts the {@link IOItemData}'s count.
     * @param val the amount adjusted by
     */
    public final void adjustCount(final int val) throws RPGException {
        if (count + val < 0) {
            throw new RPGException(ErrorMessage.INVALID_PARAM,
                    "Cannot remove that many items");
        }
        if (count + val > maxOwned) {
            throw new RPGException(ErrorMessage.INVALID_PARAM,
                    "Cannot add that many items");
        }
        count += val;
    }
    protected abstract float applyCriticalModifier();
    public float ARX_EQUIPMENT_ComputeDamages(final IO io_source,
            final IO io_target, float dmgModifier) throws RPGException {
        float damages = 0;
        // send event to target. someone attacked you!
        Script.getInstance().setEventSender(io_source);
        Script.getInstance().sendIOScriptEvent(io_target,
                ScriptConstants.SM_057_AGGRESSION, null, null);
        if (io_source != null
                && io_target != null) {
            if (!io_target.hasIOFlag(IoGlobals.IO_01_PC)
                    && !io_target.hasIOFlag(IoGlobals.IO_03_NPC)
            /* && io_target.hasIOFlag(IoGlobals.fix) */) {
                if (io_source.hasIOFlag(IoGlobals.IO_01_PC)) {
                    // player fixing the target object
                    // ARX_DAMAGES_DamageFIX(io_target, player.Full_damages, 0,
                    // 0);
                } else if (io_source.hasIOFlag(IoGlobals.IO_03_NPC)) {
                    // NPC fixing target
                    // ARX_DAMAGES_DamageFIX(io_target,
                    // io_source->_npcdata->damages, GetInterNum(io_source), 0);
                } else {
                    // unknown fixing target
                    // ARX_DAMAGES_DamageFIX(io_target, 1,
                    // GetInterNum(io_source), 0);
                }
            } else {
                float attack, ac;
                float backstab = 1.f;
                // weapon material
                String wmat = "BARE";
                // armor material
                String amat = "FLESH";
                boolean critical = false;
                if (io_source.hasIOFlag(IoGlobals.IO_01_PC)) {
                    int wpnId = io_source.getPCData().getEquippedItem(
                            EquipmentGlobals.EQUIP_SLOT_WEAPON);
                    if (Interactive.getInstance().hasIO(wpnId)) {
                        IO io = (IO) Interactive.getInstance().getIO(wpnId);
                        if (io.getWeaponmaterial() != null
                                && io.getWeaponmaterial().length() > 0) {
                            wmat = io.getWeaponmaterial();
                        }
                        io = null;
                    }
                    attack = io_source.getPCData().getFullDamage();
                    if (io_source.getPCData().calculateCriticalHit()
                            && Script.getInstance().sendIOScriptEvent(
                                    io_source, ScriptConstants.SM_054_CRITICAL,
                                    null, null) != ScriptConstants.REFUSE) {
                        critical = true;
                    }
                    damages = attack * dmgModifier;
                    if (io_source.getPCData().calculateBackstab()
                            && Script.getInstance().sendIOScriptEvent(
                                    io_source, ScriptConstants.SM_056_BACKSTAB,
                                    null, null) != ScriptConstants.REFUSE) {
                        backstab = this.getBackstabModifier();
                    }
                } else {
                    if (io_source.hasIOFlag(IoGlobals.IO_03_NPC)) {
                        int wpnId = io_source.getNPCData().getEquippedItem(
                                EquipmentGlobals.EQUIP_SLOT_WEAPON);
                        if (Interactive.getInstance().hasIO(wpnId)) {
                            IO io = (IO) Interactive.getInstance().getIO(wpnId);
                            if (io.getWeaponmaterial() != null
                                    && io.getWeaponmaterial().length() > 0) {
                                wmat = io.getWeaponmaterial();
                            }
                            io = null;
                        } else {
                            if (io_source.getWeaponmaterial() != null
                                    && io_source.getWeaponmaterial()
                                            .length() > 0) {
                                wmat = io_source.getWeaponmaterial();
                            }
                        }
                        attack = io_source.getNPCData().getFullDamage();
                        if (io_source.getNPCData().calculateCriticalHit()
                                && Script.getInstance().sendIOScriptEvent(
                                        io_source,
                                        ScriptConstants.SM_054_CRITICAL,
                                        null, null) != ScriptConstants.REFUSE) {
                            critical = true;
                        }
                        damages = attack * dmgModifier;
                        if (io_source.getNPCData().calculateBackstab()
                                && Script.getInstance().sendIOScriptEvent(
                                        io_source,
                                        ScriptConstants.SM_056_BACKSTAB,
                                        null, null) != ScriptConstants.REFUSE) {
                            backstab = this.getBackstabModifier();
                        }
                    } else {
                        throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                                "Compute Damages call made by non-character");
                    }
                }
                // calculate how much damage is absorbed by armor
                float absorb = this.calculateArmorDeflection();
                // float absorb;

                // if (io_target == inter.iobj[0]) {
                // ac = player.Full_armor_class;
                // absorb = player.Full_Skill_Defense * DIV2;
                // } else {
                // ac = ARX_INTERACTIVE_GetArmorClass(io_target);
                // absorb = io_target->_npcdata->absorb;
                // long value = ARX_SPELLS_GetSpellOn(io_target, SPELL_CURSE);
                // if (value >= 0) {
                // float modif = (spells[value].caster_level * 0.05f);
                // ac *= modif;
                // absorb *= modif;
                // }
                // }
                if (io_target.getArmormaterial() != null
                        && io.getArmormaterial().length() > 0) {
                    amat = io.getArmormaterial();
                }
                if (io_target.hasIOFlag(IoGlobals.IO_03_NPC)
                        || io_target.hasIOFlag(IoGlobals.IO_01_PC)) {
                    int armrId;
                    if (io_target.hasIOFlag(IoGlobals.IO_03_NPC)) {
                        armrId = io_target.getNPCData().getEquippedItem(
                                EquipmentGlobals.EQUIP_SLOT_TORSO);
                    } else {
                        armrId = io_target.getPCData().getEquippedItem(
                                EquipmentGlobals.EQUIP_SLOT_TORSO);
                    }
                    if (Interactive.getInstance().hasIO(armrId)) {
                        IO io = (IO) Interactive.getInstance().getIO(armrId);
                        if (io.getArmormaterial() != null
                                && io.getArmormaterial().length() > 0) {
                            amat = io.getArmormaterial();
                        }
                        io = null;
                    }
                }
                damages *= backstab;
                // dmgs -= dmgs * (absorb * DIV100);

                // TODO - play sound based on the power of the hit
                if (damages > 0.f) {
                    if (critical) {
                        damages = this.applyCriticalModifier();
                        // dmgs *= 1.5f;
                    }

                    if (io_target.hasIOFlag(IoGlobals.IO_01_PC)) {
                        // TODO - push player when hit
                        // ARX_DAMAGES_SCREEN_SPLATS_Add(&ppos, dmgs);
                        io_target.getPCData().ARX_DAMAGES_DamagePlayer(damages,
                                0, io_source.getRefId());
                        // ARX_DAMAGES_DamagePlayerEquipment(dmgs);
                    } else {
                        // TODO - push NPC when hit
                        io_target.getNPCData().damageNPC(damages,
                                io_source.getRefId(), false);
                    }
                }
            }

        }
        return damages;
    }
    /**
     * Equips the item on a target IO.
     * @param target the target IO
     * @throws PooledException if an error occurs
     * @throws RPGException if an error occurs
     */
    public final void ARX_EQUIPMENT_Equip(final IO target)
            throws RPGException {
        if (io == null) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR,
                    "Cannot equip item with no IO data");
        }
        if (target != null) {
            if (target.hasIOFlag(IoGlobals.IO_01_PC)
                    || target.hasIOFlag(IoGlobals.IO_03_NPC)) {
                IOCharacter charData;
                if (target.hasIOFlag(IoGlobals.IO_01_PC)) {
                    charData = target.getPCData();
                } else {
                    charData = target.getNPCData();
                }
                int validid = -1;
                int i = Interactive.getInstance().getMaxIORefId();
                for (; i >= 0; i--) {
                    if (Interactive.getInstance().hasIO(i)
                            && Interactive.getInstance().getIO(i) != null
                            && io.equals(Interactive.getInstance().getIO(i))) {
                        validid = i;
                        break;
                    }
                }
                if (validid >= 0) {
                    Interactive.getInstance().RemoveFromAllInventories(io);
                    io.setShow(IoGlobals.SHOW_FLAG_ON_PLAYER); // on player
                    // handle drag
                    // if (toequip == DRAGINTER)
                    // Set_DragInter(NULL);
                    if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_WEAPON)) {
                        equipWeapon(charData);
                    } else if (io
                            .hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_SHIELD)) {
                        equipShield(charData);
                    } else if (io
                            .hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_RING)) {
                        equipRing(charData);
                    } else if (io
                            .hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_ARMOR)) {
                        // unequip old armor
                        unequipItemInSlot(
                                charData, EquipmentGlobals.EQUIP_SLOT_TORSO);
                        // equip new armor
                        charData.setEquippedItem(
                                EquipmentGlobals.EQUIP_SLOT_TORSO, validid);
                    } else if (io
                            .hasTypeFlag(
                                    EquipmentGlobals.OBJECT_TYPE_LEGGINGS)) {
                        // unequip old leggings
                        unequipItemInSlot(
                                charData, EquipmentGlobals.EQUIP_SLOT_LEGGINGS);
                        // equip new leggings
                        charData.setEquippedItem(
                                EquipmentGlobals.EQUIP_SLOT_LEGGINGS, validid);
                    } else if (io
                            .hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_HELMET)) {
                        // unequip old helmet
                        unequipItemInSlot(
                                charData, EquipmentGlobals.EQUIP_SLOT_HELMET);
                        // equip new helmet
                        charData.setEquippedItem(
                                EquipmentGlobals.EQUIP_SLOT_HELMET, validid);
                    }
                    if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_HELMET)
                            || io.hasTypeFlag(
                                    EquipmentGlobals.OBJECT_TYPE_ARMOR)
                            || io.hasTypeFlag(
                                    EquipmentGlobals.OBJECT_TYPE_LEGGINGS)) {
                        charData.ARX_EQUIPMENT_RecreatePlayerMesh();
                    }
                    charData.computeFullStats();
                }
            }
        }
    }
    public final void ARX_EQUIPMENT_ReleaseAll() {
        ARX_EQUIPMENT_ReleaseEquipItem();
    }
    /** Releases the {@link IOEquipItem} data from the item. */
    public final void ARX_EQUIPMENT_ReleaseEquipItem() {
        if (equipitem != null) {
            equipitem = null;
        }
    }
    /**
     * Sets the item's object type.
     * @param flag the type flag
     * @param added if <tt>true</tt>, the type is set; otherwise it is removed
     * @throws RPGException if an error occurs
     */
    public final void ARX_EQUIPMENT_SetObjectType(final int flag,
            final boolean added) throws RPGException {
        if (added) {
            io.addTypeFlag(flag);
        } else {
            io.removeTypeFlag(flag);
        }
    }
    /**
     * Unequips the item from the targeted IO.
     * @param target the targeted IO
     * @param isDestroyed if<tt>true</tt> the item is destroyed afterwards
     * @throws PooledException if an error occurs
     * @throws RPGException if an error occurs
     */
    public void ARX_EQUIPMENT_UnEquip(final IO target,
            final boolean isDestroyed) throws RPGException {
        if (target != null) {
            if (target.hasIOFlag(IoGlobals.IO_01_PC)) {
                int i = ProjectConstants.getInstance().getMaxEquipped() - 1;
                for (; i >= 0; i--) {
                    IoPcData player = target.getPCData();
                    int itemRefId = player.getEquippedItem(i);
                    if (itemRefId >= 0
                            && Interactive.getInstance().hasIO(itemRefId)
                            && Interactive.getInstance().getIO(
                                    itemRefId).equals(io)) {
                        // EERIE_LINKEDOBJ_UnLinkObjectFromObject(
                        // target->obj, tounequip->obj);
                        player.ARX_EQUIPMENT_Release(itemRefId);
                        // target->bbox1.x = 9999;
                        // target->bbox2.x = -9999;

                        if (!isDestroyed) {
                            // if (DRAGINTER == null) {
                            // ARX_SOUND_PlayInterface(SND_INVSTD);
                            // Set_DragInter(tounequip);
                            // } else
                            if (!target.getInventory().CanBePutInInventory(
                                    io)) {
                                target.getInventory().PutInFrontOfPlayer(
                                        io, true);
                            }
                        }
                        // send event from this item to target to unequip
                        Script.getInstance().setEventSender(io);
                        Script.getInstance().sendIOScriptEvent(target,
                                ScriptConsts.SM_007_EQUIPOUT, null, null);
                        // send event from target to this item to unequip
                        Script.getInstance().setEventSender(target);
                        Script.getInstance().sendIOScriptEvent(io,
                                ScriptConsts.SM_007_EQUIPOUT, null, null);
                    }
                }
                if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_HELMET)
                        || io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_ARMOR)
                        || io.hasTypeFlag(
                                EquipmentGlobals.OBJECT_TYPE_LEGGINGS)) {
                    target.getPCData().ARX_EQUIPMENT_RecreatePlayerMesh();
                }
            }
        }
    }
    protected abstract float calculateArmorDeflection();
    /**
     * Equips a ring on a character.
     * @param charData the character data
     * @throws RPGException if an error occurs
     */
    private void equipRing(final IOCharacter charData) throws RPGException {
        // check left and right finger
        // to see if it can be equipped
        boolean canEquip = true;
        int ioid = charData.getEquippedItem(
                EquipmentGlobals.EQUIP_SLOT_RING_LEFT);
        if (Interactive.getInstance().hasIO(ioid)) {
            IO oldRing = (IO) Interactive.getInstance().getIO(ioid);
            if (oldRing.getItemData().getRingType() == ringType) {
                // already wearing that type
                // of ring on left finger
                canEquip = false;
            }
            oldRing = null;
        }
        if (canEquip) {
            ioid = charData.getEquippedItem(
                    EquipmentGlobals.EQUIP_SLOT_RING_RIGHT);
            if (Interactive.getInstance().hasIO(ioid)) {
                IO oldRing = (IO) Interactive.getInstance().getIO(ioid);
                if (oldRing.getItemData().getRingType() == ringType) {
                    // already wearing that type
                    // of ring on right finger
                    canEquip = false;
                }
                oldRing = null;
            }
        }
        if (canEquip) {
            int equipSlot = -1;
            if (charData.getEquippedItem(
                    EquipmentGlobals.EQUIP_SLOT_RING_LEFT) < 0) {
                equipSlot = EquipmentGlobals.EQUIP_SLOT_RING_LEFT;
            }
            if (charData.getEquippedItem(
                    EquipmentGlobals.EQUIP_SLOT_RING_RIGHT) < 0) {
                equipSlot = EquipmentGlobals.EQUIP_SLOT_RING_RIGHT;
            }
            if (equipSlot == -1) {
                if (!charData.getIo().getInventory().isLeftRing()) {
                    ioid = charData.getEquippedItem(
                            EquipmentGlobals.EQUIP_SLOT_RING_RIGHT);
                    if (Interactive.getInstance().hasIO(ioid)) {
                        IO oldIO = (IO) Interactive.getInstance().getIO(ioid);
                        if (oldIO.hasIOFlag(IoGlobals.IO_02_ITEM)) {
                            unequipItemInSlot(charData,
                                    EquipmentGlobals.EQUIP_SLOT_RING_RIGHT);
                        }
                        oldIO = null;
                    }
                    equipSlot =
                            EquipmentGlobals.EQUIP_SLOT_RING_RIGHT;
                } else {
                    ioid = charData.getEquippedItem(
                            EquipmentGlobals.EQUIP_SLOT_RING_LEFT);
                    if (Interactive.getInstance().hasIO(ioid)) {
                        IO oldIO = (IO) Interactive.getInstance().getIO(ioid);
                        if (oldIO.hasIOFlag(IoGlobals.IO_02_ITEM)) {
                            unequipItemInSlot(charData,
                                    EquipmentGlobals.EQUIP_SLOT_RING_LEFT);
                        }
                        oldIO = null;
                    }
                    equipSlot = EquipmentGlobals.EQUIP_SLOT_RING_LEFT;
                }
                charData.getIo().getInventory().setLeftRing(
                        !charData.getIo().getInventory().isLeftRing());
            }
            charData.setEquippedItem(equipSlot, io.getRefId());
        }
    }
    /**
     * Equips a shield on a character.
     * @param charData the character data
     * @throws RPGException if an error occurs
     */
    private void equipShield(final IOCharacter charData) throws RPGException {
        // unequip old shield
        unequipItemInSlot(charData, EquipmentGlobals.EQUIP_SLOT_SHIELD);
        // equip new shield
        charData.setEquippedItem(
                EquipmentGlobals.EQUIP_SLOT_SHIELD, io.getRefId());
        // TODO - attach new shield to mesh
        // EERIE_LINKEDOBJ_LinkObjectToObject(target->obj,
        // io->obj, "SHIELD_ATTACH", "SHIELD_ATTACH", io);
        int wpnID =
                charData.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON);
        if (wpnID >= 0) {
            if (Interactive.getInstance().hasIO(wpnID)) {
                IO wpn = (IO) Interactive.getInstance().getIO(wpnID);
                if (wpn.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_2H)
                        || wpn.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW)) {
                    // unequip old weapon
                    unequipItemInSlot(
                            charData, EquipmentGlobals.EQUIP_SLOT_WEAPON);
                }
            }
        }
    }
    /**
     * Equips a weapon for a character.
     * @param charData the character data
     * @throws RPGException if an error occurs
     */
    private void equipWeapon(final IOCharacter charData) throws RPGException {
        // unequip old weapon
        unequipItemInSlot(charData, EquipmentGlobals.EQUIP_SLOT_WEAPON);
        // equip new weapon
        charData.setEquippedItem(
                EquipmentGlobals.EQUIP_SLOT_WEAPON, io.getRefId());
        // attach it to player mesh
        if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW)) {
            // EERIE_LINKEDOBJ_LinkObjectToObject(
            // target->obj, io->obj,
            // "WEAPON_ATTACH", "TEST", io);
        } else {
            // EERIE_LINKEDOBJ_LinkObjectToObject(
            // target->obj,
            // io->obj,
            // "WEAPON_ATTACH", "PRIMARY_ATTACH", io); //
        }
        if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_2H)
                || io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW)) {
            // for bows or 2-handed swords, unequip old shield
            unequipItemInSlot(charData, EquipmentGlobals.EQUIP_SLOT_SHIELD);
        }
    }
    protected abstract float getBackstabModifier();
    /**
     * Gets the current number in an inventory slot.
     * @return {@link short}
     */
    public final int getCount() {
        return count;
    }
    /**
     * Gets the item's description.
     * @return {@link char[]}
     */
    public final char[] getDescription() {
        return description;
    }
    /**
     * Gets the list of equipment item modifiers.
     * @return {@link EquipmentItemModifier}[]
     */
    public final IOEquipItem getEquipitem() {
        return equipitem;
    }
    /**
     * Gets the value for the foodValue.
     * @return {@link char}
     */
    public char getFoodValue() {
        return foodValue;
    }
    /**
     * Gets the IO associated with this data.
     * @return {@link IO}
     */
    public IO getIo() {
        return io;
    }
    /**
     * Gets the item's name.
     * @return <code>char</code>[]
     */
    public final char[] getItemName() {
        return itemName;
    }
    /**
     * Gets the value for the lightValue.
     * @return {@link int}
     */
    public int getLightValue() {
        return lightValue;
    }
    /**
     * Gets the maximum number of the item the player can own.
     * @return {@link int}
     */
    public final int getMaxOwned() {
        return maxOwned;
    }
    /**
     * Gets the item's price.
     * @return {@link float}
     */
    public final float getPrice() {
        return price;
    }
    /**
     * Gets the type of ring the item is.
     * @return {@link int}
     */
    public int getRingType() {
        return ringType;
    }
    /**
     * Gets the value for the stackSize.
     * @return {@link int}
     */
    public int getStackSize() {
        return stackSize;
    }
    /**
     * Gets the value for the stealvalue.
     * @return {@link char}
     */
    public char getStealvalue() {
        return stealvalue;
    }
    /**
     * Gets the type of weapon an item is.
     * @return {@link int}
     */
    public final int getWeaponType() {
        int type = EquipmentGlobals.WEAPON_BARE;
        if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER)) {
            type = EquipmentGlobals.WEAPON_DAGGER;
        } else if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_1H)) {
            type = EquipmentGlobals.WEAPON_1H;
        } else if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_2H)) {
            type = EquipmentGlobals.WEAPON_2H;
        } else if (io.hasTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW)) {
            type = EquipmentGlobals.WEAPON_BOW;
        }
        return type;
    }
    /**
     * Gets the item's weight.
     * @return {@link float}
     */
    public final float getWeight() {
        return weight;
    }
    /**
     * Sets the current number in an inventory slot.
     * @param val the new value to set
     */
    public final void setCount(final int val) {
        count = val;
    }
    /**
     * Sets the {@link IOItemData}'s description.
     * @param val the name to set
     * @throws RPGException if the parameter is null
     */
    public final void setDescription(final char[] val) throws RPGException {
        if (val == null) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Description cannot be null");
        }
        description = val;
    }
    /**
     * Sets the {@link IOItemData}'s description.
     * @param val the name to set
     * @throws RPGException if the parameter is null
     */
    public final void setDescription(final String val) throws RPGException {
        if (val == null) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Description cannot be null");
        }
        description = val.toCharArray();
    }
    /**
     * Sets the equipitem
     * @param val the equipitem to set
     */
    public void setEquipitem(final IOEquipItem val) {
        this.equipitem = val;
    }
    /**
     * Sets the value of the foodValue.
     * @param foodValue the new value to set
     */
    public void setFoodValue(final char foodValue) {
        this.foodValue = foodValue;
    }
    /**
     * Sets the the IO associated with this data.
     * @param val the new value to set
     */
    public void setIo(final IO val) {
        io = val;
        if (val != null
                && val.getItemData() == null) {
            val.setItemData(this);
        }
    }
    /**
     * Sets the item's name.
     * @param val the name to set
     * @throws RPGException if the parameter is null
     */
    public final void setItemName(final char[] val) throws RPGException {
        if (val == null) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Item name cannot be null");
        }
        this.itemName = val;
    }
    /**
     * Sets the item's name.
     * @param val the name to set
     * @throws RPGException if the parameter is null
     */
    public final void setItemName(final String val) throws RPGException {
        if (val == null) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Item name cannot be null");
        }
        this.itemName = val.toCharArray();
    }
    /**
     * Sets the value of the lightValue.
     * @param lightValue the new value to set
     */
    public void setLightValue(final int lightValue) {
        this.lightValue = lightValue;
    }
    /**
     * Sets the maximum number of the item the player can own.
     * @param val the new value
     */
    public final void setMaxOwned(final int val) {
        this.maxOwned = val;
    }
    /**
     * Sets the item's price.
     * @param val the price to set
     */
    public final void setPrice(final float val) {
        price = val;
    }
    /**
     * Sets the type of ring the item is.
     * @param val the new value to set
     */
    public void setRingType(final int val) {
        this.ringType = val;
    }
    /**
     * Sets the amount of the item that can be stacked in one inventory slot.
     * @param val the value to set
     */
    public void setStackSize(final int val) {
        this.stackSize = val;
    }
    /**
     * Sets the value of the stealvalue.
     * @param stealvalue the new value to set
     */
    public void setStealvalue(final char stealvalue) {
        this.stealvalue = stealvalue;
    }
    /**
     * Sets the item's weight.
     * @param f the weight to set
     */
    public final void setWeight(final float f) {
        weight = f;
    }
    private void unequipItemInSlot(final IOCharacter player, final int slot)
            throws RPGException {
        if (player.getEquippedItem(slot) >= 0) {
            int slotioid = player.getEquippedItem(slot);
            if (Interactive.getInstance().hasIO(slotioid)) {
                IO equipIO = (IO) Interactive.getInstance().getIO(slotioid);
                if (equipIO.hasIOFlag(IoGlobals.IO_02_ITEM)
                        && equipIO.getItemData() != null) {
                    equipIO.getItemData().ARX_EQUIPMENT_UnEquip(
                            player.getIo(), false);
                }
                equipIO = null;
            }
        }
    }
    public String getTitle() {
        return new String(title);
    }
    public void setTitle(String val) {
        title = val.toCharArray();
    }
}
