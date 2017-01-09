package com.dalonedrow.engine.sprite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public class SimpleVector2Test {
	SimpleVector2 v2;
	@Before
	public void before() {
		v2 = new SimpleVector2();
	}
	@Test
	public void canCompareWithFloatCoordinates() {
		assertTrue("pt equals 0,0", v2.equals(0, 0));
		assertFalse("pt does not equal 1,1", v2.equals(1, 1));
	}
	@Test
	public void canCompareWithPoint() {
		assertTrue("pt equals itself", v2.equals(v2));
		SimplePoint pt1 = new SimplePoint(v2);
		assertTrue("pt equals copy of itself", v2.equals(pt1));
		assertTrue("pt equals identical vector",
				v2.equals(new SimpleVector2(0, 0)));
		assertFalse("pt does not equal random object", v2.equals(new Object()));
		assertFalse("pt does not null", v2.equals(null));
	}
	@Test
	public void canCreateDefaultOrigin() {
		assertEquals("x is 0", v2.getX(), 0, 0.0001f);
		assertEquals("y is 0", v2.getY(), 0, 0.0001f);
	}
	@Test
	public void canCreateVector2FromFloat() {
		v2 = new SimpleVector2(1.85f, 1.85f);
		assertEquals("x is 1.85", v2.getX(), 1.85, 0.0001f);
		assertEquals("y is 1.85", v2.getY(), 1.85, 0.0001f);
	}
	@Test
	public void canCreateVector2FromInt() {
		v2 = new SimpleVector2(1, 1);
		assertEquals("x is 1", v2.getX(), 1, 0.0001f);
		assertEquals("y is 1", v2.getY(), 1, 0.0001f);
	}
	@Test
	public void canCreateVector2FromVector2() throws RPGException {
		SimpleVector2 pt1 = new SimpleVector2(v2);
		assertEquals("x is 0", pt1.getX(), 0, 0.0001f);
		assertEquals("y is 0", pt1.getY(), 0, 0.0001f);
	}
	@Test
	public void canCreateVector2FromVector3() throws RPGException {
		SimpleVector2 pt1 = new SimpleVector2(
				new SimpleVector3(1.85f, 1.85f, 1.85f));
		assertEquals("x is 1.85", pt1.getX(), 1.85, 0.0001f);
		assertEquals("y is 1.85", pt1.getY(), 1.85, 0.0001f);
	}
	@Test
	public void canCreateVector2FromVectorString()
			throws RPGException, PooledException {
		SimpleVector2 pt1 = new SimpleVector2("1.85f, 1.85f");
		assertEquals("x is 1.85", pt1.getX(), 1.85, 0.0001f);
		assertEquals("y is 1.85", pt1.getY(), 1.85, 0.0001f);
	}
	@Test
	public void canDecrement()
			throws RPGException {
		v2.decrement(new SimpleVector2(1, 1));
		assertEquals("x is -1", v2.getX(), -1, 0.0001f);
		assertEquals("y is -1", v2.getY(), -1, 0.0001f);
	}
	@Test
	public void canDivide() throws RPGException {
		v2.move(5, 5);
		v2.divide(new SimpleVector2(2, 2));
		assertEquals("x is 2.5", v2.getX(), 2.5f, 0.0001f);
		assertEquals("y is 2.5", v2.getY(), 2.5f, 0.0001f);
	}
	@Test
	public void canDotProduct() throws RPGException {
		assertEquals("found dot product", 0f,
				v2.dotProduct(new SimpleVector2(1, 1)), 0.0001f);
	}
	@Test
	public void canFindDistance() throws RPGException {
		assertEquals("found distance", 1.4142135623730951f,
				v2.distance(new SimpleVector2(1, 1)), 0.0001f);
	}
	@Test
	public void canGetLength() {
		v2.move(5, 5);
		assertEquals("length is 7", v2.length(), 7, 0.08);
	}
	@Test
	public void canGetNormal() throws RPGException {
		v2.move(5, 5);
		SimpleVector2 v21 = v2.normal();
		assertEquals("x is 0.7071067811865475", v21.getX(),
				0.7071067811865475f, 0.0001);
		assertEquals("y is 0.7071067811865475", v21.getY(),
				0.7071067811865475f, 0.0001);
	}
	@Test
	public void canGetString() {
		assertEquals("got string", v2.toString(),
				"com.dalonedrow.engine.sprite.base.SimpleVector2[x=0.0, y=0.0]");
	}
	@Test
	public void canIncrement()
			throws RPGException {
		v2.increment(new SimpleVector2(1, 1));
		assertEquals("x is 1", v2.getX(), 1, 0.0001f);
		assertEquals("y is 1", v2.getY(), 1, 0.0001f);
	}
	@Test
	public void canMoveFromDouble() {
		v2.move(1.85f, 1.85f);
		assertEquals("x is 1", v2.getX(), 1.85f, 0.0001);
		assertEquals("y is 1", v2.getY(), 1.85f, 0.0001);
	}
	@Test
	public void canMultiply() throws RPGException {
		v2.move(5, 5);
		v2.multiply(new SimpleVector2(2, 2));
		assertEquals("x is 10", v2.getX(), 10f, 0.0001);
		assertEquals("y is 10", v2.getY(), 10f, 0.0001);
	}
	@Test
	public void canSetFromFloats() throws RPGException {
		v2.set(5f, 5f);
		assertEquals("x is 5", v2.getX(), 5f, 0.0001);
		assertEquals("y is 5", v2.getY(), 5f, 0.0001);
	}
	@Test
	public void canSetFromVector() throws RPGException {
		v2.set(new SimpleVector2(5f, 5f));
		assertEquals("x is 5", v2.getX(), 5f, 0.0001);
		assertEquals("y is 5", v2.getY(), 5f, 0.0001);
	}
	@Test
	public void canSetXFromFloat() {
		v2.setX(1.85f);
		assertEquals("x is 1", (int) v2.getX(), 1);
	}
	@Test
	public void canSetYFromFloat() {
		v2.setY(1.85f);
		assertEquals("y is 1", (int) v2.getY(), 1);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateVector2FromNullString()
			throws RPGException, PooledException {
		SimpleVector2 pt1 = new SimpleVector2((String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateVector2FromNullVector3() throws RPGException {
		SimpleVector2 pt1 = new SimpleVector2((SimpleVector3) null);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateVector2FromStringNot2Parameters()
			throws RPGException, PooledException {
		SimpleVector2 pt1 = new SimpleVector2("1");
	}
	@Test(expected = RPGException.class)
	public void willNotDecrementFromNull() throws RPGException {
		v2.decrement(null);
	}
	@Test(expected = RPGException.class)
	public void willNotDistanceFromNull() throws RPGException {
		v2.distance(null);
	}
	@Test(expected = RPGException.class)
	public void willNotDivideFromNull() throws RPGException {
		v2.divide(null);
	}
	@Test(expected = RPGException.class)
	public void willNotDotProductFromNull() throws RPGException {
		v2.dotProduct(null);
	}
	@Test(expected = RPGException.class)
	public void willNotIncrementFromNull() throws RPGException {
		v2.increment(null);
	}
	@Test(expected = RPGException.class)
	public void willNotMultiplyFromNull() throws RPGException {
		v2.multiply(null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetFromNull() throws RPGException {
		v2.set(null);
	}
}
