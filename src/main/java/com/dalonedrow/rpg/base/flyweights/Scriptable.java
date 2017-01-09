/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import java.util.HashMap;
import java.util.Map;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * @author Donald
 * @param <IO>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Scriptable<IO extends BaseInteractiveObject> {
    /** bit flag storing which events are allowed. */
    private long allowedEvent;
    /** the list of actions for an event. */
    private Map<Integer, ScriptAction[]> eventActions;
    /** the IO associated with this script. */
    private IO io;
    /** the array of local {@link ScriptVariable}s. */
    private ScriptVariable[] lvar;
    /** the master script. */
    private Scriptable<IO> master;
    /** the list of script timers. */
    private final int[] timers;
    /** Creates a new instance of {@link Scriptable}. */
    public Scriptable() {
        this(null);
    }
    /**
     * Creates a new instance of {@link Scriptable}.
     * @param ioInstance the IO associated with this script
     */
    public Scriptable(final IO ioInstance) {
        timers = new int[ScriptConstants.MAX_SCRIPTTIMERS];
        lvar = new ScriptVariable[0];
        eventActions = new HashMap<Integer, ScriptAction[]>();
        io = ioInstance;
    }
    /**
     * Adds a local variable.
     * @param svar the local variable
     */
    public final void addLocalVariable(final ScriptVariable svar) {
        int index = -1;
        for (int i = 0; i < lvar.length; i++) {
            if (lvar[i] == null) {
                lvar[i] = svar;
                index = i;
                break;
            }
        }
        if (index == -1) {
            lvar = ArrayUtilities.getInstance().extendArray(svar, lvar);
        }
    }
    /**
     * Adds a {@link ScriptAction} to the list of actions for an event.
     * @param eventID the event ID - usually the script message #
     * @param action the script action
     */
    public final void addScriptAction(final int eventID,
            final ScriptAction action) {
        if (eventActions.get(eventID) == null) {
            eventActions.put(eventID, new ScriptAction[0]);
        }
        if (action != null) {
            action.setScript(this);
            eventActions.put(eventID, ArrayUtilities.getInstance().extendArray(
                    action, eventActions.get(eventID)));
        }
    }
    /**
     * Assigns a bit flag for an allowed event.
     * @param event the event flag
     */
    public final void assignDisallowedEvent(final int event) {
        allowedEvent |= event;
    }
    /** Clears the bit flags for allowed events. */
    public final void clearDisallowedEvents() {
        allowedEvent = 0;
    }
    /**
     * Clears a local variable assigned to the {@link Scriptable}.
     * @param varName the variable's name
     */
    public final void clearLocalVariable(final String varName) {
        for (int i = lvar.length - 1; i >= 0; i--) {
            if (lvar[i] != null
                    && lvar[i].getName() != null
                    && lvar[i].getName().equalsIgnoreCase(varName)) {
                lvar[i].clear();
            }
            lvar[i] = null;
        }
    }
    /** Clears all local variables assigned to the {@link Scriptable}. */
    public final void clearLocalVariables() {
        for (int i = lvar.length - 1; i >= 0; i--) {
            if (lvar[i] != null) {
                lvar[i].clear();
            }
            lvar[i] = null;
        }
    }
    /**
     * Gets all event actions for a scripted event.
     * @param eventID the event ID - usually the script message #
     * @return {@link ScriptAction}[]
     */
    public final ScriptAction[] getEventActions(final int eventID) {
        ScriptAction[] actions = new ScriptAction[0];
        if (eventActions.get(eventID) == null) {
            eventActions.put(eventID, actions);
        }
        return eventActions.get(eventID);
    }
    /**
     * Gets the IO associated with this script.
     * @return {@link IO}
     */
    public final IO getIO() {
        return io;
    }
    /**
     * Gets the local floating-point array value assigned to a specific
     * variable.
     * @param name the variable name
     * @return {@link String}
     * @throws PooledException if one occurs
     * @throws RPGException if no such variable was assigned
     */
    public final float[] getLocalFloatArrayVariableValue(final String name)
            throws PooledException, RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_11_FLOAT_ARR) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            sb.append("Local floating-point array type variable ");
            sb.append(name);
            sb.append(" was never set.");
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getFloatArrayVal();
    }
    /**
     * Gets the local floating-point value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws PooledException if one occurs
     * @throws RPGException if no such variable was assigned
     */
    public final float getLocalFloatVariableValue(final String name)
            throws PooledException, RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_10_FLOAT) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            sb.append("Local floating-point variable ");
            sb.append(name);
            sb.append(" was never set.");
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getFloatVal();
    }
    /**
     * Gets the local integer array value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws PooledException if one occurs
     * @throws RPGException if no such variable was assigned
     */
    public final int[] getLocalIntArrayVariableValue(final String name)
            throws PooledException, RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_13_INT_ARR) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            sb.append("Local floating-point variable ");
            sb.append(name);
            sb.append(" was never set.");
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getIntArrayVal();
    }
    /**
     * Gets the local integer value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws RPGException if no such variable was assigned
     */
    public final int getLocalIntVariableValue(final String name)
            throws RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_12_INT) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Local integer variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getIntVal();
    }
    /**
     * Gets the local long integer value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws PooledException if one occurs
     * @throws RPGException if no such variable was assigned
     */
    public final long[] getLocalLongArrayVariableValue(final String name)
            throws PooledException, RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_15_LONG_ARR) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            sb.append("Local floating-point variable ");
            sb.append(name);
            sb.append(" was never set.");
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getLongArrayVal();
    }
    /**
     * Gets the local long integer value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws PooledException if one occurs
     * @throws RPGException if no such variable was assigned
     */
    public final long getLocalLongVariableValue(final String name)
            throws PooledException, RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_14_LONG) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            sb.append("Local floating-point variable ");
            sb.append(name);
            sb.append(" was never set.");
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getLongVal();
    }
    /**
     * Gets the local text array value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws RPGException if no such variable was assigned
     */
    public final String[] getLocalStringArrayVariableValue(final String name)
            throws RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_09_TEXT_ARR) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Local string array variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getTextArrayVal();
    }
    /**
     * Gets the local text value assigned to a specific variable.
     * @param name the variable name
     * @return {@link String}
     * @throws RPGException if no such variable was assigned
     */
    public final String getLocalStringVariableValue(final String name)
            throws RPGException {
        ScriptVariable svar = getLocalVariable(name);
        if (svar == null
                || svar.getType() != ScriptConstants.TYPE_L_08_TEXT) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Local string variable ");
                sb.append(name);
                sb.append(" was never set.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            throw ex;
        }
        return svar.getText();
    }
    /**
     * Gets the length of the local variable array.
     * @return int
     */
    public final int getLocalVarArrayLength() {
        return lvar.length;
    }
    /**
     * Gets a local {@link Scriptable} variable.
     * @param index the index of the variable
     * @return {@link ScriptVariable}
     */
    public final ScriptVariable getLocalVariable(final int index) {
        ScriptVariable svar = null;
        if (index >= 0
                && index < lvar.length) {
            svar = lvar[index];
        }
        return svar;
    }
    /**
     * Gets a local {@link Scriptable} variable.
     * @param name the name of the variable
     * @return {@link ScriptVariable}
     */
    public final ScriptVariable getLocalVariable(final String name) {
        ScriptVariable svar = null;
        for (int i = lvar.length - 1; i >= 0; i--) {
            if (lvar[i] != null
                    && lvar[i].getName() != null
                    && lvar[i].getName().equalsIgnoreCase(name)) {
                svar = lvar[i];
                break;
            }
        }
        return svar;
    }
    /**
     * Gets the master script.
     * @return {@link Scriptable<IO>}
     */
    public final Scriptable<IO> getMaster() {
        return master;
    }
    /**
     * Gets a specific script timer's reference id.
     * @param index the timer's index
     * @return {@link int}
     */
    public final int getTimer(final int index) {
        return timers[index];
    }
    /**
     * Determines if the {@link InteractiveObject} allows a specific event.
     * @param event the event flag
     * @return true if the object has the event set; false otherwise
     */
    public final boolean hasAllowedEvent(final int event) {
        return (allowedEvent & event) == event;
    }
    /**
     * Determines if a {@link ScriptObject} has local variable with a specific
     * name.
     * @param name the variable name
     * @return <tt>true</tt> if the {@link ScriptObject} has the local variable;
     *         <tt>false</tt> otherwise
     */
    public final boolean hasLocalVariable(final String name) {
        return getLocalVariable(name) != null;
    }
    /**
     * Determines if a {@link ScriptObject} has local variables assigned to it.
     * @return true if the {@link ScriptObject} has local variables; false
     *         otherwise
     */
    public final boolean hasLocalVariables() {
        boolean has = false;
        for (int i = lvar.length - 1; i >= 0; i--) {
            if (lvar[i] != null) {
                has = true;
                break;
            }
        }
        return has;
    }
    /**
     * Script run when the {@link Scriptable} is added to a party.
     * @return {@link int}
     * @throws RPGException when an error occurs
     */
    public int onAddToParty() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * Script run when the {@link Scriptable} is a target of aggression.
     * @return {@link int}
     * @throws RPGException when an error occurs
     */
    public int onAggression() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO combine.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onCombine() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO dies.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onDie() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO equipped.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onEquip() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO hit.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onHit() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO attempt to identify.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onIdentify() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO initialization.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInit() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO initialization end.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInitEnd() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO closes inventory.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInventoryClose() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO goes into inventory.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInventoryIn() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO opens inventory.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInventoryOpen() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO comes out of inventory.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInventoryOut() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO is used inside inventory.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onInventoryUse() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    public int onLoad() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO traveling on the game map.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onMovement() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO ouch.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onOuch() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    public int onReload() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    public int onSpellcast() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * On IO unequipped.
     * @return <code>int</code>
     * @throws RPGException if an error occurs
     */
    public int onUnequip() throws RPGException {
        return ScriptConstants.ACCEPT;
    }
    /**
     * Removed an event from the list of allowed events.
     * @param event the event flag
     */
    public final void removeDisallowedEvent(final int event) {
        allowedEvent = allowedEvent & ~event;
    }
    /**
     * Sets the IO associated with this script.
     * @param val the IO to set
     */
    public final void setIO(final IO val) {
        io = val;
    }
    /**
     * Sets a local {@link ScriptVariable}.
     * @param index the index of the variable
     * @param svar the local {@link ScriptVariable}
     * @throws PooledException if one occurs
     * @throws RPGException if no such variable was assigned
     */
    public final void setLocalVariable(final int index,
            final ScriptVariable svar) throws RPGException {
        // if the index number is valid
        if (index >= 0) {
            // if the local variables array needs to be extended, do so
            if (index >= lvar.length) {
                ScriptVariable[] dest = new ScriptVariable[index + 1];
                System.arraycopy(lvar, 0, dest, 0, lvar.length);
                lvar = dest;
                dest = null;
            }
            lvar[index] = svar;
        } else {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Invalid array index ");
                sb.append(index);
                sb.append(".");
            } catch (PooledException e) {
                sb.returnToPool();
                sb = null;
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                    sb.toString());
            sb.returnToPool();
            sb = null;
            throw ex;
        }
    }
    /**
     * Sets a local {@link ScriptVariable}.
     * @param svar the local {@link ScriptVariable}
     */
    public final void setLocalVariable(final ScriptVariable svar) {
        if (svar != null) {
            boolean found = false;
            for (int i = lvar.length - 1; i >= 0; i--) {
                if (lvar[i] != null
                        && lvar[i].getName() != null
                        && lvar[i].getName().equalsIgnoreCase(svar.getName())) {
                    lvar[i] = svar;
                    found = true;
                    break;
                }
            }
            // if the local variable was not found
            if (!found) {
                // find an empty index
                int i = lvar.length - 1;
                for (; i >= 0; i--) {
                    if (lvar[i] == null) {
                        break;
                    }
                }
                if (i >= 0) {
                    lvar[i] = svar;
                } else {
                    lvar = ArrayUtilities.getInstance().extendArray(svar, lvar);
                }
            }
        }
    }
    /**
     * Sets a global variable.
     * @param name the name of the global variable
     * @param value the variable's value
     * @throws RPGException if an error occurs
     */
    public final void setLocalVariable(final String name, final Object value)
            throws RPGException {
        boolean found = false;
        for (int i = 0, len = lvar.length; i < len; i++) {
            ScriptVariable svar = lvar[i];
            if (svar != null
                    && svar.getName() != null
                    && svar.getName().equalsIgnoreCase(name)) {
                svar.set(value);
                found = true;
                break;
            }
        }
        if (!found) {
            // create a new variable and add to the global array
            ScriptVariable svar = null;
            if (value instanceof String
                    || value instanceof char[]) {
                svar = new ScriptVariable(name, ScriptConstants.TYPE_L_08_TEXT,
                        value);
            } else if (value instanceof String[]
                    || value instanceof char[][]) {
                svar = new ScriptVariable(name,
                        ScriptConstants.TYPE_L_09_TEXT_ARR, value);
            } else if (value instanceof Float) {
                svar = new ScriptVariable(name, ScriptConstants.TYPE_L_10_FLOAT,
                        value);
            } else if (value instanceof Double) {
                svar = new ScriptVariable(name, ScriptConstants.TYPE_L_10_FLOAT,
                        value);
            } else if (value instanceof float[]) {
                svar = new ScriptVariable(name,
                        ScriptConstants.TYPE_L_11_FLOAT_ARR, value);
            } else if (value instanceof Integer) {
                svar = new ScriptVariable(name, ScriptConstants.TYPE_L_12_INT,
                        value);
            } else if (value instanceof int[]) {
                svar = new ScriptVariable(name,
                        ScriptConstants.TYPE_L_13_INT_ARR, value);
            } else if (value instanceof Long) {
                svar = new ScriptVariable(name, ScriptConstants.TYPE_L_14_LONG,
                        value);
            } else if (value instanceof long[]) {
                svar = new ScriptVariable(name,
                        ScriptConstants.TYPE_L_15_LONG_ARR, value);
            } else {
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                try {
                    sb.append("Local variable ");
                    sb.append(name);
                    sb.append(" was passed new value of type ");
                    sb.append(value.getClass().getCanonicalName());
                    sb.append(". Only String, Float, float[], Integer, int[],");
                    sb.append(" Long, or long[] allowed.");
                } catch (PooledException e) {
                    throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
                }
                RPGException ex = new RPGException(ErrorMessage.INVALID_PARAM,
                        sb.toString());
                sb.returnToPool();
                sb = null;
                throw ex;
            }
            lvar = ArrayUtilities.getInstance().extendArray(svar, lvar);
        }
    }
    /**
     * Sets the master script.
     * @param script the script to set
     */
    public final void setMaster(final Scriptable<IO> script) {
        master = script;
    }
    /**
     * Sets the reference id of the {@link ScriptTimer} found at a specific
     * index.
     * @param index the index
     * @param refId the reference id
     */
    public final void setTimer(final int index, final int refId) {
        timers[index] = refId;
    }
}
