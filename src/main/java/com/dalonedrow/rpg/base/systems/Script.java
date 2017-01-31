/**
 *
 */
package com.dalonedrow.rpg.base.systems;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.dalonedrow.engine.sprite.base.SimpleVector2;
import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.engine.systems.base.Time;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.rpg.base.flyweights.ScriptTimer;
import com.dalonedrow.rpg.base.flyweights.ScriptTimerAction;
import com.dalonedrow.rpg.base.flyweights.ScriptTimerInitializationParameters;
import com.dalonedrow.rpg.base.flyweights.ScriptVariable;
import com.dalonedrow.rpg.base.flyweights.Scriptable;
import com.dalonedrow.rpg.base.flyweights.SendParameters;
import com.dalonedrow.rpg.base.flyweights.SpeechParameters;
import com.dalonedrow.rpg.base.flyweights.StackedEvent;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * The Script controller executes all in-game scripts.
 * @author DaLoneDrau
 * @param <IO> {@link BaseInteractiveObject}
 * @param <TIMER> {@link ScriptTimer}
 * @param <SCRIPTABLE> {@link Scriptable}
 * @param <STACKED> the type of {@link StackedEvent}s used in the script
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class Script<IO extends BaseInteractiveObject,
        TIMER extends ScriptTimer<IO>, SCRIPTABLE extends Scriptable<IO>,
        STACKED extends StackedEvent<IO>> {
    private static final long ANIM_TALK_ANGRY = 0;
    private static final long ANIM_TALK_HAPPY = 0;
    private static final long ANIM_TALK_NEUTRAL = 0;
    /** the one and only instance of the <tt>Script</tt> class. */
    private static Script instance;
    /** the maximum number of system parameters. */
    public static final int MAX_SYSTEM_PARAMS = 5;
    /** the list of system parameters. */
    private static final String[] SYSTEM_PARAMS = new String[MAX_SYSTEM_PARAMS];
    /**
     * Gives access to the singleton instance of {@link Script}.
     * @return {@link Script}
     */
    public static Script getInstance() {
        return Script.instance;
    }
    /**
     * Sets the singleton instance.
     * @param i the instance to set
     */
    protected static void setInstance(final Script i) {
        Script.instance = i;
    }
    private boolean ARXPausedTime;
    /** the flag indicating whether debug output is turned on. */
    private boolean debug;
    private boolean EDITMODE;
    private IO eventSender;
    private int eventTotalCount;
    private int GLOB;
    /** the list of global script variables. */
    private ScriptVariable[] gvars;
    /** the maximum number of timer scripts. */
    private int maxTimerScript = 0;
    private boolean PauseScript;
    private int stackFlow = 8;
    /**
     * Adds an IO to a specific group.
     * @param io the IO
     * @param group the group name
     */
    public final void addToGroup(final IO io, final String group) {
        if (io != null
                && group != null) {
            io.addGroup(group);
        }
    }
    public final void allowInterScriptExecution() throws RPGException {
        int ppos = 0;

        if (!PauseScript && !EDITMODE && !ARXPausedTime) {
            this.eventSender = null;

            int numm = Math.min(Interactive.getInstance().getMaxIORefId(), 10);

            for (int n = 0; n < numm; n++) {
                int i = ppos;
                ppos++;

                if (ppos >= Interactive.getInstance().getMaxIORefId()) {
                    ppos = 0;
                    break;
                }
                if (Interactive.getInstance().hasIO(i)) {
                    IO io = (IO) Interactive.getInstance().getIO(i);
                    if (io.hasGameFlag(IoGlobals.GFLAG_ISINTREATZONE)) {
                        if (io.getMainevent() != null) {
                            sendIOScriptEvent(io, 0, null, io.getMainevent());
                        } else {
                            sendIOScriptEvent(
                                    io, ScriptConsts.SM_008_MAIN, null, null);
                        }
                    }
                }
            }
        }
    }
    protected abstract void clearAdditionalEventStacks();
    protected abstract void clearAdditionalEventStacksForIO(IO io);
    /**
     * Clones all local variables from the source {@link IO} to the destination
     * {@link IO}.
     * @param src the source {@link IO}
     * @param dest the destination {@link IO}
     * @throws RPGException if an error occurs
     */
    public final void cloneLocalVars(final IO src, final IO dest)
            throws RPGException {
        if (dest != null
                && src != null) {
            freeAllLocalVariables(dest);
            if (src.getScript().hasLocalVariables()) {
                int i = src.getScript().getLocalVarArrayLength() - 1;
                for (; i >= 0; i--) {
                    dest.getScript().setLocalVariable(new ScriptVariable(
                            src.getScript().getLocalVariable(i)));
                }
            }
        }
    }
    /**
     * Count the number of active script timers.
     * @return <code>int</code>
     */
    public final int countTimers() {
        int activeTimers = 0;
        TIMER[] scriptTimers = getScriptTimers();
        for (int i = scriptTimers.length - 1; i >= 0; i--) {
            if (scriptTimers[i] != null
                    && scriptTimers[i].exists()) {
                activeTimers++;
            }
        }
        return activeTimers;
    }
    protected abstract void destroyScriptTimers();
    /**
     * Checks to see if a scripted event is disallowed.
     * @param msg the event message id
     * @param script the {@link Scriptable} script
     * @return <tt>true</tt> if the event is not allowed; <tt>false</tt>
     *         otherwise
     */
    private boolean eventIsDisallowed(final int msg,
            final Scriptable script) {
        boolean disallowed = false;
        // check to see if message is for an event that was disabled
        switch (msg) {
        case ScriptConsts.SM_55_COLLIDE_NPC:
            if (script.hasAllowedEvent(ScriptConsts.DISABLE_COLLIDE_NPC)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_10_CHAT:
            if (script.hasAllowedEvent(ScriptConsts.DISABLE_CHAT)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_016_HIT:
            if (script.hasAllowedEvent(ScriptConsts.DISABLE_HIT)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_28_INVENTORY2_OPEN:
            if (script.hasAllowedEvent(
                    ScriptConsts.DISABLE_INVENTORY2_OPEN)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_46_HEAR:
            if (script.hasAllowedEvent(ScriptConsts.DISABLE_HEAR)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_23_UNDETECTPLAYER:
        case ScriptConsts.SM_22_DETECTPLAYER:
            if (script.hasAllowedEvent(ScriptConsts.DISABLE_DETECT)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_57_AGGRESSION:
            if (script.hasAllowedEvent(
                    ScriptConsts.DISABLE_AGGRESSION)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_008_MAIN:
            if (script.hasAllowedEvent(ScriptConsts.DISABLE_MAIN)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_73_CURSORMODE:
            if (script.hasAllowedEvent(
                    ScriptConsts.DISABLE_CURSORMODE)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_74_EXPLORATIONMODE:
            if (script.hasAllowedEvent(
                    ScriptConsts.DISABLE_EXPLORATIONMODE)) {
                disallowed = true;
            }
            break;
        case ScriptConsts.SM_61_KEY_PRESSED:
            // float dwCurrTime = ARX_TIME_Get();
            // if ((dwCurrTime - g_TimeStartCinemascope) < 3000) {
            // return ScriptConsts.REFUSE;
            // }
            break;
        default:
            break;
        }
        return disallowed;
    }
    public final void eventStackClear() {
        for (int i = 0; i < ScriptConsts.MAX_EVENT_STACK; i++) {
            if (getStackedEvent(i).exists()) {
                getStackedEvent(i).setParams(null);
                getStackedEvent(i).setEventname(null);
                getStackedEvent(i).setSender(null);
                getStackedEvent(i).setExist(false);
                getStackedEvent(i).setIo(null);
                getStackedEvent(i).setMsg(0);
            }
        }
        clearAdditionalEventStacks();
    }
    public final void eventStackClearForIo(final IO io) {
        for (int i = 0; i < ScriptConsts.MAX_EVENT_STACK; i++) {
            if (getStackedEvent(i).exists()
                    && io.equals(getStackedEvent(i).getIo())) {
                getStackedEvent(i).setParams(null);
                getStackedEvent(i).setEventname(null);
                getStackedEvent(i).setSender(null);
                getStackedEvent(i).setExist(false);
                getStackedEvent(i).setIo(null);
                getStackedEvent(i).setMsg(0);
            }
        }
        clearAdditionalEventStacksForIO(io);
    }
    public final void eventStackExecute() throws RPGException {
        int count = 0;
        for (int i = 0; i < ScriptConsts.MAX_EVENT_STACK; i++) {
            if (getStackedEvent(i).exists()) {
                int ioid = getStackedEvent(i).getIo().getRefId();
                if (Interactive.getInstance().hasIO(ioid)) {
                    if (getStackedEvent(i).getSender() != null) {
                        int senderid =
                                getStackedEvent(i).getSender().getRefId();
                        if (Interactive.getInstance().hasIO(senderid)) {
                            eventSender = getStackedEvent(i).getSender();
                        } else {
                            eventSender = null;
                        }
                    } else {
                        eventSender = null;
                    }
                    sendIOScriptEvent(getStackedEvent(i).getIo(),
                            getStackedEvent(i).getMsg(),
                            getStackedEvent(i).getParams(),
                            getStackedEvent(i).getEventname());
                }
                getStackedEvent(i).setParams(null);
                getStackedEvent(i).setEventname(null);
                getStackedEvent(i).setSender(null);
                getStackedEvent(i).setExist(false);
                getStackedEvent(i).setIo(null);
                getStackedEvent(i).setMsg(0);
                count++;
                if (count >= stackFlow) {
                    break;
                }
            }
        }
        executeAdditionalStacks();
    }
    public final void eventStackExecuteAll() throws RPGException {
        stackFlow = 9999999;
        eventStackExecute();
        stackFlow = 20;
    }
    public abstract void eventStackInit();
    protected abstract void executeAdditionalStacks();
    public final void forceDeath(final IO io, final String target)
            throws RPGException {
        int tioid = -1;
        if (target.equalsIgnoreCase("me")
                || target.equalsIgnoreCase("self")) {
            tioid = Interactive.getInstance().GetInterNum(io);
        } else {
            tioid = Interactive.getInstance().getTargetByNameTarget(target);
            if (tioid == -2) {
                tioid = Interactive.getInstance().GetInterNum(io);
            }
        }
        if (tioid >= 0) {
            IO tio = (IO) Interactive.getInstance().getIO(tioid);
            if (tio.hasIOFlag(IoGlobals.IO_03_NPC)) {
                tio.getNPCData().forceDeath(io);
            }
        }
    }
    public final void freeAllGlobalVariables() throws RPGException {
        if (gvars != null) {
            for (int i = gvars.length - 1; i >= 0; i--) {
                if (gvars[i] != null
                        && (gvars[i].getType() == ScriptConsts.TYPE_G_00_TEXT
                                || gvars[i]
                                        .getType() == ScriptConsts.TYPE_L_08_TEXT)
                        && gvars[i].getText() != null) {
                    gvars[i].set(null);
                }
                gvars[i] = null;
            }
        }
    }
    /**
     * Removes all local variables from an {@link IO} and frees up the memory.
     * @param io the {@link IO}
     * @throws RPGException if an error occurs
     */
    public final void freeAllLocalVariables(final IO io) throws RPGException {
        if (io != null
                && io.getScript() != null
                && io.getScript().hasLocalVariables()) {
            int i = io.getScript().getLocalVarArrayLength() - 1;
            for (; i >= 0; i--) {
                if (io.getScript().getLocalVariable(i) != null
                        && (io.getScript().getLocalVariable(i)
                                .getType() == ScriptConsts.TYPE_G_00_TEXT
                                || io.getScript().getLocalVariable(i)
                                        .getType() == ScriptConsts.TYPE_L_08_TEXT)
                        && io.getScript().getLocalVariable(i)
                                .getText() != null) {
                    io.getScript().getLocalVariable(i).set(null);
                }
                io.getScript().setLocalVariable(i, null);
            }
        }
    }
    /**
     * Gets the EVENT_SENDER global.
     * @return {@link BaseInteractiveObject}
     */
    public final IO getEventSender() {
        return eventSender;
    }
    /**
     * Gets the value of a global floating-point array variable.
     * @param name the name of the variable
     * @return <code>float</code>[]
     * @throws RPGException if the variable value was never set
     */
    public final float[] getGlobalFloatArrayVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_03_FLOAT_ARR) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Float Array variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getFloatArrayVal();
    }
    /**
     * Gets the global floating-point value assigned to a specific variable.
     * @param name the variable name
     * @return <code>float</code>
     * @throws RPGException if no such variable was assigned
     */
    public final float getGlobalFloatVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_02_FLOAT) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Float variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getFloatVal();
    }
    /**
     * Gets the value of a global integer array variable.
     * @param name the name of the variable
     * @return <code>int</code>[]
     * @throws RPGException if the variable value was never set
     */
    public final int[] getGlobalIntArrayVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_05_INT_ARR) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Integer Array variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getIntArrayVal();
    }
    /**
     * Gets the value of a global integer variable.
     * @param name the name of the variable
     * @return <code>int</code>
     * @throws RPGException if the variable value was never set
     */
    public final int getGlobalIntVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_04_INT) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Integer variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getIntVal();
    }
    /**
     * Gets the value of a global long integer array variable.
     * @param name the name of the variable
     * @return <code>long</code>[]
     * @throws RPGException if the variable value was never set
     */
    public final long[] getGlobalLongArrayVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_07_LONG_ARR) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Long Integer Array variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getLongArrayVal();
    }
    /**
     * Gets the value of a global long integer variable.
     * @param name the name of the variable
     * @return <code>long</code>
     * @throws RPGException if the variable value was never set
     */
    public final long getGlobalLongVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_06_LONG) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Long Integer variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getLongVal();
    }
    /**
     * Gets the local text array value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws RPGException if no such variable was assigned
     */
    public final String[] getGlobalStringArrayVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_01_TEXT_ARR) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global Text Array variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getTextArrayVal();
    }
    /**
     * Gets the global text value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws RPGException if no such variable was assigned
     */
    public final String getGlobalStringVariableValue(final String name)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        int index = -1;
        for (int i = 0; i < gvars.length; i++) {
            if (gvars[i] != null
                    && gvars[i].getName().equals(name)
                    && gvars[i].getType() == ScriptConsts.TYPE_G_00_TEXT) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Global String variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_OPERATION,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
        return gvars[index].getText();
    }
    public int getGlobalTargetParam(final IO io) {
        return io.getTargetinfo();
    }
    /**
     * Gets a global {@link Scriptable} variable.
     * @param name the name of the variable
     * @return {@link ScriptVariable}
     */
    public final ScriptVariable getGlobalVariable(final String name) {
        ScriptVariable var = null;
        for (int i = gvars.length - 1; i >= 0; i--) {
            if (gvars[i] != null
                    && gvars[i].getName() != null
                    && gvars[i].getName().equalsIgnoreCase(name)) {
                var = gvars[i];
                break;
            }
        }
        return var;
    }
    public final IO getIOMaxEvents() throws RPGException {
        int max = -1;
        int ionum = -1;
        IO io = null;
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO hio = (IO) Interactive.getInstance().getIO(i);
                if (hio.getStatCount() > max) {
                    ionum = i;
                    max = hio.getStatCount();
                }
                hio = null;
            }
        }
        if (max > 0
                && ionum > -1) {
            io = (IO) Interactive.getInstance().getIO(ionum);
        }
        return io;
    }
    public final IO getIOMaxEventsSent() throws RPGException {
        int max = -1;
        int ionum = -1;
        IO io = null;
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO hio = (IO) Interactive.getInstance().getIO(i);
                if (hio.getStatSent() > max) {
                    ionum = i;
                    max = hio.getStatSent();
                }
            }
        }
        if (max > 0
                && ionum > -1) {
            io = (IO) Interactive.getInstance().getIO(ionum);
        }
        return io;
    }
    /**
     * Gets the maximum number of timer scripts.
     * @return <code>int</code>
     */
    public final int getMaxTimerScript() {
        return maxTimerScript;
    }
    /**
     * Gets a script timer.
     * @param id the timer's id
     * @return {@link TIMER}
     */
    public abstract TIMER getScriptTimer(int id);
    /**
     * Gets the script timers.
     * @return {@link TIMER}[]
     */
    protected abstract TIMER[] getScriptTimers();
    /**
     * Gets the stacked event at a specific index.
     * @param index the index
     * @return {@link STACKED}
     */
    protected abstract STACKED getStackedEvent(int index);
    /**
     * Gets the id of a named script assigned to a specific IO.
     * @param io the IO
     * @param name the script's name
     * @return the script's id, if found. If no script exists, -1 is returned
     */
    public final int getSystemIOScript(final IO io, final String name) {
        int index = -1;
        if (countTimers() > 0) {
            for (int i = 0; i < maxTimerScript; i++) {
                TIMER[] scriptTimers = getScriptTimers();
                if (scriptTimers[i].exists()) {
                    if (scriptTimers[i].getIo().equals(io)
                            && scriptTimers[i].getName().equalsIgnoreCase(
                                    name)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }
    /**
     * Determines if a {@link Script} has local variable with a specific name.
     * @param name the variable name
     * @return <tt>true</tt> if the {@link Script} has the local variable;
     *         <tt>false</tt> otherwise
     */
    public final boolean hasGlobalVariable(final String name) {
        return getGlobalVariable(name) != null;
    }
    public final void initEventStats() throws RPGException {
        eventTotalCount = 0;
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO io = (IO) Interactive.getInstance().getIO(i);
                io.setStatCount(0);
                io.setStatSent(0);
            }
        }
    }
    protected abstract void initScriptTimers();
    /**
     * Gets the flag indicating whether debug output is turned on.
     * @return <tt>true</tt> if the debug output is turned on; <tt>false</tt>
     *         otherwise
     */
    public final boolean isDebug() {
        return debug;
    }
    /**
     * Determines if an IO is in a specific group.
     * @param io the IO
     * @param group the group name
     * @return true if the IO is in the group; false otherwise
     */
    public final boolean isIOInGroup(final IO io, final String group) {
        boolean val = false;
        if (io != null
                && group != null) {
            for (int i = 0; i < io.getNumIOGroups(); i++) {
                if (group.equalsIgnoreCase(io.getIOGroup(i))) {
                    val = true;
                    break;
                }
            }
        }
        return val;
    }
    public boolean isPlayerInvisible(IO io) {
        boolean invisible = false;
        // if (inter.iobj[0]->invisibility > 0.3f) {
        // invisible = true;
        // }
        return invisible;
    }
    private void MakeSSEPARAMS(String params) {
        for (int i = MAX_SYSTEM_PARAMS - 1; i >= 0; i--) {
            SYSTEM_PARAMS[i] = null;
        }
        if (params != null) {
            String[] split = params.split(" ");
            for (int i = 0, len = split.length - 1; i < len; i++) {
                if (i / 2 < MAX_SYSTEM_PARAMS) {
                    SYSTEM_PARAMS[i] = split[i];
                } else {
                    break;
                }
            }
        }
    }
    /**
     * Sends an event message to the IO.
     * @param io the IO
     * @param msg the message
     * @param params the script parameters
     * @return {@link int}
     * @throws RPGException if an error occurs
     */
    public final int notifyIOEvent(final IO io, final int msg,
            final String params) throws RPGException {
        int acceptance = ScriptConsts.REFUSE;
        if (sendIOScriptEvent(io, msg, null, null) != acceptance) {
            switch (msg) {
            case ScriptConsts.SM_017_DIE:
                if (io != null && Interactive.getInstance().hasIO(io)) {
                    // TODO - set death color
                    // io->infracolor.b = 1.f;
                    // io->infracolor.g = 0.f;
                    // io->infracolor.r = 0.f;
                }
                break;
            default:
                break;
            }
            acceptance = ScriptConsts.ACCEPT;
        }
        return acceptance;
    }
    /**
     * Hides a target IO.
     * @param io the IO sending the event.
     * @param megahide if true, the target IO is "mega-hidden"
     * @param targetName the target's name
     * @param hideOn if true, the hidden flags are set; otherwise all hidden
     *            flags are removed
     * @throws RPGException if an error occurs
     */
    public final void objectHide(final IO io, final boolean megahide,
            final String targetName, final boolean hideOn) throws RPGException {
        int targetId =
                Interactive.getInstance().getTargetByNameTarget(targetName);
        if (targetId == -2) {
            targetId = io.getRefId();
        }
        if (Interactive.getInstance().hasIO(targetId)) {
            IO tio = (IO) Interactive.getInstance().getIO(targetId);
            tio.removeGameFlag(IoGlobals.GFLAG_MEGAHIDE);
            if (hideOn) {
                if (megahide) {
                    tio.addGameFlag(IoGlobals.GFLAG_MEGAHIDE);
                    tio.setShow(IoGlobals.SHOW_FLAG_MEGAHIDE);
                } else {
                    tio.setShow(IoGlobals.SHOW_FLAG_HIDDEN);
                }
            } else if (tio.getShow() == IoGlobals.SHOW_FLAG_MEGAHIDE
                    || tio.getShow() == IoGlobals.SHOW_FLAG_HIDDEN) {
                tio.setShow(IoGlobals.SHOW_FLAG_IN_SCENE);
                if (tio.hasIOFlag(IoGlobals.IO_03_NPC)
                        && tio.getNPCData().getBaseLife() <= 0f) {
                    // tio.animlayer[0].cur_anim =
                    // inter.iobj[t]->anims[ANIM_DIE];
                    // tio.animlayer[1].cur_anim = NULL;
                    // tio.animlayer[2].cur_anim = NULL;
                    // tio.animlayer[0].ctime = 9999999;
                }
            }
        }
    }
    /**
     * Removes an IO from all groups to which it was assigned.
     * @param io the IO
     */
    public final void releaseAllGroups(final IO io) {
        while (io != null
                && io.getNumIOGroups() > 0) {
            io.removeGroup(io.getIOGroup(0));
        }
    }
    /**
     * Releases an event, clearing all variables.
     * @param event the scriptable event
     */
    public final void releaseScript(final SCRIPTABLE event) {
        if (event != null) {
            event.clearLocalVariables();
        }
    }
    /**
     * Removes an IO from a group.
     * @param io the IO
     * @param group the group
     */
    public final void removeGroup(final IO io, final String group) {
        if (io != null
                && group != null) {
            io.removeGroup(group);
        }
    }
    /**
     * Resets the object's script.
     * @param io the object
     * @param initialize if <tt>true</tt> and the object is script-loaded, it
     *            will be initialized again
     * @throws RPGException if an error occurs
     */
    public final void reset(final IO io, final boolean initialize)
            throws RPGException {
        // Release Script Local Variables
        if (io.getScript().getLocalVarArrayLength() > 0) {
            int i = io.getScript().getLocalVarArrayLength() - 1;
            for (; i >= 0; i--) {
                if (io.getScript().getLocalVariable(i) != null) {
                    io.getScript().getLocalVariable(i).set(null);
                    io.getScript().setLocalVariable(i, null);
                }
            }
        }

        // Release Script Over-Script Local Variables
        if (io.getOverscript().getLocalVarArrayLength() > 0) {
            int i = io.getOverscript().getLocalVarArrayLength() - 1;
            for (; i >= 0; i--) {
                if (io.getOverscript().getLocalVariable(i) != null) {
                    io.getOverscript().getLocalVariable(i).set(null);
                    io.getOverscript().setLocalVariable(i, null);
                }
            }
        }
        if (!io.isScriptLoaded()) {
            resetObject(io, initialize);
        }
    }
    /**
     * Resets all interactive objects.
     * @param initialize if <tt>true</tt> and an object is script-loaded, it
     *            will be initialized again
     * @throws RPGException if an error occurs
     */
    public final void resetAll(final boolean initialize) throws RPGException {
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO io = (IO) Interactive.getInstance().getIO(i);
                if (!io.isScriptLoaded()) {
                    resetObject(io, initialize);
                }
            }
        }
    }
    /**
     * Resets the IO.
     * @param io the IO
     * @param initialize if <tt>true</tt>, the object needs to be initialized as
     *            well
     * @throws RPGException if an error occurs
     */
    public final void resetObject(final IO io, final boolean initialize)
            throws RPGException {
        // Now go for Script INIT/RESET depending on Mode
        int num = Interactive.getInstance().GetInterNum(io);
        if (Interactive.getInstance().hasIO(num)) {
            IO objIO = (IO) Interactive.getInstance().getIO(num);
            if (objIO != null
                    && objIO.getScript() != null) {
                objIO.getScript().clearDisallowedEvents();

                if (initialize) {
                    sendScriptEvent((SCRIPTABLE) objIO.getScript(),
                            ScriptConsts.SM_001_INIT,
                            new Object[0],
                            objIO,
                            null);
                }
                objIO = (IO) Interactive.getInstance().getIO(num);
                if (objIO != null) {
                    setMainEvent(objIO, "MAIN");
                }
            }

            // Do the same for Local Script
            objIO = (IO) Interactive.getInstance().getIO(num);
            if (objIO != null
                    && objIO.getOverscript() != null) {
                objIO.getOverscript().clearDisallowedEvents();

                if (initialize) {
                    sendScriptEvent((SCRIPTABLE) objIO.getOverscript(),
                            ScriptConsts.SM_001_INIT,
                            new Object[0],
                            objIO,
                            null);
                }
            }

            // Sends InitEnd Event
            if (initialize) {
                objIO = (IO) Interactive.getInstance().getIO(num);
                if (objIO != null
                        && objIO.getScript() != null) {
                    sendScriptEvent((SCRIPTABLE) objIO.getScript(),
                            ScriptConsts.SM_033_INITEND,
                            new Object[0],
                            objIO,
                            null);
                }
                objIO = (IO) Interactive.getInstance().getIO(num);
                if (objIO != null
                        && objIO.getOverscript() != null) {
                    sendScriptEvent((SCRIPTABLE) objIO.getOverscript(),
                            ScriptConsts.SM_033_INITEND,
                            new Object[0],
                            objIO,
                            null);
                }
            }

            objIO = (IO) Interactive.getInstance().getIO(num);
            if (objIO != null) {
                objIO.removeGameFlag(IoGlobals.GFLAG_NEEDINIT);
            }
        }
    }
    protected void runEvent(SCRIPTABLE script, String eventName, IO io)
            throws RPGException {
        int msg = 0;
        if (eventName.equalsIgnoreCase("INIT")) {
            msg = ScriptConsts.SM_001_INIT;
        } else if (eventName.equalsIgnoreCase("HIT")) {
            msg = ScriptConsts.SM_016_HIT;
        } else if (eventName.equalsIgnoreCase("INIT_END")) {
            msg = ScriptConsts.SM_033_INITEND;
        }
        if (msg > 0) {
            runMessage(script, msg, io);
        } else {
            try {
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                sb.append("on");
                sb.append(eventName.toUpperCase().charAt(0));
                sb.append(eventName.substring(1));
                Method method = script.getClass().getMethod(sb.toString());
                sb.returnToPool();
                sb = null;
                method.invoke(script, (Object[]) null);
            } catch (NoSuchMethodException | SecurityException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | PooledException e) {
                e.printStackTrace();
                throw new RPGException(ErrorMessage.INVALID_PARAM, e);
            }
        }
    }
    protected void runMessage(SCRIPTABLE script, int msg, IO io)
            throws RPGException {
        switch (msg) {
        case ScriptConsts.SM_001_INIT:
            script.onInit();
            break;
        case ScriptConsts.SM_002_INVENTORYIN:
            script.onInventoryIn();
            break;
        case ScriptConsts.SM_004_INVENTORYUSE:
            script.onInventoryUse();
            break;
        case ScriptConsts.SM_007_EQUIPOUT:
            script.onUnequip();
            break;
        case ScriptConsts.SM_016_HIT:
            script.onHit();
            break;
        case ScriptConsts.SM_017_DIE:
            script.onDie();
            break;
        case ScriptConsts.SM_24_COMBINE:
            script.onCombine();
            break;
        case ScriptConsts.SM_033_INITEND:
            script.onInitEnd();
            break;
        case ScriptConsts.SM_41_LOAD:
            script.onLoad();
            break;
        case ScriptConsts.SM_43_RELOAD:
            script.onReload();
            break;
        case ScriptConsts.SM_045_OUCH:
            script.onOuch();
            break;
        case ScriptConsts.SM_57_AGGRESSION:
            script.onAggression();
            break;
        case ScriptConsts.SM_69_IDENTIFY:
            script.onIdentify();
            break;
        default:
            throw new RPGException(ErrorMessage.INVALID_PARAM,
                    "No action defined for message " + msg);
        }
    }
    public final void sendEvent(final IO io, final SendParameters params)
            throws RPGException {
        IO oes = eventSender;
        eventSender = io;
        if (params.hasFlag(SendParameters.RADIUS)) {
            // SEND EVENT TO ALL OBJECTS IN A RADIUS

            // LOOP THROUGH ALL IOs.
            int i = Interactive.getInstance().getMaxIORefId();
            for (; i >= 0; i--) {
                if (Interactive.getInstance().hasIO(i)) {
                    IO iio = (IO) Interactive.getInstance().getIO(i);
                    // skip cameras and markers
                    // if (iio.hasIOFlag(IoGlobals.io_camera)
                    // || iio.hasIOFlag(IoGlobals.io_marker)) {
                    // continue;
                    // }
                    // skip IOs not in required group
                    if (params.hasFlag(SendParameters.GROUP)) {
                        if (!this.isIOInGroup(iio, params.getGroupName())) {
                            continue;
                        }
                    }
                    // if send event is for NPCs, send to NPCs,
                    // if for Items, send to Items, etc...
                    if ((params.hasFlag(SendParameters.NPC)
                            && iio.hasIOFlag(IoGlobals.IO_03_NPC))
                            // || (params.hasFlag(SendParameters.FIX)
                            // && iio.hasIOFlag(IoGlobals.IO_FIX))
                            || (params.hasFlag(SendParameters.ITEM)
                                    && iio.hasIOFlag(IoGlobals.IO_02_ITEM))) {
                        SimpleVector2 senderPos = new SimpleVector2(),
                                ioPos = new SimpleVector2();
                        Interactive.getInstance().GetItemWorldPosition(io,
                                senderPos);
                        Interactive.getInstance().GetItemWorldPosition(iio,
                                ioPos);
                        // IF IO IS IN SENDER RADIUS, SEND EVENT
                        io.setStatSent(io.getStatSent() + 1);
                        this.stackSendIOScriptEvent(
                                iio,
                                0,
                                params.getEventParameters(),
                                params.getEventName());
                    }
                }
            }
        }
        if (params.hasFlag(SendParameters.ZONE)) {
            // SEND EVENT TO ALL OBJECTS IN A ZONE
            // ARX_PATH * ap = ARX_PATH_GetAddressByName(zonename);

            // if (ap != NULL) {
            // LOOP THROUGH ALL IOs.
            int i = Interactive.getInstance().getMaxIORefId();
            for (; i >= 0; i--) {
                if (Interactive.getInstance().hasIO(i)) {
                    IO iio = (IO) Interactive.getInstance().getIO(i);
                    // skip cameras and markers
                    // if (iio.hasIOFlag(IoGlobals.io_camera)
                    // || iio.hasIOFlag(IoGlobals.io_marker)) {
                    // continue;
                    // }
                    // skip IOs not in required group
                    if (params.hasFlag(SendParameters.GROUP)) {
                        if (!this.isIOInGroup(iio, params.getGroupName())) {
                            continue;
                        }
                    }
                    // if send event is for NPCs, send to NPCs,
                    // if for Items, send to Items, etc...
                    if ((params.hasFlag(SendParameters.NPC)
                            && iio.hasIOFlag(IoGlobals.IO_03_NPC))
                            // || (params.hasFlag(SendParameters.FIX)
                            // && iio.hasIOFlag(IoGlobals.IO_FIX))
                            || (params.hasFlag(SendParameters.ITEM)
                                    && iio.hasIOFlag(IoGlobals.IO_02_ITEM))) {
                        SimpleVector2 ioPos = new SimpleVector2();
                        Interactive.getInstance().GetItemWorldPosition(iio,
                                ioPos);
                        // IF IO IS IN ZONE, SEND EVENT
                        // if (ARX_PATH_IsPosInZone(ap, _pos.x, _pos.y, _pos.z))
                        // {
                        io.setStatSent(io.getStatSent() + 1);
                        this.stackSendIOScriptEvent(
                                iio,
                                0,
                                params.getEventParameters(),
                                params.getEventName());
                        // }
                    }
                }
            }
        }
        if (params.hasFlag(SendParameters.GROUP)) {
            // sends an event to all members of a group
            // LOOP THROUGH ALL IOs.
            int i = Interactive.getInstance().getMaxIORefId();
            for (; i >= 0; i--) {
                if (Interactive.getInstance().hasIO(i)) {
                    IO iio = (IO) Interactive.getInstance().getIO(i);
                    // skip IOs not in required group
                    if (!this.isIOInGroup(iio, params.getGroupName())) {
                        continue;
                    }
                    iio.setStatSent(io.getStatSent() + 1);
                    this.stackSendIOScriptEvent(
                            iio,
                            0,
                            params.getEventParameters(),
                            params.getEventName());
                }
            }
        } else {
            // SINGLE OBJECT EVENT
            int tioid = Interactive.getInstance().getTargetByNameTarget(
                    params.getTargetName());

            if (tioid == -2) {
                tioid = Interactive.getInstance().GetInterNum(io);
            }
            if (Interactive.getInstance().hasIO(tioid)) {
                io.setStatSent(io.getStatSent() + 1);
                this.stackSendIOScriptEvent(
                        (IO) Interactive.getInstance().getIO(tioid),
                        0,
                        params.getEventParameters(),
                        params.getEventName());
            }
        }
        this.eventSender = oes;
    }
    /**
     * Sends an initialization event to an IO. The initialization event runs the
     * local script first, followed by the over script.
     * @param io the IO
     * @return {@link int}
     * @throws RPGException if an error occurs
     */
    public final int sendInitScriptEvent(final IO io) throws RPGException {
        if (io == null) {
            return -1;
        }
        int num = io.getRefId();
        if (!Interactive.getInstance().hasIO(num)) {
            return -1;
        }
        IO oldEventSender = eventSender;
        eventSender = null;
        // send script the init message
        IO hio = (IO) Interactive.getInstance().getIO(num);
        if (hio.getScript() != null) {
            GLOB = 0;
            sendScriptEvent((SCRIPTABLE) hio.getScript(),
                    ScriptConsts.SM_001_INIT,
                    null,
                    hio,
                    null);
        }
        hio = null;
        // send overscript the init message
        if (Interactive.getInstance().getIO(num) != null) {
            hio = (IO) Interactive.getInstance().getIO(num);
            if (hio.getOverscript() != null) {
                GLOB = 0;
                sendScriptEvent((SCRIPTABLE) hio.getOverscript(),
                        ScriptConsts.SM_001_INIT,
                        null,
                        hio,
                        null);
            }
            hio = null;
        }
        // send script the init end message
        if (Interactive.getInstance().getIO(num) != null) {
            hio = (IO) Interactive.getInstance().getIO(num);
            if (hio.getScript() != null) {
                GLOB = 0;
                sendScriptEvent((SCRIPTABLE) hio.getScript(),
                        ScriptConsts.SM_033_INITEND,
                        null,
                        hio,
                        null);
            }
            hio = null;
        }
        // send overscript the init end message
        if (Interactive.getInstance().getIO(num) != null) {
            hio = (IO) Interactive.getInstance().getIO(num);
            if (hio.getOverscript() != null) {
                GLOB = 0;
                sendScriptEvent((SCRIPTABLE) hio.getOverscript(),
                        ScriptConsts.SM_033_INITEND,
                        null,
                        hio,
                        null);
            }
            hio = null;
        }
        eventSender = oldEventSender;
        return ScriptConsts.ACCEPT;
    }
    /**
     * Sends a script event to an interactive object. The returned value is a
     * flag indicating whether the event was successful or refused.
     * @param target the reference id of the targeted io
     * @param msg the message being sent
     * @param params the list of parameters applied, grouped in key-value pairs
     * @param eventname the name of the event, for example, new Object[]
     *            {"key0", value, "key1", new int[] {0, 0}}
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public final int sendIOScriptEvent(final IO target, final int msg,
            final Object[] params, final String eventname) throws RPGException {
        // checks invalid IO
        if (target == null) {
            return -1;
        }
        int num = target.getRefId();

        if (Interactive.getInstance().hasIO(num)) {
            IO originalEventSender = eventSender;
            if (msg == ScriptConsts.SM_001_INIT
                    || msg == ScriptConsts.SM_033_INITEND) {
                IO hio = (IO) Interactive.getInstance().getIO(num);
                sendIOScriptEventReverse(hio, msg, params, eventname);
                eventSender = originalEventSender;
                hio = null;
            }

            if (Interactive.getInstance().hasIO(num)) {
                // if this IO only has a Local script, send event to it
                IO hio = (IO) Interactive.getInstance().getIO(num);
                if (hio.getOverscript() == null) {
                    GLOB = 0;
                    int ret = sendScriptEvent(
                            (SCRIPTABLE) hio.getScript(),
                            msg,
                            params,
                            hio,
                            eventname);
                    eventSender = originalEventSender;
                    return ret;
                }

                // If this IO has a Global script send to Local (if exists)
                // then to Global if not overridden by Local
                int s = sendScriptEvent(
                        (SCRIPTABLE) hio.getOverscript(),
                        msg,
                        params,
                        hio,
                        eventname);
                if (s != ScriptConsts.REFUSE) {
                    eventSender = originalEventSender;
                    GLOB = 0;

                    if (Interactive.getInstance().hasIO(num)) {
                        hio = (IO) Interactive.getInstance().getIO(num);
                        int ret = sendScriptEvent(
                                (SCRIPTABLE) hio.getScript(),
                                msg,
                                params,
                                hio,
                                eventname);
                        eventSender = originalEventSender;
                        return ret;
                    } else {
                        return ScriptConsts.REFUSE;
                    }
                }
                hio = null;
            }
            GLOB = 0;
        }

        // Refused further processing.
        return ScriptConsts.REFUSE;
    }
    private int sendIOScriptEventReverse(final IO io, final int msg,
            final Object[] params, final String eventname) throws RPGException {
        // checks invalid IO
        if (io == null) {
            return -1;
        }
        // checks for no script assigned
        if (io.getOverscript() == null
                && io.getScript() == null) {
            return -1;
        }
        int num = io.getRefId();
        if (Interactive.getInstance().hasIO(num)) {
            IO hio = (IO) Interactive.getInstance().getIO(num);
            // if this IO only has a Local script, send event to it
            if (hio.getOverscript() == null
                    && hio.getScript() != null) {
                GLOB = 0;
                return sendScriptEvent(
                        (SCRIPTABLE) hio.getScript(),
                        msg,
                        params,
                        hio,
                        eventname);
            }

            // If this IO has a Global script send to Local (if exists)
            // then to global if no overriden by Local
            if (Interactive.getInstance().hasIO(num)) {
                hio = (IO) Interactive.getInstance().getIO(num);
                int s = sendScriptEvent(
                        (SCRIPTABLE) hio.getScript(),
                        msg,
                        params,
                        hio,
                        eventname);
                if (s != ScriptConsts.REFUSE) {
                    GLOB = 0;
                    if (Interactive.getInstance().hasIO(io.getRefId())) {
                        return sendScriptEvent(
                                (SCRIPTABLE) io.getOverscript(),
                                msg,
                                params,
                                io,
                                eventname);
                    } else {
                        return ScriptConsts.REFUSE;
                    }
                }
            }
            hio = null;
            GLOB = 0;
        }
        // Refused further processing.
        return ScriptConsts.REFUSE;
    }
    /**
     * Sends a scripted event to all IOs.
     * @param msg the message
     * @param dat any script variables
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public final int sendMsgToAllIO(final int msg, final Object[] dat)
            throws RPGException {
        int ret = ScriptConsts.ACCEPT;
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO io = (IO) Interactive.getInstance().getIO(i);
                if (sendIOScriptEvent(io, msg, dat,
                        null) == ScriptConsts.REFUSE) {
                    ret = ScriptConsts.REFUSE;
                }
            }
        }
        return ret;
    }
    /**
     * Sends a scripted event to an IO.
     * @param localScript the local script the IO shoulod follow
     * @param msg the event message
     * @param params any parameters to be applied
     * @param io
     * @param eventName
     * @param info
     * @return
     * @throws RPGException
     */
    public final int sendScriptEvent(final SCRIPTABLE localScript,
            final int msg, final Object[] params, final IO io,
            final String eventName) throws RPGException {
        int retVal = ScriptConsts.ACCEPT;
        boolean keepGoing = true;
        if (localScript == null) {
            throw new RPGException(
                    ErrorMessage.INVALID_PARAM, "script cannot be null");
        }
        if (io != null) {
            if (io.hasGameFlag(IoGlobals.GFLAG_MEGAHIDE)
                    && msg != ScriptConsts.SM_43_RELOAD) {
                return ScriptConsts.ACCEPT;
            }

            if (io.getShow() == IoGlobals.SHOW_FLAG_DESTROYED) {
                // destroyed
                return ScriptConsts.ACCEPT;
            }
            eventTotalCount++;
            io.setStatCount(io.getStatCount() + 1);

            if (io.hasIOFlag(IoGlobals.IO_06_FREEZESCRIPT)) {
                if (msg == ScriptConsts.SM_41_LOAD) {
                    return ScriptConsts.ACCEPT;
                }
                return ScriptConsts.REFUSE;
            }

            if (io.hasIOFlag(IoGlobals.IO_03_NPC)
                    && !io.hasIOFlag(IoGlobals.IO_09_DWELLING)) {
                if (io.getNPCData().getBaseLife() <= 0.f
                        && msg != ScriptConsts.SM_001_INIT
                        && msg != ScriptConsts.SM_12_DEAD
                        && msg != ScriptConsts.SM_017_DIE
                        && msg != ScriptConsts.SM_255_EXECUTELINE
                        && msg != ScriptConsts.SM_43_RELOAD
                        && msg != ScriptConsts.SM_255_EXECUTELINE
                        && msg != ScriptConsts.SM_28_INVENTORY2_OPEN
                        && msg != ScriptConsts.SM_29_INVENTORY2_CLOSE) {
                    return ScriptConsts.ACCEPT;
                }
            }

            // change weapon if one breaks
            /*
             * if (((io->ioflags & IO_FIX) || (io->ioflags & IO_ITEM)) && (msg
             * == ScriptConsts.SM_BREAK)) { ManageCasseDArme(io); }
             */
        }
        // use master script if available
        SCRIPTABLE script = (SCRIPTABLE) localScript.getMaster();
        if (script == null) { // no master - use local script
            script = localScript;
        }
        // set parameters on script that will be used
        if (params != null
                && params.length > 0) {
            for (int i = 0; i < params.length; i += 2) {
                script.setLocalVariable((String) params[i], params[i + 1]);
            }
        }
        // run the event
        if (eventName != null
                && eventName.length() > 0) {
            runEvent(script, eventName, io);
        } else {
            if (eventIsDisallowed(msg, script)) {
                return ScriptConsts.REFUSE;
            }
            runMessage(script, msg, io);
        }
        int ret = ScriptConsts.ACCEPT;
        return ret;
    }
    /**
     * Sets the value for the flag indicating whether debug output is turned on.
     * @param val the value to set
     */
    public final void setDebug(final boolean val) {
        this.debug = val;
    }
    public final void setEvent(final IO io, final String event,
            final boolean isOn) {
        if (event.equalsIgnoreCase("COLLIDE_NPC")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(
                        ScriptConsts.DISABLE_COLLIDE_NPC);
            } else {
                io.getScript().assignDisallowedEvent(
                        ScriptConsts.DISABLE_COLLIDE_NPC);
            }
        } else if (event.equalsIgnoreCase("CHAT")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(ScriptConsts.DISABLE_CHAT);
            } else {
                io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_CHAT);
            }
        } else if (event.equalsIgnoreCase("HIT")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(ScriptConsts.DISABLE_HIT);
            } else {
                io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_HIT);
            }
        } else if (event.equalsIgnoreCase("INVENTORY2_OPEN")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(
                        ScriptConsts.DISABLE_INVENTORY2_OPEN);
            } else {
                io.getScript().assignDisallowedEvent(
                        ScriptConsts.DISABLE_INVENTORY2_OPEN);
            }
        } else if (event.equalsIgnoreCase("DETECTPLAYER")) {
            if (isOn) {
                io.getScript()
                        .removeDisallowedEvent(ScriptConsts.DISABLE_DETECT);
            } else {
                io.getScript()
                        .assignDisallowedEvent(ScriptConsts.DISABLE_DETECT);
            }
        } else if (event.equalsIgnoreCase("HEAR")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(ScriptConsts.DISABLE_HEAR);
            } else {
                io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_HEAR);
            }
        } else if (event.equalsIgnoreCase("AGGRESSION")) {
            if (isOn) {
                io.getScript()
                        .removeDisallowedEvent(ScriptConsts.DISABLE_AGGRESSION);
            } else {
                io.getScript()
                        .assignDisallowedEvent(ScriptConsts.DISABLE_AGGRESSION);
            }
        } else if (event.equalsIgnoreCase("MAIN")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(ScriptConsts.DISABLE_MAIN);
            } else {
                io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_MAIN);
            }
        } else if (event.equalsIgnoreCase("CURSORMODE")) {
            if (isOn) {
                io.getScript()
                        .removeDisallowedEvent(ScriptConsts.DISABLE_CURSORMODE);
            } else {
                io.getScript()
                        .assignDisallowedEvent(ScriptConsts.DISABLE_CURSORMODE);
            }
        } else if (event.equalsIgnoreCase("EXPLORATIONMODE")) {
            if (isOn) {
                io.getScript().removeDisallowedEvent(
                        ScriptConsts.DISABLE_EXPLORATIONMODE);
            } else {
                io.getScript().assignDisallowedEvent(
                        ScriptConsts.DISABLE_EXPLORATIONMODE);
            }
        }
    }
    /**
     * Sets the value of the eventSender.
     * @param val the new value to set
     */
    public final void setEventSender(final IO val) {
        eventSender = val;
    }
    /**
     * Sets a global variable.
     * @param name the name of the global variable
     * @param value the variable's value
     * @throws RPGException if an error occurs
     */
    public final void setGlobalVariable(final String name, final Object value)
            throws RPGException {
        if (gvars == null) {
            gvars = new ScriptVariable[0];
        }
        boolean found = false;
        for (int i = gvars.length - 1; i >= 0; i--) {
            ScriptVariable var = gvars[i];
            if (var != null
                    && var.getName() != null
                    && var.getName().equalsIgnoreCase(name)) {
                // found the correct script variable
                var.set(value);
                found = true;
                break;
            }
        }
        if (!found) {
            // create a new variable and add to the global array
            ScriptVariable var = null;
            if (value instanceof String
                    || value instanceof char[]) {
                var = new ScriptVariable(name, ScriptConsts.TYPE_G_00_TEXT,
                        value);
            } else if (value instanceof String[]
                    || value instanceof char[][]) {
                var = new ScriptVariable(name,
                        ScriptConsts.TYPE_G_01_TEXT_ARR, value);
            } else if (value instanceof Float) {
                var = new ScriptVariable(name, ScriptConsts.TYPE_G_02_FLOAT,
                        value);
            } else if (value instanceof Double) {
                var = new ScriptVariable(name, ScriptConsts.TYPE_G_02_FLOAT,
                        value);
            } else if (value instanceof float[]) {
                var = new ScriptVariable(name,
                        ScriptConsts.TYPE_G_03_FLOAT_ARR, value);
            } else if (value instanceof Integer) {
                var = new ScriptVariable(name, ScriptConsts.TYPE_G_04_INT,
                        value);
            } else if (value instanceof int[]) {
                var = new ScriptVariable(name,
                        ScriptConsts.TYPE_G_05_INT_ARR, value);
            } else if (value instanceof Long) {
                var = new ScriptVariable(name, ScriptConsts.TYPE_G_06_LONG,
                        value);
            } else if (value instanceof long[]) {
                var = new ScriptVariable(name,
                        ScriptConsts.TYPE_G_07_LONG_ARR, value);
            } else {
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                try {
                    sb.append("Global variable ");
                    sb.append(name);
                    sb.append(" was passed new value of type ");
                    sb.append(value.getClass().getCanonicalName());
                    sb.append(". Only String, String[], char[][], Float, ");
                    sb.append("float[], Integer, int[], Long, or long[] ");
                    sb.append("allowed.");
                } catch (PooledException e) {
                    throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
                }
                RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                        sb.toString());
                sb.returnToPool();
                sb = null;
                throw ex;
            }
            gvars = ArrayUtilities.getInstance().extendArray(var, gvars);
        }
    }
    /**
     * Sets the main event for an IO.
     * @param io the IO
     * @param newevent the event's name
     */
    public final void setMainEvent(final IO io, final String newevent) {
        if (io != null) {
            if (!"MAIN".equalsIgnoreCase(newevent)) {
                io.setMainevent(null);
            } else {
                io.setMainevent(newevent);
            }
        }
    }
    /**
     * Sets the maximum number of timer scripts.
     * @param val the maximum number of timer scripts to set
     */
    protected final void setMaxTimerScript(final int val) {
        maxTimerScript = val;
    }
    protected abstract void setScriptTimer(int index, TIMER timer);
    public final void speak(final IO io, final SpeechParameters params) {
        // speech variables
        // ARX_CINEMATIC_SPEECH acs;
        // acs.type = ARX_CINE_SPEECH_NONE;
        long voixoff = 0;
        long mood = ANIM_TALK_NEUTRAL;
        if (params.isKillAllSpeech()) {
            // ARX_SPEECH_Reset();
        } else {
            if (params.hasFlag(SpeechParameters.HAPPY)) {
                mood = ANIM_TALK_HAPPY;
            }
            if (params.hasFlag(SpeechParameters.ANGRY)) {
                mood = ANIM_TALK_ANGRY;
            }
            if (params.hasFlag(SpeechParameters.OFF_VOICE)) {
                voixoff = 2;
            }
            if (params.hasFlag(SpeechParameters.KEEP_SPEECH)
                    || params.hasFlag(SpeechParameters.ZOOM_SPEECH)
                    || params.hasFlag(SpeechParameters.SPEECH_CCCTALKER_L)
                    || params.hasFlag(SpeechParameters.SPEECH_CCCTALKER_R)
                    || params.hasFlag(SpeechParameters.SPEECH_CCCLISTENER_L)
                    || params.hasFlag(SpeechParameters.SPEECH_CCCLISTENER_R)
                    || params.hasFlag(SpeechParameters.SIDE_L)
                    || params.hasFlag(SpeechParameters.SIDE_R)) {
                // FRAME_COUNT = 0;
                if (params.hasFlag(SpeechParameters.KEEP_SPEECH)) {
                    // acs.type = ARX_CINE_SPEECH_KEEP;
                    // acs.pos1.x = LASTCAMPOS.x;
                    // acs.pos1.y = LASTCAMPOS.y;
                    // acs.pos1.z = LASTCAMPOS.z;
                    // acs.pos2.a = LASTCAMANGLE.a;
                    // acs.pos2.b = LASTCAMANGLE.b;
                    // acs.pos2.g = LASTCAMANGLE.g;
                }
                if (params.hasFlag(SpeechParameters.ZOOM_SPEECH)) {
                    // acs.type = ARX_CINE_SPEECH_ZOOM;
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.startangle.a = GetVarValueInterpretedAsFloat(temp2,
                    // esss, io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.startangle.b = GetVarValueInterpretedAsFloat(temp2,
                    // esss, io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.endangle.a = GetVarValueInterpretedAsFloat(temp2,
                    // esss, io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.endangle.b = GetVarValueInterpretedAsFloat(temp2,
                    // esss, io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.startpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.endpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);

                    // ARX_CHECK_NO_ENTRY(); //ARX: xrichter (2010-07-20) -
                    // temp2 is often (always?) a string number and
                    // GetTargetByNameTarget return -1. To be careful if temp2
                    // is not a string number, we choose to test
                    // GetTargetByNameTarget return value.
                    // acs.ionum = GetTargetByNameTarget(temp2);

                    // if (acs.ionum == -2) //means temp2 is "me" or "self"
                    // acs.ionum = GetInterNum(io);

                    if (params.hasFlag(SpeechParameters.PLAYER)) {
                        // ComputeACSPos(&acs, inter.iobj[0], acs.ionum);
                    } else {
                        // ComputeACSPos(&acs, io, -1);
                    }
                }
                if (params.hasFlag(SpeechParameters.SPEECH_CCCTALKER_L)
                        || params
                                .hasFlag(SpeechParameters.SPEECH_CCCTALKER_R)) {
                    if (params.hasFlag(SpeechParameters.SPEECH_CCCTALKER_L)) {
                        // acs.type = ARX_CINE_SPEECH_CCCTALKER_R;
                    } else {
                        // acs.type = ARX_CINE_SPEECH_CCCTALKER_L;
                    }
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.ionum = GetTargetByNameTarget(temp2);

                    // if (acs.ionum == -2) acs.ionum = GetInterNum(io);

                    // pos = GetNextWord(es, pos, temp2);
                    // acs.startpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.endpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);

                    if (params.hasFlag(SpeechParameters.PLAYER)) {
                        // ComputeACSPos(&acs, inter.iobj[0], acs.ionum);
                    } else {
                        // ComputeACSPos(&acs, io, acs.ionum);
                    }
                }
                if (params.hasFlag(SpeechParameters.SPEECH_CCCLISTENER_L)
                        || params.hasFlag(
                                SpeechParameters.SPEECH_CCCLISTENER_R)) {
                    if (params.hasFlag(SpeechParameters.SPEECH_CCCLISTENER_L)) {
                        // acs.type = ARX_CINE_SPEECH_CCCLISTENER_L;
                    } else {
                        // acs.type = ARX_CINE_SPEECH_CCCLISTENER_R;
                    }
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.ionum = GetTargetByNameTarget(temp2);

                    // if (acs.ionum == -2) acs.ionum = GetInterNum(io);

                    // pos = GetNextWord(es, pos, temp2);
                    // acs.startpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.endpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);

                    if (params.hasFlag(SpeechParameters.PLAYER)) {
                        // ComputeACSPos(&acs, inter.iobj[0], acs.ionum);
                    } else {
                        // ComputeACSPos(&acs, io, acs.ionum);
                    }
                }
                if (params.hasFlag(SpeechParameters.SIDE_L)
                        || params.hasFlag(SpeechParameters.SIDE_R)) {
                    if (params.hasFlag(SpeechParameters.SIDE_L)) {
                        // acs.type = ARX_CINE_SPEECH_SIDE_LEFT;
                    } else {
                        // acs.type = ARX_CINE_SPEECH_SIDE;
                    }
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.ionum = GetTargetByNameTarget(temp2);

                    // if (acs.ionum == -2) acs.ionum = GetInterNum(io);

                    // pos = GetNextWord(es, pos, temp2);
                    // acs.startpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.endpos = GetVarValueInterpretedAsFloat(temp2, esss,
                    // io);
                    // startdist
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.f0 = GetVarValueInterpretedAsFloat(temp2, esss, io);
                    // enddist
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.f1 = GetVarValueInterpretedAsFloat(temp2, esss, io);
                    // height modifier
                    // pos = GetNextWord(es, pos, temp2);
                    // acs.f2 = GetVarValueInterpretedAsFloat(temp2, esss, io);

                    if (params.hasFlag(SpeechParameters.PLAYER)) {
                        // ComputeACSPos(&acs, inter.iobj[0], acs.ionum);
                    } else {
                        // ComputeACSPos(&acs, io, acs.ionum);
                    }
                }
            }

            long speechnum = 0;

            if (params.getSpeechName() == null
                    || params.getSpeechName().length() == 0) {
                // ARX_SPEECH_ClearIOSpeech(io);
            } else {
                if (params.hasFlag(SpeechParameters.NO_TEXT)) {
                    // voixoff |= ARX_SPEECH_FLAG_NOTEXT;
                }

                // if (!CINEMASCOPE) voixoff |= ARX_SPEECH_FLAG_NOTEXT;
                if (params.hasFlag(SpeechParameters.PLAYER)) {
                    // speechnum = ARX_SPEECH_AddSpeech(inter.iobj[0], temp1,
                    // PARAM_LOCALISED, mood, voixoff);
                } else {
                    // speechnum = ARX_SPEECH_AddSpeech(io, temp1,
                    // PARAM_LOCALISED, mood, voixoff);
                }

                if (speechnum >= 0) {
                    // ARX_SCRIPT_Timer_GetDefaultName(timername2);
                    // sprintf(timername, "SPEAK_%s", timername2);
                    // aspeech[speechnum].scrpos = pos;
                    // aspeech[speechnum].es = es;
                    // aspeech[speechnum].ioscript = io;
                    if (params.hasFlag(SpeechParameters.UNBREAKABLE)) {
                        // aspeech[speechnum].flags |=
                        // ARX_SPEECH_FLAG_UNBREAKABLE;
                    }

                    // memcpy(&aspeech[speechnum].cine, &acs,
                    // sizeof(ARX_CINEMATIC_SPEECH));
                    // pos = GotoNextLine(es, pos);
                }
            }
        }
    }
    /**
     * Sends a scripted event to the event stack for all members of a group, to
     * be fired during the game cycle.
     * @param group the name of the IO group
     * @param msg the script message
     * @param params the parameters assigned to the script
     * @param eventname the event name
     * @throws RPGException if an error occurs
     */
    public final void stackSendGroupScriptEvent(final String group,
            final int msg, final Object[] params, final String eventname)
            throws RPGException {
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO io = (IO) Interactive.getInstance().getIO(i);
                if (isIOInGroup(io, group)) {
                    stackSendIOScriptEvent(io, msg, params, eventname);
                }
                io = null;
            }
        }
    }
    /**
     * Sends an IO scripted event to the event stack, to be fired during the
     * game cycle.
     * @param io the IO
     * @param msg the script message
     * @param params the parameters assigned to the script
     * @param eventname the event name
     */
    public final void stackSendIOScriptEvent(final IO io,
            final int msg, final Object[] params, final String eventname) {
        for (int i = 0; i < ScriptConsts.MAX_EVENT_STACK; i++) {
            if (!getStackedEvent(i).exists()) {
                if (params != null
                        && params.length > 0) {
                    getStackedEvent(i).setParams(params);
                } else {
                    getStackedEvent(i).setParams(null);
                }
                if (eventname != null
                        && eventname.length() > 0) {
                    getStackedEvent(i).setEventname(eventname);
                } else {
                    getStackedEvent(i).setEventname(null);
                }

                getStackedEvent(i).setSender(eventSender);
                getStackedEvent(i).setIo(io);
                getStackedEvent(i).setMsg(msg);
                getStackedEvent(i).setExist(true);
                break;
            }
        }
    }
    /**
     * Adds messages for all NPCs to the event stack.
     * @param msg the message
     * @param dat the message parameters
     * @throws RPGException if an error occurs
     */
    public final void stackSendMsgToAllNPCIO(final int msg, final Object[] dat)
            throws RPGException {
        int i = Interactive.getInstance().getMaxIORefId();
        for (; i >= 0; i--) {
            if (Interactive.getInstance().hasIO(i)) {
                IO io = (IO) Interactive.getInstance().getIO(i);
                if (io.hasIOFlag(IoGlobals.IO_03_NPC)) {
                    stackSendIOScriptEvent(io, msg, dat, null);
                }
            }
        }
    }
    /**
     * Starts a timer using a defined set of parameters.
     * @param params the parameters
     */
    public final void startTimer(
            final ScriptTimerInitializationParameters<IO> params) {
        int timerNum = timerGetFree();
        ScriptTimer timer = getScriptTimer(timerNum);
        timer.setScript(params.getScript());
        timer.setExists(true);
        timer.setIo(params.getIo());
        timer.setCycleLength(params.getMilliseconds());
        if (params.getName() == null
                || (params.getName() != null
                        && params.getName().length() == 0)) {
            timer.setName(timerGetDefaultName());
        } else {
            timer.setName(params.getName());
        }
        timer.setAction(new ScriptTimerAction(
                params.getObj(),
                params.getMethod(),
                params.getArgs()));
        timer.setLastTimeCheck(params.getStartTime());
        timer.setRepeatTimes(params.getRepeatTimes());
        timer.clearFlags();
        timer.addFlag(params.getFlagValues());
    }
    /**
     * Teleports an IO to a target location.
     * @param io the io calling for the teleport event
     * @param behind flag indicating the target teleports behind
     * @param isPlayer flag indicating object being teleported is the player
     * @param initPosition flag indicating the object being teleported goes to
     *            its initial position
     * @param target the name of teleport destination
     * @throws RPGException if an error occurs
     */
    public final void teleport(final IO io, final boolean behind,
            final boolean isPlayer, final boolean initPosition,
            final String target) throws RPGException {
        if (behind) {
            Interactive.getInstance().ARX_INTERACTIVE_TeleportBehindTarget(io);
        } else {
            if (!initPosition) {
                int ioid =
                        Interactive.getInstance().getTargetByNameTarget(target);

                if (ioid == -2) {
                    ioid = Interactive.getInstance().GetInterNum(io);
                }
                if (ioid != -1
                        && ioid != -2) {
                    if (ioid == -3) {
                        if (io.getShow() != IoGlobals.SHOW_FLAG_LINKED
                                && io.getShow() != IoGlobals.SHOW_FLAG_HIDDEN
                                && io.getShow() != IoGlobals.SHOW_FLAG_MEGAHIDE
                                && io.getShow() != IoGlobals.SHOW_FLAG_DESTROYED
                                && io.getShow() != IoGlobals.SHOW_FLAG_KILLED) {
                            io.setShow(IoGlobals.SHOW_FLAG_IN_SCENE);
                        }
                        IO pio = (IO) Interactive.getInstance().getIO(
                                ProjectConstants.getInstance().getPlayer());
                        Interactive.getInstance().ARX_INTERACTIVE_Teleport(
                                io, pio.getPosition());
                        pio = null;
                    } else {
                        if (Interactive.getInstance().hasIO(ioid)) {
                            IO tio = (IO) Interactive.getInstance().getIO(ioid);
                            SimpleVector2 pos = new SimpleVector2();

                            if (Interactive.getInstance()
                                    .GetItemWorldPosition(tio, pos)) {
                                if (isPlayer) {
                                    IO pio = (IO) Interactive.getInstance()
                                            .getIO(
                                                    ProjectConstants
                                                            .getInstance()
                                                            .getPlayer());
                                    Interactive.getInstance()
                                            .ARX_INTERACTIVE_Teleport(
                                                    pio, pos);
                                    pio = null;
                                } else {
                                    if (io.hasIOFlag(IoGlobals.IO_03_NPC)
                                            && io.getNPCData()
                                                    .getBaseLife() <= 0) {
                                        // do nothing
                                    } else {
                                        if (io.getShow() != IoGlobals.SHOW_FLAG_HIDDEN
                                                && io.getShow() != IoGlobals.SHOW_FLAG_MEGAHIDE) {
                                            io.setShow(
                                                    IoGlobals.SHOW_FLAG_IN_SCENE);
                                        }
                                        Interactive.getInstance()
                                                .ARX_INTERACTIVE_Teleport(
                                                        io, pos);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (io != null) {
                    if (isPlayer) {
                        SimpleVector2 pos = new SimpleVector2();
                        if (Interactive.getInstance().GetItemWorldPosition(io,
                                pos)) {
                            IO pio = (IO) Interactive.getInstance().getIO(
                                    ProjectConstants.getInstance().getPlayer());
                            Interactive.getInstance().ARX_INTERACTIVE_Teleport(
                                    pio, pos);
                            pio = null;

                        }
                    } else {
                        if (io.hasIOFlag(IoGlobals.IO_03_NPC)
                                && io.getNPCData().getBaseLife() <= 0) {
                            // do nothing
                        } else {
                            if (io.getShow() != IoGlobals.SHOW_FLAG_HIDDEN
                                    && io.getShow() != IoGlobals.SHOW_FLAG_MEGAHIDE) {
                                io.setShow(IoGlobals.SHOW_FLAG_IN_SCENE);
                            }
                            Interactive.getInstance().ARX_INTERACTIVE_Teleport(
                                    io, io.getInitPosition());
                        }
                    }
                }
            }
        }
    }
    public final void timerCheck() throws RPGException {
        if (countTimers() > 0) {
            for (int i = 0, len = this.maxTimerScript; i < len; i++) {
                TIMER timer = getScriptTimers()[i];
                if (timer.exists()) {
                    long currentTime = Time.getInstance().getGameTime();
                    System.out.println(currentTime);
                    if (timer.isTurnBased()) {
                        currentTime = Time.getInstance().getGameRound();
                    }
                    if (timer.hasFlag(1)) {
                        if (!timer.getIo().hasGameFlag(
                                IoGlobals.GFLAG_ISINTREATZONE)) {
                            while (timer.getLastTimeCheck()
                                    + timer.getCycleLength() < currentTime) {
                                timer.setLastTimeCheck(timer.getLastTimeCheck()
                                        + timer.getCycleLength());
                            }
                            continue;
                        }
                    }
                    System.out.println((timer.getLastTimeCheck()
                            + timer.getCycleLength()));
                    if (timer.getLastTimeCheck()
                            + timer.getCycleLength() < currentTime) {
                        SCRIPTABLE script = (SCRIPTABLE) timer.getScript();
                        IO io = timer.getIo();
                        System.out.println("script::" + script);
                        System.out.println("io::" + io);
                        System.out.println(Interactive.getInstance().hasIO(io));
                        if (script != null) {
                            if (timer.getName().equalsIgnoreCase("_R_A_T_")) {
                                // if (Manage_Specific_RAT_Timer(st))
                                continue;
                            }
                        }
                        if (timer.getRepeatTimes() == 1) {
                            timerClearByNum(i);
                        } else {
                            if (timer.getRepeatTimes() != 0) {
                                timer.setRepeatTimes(
                                        timer.getRepeatTimes() - 1);
                            }
                            timer.setLastTimeCheck(timer.getLastTimeCheck()
                                    + timer.getCycleLength());
                        }
                        System.out.println(script);
                        System.out.println(Interactive.getInstance().hasIO(io));
                        if (script != null
                                && Interactive.getInstance().hasIO(io)) {
                            timer.getAction().process();
                        }
                        script = null;
                        io = null;
                    }
                }
                timer = null;
            }
        }
    }
    /** Clears all timers in play. */
    public final void timerClearAll() {
        for (int i = 0; i < maxTimerScript; i++) {
            timerClearByNum(i);
        }
    }
    public final void timerClearAllLocalsForIO(final IO io) {
        TIMER[] scriptTimers = getScriptTimers();
        for (int i = 0; i < maxTimerScript; i++) {
            if (scriptTimers[i].exists()) {
                if (scriptTimers[i].getIo().equals(io)
                        && scriptTimers[i].getScript()
                                .equals(io.getOverscript())) {
                    timerClearByNum(i);
                }
            }
        }
    }
    /**
     * Clears a timer by the IO assigned to it.
     * @param io the IO
     */
    public final void timerClearByIO(final IO io) {
        if (io != null) {
            TIMER[] scriptTimers = getScriptTimers();
            for (int i = 0; i < maxTimerScript; i++) {
                if (scriptTimers[i] != null
                        && scriptTimers[i].exists()) {
                    if (scriptTimers[i].getIo().getRefId() == io.getRefId()) {
                        timerClearByNum(i);
                    }
                }
            }
        }
    }
    public final void timerClearByNameAndIO(final String timername,
            final IO io) {
        if (io != null) {
            TIMER[] scriptTimers = getScriptTimers();
            for (int i = 0; i < maxTimerScript; i++) {
                if (scriptTimers[i] != null
                        && scriptTimers[i].exists()) {
                    if (scriptTimers[i].getIo().getRefId() == io.getRefId()
                            && timername.equalsIgnoreCase(
                                    scriptTimers[i].getName())) {
                        timerClearByNum(i);
                    }
                }
            }
        }
    }
    /**
     * Clears a timer by its index on the timers list.
     * @param timeridx the index
     */
    public void timerClearByNum(final int timeridx) {
        TIMER[] scriptTimers = getScriptTimers();
        if (scriptTimers[timeridx].exists()) {
            scriptTimers[timeridx].setName(null);
            scriptTimers[timeridx].setExists(false);
        }
    }
    /**
     * Determines whether a specific named timer exists.
     * @param texx the timer's name
     * @return the timer's index if it exists, otherwise returns -1
     */
    private int timerExist(final String texx) {
        int index = -1;
        TIMER[] scriptTimers = getScriptTimers();
        for (int i = 0; i < maxTimerScript; i++) {
            if (scriptTimers[i].exists()) {
                if (scriptTimers[i].getName() != null
                        && scriptTimers[i].getName().equalsIgnoreCase(texx)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }
    /**
     * Initializes all game timers.
     * @param number the maximum number of timers used. Must be at least 100.
     */
    public final void timerFirstInit(final int number) {
        if (number < 100) {
            setMaxTimerScript(100);
        } else {
            setMaxTimerScript(number);
        }
        destroyScriptTimers();
        initScriptTimers();
    }
    /**
     * Generates a random name for an unnamed timer.
     * @return {@link String}
     */
    private String timerGetDefaultName() {
        int i = 1;
        String texx;

        while (true) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("TIMER_");
                sb.append(i);
            } catch (PooledException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            i++;

            if (timerExist(sb.toString()) == -1) {
                texx = sb.toString();
                sb.returnToPool();
                sb = null;
                break;
            }
            sb.returnToPool();
            sb = null;
        }
        return texx;
    }
    /**
     * Gets the index of a free script timer.
     * @return <code>int</code>
     */
    public final int timerGetFree() {
        int index = -1;
        TIMER[] scriptTimers = getScriptTimers();
        for (int i = 0; i < maxTimerScript; i++) {
            if (!scriptTimers[i].exists()) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Determines if an IO is speaking.
     * @param io the IO
     * @return <tt>true</tt> if the IO is speaking; <tt>false</tt> otherwise
     */
    public boolean amISpeaking(final IO io) {
        // TODO Auto-generated method stub
        // GO THROUGH ALL SPEECH INSTANCES.  IF THE IO IS SPEAKING
        // RETURN FALSE.  OTHERWISE TRUE
        //for (long i = 0; i < MAX_ASPEECH; i++) {
            //if (aspeech[i].exist) {
                //if (io == aspeech[i].io) {
                    //*lcontent = 1;
                    //return TYPE_LONG;
                //}
            //}
        //}
        return false;
    }
    public long getGameSeconds() {
        return Time.getInstance().getGameTime(false);
    }
}
