/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.constants.Behaviour;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.systems.Script;
import com.dalonedrow.utils.Watchable;

/**
 * @author drau
 * @param <IO>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class IoNpcData<IO extends BaseInteractiveObject>
        extends IOCharacter implements Watchable {
    public static final int MAX_STACKED_BEHAVIOR = 5;
    float absorb;
    long aiming_start;
    float aimtime;
    float armor_class;
    float armorClass;
    float backstab_skill;
    private int behavior;
    float behavior_param;
    float climb_count;
    long collid_state;
    long collid_time;
    float critical;
    long cut;
    short cuts;
    float damages;
    long detect;
    float fDetect;
    long fightdecision;
    /** the {@link IoNpcData}'s gender. */
    private int gender;
    /** the IO associated with this {@link IoNpcData}. */
    private IO io;
    float lastmouth;
    float life;
    float look_around_inc;
    long ltemp;
    float mana;
    float maxlife;
    float maxmana;
    private int movemode;
    float moveproblem;
    /** the {@link IoNpcData}'s name. */
    private char[] name;
    /** all NPC flags. */
    private long npcFlags;
    // IO_PATHFIND pathfind;
    // EERIE_EXTRA_ROTATE * ex_rotate;
    // D3DCOLOR blood_color;
    char padd;
    private IOPathfind pathfinder;
    // IO_BEHAVIOR_DATA stacked[MAX_STACKED_BEHAVIOR];
    float poisonned;
    float reach;
    private boolean reachedtarget; // Is
    // target
    // in
    // REACHZONE ?
    long reachedtime;
    char resist_fire;
    char resist_magic;
    char resist_poison;
    float speakpitch;
    private int splatDamages;
    private int splatTotNb;
    /** the stack of behaviors. */
    private final BehaviourData[] stacked;
    float stare_factor;
    short strike_time;
    private int tactics; // 0=none
    private int targetInfo;
    // ;
    /** the {@link IoNpcData}'s title. */
    private char[] title;
    // 1=side ;
    // 2=side+back
    float tohit;
    short unused;
    // EERIE_3D last_splat_pos;
    float vvpos;

    short walk_start_time;
    /** the NPC's weapon. */
    private IO weapon;
    private int weaponInHand;
    char[] weaponname = new char[256];
    long weapontype;
    private int xpvalue;
    /**
     * Creates a new instance of {@link IoNpcData}.
     * @throws RPGException if there is an error defining attributes
     */
    protected IoNpcData() throws RPGException {
        super();
        name = new char[0];
        stacked = new BehaviourData[MAX_STACKED_BEHAVIOR];
        pathfinder = new IOPathfind();
    }
    /**
     * Adds a behavior flag.
     * @param flag the flag
     */
    public void addBehavior(final Behaviour behaviorEnum) {
        behavior |= behaviorEnum.getFlag();
    }
    /**
     * Adds a behavior flag.
     * @param flag the flag
     */
    public void addBehavior(final long flag) {
        behavior |= flag;
    }
    /**
     * Adds an NPC flag.
     * @param flag the flag
     */
    public final void addNPCFlag(final long flag) {
        npcFlags |= flag;
    }
    /**
     * Adjusts the NPC's life by a specific amount.
     * @param dmg the amount
     */
    protected abstract void adjustLife(float dmg);
    /**
     * Adjusts the NPC's mana by a specific amount.
     * @param dmg the amount
     */
    protected abstract void adjustMana(float dmg);
    /**
     * Drains mana from the NPC, returning the full amount drained.
     * @param dmg the attempted amount of mana to be drained
     * @return {@link float}
     */
    public final float ARX_DAMAGES_DrainMana(final float dmg) {
        float manaDrained = 0;
        if (getBaseMana() >= dmg) {
            adjustMana(-dmg);
            manaDrained = dmg;
        } else {
            manaDrained = getBaseMana();
            adjustMana(-manaDrained);
        }
        return manaDrained;
    }
    /**
     * Heals the NPC's mana.
     * @param dmg the amount of healing
     */
    public final void ARX_DAMAGES_HealManaInter(final float dmg) {
        if (getBaseLife() > 0.f) {
            if (dmg > 0.f) {
                adjustMana(dmg);
            }
        }
    }
    public final void ARX_NPC_Behaviour_Change(final int newBehavior,
            final long params) {
        if (hasBehavior(Behaviour.BEHAVIOUR_FIGHT)
                && (newBehavior & Behaviour.BEHAVIOUR_FIGHT
                        .getFlag()) == Behaviour.BEHAVIOUR_FIGHT.getFlag()) {
            stopActiveAnimation();
            // ANIM_USE * ause1 = &io->animlayer[1];
            // AcquireLastAnim(io);
            // FinishAnim(io, ause1->cur_anim);
            // ause1->cur_anim = NULL;
        }

        if (hasBehavior(Behaviour.BEHAVIOUR_NONE)
                && (newBehavior & Behaviour.BEHAVIOUR_NONE
                        .getFlag()) == Behaviour.BEHAVIOUR_NONE.getFlag()) {
            stopIdleAnimation();
            // ANIM_USE * ause0 = &io->animlayer[0];
            // AcquireLastAnim(io);
            // FinishAnim(io, ause0->cur_anim);
            // ause0->cur_anim = NULL;
            // ANIM_Set(ause0, io->anims[ANIM_DEFAULT]);
            // ause0->flags &= ~EA_LOOP;

            stopActiveAnimation();
            // ANIM_USE * ause1 = &io->animlayer[1];
            // AcquireLastAnim(io);
            // FinishAnim(io, ause1->cur_anim);
            // ause1->cur_anim = NULL;
            // ause1->flags &= ~EA_LOOP;

            // stop whatever animation this is
            // ANIM_USE * ause2 = &io->animlayer[2];
            // AcquireLastAnim(io);
            // FinishAnim(io, ause2->cur_anim);
            // ause2->cur_anim = NULL;
            // ause2->flags &= ~EA_LOOP;
        }

        if ((newBehavior & Behaviour.BEHAVIOUR_FRIENDLY
                .getFlag()) == Behaviour.BEHAVIOUR_FRIENDLY.getFlag()) {
            stopIdleAnimation();
            // ANIM_USE * ause0 = &io->animlayer[0];
            // AcquireLastAnim(io);
            // FinishAnim(io, ause0->cur_anim);
            // ANIM_Set(ause0, io->anims[ANIM_DEFAULT]);
            // ause0->altidx_cur = 0;
        }
        clearBehavior();
        behavior = newBehavior;
        behavior_param = params;
    }
    public void ARX_NPC_Behaviour_Stack() {
        for (int i = 0; i < MAX_STACKED_BEHAVIOR; i++) {
            BehaviourData bd = stacked[i];
            if (!bd.exists()) {
                bd.setBehaviour(behavior);
                bd.setBehaviorParam(behavior_param);
                bd.setTactics(tactics);
                // set pathfinding information
                // if (io->_npcdata->pathfind.listnb > 0)
                // bd->target = io->_npcdata->pathfind.truetarget;
                // else
                // bd->target = io->targetinfo;

                bd.setMovemode(movemode);
                bd.setExists(true);
                return;
            }
        }
    }
    public void ARX_NPC_Behaviour_UnStack() {
        for (int i = 0; i < MAX_STACKED_BEHAVIOR; i++) {
            BehaviourData bd = stacked[i];
            if (bd.exists()) {
                // AcquireLastAnim(io);
                behavior = bd.getBehaviour();
                behavior_param = bd.getBehaviorParam();
                this.tactics = bd.getTactics();
                this.targetInfo = bd.getTarget();
                this.movemode = bd.getMoveMode();
                bd.setExists(false);
                // ARX_NPC_LaunchPathfind(io, bd->target);

                if (this.hasBehavior(Behaviour.BEHAVIOUR_NONE)) {
                    // memcpy(io->animlayer, bd->animlayer,
                    // sizeof(ANIM_USE)*MAX_ANIM_LAYERS);
                }
            }
        }
    }
    public abstract void ARX_NPC_ManagePoison();
    /**
     * Revives the NPC.
     * @param reposition if <tt>true</tt> NPC is moved to their initial position
     * @throws RPGException if an error occurs
     */
    public final void ARX_NPC_Revive(final boolean reposition)
            throws RPGException {
        // TODO - check if secondary inventory belongs to the NPC
        // and kill it
        // if ((TSecondaryInventory) && (TSecondaryInventory->io == io)) {
        // TSecondaryInventory = NULL;
        // }

        Script.getInstance().setMainEvent(getIo(), "MAIN");

        getIo().removeIOFlag(IoGlobals.IO_07_NO_COLLISIONS);
        restoreLifeToMax();
        Script.getInstance().resetObject(io, true);
        restoreLifeToMax();

        if (reposition) {
            moveToInitialPosition();
        }
        // reset texture - later
        // long goretex = -1;

        // for (long i = 0; i < io->obj->nbmaps; i++) {
        // if (io->obj->texturecontainer
        // && io->obj->texturecontainer[i]
        // && (IsIn(io->obj->texturecontainer[i]->m_strName, "GORE"))) {
        // goretex = i;
        // break;
        // }
        // }

        // for (long ll = 0; ll < io->obj->nbfaces; ll++) {
        // if (io->obj->facelist[ll].texid != goretex) {
        // io->obj->facelist[ll].facetype &= ~POLY_HIDE;
        // } else {
        // io->obj->facelist[ll].facetype |= POLY_HIDE;
        // }
        // }

        cuts = 0;
    }
    /** Clears all behavior flags that were set. */
    public void clearBehavior() {
        behavior = 0;
    }
    /** Clears all NPC flags that were set. */
    public final void clearNPCFlags() {
        npcFlags = 0;
    }
    /**
     * Damages an NPC.
     * @param dmg the amount of damage
     * @param source the source of the damage
     * @param isSpellDamage flag indicating whether the damage is from a spell
     * @return {@link float}
     * @throws RPGException if an error occurs
     * @throws PooledException if an error occurs
     */
    public final float damageNPC(final float dmg, final int source,
            final boolean isSpellDamage) throws PooledException, RPGException {
        float damagesdone = 0.f;
        if (io.getShow() > 0
                && !io.hasIOFlag(IoGlobals.IO_08_INVULNERABILITY)) {
            if (getBaseLife() <= 0f) {
                if (Interactive.getInstance().hasIO(source)) {
                    IO sourceIO = (IO) Interactive.getInstance().getIO(source);
                    if (sourceIO.hasIOFlag(IoGlobals.IO_01_PC)) {
                        // handle PC trying to damage non-living NPC
                    } else {
                        // handle NPC trying to damage non-living NPC
                    }
                    sourceIO = null;
                }
            } else {
                io.setDamageSum(io.getDamageSum() + dmg);
                if (Interactive.getInstance().hasIO(source)) {
                    Script.getInstance().setEventSender(
                            Interactive.getInstance().getIO(source));
                } else {
                    Script.getInstance().setEventSender(null);
                }
                Object[] params;
                if (Script.getInstance().getEventSender() != null
                        && Script.getInstance().getEventSender()
                                .getSummoner() == ProjectConstants.getInstance()
                                        .getPlayer()) {
                    IO playerIO = (IO) Interactive.getInstance().getIO(
                            ProjectConstants.getInstance().getPlayer());
                    Script.getInstance().setEventSender(playerIO);
                    playerIO = null;
                    params = new Object[] {
                            "SUMMONED_OUCH", io.getDamageSum(),
                            "OUCH", 0f };
                } else {
                    params = new Object[] {
                            "SUMMONED_OUCH", 0f,
                            "OUCH", io.getDamageSum() };
                }
                Script.getInstance().sendIOScriptEvent(io,
                        ScriptConsts.SM_045_OUCH, params, null);
                io.setDamageSum(0f);
                // TODO - remove Confusion spell when hit

                if (dmg >= 0.f) {
                    if (Interactive.getInstance().hasIO(source)) {
                        IO poisonWeaponIO = null;
                        IO sourceIO = (IO) Interactive.getInstance().getIO(
                                source);

                        if (sourceIO.hasIOFlag(IoGlobals.IO_01_PC)) {
                            IoPcData player = sourceIO.getPCData();
                            if (player.getEquippedItem(
                                    EquipmentGlobals.EQUIP_SLOT_WEAPON) > 0
                                    && Interactive.getInstance().hasIO(
                                            player.getEquippedItem(
                                                    EquipmentGlobals.EQUIP_SLOT_WEAPON))) {
                                poisonWeaponIO = (IO) Interactive.getInstance()
                                        .getIO(player.getEquippedItem(
                                                EquipmentGlobals.EQUIP_SLOT_WEAPON));

                                if (poisonWeaponIO != null
                                        && (poisonWeaponIO.getPoisonLevel() == 0
                                                || poisonWeaponIO
                                                        .getPoisonCharges() == 0)
                                        || isSpellDamage) {
                                    poisonWeaponIO = null;
                                }
                            }
                        } else {
                            if (sourceIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
                                poisonWeaponIO =
                                        (IO) sourceIO.getNPCData().getWeapon();
                                if (poisonWeaponIO != null
                                        && (poisonWeaponIO.getPoisonLevel() == 0
                                                || poisonWeaponIO
                                                        .getPoisonCharges() == 0)) {
                                    poisonWeaponIO = null;
                                }
                            }
                        }

                        if (poisonWeaponIO == null) {
                            poisonWeaponIO = sourceIO;
                        }

                        if (poisonWeaponIO != null
                                && poisonWeaponIO.getPoisonLevel() > 0
                                && poisonWeaponIO.getPoisonCharges() > 0) {
                            // TODO - apply poison damage

                            if (poisonWeaponIO.getPoisonCharges() > 0) {
                                poisonWeaponIO.setPoisonCharges(
                                        poisonWeaponIO.getPoisonCharges() - 1);
                            }
                        }
                        sourceIO = null;
                        poisonWeaponIO = null;
                    }

                    if (io.getScript() != null) {
                        if (Interactive.getInstance().hasIO(source)) {
                            Script.getInstance().setEventSender(
                                    Interactive
                                            .getInstance().getIO(source));
                        } else {
                            Script.getInstance().setEventSender(null);
                        }

                        if (Script.getInstance().getEventSender() != null
                                && Script.getInstance().getEventSender()
                                        .hasIOFlag(IoGlobals.IO_01_PC)) {
                            if (isSpellDamage) {
                                params = new Object[] { "SPELL_DMG", dmg };
                            } else {
                                IO wpnIO =
                                        (IO) Interactive
                                                .getInstance().getIO(
                                                        Script.getInstance()
                                                                .getEventSender()
                                                                .getPCData()
                                                                .getEquippedItem(
                                                                        EquipmentGlobals.EQUIP_SLOT_WEAPON));
                                int wpnType = EquipmentGlobals.WEAPON_BARE;
                                if (wpnIO != null) {
                                    wpnType =
                                            wpnIO.getItemData()
                                                    .getWeaponType();
                                }
                                switch (wpnType) {
                                case EquipmentGlobals.WEAPON_BARE:
                                    params = new Object[] { "BARE_DMG", dmg };
                                    break;
                                case EquipmentGlobals.WEAPON_DAGGER:
                                    params = new Object[] { "DAGGER_DMG", dmg };
                                    break;
                                case EquipmentGlobals.WEAPON_1H:
                                    params = new Object[] { "1H_DMG", dmg };
                                    break;
                                case EquipmentGlobals.WEAPON_2H:
                                    params = new Object[] { "2H_DMG", dmg };
                                    break;
                                case EquipmentGlobals.WEAPON_BOW:
                                    params = new Object[] { "ARROW_DMG", dmg };
                                    break;
                                default:
                                    params = new Object[] { "DMG", dmg };
                                    break;
                                }
                                wpnIO = null;
                            }
                        } else {
                            params = new Object[] { "DMG", dmg };
                        }
                        if (Script.getInstance().getEventSender() != null
                                && Script.getInstance().getEventSender()
                                        .getSummoner() == ProjectConstants
                                                .getInstance().getPlayer()) {
                            IO playerIO = (IO) Interactive.getInstance().getIO(
                                    ProjectConstants.getInstance().getPlayer());
                            Script.getInstance().setEventSender(playerIO);
                            playerIO = null;
                            params = new Object[] { "SUMMONED_DMG", dmg };
                        }

                        if (Script.getInstance().sendIOScriptEvent(
                                io, ScriptConsts.SM_016_HIT, params,
                                null) != ScriptConsts.ACCEPT) {
                            return damagesdone;
                        }
                    }

                    damagesdone = Math.min(dmg, getBaseLife());
                    adjustLife(-dmg);

                    if (getBaseAttributeScore("ST") <= 0.f) {
                        // base life should be 0

                        if (Interactive.getInstance().hasIO(source)) {
                            int xp = xpvalue;
                            forceDeath(
                                    (IO) Interactive
                                            .getInstance().getIO(source));

                            if (source == ProjectConstants.getInstance()
                                    .getPlayer()) {
                                IO playerIO =
                                        (IO) Interactive.getInstance().getIO(
                                                ProjectConstants.getInstance()
                                                        .getPlayer());
                                playerIO.getPCData().adjustXp(xp);
                            }
                        } else {
                            forceDeath(null);
                        }
                    }
                }
            }
        }
        return damagesdone;
    }
    /**
     * Forces the NPC to die.
     * @param killerIO the IO that killed the NPC
     * @throws RPGException if an error occurs
     */
    public final void forceDeath(final IO killerIO) throws RPGException {
        if (io.getMainevent().equalsIgnoreCase("DEAD")) {
            IO oldSender = (IO) Script.getInstance().getEventSender();
            Script.getInstance().setEventSender(killerIO);

            // TODO - reset drag IO
            // if (io == DRAGINTER)
            // Set_DragInter(NULL);

            // TODO - reset flying over (with mouse) IO
            // if (io == FlyingOverIO)
            // FlyingOverIO = NULL;

            // TODO - reset camera 1 when pointing to IO
            // if ((MasterCamera.exist & 1) && (MasterCamera.io == io))
            // MasterCamera.exist = 0;

            // TODO - reset camera 2 when pointing to IO
            // if ((MasterCamera.exist & 2) && (MasterCamera.want_io == io))
            // MasterCamera.exist = 0;

            // TODO - kill dynamic lighting for IO
            // if (ValidDynLight(io->dynlight))
            // DynLight[io->dynlight].exist = 0;

            // io->dynlight = -1;

            // if (ValidDynLight(io->halo.dynlight))
            // DynLight[io->halo.dynlight].exist = 0;

            // io->halo.dynlight = -1;

            // reset all behaviors
            resetBehavior();

            // TODO - kill speeches
            // ARX_SPEECH_ReleaseIOSpeech(io);

            // Kill all Timers...
            Script.getInstance().timerClearByIO(io);

            if (io.getMainevent().equalsIgnoreCase("DEAD")) {
                Script.getInstance().notifyIOEvent(
                        io, ScriptConsts.SM_017_DIE, "");
            }

            if (Interactive.getInstance().hasIO(io)) {
                Script.getInstance().setMainEvent(io, "DEAD");

                // TODO - kill animations
                // if (EEDistance3D(&io_dead->pos, &ACTIVECAM->pos) > 3200) {
                // io_dead->animlayer[0].ctime = 9999999;
                // io_dead->lastanimtime = 0;
                // }

                // set killer
                String killer = "";

                setWeaponInHand(-1);

                Interactive.getInstance().ARX_INTERACTIVE_DestroyDynamicInfo(
                        io);

                // set killer name
                if (killerIO != null
                        && killerIO.hasIOFlag(IoGlobals.IO_01_PC)) {
                    killer = "PLAYER";
                } else if (killerIO != null
                        && killerIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
                    killer = new String(killerIO.getNPCData().getName());
                }
                int i = Interactive.getInstance().getMaxIORefId();
                for (; i >= 0; i--) {
                    IO ioo = (IO) Interactive.getInstance().getIO(i);
                    if (ioo == null) {
                        continue;
                    }
                    if (ioo.equals(io)) {
                        continue;
                    }
                    if (ioo.hasIOFlag(IoGlobals.IO_03_NPC)) {
                        if (Interactive.getInstance().hasIO(
                                ioo.getTargetinfo())) {
                            if (Interactive.getInstance().getIO(
                                    ioo.getTargetinfo()).equals(io)) {
                                Script.getInstance().setEventSender(io);
                                Script.getInstance().stackSendIOScriptEvent(ioo,
                                        0,
                                        new Object[] { "killer", killer },
                                        "TARGET_DEATH");
                                ioo.setTargetinfo(IoGlobals.TARGET_NONE);
                                ioo.getNPCData().setReachedtarget(false);
                            }
                        }
                        // TODO - handle pathfinding target cleanup
                        // if (ValidIONum(ioo->_npcdata->pathfind.truetarget)) {
                        // if (inter.iobj[ioo->_npcdata->pathfind.truetarget] ==
                        // io_dead) {
                        // EVENT_SENDER = io_dead;
                        // Stack_SendIOScriptEvent(inter.iobj[i], 0, killer,
                        // "TARGET_DEATH");
                        // ioo->_npcdata->pathfind.truetarget = TARGET_NONE;
                        // ioo->_npcdata->reachedtarget = 0;
                        // }
                        // }
                    }
                }

                // TODO - kill animations
                // IO_UnlinkAllLinkedObjects(io_dead);
                // io_dead->animlayer[1].cur_anim = NULL;
                // io_dead->animlayer[2].cur_anim = NULL;
                // io_dead->animlayer[3].cur_anim = NULL;

                // reduce life to 0
                adjustLife(-99999);

                if (getWeapon() != null) {
                    IO ioo = getWeapon();
                    if (Interactive.getInstance().hasIO(ioo)) {
                        ioo.setShow(IoGlobals.SHOW_FLAG_IN_SCENE);
                        ioo.addIOFlag(IoGlobals.IO_07_NO_COLLISIONS);
                        // TODO - reset positioning and velocity
                        // ioo->pos.x =
                        // ioo->obj->vertexlist3[ioo->obj->origin].v.x;
                        // ioo->pos.y =
                        // ioo->obj->vertexlist3[ioo->obj->origin].v.y;
                        // ioo->pos.z =
                        // ioo->obj->vertexlist3[ioo->obj->origin].v.z;
                        // ioo->velocity.x = 0.f;
                        // ioo->velocity.y = 13.f;
                        // ioo->velocity.z = 0.f;
                        // ioo->stopped = 0;
                    }
                }
                Script.getInstance().setEventSender(oldSender);
            }
        }
    }
    /**
     * Gets the armorClass
     * @return {@link float}
     */
    public float getArmorClass() {
        return armorClass;
    }
    /**
     * Gets the NPC's base life value from the correct attribute.
     * @return {@link float}
     */
    public abstract float getBaseLife();
    /**
     * Gets the NPC's base mana value from the correct attribute.
     * @return {@link float}
     */
    public abstract float getBaseMana();
    /**
     * Gets the {@link IoNpcData}'s gender.
     * @return int
     */
    public final int getGender() {
        return gender;
    }
    /**
     * Gets the IO associated with this {@link IoNpcData}.
     * @return {@link IO}
     */
    @Override
    public final IO getIo() {
        return io;
    }
    /**
     * Gets the value for the movemode.
     * @return {@link int}
     */
    public int getMovemode() {
        return movemode;
    }
    /**
     * Gets the {@link IoNpcData}'s name.
     * @return char[]
     */
    public final char[] getName() {
        return name;
    }
    public IOPathfind getPathfinding() {
        return pathfinder;
    }
    public abstract int getPoisonned();
    /**
     * Gets the splatDamages
     * @return {@link int}
     */
    public int getSplatDamages() {
        return splatDamages;
    }
    /**
     * Gets the splatTotNb
     * @return {@link int}
     */
    public int getSplatTotNb() {
        return splatTotNb;
    }
    /**
     * Gets the value for the tactics.
     * @return {@link int}
     */
    public int getTactics() {
        return tactics;
    }
    /**
     * Gets the {@link IoNpcData}'s title.
     * @return char[]
     */
    public final char[] getTitle() {
        return title;
    }
    /**
     * Gets the NPC's weapon.
     * @return {@link IO}
     */
    public final IO getWeapon() {
        return weapon;
    }
    /**
     * Gets the value for the weaponInHand.
     * @return {@link int}
     */
    public int getWeaponInHand() {
        return weaponInHand;
    }
    /**
     * Gets the value for the xpvalue.
     * @return {@link int}
     */
    public int getXpvalue() {
        return xpvalue;
    }
    public long getXPValue() {
        return this.xpvalue;
    }
    /**
     * Determines if the {@link BaseInteractiveObject} has a specific behavior
     * flag.
     * @param flag the flag
     * @return true if the {@link BaseInteractiveObject} has the flag; false
     *         otherwise
     */
    public final boolean hasBehavior(final Behaviour behaviorEnum) {
        return hasBehavior(behaviorEnum.getFlag());
    }
    /**
     * Determines if the {@link BaseInteractiveObject} has a specific behavior
     * flag.
     * @param flag the flag
     * @return true if the {@link BaseInteractiveObject} has the flag; false
     *         otherwise
     */
    public final boolean hasBehavior(final long flag) {
        return (behavior & flag) == flag;
    }
    /**
     * Determines if the NPC has life remaining.
     * @return <tt>true</tt> if the NPC still have some LP/HP remaining;
     *         <tt>false</tt> otherwise
     */
    protected abstract boolean hasLifeRemaining();
    /**
     * Determines if the {@link IoNpcData} has a specific flag.
     * @param flag the flag
     * @return true if the {@link IoNpcData} has the flag; false otherwise
     */
    public final boolean hasNPCFlag(final long flag) {
        return (npcFlags & flag) == flag;
    }
    /**
     * Gets the value for the reachedtarget.
     * @return {@link long}
     */
    public boolean hasReachedtarget() {
        return reachedtarget;
    }
    /**
     * Heals an NPC for a specific amount.
     * @param healAmt the healing amount
     */
    public final void healNPC(final float healAmt) {
        if (getBaseLife() > 0.f) {
            if (healAmt > 0.f) {
                adjustLife(healAmt);
            }
        }
    }
    /**
     * Determines if an NPC is dead.
     * @return <tt>true</tt> if the NPC is dead; <tt>false</tt> otherwise
     */
    public boolean IsDeadNPC() {
        boolean dead = false;
        if (!hasLifeRemaining()) {
            dead = true;
        }
        if (!dead
                && io.getMainevent() != null
                && io.getMainevent().equalsIgnoreCase("DEAD")) {
            dead = true;
        }
        return dead;
    }
    /** Moves the NPC to their initial position. */
    protected abstract void moveToInitialPosition();
    /**
     * Removes a behavior flag.
     * @param flag the flag
     */
    public final void removeBehavior(final Behaviour behaviorEnum) {
        behavior &= ~behaviorEnum.getFlag();
    }
    /**
     * Removes a behavior flag.
     * @param flag the flag
     */
    public final void removeBehavior(final long flag) {
        behavior &= ~flag;
    }
    /**
     * Removes an NPC flag.
     * @param flag the flag
     */
    public final void removeNPCFlag(final long flag) {
        npcFlags &= ~flag;
    }
    /** Resets the behavior. */
    public void resetBehavior() {
        behavior = Behaviour.BEHAVIOUR_NONE.getFlag();
        for (int i = 0; i < MAX_STACKED_BEHAVIOR; i++) {
            stacked[i].setExists(false);
        }
    }
    /** Restores the NPC to their maximum life. */
    protected abstract void restoreLifeToMax();
    /**
     * Sets the armorClass
     * @param val the armorClass to set
     */
    public void setArmorClass(final float val) {
        this.armorClass = val;
    }
    /**
     * Sets the {@link IoNpcData}'s gender.
     * @param val the gender to set
     */
    public final void setGender(final int val) {
        gender = val;
        notifyWatchers();
    }
    /**
     * Sets the IO associated with this {@link IoNpcData}.
     * @param newIO the IO to set
     */
    public final void setIo(final IO newIO) {
        this.io = newIO;
    }
    /**
     * Sets the value of the movemode.
     * @param val the new value to set
     */
    public void setMovemode(final int val) {
        this.movemode = val;
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
     * Sets the value of the reachedtarget.
     * @param val the new value to set
     */
    public void setReachedtarget(final boolean val) {
        this.reachedtarget = val;
    }
    /**
     * Sets the splatDamages
     * @param splatDamages the splatDamages to set
     */
    public void setSplatDamages(final int val) {
        this.splatDamages = val;
    }
    /**
     * Sets the splatTotNb
     * @param splatTotNb the splatTotNb to set
     */
    public void setSplatTotNb(final int val) {
        this.splatTotNb = val;
    }
    /**
     * Sets the value of the tactics.
     * @param val the new value to set
     */
    public void setTactics(final int val) {
        this.tactics = val;
    }
    /**
     * Sets the {@link IoPcData}'s title.
     * @param val the title to set
     */
    public final void setTitle(final char[] val) {
        title = val;
        notifyWatchers();
    }
    /**
     * Sets the {@link IoPcData}'s title.
     * @param val the title to set
     */
    public final void setTitle(final String val) {
        title = val.toCharArray();
        notifyWatchers();
    }
    /**
     * Sets the NPC's weapon.
     * @param wpnIO the weapon to set
     */
    public final void setWeapon(final IO wpnIO) {
        weapon = wpnIO;
        if (weapon != null) {
            weaponInHand = weapon.getRefId();
        } else {
            weaponInHand = -1;
        }
    }
    /**
     * Sets the value of the weaponInHand.
     * @param ioid the new value to set
     * @throws RPGException if an error occurs
     */
    public final void setWeaponInHand(final int ioid) throws RPGException {
        this.weaponInHand = ioid;
        if (Interactive.getInstance().hasIO(weaponInHand)) {
            weapon = (IO) Interactive.getInstance().getIO(weaponInHand);
        } else {
            weapon = null;
        }
    }
    /**
     * Sets the value of the xpvalue.
     * @param val the new value to set
     */
    public void setXpvalue(final int val) {
        this.xpvalue = val;
    }
    /** Restores the NPC to their maximum life. */
    protected abstract void stopActiveAnimation();
    /** Restores the NPC to their maximum life. */
    protected abstract void stopIdleAnimation();
}
