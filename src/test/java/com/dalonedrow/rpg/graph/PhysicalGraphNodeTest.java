package com.dalonedrow.rpg.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public class PhysicalGraphNodeTest {
	@Test
	public void canCreate() throws RPGException {
		PhysicalGraphNode g = new PhysicalGraphNode(0, 0, 0);
		SimplePoint pt = new SimplePoint(2, 5);
		assertNotNull(g);
		assertTrue(g.getLocation().equals(0, 0));
		assertTrue(g.equals(new SimplePoint()));
		assertFalse(g.equals(2, 5));
		assertFalse(g.equals(pt));
		g = new PhysicalGraphNode(1, pt);
		assertTrue(g.getLocation().equals(2, 5));
		assertTrue(g.equals(pt));
		assertFalse(g.equals(0, 0));
		assertFalse(g.equals(new SimplePoint()));
        
        g = new PhysicalGraphNode("test", 2, pt);
        assertTrue(g.getLocation().equals(2, 5));
        assertTrue(g.equals(pt));
        assertEquals("test", new String(g.getName()));
        assertFalse(g.equals(0, 0));
        assertFalse(g.equals(new SimplePoint()));
        
        g = new PhysicalGraphNode(new char[] { 't', 'e', 's', 't' }, 3, pt);
        assertTrue(g.getLocation().equals(2, 5));
        assertTrue(g.equals(pt));
        assertEquals("test", new String(g.getName()));
        assertFalse(g.equals(0, 0));
        assertFalse(g.equals(new SimplePoint()));
        
        g = new PhysicalGraphNode("test", 4, 2, 5);
        assertTrue(g.getLocation().equals(2, 5));
        assertTrue(g.equals(pt));
        assertEquals("test", new String(g.getName()));
        assertFalse(g.equals(0, 0));
        assertFalse(g.equals(new SimplePoint()));
        
        g = new PhysicalGraphNode(new char[] { 't', 'e', 's', 't' }, 5, 2, 5);
        assertTrue(g.getLocation().equals(2, 5));
        assertTrue(g.equals(pt));
        assertEquals("test", new String(g.getName()));
        assertFalse(g.equals(0, 0));
        assertFalse(g.equals(new SimplePoint()));
	}
}
