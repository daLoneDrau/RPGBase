package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IOSpellCastDataTest {
	@Test
	public void canAddFlags() {
		IOSpellCastData data = new IOSpellCastData();
		data.addSpellFlag(1);
		data.addSpellFlag(64);
		assertTrue(data.hasSpellFlag(1));
		assertTrue(data.hasSpellFlag(64));
	}
	@Test
	public void canClearFlags() {
		IOSpellCastData data = new IOSpellCastData();
		data.addSpellFlag(1);
		data.addSpellFlag(64);
		assertTrue(data.hasSpellFlag(1));
		assertTrue(data.hasSpellFlag(64));
		data.clearSpellFlags();
		assertFalse(data.hasSpellFlag(1));
		assertFalse(data.hasSpellFlag(64));
	}
	@Test
	public void canRemoveFlags() {
		IOSpellCastData data = new IOSpellCastData();
		data.addSpellFlag(1);
		assertTrue(data.hasSpellFlag(1));
		data.removeSpellFlag(1);
		assertFalse(data.hasSpellFlag(1));
	}
	@Test
	public void canSetFields() {
		IOSpellCastData data = new IOSpellCastData();
		data.setCastingspell(2);
		data.setDuration(5);
		data.setSpellLevel(3);
		data.setTarget(1);
		assertEquals(data.getCastingspell(), 2);
		assertEquals(data.getDuration(), 5);
		assertEquals(data.getTarget(), 1);
		assertEquals(data.getSpellLevel(), 3);
	}
}
