package com.dalonedrow.pooled;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class PooledStringBuilderTest {
	@Test
	public void canAppendDifferentTypes() throws PooledException {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		sb.append('t');
		sb.append("his".toCharArray());
		sb.append(0.5f);
		sb.append(-22);
		sb.append(145236789125489l);
		sb.append("end");
		assertEquals(sb.toString(), "this0.5-22145236789125489end");
	}
	@Test
	public void canAppendObjects() throws PooledException {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		Object o = new Object();
		sb.append(o);
		assertEquals(sb.toString(), o.toString());
		sb.setLength(0);
		sb.append((Object) null);
		assertEquals(sb.toString(), "null");
	}
	@Test
	public void canClearBuilder() throws PooledException {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		Object o = new Object();
		sb.append(o);
		sb.setLength(0);
		assertEquals(sb.toString(), "");
	}
	@Test
	public void canGetLength() throws PooledException {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		sb.init();
		sb.append('b');
		assertEquals(sb.length(), 1);
	}
	@Test(expected = PooledException.class)
	public void isUnlockedAfterReturn() throws PooledException {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		sb.returnToPool();
		sb.append(0);
	}
}
