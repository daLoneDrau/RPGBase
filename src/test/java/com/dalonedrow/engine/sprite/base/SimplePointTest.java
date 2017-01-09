package com.dalonedrow.engine.sprite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SimplePointTest {
	SimplePoint pt;
	@Before
	public void before() {
		pt = new SimplePoint();
	}
	@Test
	public void canCompareWithIntCoordinates() {
		assertTrue("pt equals 0,0", pt.equals(0, 0));
		assertFalse("pt does not equal 1,1", pt.equals(1, 1));
	}
	@Test
	public void canCompareWithPoint() {
		assertTrue("pt equals itself", pt.equals(pt));
		SimplePoint pt1 = new SimplePoint(pt);
		assertTrue("pt equals copy of itself", pt.equals(pt1));
		assertTrue("pt equals identical vector",
				pt.equals(new SimpleVector2(0.85f, 0.85f)));
		assertFalse("pt does not equal random object", pt.equals(new Object()));
		assertFalse("pt does not null", pt.equals(null));
	}
	@Test
	public void canCreateDefaultOrigin() {
		assertEquals("x is 0", (int) pt.getX(), 0);
		assertEquals("y is 0", (int) pt.getY(), 0);
	}
	@Test
	public void canCreatePointFromFloat() {
		pt = new SimplePoint(1.85f, 1.85f);
		assertEquals("x is 1", (int) pt.getX(), 1);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test
	public void canCreatePointFromInt() {
		pt = new SimplePoint(1, 1);
		assertEquals("x is 1", (int) pt.getX(), 1);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test
	public void canCreatePointFromPoint() {
		SimplePoint pt1 = new SimplePoint(pt);
		assertEquals("x is 0", (int) pt1.getX(), 0);
		assertEquals("y is 0", (int) pt1.getY(), 0);
	}
	@Test
	public void canCreatePointFromVector() {
		SimplePoint pt1 = new SimplePoint(new SimpleVector2(1.85f, 1.85f));
		assertEquals("x is 1", (int) pt1.getX(), 1);
		assertEquals("y is 1", (int) pt1.getY(), 1);
	}
	@Test
	public void canGetString() {
		assertEquals("got string", pt.toString(), "[x=0,y=0]");
	}
	@Test
	public void canMoveFromDouble() {
		pt.move(1.85f, 1.85f);
		assertEquals("x is 1", (int) pt.getX(), 1);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test
	public void canMoveFromInt() {
		pt.move(1, 1);
		assertEquals("x is 1", (int) pt.getX(), 1);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test
	public void canMoveFromPoint() {
		SimplePoint pt1 = new SimplePoint(new SimpleVector2(1.85f, 1.85f));
		pt.move(pt1);
		assertEquals("x is 1", (int) pt.getX(), 1);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test
	public void canSetXFromFloat() {
		pt.setX(1.85f);
		assertEquals("x is 1", (int) pt.getX(), 1);
	}
	@Test
	public void canSetXFromInt() {
		pt.setX(1);
		assertEquals("x is 1", (int) pt.getX(), 1);
	}
	@Test
	public void canSetYFromFloat() {
		pt.setY(1.85f);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test
	public void canSetYFromInt() {
		pt.setY(1);
		assertEquals("y is 1", (int) pt.getY(), 1);
	}
	@Test(expected = NullPointerException.class)
	public void willNotMoveFromNull() {
		pt.move(null);
	}
}
