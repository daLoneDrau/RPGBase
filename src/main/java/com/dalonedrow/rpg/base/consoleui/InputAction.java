package com.dalonedrow.rpg.base.consoleui;

import java.lang.reflect.Method;

/**
 * 
 * @author drau
 *
 */
public class InputAction {
    /**
     * the argument list supplied to the {@link Method} being invoked. can be
     * null.
     */
    private Object[] args;
    /** if true, the {@link InputAction} has an existing action. */
    private boolean  exists = false;
    /** the {@link Method} invoked on the associated {@link Object}. */
    private Method   method;
    /** the {@link Object} associated with the {@link InputAction}. */
    private Object   object;
    /**
     * Creates a new instance of {@link InputAction}.
     * @param o the object having the action applied
     * @param m the method invoked
     * @param a any arguments supplied to the method
     */
    public InputAction(final Object o, final Method m, final Object[] a) {
        exists = true;
        object = o;
        method = m;
        args = a;
    }
    /** Clears the action without processing. */
    public void clear() {
        exists = false;
    }
    /**
     * Determines if the {@link InputAction} has an existing action.
     * @return <tt>true</tt> if the {@link InputAction} has an existing action;
     *         <tt>false</tt> otherwise
     */
    public boolean exists() {
        return exists;
    }
    /**
     * Process the associated action.
     * @throws Exception if an error occurs
     */
    public void process() throws Exception {
        exists = false;
        method.invoke(object, args);
    }
    /**
     * Sets a new action to process.
     * @param action the new action to process
     */
    public void set(final InputAction action) {
        exists = true;
        object = action.object;
        method = action.method;
        args = action.args;
    }
    /**
     * Sets a new action to process.
     * @param o the object having the action applied
     * @param m the method invoked
     * @param a any arguments supplied to the method
     */
    public void set(final Object o, final Method m, final Object[] a) {
        exists = true;
        object = o;
        method = m;
        args = a;
    }
}
