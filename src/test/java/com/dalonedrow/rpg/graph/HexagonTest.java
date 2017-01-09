package com.dalonedrow.rpg.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.engine.sprite.base.SimpleVector3;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public class HexagonTest {
	@Test
	public void canCreateHexagon() {
		Hexagon hex = new Hexagon(true, 0, -1);
		assertNotNull(hex);
		assertEquals(hex.getSize(), 0, .000001f);
		hex = new Hexagon(true, 0, 10);
		assertEquals(hex.getSize(), 10, .000001f);
		hex = new Hexagon(true, 0);
		assertEquals(hex.getSize(), 0, .000001f);
		hex = new Hexagon(1);
		assertTrue(hex.isFlat());
		hex = new Hexagon(1, 5);
		assertEquals(hex.getSize(), 5, .000001f);
		assertEquals(hex.getHeight(), 8.6602545f, .00001f);
		assertEquals(hex.getHorizontalDistance(), 7.5f, .00001f);
		assertEquals(hex.getVerticalDistance(), 8.6602545f, .00001f);
		assertEquals(hex.getId(), 1);
		assertEquals(hex.getWidth(), 10, .000001f);
	}
	@Test
	public void canCopy() {
		Hexagon hex0 = new Hexagon(true, 0, 5);
		Hexagon hex1 = new Hexagon(true, 1, 7);
		assertEquals(hex0.getSize(), 5, .000001f);
		hex0.copyOf(hex1);
		assertEquals(hex0.getSize(), 7, .000001f);
	}
	@Test
	public void canSetCoordinates() {
		Hexagon hex = new Hexagon(true, 0, 5);
		hex.setCoordinates(new SimpleVector3(0, 1, 2));
		assertEquals(hex.getX(), 0, .000001f);
		assertEquals(hex.getY(), 1, .000001f);
		assertEquals(hex.getZ(), 2, .000001f);
		hex.setCoordinates(4, 5, 6);
		assertEquals(hex.getX(), 4, .000001f);
		assertEquals(hex.getY(), 5, .000001f);
		assertEquals(hex.getZ(), 6, .000001f);
		assertEquals(hex.getVector().getZ(), 6, .000001f);
	}
	@Test
	public void equals() {
		Hexagon hex0 = new Hexagon(true, 0, 5);
		Hexagon hex1 = new Hexagon(true, 1, 5);
		hex0.setCoordinates(1, 1, 1);
		hex1.setCoordinates(1, 1, 1);
		assertTrue(hex0.equals(hex1));
		hex1.setCoordinates(0, 0, 0);
		assertFalse(hex0.equals(hex1));
		assertFalse(hex0.equals(new Object()));
		assertTrue(hex0.equals(1, 1, 1));
		assertFalse(hex0.equals(0, 0, 0));		
	}
	@Test
	public void canGetVertexPosition() throws RPGException {
		Hexagon hex = new Hexagon(true, 0, 5);
		SimplePoint pt = hex.getHexCornerVertex(new SimplePoint(0, 0), 0);
		assertEquals(pt.getX(), 5, .00001f);
		assertEquals(pt.getY(), 0, .00001f);
		hex = new Hexagon(false, 0, 5);
		pt = hex.getHexCornerVertex(new SimplePoint(0, 0), 0);
		assertEquals(pt.getX(), 4, .00001f);
		assertEquals(pt.getY(), 2, .00001f);
		pt = hex.getHexCornerVertex(new SimplePoint(0, 0), 8, 0);
		assertEquals(pt.getX(), 6, .00001f);
		assertEquals(pt.getY(), 4, .00001f);
	}
	@Test(expected = RPGException.class)
	public void willNotGetVertexPosition() throws RPGException {
		Hexagon hex = new Hexagon(true, 0, 0);
		hex.getHexCornerVertex(new SimplePoint(0, 0), 0);
	}
	@Test
	public void canRotate() throws RPGException {
		Hexagon hex = new Hexagon(true, 0, 5);
		hex.setCoordinates(1, 1, 1);
		hex.rotate();
		assertEquals(hex.getX(), -1);
		assertEquals(hex.getY(), -1);
		assertEquals(hex.getZ(), -1);
	}
}
