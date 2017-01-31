package com.dalonedrow.rpg.base.systems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.TestInteractiveInstance;
import com.dalonedrow.engine.systems.base.TestProjectConstants;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.IoNpcData;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.rpg.base.flyweights.ScriptTimerAction;
import com.dalonedrow.rpg.base.flyweights.ScriptTimerInitializationParameters;
import com.dalonedrow.rpg.base.flyweights.Scriptable;
import com.dalonedrow.rpg.base.flyweights.TestScriptable;

/**
 * Test cases for the {@link Script} class.
 * @author drau
 */
public final class ScriptTest {
	protected int	died	= -1;
	private int		init	= -1;
	private int		initend	= -1;
	protected int	invin	= -1;
	@Before
	public void before() {
        new TestProjectConstants();
		new TestScriptInstance();
		new TestInteractiveInstance();
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io.setScript(new Scriptable());
		Script.getInstance().addToGroup(io, "test");
	}
	/**
	 * Tests that an IO can be set to a group.
	 * @throws RPGException
	 */
	@Test
	public void canClearEventStack() throws RPGException {
		final ScriptTest me = this;
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io.setScript(new Scriptable());
		Script.getInstance().addToGroup(io, "test2");
		io = ((TestInteractiveInstance) Interactive.getInstance()).getTestIO();
		io.setScript(new Scriptable() {
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onDie()
			 */
			@Override
			public int onDie() throws RPGException {
				if (super.hasLocalVariable("deathtest")) {
					died = super.getLocalIntVariableValue("deathtest");
					super.clearLocalVariables();
				} else {
					died = 0;
				}
				return super.onDie();
			}
		});
		Script.getInstance().addToGroup(io, "test");
		Script.getInstance().stackSendGroupScriptEvent(
				"test", ScriptConsts.SM_017_DIE, null, null);
		Script.getInstance().eventStackClearForIo(io);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(-1, died);
		Script.getInstance().stackSendGroupScriptEvent(
				"test", ScriptConsts.SM_017_DIE, null, null);
		Script.getInstance().eventStackClear();
		Script.getInstance().eventStackExecuteAll();
		assertEquals(-1, died);
		BaseInteractiveObject io2 =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io2.setScript(new Scriptable());
		Script.getInstance().setEventSender(io2);
		Script.getInstance().stackSendGroupScriptEvent(
				"test", ScriptConsts.SM_017_DIE, null, null);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(0, died);
		died = -1;
		assertEquals(-1, died);
		Script.getInstance().stackSendIOScriptEvent(
				io, ScriptConsts.SM_017_DIE, null, null);
		assertEquals(-1, died);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(0, died);
		died = -1;
		assertEquals(-1, died);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(-1, died);
		Script.getInstance().stackSendIOScriptEvent(
				io, ScriptConsts.SM_017_DIE,
				new Object[] { "deathtest", 255 }, null);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(255, died);
		died = -1;
		assertEquals(-1, died);
		Script.getInstance().stackSendIOScriptEvent(
				io, 0, null, "Die");
		Script.getInstance().eventStackExecuteAll();
		assertEquals(0, died);
		died = -1;
		assertEquals(-1, died);
		Script.getInstance().stackSendMsgToAllNPCIO(
				ScriptConsts.SM_017_DIE, null);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(-1, died);
		io.addIOFlag(IoGlobals.IO_03_NPC);
		io.setNPCData(new IoNpcData() {

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

            @Override
            public void ARX_EQUIPMENT_RecreatePlayerMesh() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public int getPoisonned() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected void awardXpForNpcDeath(int xp,
                    BaseInteractiveObject killerIO) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void damageNonLivingNPC(float dmg, int srcIoid,
                    boolean isSpellDamage) throws RPGException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public boolean calculateBackstab() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean calculateCriticalHit() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public float getFullDamage() {
                // TODO Auto-generated method stub
                return 0;
            }
		});
		Script.getInstance().stackSendMsgToAllNPCIO(
				ScriptConsts.SM_017_DIE, null);
		Script.getInstance().eventStackExecuteAll();
		assertEquals(0, died);
	}
	/**
	 * Tests that an IO can be set to a group.
	 * @throws RPGException
	 */
	@Test
	public void canCloneLocalVariables() throws RPGException {
		BaseInteractiveObject io1 =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io1.setScript(new Scriptable() {

		});
		io1.getScript().setLocalVariable("testvar", 1);
		BaseInteractiveObject io2 =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io2.setScript(new Scriptable() {

		});
		io2.getScript().setLocalVariable("testvar", 2);
		assertEquals(1, io1.getScript().getLocalIntVariableValue("testvar"));
		assertEquals(2, io2.getScript().getLocalIntVariableValue("testvar"));
		assertNotEquals(io1.getScript().getLocalIntVariableValue("testvar"),
				io2.getScript().getLocalIntVariableValue("testvar"));
		Script.getInstance().cloneLocalVars(null, null);
		assertNotEquals(io1.getScript().getLocalIntVariableValue("testvar"),
				io2.getScript().getLocalIntVariableValue("testvar"));
		Script.getInstance().cloneLocalVars(io1, null);
		assertNotEquals(io1.getScript().getLocalIntVariableValue("testvar"),
				io2.getScript().getLocalIntVariableValue("testvar"));
		Script.getInstance().cloneLocalVars(null, io2);
		assertNotEquals(io1.getScript().getLocalIntVariableValue("testvar"),
				io2.getScript().getLocalIntVariableValue("testvar"));
		Script.getInstance().cloneLocalVars(io1, io2);
		assertEquals(io1.getScript().getLocalIntVariableValue("testvar"),
				io2.getScript().getLocalIntVariableValue("testvar"));
		assertTrue(io2.getScript().hasLocalVariable("testvar"));
		io1.getScript().clearLocalVariables();
		assertFalse(io1.getScript().hasLocalVariable("testvar"));
		assertTrue(io2.getScript().hasLocalVariable("testvar"));
		Script.getInstance().cloneLocalVars(io1, io2);
		assertFalse(io2.getScript().hasLocalVariable("testvar"));
	}
	/**
	 * Tests that an IO can be set to a group.
	 * @throws RPGException
	 */
	@Test
	public void canFreeLocalVars() throws RPGException {
		Script.getInstance().freeAllLocalVariables(null);
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		Script.getInstance().freeAllLocalVariables(io);
		io.setScript(new Scriptable());
		io.getScript().setLocalVariable("1", 1);
		io.getScript().setLocalVariable("2", "2");
		assertTrue(io.getScript().hasLocalVariables());
		Script.getInstance().freeAllLocalVariables(io);
		assertFalse(io.getScript().hasLocalVariables());
	}
	@Test
	public void canGetInstance() {
		assertFalse(Script.getInstance().isDebug());
		Script.getInstance().setDebug(true);
		assertTrue(Script.getInstance().isDebug());
		assertEquals(100, Script.getInstance().getMaxTimerScript());
		assertNull(Script.getInstance().getEventSender());
		Script.getInstance().setEventSender(new BaseInteractiveObject(255) {

		});
		assertNotNull(Script.getInstance().getEventSender());
	}
	@Test
	public void canInitIO() throws RPGException {
		final ScriptTest me = this;
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io.setScript(new Scriptable() {
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInit()
			 */
			@Override
			public int onInit() throws RPGException {
				if (super.hasLocalVariable("inittest")) {
					init = super.getLocalIntVariableValue("inittest");
					super.clearLocalVariable("inittest");
				} else {
					init = 0;
				}
				return super.onInit();
			}
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInitEnd()
			 */
			@Override
			public int onInitEnd() throws RPGException {
				if (super.hasLocalVariable("initendtest")) {
					initend = super.getLocalIntVariableValue("initendtest");
					super.clearLocalVariable("initendtest");
				} else {
					initend = 0;
				}
				return super.onInitEnd();
			}
		});
		assertEquals(-1, init);
		assertEquals(-1, initend);
		assertEquals(-1, Script.getInstance().sendInitScriptEvent(null));
		assertEquals(-1, Script.getInstance().sendInitScriptEvent(
				new BaseInteractiveObject(255) {

				}));
		assertEquals(1, Script.getInstance().sendInitScriptEvent(io));
		assertEquals(0, init);
		assertEquals(0, initend);
		init = -1;
		initend = -1;
		assertEquals(-1, init);
		assertEquals(-1, initend);
		io.setOverscript(new Scriptable() {
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInit()
			 */
			@Override
			public int onInit() throws RPGException {
				if (super.hasLocalVariable("inittest")) {
					init = super.getLocalIntVariableValue("inittest");
					super.clearLocalVariable("inittest");
				} else {
					init = 2;
				}
				return super.onInit();
			}
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInitEnd()
			 */
			@Override
			public int onInitEnd() throws RPGException {
				if (super.hasLocalVariable("initendtest")) {
					initend = super.getLocalIntVariableValue("initendtest");
					super.clearLocalVariable("initendtest");
				} else {
					initend = 2;
				}
				return super.onInitEnd();
			}
		});
		assertEquals(1, Script.getInstance().sendInitScriptEvent(io));
		assertEquals(2, init);
		assertEquals(2, initend);
	}
	@Test
	public void canReleaseScript() throws RPGException {
		Scriptable scr = new Scriptable();
		scr.setLocalVariable("test", 2);
		assertEquals(2, scr.getLocalIntVariableValue("test"));
		Script.getInstance().releaseScript(scr);
		assertFalse(scr.hasLocalVariable("test"));
	}
	@Test
	public void canSetGroups() {
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		String group = "test";
		assertFalse(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().addToGroup(io, group);
		Script.getInstance().addToGroup(io, "Test2");
		assertTrue(Script.getInstance().isIOInGroup(io, group));
		assertTrue(Script.getInstance().isIOInGroup(io, "Test2"));
		Script.getInstance().releaseAllGroups(io);
		assertFalse(Script.getInstance().isIOInGroup(io, group));
		assertFalse(Script.getInstance().isIOInGroup(io, "Test2"));
	}
	@Test
	public void canSendScriptEvent() throws RPGException {
		assertEquals(-1, Script.getInstance().sendIOScriptEvent(
				null, ScriptConsts.SM_002_INVENTORYIN, null, null));
		final ScriptTest me = this;
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		io.setScript(new Scriptable() {
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInit()
			 */
			@Override
			public int onInit() throws RPGException {
				if (super.hasLocalVariable("inittest")) {
					init = super.getLocalIntVariableValue("inittest");
					super.clearLocalVariable("inittest");
				} else {
					init += 2;
				}
				return super.onInit();
			}
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInitEnd()
			 */
			@Override
			public int onInitEnd() throws RPGException {
				if (super.hasLocalVariable("initendtest")) {
					initend = super.getLocalIntVariableValue("initendtest");
					super.clearLocalVariable("initendtest");
				} else {
					initend += 2;
				}
				return super.onInitEnd();
			}
			/*
			 * (non-Javadoc)
			 * @see
			 * com.dalonedrow.rpg.base.flyweights.Scriptable#onInventoryIn()
			 */
			@Override
			public int onInventoryIn() throws RPGException {
				if (super.hasLocalVariable("invintest")) {
					invin = super.getLocalIntVariableValue("invintest");
					super.clearLocalVariable("invintest");
				} else {
					invin += 2;
				}
				return super.onInventoryIn();
			}
		});
		assertEquals(-1, invin);
		assertEquals(1, Script.getInstance().sendIOScriptEvent(
				io, ScriptConsts.SM_002_INVENTORYIN, null, null));
		assertEquals(1, invin);
		init = -1;
		initend = -1;
		assertEquals(1, Script.getInstance().sendIOScriptEvent(
				io, ScriptConsts.SM_001_INIT, null, null));
		assertEquals(3, init);
		assertEquals(-1, initend);
		assertEquals(1, Script.getInstance().sendIOScriptEvent(
				io, ScriptConsts.SM_033_INITEND, null, null));
		assertEquals(3, init);
		assertEquals(3, initend);
		io.setOverscript(new Scriptable() {
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInit()
			 */
			@Override
			public int onInit() throws RPGException {
				if (super.hasLocalVariable("inittest")) {
					init = super.getLocalIntVariableValue("inittest");
					super.clearLocalVariable("inittest");
				} else {
					init += 20;
				}
				return super.onInit();
			}
			/*
			 * (non-Javadoc)
			 * @see com.dalonedrow.rpg.base.flyweights.Scriptable#onInitEnd()
			 */
			@Override
			public int onInitEnd() throws RPGException {
				if (super.hasLocalVariable("initendtest")) {
					initend = super.getLocalIntVariableValue("initendtest");
					super.clearLocalVariable("initendtest");
				} else {
					initend += 20;
				}
				return super.onInitEnd();
			}
			/*
			 * (non-Javadoc)
			 * @see
			 * com.dalonedrow.rpg.base.flyweights.Scriptable#onInventoryIn()
			 */
			@Override
			public int onInventoryIn() throws RPGException {
				if (super.hasLocalVariable("invintest")) {
					invin = super.getLocalIntVariableValue("invintest");
					super.clearLocalVariable("invintest");
				} else {
					invin += 20;
				}
				return -1;
			}
		});
		init = -1;
		initend = -1;
		invin = -1;
		assertEquals(-1, invin);
		assertEquals(1, Script.getInstance().sendIOScriptEvent(
				io, ScriptConsts.SM_002_INVENTORYIN, null, null));
		assertEquals(21, invin);
		assertEquals(1, Script.getInstance().sendIOScriptEvent(
				io, ScriptConsts.SM_001_INIT, null, null));
		assertEquals(43, init);
		assertEquals(-1, initend);
		assertEquals(1, Script.getInstance().sendIOScriptEvent(
				io, ScriptConsts.SM_033_INITEND, null, null));
		assertEquals(43, init);
		assertEquals(43, initend);
		init = -1;
		initend = -1;
		Script.getInstance().resetObject(io, true);
		assertEquals(21, init);
		assertEquals(21, initend);
		init = -1;
		initend = -1;
		io.getScript().setLocalVariable("lvar", 2);
		io.getOverscript().setLocalVariable("lvar", 2);
		assertTrue(io.getScript().hasLocalVariable("lvar"));
		assertTrue(io.getOverscript().hasLocalVariable("lvar"));
		Script.getInstance().reset(io, true);
		assertEquals(21, init);
		assertEquals(21, initend);
		assertFalse(io.getScript().hasLocalVariable("lvar"));
		assertFalse(io.getOverscript().hasLocalVariable("lvar"));
		init = -1;
		initend = -1;
		Script.getInstance().resetAll(true);
		assertEquals(21, init);
		assertEquals(21, initend);
		init = -1;
		initend = -1;
		Script.getInstance().sendMsgToAllIO(ScriptConsts.SM_001_INIT,
				new Object[] { "testvar", 1 });
		assertEquals(43, init);
		assertEquals(-1, initend);
		Script.getInstance().sendMsgToAllIO(ScriptConsts.SM_033_INITEND,
				new Object[] { "testvar", 1 });
		assertEquals(43, initend);
		Scriptable script = new Scriptable() {

		};
		assertEquals(1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_045_OUCH, null, null, null));
		io.addGameFlag(IoGlobals.GFLAG_MEGAHIDE);
		assertEquals(1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_045_OUCH, null, io, null));
		assertEquals(1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_43_RELOAD, null, io, null));
		io.removeGameFlag(IoGlobals.GFLAG_MEGAHIDE);
		int oldShow = io.getShow();
		io.setShow(IoGlobals.SHOW_FLAG_DESTROYED);
		assertEquals(1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_43_RELOAD, null, io, null));
		io.setShow(oldShow);
		io.addIOFlag(IoGlobals.IO_06_FREEZESCRIPT);
		assertEquals(1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_41_LOAD, null, io, null));
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_43_RELOAD, null, io, null));
		io.removeIOFlag(IoGlobals.IO_06_FREEZESCRIPT);
		io.addIOFlag(IoGlobals.IO_03_NPC);
		io.setNPCData(new IoNpcData() {

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

            @Override
            public void ARX_EQUIPMENT_RecreatePlayerMesh() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public int getPoisonned() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected void awardXpForNpcDeath(int xp,
                    BaseInteractiveObject killerIO) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void damageNonLivingNPC(float dmg, int srcIoid,
                    boolean isSpellDamage) throws RPGException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public boolean calculateBackstab() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean calculateCriticalHit() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public float getFullDamage() {
                // TODO Auto-generated method stub
                return 0;
            }
		});
		assertEquals(1, Script.getInstance().sendScriptEvent(
				script, ScriptConsts.SM_41_LOAD, null, io, null));
		io.removeIOFlag(IoGlobals.IO_03_NPC);
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_COLLIDE_NPC);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_55_COLLIDE_NPC, null, io,
				null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_CHAT);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_10_CHAT, null, io, null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_HIT);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_016_HIT, null, io, null));
		io.getScript()
				.assignDisallowedEvent(ScriptConsts.DISABLE_INVENTORY2_OPEN);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_28_INVENTORY2_OPEN, null, io,
				null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_HEAR);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_46_HEAR, null, io, null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_DETECT);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_22_DETECTPLAYER, null, io,
				null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_DETECT);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_23_UNDETECTPLAYER, null, io,
				null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_AGGRESSION);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_57_AGGRESSION, null, io, null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_MAIN);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_008_MAIN, null, io, null));
		io.getScript().assignDisallowedEvent(ScriptConsts.DISABLE_CURSORMODE);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_73_CURSORMODE, null, io, null));
		io.getScript()
				.assignDisallowedEvent(ScriptConsts.DISABLE_EXPLORATIONMODE);
		assertEquals(-1, Script.getInstance().sendScriptEvent(
				io.getScript(), ScriptConsts.SM_74_EXPLORATIONMODE, null, io,
				null));
		io.getScript().clearDisallowedEvents();
	}
	@Test
	public void canGetGlobalVars() throws RPGException {
		Script.getInstance().setGlobalVariable("testString", "test");
		assertEquals("test", Script.getInstance().getGlobalStringVariableValue(
				"testString"));
		Script.getInstance().setGlobalVariable("testString[]", new String[] {"test"});
		assertNotNull(Script.getInstance().getGlobalStringArrayVariableValue("testString[]"));
		Script.getInstance().setGlobalVariable("testString", new char[] { 't', 'e', 's', 't' });
		assertEquals("test", Script.getInstance().getGlobalStringVariableValue(
				"testString"));
		Script.getInstance().setGlobalVariable("testString[]", new char[][] {{ 't', 'e', 's', 't' }});
		Script.getInstance().setGlobalVariable("testFloat", 5f);
		assertEquals(5, Script.getInstance().getGlobalFloatVariableValue(
				"testFloat"), .01f);
		Script.getInstance().setGlobalVariable("testFloat", 5d);
		assertEquals(5, Script.getInstance().getGlobalFloatVariableValue(
				"testFloat"), .01f);
		Script.getInstance().setGlobalVariable("testFloat[]", new float[] { 5f });
		assertNotNull(Script.getInstance().getGlobalFloatArrayVariableValue("testFloat[]"));
		Script.getInstance().setGlobalVariable("testInt", 3);
		assertEquals(3, Script.getInstance().getGlobalIntVariableValue(
				"testInt"));
		Script.getInstance().setGlobalVariable("testInt[]", new int[] { 5 });
		assertNotNull(Script.getInstance().getGlobalIntArrayVariableValue("testInt[]"));
		Script.getInstance().setGlobalVariable("testLong", 3L);
		assertEquals(3, Script.getInstance().getGlobalLongVariableValue(
				"testLong"));
		Script.getInstance().setGlobalVariable("testLong[]", new long[] { 5 });
		assertNotNull(Script.getInstance().getGlobalLongArrayVariableValue("testLong[]"));
		assertTrue(Script.getInstance().hasGlobalVariable("testString"));
		assertTrue(Script.getInstance().hasGlobalVariable("testString[]"));
		assertTrue(Script.getInstance().hasGlobalVariable("testFloat"));
		assertTrue(Script.getInstance().hasGlobalVariable("testFloat[]"));
		assertTrue(Script.getInstance().hasGlobalVariable("testInt"));
		assertTrue(Script.getInstance().hasGlobalVariable("testInt[]"));
		assertTrue(Script.getInstance().hasGlobalVariable("testLong"));
		assertTrue(Script.getInstance().hasGlobalVariable("testLong[]"));
		Script.getInstance().freeAllGlobalVariables();
		assertFalse(Script.getInstance().hasGlobalVariable("testString"));
		assertFalse(Script.getInstance().hasGlobalVariable("testString[]"));
		assertFalse(Script.getInstance().hasGlobalVariable("testFloat"));
		assertFalse(Script.getInstance().hasGlobalVariable("testFloat[]"));
		assertFalse(Script.getInstance().hasGlobalVariable("testInt"));
		assertFalse(Script.getInstance().hasGlobalVariable("testInt[]"));
		assertFalse(Script.getInstance().hasGlobalVariable("testLong"));
		assertFalse(Script.getInstance().hasGlobalVariable("testLong[]"));
	}
	private int timerset=0;
	@Test
	public void canSetTimer() throws NoSuchMethodException, SecurityException, RPGException {
		ScriptTimerInitializationParameters params = new ScriptTimerInitializationParameters();
		Scriptable script = new TestScriptable();
		params.setScript(script);
		params.setIo(((TestInteractiveInstance)
				Interactive.getInstance()).getTestIO());
		params.setMilliseconds(5);
		params.setName("aggression");
		params.setObj(script);
		params.setMethod(script.getClass().getMethod("onTargetDeath"));
		params.setStartTime(0);
		params.setRepeatTimes(1);
		params.setFlagValues(0);
		Script.getInstance().startTimer(params);
		params.setName(null);
		Script.getInstance().startTimer(params);
		params.setName("");
		Script.getInstance().startTimer(params);
		assertEquals(3, Script.getInstance().countTimers());
		Script.getInstance().timerClearAll();
		assertEquals(0, Script.getInstance().countTimers());
		Script.getInstance().startTimer(params);
		assertEquals(1, Script.getInstance().countTimers());
		Script.getInstance().timerClearByIO(params.getIo());
		assertEquals(0, Script.getInstance().countTimers());
		params.setName("aggression");
		Script.getInstance().startTimer(params);
		assertEquals(1, Script.getInstance().countTimers());
		Script.getInstance().timerClearByNameAndIO("aggression", null);
		assertEquals(1, Script.getInstance().countTimers());
		Script.getInstance().timerClearByNameAndIO("aggression", params.getIo());
		assertEquals(0, Script.getInstance().countTimers());
		Script.getInstance().startTimer(params);
		assertEquals(1, Script.getInstance().countTimers());
		Script.getInstance().timerClearByIO(params.getIo());
		assertEquals(0, Script.getInstance().countTimers());
		Script.getInstance().startTimer(params);
		assertEquals(1, Script.getInstance().countTimers());
		Script.getInstance().timerClearAllLocalsForIO(params.getIo());
		assertEquals(1, Script.getInstance().countTimers());
		params.getIo().setOverscript(params.getScript());
		Script.getInstance().timerClearAllLocalsForIO(params.getIo());
		assertEquals(0, Script.getInstance().countTimers());
		Script.getInstance().startTimer(params);
		assertFalse(((TestScriptable) params.getScript()).targetDied());
		Script.getInstance().timerCheck();
		assertTrue(((TestScriptable) params.getScript()).targetDied());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarString() throws RPGException {
		Script.getInstance().getGlobalStringVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarStringArr() throws RPGException {
		Script.getInstance().getGlobalStringArrayVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarFloat() throws RPGException {
		Script.getInstance().getGlobalFloatVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarFloatArr() throws RPGException {
		Script.getInstance().getGlobalFloatArrayVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarInt() throws RPGException {
		Script.getInstance().getGlobalIntVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarIntArr() throws RPGException {
		Script.getInstance().getGlobalIntArrayVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarLong() throws RPGException {
		Script.getInstance().getGlobalLongVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingVarLongArr() throws RPGException {
		Script.getInstance().getGlobalLongArrayVariableValue("blah");
	}
	@Test(expected = RPGException.class)
	public void willNotSetVarObject() throws RPGException {
		Script.getInstance().setGlobalVariable("blah", new Object());
	}
	@Test
	public void canSetMainEvent() {
		Script.getInstance().setMainEvent(null, null);
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		Script.getInstance().setMainEvent(io, null);
		Script.getInstance().setMainEvent(io, "main");
	}
	/** Tests that an IO can be set to a group. */
	@Test
	public void canSetGroup() {
		String group = "mobs";
		BaseInteractiveObject io =
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO();
		Script.getInstance().addToGroup(null, null);
		assertFalse(Script.getInstance().isIOInGroup(null, null));
		assertFalse(Script.getInstance().isIOInGroup(io, null));
		assertFalse(Script.getInstance().isIOInGroup(null, group));
		assertFalse(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().addToGroup(io, null);
		assertFalse(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().addToGroup(null, group);
		assertFalse(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().addToGroup(io, group);
		assertTrue(Script.getInstance().isIOInGroup(io, group));
		assertFalse(Script.getInstance().isIOInGroup(io, "TEST"));
		assertFalse(Script.getInstance().isIOInGroup(
				((TestInteractiveInstance) Interactive.getInstance())
						.getTestIO(),
				group));
		assertTrue(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().removeGroup(null, null);
		assertTrue(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().removeGroup(io, null);
		assertTrue(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().removeGroup(null, group);
		assertTrue(Script.getInstance().isIOInGroup(io, group));
		Script.getInstance().removeGroup(io, group);
		assertFalse(Script.getInstance().isIOInGroup(io, group));
	}
	@Test(expected = RPGException.class)
	public void willNotRunNullScript() throws RPGException {
		Script.getInstance().sendScriptEvent(null, 0, null, null, null);
	}
}
