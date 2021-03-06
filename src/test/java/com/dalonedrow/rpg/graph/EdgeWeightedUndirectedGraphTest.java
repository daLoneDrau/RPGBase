package com.dalonedrow.rpg.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dalonedrow.rpg.base.flyweights.RPGException;

public class EdgeWeightedUndirectedGraphTest {
	@Test
	public void canCreate() throws RPGException {
		// new graph of 10 vertices
		EdgeWeightedUndirectedGraph g = new EdgeWeightedUndirectedGraph(10);
		assertNotNull(g);
		assertEquals(g.getNumberOfVertices(), 10);
		EdgeWeightedUndirectedGraph g2 = new EdgeWeightedUndirectedGraph(g);
		assertEquals(g2.getNumberOfVertices(), 10);
	}
	@Test
	public void canAddEdges() throws RPGException {
		// new graph of 10 vertices
		EdgeWeightedUndirectedGraph g = new EdgeWeightedUndirectedGraph(10);
		assertEquals(g.getNumberOfEdges(), 0);
		g.addEdge(0, 1, 1);
		assertEquals(g.getNumberOfEdges(), 1);
		g.addEdge(1, 0, 2);
		assertEquals(g.getNumberOfEdges(), 1);
		g.addEdge(new WeightedGraphEdge(1, 0, 2));
		assertEquals(g.getNumberOfEdges(), 1);
		int[] adjacencies = g.getAdjacencies(0);
		assertEquals(adjacencies.length, 1);
		assertEquals(adjacencies[0], 1);
		g.addEdge(2, 3, 1);
		adjacencies = g.getAdjacencies(2);
		assertEquals(adjacencies.length, 1);
		assertEquals(adjacencies[0], 3);
		g.addEdge(20, 4, 1);
		adjacencies = g.getAdjacencies(4);
		assertEquals(adjacencies.length, 1);
		assertEquals(adjacencies[0], 20);
		g.addEdge(21, 5, 1);
		adjacencies = g.getAdjacencies(5);
		assertEquals(adjacencies.length, 1);
		assertEquals(adjacencies[0], 21);
		g.addEdge(new WeightedGraphEdge(6, 7, 1));
		adjacencies = g.getAdjacencies(6);
		assertEquals(adjacencies.length, 1);
		assertEquals(adjacencies[0], 7);
		assertTrue(
				new WeightedGraphEdge(6, 7, 1).equalsUndirected(g.getEdge(4)));
		WeightedGraphEdge[] adjs = g.getVertexAdjacencies(5);
		assertEquals(adjs.length, 1);
	}
	@Test
	public void canRemoveEdges() throws RPGException {
		// new graph of 10 vertices
		EdgeWeightedUndirectedGraph g = new EdgeWeightedUndirectedGraph(10);
		assertFalse(g.removeEdge(0, 1));
		g.addEdge(0, 1, 1);
		g.addEdge(1, 2, 1);
		assertTrue(g.removeEdge(0, 1));
		assertTrue(g.removeEdge(2, 1));
	}
	@Test
	public void canAddVertices() throws RPGException {
		// new graph of 10 vertices
		EdgeWeightedUndirectedGraph g = new EdgeWeightedUndirectedGraph(10);
		g.addVertex(0);
		assertNotNull(g.getVertex(0));
		assertNull(g.getVertex(10));
		g.addVertex(10);
		assertNotNull(g.getVertex(10));
		assertNull(g.getVertex(11));
		g.addVertex(new GraphNode(11));
		assertNotNull(g.getVertex(11));
	}
}
