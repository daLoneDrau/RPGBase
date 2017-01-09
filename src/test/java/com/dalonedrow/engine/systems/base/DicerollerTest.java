package com.dalonedrow.engine.systems.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DicerollerTest {
	@Test
	public void canGetRandom() {
		char[] ca = new char[] { 't', 'e', 's', 't' };
		for (int i = 255; i >= 0; i--) {
			char c = Diceroller.getInstance().getRandomIndex(ca);
			assertTrue(c == ca[0] || c == ca[1] || c == ca[2] || c == ca[3]);
		}
		int[] ia = new int[] { 1, 2, 3, 4 };
		for (int i = 255; i >= 0; i--) {
			int c = Diceroller.getInstance().getRandomIndex(ia);
			assertTrue(c == ia[0] || c == ia[1] || c == ia[2] || c == ia[3]);
		}
		for (int i = 255; i >= 0; i--) {
			long l = Diceroller.getInstance().getRandomLong();
			assertFalse("".equalsIgnoreCase(Long.toString(l)));
		}
		List<Object> ol = new ArrayList<Object>();
		ol.add(new Object());
		ol.add(new Object());
		ol.add(new Object());
		for (int i = 255; i >= 0; i--) {
			Object o = Diceroller.getInstance().getRandomObject(ol);
			assertTrue(ol.contains(o));
		}
		Map<Object, Object> om = new HashMap<Object, Object>();
		om.put(new Object(), new Object());
		om.put(new Object(), new Object());
		om.put(new Object(), new Object());
		for (int i = 255; i >= 0; i--) {
			Object o = Diceroller.getInstance().getRandomObject(om);
			assertTrue(om.containsValue(o));
		}
		Object[] oa = new Object[] { new Object(), new Object(), new Object() };
		for (int i = 255; i >= 0; i--) {
			Object o = Diceroller.getInstance().getRandomObject(oa);
		}
		for (int i = ol.size(); i > 0; i--) {
			Object o = Diceroller.getInstance().removeRandomObject(ol);
			assertFalse(ol.contains(o));
		}
		assertEquals(ol.size(), 0);
		for (int i = om.size(); i > 0; i--) {
			Object o = Diceroller.getInstance().removeRandomObject(om);
			assertFalse(om.containsValue(o));
		}
		assertEquals(om.size(), 0);
		for (int i = 255; i >= 0; i--) {
			float p = Diceroller.getInstance().rollPercent();
			assertTrue(p >= 0.0f && p <= 1.0f);
		}
		for (int i = 255; i >= 0; i--) {
			int d = Diceroller.getInstance().rolldX(6);
			assertTrue(d >= 1 && d <= 6);
		}
		for (int i = 255; i >= 0; i--) {
			int d = Diceroller.getInstance().rollXdY(3, 6);
			assertTrue(d >= 3 && d <= 18);
		}
		for (int i = 255; i >= 0; i--) {
			int d = Diceroller.getInstance().rolldXPlusY(4, 1);
			assertTrue(d >= 2 && d <= 5);
		}
	}
}
