package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.engine.systems.base.TestInteractiveInstance;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;

/**
 * Test cases for {@link StackedEvent}.
 * @author drau
 *
 */
public class BaseInteractiveObjectTest {
	private TestInteractiveObject testObject;
	/** Tests instantiation. */
	@Before
	public void canInit() {
		testObject = new TestInteractiveObject(0);
	}
	@Test(expected = RPGException.class)
	public void willNotAddNullAnimation() throws RPGException, PooledException {
		testObject.addAnimation(null, 1);
	}
	@Test(expected = Exception.class)
	public void willNotGetNullAnimation() throws Exception {
		testObject.getAnimation(null);
	}
	@Test(expected = Exception.class)
	public void willNotGetMissingAnimation() throws Exception {
		testObject.getAnimation("missing");
	}
	@Test
	public void testAnimation() throws Exception {
		testObject.addAnimation("name", 1);
		testObject.addAnimation("name", 2);
		assertEquals(2, testObject.getAnimation("name"));
	}
	@Test
	public void testFlags() {
		assertFalse("has no flags", testObject.hasBehaviorFlag(1));
		testObject.addBehaviorFlag(1);
		testObject.addBehaviorFlag(64);
		assertTrue("has 1 flags", testObject.hasBehaviorFlag(1));
		assertTrue("has 64 flags", testObject.hasBehaviorFlag(64));
		testObject.removeBehaviorFlag(64);
		assertFalse("has 64 flags", testObject.hasBehaviorFlag(64));
		assertTrue("has 1 flags", testObject.hasBehaviorFlag(1));
		testObject.clearBehaviorFlags();
		

		assertFalse("has no flags", testObject.hasGameFlag(1));
		testObject.addGameFlag(1);
		testObject.addGameFlag(64);
		assertTrue("has 1 flags", testObject.hasGameFlag(1));
		assertTrue("has 64 flags", testObject.hasGameFlag(64));
		testObject.removeGameFlag(64);
		assertFalse("has 64 flags", testObject.hasGameFlag(64));
		assertTrue("has 1 flags", testObject.hasGameFlag(1));
		testObject.clearGameFlags();
		

		assertFalse("has no flags", testObject.hasIOFlag(1));
		testObject.addIOFlag(1);
		testObject.addIOFlag(64);
		assertTrue("has 1 flags", testObject.hasIOFlag(1));
		assertTrue("has 64 flags", testObject.hasIOFlag(64));
		testObject.removeIOFlag(64);
		assertFalse("has 64 flags", testObject.hasIOFlag(64));
		assertTrue("has 1 flags", testObject.hasIOFlag(1));
		testObject.clearIOFlags();
	}
	@Test
	public void testGroups() {
		assertFalse("no group", testObject.isInGroup("test"));
		testObject.addGroup("test");
		testObject.addGroup("test");
		testObject.addGroup("test2");
		assertTrue("in group", testObject.isInGroup("test"));
		assertTrue("in group", testObject.isInGroup("test2"));
		assertEquals(2, testObject.getNumIOGroups());
		assertEquals("test", testObject.getIOGroup(0));
		testObject.removeGroup("test");
		assertFalse("no group", testObject.isInGroup("test"));
		assertEquals(1, testObject.getNumIOGroups());
	}
	@Test
	public void testSpells() {
		assertEquals(0, testObject.getNumberOfSpellsOn());
		testObject.addSpellOn(1);
		testObject.addSpellOn(2);
		assertEquals(2, testObject.getNumberOfSpellsOn());
		assertEquals(1, testObject.getSpellOn(0));
		testObject.removeSpellOn(1);
		assertEquals(1, testObject.getNumberOfSpellsOn());
		testObject.removeAllSpells();
		assertEquals(0, testObject.getNumberOfSpellsOn());
	}
	@Test
	public void testTypes() throws RPGException {
		testObject.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER);
		testObject.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_SHIELD);
		testObject.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_FOOD);
	}
	@Test(expected = RPGException.class)
	public void willNotSetWeaponType() throws RPGException {
		testObject.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_WEAPON);
	}
	@Test(expected = RPGException.class)
	public void willNotSetType22() throws RPGException {
		testObject.addTypeFlag(22);
	}
	@Test
	public void testEquals() {
		assertTrue(testObject.equals(testObject));
		assertFalse(testObject.equals(null));
		assertFalse(testObject.equals(new Object()));
		assertTrue(testObject.equals(new TestInteractiveObject(0)));
	}
	@Test
	public void testGettersSetters() {
		testObject.setArmormaterial("velcro");
		assertEquals("velcro", testObject.getArmormaterial());
		testObject.setDamageSum(2.5f);
		assertEquals(2.5f, testObject.getDamageSum(), 0.0001f);
	}
}
