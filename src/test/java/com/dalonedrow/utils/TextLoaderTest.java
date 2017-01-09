package com.dalonedrow.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public class TextLoaderTest {
	@Test
	public void canGetInstance() {
		assertNotNull(TextLoader.getInstance());
		assertNotNull(TextLoader.getInstance());
	}
	@Test
	public void canGetInstanceWithFolder() {
		assertNotNull(TextLoader.getInstance("test"));
		assertNotNull(TextLoader.getInstance("test"));
	}
	@Test
	public void canLoadFile() throws RPGException, PooledException {
		TextLoader.getInstance("com/dalonedrow/utils");
		assertEquals("\n// test comment\ng\n§section0\nblah blah \n§"
				+ "\n§section1\n// comment\nblah blah \n§\n",
				TextLoader.getInstance().loadText("sample_data.txt"));
	}
	@Test
	public void canLoadFileSection() throws RPGException, PooledException {
		TextLoader.getInstance("com/dalonedrow/utils");
		assertEquals("blah blah \n", TextLoader.getInstance().loadText(
				"sample_data.txt", "section1"));
	}
	@Test(expected = RPGException.class)
	public void willNotLoadMissingFile() throws RPGException, PooledException {
		TextLoader.getInstance("");
		TextLoader.getInstance().loadText("sample_data.txt");
	}
	@Test(expected = RPGException.class)
	public void willNotLoadMissingSection()
			throws RPGException, PooledException {
		TextLoader.getInstance("");
		TextLoader.getInstance().loadText("sample_data.txt", "missing");
	}
}
