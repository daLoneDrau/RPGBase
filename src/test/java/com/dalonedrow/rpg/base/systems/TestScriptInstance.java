package com.dalonedrow.rpg.base.systems;

import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.ScriptTimer;
import com.dalonedrow.rpg.base.flyweights.StackedEvent;

/**
 * Testing class.
 * @author drau
 */
public class TestScriptInstance extends Script {
	private StackedEvent[]			eventstack;
	/** Sets the test instance as the static instance. */
	public TestScriptInstance() {
		super.setInstance(this);
		eventStackInit();
		super.timerFirstInit(0);
		super.timerFirstInit(100);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void clearAdditionalEventStacks() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void clearAdditionalEventStacksForIO(BaseInteractiveObject io) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void destroyScriptTimers() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventStackInit() {
		eventstack = new StackedEvent[ScriptConsts.MAX_EVENT_STACK];
		for (int i = 0; i < ScriptConsts.MAX_EVENT_STACK; i++) {
			eventstack[i] = new StackedEvent();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeAdditionalStacks() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptTimer getScriptTimer(int id) {
		return timers[id];
	}
	private ScriptTimer[] timers;
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ScriptTimer[] getScriptTimers() {
		return timers;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StackedEvent getStackedEvent(int index) {
		return eventstack[index];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initScriptTimers() {
		timers = new ScriptTimer[super.getMaxTimerScript()];
		for (int i = timers.length - 1; i >= 0; i--) {
			timers[i] = new ScriptTimer();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setScriptTimer(int index, ScriptTimer timer) {
		// TODO Auto-generated method stub
		
	}
}
