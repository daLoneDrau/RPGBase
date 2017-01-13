package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.engine.systems.base.TestInteractiveInstance;
import com.dalonedrow.engine.systems.base.TestProjectConstants;
import com.dalonedrow.rpg.base.constants.EquipmentGlobals;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.systems.TestScriptInstance;
import com.dalonedrow.utils.ArrayUtilities;
import com.dalonedrow.utils.Watchable;
import com.dalonedrow.utils.Watcher;

public class IOPCDataTest {
	/** creature for testing. */
	private BaseInteractiveObject	crtrIo;
	/** pc data. */
	private IoPcData				data;
	private boolean					died			= false;
	/** the interactive instance. */
	TestInteractiveInstance			interactive;
	private BaseInteractiveObject	io;
	private boolean					ouch1			= false;
	/** poison creature for testing. */
	private BaseInteractiveObject	psnCrtrIo;
	/** poison weapon for testing. */
	private BaseInteractiveObject	psnWpnIo;
	private Scriptable				script;
	private Watcher					watcher0;
	private Watcher					watcher1;
	private String[]				watcherNotes	= new String[0];
	/** weapon for testing. */
	private BaseInteractiveObject	wpnIo;
	@Before
	public void before() throws RPGException {
		interactive = new TestInteractiveInstance();
		new TestProjectConstants();
		new TestScriptInstance();
		beforeCreatePCIO();
		beforeCreateCreature();
		beforeCreatePoisonCreature();
		beforeCreateWeapon();
		beforeCreatePoisonWeapon();
	}
	private void beforeCreateCreature() throws RPGException {
		crtrIo = interactive.getTestIO();
		crtrIo.addIOFlag(IoGlobals.IO_03_NPC);
		crtrIo.setScript(new TestScriptable());
		crtrIo.setNPCData(new IoNpcData() {
			@Override
			protected void adjustLife(final float dmg) {
				// TODO Auto-generated method stub
			}
			@Override
			protected void adjustMana(final float dmg) {
				// TODO Auto-generated method stub
			}
			@Override
			public void ARX_NPC_ManagePoison() {
				// TODO Auto-generated method stub
			}
			@Override
			public float getBaseLife() {
				// TODO Auto-generated method stub
				return 10;
			}
			@Override
			public float getBaseMana() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			protected boolean hasLifeRemaining() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected void moveToInitialPosition() {
				// TODO Auto-generated method stub

			}
			@Override
			protected void restoreLifeToMax() {
				// TODO Auto-generated method stub

			}
			@Override
			protected void stopActiveAnimation() {
				// TODO Auto-generated method stub

			}
			@Override
			protected void stopIdleAnimation() {
				// TODO Auto-generated method stub

			}
            @Override
            protected void applyRulesModifiers() throws RPGException {
                // TODO Auto-generated method stub
                
            }
            @Override
            protected void applyRulesPercentModifiers() {
                // TODO Auto-generated method stub
                
            }
            @Override
            protected Object[][] getAttributeMap() {
                return new Object[][] {
                    { "ST", "Strength", 0 },
                    { "LF", "Life", 0 },
                    { "MLF", "Max Life", 1 },
                    { "MN", "Mana", 1 },
                    { "MMN", "Max Mana", 1 }
                };
            }
		});
	}
	public void beforeCreatePCIO() throws RPGException {
		data = new IoPcData() {
			@Override
			protected void adjustMana(final float dmg) {
				super.setBaseAttributeScore("MN",
						super.getBaseAttributeScore("MN") + dmg);
				if (super.getBaseAttributeScore(
						"LF") > super.getFullAttributeScore("MMN")) {
					super.setBaseAttributeScore("MN",
							super.getFullAttributeScore("MMN"));
				}
				if (super.getBaseAttributeScore("MN") < 0f) {
					super.setBaseAttributeScore("MN", 0f);
				}
			}
			@Override
			public void ARX_EQUIPMENT_RecreatePlayerMesh() {
				// TODO Auto-generated method stub

			}
			@Override
			public boolean canIdentifyEquipment(final IOEquipItem equipitem) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected float getBaseLife() {
				return this.getBaseAttributeScore("LF");
			}
			@Override
			protected float getBaseMana() {
				return this.getBaseAttributeScore("MN");
			}
			@Override
			public boolean isInCombat() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected String getLifeAttribute() {
				return "LF";
			}
			@Override
			protected void applyRulesPercentModifiers() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected void applyRulesModifiers() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected Object[][] getAttributeMap() {
				return new Object[][] {
					{ "ST", "Strength", 0 },
					{ "LF", "Life", 0 },
					{ "MLF", "Max Life", 1 },
					{ "MN", "Mana", 1 },
					{ "MMN", "Max Mana", 1 }
				};
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
		data.setBaseAttributeScore("ST", 6);
		data.setBaseAttributeScore("LF", 8);
		data.setBaseAttributeScore("MLF", 8);
		data.setBaseAttributeScore("MN", 8);
		data.setBaseAttributeScore("MMN", 8);
		data.setIo(interactive.getTestIO());
		data.getIo().setScript(new Scriptable() {
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onDie()
			 */
			@Override
			public int onDie() throws RPGException {
				died = true;
				return super.onDie();
			}

			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onOuch()
			 */
			@Override
			public int onOuch() throws RPGException {
				ouch1 = true;
				return super.onOuch();
			}
		});
		data.getIo().addIOFlag(IoGlobals.IO_01_PC);
		data.getIo().setInventory(new InventoryData() {
			@Override
			public void PutInFrontOfPlayer(BaseInteractiveObject itemIO,
					boolean doNotApplyPhysics) {
				// TODO Auto-generated method stub
				
			}			
		});
	}
	private void beforeCreatePoisonCreature() throws RPGException {
		psnCrtrIo = interactive.getTestIO();
		psnCrtrIo.setScript(new TestScriptable());
		psnCrtrIo.addIOFlag(IoGlobals.IO_03_NPC);
		psnCrtrIo.setNPCData(new IoNpcData() {
			@Override
			protected void adjustLife(final float dmg) {
				// TODO Auto-generated method stub
			}
			@Override
			protected void adjustMana(final float dmg) {
				// TODO Auto-generated method stub
			}
			@Override
			public void ARX_NPC_ManagePoison() {
				// TODO Auto-generated method stub
			}
			@Override
			public float getBaseLife() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getBaseMana() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			protected boolean hasLifeRemaining() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected void moveToInitialPosition() {
				// TODO Auto-generated method stub

			}
			@Override
			protected void restoreLifeToMax() {
				// TODO Auto-generated method stub

			}
			@Override
			protected void stopActiveAnimation() {
				// TODO Auto-generated method stub

			}
			@Override
			protected void stopIdleAnimation() {
				// TODO Auto-generated method stub

			}
            @Override
            protected void applyRulesModifiers() throws RPGException {
                // TODO Auto-generated method stub
                
            }
            @Override
            protected void applyRulesPercentModifiers() {
                // TODO Auto-generated method stub
                
            }
            @Override
            protected Object[][] getAttributeMap() {
                return new Object[][] {
                    { "ST", "Strength", 0 },
                    { "LF", "Life", 0 },
                    { "MLF", "Max Life", 1 },
                    { "MN", "Mana", 1 },
                    { "MMN", "Max Mana", 1 }
                };
            }
		});
		psnCrtrIo.setPoisonCharges(5);
		psnCrtrIo.setPoisonLevel(10);
	}
	private void beforeCreatePoisonWeapon() throws RPGException {
		psnWpnIo = interactive.getTestIO();
		psnWpnIo.addIOFlag(IoGlobals.IO_02_ITEM);
		psnWpnIo.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER);
		psnWpnIo.setItemData(new IOItemData() {

		});
		psnWpnIo.setPoisonCharges(5);
		psnWpnIo.setPoisonLevel(10);
		psnWpnIo.setScript(new Scriptable());
	}
	private void beforeCreateWeapon() throws RPGException {
		wpnIo = interactive.getTestIO();
		wpnIo.addIOFlag(IoGlobals.IO_02_ITEM);
		wpnIo.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER);
		wpnIo.setItemData(new IOItemData() {

		});
	}
	@Test
	public void canAddAndNotifyWatchers() {
		data.addWatcher(watcher0);
		data.addWatcher(watcher1);
		data.notifyWatchers();
		assertTrue("watcher0 notified",
				watcherNotes[0].equalsIgnoreCase("watcher0")
						|| watcherNotes[1].equalsIgnoreCase("watcher0"));
		assertTrue("watcher1 notified",
				watcherNotes[0].equalsIgnoreCase("watcher1")
						|| watcherNotes[1].equalsIgnoreCase("watcher1"));
	}
	@Test
	public void canAddFlags() {
		data.addInterfaceFlag(1);
		data.addInterfaceFlag(1);
		data.addInterfaceFlag(64);
		assertTrue(data.hasInterfaceFlag(1));
		assertTrue(data.hasInterfaceFlag(64));
	}
	@Test
	public void canAddKeys() {
		assertEquals("0 keys", data.getNumKeys(), 0);
		data.addKey(new char[] { 'k', 'e', 'y', '1' });
		assertEquals("1 keys", data.getNumKeys(), 1);
		data.addKey("Rusty Key");
		data.addKey("Rusty Key");
		assertEquals("2 keys", data.getNumKeys(), 2);
	}
	@Test
	public void canAdjustAttributes() throws RPGException {
		assertEquals(data.getBaseAttributeScore("ST"), 6, 0f);
		assertEquals(data.getAttributeModifier("ST"), 0, 0f);
		assertEquals(data.getFullAttributeScore("ST"), 6, 0f);
		data.setBaseAttributeScore("ST", 5);
		data.adjustAttributeModifier("ST", 5);
		assertEquals(data.getBaseAttributeScore("ST"), 5, 0f);
		assertEquals(data.getAttributeModifier("ST"), 5, 0f);
		assertEquals(data.getFullAttributeScore("ST"), 10, 0f);
		data.clearAttributeModifier("ST");
		assertEquals(data.getAttributeModifier("ST"), 0, 0f);
		assertEquals(data.getFullAttributeScore("ST"), 5, 0f);
		data.clearModAbilityScores();
		assertEquals(data.getAttributeModifier("ST"), 0, 0f);
		assertEquals(data.getFullAttributeScore("ST"), 5, 0f);
	}
	@Test
	public void canAdjustGold() {
		assertEquals("0gp", data.getGold(), 0, .0001);
		data.adjustGold(25);
		assertEquals("25gp", data.getGold(), 25, .0001);
		data.adjustGold(-5);
		assertEquals("20gp", data.getGold(), 20, .0001);
		data.adjustGold(-500);
		assertEquals("0gp", data.getGold(), 0, .0001);
	}
	@Test
	public void canAdjustXP() {
		assertEquals("0xp", data.getXp(), 0);
		data.adjustXp(25);
		assertEquals("25xp", data.getXp(), 25);
		data.adjustXp(-5);
		assertEquals("20xp", data.getXp(), 20);
		data.adjustXp(-500);
		assertEquals("0xp", data.getXp(), 0, .0001);
	}
	@Test
	public void canApplyModifier() throws RPGException {
		assertEquals(0, data.ARX_EQUIPMENT_Apply(0), .01f);
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON,
				new BaseInteractiveObject(255) {
			
		});
		data.ARX_EQUIPMENT_Apply(0);
		assertEquals(0, data.ARX_EQUIPMENT_Apply(0), .01f);
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, psnWpnIo);
		data.ARX_EQUIPMENT_Apply(0);
		assertEquals(0, data.ARX_EQUIPMENT_Apply(0), .01f);
		psnWpnIo.getItemData().setEquipitem(new IOEquipItem());
		psnWpnIo.getItemData().getEquipitem().getElement(0).setValue(5);
		assertEquals(5, data.ARX_EQUIPMENT_Apply(0), .01f);
		psnWpnIo.getItemData().getEquipitem().getElement(0).setPercentage(true);
		assertEquals(0, data.ARX_EQUIPMENT_Apply(0), .01f);
	}
	@Test
	public void canApplyPercentModifier() throws RPGException {
		assertEquals(0, data.ARX_EQUIPMENT_ApplyPercent(0, 5), .01f);
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON,
				new BaseInteractiveObject(255) {
			
		});
		assertEquals(0, data.ARX_EQUIPMENT_ApplyPercent(0, 5), .01f);
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, psnWpnIo);
		assertEquals(0, data.ARX_EQUIPMENT_ApplyPercent(0, 5), .01f);
		psnWpnIo.getItemData().setEquipitem(new IOEquipItem());
		psnWpnIo.getItemData().getEquipitem().getElement(0).setValue(5);
		assertEquals(0, data.ARX_EQUIPMENT_ApplyPercent(0, 5), .01f);
		psnWpnIo.getItemData().getEquipitem().getElement(0).setPercentage(true);
		assertEquals(.25, data.ARX_EQUIPMENT_ApplyPercent(0, 5), .01f);
	}
	@Test
	public void canClearFlags() {
		data.addInterfaceFlag(1);
		data.addInterfaceFlag(64);
		assertTrue(data.hasInterfaceFlag(1));
		assertTrue(data.hasInterfaceFlag(64));
		data.clearInterfaceFlags();
		assertFalse(data.hasInterfaceFlag(1));
		assertFalse(data.hasInterfaceFlag(64));
	}
	@Test
	public void canCreate() throws RPGException {
		assertNotNull(data);
		data = new IoPcData() {
			@Override
			protected void adjustMana(final float dmg) {
				// TODO Auto-generated method stub

			}
			@Override
			public void ARX_EQUIPMENT_RecreatePlayerMesh() {
				// TODO Auto-generated method stub

			}
			@Override
			public boolean canIdentifyEquipment(final IOEquipItem equipitem) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected float getBaseLife() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			protected float getBaseMana() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public boolean isInCombat() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected String getLifeAttribute() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			protected void applyRulesPercentModifiers() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected void applyRulesModifiers() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected Object[][] getAttributeMap() {
				return new Object[][] {
					{ "ST", "Strength", 0 },
					{ "LF", "Life", 0 },
					{ "MLF", "Max Life", 1 },
					{ "MN", "Mana", 1 },
					{ "MMN", "Max Mana", 1 }
				};
			}
		};
		data.clearModAbilityScores();
		assertNotNull(data);
		data = new IoPcData() {
			@Override
			protected void adjustMana(final float dmg) {
				// TODO Auto-generated method stub

			}
			@Override
			public void ARX_EQUIPMENT_RecreatePlayerMesh() {
				// TODO Auto-generated method stub

			}
			@Override
			public boolean canIdentifyEquipment(final IOEquipItem equipitem) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected float getBaseLife() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			protected float getBaseMana() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public boolean isInCombat() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			protected String getLifeAttribute() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			protected void applyRulesPercentModifiers() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected void applyRulesModifiers() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected Object[][] getAttributeMap() {
				return new Object[][] {
					{ "ST", "Strength", 0 },
					{ "LF", "Life", 0 },
					{ "MLF", "Max Life", 1 },
					{ "MN", "Mana", 1 },
					{ "MMN", "Max Mana", 1 }
				};
			}
		};
		data.clearModAbilityScores();
		assertNotNull(data);
	}
	@Test
	public void canDamageWithAttacker() throws RPGException {
		crtrIo.setTargetinfo(data.getIo().getRefId());
		data.setBaseAttributeScore("LF", 6);
		data.ARX_DAMAGES_DamagePlayer(2, 0, crtrIo.getRefId());
		assertEquals(4, data.getBaseAttributeScore("LF"), .01f);
		assertFalse(((TestScriptable) crtrIo.getScript()).targetDied());
		data.ARX_DAMAGES_DamagePlayer(6, 0, crtrIo.getRefId());
		assertTrue(((TestScriptable) crtrIo.getScript()).targetDied());
		assertEquals(0, data.getBaseAttributeScore("LF"), .01f);
		ouch1 = false;
		died = false;
		((TestScriptable) crtrIo.getScript()).setTargetDied(false);
	}
	@Test
	public void canDamageWithNoAttacker() throws RPGException {
		assertFalse(ouch1);
		data.setBaseAttributeScore("LF", 6);
		data.ARX_DAMAGES_DamagePlayer(2, 0, -1);
		assertTrue(ouch1);
		ouch1 = false;
		assertEquals(4, data.getBaseAttributeScore("LF"), .01f);
		assertFalse(ouch1);
		assertFalse(died);
		data.ARX_DAMAGES_DamagePlayer(6, 0, -1);
		assertEquals(0, data.getBaseAttributeScore("LF"), .01f);
		assertTrue(ouch1);
		ouch1 = false;
		assertTrue(died);
		died = false;
	}
	@Test
	public void canDamageWithPoisonCreature() throws RPGException {
		data.setBaseAttributeScore("LF", 6);
		assertEquals(5, psnCrtrIo.getPoisonCharges());
		data.ARX_DAMAGES_DamagePlayer(2, 0, psnCrtrIo.getRefId());
		assertEquals(4, data.getBaseAttributeScore("LF"), .01f);
		assertEquals(4, psnCrtrIo.getPoisonCharges());
		data.ARX_DAMAGES_DamagePlayer(6, 0, psnCrtrIo.getRefId());
		assertEquals(0, data.getBaseAttributeScore("LF"), .01f);
		assertEquals(3, psnCrtrIo.getPoisonCharges());
		ouch1 = false;
		died = false;
	}
	@Test
	public void canDamageWithPoisonWeapon() throws RPGException {
		data.setBaseAttributeScore("LF", 6);
		crtrIo.getNPCData().setWeapon(psnWpnIo);
		assertEquals(5, psnWpnIo.getPoisonCharges());
		data.ARX_DAMAGES_DamagePlayer(2, 0, crtrIo.getRefId());
		assertEquals(4, data.getBaseAttributeScore("LF"), .01f);
		assertEquals(4, psnWpnIo.getPoisonCharges());
		data.ARX_DAMAGES_DamagePlayer(6, 0, crtrIo.getRefId());
		assertEquals(0, data.getBaseAttributeScore("LF"), .01f);
		assertEquals(3, psnWpnIo.getPoisonCharges());
		ouch1 = false;
		died = false;
	}
	@Test
	public void canDrainMana() {
		assertEquals(8, data.getBaseMana(), .01f);
		data.getIo().addIOFlag(IoGlobals.PLAYERFLAGS_NO_MANA_DRAIN);
		data.ARX_DAMAGES_DrainMana(10);
		assertEquals(8, data.getBaseMana(), .01f);
		data.getIo().removeIOFlag(IoGlobals.PLAYERFLAGS_NO_MANA_DRAIN);
		data.ARX_DAMAGES_DrainMana(0);
		assertEquals(8, data.getBaseMana(), .01f);
		data.ARX_DAMAGES_DrainMana(1);
		assertEquals(7, data.getBaseMana(), .01f);
		data.ARX_DAMAGES_DrainMana(70);
		assertEquals(0, data.getBaseMana(), .01f);
	}
	@Test
	public void canFindKeys() {
		data.addKey(new char[] { 'k', 'e', 'y', '1' });
		data.addKey("Rusty Key");
		assertTrue("has key1", data.hasKey("key1"));
		assertTrue("has Rusty Key", data.hasKey("rusty key"));
	}
	@Test
	public void canGetKeys() {
		data.addKey(new char[] { 'k', 'e', 'y', '1' });
		data.addKey("Rusty Key");
		assertTrue(Arrays.equals("key1".toCharArray(), data.getKey(0)));
	}
	@Test
	public void canGetMissingKeys() {
		assertFalse(Arrays.equals("key1".toCharArray(), data.getKey(0)));
	}
	@Test
	public void canGetWeaponType() throws RPGException {
		assertEquals(EquipmentGlobals.WEAPON_BARE,
				data.ARX_EQUIPMENT_GetPlayerWeaponType());
		BaseInteractiveObject bio = new BaseInteractiveObject(255) {
			
		};
		bio.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_DAGGER);
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, bio);
		assertEquals(EquipmentGlobals.WEAPON_BARE,
				data.ARX_EQUIPMENT_GetPlayerWeaponType());
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, psnWpnIo);
		assertEquals(EquipmentGlobals.WEAPON_DAGGER,
				data.ARX_EQUIPMENT_GetPlayerWeaponType());
		psnWpnIo.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_1H);
		assertEquals(EquipmentGlobals.WEAPON_1H,
				data.ARX_EQUIPMENT_GetPlayerWeaponType());
		psnWpnIo.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_2H);
		assertEquals(EquipmentGlobals.WEAPON_2H,
				data.ARX_EQUIPMENT_GetPlayerWeaponType());
		psnWpnIo.addTypeFlag(EquipmentGlobals.OBJECT_TYPE_BOW);
		assertEquals(EquipmentGlobals.WEAPON_BOW,
				data.ARX_EQUIPMENT_GetPlayerWeaponType());
	}
	@Test
	public void canHealPlayer() {
		data.setBaseAttributeScore("LF", 0);
		data.ARX_DAMAGES_HealPlayer(2);
		assertEquals(0, data.getBaseAttributeScore("LF"), .01f);
		data.setBaseAttributeScore("LF", 2);
		data.ARX_DAMAGES_HealPlayer(0);
		assertEquals(2, data.getBaseAttributeScore("LF"), .01f);
		data.ARX_DAMAGES_HealPlayer(50);
		assertEquals(8, data.getBaseAttributeScore("LF"), .01f);
	}
	@Test
	public void canHealPlayerMana() {
		data.setBaseAttributeScore("LF", 0);
		data.ARX_DAMAGES_HealManaPlayer(2);
		data.setBaseAttributeScore("LF", 2);
		data.ARX_DAMAGES_HealManaPlayer(0);
		data.ARX_DAMAGES_HealManaPlayer(50);
	}
	@Test
	public void cannotFindKeysThatDontExist() {
		assertFalse("has key1", data.hasKey("key1"));
		assertFalse("has Rusty Key", data.hasKey("rusty key"));
	}
	@Test
	public void canReleaseEquipment() throws RPGException {
		assertEquals(-1, data.getEquippedItem(0));
		data.ARX_EQUIPMENT_Release(-1);
		data.setEquippedItem(0, 255);
		assertEquals(255, data.getEquippedItem(0));
		data.ARX_EQUIPMENT_Release(-1);
		assertEquals(255, data.getEquippedItem(0));
		data.ARX_EQUIPMENT_Release(255);
		assertEquals(-1, data.getEquippedItem(0));
	}
	@Test
	public void cannotRemoveMissingKeys() {
		data.addKey(new char[] { 'k', 'e', 'y', '1' });
		data.addKey("Rusty Key");
		assertTrue("has key1", data.hasKey("key1"));
		assertTrue("has Rusty Key", data.hasKey("rusty key"));
		assertEquals("2 keys", data.getNumKeys(), 2);
		data.removeKey("void");
		assertEquals("2 keys", data.getNumKeys(), 2);
	}
	@Test
	public void canRemoveFlags() {
		data.addInterfaceFlag(1);
		assertTrue(data.hasInterfaceFlag(1));
		data.removeInterfaceFlag(1);
		assertFalse(data.hasInterfaceFlag(1));
	}
	@Test
	public void canRemoveKeys() {
		data.removeKey("rusty key");
		data.addKey(new char[] { 'k', 'e', 'y', '1' });
		data.addKey("Rusty Key");
		assertTrue("has key1", data.hasKey("key1"));
		assertTrue("has Rusty Key", data.hasKey("rusty key"));
		data.removeKey(new char[] { 'k', 'e', 'y', '1' });
		data.removeKey("rusty key");
		assertFalse("does not have key1", data.hasKey("key1"));
		assertFalse("does not have Rusty Key", data.hasKey("rusty key"));
		assertEquals("0 keys", data.getNumKeys(), 0);
	}
	@Test
	public void canRemoveWatchers() {
		data.addWatcher(watcher0);
		data.addWatcher(watcher1);
		data.removeWatcher(watcher0);
		data.notifyWatchers();
		assertTrue("1 notifications", watcherNotes.length == 1);
	}
	@Test
	public void canSetEquipment() throws Exception {
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, null);
		assertEquals(-1,
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, wpnIo);
		assertEquals(wpnIo.getRefId(),
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, 0);
		assertEquals(0,
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
	}
	@Test
	public void canSetFields() {
		assertEquals("gender is -1", data.getGender(), -1);
		assertEquals("level is 0", data.getLevel(), 0);
		assertEquals("profession is -1", data.getProfession(), -1);
		assertEquals("race is -1", data.getRace(), -1);
		assertTrue("name is empty",
				Arrays.equals(data.getName(), "".toCharArray()));
		data.setGender(1);
		data.setLevel(12);
		data.setProfession(3);
		data.setRace(2);
		data.setName("Thomas".toCharArray());
		assertEquals("gender is 1", data.getGender(), 1);
		assertEquals("level is 12", data.getLevel(), 12);
		assertEquals("profession is 3", data.getProfession(), 3);
		assertEquals("race is 2", data.getRace(), 2);
		assertTrue("name is Thomas",
				Arrays.equals(data.getName(), "Thomas".toCharArray()));
		data.setName("Brian");
		assertTrue("name is Brian",
				Arrays.equals(data.getName(), "Brian".toCharArray()));
		assertEquals(0, data.getNumberOfBags());
		BaseInteractiveObject io = interactive.getTestIO();
		data.setIo(io);
		assertEquals(io, data.getIo());
		assertEquals(data, io.getPCData());
		assertEquals(6, data.getBaseAttributeScore(new char[] { 'S', 'T' }),
				.01f);
		assertEquals("Strength", data.getAttributeName("ST"));
	}
	@Test
	public void canTellIfItemIsEquipped() throws RPGException {
		assertFalse(data.ARX_EQUIPMENT_IsPlayerEquip(psnWpnIo));
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, psnWpnIo);
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_TORSO,
				new BaseInteractiveObject(255) {
			
		});
		assertFalse(data.ARX_EQUIPMENT_IsPlayerEquip(this.psnCrtrIo));
		assertTrue(data.ARX_EQUIPMENT_IsPlayerEquip(this.psnWpnIo));
	}
	@Test
	public void canUnequipWeapon() throws RPGException {
		data.ARX_EQUIPMENT_UnEquipPlayerWeapon();
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, 255);
		data.ARX_EQUIPMENT_UnEquipPlayerWeapon();
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, psnWpnIo);
		assertEquals(psnWpnIo.getRefId(),
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
		data.ARX_EQUIPMENT_UnEquipPlayerWeapon();
		assertEquals(-1,
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
	}
	@Test
	public void canUnequipAll() throws RPGException {
		data.ARX_EQUIPMENT_UnEquipAll();
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, this.psnWpnIo);
		assertEquals(psnWpnIo.getRefId(),
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
		data.ARX_EQUIPMENT_UnEquipAll();
		assertEquals(-1,
				data.getEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON));
	}
	@Test(expected = RPGException.class)
	public void willNotUnequipUnregistered() throws RPGException {
		data.setEquippedItem(EquipmentGlobals.EQUIP_SLOT_WEAPON, 255);
		data.ARX_EQUIPMENT_UnEquipAll();
	}
	@Test
	public void willNotAddNullWatcher() {
		data.addWatcher(null);
		data.notifyWatchers();
		assertTrue("no notifications", watcherNotes.length == 0);
	}
	@Test
	public void willNotDamageDeadPlayer() throws RPGException {
		data.setBaseAttributeScore("LF", 0);
		data.ARX_DAMAGES_DamagePlayer(0, 0, 0);
		assertEquals(0, data.getBaseAttributeScore("LF"), .01f);
	}
	@Test
	public void willNotDamageInvulnerablePlayer() throws RPGException {
		data.setBaseAttributeScore("LF", 6);
		data.getIo().addIOFlag(IoGlobals.PLAYERFLAGS_INVULNERABILITY);
		data.ARX_DAMAGES_DamagePlayer(3, 0, -1);
		assertEquals(6, data.getBaseAttributeScore("LF"), .01f);
		data.getIo().removeIOFlag(IoGlobals.PLAYERFLAGS_INVULNERABILITY);
	}
	@Test
	public void willNotDamageWithZero() throws RPGException {
		data.setBaseAttributeScore("LF", 6);
		data.ARX_DAMAGES_DamagePlayer(0, 0, -1);
		assertEquals(6, data.getBaseAttributeScore("LF"), .01f);
	}
	@Test(expected = RPGException.class)
	public void willNotGetInvalidEquipmentSlot() throws Exception {
		data.getEquippedItem(255);
	}
	@Test
	public void willNotRemoveNullWatcher() {
		data.addWatcher(watcher0);
		data.addWatcher(watcher1);
		data.removeWatcher(null);
		data.notifyWatchers();
		assertTrue("2 notifications", watcherNotes.length == 2);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidEquipmentSlotIO() throws Exception {
		data.setEquippedItem(255, null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidEquipmentSlotRefId() throws Exception {
		data.setEquippedItem(255, 0);
	}
}
