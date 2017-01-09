package com.dalonedrow.engine.sprite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public class SimpleVector3Test {
	SimpleVector3 v3;
	@Before
	public void before() {
		v3 = new SimpleVector3();
	}
	@Test
	public void canCompareVector() {
		assertTrue("pt equals pt", v3.equals(v3));
		assertTrue("pt equals 0,0,0", v3.equals(new SimpleVector3()));
		assertTrue("pt equals 0,0,0", v3.equals(0, 0, 0));
		SimpleVector3 vv = new SimpleVector3(2, 2, 2);
		assertFalse("pt does not equal 2,2,2,", v3.equals(vv));
		assertFalse("pt does not equal null", v3.equals(null));
		assertFalse("pt does not equal non-vector", v3.equals(""));
	}
	@Test
	public void canCreateDefaultOrigin() {
		assertEquals("x is 0", v3.getX(), 0, 0.0001f);
		assertEquals("y is 0", v3.getY(), 0, 0.0001f);
		assertEquals("z is 0", v3.getZ(), 0, 0.0001f);
	}
	@Test
	public void canCreateVector3FromFloat() {
		v3 = new SimpleVector3(1.85f, 1.85f, 1.85f);
		assertEquals("x is 1.85", v3.getX(), 1.85, 0.0001f);
		assertEquals("y is 1.85", v3.getY(), 1.85, 0.0001f);
		assertEquals("z is 1.85", v3.getZ(), 1.85, 0.0001f);
	}
	@Test
	public void canCreateVector3FromInt() {
		v3 = new SimpleVector3(1, 1, 1);
		assertEquals("x is 1", v3.getX(), 1, 0.0001f);
		assertEquals("y is 1", v3.getY(), 1, 0.0001f);
		assertEquals("z is 1", v3.getZ(), 1, 0.0001f);
	}
	@Test
	public void canCreateVector3FromVector2() throws RPGException {
		SimpleVector3 pt1 = new SimpleVector3(new SimpleVector2(1, 1));
		assertEquals("x is 1", pt1.getX(), 1, 0.0001f);
		assertEquals("y is 1", pt1.getY(), 1, 0.0001f);
		assertEquals("z is 0", pt1.getZ(), 0, 0.0001f);
		pt1 = new SimpleVector3(new SimpleVector2(1, 1), 1);
		assertEquals("x is 1", pt1.getX(), 1, 0.0001f);
		assertEquals("y is 1", pt1.getY(), 1, 0.0001f);
		assertEquals("z is 1", pt1.getZ(), 1, 0.0001f);
		pt1 = new SimpleVector3(new SimpleVector2(1, 1), 2f);
		assertEquals("x is 1", pt1.getX(), 1, 0.0001f);
		assertEquals("y is 1", pt1.getY(), 1, 0.0001f);
		assertEquals("z is 1", pt1.getZ(), 2, 0.0001f);
	}
	@Test
	public void canCreateVector3FromVector3() throws RPGException {
		SimpleVector3 pt1 = new SimpleVector3(
				new SimpleVector3(1.85f, 1.85f, 1.85f));
		assertEquals("x is 1.85", pt1.getX(), 1.85, 0.0001f);
		assertEquals("y is 1.85", pt1.getY(), 1.85, 0.0001f);
		assertEquals("z is 1.85", pt1.getZ(), 1.85, 0.0001f);
	}
	@Test
	public void canCreateVector3FromVectorString() throws Exception {
		SimpleVector3 pt1 = new SimpleVector3("1.85f, 1.85f, 1.85f");
		assertEquals("x is 1.85", pt1.getX(), 1.85, 0.0001f);
		assertEquals("y is 1.85", pt1.getY(), 1.85, 0.0001f);
		assertEquals("z is 1.85", pt1.getZ(), 1.85, 0.0001f);
	}
	@Test
	public void canCrossProduct()
			throws RPGException {
		v3.crossProduct(new SimpleVector3(1, 1, 1));
		assertEquals("x is 0", v3.getX(), 0, 0.0001f);
		assertEquals("y is -1", v3.getY(), 0, 0.0001f);
		assertEquals("z is -1", v3.getZ(), 0, 0.0001f);
	}
	@Test
	public void canDecrement()
			throws RPGException {
		v3.decrement(new SimpleVector3(1, 1, 1));
		assertEquals("x is -1", v3.getX(), -1, 0.0001f);
		assertEquals("y is -1", v3.getY(), -1, 0.0001f);
		assertEquals("z is -1", v3.getZ(), -1, 0.0001f);
	}
	@Test
	public void canDivide() throws RPGException {
		v3.move(5, 5, 5);
		v3.divide(new SimpleVector3(2, 2, 2));
		assertEquals("x is 2.5", v3.getX(), 2.5f, 0.0001f);
		assertEquals("y is 2.5", v3.getY(), 2.5f, 0.0001f);
		assertEquals("z is 2.5", v3.getZ(), 2.5f, 0.0001f);
	}
	@Test
	public void canDotProduct() throws RPGException {
		assertEquals("found dot product", 0f,
				v3.dotProduct(new SimpleVector3(1, 1, 1)), 0.0001f);
	}
	@Test
	public void canFindDistance() throws RPGException {
		assertEquals("found distance", 1.4142135623730951f,
				v3.distance(new SimpleVector3(1, 1, 1)), 0.0001f);
	}
	@Test
	public void canGetLength() {
		v3.move(5, 5, 5);
		assertEquals("length is 7", v3.length(), 8.6, 0.08);
	}
	@Test
	public void canGetNormal() throws RPGException {
		v3.move(5, 5, 5);
		SimpleVector3 v21 = v3.normal();
		assertEquals("x is 0.7071067811865475", v21.getX(),
				0.57735f, 0.0001);
		assertEquals("y is 0.7071067811865475", v21.getY(),
				0.57735f, 0.0001);
	}
	@Test
	public void canGetString() {
		assertEquals("got string", v3.toString(),
				"[x=0.0, y=0.0, z=0.0]");
	}
	@Test
	public void canIncrement()
			throws RPGException {
		v3.increment(new SimpleVector3(1, 1, 1));
		assertEquals("x is 1", v3.getX(), 1, 0.0001f);
		assertEquals("y is 1", v3.getY(), 1, 0.0001f);
	}
	@Test
	public void canMoveFromDouble() {
		v3.move(1.85f, 1.85f, 1.85f);
		assertEquals("x is 1", v3.getX(), 1.85f, 0.0001);
		assertEquals("y is 1", v3.getY(), 1.85f, 0.0001);
	}
	@Test
	public void canMultiply() throws RPGException {
		v3.move(5, 5, 5);
		v3.multiply(new SimpleVector3(2, 2, 2));
		assertEquals("x is 10", v3.getX(), 10f, 0.0001);
		assertEquals("y is 10", v3.getY(), 10f, 0.0001);
	}
	@Test
	public void canSetFromFloats() throws RPGException {
		v3.set(5f, 5f, 5f);
		assertEquals("x is 5", v3.getX(), 5f, 0.0001);
		assertEquals("y is 5", v3.getY(), 5f, 0.0001);
	}
	@Test
	public void canSetFromVector() throws RPGException {
		v3.set(new SimpleVector3(5f, 5f, 5f));
		assertEquals("x is 5", v3.getX(), 5f, 0.0001);
		assertEquals("y is 5", v3.getY(), 5f, 0.0001);
	}
	@Test
	public void canSetXFromFloat() {
		v3.setX(1.85f);
		assertEquals("x is 1", (int) v3.getX(), 1);
	}
	@Test
	public void canSetYFromFloat() {
		v3.setY(1.85f);
		assertEquals("y is 1", (int) v3.getY(), 1);
	}
	@Test
	public void canSetZFromFloat() {
		v3.setZ(1.85f);
		assertEquals("z is 1", (int) v3.getZ(), 1);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateVector3FromNullString()
			throws RPGException, PooledException {
		SimpleVector3 pt1 = new SimpleVector3((String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateVector3FromNullVector3() throws RPGException {
		SimpleVector3 pt1 = new SimpleVector3((SimpleVector3) null);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateVector3FromStringNot3Parameters()
			throws RPGException, PooledException {
		SimpleVector3 pt1 = new SimpleVector3("1");
	}
	@Test(expected = RPGException.class)
	public void willNotDecrementFromNull() throws RPGException {
		v3.decrement(null);
	}
	@Test(expected = RPGException.class)
	public void willNotDistanceFromNull() throws RPGException {
		v3.distance(null);
	}
	@Test(expected = RPGException.class)
	public void willNotDivideFromNull() throws RPGException {
		v3.divide(null);
	}
	@Test(expected = RPGException.class)
	public void willNotDotProductFromNull() throws RPGException {
		v3.dotProduct(null);
	}
	@Test(expected = RPGException.class)
	public void willNotIncrementFromNull() throws RPGException {
		v3.increment(null);
	}
	@Test(expected = RPGException.class)
	public void willNotMultiplyFromNull() throws RPGException {
		v3.multiply(null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetFromNull() throws RPGException {
		v3.set(null);
	}
}
