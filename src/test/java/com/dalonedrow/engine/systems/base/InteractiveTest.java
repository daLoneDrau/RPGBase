package com.dalonedrow.engine.systems.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.IOItemData;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.rpg.base.flyweights.Scriptable;
import com.dalonedrow.rpg.base.systems.TestScriptInstance;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * Test cases for {@link Interactive}.
 * @author drau
 *
 */
public final class InteractiveTest {
	/** the test instance. */
	private TestInteractiveInstance instance;
	@Before
	public void before() {
		instance = new TestInteractiveInstance();
		new TestScriptInstance();
		new TestProjectConstants();
	}
	/** Test that you can get the right instance. */
	@Test
	public void canGetInstance() {
		assertEquals(instance, Interactive.getInstance());
		assertNotEquals(null, Interactive.getInstance());
	}
	/**
	 * Cannot release IOs that are not in the IO pool. 
	 * @throws RPGException
	 */
	@Test(expected = RPGException.class)
	public void willNotReleaseMissingIO() throws RPGException {
		instance.releaseIO(-1);
	}
	/**
	 * Cannot release IOs that are not in the IO pool. 
	 * @throws RPGException
	 */
	@Test
	public void canReleaseIO() throws RPGException {
		BaseInteractiveObject io = instance.getNewIO();
		assertTrue(instance.hasIO(io.getRefId()));
		instance.releaseIO(io.getRefId());
	}
	/**
	 * Cannot release IOs that are not in the IO pool. 
	 * @throws RPGException
	 */
	@Test
	public void canRemoveFromInventory() throws RPGException {
		instance.RemoveFromAllInventories(null);
		BaseInteractiveObject io = instance.getNewIO();
		instance.RemoveFromAllInventories(io);
	}
	/**
	 * Cannot release IOs that are not in the IO pool. 
	 * @throws RPGException
	 */
	@Test
	public void testIsSameObject() throws RPGException {
		BaseInteractiveObject io0 = instance.getNewIO();
		BaseInteractiveObject io1 = instance.getNewIO();
		assertFalse(instance.isSameObject(null, null));
		assertFalse(instance.isSameObject(io0, null));
		assertFalse(instance.isSameObject(null, io1));
		io0.addIOFlag(IoGlobals.IO_13_UNIQUE);
		assertFalse(instance.isSameObject(io0, io1));
		io1.addIOFlag(IoGlobals.IO_13_UNIQUE);
		assertFalse(instance.isSameObject(io0, io1));
		io0.removeIOFlag(IoGlobals.IO_13_UNIQUE);
		assertFalse(instance.isSameObject(io0, io1));
		io1.removeIOFlag(IoGlobals.IO_13_UNIQUE);
		assertFalse(instance.isSameObject(io0, io1));
		io0.addIOFlag(IoGlobals.IO_02_ITEM);
		assertFalse(instance.isSameObject(io0, io1));
		io0.removeIOFlag(IoGlobals.IO_02_ITEM);
		io1.addIOFlag(IoGlobals.IO_02_ITEM);
		assertFalse(instance.isSameObject(io0, io1));
		io0.addIOFlag(IoGlobals.IO_02_ITEM);
		io1.setOverscript(new Scriptable() {
			
		});
		assertFalse(instance.isSameObject(io0, io1));
		io0.setOverscript(new Scriptable() {
			
		});
		io1.setOverscript(null);
		assertFalse(instance.isSameObject(io0, io1));
		io0.setOverscript(null);
		io0.setItemData(new IOItemData() {

            @Override
            protected float applyCriticalModifier() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected float calculateArmorDeflection() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected float getBackstabModifier() {
                // TODO Auto-generated method stub
                return 0;
            }
			
		});
		io0.getItemData().setItemName("coke");
		io1.setItemData(new IOItemData() {

            @Override
            protected float applyCriticalModifier() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected float calculateArmorDeflection() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected float getBackstabModifier() {
                // TODO Auto-generated method stub
                return 0;
            }
			
		});
		io1.getItemData().setItemName("pepsi");
		assertFalse(instance.isSameObject(io0, io1));
		io1.getItemData().setItemName("coke");
		assertTrue(instance.isSameObject(io0, io1));
	}
}
