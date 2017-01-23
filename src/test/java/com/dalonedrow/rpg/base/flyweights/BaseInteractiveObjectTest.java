package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.engine.systems.base.TestInteractiveInstance;

/**
 * Test cases for {@link StackedEvent}.
 * @author drau
 *
 */
public class BaseInteractiveObjectTest {
	/** Tests instantiation. */
	@Test
	public void canInit() {
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		BehaviourData bd = new BehaviourData();
		assertNotNull(bd);
		bd.setBehaviorParam(2.5f);
		assertEquals(2.5f, bd.getBehaviorParam(), .001f);
		bd.setBehaviour(5);
		assertEquals(5, bd.getBehaviour());
		bd.setExists(true);
		assertTrue(bd.exists());
		bd.setMovemode(3);
		assertEquals(3, bd.getMoveMode());
		bd.setTactics(4);
		assertEquals(4, bd.getTactics());
		bd.setTarget(25);
		assertEquals(25, bd.getTarget());
	}
}
