package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.engine.systems.base.TestInteractiveInstance;
import com.dalonedrow.engine.systems.base.TestProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.systems.TestScriptInstance;
import com.dalonedrow.utils.ArrayUtilities;
import com.dalonedrow.utils.Watchable;
import com.dalonedrow.utils.Watcher;

public class IOEquipItemTest {
	private IOEquipItem data;
	@Before
	public void before() throws RPGException {
		new TestProjectConstants();
		new TestScriptInstance();
		data = new IOEquipItem();
	}
	@Test
	public void canCreate() throws RPGException {
		data = new IOEquipItem();
		assertNotNull(data);
		assertNotNull(data.getElement(0));
		data.getElement(0).setValue(25);
		assertEquals(25f, data.getElement(0).getValue(), .01f);
		data.reset();
		assertEquals(0f, data.getElement(0).getValue(), .01f);
		data.free();
		assertNull(data.getElement(0));
	}
}
