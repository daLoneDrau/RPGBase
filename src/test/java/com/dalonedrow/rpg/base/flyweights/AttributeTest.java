/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * @author drau
 */
public final class AttributeTest {
	/** test abbreviation. */
	private char[]		abbr;
	/** test Attribute. */
	private Attribute	att;
	/** test description. */
	private char[]		desc;
	/** test name. */
	private char[]		name;

	@Before
	public void beforeTests() {
		abbr = new char[] { 'a', 'b', 'b', 'r' };
		name = new char[] { 'n', 'a', 'm', 'e' };
		desc = new char[] { 'd', 'e', 's', 'c' };
		att = new Attribute(abbr, name, desc);
	}
	@Test
	public void canAdjustModifier() {
		// given

		// when
		att.adjustModifier(2);

		// then
		assertEquals("modifier is 2", att.getModifier(), 2, 0f);
	}
	@Test
	public void canCalculateFullValue() {
		// given

		// when
		att.adjustModifier(2);
		att.setBase(10);

		// then
		assertEquals("full value is 12", att.getFull(), 12, 0f);
	}
	@Test
	public void canClearModifier() {
		// given

		// when
		att.adjustModifier(2);
		att.clearModifier();

		// then
		assertEquals("modifier is 0", att.getModifier(), 0, 0f);
	}
	@Test
	public void canConstruct2Strings() throws RPGException {
		Attribute att = new Attribute("abbreviation", "name");
		assertTrue("abbreviation",
				Arrays.equals(att.getAbbr(), "abbreviation".toCharArray()));
	}
	@Test
	public void canConstructAllStrings() throws RPGException {
		// given

		// when
		Attribute att = new Attribute("abbreviation", "name", "description");

		// then
		assertTrue("abbreviation",
				Arrays.equals(att.getAbbr(), "abbreviation".toCharArray()));
		assertTrue("name",
				Arrays.equals(att.getDisplayName(), "name".toCharArray()));
		assertTrue("description", Arrays.equals(att.getDescription(),
				"description".toCharArray()));
	}
	@Test
	public void canConstructNullAbbreviationNullName() {
		// given

		// when
		Attribute att = new Attribute((char[]) null, (char[]) null);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), (char[]) null));
		assertTrue("name", Arrays.equals(att.getDisplayName(), (char[]) null));
	}
	@Test
	public void canConstructNullAbbreviationNullNameNullDescription() {
		// given

		// when
		Attribute att =
				new Attribute((char[]) null, (char[]) null, (char[]) null);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), (char[]) null));
		assertTrue("name", Arrays.equals(att.getDisplayName(), (char[]) null));
		assertTrue("description",
				Arrays.equals(att.getDescription(), (char[]) null));
	}
	@Test
	public void canConstructNullAbbreviationNullNameValidDescription() {
		// given

		// when
		Attribute att = new Attribute((char[]) null, (char[]) null, desc);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), (char[]) null));
		assertTrue("name", Arrays.equals(att.getDisplayName(), (char[]) null));
		assertTrue("description", Arrays.equals(att.getDescription(), desc));
	}
	@Test
	public void canConstructNullAbbreviationValidName() {
		// given

		// when
		Attribute att = new Attribute((char[]) null, name);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), (char[]) null));
		assertTrue("name", Arrays.equals(att.getDisplayName(), name));
	}
	@Test
	public void canConstructNullAbbreviationValidNameNullDescription() {
		// given

		// when
		Attribute att = new Attribute((char[]) null, name, (char[]) null);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), (char[]) null));
		assertTrue("name", Arrays.equals(att.getDisplayName(), name));
		assertTrue("description",
				Arrays.equals(att.getDescription(), (char[]) null));
	}
	@Test
	public void canConstructValidAbbreviationNullName() {
		// given

		// when
		Attribute att = new Attribute(abbr, (char[]) null);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), abbr));
		assertTrue("name", Arrays.equals(att.getDisplayName(), (char[]) null));
	}

	@Test
	public void canConstructValidAbbreviationNullNameNullDescription() {
		// given

		// when
		Attribute att = new Attribute(abbr, (char[]) null, (char[]) null);

		// then
		assertTrue("abbreviation", Arrays.equals(att.getAbbr(), abbr));
		assertTrue("name", Arrays.equals(att.getDisplayName(), (char[]) null));
		assertTrue("description", Arrays.equals(
				att.getDescription(), (char[]) null));
	}

	@Test
	public void canSetAbbr() throws RPGException {
		assertTrue("abbreviation",
				Arrays.equals(att.getAbbr(), "abbr".toCharArray()));
		att.setAbbr("test".toCharArray());
		assertTrue("abbreviation",
				Arrays.equals(att.getAbbr(), "test".toCharArray()));
		att.setAbbr("abbr");
		assertTrue("abbreviation",
				Arrays.equals(att.getAbbr(), "abbr".toCharArray()));
	}
	@Test
	public void canSetBase() throws RPGException {
		att.setBase(2);
		assertEquals("base", att.getBase(), 2, 0f);
	}
	@Test
	public void canSetDesc() throws RPGException {
		assertTrue("description",
				Arrays.equals(att.getDescription(), "desc".toCharArray()));
		att.setDescription("test".toCharArray());
		assertTrue("description",
				Arrays.equals(att.getDescription(), "test".toCharArray()));
		att.setDescription("desc");
		assertTrue("description",
				Arrays.equals(att.getDescription(), "desc".toCharArray()));
	}

	@Test
	public void canSetName() throws RPGException {
		assertTrue("name",
				Arrays.equals(att.getDisplayName(), "name".toCharArray()));
		att.setDisplayName("test".toCharArray());
		assertTrue("name",
				Arrays.equals(att.getDisplayName(), "test".toCharArray()));
		att.setDisplayName("name");
		assertTrue("name",
				Arrays.equals(att.getDisplayName(), "name".toCharArray()));
	}

	@Test(expected = RPGException.class)
	public void willNotConstructNullStringAbbreviation()
			throws RPGException {
		new Attribute(null, "name");
	}
	@Test(expected = RPGException.class)
	public void willNotConstructNullStringAbbreviation2()
			throws RPGException {
		new Attribute((String) null, "", "");
	}
	@Test(expected = RPGException.class)
	public void willNotConstructNullStringName()
			throws RPGException {
		new Attribute("abbr", null);
	}
	@Test(expected = RPGException.class)
	public void willNotConstructStringNullDesc()
			throws RPGException {
		new Attribute("", "", (String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotConstructStringNullName()
			throws RPGException {
		new Attribute("", (String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotConstructStringNullName2()
			throws RPGException {
		new Attribute("", (String) null, "");
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullCharAbbr() throws RPGException {
		att.setAbbr((char[]) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullCharDesc() throws RPGException {
		att.setDescription((char[]) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullCharName() throws RPGException {
		att.setDisplayName((char[]) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullStringAbbr() throws RPGException {
		att.setAbbr((String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullStringDesc() throws RPGException {
		att.setDescription((String) null);
	}
	@Test(expected = RPGException.class)
	public void willNotSetNullStringName() throws RPGException {
		att.setDisplayName((String) null);
	}
}
