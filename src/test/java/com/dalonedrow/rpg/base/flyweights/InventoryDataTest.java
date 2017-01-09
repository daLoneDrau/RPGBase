package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.TestInteractiveInstance;
import com.dalonedrow.engine.systems.base.TestProjectConstants;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.systems.Script;
import com.dalonedrow.rpg.base.systems.TestScriptInstance;

/**
 * Test cases for {@link InventoryData}.
 * @author drau
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class InventoryDataTest {
	/** the inventory data. */
	private InventoryData			id;
	/** test IO. */
	private BaseInteractiveObject	invOwnerIO;
	/** test IO. */
	private BaseInteractiveObject	itemIO;
	/** test slots. */
	private InventorySlot[]			slots;
	/** test flag. */
	private boolean newItemInInventory = false;
	/** test flag. */
	private boolean placedInInventory = false;
	/**
	 * Set up test data. 
	 * @throws RPGException
	 */
	@Before
	public void before() throws RPGException {
		new TestProjectConstants();
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		TestScriptInstance script = new TestScriptInstance();
		id = new InventoryData<BaseInteractiveObject,
				InventorySlot<BaseInteractiveObject>>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void PutInFrontOfPlayer(final BaseInteractiveObject itemIO,
					final boolean doNotApplyPhysics) {
				// TODO Auto-generated method stub
			}			
		};
		invOwnerIO = interactive.getTestIO();
		invOwnerIO.setPCData(new TestIoPcDataInstance());
		invOwnerIO.setScript(new Scriptable() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public int onInventoryIn() throws RPGException {
				if (Script.getInstance().getEventSender().equals(itemIO)) {
					placedInInventory = true;
				}
				return super.onInventoryIn();
			}			
		});
		id.setIo(invOwnerIO);
		itemIO = interactive.getTestIO();
		itemIO.setItemData(new IOItemData() {
			
		});
		itemIO.setScript(new Scriptable() {		
			/**
			 * {@inheritDoc}
			 */
			@Override
			public int onInventoryIn() throws RPGException {
				System.out.println("onInventoryIn");
				if (Script.getInstance().getEventSender().equals(invOwnerIO)) {
					newItemInInventory = true;
				}
				return super.onInventoryIn();
			}
		});
		slots = new InventorySlot[] { new InventorySlot() };
		id.setSlots(slots);
	}
	/**
	 * If successful, items are declared in inventory. 
	 * @throws RPGException if an error occurs
	 */
	@Test
	public void canDeclareInInventory() throws RPGException {
		id.ARX_INVENTORY_Declare_InventoryIn(invOwnerIO, null);
		assertFalse(newItemInInventory);
		id.ARX_INVENTORY_Declare_InventoryIn(invOwnerIO, itemIO);
		assertTrue(newItemInInventory);
		assertTrue(placedInInventory);
	}
	/** If successful, the fields can be set. */
	@Test
	public void canSetFields() {
		id.setIo(invOwnerIO);
		assertEquals(id.getIo(), invOwnerIO);
		assertEquals(id.getNumInventorySlots(), 1);
		assertNotNull(id.getSlot(0));
	}
	/**
	 * If successful, it can be determined if an item can be placed in
	 * inventory.
	 * @throws RPGException if an error occurs
	 */
	@Test
	public void canBePutInInventory() throws RPGException {
		// cant put null in inventory
		assertFalse(id.CanBePutInInventory(null));
		// cant put movable items in inventory
		itemIO.addIOFlag(IoGlobals.IO_15_MOVABLE);
		assertFalse(id.CanBePutInInventory(itemIO));
		// can put gold in a pc inventory
		itemIO.removeIOFlag(IoGlobals.IO_15_MOVABLE);
		itemIO.addIOFlag(IoGlobals.IO_10_GOLD);
		invOwnerIO.addIOFlag(IoGlobals.IO_01_PC);
		itemIO.getItemData().setPrice(10);
		assertTrue(id.CanBePutInInventory(itemIO));
		// put gold in a pc's inventory when it is script loaded
		itemIO.setScriptLoaded(true);
		assertTrue(id.CanBePutInInventory(itemIO));
		// can put gold in a non-pc's inventory
		invOwnerIO.removeIOFlag(IoGlobals.IO_01_PC);
		assertTrue(id.CanBePutInInventory(itemIO));
		// can't put in a non-pc's inventory when no slots available
		assertFalse(id.CanBePutInInventory(itemIO));
		// can stack items
		slots[0].setIo(null);
		itemIO.removeIOFlag(IoGlobals.IO_10_GOLD);
		itemIO.addIOFlag(IoGlobals.IO_02_ITEM);
		itemIO.getItemData().setStackSize(3);
		itemIO.getItemData().setCount(1);
		itemIO.getItemData().setMaxOwned(300);
		itemIO.getItemData().setItemName("Arrows");
		assertTrue(id.CanBePutInInventory(itemIO));
		BaseInteractiveObject io2 = ((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO();
		io2.addIOFlag(IoGlobals.IO_02_ITEM);
		
		io2.setItemData(new IOItemData() {
			
		});
		io2.setScript(new Scriptable() {		
		});
		io2.getItemData().setCount(2);
		io2.getItemData().setStackSize(3);
		itemIO.getItemData().setMaxOwned(300);
		io2.getItemData().setItemName("qtips");
		assertFalse(id.CanBePutInInventory(io2));
		io2.getItemData().setItemName("Arrows");
		assertTrue(id.CanBePutInInventory(io2));
		slots[0].getIo().getItemData().setCount(1);
		io2.getItemData().setCount(2);
		io2.setScriptLoaded(true);
		assertTrue(id.CanBePutInInventory(io2));
		slots[0].getIo().getItemData().setCount(2);
		io2.getItemData().setCount(2);
		assertTrue(id.CanBePutInInventory(io2));
		assertEquals(1, io2.getItemData().getCount());
		assertEquals(3, slots[0].getIo().getItemData().getCount());
	}
	/**
	 * If successful, a script event is sent to attempt to identify an item. 
	 * @throws RPGException if an error occurs
	 */
	@Test
	public void canIdentify() throws RPGException {
		BaseInteractiveObject pio = ((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO();
		pio.setScript(new Scriptable() {
			
		});
		id.ARX_INVENTORY_IdentifyIO(null, null);
		id.ARX_INVENTORY_IdentifyIO(pio, null);
		pio.addIOFlag(IoGlobals.IO_01_PC);
		id.ARX_INVENTORY_IdentifyIO(pio, null);
		pio.setPCData(new TestIoPcDataInstance());
		id.ARX_INVENTORY_IdentifyIO(pio, null);
		BaseInteractiveObject iio = ((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO();
		iio.setScript(new Scriptable() {
			
		});
		id.ARX_INVENTORY_IdentifyIO(pio, iio);
		iio.addIOFlag(IoGlobals.IO_02_ITEM);
		id.ARX_INVENTORY_IdentifyIO(pio, iio);
		iio.setItemData(new IOItemData() {
			
		});
		id.ARX_INVENTORY_IdentifyIO(pio, iio);
		iio.getItemData().setEquipitem(new IOEquipItem());
		id.ARX_INVENTORY_IdentifyIO(pio, iio);
		id.setLeftRing(false);
		assertFalse(id.isLeftRing());
		id.setLeftRing(true);
		assertTrue(id.isLeftRing());
	}
	/** test flag. */
	private String combineMsg = "not yet";
	/**
	 * If successful, a script event is sent to attempt to identify an item. 
	 * @throws RPGException if an error occurs
	 */
	@Test
	public void canCheckIfInInventory() throws RPGException {
		BaseInteractiveObject pio = ((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO();
		pio.addIOFlag(IoGlobals.IO_01_PC);
		pio.setScript(new Scriptable());
		BaseInteractiveObject iio1 = ((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO();
		iio1.setScript(new Scriptable() {
			/* (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onCombine()
			 */
			@Override
			public int onCombine() throws RPGException {
				combineMsg = combineMsg.concat("-now");
				return super.onCombine();
			}			
		});
		iio1.addIOFlag(IoGlobals.IO_02_ITEM);
		iio1.setItemData(new IOItemData() {
			
		});
		iio1.getItemData().setStackSize(3);
		iio1.getItemData().setCount(1);
		iio1.getItemData().setMaxOwned(300);
		iio1.getItemData().setItemName("Arrows");
		BaseInteractiveObject iio2 = ((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO();
		id = new InventoryData<BaseInteractiveObject,
				InventorySlot<BaseInteractiveObject>>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void PutInFrontOfPlayer(final BaseInteractiveObject itemIO,
					final boolean doNotApplyPhysics) {
				// TODO Auto-generated method stub
			}			
		};
		id.setIo(pio);
		assertFalse(id.IsInPlayerInventory(iio1));
		assertFalse(id.IsInPlayerInventory(iio2));
		slots = new InventorySlot[] { new InventorySlot() };
		id.setSlots(slots);
		assertFalse(id.IsInPlayerInventory(iio1));
		assertFalse(id.IsInPlayerInventory(iio2));
		assertTrue(id.CanBePutInInventory(iio1));
		assertTrue(id.IsInPlayerInventory(iio1));
		assertFalse(id.IsInPlayerInventory(iio2));
		// test set inventory item levels
		assertEquals(0, iio1.getLevel());
		id.ForcePlayerInventoryObjectLevel(2);
		assertEquals(2, iio1.getLevel());
		// test sending script commands to inventory
		assertEquals("not yet", combineMsg);
		id.SendInventoryObjectCommand(null, ScriptConsts.SM_24_COMBINE);
		assertEquals("not yet", combineMsg);
		id.SendInventoryObjectCommand("", ScriptConsts.SM_24_COMBINE);
		assertEquals("not yet", combineMsg);
		id.SendInventoryObjectCommand("Arrows", ScriptConsts.SM_24_COMBINE);
		assertEquals("not yet", combineMsg);
		iio1.setItemData(null);
		iio1.addGameFlag(IoGlobals.GFLAG_INTERACTIVITY);
		id.SendInventoryObjectCommand("Arrows", ScriptConsts.SM_24_COMBINE);
		assertEquals("not yet", combineMsg);
		iio1.setItemData(new IOItemData() {
			
		});
		iio1.getItemData().setItemName("Arrows");
		id.SendInventoryObjectCommand("a", ScriptConsts.SM_24_COMBINE);
		assertEquals("not yet", combineMsg);
		id.SendInventoryObjectCommand("Arrows", ScriptConsts.SM_24_COMBINE);
		assertEquals("not yet-now", combineMsg);
		// test replacing an item in all inventories
		id.ReplaceInAllInventories(null, null);
		id.ReplaceInAllInventories(iio1, null);
		id.ReplaceInAllInventories(null, iio2);
		pio.setInventory(id);
		id.ReplaceInAllInventories(iio2, iio1);
		assertTrue(id.IsInPlayerInventory(iio1));
		assertFalse(id.IsInPlayerInventory(iio2));
		id.ReplaceInAllInventories(iio1, iio2);
		assertTrue(id.IsInPlayerInventory(iio2));
		assertFalse(id.IsInPlayerInventory(iio1));
		// test replacing an item in one inventories
		// test clean inventory
		id.CleanInventory();
		id.ForcePlayerInventoryObjectLevel(2);
		assertFalse(id.IsInPlayerInventory(iio1));
	}
}
