package com.dalonedrow.pooled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ListPoolTest {
	@Test
	public void canGetDifferentListInstances() {
		PooledList sb0 = ListPool.getInstance().getList();
		PooledList sb1 = ListPool.getInstance().getList();
		sb1 = ListPool.getInstance().getList();
		sb1 = ListPool.getInstance().getList();
		sb1 = ListPool.getInstance().getList();
		sb1 = ListPool.getInstance().getList();
		assertNotEquals("Have 2 different string builders",
				sb0.getPoolIndex(), sb1.getPoolIndex());
	}
	@Test
	public void onlyOneInstanceExists() {
		ListPool pool0 = ListPool.getInstance();
		ListPool pool1 = ListPool.getInstance();
		assertEquals("Same object", pool0, pool1);
	}
	@Test
	public void stringBuildersAreLocked() {
		PooledList sb0 = ListPool.getInstance().getList();
		assertTrue("This should be locked",
				ListPool.getInstance().isItemLocked(sb0));
	}
	@Test
	public void stringBuildersGetUnlockedOnReturn() {
		PooledList sb0 = ListPool.getInstance().getList();
		assertTrue("This should be locked",
				ListPool.getInstance().isItemLocked(sb0));
		ListPool.getInstance().returnObject(sb0);
		assertFalse("This should not be locked",
				ListPool.getInstance().isItemLocked(sb0));
	}
	@Test(expected = PooledException.class)
	public void willNotAddIndexWithoutLock() throws PooledException {
		PooledList sb0 = ListPool.getInstance().getList();
		sb0.returnToPool();
		sb0.add(0, "");
	}
	@Test(expected = PooledException.class)
	public void willNotAddWithoutLock() throws PooledException {
		PooledList sb0 = ListPool.getInstance().getList();
		sb0.returnToPool();
		sb0.add("");
	}
}
