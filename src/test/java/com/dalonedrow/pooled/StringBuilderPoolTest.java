package com.dalonedrow.pooled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringBuilderPoolTest {
	@Test
	public void canGetDifferentStringBuilderInstances() {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		PooledStringBuilder sb1 =
				StringBuilderPool.getInstance().getStringBuilder();
		assertNotEquals("Have 2 different string builders",
				sb0.getPoolIndex(), sb1.getPoolIndex());
	}
	@Test
	public void onlyOneInstanceExists() {
		StringBuilderPool pool0 = StringBuilderPool.getInstance();
		StringBuilderPool pool1 = StringBuilderPool.getInstance();
		assertEquals("Same object", pool0, pool1);
	}
	@Test
	public void stringBuildersAreLocked() {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		assertTrue("This should be locked",
				StringBuilderPool.getInstance().isItemLocked(sb0));
	}
	@Test
	public void stringBuildersGetUnlockedOnReturn() {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		assertTrue("This should be locked",
				StringBuilderPool.getInstance().isItemLocked(sb0));
		StringBuilderPool.getInstance().returnObject(sb0);
		assertFalse("This should not be locked",
				StringBuilderPool.getInstance().isItemLocked(sb0));
	}
	@Test(expected = PooledException.class)
	public void willNotAppendCharArrWithoutLock() throws PooledException {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		sb0.returnToPool();
		sb0.append(new char[] { ' ' });
	}
	@Test(expected = PooledException.class)
	public void willNotAppendCharWithoutLock() throws PooledException {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		sb0.returnToPool();
		sb0.append('c');
	}
	@Test(expected = PooledException.class)
	public void willNotAppendFloatWithoutLock() throws PooledException {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		sb0.returnToPool();
		sb0.append(2f);
	}
	@Test(expected = PooledException.class)
	public void willNotAppendLongWithoutLock() throws PooledException {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		sb0.returnToPool();
		sb0.append(2l);
	}
	@Test(expected = PooledException.class)
	public void willNotAppendObjWithoutLock() throws PooledException {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		sb0.returnToPool();
		sb0.append(new Object());
	}
	@Test(expected = PooledException.class)
	public void willNotAppendStringWithoutLock() throws PooledException {
		PooledStringBuilder sb0 =
				StringBuilderPool.getInstance().getStringBuilder();
		sb0.returnToPool();
		sb0.append("");
	}
}
