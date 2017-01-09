/**
 *
 */
package com.dalonedrow.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A utility class to parse XML files.
 * @author DaLoneDrau
 */
public final class XMLParserUtility {
	/** the one and only instance of the <code>XMLParserUtility</code> class. */
	private static XMLParserUtility instance;
	/**
	 * Gives access to the singleton instance of {@link XMLParserUtility}.
	 * @return {@link XMLParserUtility}
	 */
	public static XMLParserUtility getInstance() {
		if (XMLParserUtility.instance == null) {
			XMLParserUtility.instance = new XMLParserUtility();
		}
		return XMLParserUtility.instance;
	}
	/** Hidden constructor. */
	private XMLParserUtility() {
		super();
	}
	/**
	 * Gets the int value assigned to a tag in an element. For example, for the
	 * xml snippet &lt;employee&gt;&lt;id&gt;2473&lt;/id&gt;&lt;/employee&gt;,
	 * the parser will return 2473 as the value of the tag "id".
	 * @param element the element
	 * @param tagName the tag name
	 * @return {@link Integer}
	 */
	public Integer getIntValue(final Element element,
			final String tagName) {
		Integer intVal = null;
		NodeList nl = element.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			intVal = new Integer(el.getFirstChild().getNodeValue());
		}
		return intVal;
	}
	/**
	 * Gets multiple integer values assigned to a tag in an element. For
	 * example, for the xml snippet &lt;host
	 * id="boston"&gt;&lt;service&gt;1&lt;/service&gt;
	 * &lt;service&gt;2&lt;/service&gt;&lt;/host&gt;, the parser will return the
	 * values 1, 2 as the values of the tag "service".
	 * @param element the element
	 * @param tagName the tag name
	 * @return <code>int</code>[]
	 * @throws Exception if an error occurs
	 */
	public int[] getMultipleIntFromElement(final Element element,
			final String tagName) throws Exception {
		int[] values = new int[0];
		NodeList nl = element.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				if (!el.getParentNode().equals(element)) {
					continue;
				}
				int[] dest = new int[values.length + 1];
				System.arraycopy(values, 0, dest, 0, values.length);
				dest[values.length] =
						Integer.parseInt(el.getFirstChild().getNodeValue());
				values = dest;
				dest = null;
			}
		}
		return values;
	}
	/**
	 * Gets multiple text values assigned to a tag in an element. For example,
	 * for the xml snippet &lt;host
	 * id="boston"&gt;&lt;service&gt;deli&lt;/service&gt;
	 * &lt;service&gt;cafe&lt;/service&gt;&lt;/host&gt;, the parser will return
	 * the values "bookstore", "cafe" as the values of the tag "service".
	 * @param element the element
	 * @param tagName the tag name
	 * @return {@link String}[]
	 */
	public String[] getMultipleTextFromElement(final Element element,
			final String tagName) {
		ArrayList<String> values = new ArrayList<String>();
		NodeList nl = element.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				if (!el.getParentNode().equals(element)) {
					continue;
				}
				values.add(el.getFirstChild().getNodeValue());
			}
		}
		return values.toArray(new String[values.size()]);
	}
	/**
	 * Gets the text value assigned to a tag in an element. For example, for the
	 * xml snippet
	 * &lt;employee&gt;&lt;name&gt;John&lt;/name&gt;&lt;/employee&gt;, the
	 * parser will return John as the value of the tag "name".
	 * @param element the element
	 * @param tagName the tag name
	 * @return {@link String}
	 */
	public String getTextFromElement(final Element element,
			final String tagName) {
		String textVal = null;
		NodeList nl = element.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}
	/**
	 * Parses an xml file and returns a {@link Document}. NOT USED. DOES NOT
	 * WORK WITH FILEHANDLE GENERATED FILES.
	 * @param file {@link File} containing the content to be parsed
	 * @return {@link Document}
	 * @throws Exception if an error occurs
	 */
	public Document parseXmlFile(final File file) throws Exception {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		// Using factory get an instance of document builder
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(file);
		return doc;
	}
	/**
	 * Parses an xml file and returns a {@link Document}.
	 * @param is {@link InputStream} containing the content to be parsed
	 * @return {@link Document}
	 */
	public Document parseXmlFile(final InputStream is) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(is);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doc;
	}
	/**
	 * Parses an xml file and returns a {@link Document}.
	 * @param fileName the name xml file
	 * @return {@link Document}
	 */
	public Document parseXmlFile(final String fileName) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(fileName);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doc;
	}
	/**
	 * Parses an xml file and returns a {@link Document}.
	 * @param is {@link InputStream} containing the content to be parsed
	 * @return {@link Document}
	 * @throws Exception if an error occurs
	 */
	public Document parseXmlStream(final InputStream is) throws Exception {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		// Using factory get an instance of document builder
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(is);
		return doc;
	}
	/**
	 * Parses an xml string and returns a {@link Document}.
	 * @param xml the name xml string
	 * @return {@link Document}
	 */
	public Document parseXmlString(final String xml) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			doc = db.parse(is);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doc;
	}
}
