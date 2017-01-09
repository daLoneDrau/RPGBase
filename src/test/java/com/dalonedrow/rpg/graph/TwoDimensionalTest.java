package com.dalonedrow.rpg.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dalonedrow.engine.sprite.base.SimplePoint;
import com.dalonedrow.engine.sprite.base.SimpleVector2;

public class TwoDimensionalTest {
	@Test
	public void canConvert() {
		TwoDimensional td = new TwoDimensional() { };
		final double x = 1, y = 5;
		final SimplePoint pt = new SimplePoint(3, 19);
		final SimpleVector2 v2 = new SimpleVector2(12, 35);
		int i = td.convertPointToInt(x, y);
		assertTrue(td.convertIntToPoint(i).equals((int) x, (int) y));
		assertFalse(td.convertIntToPoint(i).equals(1, 6));
		i = td.convertPointToInt((float) x, (float) y);
		assertTrue(td.convertIntToPoint(i).equals((int) x, (int) y));
		i = td.convertPointToInt(pt);
		assertTrue(td.convertIntToPoint(i).equals(pt));
		i = td.convertPointToInt(v2);
		assertTrue(td.convertIntToVector(i).equals(v2));
		for (int yi = 21, lenY = 23; yi <= lenY; yi++) {
			for (int xi = 1, lenX = 20; xi <= lenX; xi++) {
				System.out.println("  NULL, " + xi + ", " + yi + ", " 
			+ td.convertPointToInt(xi, yi) 
			+ ", (SELECT hex_type_id FROM dwarfstar_barbarian_prince.hex_type WHERE name = 'Countryside')), (");
			}
		}
	}
}
