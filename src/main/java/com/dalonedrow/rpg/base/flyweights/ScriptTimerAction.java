package com.dalonedrow.rpg.base.flyweights;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Stores and executes actions associated with a {@link ScriptTimer}.
 * @author drau
 */
public final class ScriptTimerAction {
	/**
	 * the argument list supplied to the {@link Method} being invoked. can be
	 * null.
	 */
	private Object[]	args;
	/** if true, the {@link ScriptTimerAction} has an existing action. */
	private boolean		exists	= false;
	/** the {@link Method} invoked on the associated {@link Object}. */
	private Method		method;
	/** the {@link Object} associated with the {@link ScriptTimerAction}. */
	private Object		object;
	/**
	 * Creates a new instance of {@link ScriptTimerAction}.
	 * @param o the object having the action applied
	 * @param m the method invoked
	 * @param a any arguments supplied to the method
	 */
	public ScriptTimerAction(final Object o, final Method m, final Object[] a) {
		exists = true;
		object = o;
		method = m;
		args = a;
	}
	/** Clears the action without processing. */
	public void clear() {
		exists = false;
		object = null;
		method = null;
		args = null;
	}
	/**
	 * Determines if the {@link ScriptTimerAction} has an existing action.
	 * @return <tt>true</tt> if the {@link ScriptTimerAction} has an existing
	 *         action; <tt>false</tt> otherwise
	 */
	public boolean exists() {
		return exists;
	}
	/**
	 * Process the associated action.
	 * @throws RPGException if an error occurs
	 */
	public void process() throws RPGException {
		if (exists) {
			exists = false;
			try {
				method.invoke(object, args);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
			}
		}
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
	/**
	 * Sets a new action to process.
	 * @param action the new action to process
	 */
	public void set(final ScriptTimerAction action) {
		exists = true;
		object = action.object;
		method = action.method;
		args = action.args;
	}
}
