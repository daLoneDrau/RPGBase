package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.pooled.PooledException;

public class ScriptVariableTest {
	private ScriptVariable	faVar;
	private ScriptVariable	fVar;
	private ScriptVariable	iaVar;
	private ScriptVariable	iVar;
	private ScriptVariable	laVar;
	private ScriptVariable	lVar;
	private ScriptVariable	tVar;
	private ScriptVariable	taVar;
	private ScriptVariable	tgaVar;
	@Before
	public void before() {
		try {
			tVar = new ScriptVariable("tvar",
					ScriptConstants.TYPE_G_00_TEXT, "test");
			taVar = new ScriptVariable("laVar",
					ScriptConstants.TYPE_L_09_TEXT_ARR, new String[] { "test", "2" });
			tgaVar = new ScriptVariable("laVar",
					ScriptConstants.TYPE_G_01_TEXT_ARR, new String[] { "testg" });
			fVar = new ScriptVariable("fVar",
					ScriptConstants.TYPE_L_10_FLOAT, 1.2f);
			faVar = new ScriptVariable("faVar",
					ScriptConstants.TYPE_G_03_FLOAT_ARR,
					new float[] { 0f, 1.3f });
			iVar = new ScriptVariable("ivar", ScriptConstants.TYPE_L_12_INT, 5);
			iaVar = new ScriptVariable("iaVar",
					ScriptConstants.TYPE_G_05_INT_ARR, new int[] { 1, 2 });
			lVar =
					new ScriptVariable("lVar", ScriptConstants.TYPE_G_06_LONG,
							5l);
			laVar = new ScriptVariable("laVar",
					ScriptConstants.TYPE_L_15_LONG_ARR, new long[] { 12, 30 });
		} catch (RPGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test (expected = RPGException.class)
	public void willNotGetMisnamedTextArray() throws RPGException {
		fVar.getTextArrayVal();
	}
	@Test (expected = RPGException.class)
	public void willNotGetMisnamedTextArrayByIndex() throws RPGException {
		fVar.getTextArrayVal(1);
	}
	@Test (expected = RPGException.class)
	public void willNotGetInvalidTextArrayByIndex() throws RPGException {
		taVar.getTextArrayVal(5);
	}
	@Test
	public void canGetTextArray() throws RPGException {
		assertEquals("test", taVar.getTextArrayVal()[0]);
		assertEquals("2", taVar.getTextArrayVal(1));
		assertEquals("testg", tgaVar.getTextArrayVal()[0]);
		assertEquals("testg", tgaVar.getTextArrayVal(0));
		taVar.set(1, 33);
		assertEquals("33", taVar.getTextArrayVal(1));
		taVar = new ScriptVariable(taVar);
		assertEquals("33", taVar.getTextArrayVal(1));
	}
	@Test
	public void canCreate() throws RPGException, PooledException {
		assertNotNull(tVar);
		assertEquals(tVar.getName(), "tvar");
		assertEquals(tVar.getText(), "test");
		tVar.set("test2");
		tVar = new ScriptVariable(tVar);
		assertEquals(tVar.getText(), "test2");
		assertEquals(tVar.getType(), ScriptConstants.TYPE_G_00_TEXT);
		tVar.clear();
		assertNull(tVar.getText());
		assertNotNull(fVar);
		assertEquals(fVar.getFloatVal(), 1.2f, .001f);
		fVar.set(1f);
		assertEquals(fVar.getFloatVal(), 1.0f, .001f);
		assertNotNull(faVar);
		assertNotNull(faVar.getFloatArrayVal());
		assertEquals(faVar.getFloatArrayVal()[1], 1.3f, .001f);
		assertEquals(faVar.getFloatArrayVal(1), 1.3f, .001f);
		faVar.set(new float[] { 0.5f });
		faVar.set(2f);
		faVar = new ScriptVariable(faVar);
		assertEquals(faVar.getFloatArrayVal(1), 2.0f, .001f);
		assertNotNull(iVar);
		assertEquals(iVar.getIntVal(), 5);
		iVar.set(0);
		assertEquals(iVar.getIntVal(), 0);
		assertNotNull(iaVar);
		assertNotNull(iaVar.getIntArrayVal());
		assertEquals(iaVar.getIntArrayVal()[1], 2);
		assertEquals(iaVar.getIntArrayVal(1), 2);
		iaVar.set(new int[] { 1 });
		iaVar.set(2);
		iaVar = new ScriptVariable(iaVar);
		assertEquals(iaVar.getIntArrayVal(1), 2);
		assertNotNull(lVar);
		assertEquals(lVar.getLongVal(), 5l);
		lVar.set(31l);
		assertEquals(lVar.getLongVal(), 31l);
		assertNotNull(laVar);
		assertNotNull(laVar.getLongArrayVal());
		assertEquals(laVar.getLongArrayVal()[1], 30l);
		assertEquals(laVar.getLongArrayVal(1), 30l);
		lVar = new ScriptVariable(lVar);
		laVar = new ScriptVariable(laVar);
	}
	@Test(expected=RPGException.class)
	public void willNotCloneFromNull() throws RPGException {
		fVar = new ScriptVariable(null);
	}
	@Test
	public void canSetFields() throws PooledException, RPGException {
		fVar.set(null);
		assertEquals(fVar.getFloatVal(), 0f, .0001f);
		fVar.setType(ScriptConstants.TYPE_G_02_FLOAT);
		fVar.set(2.5);
		assertEquals(fVar.getFloatVal(), 2.5f, .0001f);
		fVar.set(0);
		assertEquals(fVar.getFloatVal(), 0f, .0001f);
		fVar.set(.5f);
		assertEquals(fVar.getFloatVal(), 0.5f, .0001f);
		fVar.set("0.5");
		assertEquals(fVar.getFloatVal(), 0.5f, .0001f);
		faVar.set(null);
		assertEquals(faVar.getFloatArrayVal().length, 0);
		faVar.set(new float[] { .5f });
		assertEquals(faVar.getFloatArrayVal(0), 0.5f, .0001f);
		faVar.set(0.5f);
		assertEquals(faVar.getFloatArrayVal(1), 0.5f, .0001f);
		faVar.set(2.2);
		assertEquals(faVar.getFloatArrayVal(2), 2.2f, .0001f);
		faVar.set(3);
		assertEquals(faVar.getFloatArrayVal(3), 3f, .0001f);
		faVar.set("4");
		assertEquals(faVar.getFloatArrayVal(4), 4f, .0001f);
		faVar.set(0, 1f);
		assertEquals(faVar.getFloatArrayVal(0), 1f, .0001f);
		faVar.set(1, 1.1);
		assertEquals(faVar.getFloatArrayVal(1), 1.1f, .0001f);
		faVar.set(20, new Integer(20));
		assertEquals(faVar.getFloatArrayVal(20), 20f, .0001f);
		faVar.set(20, "20.22");
		assertEquals(faVar.getFloatArrayVal(20), 20.22f, .0001f);
		iVar.set(null);
		assertEquals(iVar.getIntVal(), 0);
		iVar.set(2.5);
		assertEquals(iVar.getIntVal(), 2);
		iVar.set(0);
		assertEquals(iVar.getIntVal(), 0);
		iVar.set(.5f);
		assertEquals(iVar.getIntVal(), 0);
		iVar.set("20");
		assertEquals(iVar.getIntVal(), 20);
		iaVar.set(null);
		assertEquals(iaVar.getIntArrayVal().length, 0);
		iaVar.set(new int[] { 5 });
		assertEquals(iaVar.getIntArrayVal(0), 5);
		iaVar.set(0.5f);
		assertEquals(iaVar.getIntArrayVal(1), 0);
		iaVar.set(2.2);
		assertEquals(iaVar.getIntArrayVal(2), 2);
		iaVar.set(3);
		assertEquals(iaVar.getIntArrayVal(3), 3);
		iaVar.set("4");
		assertEquals(iaVar.getIntArrayVal(4), 4);
		iaVar.set(0, 1f);
		assertEquals(iaVar.getIntArrayVal(0), 1);
		iaVar.set(1, 1.1);
		assertEquals(iaVar.getIntArrayVal(1), 1);
		iaVar.set(20, 20);
		assertEquals(iaVar.getIntArrayVal(20), 20);
		iaVar.set(20, "20");
		assertEquals(iaVar.getIntArrayVal(20), 20);
		lVar.set(null);
		assertEquals(lVar.getLongVal(), 0L);
		lVar.set(2.5);
		assertEquals(lVar.getLongVal(), 2L);
		lVar.set(0);
		assertEquals(lVar.getLongVal(), 0L);
		lVar.set(.5f);
		assertEquals(lVar.getLongVal(), 0L);
		lVar.set(2234l);
		assertEquals(lVar.getLongVal(), 2234L);
		lVar.set("55555334556");
		assertEquals(lVar.getLongVal(), 55555334556L);
		laVar.set(null);
		assertEquals(laVar.getLongArrayVal().length, 0);
		laVar.set(new long[] { 5 });
		assertEquals(laVar.getLongArrayVal(0), 5l);
		laVar.set(0.5f);
		assertEquals(laVar.getLongArrayVal(1), 0l);
		laVar.set(2.2);
		assertEquals(laVar.getLongArrayVal(2), 2l);
		laVar.set(3);
		assertEquals(laVar.getLongArrayVal(3), 3l);
		laVar.set(4L);
		assertEquals(laVar.getLongArrayVal(4), 4l);
		laVar.set(5L);
		assertEquals(laVar.getLongArrayVal(5), 5L);
		laVar.set(0, 1f);
		assertEquals(laVar.getLongArrayVal(0), 1L);
		laVar.set(1, 1.1);
		assertEquals(laVar.getLongArrayVal(1), 1L);
		laVar.set(20, 20);
		assertEquals(laVar.getLongArrayVal(20), 20L);
		laVar.set(20, 20L);
		assertEquals(laVar.getLongArrayVal(20), 20L);
		laVar.set(20, "20");
		assertEquals(laVar.getLongArrayVal(20), 20L);
		tVar.set(1);
		assertEquals(tVar.getText(), "1");
		tVar.set("test");
		assertEquals(tVar.getText(), "test");
		tVar.set(new char[] { 'r', 'a' });
		assertEquals(tVar.getText(), "ra");
		tVar.set(null);
		assertNull(tVar.getText());
		tVar.setName("tVar");
		assertEquals(tVar.getName(), "tVar");
	}
	@Test(expected = RPGException.class)
	public void willNotCreateEmptyName() throws PooledException, RPGException {
		new ScriptVariable("", 255, null);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateNullInvalidType()
			throws PooledException, RPGException {
		new ScriptVariable("test", 255, null);
	}
	@Test(expected = RPGException.class)
	public void willNotCreateNullName() throws PooledException, RPGException {
		new ScriptVariable(null, 255, null);
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeF() throws RPGException {
		fVar.getFloatArrayVal();
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeF2() throws RPGException {
		fVar.getFloatArrayVal(0);
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeFA() throws RPGException {
		faVar.getFloatVal();
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeI() throws RPGException {
		iVar.getIntArrayVal();
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeI2() throws RPGException {
		iVar.getIntArrayVal(1);
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeIA() throws RPGException {
		iaVar.getIntVal();
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeL() throws RPGException {
		lVar.getLongArrayVal();
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeL2() throws RPGException {
		lVar.getLongArrayVal(2);
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeLA() throws RPGException {
		laVar.getText();
	}
	@Test(expected = RPGException.class)
	public void willNotGetWrongTypeT() throws RPGException {
		tVar.getLongVal();
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidFloat() throws RPGException, PooledException {
		fVar.set(new Object());
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidFloatArray()
			throws RPGException, PooledException {
		faVar.set(221L);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidFloatArray2()
			throws RPGException, PooledException {
		faVar.set(new Object());
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidFloatArrayIndex()
			throws RPGException, PooledException {
		faVar.set(0, null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidFloatArrayIndex2()
			throws RPGException, PooledException {
		faVar.set(0, "a");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidFloatString()
			throws RPGException, PooledException {
		fVar.set("a");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidInt() throws RPGException, PooledException {
		iVar.set(221L);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidIntArray()
			throws RPGException, PooledException {
		iaVar.set(221L);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidIntArrayIndex()
			throws RPGException, PooledException {
		iaVar.set(0, null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidIntArrayIndex2()
			throws RPGException, PooledException {
		iaVar.set(0, "a");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidIntString()
			throws RPGException, PooledException {
		iVar.set("a");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidLong() throws RPGException, PooledException {
		lVar.set(new Object());
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidLongArray()
			throws RPGException, PooledException {
		laVar.set(new int[] {});
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidLongArrayIndex()
			throws RPGException, PooledException {
		laVar.set(0, null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidLongArrayIndex2()
			throws RPGException, PooledException {
		laVar.set(0, "a");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidLongString()
			throws RPGException, PooledException {
		lVar.set("a");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidNameEmpty()
			throws RPGException, PooledException {
		laVar.setName("");
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidNameNull()
			throws RPGException, PooledException {
		laVar.setName(null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidType()
			throws RPGException, PooledException {
		laVar.setType(-1);
	}
	@Test(expected = RPGException.class)
	public void willNotSetInvalidType2()
			throws RPGException, PooledException {
		laVar.setType(16);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNonArray()
			throws RPGException, PooledException {
		lVar.set(0, 14);
	}
}
