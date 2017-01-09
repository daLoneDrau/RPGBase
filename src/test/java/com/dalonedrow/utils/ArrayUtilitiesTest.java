/**
 *
 */
package com.dalonedrow.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author drau
 */
public final class ArrayUtilitiesTest {
	boolean[]	testB;
	int[]		testI;
	Object[]	testO;
	String[]	testS;
	@Before
	public void beforeTests() {
		testI = new int[] { 0 };
		testB = new boolean[] { false };
		testS = new String[] { "TEST" };
		testO = new Object[] { new Object() };
	}

	@Test
	public void canExtendBoolArray() {
		// given

		// when
		testB = ArrayUtilities.getInstance().extendArray(false, testB);

		// then
		assertEquals("length is 2", testB.length, 2);
		assertEquals("last index is false", testB[testB.length - 1], false);
	}
	@Test
	public void canExtendIntArray() {
		// given

		// when
		testI = ArrayUtilities.getInstance().extendArray(1, testI);

		// then
		assertEquals("length is 2", testI.length, 2);
		assertEquals("last index is 1", testI[testI.length - 1], 1);
	}
	@Test
	public void canExtendObjArray() {
		// given

		// when
		Object o = new Object();
		testS =
				ArrayUtilities.getInstance().extendArray("DODO",
						testS);
		testO = ArrayUtilities.getInstance().extendArray(o, testO);
		// then
		assertEquals("length is 2", testS.length, 2);
		assertEquals("last index is DODO", testS[testS.length - 1], "DODO");
		assertEquals("last index is o", testO[testO.length - 1], o);
	}
	@Test
	public void canPrependBoolArray() {
		// given

		// when
		testB = ArrayUtilities.getInstance().prependArray(true, testB);

		// then
		assertEquals("length is 2", testB.length, 2);
		assertEquals("first index is true", testB[0], true);
	}
	@Test
	public void canPrependIntArray() {
		// given

		// when
		testI = ArrayUtilities.getInstance().prependArray(1, testI);

		// then
		assertEquals("length is 2", testI.length, 2);
		assertEquals("first index is 1", testI[0], 1);
	}
	@Test
	public void canPrependObjArray() {
		// given

		// when
		testS =
				ArrayUtilities.getInstance().prependArray("DODO",
						testS);

		// then
		assertEquals("length is 2", testS.length, 2);
		assertEquals("first index is false", testS[0], "DODO");
	}
	@Test
	public void canRemoveBoolArrayFirstIndex() {
		// given

		// when
		testB = ArrayUtilities.getInstance().extendArray(false, testB);

		// then
		assertEquals("length is 2", testB.length, 2);
		assertEquals("last index is false", testB[testB.length - 1], false);

		testB = ArrayUtilities.getInstance().removeIndex(0, testB);
		assertEquals("length is 1", testB.length, 1);
		assertEquals("first index is false", testB[0], false);
	}
	@Test
	public void canRemoveBoolArrayLastIndex() {
		// given

		// when
		testB = ArrayUtilities.getInstance().extendArray(false, testB);

		// then
		assertEquals("length is 2", testB.length, 2);
		assertEquals("last index is false", testB[testB.length - 1], false);

		testB = ArrayUtilities.getInstance().removeIndex(1, testB);
		assertEquals("length is 1", testB.length, 1);
		assertEquals("first index is false", testB[0], false);
	}
	@Test
	public void canRemoveBoolArrayMiddleIndex() {
		// given

		// when
		testB = ArrayUtilities.getInstance().extendArray(false, testB);
		testB = ArrayUtilities.getInstance().extendArray(true, testB);

		// then
		assertEquals("length is 3", testB.length, 3);
		assertEquals("last index is true", testB[testB.length - 1], true);

		testB = ArrayUtilities.getInstance().removeIndex(1, testB);
		assertEquals("length is 2", testB.length, 2);
		assertEquals("first index is false", testB[0], false);
	}
	@Test
	public void canRemoveIntArrayFirstIndex() {
		// given

		// when
		testI = ArrayUtilities.getInstance().extendArray(1, testI);

		// then
		assertEquals("length is 2", testI.length, 2);
		assertEquals("first index is 0", testI[0], 0);

		testI = ArrayUtilities.getInstance().removeIndex(0, testI);
		assertEquals("length is 1", testI.length, 1);
		assertEquals("first index is 1", testI[0], 1);
	}
	@Test
	public void canRemoveIntArrayLastIndex() {
		// given

		// when
		testI = ArrayUtilities.getInstance().extendArray(1, testI);

		// then
		assertEquals("length is 2", testI.length, 2);
		assertEquals("first index is 0", testI[0], 0);

		testI = ArrayUtilities.getInstance().removeIndex(1, testI);
		assertEquals("length is 1", testI.length, 1);
		assertEquals("first index is 0", testI[0], 0);
	}
	@Test
	public void canRemoveIntArrayMiddleIndex() {
		// given

		// when
		testI = ArrayUtilities.getInstance().extendArray(1, testI);
		testI = ArrayUtilities.getInstance().extendArray(2, testI);

		// then
		assertEquals("length is 3", testI.length, 3);
		assertEquals("first index is 0", testI[0], 0);

		testI = ArrayUtilities.getInstance().removeIndex(1, testI);
		assertEquals("length is 2", testI.length, 2);
		assertEquals("first index is 0", testI[0], 0);
	}
	@Test
	public void canRemoveObjArrayFirst() {
		// given

		// when
		testS =
				ArrayUtilities.getInstance().extendArray("DODO",
						testS);

		// then
		assertEquals("length is 2", testS.length, 2);
		assertEquals("last index is DODO", testS[testS.length - 1], "DODO");

		testS = ArrayUtilities.getInstance().removeIndex(0, testS);
		assertEquals("length is 1", testS.length, 1);
		assertEquals("first index is DODO", testS[testS.length - 1], "DODO");
	}
	@Test
	public void canRemoveObjArrayLast() {
		// given

		// when
		testS =
				ArrayUtilities.getInstance().extendArray("DODO",
						testS);

		// then
		assertEquals("length is 2", testS.length, 2);
		assertEquals("last index is DODO", testS[testS.length - 1], "DODO");

		testS = ArrayUtilities.getInstance().removeIndex(1, testS);
		assertEquals("length is 1", testS.length, 1);
		assertEquals("first index is TEST", testS[0], "TEST");
	}
	@Test
	public void canRemoveObjArrayMiddle() {
		// given

		// when
		testS =
				ArrayUtilities.getInstance().extendArray("DODO",
						testS);
		testS =
				ArrayUtilities.getInstance().extendArray("LAST",
						testS);

		// then
		assertEquals("length is 3", testS.length, 3);
		assertEquals("last index is LAST", testS[testS.length - 1], "LAST");

		testS = ArrayUtilities.getInstance().removeIndex(1, testS);
		assertEquals("length is 2", testS.length, 2);
		assertEquals("first index is TEST", testS[0], "TEST");
	}
	@Test(expected = NullPointerException.class)
	public void willNotExtendNullBoolArray() {
		// given

		// when
		testB = ArrayUtilities.getInstance().extendArray(false, null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotExtendNullIntArray() {
		// given

		// when
		testI = ArrayUtilities.getInstance().extendArray(1, (int[]) null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotExtendNullObjArray() {
		// given

		// when
		testS = ArrayUtilities.getInstance().extendArray("", null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotPrependNullBoolArray() {
		// given

		// when
		testB = ArrayUtilities.getInstance().prependArray(false, null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotPrependNullIntArray() {
		// given

		// when
		testI = ArrayUtilities.getInstance().prependArray(1, null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotPrependNullObjArray() {
		// given

		// when
		testS = ArrayUtilities.getInstance().prependArray("", null);
	}
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void willNotRemoveMissingBoolIndex() {
		// given

		// when
		testB = ArrayUtilities.getInstance().removeIndex(2, testB);
	}
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void willNotRemoveMissingIntIndex() {
		// given

		// when
		testI = ArrayUtilities.getInstance().removeIndex(2, testI);
	}
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void willNotRemoveMissingStringIndex() {
		// given

		// when
		testS = ArrayUtilities.getInstance().removeIndex(2, testS);
	}
	@Test(expected = NullPointerException.class)
	public void willNotRemoveNullBoolArray() {
		// given

		// when
		testB = ArrayUtilities.getInstance().removeIndex(1, (boolean[]) null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotRemoveNullIntArray() {
		// given

		// when
		testI = ArrayUtilities.getInstance().removeIndex(1, (int[]) null);
	}
	@Test(expected = NullPointerException.class)
	public void willNotRemoveNullObjArray() {
		// given

		// when
		testS =
				ArrayUtilities.getInstance().removeIndex(1,
						(String[]) null);
	}
}
