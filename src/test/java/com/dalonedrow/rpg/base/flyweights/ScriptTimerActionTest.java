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
public class ScriptTimerActionTest {
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
		Method method = this.getClass().getMethod("noArgsMethod");
		ScriptTimerAction noa = new ScriptTimerAction(this, method, null);
		assertTrue(noa.exists());
		noa.clear();
		assertFalse(noa.exists());
		noa.process();
		assertFalse(noargs);
		noa.set(this, method, null);
		noa.process();
		assertTrue(noargs);
		method = this.getClass().getMethod("argsMethod",
				new Class[] { String.class });
		ScriptTimerAction a = new ScriptTimerAction(
				this, method, new Object[] { "args" });
		assertNull(s);
		a.process();
		assertEquals("args", s);
		noargs = false;
		assertFalse(noargs);
		a.set(noa);
		a.process();
		assertTrue(noargs);
	}
}
