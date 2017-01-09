package com.dalonedrow.pooled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public final class PooledListTest {
	@Test
	public void canAdd() throws PooledException {
		PooledList sb = ListPool.getInstance().getList();
		sb.init();
		assertNull(sb.get(0));
		sb = ListPool.getInstance().getList();
		sb.add(0, 't');
		sb.add(0, 'g');
		assertEquals((char) sb.get(0), 'g');
		sb = ListPool.getInstance().getList();
		sb.add('t');
		sb.add('g');
		assertEquals((char) sb.get(0), 't');
	}
	@Test(expected = PooledException.class)
	public void willNotGetFromUnlocked() throws PooledException {
		PooledList sb = ListPool.getInstance().getList();
		sb.returnToPool();
		sb.get(0);
	}
}
