/**
 * TextureLoader takes the name of an images list file, and loads all images
 * from that list. images loaded can be of four different types: o - a single
 * image n - a numbered list of images s - a number of images loaded as a
 * graphics strip - such as a gif with several images in it g - a group of
 * images each with different names
 * @author Owner
 */
package com.dalonedrow.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author DaLoneDrau
 */
public final class TextLoader {
	/** the one and only instance of the <code>TextLoader</code> class. */
	private static TextLoader	instance;
	/** the section delimiter. */
	private static final char	SECTION_DELIMITER	= '§';
	/**
	 * Gives access to the singleton instance of {@link TextLoader}.
	 * @return {@link TextLoader}
	 */
	public static TextLoader getInstance() {
		if (TextLoader.instance == null) {
			TextLoader.instance = new TextLoader();
		}
		return TextLoader.instance;
	}
	/**
	 * Gives access to the singleton instance of {@link TextLoader}.
	 * @return {@link TextLoader}
	 */
	public static TextLoader getInstance(final String folder) {
		if (TextLoader.instance == null) {
			TextLoader.instance = new TextLoader();
		}
		TextLoader.instance.setLibraryFolder(folder);
		return TextLoader.instance;
	}
	/** the folder containing the image library. */
	private String libraryFolder;
	/**
	 * Finds a text section.
	 * @param section the section name
	 * @param inText the text being parsed
	 * @param input the input reader
	 * @return {@link String}
	 * @throws PooledException
	 * @throws Exception if an error occurs
	 */
	private String findTextSection(final String section, final String inText,
			final BufferedReader input) throws RPGException, PooledException {
		String text = null;
		String sectionName = inText.substring(1);
		try {
			while (text == null) {
				if (sectionName.equalsIgnoreCase(section)) {
					// found the right section. read it
					text = readSection(input);
				} else {
					// not the right section. skip past this section
					readSection(input);
					// read the next section name
					sectionName = input.readLine();
					while (sectionName != null
							&& (sectionName.length() == 0
									|| sectionName.startsWith("//"))) {
						sectionName = input.readLine();
					}
					if (sectionName == null) {
						System.out.println(
								"Missing text trying to load " + section);
						text = "MISSING TEXT";
						break;
					}
					if (sectionName.charAt(0) == SECTION_DELIMITER) {
						sectionName = sectionName.substring(1);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	/**
	 * Loads a text file and returns the text contained therein.
	 * @param fileName the name of the file
	 * @return {@link String}
	 * @throws PooledException
	 * @throws Exception if an error occurs
	 */
	public String loadText(final String fileName)
			throws RPGException, PooledException {
		String text = null;
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		sb.append(libraryFolder);
		sb.append("/");
		sb.append(fileName);
		BufferedReader input;
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		try {
			input = new BufferedReader(new InputStreamReader(
					cl.getResourceAsStream(sb.toString()), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			sb.returnToPool();
			sb = null;
			throw new RPGException(ErrorMessage.INVALID_DATA_FORMAT, e);
		} catch (NullPointerException e) {
			PooledStringBuilder sb1 =
					StringBuilderPool.getInstance().getStringBuilder();
			sb1.append("Cannot read file ");
			sb1.append(sb.toString());
			sb1.append(".");
			RPGException ex =
					new RPGException(ErrorMessage.ILLEGAL_ACCESS,
							sb1.toString(), e);
			sb1.returnToPool();
			sb.returnToPool();
			sb1 = null;
			sb = null;
			throw ex;
		}
		sb.setLength(0);
		// logger.info("loadImage(" + filePath + ")");
		String inText = ""; // String object to read
		// in each line of the file
		try {
			inText = input.readLine(); // priming read
			while (inText != null) {
				inText = inText.replaceAll("\\\\t", "\t");
				inText = inText.replaceAll("\\\\n", "\n");
				sb.append(inText);
				sb.append("\n");
				inText = input.readLine();
			}
			text = sb.toString();
			text = text.replaceAll("<removeLF>\n", "");
			sb.setLength(0);
			sb = null;
			input.close();
		} catch (IOException e) {
			sb.returnToPool();
			sb = null;
			throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
		}
		if (text == null) {
			sb = StringBuilderPool.getInstance().getStringBuilder();
			sb.append("ERROR! TextLoader loadText() file ");
			sb.append(fileName);
			sb.append(" cannot be read.");
			RPGException ex = new RPGException(ErrorMessage.ILLEGAL_OPERATION,
					sb.toString());
			sb.setLength(0);
			sb.returnToPool();
			sb = null;
			throw ex;
		}
		return text;
	}
	/**
	 * Loads a text file and returns the text contained therein.
	 * @param fileName the name of the file
	 * @return {@link String}
	 * @throws PooledException
	 * @throws Exception if an error occurs
	 */
	public String loadText(final String fileName, final String section)
			throws RPGException, PooledException {
		String text = null;
		try {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			sb.append(libraryFolder);
			sb.append("/");
			sb.append(fileName);
			BufferedReader input;
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			input = new BufferedReader(new InputStreamReader(
					cl.getResourceAsStream(sb.toString()), "UTF-8"));
			sb.setLength(0);
			sb.returnToPool();
			sb = null;
			// logger.info("loadImage(" + filePath + ")");
			String inText = ""; // String object to read
			// in each line of the file
			inText = input.readLine(); // priming read
			while (inText != null && text == null) {
				if (inText.length() == 0
						|| inText.startsWith("//")) {
					inText = input.readLine(); // read next line
					continue;
				} else {
					// read the line prefix character and convert to lower case
					char prefix = Character.toLowerCase(inText.charAt(0));
					switch (prefix) {
					case SECTION_DELIMITER:
						// logger.info("o line - " + inText);
						// DONE
						text = findTextSection(section, inText, input);
						break;
					default:
						inText = input.readLine(); // read next line
						break;
					}
				}
			}
			input.close();
		} catch (IOException ex) {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			sb.append("Error opening file ");
			sb.append(libraryFolder);
			sb.append("/");
			sb.append(fileName);
			RPGException e =
					new RPGException(ErrorMessage.INTERNAL_ERROR, sb.toString(),
							ex);
			sb.setLength(0);
			sb.returnToPool();
			sb = null;
			throw e;
		} catch (NullPointerException npe) {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			sb.append("Error opening file ");
			sb.append(libraryFolder);
			sb.append("/");
			sb.append(fileName);
			RPGException e =
					new RPGException(ErrorMessage.INTERNAL_ERROR, sb.toString(),
							npe);
			sb.setLength(0);
			sb.returnToPool();
			sb = null;
			throw e;
		}
		if (text == null) {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			sb.append("ERROR! TextLoader loadText() file ");
			sb.append(fileName);
			sb.append(" cannot be read.");
			RPGException ex = new RPGException(ErrorMessage.ILLEGAL_OPERATION,
					sb.toString());
			sb.setLength(0);
			sb.returnToPool();
			sb = null;
			throw ex;
		}
		text = text.replaceAll("<removeLF>\n", "");
		return text;
	}
	private String readSection(final BufferedReader input) throws RPGException {
		String text = null;
		try {
			PooledStringBuilder sb =
					StringBuilderPool.getInstance().getStringBuilder();
			String inText = input.readLine(); // priming read
			boolean sectionDone = false;
			while (inText != null && !sectionDone) {
				if (inText.length() == 0
						|| inText.startsWith("//")) {
					inText = input.readLine(); // read next line
					continue;
				}
				char prefix = Character.toLowerCase(inText.charAt(0));
				switch (prefix) {
				case SECTION_DELIMITER: // section done. stop reading
					sectionDone = true;
					break;
				default:
					inText = inText.replaceAll("\\\\t", "\t");
					inText = inText.replaceAll("\\\\n", "\n");
					sb.append(inText);
					sb.append("\n");
					inText = input.readLine();
				}
			}
			text = sb.toString();
			sb.returnToPool();
			sb = null;
		} catch (IOException ioe) {
			throw new RPGException(ErrorMessage.INTERNAL_ERROR, ioe);
		} catch (PooledException pe) {
			throw new RPGException(ErrorMessage.INTERNAL_ERROR, pe);
		}
		return text;
	}
	/**
	 * Sets the location of the library folder.
	 * @param folder the location to set
	 */
	public void setLibraryFolder(final String folder) {
		libraryFolder = folder;
	}
}
