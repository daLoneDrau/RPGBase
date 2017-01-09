package com.dalonedrow.rpg.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.rpg.base.flyweights.RPGException;

public class GraphNodeTest {
	@Test
	public void canCreate() throws RPGException {
		GraphNode g = new GraphNode();
		assertEquals(g.getIndex(), -1);
		g = new GraphNode(0);
		assertEquals(g.getIndex(), 0);
		g = new GraphNode("test".toCharArray(), 1);
		assertEquals(new String(g.getName()), "test");
		assertEquals(g.getIndex(), 1);
		GraphNode n = new GraphNode(g);
		assertEquals(new String(n.getName()), "test");
		assertEquals(n.getIndex(), 1);
		g = new GraphNode("ddd", 1);
		assertEquals(new String(g.getName()), "ddd");
		assertEquals(g.getIndex(), 1);
		assertFalse(g.equals(null));
		assertFalse(g.equals(new Object()));
		assertFalse(g.equals(new GraphNode(0)));
		assertTrue(g.equals(new GraphNode(1)));
		assertTrue(g.equals(g));
		g.setIndex(20);
		assertEquals(g.getIndex(), 20);
		g.setName("test3".toCharArray());
		assertEquals(new String(g.getName()), "test3");
		g.setName("test4");
		assertEquals(new String(g.getName()), "test4");
	}
	@Test(expected = RPGException.class)
	public void willNoCreate0() throws RPGException {
		GraphNode g = new GraphNode(-1);
	}
	@Test(expected = RPGException.class)
	public void willNoCreate1() throws RPGException {
		GraphNode g = new GraphNode("test".toCharArray(), -1);
	}
}
