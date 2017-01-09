package com.dalonedrow.engine.systems.base;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * Testing class.
 * @author drau
 */
public final class TestInteractiveInstance
extends Interactive<BaseInteractiveObject> {
	/** the next available id. */
	private int						nextId;
	/** the list of {@link BaseInteractiveObject}s. */
	private BaseInteractiveObject[]	objs;
	/** Sets the test instance as the static instance. */
	public TestInteractiveInstance() {
		super.setInstance(this);
		if (objs == null) {
			objs = new BaseInteractiveObject[0];
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAnimation(int id, int animId) throws RPGException {
		// TODO Auto-generated method stub
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BaseInteractiveObject addItem(String item, long flags)
			throws RPGException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ARX_INTERACTIVE_ForceIOLeaveZone(
			BaseInteractiveObject io, long flags) {
		// TODO Auto-generated method stub
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxIORefId() {
		return nextId;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BaseInteractiveObject getNewIO() {
		// step 1 - find the next id
		int id = nextId++;
		BaseInteractiveObject io = new BaseInteractiveObject(id) {
			
		};
		// step 2 - find the next available index in the objs array
		int index = -1;
		for (int i = objs.length - 1; i >= 0; i--) {
			if (objs[i] == null) {
				index = i;
				break;
			}
		}
		// step 3 - put the new object into the arrays
		if (index < 0) {
			objs = ArrayUtilities.getInstance().extendArray(io, objs);
		} else {
			objs[index] = io;
		}
		return io;
	}
	/**
	 * Gets a new IO for testing purposes.
	 * @return {@link BaseInteractiveObject}
	 */
	public BaseInteractiveObject getTestIO() {
		return getNewIO();
	}
	@Override
	protected BaseInteractiveObject[] getIOs() {
		return objs;
	}
}
