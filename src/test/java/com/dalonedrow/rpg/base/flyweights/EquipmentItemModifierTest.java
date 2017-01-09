package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EquipmentItemModifierTest {
	EquipmentItemModifier data;
	@Before
	public void before() {
		data = new EquipmentItemModifier();
		data.setPercentage(true);
		data.setSpecial(1);
		data.setValue(64);
	}
	@Test
	public void canAddFlags() {
		assertTrue(data.isPercentage());
		assertEquals(1, data.getSpecial());
		assertEquals(64, data.getValue(), .001f);
	}
	@Test
	public void canClearAll() {
		data.setPercentage(true);
		data.setSpecial(12);
		data.setValue(10);
		assertTrue(data.isPercentage());
		assertEquals(12, data.getSpecial());
		assertEquals(10, data.getValue(), .001f);
		data.clearData();
		assertFalse(data.isPercentage());
		assertEquals(data.getSpecial(), 0);
		assertEquals(data.getValue(), 0, .00001);

	}
}
