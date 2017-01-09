/**
 *
 */
package com.dalonedrow.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.rpg.graph.TwoDimensional;

/**
 * @author drau
 */
public final class XMLParserUtilityTest {
	@Test
	public void canGetInstance() {
		assertNotNull(XMLParserUtility.getInstance());
		assertNotNull(XMLParserUtility.getInstance());
	}
	@Test
	public void canLoadFile() throws RPGException, PooledException {
		final int country = 0, farm = 1, forest = 2, badlands = 3;
		final int mountains = 4, desert = 5, swamp = 6;
		Document doc = XMLParserUtility.getInstance().parseXmlFile(
				"src/main/java/com/dalonedrow/utils/bp_hex_map.xml");
		NodeList nl = doc.getElementsByTagName("mapHex");
		TwoDimensional td = new TwoDimensional() { };
		System.out.println("INSERT INTO dwarfstar_barbarian_prince"
				+ ".hex_location(name, x, y, loc_conversion, hex_type_id) "
				+ "VALUES (");
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			System.out.print("  ");
			int x = XMLParserUtility.getInstance().getIntValue(
					(Element) nl.item(i), "x");
			int y = XMLParserUtility.getInstance().getIntValue(
					(Element) nl.item(i), "y");
			int type = XMLParserUtility.getInstance().getIntValue(
					(Element) nl.item(i), "type");
			String name = XMLParserUtility.getInstance().getTextFromElement(
					(Element) nl.item(i), "name");
			if (name == null) {
				System.out.print("NULL, ");
			} else {
				System.out.print("'");
				System.out.print(name.replaceAll("'", "''"));
				System.out.print("', ");
			}
			System.out.print(x);
			System.out.print(", ");
			System.out.print(y);
			System.out.print(", ");
			System.out.print(td.convertPointToInt(x, y));
			System.out.print(", (SELECT hex_type_id FROM "
					+ "dwarfstar_barbarian_prince.hex_type WHERE name = '");
			switch (type) {
			case country:
				System.out.print("Countryside");
				break;
			case farm:
				System.out.print("Farmland");
				break;
			case forest:
				System.out.print("Forest");
				break;
			case badlands:
				System.out.print("Badlands");
				break;
			case mountains:
				System.out.print("Mountains");
				break;
			case desert:
				System.out.print("Desert");
				break;
			case swamp:
				System.out.print("Swamp");
				break;
			default:
				throw new RPGException(ErrorMessage.BAD_PARAMETERS,
						"unknown type " + type);
			}
			System.out.print("'))");
			if (i + 1 < len) {
				System.out.println(", (");
			} else {
				System.out.println(";");
			}
		}
	}
}
