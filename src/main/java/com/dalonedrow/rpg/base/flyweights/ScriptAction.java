package com.dalonedrow.rpg.base.flyweights;

/**
 * 
 * @author drau
 *
 * @param <SCRIPT> {@link Scriptable}
 */
@SuppressWarnings("rawtypes")
public interface ScriptAction<SCRIPT extends Scriptable> {
	/**
	 * Executes the script action.
	 * @throws RPGException if an error occurs
	 */
	void execute() throws RPGException;
	/**
	 * Sets the {@link SCRIPT} associated with the action.
	 * @param script the script
	 */
	void setScript(SCRIPT script);
}
