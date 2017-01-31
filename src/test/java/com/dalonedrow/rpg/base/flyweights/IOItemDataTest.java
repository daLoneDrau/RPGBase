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

public class IOItemDataTest {
	IOItemData	data;
	IOEquipItem	ioe;
	Watcher		watcher0;
	Watcher		watcher1;
	String[]	watcherNotes	= new String[0];
	@Before
	public void before() {
		new TestProjectConstants();
		new TestScriptInstance();
        data = new IOItemData() {

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

        };
		watcher0 = new Watcher() {
			@Override
			public void watchUpdated(final Watchable data) {
				watcherNotes = ArrayUtilities.getInstance().extendArray(
						"watcher0", watcherNotes);
			}
		};
		watcher1 = new Watcher() {
			@Override
			public void watchUpdated(final Watchable data) {
				watcherNotes = ArrayUtilities.getInstance().extendArray(
						"watcher1", watcherNotes);
			}
		};
	}
	@Test (expected = RPGException.class)
	public void willNotEquipWithMissingIO() throws PooledException, RPGException {
		data.ARX_EQUIPMENT_Equip(null);
	}
	@Test
	public void canEquip() throws PooledException, RPGException {
		IOItemData dagger = new IOItemData() {

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

		};
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		dagger.setIo(new BaseInteractiveObject(0) {
			
		});
		dagger.ARX_EQUIPMENT_Equip(null);
		BaseInteractiveObject pio = interactive.getTestIO();
		dagger.ARX_EQUIPMENT_Equip(pio);
		pio.addIOFlag(IoGlobals.IO_01_PC);
		pio.setScript(new Scriptable());
		pio.setPCData(new TestIoPcDataInstance());
		pio.setInventory(new InventoryData() {
			@Override
			public void PutInFrontOfPlayer(BaseInteractiveObject itemIO,
					boolean doNotApplyPhysics) {
				// TODO Auto-generated method stub
				
			}			
		});
		dagger.ARX_EQUIPMENT_Equip(pio);
		dagger.setIo(interactive.getTestIO());
		dagger.ARX_EQUIPMENT_Equip(pio);
		dagger.getIo().setScript(new Scriptable());
		// equip dagger in weapon slot
		dagger.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER);
		dagger.ARX_EQUIPMENT_Equip(pio);
		assertEquals(dagger.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		assertEquals(EquipmentGlobals.WEAPON_DAGGER, dagger.getWeaponType());
		// equip 1H in weapon slot
		IOItemData oneH = new IOItemData() {

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

		};
		oneH.setIo(interactive.getTestIO());
		oneH.getIo().setScript(new Scriptable());
		assertNotEquals(oneH.getIo().getRefId(),
				pio.getPCData().getEquippedItem(
						EquipmentGlobals.EQUIP_SLOT_WEAPON));
		oneH.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_1H);
		oneH.ARX_EQUIPMENT_Equip(pio);
		assertEquals(oneH.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		assertEquals(EquipmentGlobals.WEAPON_1H, oneH.getWeaponType());
		// equip shield
		IOItemData shield = new IOItemData() {

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

		};
		shield.setIo(interactive.getTestIO());
		shield.getIo().setScript(new Scriptable());
		shield.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_SHIELD);
		shield.ARX_EQUIPMENT_Equip(pio);
		assertEquals(shield.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_SHIELD));
		assertEquals(oneH.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		// equip 2H, shield gets removed.
		IOItemData twoH = new IOItemData() {

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

		};
		twoH.setIo(interactive.getTestIO());
		twoH.getIo().setScript(new Scriptable());
		twoH.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_2H);
		twoH.ARX_EQUIPMENT_Equip(pio);
		assertEquals(twoH.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		assertEquals(-1, pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_SHIELD));
		assertEquals(EquipmentGlobals.WEAPON_2H, twoH.getWeaponType());
		// equip shield, 2h is removed
		shield.ARX_EQUIPMENT_Equip(pio);
		assertEquals(shield.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_SHIELD));
		assertEquals(-1, pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		// equip bow, shield gets removed.
		IOItemData bow = new IOItemData() {

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

		};
		bow.setIo(interactive.getTestIO());
		bow.getIo().setScript(new Scriptable());
		bow.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW);
		bow.ARX_EQUIPMENT_Equip(pio);
		assertEquals(bow.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		assertEquals(-1, pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_SHIELD));
		assertEquals(EquipmentGlobals.WEAPON_BOW, bow.getWeaponType());
		// equip shield, bow is removed
		shield.ARX_EQUIPMENT_Equip(pio);
		assertEquals(shield.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_SHIELD));
		assertEquals(-1, pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_WEAPON));
		// equip ring 0 on right
		IOItemData ring0 = new IOItemData() {

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

		};
		ring0.setIo(interactive.getTestIO());
		ring0.getIo().setScript(new Scriptable());
		ring0.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_RING);
		ring0.setRingType(0);
		ring0.ARX_EQUIPMENT_Equip(pio);
		assertEquals(ring0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_RIGHT));
		// equip ring 1 on left
		IOItemData ring1 = new IOItemData() {

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

		};
		ring1.setIo(interactive.getTestIO());
		ring1.getIo().setScript(new Scriptable());
		ring1.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_RING);
		ring1.setRingType(1);
		ring1.ARX_EQUIPMENT_Equip(pio);
		assertEquals(ring1.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_LEFT));
		// cannot equip ring of same type
		IOItemData ring2 = new IOItemData() {

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

		};
		ring2.setIo(interactive.getTestIO());
		ring2.getIo().setScript(new Scriptable());
		ring2.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_RING);
		ring2.setRingType(1);
		ring2.ARX_EQUIPMENT_Equip(pio);
		assertEquals(ring0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_RIGHT));
		assertEquals(ring1.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_LEFT));
		// cannot equip ring of same type
		ring2.setRingType(0);
		ring2.ARX_EQUIPMENT_Equip(pio);
		assertEquals(ring0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_RIGHT));
		assertEquals(ring1.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_LEFT));
		// equipping new ring will remove right ring first
		ring2.setRingType(3);
		ring2.ARX_EQUIPMENT_Equip(pio);
		assertEquals(ring2.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_RIGHT));
		assertEquals(ring1.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_LEFT));
		// equipping new ring will change left ring 2nd
		ring0.ARX_EQUIPMENT_Equip(pio);
		assertEquals(ring2.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_RIGHT));
		assertEquals(ring0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_RING_LEFT));
		// equipping new armor
		IOItemData armor0 = new IOItemData() {

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

		};
		armor0.setIo(interactive.getTestIO());
		armor0.getIo().setScript(new Scriptable());
		armor0.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_ARMOR);
		armor0.ARX_EQUIPMENT_Equip(pio);
		assertEquals(armor0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_TORSO));
		// equipping new armor
		IOItemData armor1 = new IOItemData() {

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

		};
		armor1.setIo(interactive.getTestIO());
		armor1.getIo().setScript(new Scriptable());
		armor1.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_ARMOR);
		armor1.ARX_EQUIPMENT_Equip(pio);
		assertEquals(armor1.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_TORSO));
		// equipping new armor
		IOItemData leg0 = new IOItemData() {

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

		};
		leg0.setIo(interactive.getTestIO());
		leg0.getIo().setScript(new Scriptable());
		leg0.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_LEGGINGS);
		leg0.ARX_EQUIPMENT_Equip(pio);
		assertEquals(leg0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_LEGGINGS));
		// equipping new armor
		IOItemData leg1 = new IOItemData() {

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

		};
		leg1.setIo(interactive.getTestIO());
		leg1.getIo().setScript(new Scriptable());
		leg1.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_LEGGINGS);
		leg1.ARX_EQUIPMENT_Equip(pio);
		assertEquals(leg1.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_LEGGINGS));
		// equipping new helm
		IOItemData helm0 = new IOItemData() {

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

		};
		helm0.setIo(interactive.getTestIO());
		helm0.getIo().setScript(new Scriptable());
		helm0.getIo().addTypeFlag(EquipmentGlobals.OBJECT_TYPE_HELMET);
		helm0.ARX_EQUIPMENT_Equip(pio);
		assertEquals(helm0.getIo().getRefId(), pio.getPCData().getEquippedItem(
				EquipmentGlobals.EQUIP_SLOT_HELMET));
		
	}
	@Test
	public void canAdjustCount() throws RPGException {
		assertEquals(data.getCount(), 0);
		data.setMaxOwned(50);
		assertEquals(data.getMaxOwned(), 50);
		data.adjustCount(5);
		assertEquals(data.getCount(), 5);
	}
	@Test
	public void canSetDesc() throws RPGException {
		assertNull("description", data.getDescription());
		data.setDescription("desc1".toCharArray());
		assertTrue("description",
				Arrays.equals(data.getDescription(), "desc1".toCharArray()));
		data.setDescription("desc2".toCharArray());
		assertTrue("description",
				Arrays.equals(data.getDescription(), "desc2".toCharArray()));
	}
	@Test
	public void canSetFields() throws RPGException {
		assertEquals("price is 0", data.getPrice(), 0, 0.0f);
		assertEquals("weight is 0", data.getWeight(), 0, 0.0f);
		assertNull("desc is null", data.getDescription());
		assertNotNull("ioe is not null", data.getEquipitem());
		data.setPrice(12);
		data.setWeight(3);
		data.setDescription(new char[] { 't', 'e', 's', 't' });
		data.setEquipitem(ioe);
		data.setItemName(new char[] { 't', 'e', 's', 't' });
		assertEquals("price is 12", data.getPrice(), 12, 0.0f);
		assertEquals("weight is 3", data.getWeight(), 3, 0.0f);
		assertEquals("desc is test",
				new String(data.getDescription()), "test");
		assertEquals("name is test",
				new String(data.getItemName()), "test");
		data.setDescription("test");
		data.setItemName("test");
		assertEquals("desc is test",
				new String(data.getDescription()), "test");
		assertEquals("name is test",
				new String(data.getItemName()), "test");
		assertEquals("ioe is ioe", data.getEquipitem(), ioe);
		data.setStealvalue('g');
		assertEquals('g', data.getStealvalue());
		data.setLightValue(5);
		assertEquals(5, data.getLightValue());
		TestInteractiveInstance interactive = new TestInteractiveInstance();
		BaseInteractiveObject io = interactive.getTestIO();
		data.setIo(io);
		assertEquals(io, data.getIo());
		data.setStackSize(9);
		assertEquals(9, data.getStackSize());
		data.setFoodValue('h');
		assertEquals('h', data.getFoodValue());
		data.setCount(4);
		assertEquals(4, data.getCount());
	}
	@Test
	public void canSetName() throws RPGException {
		assertNull("name", data.getItemName());
		data.setItemName("Torch".toCharArray());
		assertTrue("name",
				Arrays.equals(data.getItemName(), "Torch".toCharArray()));
		data.setItemName("Lockpick".toCharArray());
		assertTrue("name",
				Arrays.equals(data.getItemName(), "Lockpick".toCharArray()));
	}
	@Test(expected = RPGException.class)
	public void willNotAdjustCountBelowZero() throws RPGException {
		data.adjustCount(-100);
	}
	@Test(expected = RPGException.class)
	public void willNotAdjustCountMoreThanMax() throws RPGException {
		data.setMaxOwned(5);
		data.adjustCount(20);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullCharDesc() throws RPGException {
		data.setDescription((char[]) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullCharName() throws RPGException {
		data.setItemName((char[]) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullStringDesc() throws RPGException {
		data.setDescription((String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullStringName() throws RPGException {
		data.setItemName((String) null);
	}
}
