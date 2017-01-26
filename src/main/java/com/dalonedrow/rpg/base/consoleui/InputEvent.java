package com.dalonedrow.rpg.base.consoleui;

import java.lang.reflect.Method;

import com.dalonedrow.engine.systems.base.JOGLErrorHandler;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * Interface handler for all input events. Takes an action, and calls them in
 * the order that they were added.
 * @author DaLoneDrau
 */
public final class InputEvent {
	/** the one and only instance of the <code>InputEvent</code> class. */
	private static InputEvent	instance;
	/**
	 * Gives access to the singleton instance of {@link InputEvent}.
	 * @return {@link InputEvent}
	 */
	public static InputEvent getInstance() {
		if (InputEvent.instance == null) {
			InputEvent.instance = new InputEvent();
		}
		return InputEvent.instance;
	}
	/** the list of actions. */
	private InputAction[]	actions;
	/**
	 * Creates a new instance of {@link InputEvent}.
	 */
	private InputEvent() {
		actions = new InputAction[0];
	}
	/**
	 * Adds an action to be processed.
	 * @param object the object having the action applied
	 * @param method the method invoked
	 * @param args any arguments supplied to the method
	 */
	public void addAction(final Object object, final Method method,
			final Object[] args) {
		// find a free index
		int index = -1;
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] == null) {
				index = i;
				break;
			}
			if (!actions[i].exists()) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			// no free index was found. extend the array
		    actions = (InputAction[])
		            ArrayUtilities.getInstance().extendArray(
		                    new InputAction(object, method, args), actions);
		} else {
			actions[index].set(object, method, args);
		}
	}
	/** Processes all actions. */
	public void processActions() {
		try {
			for (int i = 0; i < actions.length; i++) {
				if (actions[i].exists()) {
					actions[i].process();
				}
			}
		} catch (Exception ex) {
			JOGLErrorHandler.getInstance().fatalError(ex);
		}
	}
}
