package com.dalonedrow.rpg.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.rpg.base.flyweights.RPGException;

public class WeightedGraphEdgeTest {
	@Test
	public void canCreate() throws RPGException {
		WeightedGraphEdge g = new WeightedGraphEdge(0, 1, 1);
		assertEquals(g.getFrom(), 0);
		assertEquals(g.getTo(), 1);
		assertEquals(g.getCost(), 1, .00001f);
		WeightedGraphEdge n = new WeightedGraphEdge(g);
		assertEquals(n.getFrom(), 0);
		assertEquals(n.getTo(), 1);
		assertEquals(g.getCost(), 1, .00001f);
		assertEquals(n.toString(), "[from=0,to=1]");
		g.setCost(2.5f);
		assertEquals(g.getCost(), 2.5f, .00001f);
	}
	@Test
	public void testEquals() throws RPGException {
		WeightedGraphEdge g = new WeightedGraphEdge(0, 1, 1);
		WeightedGraphEdge n = new WeightedGraphEdge(1, 0, 1);
		WeightedGraphEdge o = new WeightedGraphEdge(12, 10, 1);
		assertTrue(g.equalsDirected(0, 1));
		assertFalse(g.equalsDirected(1, 0));
		assertFalse(g.equalsDirected(10, 12));
		assertFalse(g.equalsDirected(0, 12));
		assertTrue(g.equalsUndirected(0, 1));
		assertTrue(g.equalsUndirected(1, 0));
		assertFalse(g.equalsUndirected(1, 10));
		assertTrue(g.equalsUndirected(n));
		assertFalse(g.equalsUndirected(o));
		GraphEdge g1 = new GraphEdge(1, 2);
		GraphEdge g2 = new GraphEdge(0, 2);
		assertFalse(g.equalsUndirected(g1));
		assertFalse(g.equalsUndirected(g2));
	}
}
