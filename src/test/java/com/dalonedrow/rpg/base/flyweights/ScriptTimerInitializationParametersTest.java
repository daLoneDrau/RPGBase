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
public class ScriptTimerInitializationParametersTest {
	/** No arguments method. */
	public void noArgsMethod() {
	}
	/**
	 * Tests instantiation. 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void canInit() throws Exception {
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		BaseInteractiveObject io = interactive.getTestIO();
		ScriptTimerInitializationParameters p =
				new ScriptTimerInitializationParameters();
		Object[] args = new String[] { "args" };
		p.setArgs(args);
		assertEquals("args", (String) p.getArgs()[0]);
		p.setFlagValues(2);
		assertEquals(2, p.getFlagValues());
		p.setIo(io);
		assertEquals(io, p.getIo());
		Method method = this.getClass().getMethod("noArgsMethod");
		p.setMethod(method);
		assertEquals(method, p.getMethod());
		p.setMilliseconds(250);
		assertEquals(250, p.getMilliseconds());
		p.setName("stip");
		assertEquals("stip", p.getName());
		p.setObj(this);
		assertEquals(this, p.getObj());
		p.setRepeatTimes(1);
		assertEquals(1, p.getRepeatTimes());
		Scriptable s = new Scriptable();
		p.setScript(s);
		assertEquals(s, p.getScript());
		p.setStartTime(5);
		assertEquals(5, p.getStartTime());
		
		p.clear();
		assertNull(p.getArgs());
		assertEquals(0, p.getFlagValues());
		assertNull(p.getIo());
		assertNull(p.getMethod());
		assertEquals(0, p.getMilliseconds());
		assertNull(p.getName());
		assertNull(p.getObj());
		assertEquals(0, p.getRepeatTimes());
		assertNull(p.getScript());
		assertEquals(0, p.getStartTime());
	}
}
