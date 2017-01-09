package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.pooled.PooledException;

public class ScriptableTest {
	private ScriptVariable						faVar;
	private ScriptVariable						fVar;
	private ScriptVariable						gfaVar;
	private ScriptVariable						gfVar;
	private ScriptVariable						giaVar;
	private ScriptVariable						giVar;
	private ScriptVariable						glaVar;
	private ScriptVariable						glVar;
	private ScriptVariable						gtVar;
	private ScriptVariable						iaVar;
	private BaseInteractiveObject				io;
	private ScriptVariable						iVar;
	private ScriptVariable						laVar;
	private ScriptVariable						lVar;
	private Scriptable<BaseInteractiveObject>	scriptable;
	private ScriptVariable						tVar;
	@Before
	public void before() throws RPGException {
		scriptable = new Scriptable();
		io = new BaseInteractiveObject(0) {

		};
		try {
			tVar = new ScriptVariable("tvar",
					ScriptConstants.TYPE_L_08_TEXT, "test");
			fVar = new ScriptVariable("fVar",
					ScriptConstants.TYPE_L_10_FLOAT, 1.2f);
			faVar = new ScriptVariable("faVar",
					ScriptConstants.TYPE_L_11_FLOAT_ARR,
					new float[] { 0f, 1.3f });
			iVar = new ScriptVariable("ivar", ScriptConstants.TYPE_L_12_INT, 5);
			iaVar = new ScriptVariable("iaVar",
					ScriptConstants.TYPE_L_13_INT_ARR, new int[] { 1, 2 });
			lVar =
					new ScriptVariable("lVar", ScriptConstants.TYPE_L_14_LONG,
							5l);
			laVar = new ScriptVariable("laVar",
					ScriptConstants.TYPE_L_15_LONG_ARR, new long[] { 12, 30 });
			gtVar = new ScriptVariable("tvar",
					ScriptConstants.TYPE_G_00_TEXT, "test");
			gfVar = new ScriptVariable("fVar",
					ScriptConstants.TYPE_G_02_FLOAT, 1.2f);
			gfaVar = new ScriptVariable("faVar",
					ScriptConstants.TYPE_G_03_FLOAT_ARR,
					new float[] { 0f, 1.3f });
			giVar = new ScriptVariable("ivar",
					ScriptConstants.TYPE_G_04_INT, 5);
			giaVar = new ScriptVariable("iaVar",
					ScriptConstants.TYPE_G_05_INT_ARR, new int[] { 1, 2 });
			glVar =
					new ScriptVariable("lVar", ScriptConstants.TYPE_G_06_LONG,
							5l);
			glaVar = new ScriptVariable("laVar",
					ScriptConstants.TYPE_G_07_LONG_ARR, new long[] { 12, 30 });
		} catch (RPGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void canSetActions() {
		// clear local variables
		scriptable.clearLocalVariables();
		ScriptAction sa = new ScriptAction() {
			@Override
			public void execute() throws RPGException {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void setScript(Scriptable script) {
				// TODO Auto-generated method stub
				
			}			
		};
		scriptable.addScriptAction(0, sa);
		scriptable.addScriptAction(0, null);
		ScriptAction[] list = scriptable.getEventActions(0);
		assertEquals(1, list.length);
		list = scriptable.getEventActions(1);
		assertEquals(0, list.length);
		scriptable.addScriptAction(0,  new ScriptAction() {
			@Override
			public void execute() throws RPGException {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void setScript(Scriptable script) {
				// TODO Auto-generated method stub
				
			}			
		});
		list = scriptable.getEventActions(0);
		assertEquals(2, list.length);
	}
	@Test (expected = RPGException.class)
	public void willNotGetMissingStringArrayVariable() throws RPGException {
		// clear local variables
		scriptable.clearLocalVariables();
		scriptable.getLocalStringArrayVariableValue("test");
	}
	@Test (expected = RPGException.class)
	public void willNotGetMisnamedStringArrayVariable() throws RPGException {
		// clear local variables
		scriptable.clearLocalVariables();
		scriptable.setLocalVariable("test", 2);
		scriptable.getLocalStringArrayVariableValue("test");
	}
	@Test
	public void canGetStringArrayVariable() throws RPGException {
		// clear local variables
		scriptable.clearLocalVariables();
		scriptable.setLocalVariable("test", new String[] { "test" });
		assertEquals("test",
				scriptable.getLocalStringArrayVariableValue("test")[0]);
		scriptable.clearLocalVariables();
		scriptable.setLocalVariable("test",
				new char[][] { { 't', 'e', 's', 't' } });
		assertEquals("test",
				scriptable.getLocalStringArrayVariableValue("test")[0]);
		assertTrue(scriptable.hasLocalVariable("test"));
		scriptable.clearLocalVariables();
		assertFalse(scriptable.hasLocalVariable("test"));
	}
	@Test
	public void canAddLocalVariables() throws Exception {
		// clear local variables
		scriptable.clearLocalVariables();
		assertEquals(0, scriptable.getLocalVarArrayLength());
		// add float variable
		scriptable.addLocalVariable(fVar);
		assertTrue(scriptable.hasLocalVariables());
		assertEquals(1, scriptable.getLocalVarArrayLength());
		assertEquals(fVar, scriptable.getLocalVariable(0));
		assertEquals(fVar, scriptable.getLocalVariable(fVar.getName()));
		assertEquals(fVar.getFloatVal(), 
				scriptable.getLocalFloatVariableValue(fVar.getName()),
				.0001f);
		scriptable.setLocalVariable(0, null);
		assertEquals(1, scriptable.getLocalVarArrayLength());
		assertFalse(scriptable.hasLocalVariables());
		scriptable.clearLocalVariables();
		scriptable.addLocalVariable(fVar);
		assertEquals(1, scriptable.getLocalVarArrayLength());
		assertTrue(scriptable.hasLocalVariables());
		scriptable.clearLocalVariables();
		assertEquals(1, scriptable.getLocalVarArrayLength());
		assertFalse(scriptable.hasLocalVariables());
		scriptable.setLocalVariable(0, fVar);
		scriptable.setLocalVariable(255, faVar);
		assertEquals(scriptable.getLocalVariable(0), fVar);
		assertEquals(scriptable.getLocalVariable(255), faVar);
		scriptable.setLocalVariable(tVar);
		scriptable.setLocalVariable(tVar);
		assertEquals(scriptable.getLocalVariable(tVar.getName()), tVar);
		String s = tVar.getText();
		scriptable.setLocalVariable(tVar.getName(), "not same");
		assertNotEquals(scriptable.getLocalStringVariableValue(tVar.getName()),
				s);
		scriptable.setLocalVariable("lf", 0.5f);
		scriptable.setLocalVariable("ld", 0.5);
		scriptable.setLocalVariable("li", 2);
		scriptable.setLocalVariable("ll", 2L);
		scriptable.setLocalVariable("lfa", new float[] { 3f });
		scriptable.setLocalVariable("lia", new int[] { 3 });
		scriptable.setLocalVariable("lla", new long[] { 3L });
		scriptable.setLocalVariable("lt", "local text");
		assertTrue(scriptable.hasLocalVariable("lt"));
		scriptable.clearLocalVariable("lt");
		assertFalse(scriptable.hasLocalVariable("lt"));
	}
	@Test
	public void canClearFlags() {
		scriptable.assignDisallowedEvent(1);
		scriptable.assignDisallowedEvent(64);
		assertTrue(scriptable.hasAllowedEvent(1));
		assertTrue(scriptable.hasAllowedEvent(64));
		scriptable.removeDisallowedEvent(64);
		assertFalse(scriptable.hasAllowedEvent(64));
		scriptable.clearDisallowedEvents();
		assertFalse(scriptable.hasAllowedEvent(1));
	}
	@Test
	public void canCreate() throws RPGException {
		assertNotNull(scriptable);
		assertFalse(scriptable.hasLocalVariables());
		assertNull(scriptable.getIO());
		assertNull(scriptable.getMaster());
		scriptable.setIO(io);
		Scriptable m = new Scriptable();
		scriptable.setMaster(m);
		assertEquals(scriptable.getIO(), io);
		assertEquals(scriptable.getMaster(), m);
		assertEquals(scriptable.onAddToParty(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onAggression(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onCombine(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onEquip(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInit(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInitEnd(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInventoryClose(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInventoryIn(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInventoryOpen(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInventoryOut(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onInventoryUse(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onMovement(), ScriptConstants.ACCEPT);
		assertEquals(scriptable.onUnequip(), ScriptConstants.ACCEPT);
		scriptable.setTimer(4, 255);
		assertEquals(scriptable.getTimer(4), 255);
	}
	@Test
	public void canGetLocalVariables() throws Exception {
		scriptable.addLocalVariable(fVar);
		assertEquals(scriptable.getLocalVariable(0), fVar);
		assertNull(scriptable.getLocalVariable(-1));
		assertNull(scriptable.getLocalVariable(255));
		scriptable.addLocalVariable(faVar);
		scriptable.addLocalVariable(iVar);
		scriptable.addLocalVariable(iaVar);
		scriptable.addLocalVariable(lVar);
		scriptable.addLocalVariable(laVar);
		scriptable.addLocalVariable(tVar);
		assertEquals(scriptable.getLocalFloatVariableValue(fVar.getName()),
				fVar.getFloatVal(), .0001f);
		assertEquals(scriptable.getLocalFloatArrayVariableValue(
				faVar.getName()), faVar.getFloatArrayVal());
		assertEquals(scriptable.getLocalIntVariableValue(iVar.getName()),
				iVar.getIntVal());
		assertEquals(scriptable.getLocalIntArrayVariableValue(
				iaVar.getName()), iaVar.getIntArrayVal());
		assertEquals(scriptable.getLocalLongVariableValue(lVar.getName()),
				lVar.getLongVal(), .0001f);
		assertEquals(scriptable.getLocalLongArrayVariableValue(
				laVar.getName()), laVar.getLongArrayVal());
		assertEquals(scriptable.getLocalStringVariableValue(
				tVar.getName()), tVar.getText());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingFloat() throws Exception {
		scriptable.addLocalVariable(gfVar);
		scriptable.getLocalFloatVariableValue(gfVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingFloat2() throws Exception {
		scriptable.getLocalFloatVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingFloatArray() throws Exception {
		scriptable.addLocalVariable(gfaVar);
		scriptable.getLocalFloatArrayVariableValue(gfaVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingFloatArray2() throws Exception {
		scriptable.getLocalFloatArrayVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingInt() throws Exception {
		scriptable.addLocalVariable(giVar);
		scriptable.getLocalIntVariableValue(giVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingInt2() throws Exception {
		scriptable.getLocalIntVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingIntArray() throws Exception {
		scriptable.addLocalVariable(giaVar);
		scriptable.getLocalIntArrayVariableValue(giaVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingIntArray2() throws Exception {
		scriptable.getLocalIntArrayVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingLong() throws Exception {
		scriptable.addLocalVariable(glVar);
		scriptable.getLocalLongVariableValue(glVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingLong2() throws Exception {
		scriptable.getLocalLongVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingLongArray() throws Exception {
		scriptable.addLocalVariable(glaVar);
		scriptable.getLocalLongArrayVariableValue(glaVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingLongArray2() throws Exception {
		scriptable.getLocalLongArrayVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingString() throws Exception {
		scriptable.addLocalVariable(gtVar);
		scriptable.getLocalStringVariableValue(gtVar.getName());
	}
	@Test(expected = RPGException.class)
	public void willNotGetMissingString2() throws Exception {
		scriptable.getLocalStringVariableValue(null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidVariable() throws Exception {
		scriptable.setLocalVariable("t", new Object());
	}
	@Test(expected = RPGException.class)
	public void willNotSetLocalVariable() throws Exception {
		scriptable.setLocalVariable(-1, null);
	}
}
