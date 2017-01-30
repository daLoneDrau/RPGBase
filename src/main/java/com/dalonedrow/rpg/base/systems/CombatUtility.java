package com.dalonedrow.rpg.base.systems;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * 
 * @author 588648
 *
 * @param <IO>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class CombatUtility<IO extends BaseInteractiveObject> {
    public void tryToHit() {
        // manage attempts to hit.
        
        // 1. check to see if IO has a weapon
        
        // 2. if IO has weapon and hits, call IO weapon compute damages
    }
    public float ARX_DAMAGES_DealDamages(final int target, final float dmg,
            final int source, final int flags)
            throws RPGException, PooledException {
        if (!Interactive.getInstance().hasIO(target)) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Invalid target object reference");
        }
        if (!Interactive.getInstance().hasIO(source)) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Invalid source object reference");
        }
        return ARX_DAMAGES_DealDamages(
                (IO) Interactive.getInstance().getIO(target),
                dmg,
                (IO) Interactive.getInstance().getIO(source),
                flags);
    }
    /**
     * @param targetIO
     * @param dmg
     * @param sourceIO
     * @param flags
     * @return
     * @throws RPGException
     * @throws PooledException
     */
    public float ARX_DAMAGES_DealDamages(final IO targetIO,
            final float dmg, final IO sourceIO,
            final int flags) throws RPGException, PooledException {
        if (!Interactive.getInstance().hasIO(targetIO)) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Invalid target object");
        }
        if (!Interactive.getInstance().hasIO(sourceIO)) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Invalid source object");
        }
        float damagesdone = 0;
        if (targetIO.hasIOFlag(IoGlobals.IO_01_PC)) {
            // check poison
            if (damageIsPoison(flags)) {
                // TODO - figure out poison resistance
                damagesdone = dmg;
            } else if (damageIsManaDrain(flags)) {
                damagesdone = dmg;
            } else {
                // no need to damage equipment in this game
                // ARX_DAMAGES_DamagePlayerEquipment(dmg);
                damagesdone = targetIO.getPCData().ARX_DAMAGES_DamagePlayer(
                        dmg, flags, sourceIO.getRefId());
            }
            damagesdone = damageByType(sourceIO, damagesdone, flags);
        } else if (targetIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
            if (damageIsPoison(flags)) {
                // TODO - figure out poison resistance
                damagesdone = dmg;
            } else {
                if (damageIsFire(flags)) {
                    // no fire damage (yet!)
                    // ARX_DAMAGES_IgnitIO(io_target, damagesdone);
                    damagesdone = dmg;
                }
                if (damageIsManaDrain(flags)) {
                    // ARX_DAMAGES_HealManaInter(io_source,
                    damagesdone = dmg;
                } else {
                    damagesdone = targetIO.getNPCData().damageNPC(
                            dmg, sourceIO.getRefId(), false);
                }
            }
            damagesdone = damageByType(sourceIO, damagesdone, flags);
        }
        return damagesdone;
    }
    /**
     * perform additional calculations for damage by type.
     * @param sourceIO the {@link IO} that is the source of the damage
     * @param damagesdone the amount of damages done
     * @param flags any flags applied to the damage type
     * @return {@link float}
     * @throws RPGException if an error occurs
     */
    protected float damageByType(final IO sourceIO,
            final float damagesdone, final int flags) throws RPGException {
        float dmg = damagesdone;
        if (damageIsFire(flags)) {
            // no fire damage (yet!)
            // ARX_DAMAGES_IgnitIO(io_target, damagesdone);
        }
        if ((flags
                & EquipmentGlobals.DAMAGE_TYPE_DRAIN_LIFE) == EquipmentGlobals.DAMAGE_TYPE_DRAIN_LIFE) {
            if (sourceIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
                sourceIO.getNPCData().healNPC(damagesdone);
            } else if (sourceIO.hasIOFlag(IoGlobals.IO_01_PC)) {
                sourceIO.getPCData().ARX_DAMAGES_HealPlayer(damagesdone);
            }
        }
        if ((flags
                & EquipmentGlobals.DAMAGE_TYPE_DRAIN_MANA) == EquipmentGlobals.DAMAGE_TYPE_DRAIN_MANA) {
            if (sourceIO.hasIOFlag(IoGlobals.IO_01_PC)) {
                sourceIO.getPCData().ARX_DAMAGES_HealManaPlayer(damagesdone);
            } else if (sourceIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
                sourceIO.getNPCData().ARX_DAMAGES_HealManaInter(damagesdone);
            }
        }
        if ((flags
                & EquipmentGlobals.DAMAGE_TYPE_PUSH) == EquipmentGlobals.DAMAGE_TYPE_PUSH) {
            // ARX_DAMAGES_PushIO(io_target, source, damagesdone *
            // DIV2);
        }
        if ((flags
                & EquipmentGlobals.DAMAGE_TYPE_MAGICAL) == EquipmentGlobals.DAMAGE_TYPE_MAGICAL
                && (flags
                        & EquipmentGlobals.DAMAGE_TYPE_FIRE) != EquipmentGlobals.DAMAGE_TYPE_FIRE
                && (flags
                        & EquipmentGlobals.DAMAGE_TYPE_COLD) != EquipmentGlobals.DAMAGE_TYPE_COLD) {
            // no need to do magical damages (yet)
            // damagesdone -= player.Full_resist_magic * DIV100 *
            // damagesdone;
            // damagesdone = __max(0, damagesdone);
        }
        return dmg;
    }
    /**
     * Determines if the damage type includes fire.
     * @param damageTypeFlags the damage type flags
     * @return <tt>true</tt> if the damage types include fire; <tt>false</tt>
     *         otherwise
     */
    protected boolean damageIsFire(final int damageTypeFlags) {
        return (damageTypeFlags
                & EquipmentGlobals.DAMAGE_TYPE_FIRE) == EquipmentGlobals.DAMAGE_TYPE_FIRE;
    }
    /**
     * Determines if the damage type includes mana drain.
     * @param damageTypeFlags the damage type flags
     * @return <tt>true</tt> if the damage types include mana drain;
     *         <tt>false</tt> otherwise
     */
    protected boolean damageIsManaDrain(final int damageTypeFlags) {
        return (damageTypeFlags
                & EquipmentGlobals.DAMAGE_TYPE_DRAIN_MANA) == EquipmentGlobals.DAMAGE_TYPE_DRAIN_MANA;
    }
    /**
     * Determines if the damage type includes poison.
     * @param damageTypeFlags the damage type flags
     * @return <tt>true</tt> if the damage types include poison; <tt>false</tt>
     *         otherwise
     */
    protected boolean damageIsPoison(final int damageTypeFlags) {
        return (damageTypeFlags
                & EquipmentGlobals.DAMAGE_TYPE_POISON) == EquipmentGlobals.DAMAGE_TYPE_POISON;
    }
    /**
     * Gets the damage from an IO's weapon. The minimum damage is 1.
     * @param io the IO
     * @return {@link int}
     * @throws PooledException if an error occurs
     * @throws RPGException if an error occurs
     */
    protected int getIOWeaponDamage(final IO io) throws RPGException {
        int dmg = 1;
        int wpnId = -1;
        if (io.hasIOFlag(IoGlobals.IO_01_PC)) {
            wpnId = io.getPCData().getEquippedItem(
                    EquipmentGlobals.EQUIP_SLOT_WEAPON);
        } else if (io.hasIOFlag(IoGlobals.IO_03_NPC)) {
            wpnId = io.getNPCData().getWeaponInHand();
        }
        if (Interactive.getInstance().hasIO(wpnId)) {
            IO wpnIO = (IO) Interactive.getInstance().getIO(wpnId);
            dmg = (int) wpnIO.getItemData().getEquipitem().getElement(
                    ProjectConstants.getInstance().getDamageElementIndex())
                    .getValue();
        }
        return dmg;
    }
}
