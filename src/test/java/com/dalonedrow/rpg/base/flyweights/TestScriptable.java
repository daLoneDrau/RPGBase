package com.dalonedrow.rpg.base.flyweights;

public class TestScriptable extends Scriptable<BaseInteractiveObject> {
	private boolean targetDied;
	/**
	 * Gets the value for the targetDied.
	 * @return {@link boolean}
	 */
	public boolean targetDied() {
		return targetDied;
	}
	/**
	 * Sets the value of the targetDied.
	 * @param targetDied the new value to set
	 */
	public void setTargetDied(boolean targetDied) {
		this.targetDied = targetDied;
	}
	public int onTargetDeath() throws RPGException {
		targetDied = true;
		return 1;
	}
}
