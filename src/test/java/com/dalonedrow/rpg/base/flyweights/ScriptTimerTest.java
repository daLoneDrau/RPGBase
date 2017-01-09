package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.dalonedrow.engine.systems.base.TestInteractiveInstance;

/**
 * Test cases for {@link StackedEvent}.
 * @author drau
 *
 */
public class ScriptTimerTest {
	private String s;
	/** flag. */
	private boolean noargs = false;
	/** No arguments method. */
	public void noArgsMethod() {
		noargs = true;
	}
	/** No arguments method. */
	public void argsMethod(final String a) {
		s = a;
	}
	/**
	 * Tests instantiation. 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void canInit() throws Exception {
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		BaseInteractiveObject io = interactive.getTestIO();
		Method method = this.getClass().getMethod("noArgsMethod");
		ScriptTimerAction noa = new ScriptTimerAction(this, method, null);
		ScriptTimer st = new ScriptTimer();
		st.setAction(noa);
		st.setExists(true);
		st.setIo(io);
		st.setLonginfo(2);
		st.setCycleLength(2000);
		st.setName("name");
		Scriptable s = new Scriptable();
		st.setScript(s);
		st.setLastTimeCheck(2000);
		st.setRepeatTimes(2);
		assertEquals(noa, st.getAction());
		assertTrue(st.exists());
		assertEquals(io, st.getIo());
		assertEquals(2, st.getLonginfo());
		assertEquals(2000, st.getCycleLength());
		assertEquals("name", st.getName());
		assertEquals(s, st.getScript());
		assertEquals(2000, st.getLastTimeCheck());
		assertEquals(2, st.getRepeatTimes());
		st.addFlag(2);
		st.addFlag(1);
		assertTrue(st.hasFlag(1));
		assertTrue(st.hasFlag(2));
		st.removeFlag(1);
		assertFalse(st.hasFlag(1));
		assertTrue(st.hasFlag(2));
		

		ScriptTimerInitializationParameters p =
				new ScriptTimerInitializationParameters();
		Object[] args = new String[] { "args" };
		p.setArgs(args);
		p.setFlagValues(2);
		p.setIo(interactive.getTestIO());
		p.setMethod(getClass().getMethod("argsMethod",
				new Class[] { String.class }));
		p.setMilliseconds(250);
		p.setName("stip");
		p.setObj(this);
		p.setRepeatTimes(1);
		s = new Scriptable();
		p.setScript(s);
		p.setStartTime(5);
		st.set(p);
		
		assertNotEquals(noa, st.getAction());
		assertTrue(st.exists());
		assertNotEquals(io, st.getIo());
		assertEquals(2, st.getLonginfo());
		assertEquals(250, st.getCycleLength());
		assertEquals("stip", st.getName());
		assertEquals(s, st.getScript());
		assertEquals(5, st.getLastTimeCheck());
		assertEquals(1, st.getRepeatTimes());

		assertFalse(st.isTurnBased());
		st.setTurnBased(true);
		assertTrue(st.isTurnBased());
		st.setTurnBased(false);
		assertFalse(st.isTurnBased());
	}
}
