package com.dalonedrow.rpg.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.engine.sprite.base.SimpleVector3;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public class HexCoordinateSystemTest {
	@Test
	public void canDoCoordsOddR() throws RPGException {
		// pointy-topped, with odd numbers out to right
		HexCoordinateSystem coords = new HexCoordinateSystem(
				HexCoordinateSystem.ODD_R);
		// new hex at 0,0,0
		Hexagon hexagon = new Hexagon(false, 0);
		// axial coords are 0,0
		assertTrue(coords.getAxialCoordinates(hexagon).equals(0, 0));
		// change hex to 1,0,-1
		hexagon.setCoordinates(1, 0, -1);
		// axial coords are 0,-1
		assertTrue(coords.getAxialCoordinates(hexagon).equals(0, -1));
		assertTrue(coords.getCubeCoordinates(0, -1).equals(1, 0, -1));
		// change hex to 0,0,0
		hexagon.setCoordinates(new SimpleVector3(0, 0, 0));
		// neighbor to north should be 0,-1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(1, 0, -1));
		// change hex to 1,-3,2
		hexagon.setCoordinates(new SimpleVector3(1, -3, 2));
		// axial coords are 2,2
		assertTrue(coords.getAxialCoordinates(hexagon).equals(2, 2));
		// neighbor to north should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(2, 1)));
		
		// change hex to 1,1
		hexagon.setCoordinates(coords.getCubeCoordinates(1, 1));
		// axial coords are 1,1
		assertTrue(coords.getAxialCoordinates(hexagon).equals(1,1));
		// neighbor to n should be 2,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(2, 0)));
		// neighbor to nne should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to sse should be 2,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(2, 2)));
		// neighbor to s should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to ssw should be 0,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(0, 1)));
		// neighbor to nnw should be 1,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(1, 0)));
		
		// change hex to 2,2
		hexagon.setCoordinates(coords.getCubeCoordinates(2, 2));
		// axial coords are 2, 2
		assertTrue(coords.getAxialCoordinates(hexagon).equals(2, 2));
		// neighbor to n should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to nne should be 3,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(3, 2)));
		// neighbor to sse should be 2,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(2, 3)));
		// neighbor to s should be 1,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(1, 3)));
		// neighbor to ssw should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to nnw should be 1,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(1, 1)));
		
		// change hex to 6,0
		hexagon.setCoordinates(coords.getCubeCoordinates(6, 0));
		// axial coords are 6,0
		assertTrue(coords.getAxialCoordinates(hexagon).equals(6, 0));
		// neighbor to sse should be 6,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(6, 1)));
		
		// change hex to 5,2
		hexagon.setCoordinates(coords.getCubeCoordinates(5, 2));
		// axial coords are 5, 2
		assertTrue(coords.getAxialCoordinates(hexagon).equals(5, 2));
		// neighbor to s should be 4,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(4, 3)));
		
		// change hex to 1,1
		hexagon.setCoordinates(coords.getCubeCoordinates(1, 1));
		// axial coords are 1,1
		assertTrue(coords.getAxialCoordinates(hexagon).equals(1, 1));
		// neighbor to ssw should be 0,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(0, 1)));
		
		// change hex to 3, 6
		hexagon.setCoordinates(coords.getCubeCoordinates(3, 6));
		// axial coords are 3, 6
		assertTrue(coords.getAxialCoordinates(hexagon).equals(3, 6));
		// neighbor to nnw should be 2,5
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(2, 5)));
	}
	@Test
	public void canDoCoordsEvenR() throws RPGException {
		// pointy-topped, with even numbers out to right
		HexCoordinateSystem coords = new HexCoordinateSystem(
				HexCoordinateSystem.EVEN_R);
		// new hex at 0,0,0
		Hexagon hexagon = new Hexagon(false, 0);
		
		// change hex to 1,1
		hexagon.setCoordinates(coords.getCubeCoordinates(1, 1));
		// axial coords are 1,1
		assertTrue(coords.getAxialCoordinates(hexagon).equals(1, 1));
		// neighbor to n should be 1,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(1, 0)));
		// neighbor to nne should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to sse should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to s should be 0,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(0, 2)));
		// neighbor to ssw should be 0,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(0, 1)));
		// neighbor to nnw should be 0,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(0, 0)));
		
		// change hex to 2,2
		hexagon.setCoordinates(coords.getCubeCoordinates(2, 2));
		// axial coords are 2, 2
		assertTrue(coords.getAxialCoordinates(hexagon).equals(2, 2));
		// neighbor to n should be 3,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(3, 1)));
		// neighbor to nne should be 3,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(3, 2)));
		// neighbor to sse should be 3,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(3, 3)));
		// neighbor to s should be 2,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(2, 3)));
		// neighbor to ssw should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to nnw should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(2, 1)));
	}
	@Test
	public void canDoCoordsOddQ() throws RPGException {
		// pointy-topped, with even numbers out to right
		HexCoordinateSystem coords = new HexCoordinateSystem(
				HexCoordinateSystem.ODD_Q);
		// new hex at 0,0,0
		Hexagon hexagon = new Hexagon(false, 0);
		
		// change hex to 1,1
		hexagon.setCoordinates(coords.getCubeCoordinates(1, 1));
		// axial coords are 1,1
		assertTrue(coords.getAxialCoordinates(hexagon).equals(1, 1));
		// neighbor to n should be 1,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(1, 0)));
		// neighbor to nne should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to sse should be 2,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(2, 2)));
		// neighbor to s should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to ssw should be 0,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(0, 2)));
		// neighbor to nnw should be 0,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(0, 1)));
		
		// change hex to 2,2
		hexagon.setCoordinates(coords.getCubeCoordinates(2, 2));
		// axial coords are 2, 2
		assertTrue(coords.getAxialCoordinates(hexagon).equals(2, 2));
		// neighbor to n should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to nne should be 3,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(3, 1)));
		// neighbor to sse should be 3,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(3, 2)));
		// neighbor to s should be 2,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(2, 3)));
		// neighbor to ssw should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to nnw should be 1,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(1, 1)));
	}
	@Test
	public void canDoCoordsEvenQ() throws RPGException {
		// pointy-topped, with even numbers out to right
		HexCoordinateSystem coords = new HexCoordinateSystem(
				HexCoordinateSystem.EVEN_Q);
		// new hex at 0,0,0
		Hexagon hexagon = new Hexagon(false, 0);
		
		// change hex to 1,1
		hexagon.setCoordinates(coords.getCubeCoordinates(1, 1));
		// axial coords are 1,1
		assertTrue(coords.getAxialCoordinates(hexagon).equals(1, 1));
		// neighbor to n should be 1,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(1, 0)));
		// neighbor to nne should be 20
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(2, 0)));
		// neighbor to sse should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to s should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(1, 2)));
		// neighbor to ssw should be 0,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(0, 1)));
		// neighbor to nnw should be 0,0
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(0, 0)));
		
		// change hex to 2,2
		hexagon.setCoordinates(coords.getCubeCoordinates(2, 2));
		// axial coords are 2, 2
		assertTrue(coords.getAxialCoordinates(hexagon).equals(2, 2));
		// neighbor to n should be 2,1
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_N).equals(
						coords.getCubeCoordinates(2, 1)));
		// neighbor to nne should be 3,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNE).equals(
						coords.getCubeCoordinates(3, 2)));
		// neighbor to sse should be 3,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSE).equals(
						coords.getCubeCoordinates(3, 3)));
		// neighbor to s should be 2,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_S).equals(
						coords.getCubeCoordinates(2, 3)));
		// neighbor to ssw should be 1,3
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_SSW).equals(
						coords.getCubeCoordinates(1, 3)));
		// neighbor to nnw should be 1,2
		assertTrue(coords.getNeighborCoordinates(
				hexagon, HexCoordinateSystem.DIRECTION_NNW).equals(
						coords.getCubeCoordinates(1, 2)));
	}
	@Test
	public void canCreate() throws RPGException {
		HexCoordinateSystem coords = new HexCoordinateSystem(3);
		assertEquals(coords.getOffsetConfiguration(), 3);
		Hexagon hex = new Hexagon(0);
		coords.addHexagon(hex);
		coords.addHexagon(hex);
		coords.addHexagon(0, 0);
		coords.addHexagon(0, 1);
		hex = coords.getHexagon(0, 0);
		assertEquals(hex.getId(), 0);
		hex = coords.getHexagon(0, 1);
		assertEquals(hex.getId(), 1);
		coords = new HexCoordinateSystem(3);
		coords.addHexagon(0, 0);
		hex = coords.getHexagon(0, 0);
		hex = coords.getHexagon(new SimpleVector3(0, 0, 0));
		assertEquals(hex.getId(), 0);
		coords.addHexagon(0, 1);
		hex = coords.getHexagon(0, 1);
		assertEquals(hex.getId(), 1);
		assertEquals(coords.distance(
				coords.getHexagon(0, 0), coords.getHexagon(0, 1)), 1);
		assertEquals(coords.distance(
				new SimpleVector3(0, 0, 0), new SimpleVector3(1, -2, 1)), 2);
		assertEquals(coords.getAxialCoordinates(coords.getHexagon(0, 0)),
				new SimplePoint(0, 0));
	}
	@Test
	public void testSharedEdges() throws RPGException {
		HexCoordinateSystem coords = 
				new HexCoordinateSystem(HexCoordinateSystem.ODD_Q);
		coords.addHexagon(0, 0);
		coords.addHexagon(1, 0);
		coords.addHexagon(1, 1);
		assertEquals(coords.getSharedEdge(
				coords.getHexagon(0, 0), coords.getHexagon(1, 0)), 2);
		coords = new HexCoordinateSystem(HexCoordinateSystem.EVEN_Q);
		coords.addHexagon(0, 0);
		coords.addHexagon(1, 0);
		coords.addHexagon(1, 1);
		assertNotEquals(coords.getSharedEdge(
				coords.getHexagon(0, 0), coords.getHexagon(1, 0)), 2);
		assertEquals(coords.getSharedEdge(
				coords.getHexagon(0, 0), coords.getHexagon(1, 0)), 1);
	}
	@Test(expected = RPGException.class)
	public void willNotShareEdges() throws RPGException {
		HexCoordinateSystem coords = new HexCoordinateSystem(
				HexCoordinateSystem.ODD_Q);
		coords.addHexagon(0, 0);
		coords.addHexagon(1, 0);
		coords.addHexagon(1, 1);
		assertEquals(coords.getSharedEdge(
				coords.getHexagon(0, 0), coords.getHexagon(1, 1)), 2);
	}
	@Test
	public void canGetHexagon() throws RPGException {
		HexCoordinateSystem coords = 
				new HexCoordinateSystem(HexCoordinateSystem.EVEN_Q);
		coords.addHexagon(0, 0);
		Hexagon hex = coords.getHexagon(0, 0);
		assertNull(coords.getHexagon(0, 1, -1));
		assertNotNull(coords.getHexagon(0, 0, 0));
	}
	@Test
	public void canGetCubeCoordinates() throws RPGException {
		HexCoordinateSystem coords = 
				new HexCoordinateSystem(HexCoordinateSystem.EVEN_Q);
		coords.addHexagon(0, 0);
		assertEquals(coords.getCubeCoordinates(coords.getHexagon(0, 0)),
				new SimpleVector3(0, 0, 0));
		assertEquals(coords.getCubeCoordinates(1, 1),
				new SimpleVector3(1, -1, 0));
		coords = new HexCoordinateSystem(2);
		assertEquals(coords.getCubeCoordinates(1, 1),
				new SimpleVector3(1, -2, 1));
		coords = new HexCoordinateSystem(1);
		assertEquals(coords.getCubeCoordinates(1, 1),
				new SimpleVector3(0, -1, 1));
		coords = new HexCoordinateSystem(0);
		assertEquals(coords.getCubeCoordinates(1, 1),
				new SimpleVector3(1, -2, 1));		
	}
	@Test(expected = RPGException.class)
	public void willNotWorkWithInvalidConfig() throws RPGException {
		HexCoordinateSystem coords = new HexCoordinateSystem(4);
		assertEquals(coords.getCubeCoordinates(0, 0),
				new SimpleVector3(0, 0, 0));		
	}
	@Test
	public void canGetNeighborCoordinates() throws RPGException {
		HexCoordinateSystem coords = 
				new HexCoordinateSystem(HexCoordinateSystem.EVEN_R);
		// create new hex at 0,0(0,0,0)
		Hexagon hex = new Hexagon(0);
		assertTrue(coords.getAxialCoordinates(hex).equals(0, 0));
		assertTrue(coords.getCubeCoordinates(hex).equals(0, 0, 0));
		assertEquals(coords.getNeighborCoordinates(hex, 
				HexCoordinateSystem.DIRECTION_N), 
				coords.getCubeCoordinates(1, -1));
		assertEquals(coords.getNeighborCoordinates(
				new SimpleVector3(0, 0, 0), 0), 
				coords.getCubeCoordinates(1, -1));
		assertEquals(coords.getNeighborCoordinates(
				new SimpleVector3(0, 0, 0),
				HexCoordinateSystem.DIRECTION_NNE), 
				coords.getCubeCoordinates(1, 0));
	}
	@Test
	public void canPrintView() throws RPGException, PooledException {
		HexCoordinateSystem coords = new HexCoordinateSystem(2);
		coords.addHexagon(0, 0);
		coords.addHexagon(1, 1);
		System.out.println(
				coords.printCubeCoordinatesView(coords.getHexagon(0, 0)));
		System.out.println(
				coords.printCubeCoordinatesView(coords.getHexagon(1, 1)));
		System.out.println(
				coords.printGrid(coords.getHexagon(0, 0)));
		System.out.println(
				coords.printGrid(coords.getHexagon(1, 1)));
	}
}
