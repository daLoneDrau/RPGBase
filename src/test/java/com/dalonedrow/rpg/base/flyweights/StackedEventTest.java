package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.engine.systems.base.TestInteractiveInstance;

/**
 * Test cases for {@link StackedEvent}.
 * @author drau
 *
 */
public class StackedEventTest {
	/** Tests instantiation. */
	@Test
	public void canInit() {
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		BaseInteractiveObject io = interactive.getTestIO();
		StackedEvent se = new StackedEvent();
		assertNotNull(se);
		se.setEventname("name");
		assertEquals("name", se.getEventname());
		se.setExist(true);
		assertTrue(se.exists());
		se.setIo(io);
		assertEquals(io, se.getIo());
		se.setMsg(32);
		assertEquals(32, se.getMsg());
		Object[] os = new Object[5];
		se.setParams(os);
		assertEquals(os, se.getParams());
		se.setSender(io);
		assertEquals(io, se.getSender());
	}
}
