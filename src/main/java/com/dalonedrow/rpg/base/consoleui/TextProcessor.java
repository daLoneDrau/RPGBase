package com.dalonedrow.rpg.base.consoleui;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dalonedrow.pooled.ListPool;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledList;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.constants.Gender;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * @author drau
 */
@SuppressWarnings("rawtypes")
public abstract class TextProcessor {
    /** the singleton instance of {@link TextProcessor}. */
    private static TextProcessor instance;
    /** the TAB string. */
    public static final String TAB = "        ";
    /** the length of a TAB string. */
    private static final int TAB_LEN = 8;
    /** the closing tag for a table border block. */
    public static final String TABLE_BORDER_CLOSE = "</tableBorder>";
    /** the opening tag for a table border block. */
    public static final String TABLE_BORDER_OPEN = "<tableBorder>";
    /** the closing tag for a table block. */
    public static final String TABLE_CLOSE = "</table>";
    /** the opening tag for a table block. */
    public static final String TABLE_OPEN = "<table>";
    /** the closing tag for a table title block. */
    public static final String TABLE_TITLE_CLOSE = "</title>";
    /** the opening tag for a table title block. */
    public static final String TABLE_TITLE_OPEN = "<title>";
    /**
     * Gets the one and only instance of {@link TextProcessor}.
     * @return {@link TextProcessor}
     */
    public static TextProcessor getInstance() {
        return TextProcessor.instance;
    }
    protected static void setInstance(final TextProcessor i) {
        TextProcessor.instance = i;
    }
    /** flag to display debugging. */
    private boolean debug = false;
    /** the default dialog width - 50 characters. */
    private final int defaultDlgWidth = 50;
    private String[] stringArr;
    /** Hidden constructor. */
    protected TextProcessor() {
        super();
    }
    /**
     * Gets
     * @param columns
     * @param headers
     * @param isNumbered
     * @return
     * @throws RPGException
     */
    public final String columnsToString(final String[][] columns,
            final String[] headers, final boolean isNumbered)
            throws RPGException {
        return columnsToString(columns, headers, " ", isNumbered);
    }
    public final String columnsToString(final String[][] columns,
            final String[] headers, final char seperator,
            final boolean isNumbered) throws RPGException {
        return columnsToString(columns, headers, new String(
                new char[] { seperator }), isNumbered);
    }
    public final String columnsToString(final String[][] columns,
            final String[] headers, final String seperator,
            final boolean isNumbered) throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        if (columns == null && headers == null) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "All data is null");
        }
        if (columns != null && headers != null
                && columns.length != headers.length) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Length of columns and headers does not match");
        }
        int len = 0, numIndent = 0, numRows = 0;
        if (headers != null) {
            len = Math.max(len, headers.length);
        } else {
            len = Math.max(len, columns.length);
        }
        if (len == 0) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Data is empty");
        }
        // determine widths of each column
        int[] colWidths = new int[len];
        for (int i = len - 1; i >= 0; i--) {
            if (headers != null && headers.length > 0) {
                colWidths[i] = Math.max(colWidths[i], headers[i].length());
            }
            if (columns != null && columns.length > 0) {
                numRows = Math.max(numRows, columns[i].length);
                for (int j = columns[i].length - 1; j >= 0; j--) {
                    colWidths[i] =
                            Math.max(colWidths[i], columns[i][j].length());
                }
            }
        }
        if (isNumbered) {
            numIndent = 3;
            if (numRows > 9) {
                numIndent++;
            }
        }
        try {
            // print the headers to a buffer
            if (headers != null && headers.length > 0) {
                for (int j = numIndent; j > 0; j--) {
                    sb.append(' ');
                }
                for (int i = 0; i < len; i++) {
                    // center the header
                    int pre = (colWidths[i] - headers[i].length()) / 2;
                    for (int j = pre; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(headers[i]);
                    int j = colWidths[i] - headers[i].length() - pre;
                    for (; j > 0; j--) {
                        sb.append(' ');
                    }
                    if (i + 1 < len) {
                        sb.append(seperator);
                    } else {
                        sb.append('\n');
                    }
                }
            }
            // print data to buffer
            if (columns != null && columns.length > 0) {
                for (int row = 0; row < numRows; row++) {
                    if (isNumbered) {
                        if (numRows > 9 && row < 9) {
                            sb.append(' ');
                        }
                        sb.append(row + 1);
                        sb.append(") ");
                    }
                    for (int col = 0; col < len; col++) {
                        if (row < columns[col].length) {
                            sb.append(columns[col][row]);
                            int i = colWidths[col] - columns[col][row].length();
                            for (; i > 0; i--) {
                                sb.append(' ');
                            }
                            if (col + 1 < len) {
                                sb.append(seperator);
                            } else {
                                sb.append('\n');
                            }
                        } else {
                            for (int i = colWidths[col]; i > 0; i--) {
                                sb.append(' ');
                            }
                            if (col + 1 < len) {
                                sb.append(seperator);
                            } else {
                                sb.append('\n');
                            }
                        }
                    }
                }
            }
        } catch (PooledException e) {
            throw new RPGException(
                    ErrorMessage.INTERNAL_BAD_ARGUMENT, e);
        }
        String s = sb.toString();
        sb.returnToPool();
        sb = null;
        return s;
    }
    /**
     * Finds the beginning and ending index of a tag block inside some text.
     * @param tagOpen the opening tag
     * @param tagClose the closing tag
     * @param text the text
     * @return <tt>int</tt>[]
     */
    private int[] findTagBlock(final String tagOpen, final String tagClose,
            final String text) {
        int lastIndex = text.indexOf(tagClose);
        int firstIndex = text.indexOf(tagOpen);
        if (lastIndex >= 0 && firstIndex >= 0) {
            while (true) {
                String s = text.substring(
                        firstIndex + tagOpen.length(), lastIndex);
                int blockStart = findTagBlock(tagOpen, tagClose, s)[0];
                if (blockStart >= 0) {
                    firstIndex = blockStart;
                } else {
                    break;
                }
            }
        }
        return new int[] { firstIndex, lastIndex };
    }
    private String getBlockValue(final String text, final String openTag,
            final int blockStart, final int blockEnd) {
        return text.substring(blockStart + openTag.length(), blockEnd);
    }
    public final String getCenteredText(final String string)
            throws RPGException {
        return this.getCenteredText(string, 0);
    }
    public final String getCenteredText(final String string, final int width)
            throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        try {
            final int maxCols = 100, four = 4;
            int innerWidth = width;
            if (innerWidth == 0) {
                innerWidth = maxCols;
            }
            if (string.length() > innerWidth) {
                String[] split = wrapText(string, innerWidth).split("\n");
                for (int i = 0, len = split.length; i < len; i++) {
                    int left = (innerWidth - split[i].length()) / 2;
                    for (int j = left; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(split[i]);
                    for (int j = innerWidth - left - split[i].length(); j > 0;
                            j--) {
                        sb.append(' ');
                    }
                    if (i + 1 < len) {
                        sb.append('\n');
                    }
                }
            } else {
                int left = (innerWidth - string.length()) / 2;
                for (int j = left; j > 0; j--) {
                    sb.append(' ');
                }
                sb.append(string);
                for (int j = innerWidth - left - string.length(); j > 0; j--) {
                    sb.append(' ');
                }
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        String s = sb.toString();
        sb.returnToPool();
        sb = null;
        return s;
    }
    /**
     * Gets a list of {@link String}s and sorts them as columns.
     * @param numberOfColumns the number of columns
     * @param list the list of {@link String}s
     * @param separator the column separator
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public String getSelectionsAsColumns(final int numberOfColumns,
            final String[] list, final String separator) throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        String text = null;
        int[] columnLengths = new int[numberOfColumns];
        for (int i = 0, currentColumn = 0, len = list.length; i < len; i++) {
            list[i] = list[i].replaceAll("\t", TextProcessor.TAB);
            columnLengths[currentColumn] =
                    Math.max(columnLengths[currentColumn], list[i].length());
            currentColumn++;
            if (currentColumn >= columnLengths.length) {
                currentColumn = 0;
            }
        }
        try {
            boolean prependSeperator = false;
            for (int i = 0, currentColumn = 0, len =
                    list.length; i < len; i++) {
                if (prependSeperator) {
                    sb.append(separator);
                }
                sb.append(list[i]);
                for (int j =
                        columnLengths[currentColumn]
                                - list[i].length();
                        j > 0; j--) {
                    sb.append(' ');
                }
                currentColumn++;
                if (currentColumn >= columnLengths.length) {
                    currentColumn = 0;
                    prependSeperator = false;
                    sb.append('\n');
                } else {
                    prependSeperator = true;
                }
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        text = sb.toString();
        sb.returnToPool();
        sb = null;
        return text;
    }
    /**
     * Gets a list of {@link String}s and sorts them as columns separated by a
     * space.
     * @param cols the list of {@link String}s
     * @param bordered if <tt>true</tt>, the columns will be bordered on both
     *            sides by the '|' character, otherwise there is no border
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public String getSelectionsAsColumns(final String[] cols,
            final boolean bordered) throws RPGException {
        return getSelectionsAsColumns(cols, " ", bordered, '|');
    }
    /**
     * Gets a list of {@link String}s and sorts them as columns.
     * @param cols the list of {@link String}s
     * @param seperator the separator between columns
     * @param bordered if <tt>true</tt>, the columns will be bordered on both
     *            sides by the '|' character, otherwise there is no border
     * @return {@link String}
     * @throws RPGException
     */
    public String getSelectionsAsColumns(final String[] cols,
            final char seperator, final boolean bordered) throws RPGException {
        return getSelectionsAsColumns(cols,
                new String(new char[] { seperator }), bordered, '|');
    }
    /**
     * Gets a list of {@link String}s and sorts them as columns.
     * @param cols the list of {@link String}s
     * @param seperator the seperator between columns
     * @param bordered if <tt>true</tt>, the columns will be bordered on both
     *            sides by the '|' character, otherwise there is no border
     * @return {@link String}
     * @throws RPGException
     */
    public String getSelectionsAsColumns(final String[] cols,
            final String seperator, final boolean bordered)
            throws RPGException {
        return getSelectionsAsColumns(cols, seperator, bordered, '|');
    }
    /**
     * Gets a list of {@link String}s and sorts them as columns.
     * @param cols the list of {@link String}s
     * @param seperator the seperator between columns
     * @param bordered if <tt>true</tt>, the columns will be bordered on both
     *            sides by the border character, otherwise there is no border
     * @param borderCharacter the character used for the border
     * @return {@link String}
     */
    public String getSelectionsAsColumns(final String[] cols,
            final String seperator, final boolean bordered,
            final char borderCharacter) throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        String text = null;
        int maxLen1 = 0, maxLen2 = 0;
        for (int i = 0, len = cols.length; i < len; i += 2) {
            cols[i] = cols[i].replaceAll("\t", TextProcessor.TAB);
            maxLen1 = Math.max(maxLen1, cols[i].length());
            if (i + 1 < cols.length) {
                maxLen2 = Math.max(maxLen2, cols[i + 1].length());
            }
        }
        try {
            for (int i = 0, len = cols.length; i < len; i += 2) {
                if (bordered) {
                    sb.append(borderCharacter);
                    sb.append(' ');
                }
                sb.append(cols[i]);
                for (int j = maxLen1 - cols[i].length(); j > 0; j--) {
                    sb.append(' ');
                }
                sb.append(seperator);
                if (i + 1 < cols.length) {
                    sb.append(cols[i + 1]);
                    for (int j = maxLen2 - cols[i + 1].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                } else {
                    for (int j = maxLen2; j > 0; j--) {
                        sb.append(' ');
                    }
                }
                if (bordered) {
                    sb.append(' ');
                    sb.append(borderCharacter);
                }
                sb.append('\n');
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        text = sb.toString();
        sb.returnToPool();
        sb = null;
        return text;
    }
    /**
     * Creates the markup for a table block.
     * @param border the table border
     * @param title the table's title. can be null
     * @param tableWidth the table's width; can be null
     * @param body the list of lines making up the body
     * @return {@link String}
     * @throws RPGException should never occur
     */
    private String getTableMarkup(final char border, final String title,
            final Integer tableWidth, final String[] body) throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        try {
            if (border == '|') {
                sb.append("|-");
            } else {
                sb.append(border);
                sb.append(border);
            }
            int maxLen;
            if (tableWidth != null) {
                maxLen = tableWidth;
            } else {
                maxLen = 0;
                for (int i = body.length - 1; i >= 0; i--) {
                    if (body[i].length() > 0 && body[i].charAt(0) == '|'
                            && body[i].charAt(body[i].length() - 1) == '|') {
                        maxLen = Math.max(maxLen, body[i].length() - 4);
                    } else {
                        maxLen = Math.max(maxLen, body[i].length());
                    }
                }
            }
            if (title != null && title.length() > 0) {
                for (int i = maxLen - title.length(); i > 0; i--) {
                    if (border == '|') {
                        sb.append('-');
                    } else {
                        sb.append(border);
                    }
                }
                if (border == '|') {
                    sb.append(title.replaceAll(" ", "-"));
                } else {
                    sb.append(title.replaceAll(" ", new String(
                            new char[] { border })));
                }
            } else {
                for (int i = maxLen; i > 0; i--) {
                    if (border == '|') {
                        sb.append('-');
                    } else {
                        sb.append(border);
                    }
                }
            }
            if (border == '|') {
                sb.append("-|\n");
            } else {
                sb.append(border);
                sb.append(border);
                sb.append('\n');
            }
            for (int i = 0, len = body.length; i < len; i++) {
                if (body[i].length() > 0 && body[i].charAt(0) == '|'
                        && body[i].charAt(body[i].length() - 1) == '|') {
                    // line is already bordered - add it
                    if (border == '|') {
                        sb.append(body[i]);
                    } else {
                        sb.append(border);
                        sb.append(' ');
                        sb.append(body[i].substring(2, body[i].length() - 2));
                        sb.append(' ');
                        sb.append(border);
                    }
                } else {
                    sb.append(border);
                    sb.append(' ');
                    sb.append(body[i]);
                    for (int j = maxLen - body[i].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(' ');
                    sb.append(border);
                }
                sb.append('\n');
            }
            if (border == '|') {
                sb.append("|-");
            } else {
                sb.append(border);
                sb.append(border);
            }
            for (int i = maxLen; i > 0; i--) {
                if (border == '|') {
                    sb.append('-');
                } else {
                    sb.append(border);
                }
            }
            if (border == '|') {
                sb.append("-|\n");
            } else {
                sb.append(border);
                sb.append(border);
                sb.append('\n');
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        String table = sb.toString();
        sb.returnToPool();
        sb = null;
        return table;
    }
    /**
     * Processes a group of text, replacing all tokens.
     * @param playerIO the player IO. can be null
     * @param targetIO the target IO. can be null
     * @param generalText any general text to be spliced into the text
     * @param conditions the number of conditions to be checked for
     * @param text the text
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public String processText(final BaseInteractiveObject playerIO,
            final BaseInteractiveObject targetIO,
            final String generalText, final boolean[] conditions,
            final String text) throws RPGException {
        return processText(playerIO, targetIO, new String[] { generalText },
                conditions, text);
    }
    /**
     * Processes a group of text, replacing all tokens.
     * @param playerIO the player IO. can be null
     * @param targetIO the target IO. can be null
     * @param generalText any general text to be spliced into the text
     * @param text the text
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public String processText(final BaseInteractiveObject playerIO,
            final BaseInteractiveObject targetIO,
            final String generalText, final String text) throws RPGException {
        return processText(playerIO, targetIO, new String[] { generalText },
                null, text);
    }
    /**
     * Processes a group of text, replacing all tokens.
     * @param playerIO the player IO. can be null
     * @param targetIO the target IO. can be null
     * @param generalText any general text to be spliced into the text
     * @param conditions the number of conditions to be checked for
     * @param text the text
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public String processText(final BaseInteractiveObject playerIO,
            final BaseInteractiveObject targetIO,
            final String[] generalText, final boolean[] conditions,
            final String text) throws RPGException {
        if (debug) {
            System.out.println("processText");
        }
        String tokenized = text;
        tokenized = tokenized.replaceAll("\t", TextProcessor.TAB);
        if (tokenized.indexOf("<ioName>") > -1) {
            tokenized =
                    tokenized.replaceAll("<ioName>", new String(playerIO
                            .getPCData().getName()));
        }
        if (tokenized.indexOf("<ioNameApos>") > -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            String name = new String(playerIO.getPCData().getName());
            try {
                sb.append(name);
                if (name.toLowerCase().charAt(sb.length() - 1) == 's'
                        || name.toLowerCase().charAt(sb.length() - 1) == 'x'
                        || name.toLowerCase().charAt(sb.length() - 1) == 'z') {
                    sb.append('\'');
                } else {
                    sb.append("'s");
                }
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            tokenized = tokenized.replaceAll("<ioNameApos>", sb.toString());
            sb.returnToPool();
            sb = null;
        }
        if (tokenized.indexOf("<ioGenObj>") > -1) {
            String genObj = new String(
                    Gender.GENDER_OBJECTIVE[playerIO.getPCData()
                            .getGender()]);
            tokenized = tokenized.replaceAll("<ioGenObj>", genObj);
            genObj = null;
        }
        if (tokenized.indexOf("<ioGenPoss>") > -1) {
            String genPoss = new String(
                    Gender.GENDER_POSSESSIVE[playerIO.getPCData()
                            .getGender()]);
            tokenized = tokenized.replaceAll("<ioGenPoss>", genPoss);
            genPoss = null;
        }
        if (tokenized.indexOf("<ioGenPossObj>") > -1) {
            String genPossObj = new String(
                    Gender.GENDER_POSSESSIVE_OBJECTIVE[playerIO
                            .getPCData()
                            .getGender()]);
            tokenized = tokenized.replaceAll("<ioGenPossObj>", genPossObj);
            genPossObj = null;
        }
        if (tokenized.indexOf("<ioGenPro>") > -1) {
            String genPro = new String(
                    Gender.GENDER_PRONOUN[playerIO.getPCData()
                            .getGender()]);
            tokenized = tokenized.replaceAll("<ioGenPossObj>", genPro);
            genPro = null;
        }
        if (tokenized.indexOf("<tioName>") > -1) {
            if (targetIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
                tokenized =
                        tokenized.replaceAll("<tioName>", new String(targetIO
                                .getNPCData().getName()));
            } else if (targetIO.hasIOFlag(IoGlobals.IO_02_ITEM)) {
                tokenized =
                        tokenized.replaceAll("<tioName>", new String(targetIO
                                .getItemData().getItemName()));
            }
        }
        if (tokenized.indexOf("<tioNameApos>") > -1) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                String name = "";
                if (targetIO.hasIOFlag(IoGlobals.IO_03_NPC)) {
                    name = new String(targetIO.getNPCData().getName());
                } else if (targetIO.hasIOFlag(IoGlobals.IO_02_ITEM)) {
                    name = new String(targetIO.getItemData().getItemName());
                }
                sb.append(name);
                if (name.toLowerCase().charAt(sb.length() - 1) == 's'
                        || name.toLowerCase().charAt(sb.length() - 1) == 'x'
                        || name.toLowerCase().charAt(sb.length() - 1) == 'z') {
                    sb.append('\'');
                } else {
                    sb.append("'s");
                }
                tokenized = tokenized.replaceAll(
                        "<tioNameApos>", sb.toString());
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            sb.returnToPool();
            sb = null;
        }
        Pattern r = Pattern.compile("<genText>");
        if (generalText != null && generalText.length > 0) {
            for (int i = 0, len = generalText.length; i < len; i++) {
                Matcher match = r.matcher(tokenized);
                if (match.find()) {
                    tokenized = match.replaceFirst(match.group().replaceAll(
                            "<genText>",
                            generalText[i].replaceAll("\t",
                                    TextProcessor.TAB)));
                }
            }
        }
        r = Pattern.compile("\n<ifCond>[^>]+</ifCond>");
        if (conditions != null && conditions.length > 0) {
            for (int i = 0, len = conditions.length; i < len; i++) {
                Matcher match = r.matcher(tokenized);
                if (match.find()) {
                    if (conditions[i]) {
                        tokenized =
                                match.replaceFirst(match.group().replaceAll(
                                        "<ifCond>", "").replaceAll("</ifCond>",
                                                ""));
                    } else {
                        tokenized = match.replaceFirst("");
                    }
                }
            }
        }
        r = Pattern.compile("<ifCond>[^>]+</ifCond>");
        if (conditions != null && conditions.length > 0) {
            for (int i = 0, len = conditions.length; i < len; i++) {
                Matcher match = r.matcher(tokenized);
                if (match.find()) {
                    if (conditions[i]) {
                        tokenized =
                                match.replaceFirst(match.group().replaceAll(
                                        "<ifCond>", "").replaceAll("</ifCond>",
                                                ""));
                    } else {
                        tokenized = match.replaceFirst("");
                    }
                }
            }
        }
        // use this pattern to match with line breaks
        r = Pattern.compile("<table>(.\n*)*</table>");
        Matcher match = r.matcher(tokenized);
        try {
            while (match.find()) {
                String original = match.group();
                Pattern p = Pattern.compile("<title>(.|\n)*?</title>");
                Matcher m2 = p.matcher(original);
                String table = original.substring("<table>".length());
                table = table.substring(0,
                        table.length() - "</table>".length());
                String title = "";
                if (m2.find()) {
                    title = m2.group().substring("<title>".length());
                    title =
                            title.substring(0, title.length()
                                    - "</title>".length());
                    table = table.replace(m2.group(), "");
                }
                // set border character
                char border = '|';
                p = Pattern.compile("<tableBorder>(.|\n)*?</tableBorder>");
                m2 = p.matcher(original);
                if (m2.find()) {
                    String b = m2.group().substring("<tableBorder>".length());
                    b = b.substring(0, b.length() - "</tableBorder>".length());
                    border = b.charAt(0);
                    table = table.replace(m2.group(), "");
                }
                String[] lines = table.split("\n");
                int maxLen = 0;
                for (int i = lines.length - 1; i >= 0; i--) {
                    if (lines[i].length() > 0 && lines[i].charAt(0) == '|'
                            && lines[i].charAt(lines[i].length() - 1) == '|') {
                        maxLen = Math.max(maxLen, lines[i].length() - 4);
                    } else {
                        maxLen = Math.max(maxLen, lines[i].length());
                    }
                }
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                if (border == '|') {
                    sb.append("|-");
                } else {
                    sb.append(border);
                    sb.append(border);
                }
                if (title.length() > 0) {
                    for (int i = maxLen - title.length(); i > 0; i--) {
                        if (border == '|') {
                            sb.append('-');
                        } else {
                            sb.append(border);
                        }
                    }
                    if (border == '|') {
                        sb.append(title.replaceAll(" ", "-"));
                    } else {
                        sb.append(title.replaceAll(" ", new String(
                                new char[] { border })));
                    }
                } else {
                    for (int i = maxLen; i > 0; i--) {
                        if (border == '|') {
                            sb.append('-');
                        } else {
                            sb.append(border);
                        }
                    }
                }
                if (border == '|') {
                    sb.append("-|\n");
                } else {
                    sb.append(border);
                    sb.append(border);
                    sb.append('\n');
                }
                for (int i = 0, len = lines.length; i < len; i++) {
                    if (lines[i].length() > 0 && lines[i].charAt(0) == '|'
                            && lines[i].charAt(lines[i].length() - 1) == '|') {
                        // line is already bordered - add it
                        if (border == '|') {
                            sb.append(lines[i]);
                        } else {
                            sb.append(border);
                            sb.append(' ');
                            sb.append(lines[i].substring(2,
                                    lines[i].length() - 2));
                            sb.append(' ');
                            sb.append(border);
                        }
                    } else {
                        sb.append(border);
                        sb.append(' ');
                        sb.append(lines[i]);
                        for (int j = maxLen - lines[i].length(); j > 0; j--) {
                            sb.append(' ');
                        }
                        sb.append(' ');
                        sb.append(border);
                    }
                    sb.append('\n');
                }
                if (border == '|') {
                    sb.append("|-");
                } else {
                    sb.append(border);
                    sb.append(border);
                }
                for (int i = maxLen; i > 0; i--) {
                    if (border == '|') {
                        sb.append('-');
                    } else {
                        sb.append(border);
                    }
                }
                if (border == '|') {
                    sb.append("-|\n");
                } else {
                    sb.append(border);
                    sb.append(border);
                    sb.append('\n');
                }
                table = sb.toString();
                sb.returnToPool();
                sb = null;
                tokenized = tokenized.replace(original, table);
            }
        } catch (StackOverflowError soe) {
            while (true) {
                // get table block
                int[] block =
                        findTagBlock(TextProcessor.TABLE_OPEN,
                                TextProcessor.TABLE_CLOSE, tokenized);
                if (block[0] >= 0 && block[1] >= 0) {
                    String tableBlock =
                            getBlockValue(tokenized, TextProcessor.TABLE_OPEN,
                                    block[0], block[1]);
                    String title = null;
                    int[] titleBlock =
                            findTagBlock(TextProcessor.TABLE_TITLE_OPEN,
                                    TextProcessor.TABLE_TITLE_CLOSE,
                                    tableBlock);
                    if (titleBlock[0] >= 0 && titleBlock[1] >= 0) {
                        title =
                                getBlockValue(tableBlock,
                                        TextProcessor.TABLE_TITLE_OPEN,
                                        titleBlock[0], titleBlock[1]);
                        tableBlock =
                                removeBlock(tableBlock,
                                        TextProcessor.TABLE_TITLE_CLOSE,
                                        titleBlock[0], titleBlock[1]);
                    }
                    char border = '|';
                    int[] borderBlock =
                            findTagBlock(TextProcessor.TABLE_BORDER_OPEN,
                                    TextProcessor.TABLE_BORDER_CLOSE,
                                    tableBlock);
                    if (borderBlock[0] >= 0 && borderBlock[1] >= 0) {
                        border =
                                getBlockValue(tableBlock,
                                        TextProcessor.TABLE_BORDER_OPEN,
                                        borderBlock[0], borderBlock[1]).charAt(
                                                0);
                        tableBlock =
                                removeBlock(tableBlock,
                                        TextProcessor.TABLE_BORDER_CLOSE,
                                        borderBlock[0], borderBlock[1]);
                    }
                    tokenized =
                            replaceBlock(tokenized, getTableMarkup(border,
                                    title, null, tableBlock.split("\n")),
                                    TextProcessor.TABLE_CLOSE, block[0],
                                    block[1]);
                } else {
                    break;
                }
            }
            soe.printStackTrace();
            throw soe;
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        return tokenized;
    }
    /**
     * Processes a group of text, replacing all tokens.
     * @param playerIO the player IO. can be null
     * @param targetIO the target IO. can be null
     * @param generalText any general text to be spliced into the text
     * @param text the text
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public final String processText(final BaseInteractiveObject playerIO,
            final BaseInteractiveObject targetIO,
            final String[] generalText, final String text) throws RPGException {
        return processText(playerIO, targetIO, generalText, null, text);
    }
    /**
     * Removes a block element from a text string.
     * @param text the text
     * @param closeTag the element's closing tag
     * @param blockStart the index where the block element starts
     * @param blockEnd the index where the block element ends
     * @return {@link String}
     * @throws RPGException should never happen
     */
    private String removeBlock(final String text, final String closeTag,
            final int blockStart, final int blockEnd) throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        try {
            // add all text before the tag block
            sb.append(text.substring(0, blockStart));
            // add all text after the tag block
            sb.append(text.substring(blockEnd + closeTag.length()));
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT, e);
        }
        String s = sb.toString();
        sb.returnToPool();
        sb = null;
        return s;
    }
    /**
     * Replaces a block element in a text string.
     * @param text the text
     * @param replacement the replacement text
     * @param closeTag the element's closing tag
     * @param blockStart the index where the block element starts
     * @param blockEnd the index where the block element ends
     * @return {@link String}
     * @throws RPGException should never happen
     */
    private String replaceBlock(final String text, final String replacement,
            final String closeTag, final int blockStart, final int blockEnd)
            throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        // add all text before the tag block
        try {
            sb.append(text.substring(0, blockStart));
            // append replacement text
            sb.append(replacement);
            // add all text after the tag block
            sb.append(text.substring(blockEnd + closeTag.length()));
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT, e);
        }
        String s = sb.toString();
        sb.returnToPool();
        sb = null;
        return s;
    }
    /**
     * Sets the value of the debug.
     * @param debug the new value to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    /**
     * Wraps the text for display in the console UI.
     * @param text the text
     * @param lineLength the maximum line length
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public final String wrapText(final String text, final int lineLength)
            throws RPGException {
        return wrapText(text, lineLength, false, false);
    }
    /**
     * Wraps the text for display in the console UI.
     * @param text the text
     * @param lineLength the maximum line length
     * @param bordered if <tt>true</tt>, the text is bordered, otherwise it is
     *            not
     * @param starred if <tt>true</tt>, the text is starred, otherwise it is not
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    public final String wrapText(final String text, final int lineLength,
            final boolean bordered, final boolean starred) throws RPGException {
        final int maxCols = 100, four = 4;
        stringArr = new String[0];
        if (text != null && text.length() > 0) {
            int innerWidth = lineLength;
            if (innerWidth == 0) {
                innerWidth = maxCols;
            }
            if (bordered || starred) {
                innerWidth -= four;
            }
            // all tabs replaced with spaces
            StringTokenizer tokenizer =
                    new StringTokenizer(new String(text), " \t\n\r\f", true);
            PooledList words =
                    ListPool.getInstance().getList();
            try {
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    words.add(token);
                }
                // start a line of text
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                // init measurements
                double currentWidth = 0d;
                String nextWord = "";
                int nextWordLength = 0;
                int spaceWidth = 1;
                if (bordered) {
                    sb.append("| ");
                } else if (starred) {
                    sb.append("* ");
                }
                for (int i = 0; i < words.size(); i++) {
                    if (debug) {
                        System.out.println(
                                "word " + i + "::" + words.get(i) + "::");
                    }
                    if (((String) words.get(i)).charAt(0) == ' ') {
                        nextWord = words.get(i);
                        nextWordLength = spaceWidth;
                        int newWidth = (int) currentWidth + nextWordLength;
                        if (newWidth > innerWidth) {
                            // end current line
                            if (bordered) {
                                for (int j =
                                        (int) (innerWidth
                                                - currentWidth);
                                        j > 0; j--) {
                                    sb.append(' ');
                                }
                                sb.append(" |");
                            } else if (starred) {
                                for (int j =
                                        (int) (innerWidth
                                                - currentWidth);
                                        j > 0; j--) {
                                    sb.append(' ');
                                }
                                sb.append(" *");
                            }
                            stringArr =
                                    ArrayUtilities.getInstance()
                                            .extendArray(sb.toString(),
                                                    stringArr);
                            sb.setLength(0);
                            if (bordered) {
                                sb.append("| ");
                            } else if (starred) {
                                sb.append("* ");
                            }
                            currentWidth = 0;
                            nextWord = "";
                            nextWordLength = 0;
                        } else {
                            sb.append(nextWord);
                            currentWidth = newWidth;
                        }
                    } else if (((String) words.get(i)).charAt(0) == '\t') {
                        // TAB
                        nextWord = TextProcessor.TAB;
                        nextWordLength =
                                spaceWidth * TextProcessor.TAB.length();
                        int newWidth = (int) currentWidth + nextWordLength;
                        if (newWidth > innerWidth) {
                            // end current line
                            if (bordered) {
                                for (int j =
                                        (int) (innerWidth
                                                - currentWidth);
                                        j > 0; j--) {
                                    sb.append(' ');
                                }
                                sb.append(" |");
                            } else if (starred) {
                                for (int j =
                                        (int) (innerWidth
                                                - currentWidth);
                                        j > 0; j--) {
                                    sb.append(' ');
                                }
                                sb.append(" *");
                            }
                            stringArr =
                                    ArrayUtilities.getInstance()
                                            .extendArray(sb.toString(),
                                                    stringArr);
                            sb.setLength(0);
                            if (bordered) {
                                sb.append("| ");
                            } else if (starred) {
                                sb.append("* ");
                            }
                            sb.append(nextWord);
                            currentWidth = nextWordLength;
                        } else {
                            sb.append(nextWord);
                            currentWidth = newWidth;
                        }
                    } else if (((String) words.get(i)).charAt(0) == '\n') {
                        // NEWLINE
                        if (sb.length() == 0) {
                            sb.append(" ");
                        }
                        if (bordered) {
                            for (int j =
                                    (int) (innerWidth
                                            - currentWidth);
                                    j > 0; j--) {
                                sb.append(' ');
                            }
                            sb.append(" |");
                        } else if (starred) {
                            for (int j =
                                    (int) (innerWidth
                                            - currentWidth);
                                    j > 0; j--) {
                                sb.append(' ');
                            }
                            sb.append(" *");
                        }
                        stringArr =
                                ArrayUtilities.getInstance()
                                        .extendArray(sb.toString(), stringArr);
                        sb.setLength(0);
                        if (bordered) {
                            sb.append("| ");
                        } else if (starred) {
                            sb.append("* ");
                        }
                        currentWidth = 0;
                        nextWord = "";
                        nextWordLength = 0;
                    } else {
                        nextWord = words.get(i);
                        nextWordLength = nextWord.length();
                        int newWidth = (int) currentWidth + nextWordLength;
                        if (newWidth > innerWidth) {
                            // end current line
                            if (bordered) {
                                for (int j =
                                        (int) (innerWidth
                                                - currentWidth);
                                        j > 0; j--) {
                                    sb.append(' ');
                                }
                                sb.append(" |");
                            } else if (starred) {
                                for (int j =
                                        (int) (innerWidth
                                                - currentWidth);
                                        j > 0; j--) {
                                    sb.append(' ');
                                }
                                sb.append(" *");
                            }
                            stringArr =
                                    ArrayUtilities.getInstance()
                                            .extendArray(sb.toString(),
                                                    stringArr);
                            sb.setLength(0);
                            if (bordered) {
                                sb.append("| ");
                            } else if (starred) {
                                sb.append("* ");
                            }
                            sb.append((String) words.get(i));
                            currentWidth = nextWordLength;
                        } else {
                            sb.append((String) words.get(i));
                            currentWidth = newWidth;
                        }
                    }
                } // end for
                if (sb.length() > 0) { // add the last line
                    if (bordered) {
                        for (int j =
                                (int) (innerWidth - currentWidth); j > 0; j--) {
                            sb.append(' ');
                        }
                        sb.append(" |");
                    } else if (starred) {
                        for (int j =
                                (int) (innerWidth - currentWidth); j > 0; j--) {
                            sb.append(' ');
                        }
                        sb.append(" *");
                    }
                    stringArr =
                            ArrayUtilities.getInstance().extendArray(
                                    sb.toString(), stringArr);
                }
                ListPool.getInstance().returnObject(words);
                StringBuilderPool.getInstance().returnObject(sb);
                sb = null;
                words = null;
                tokenizer = null;
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
        }
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        for (int i = 0, len = stringArr.length; i < len; i++) {
            try {
                sb.append(stringArr[i]);
                sb.append('\n');
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
        }
        String s = sb.toString();
        sb.returnToPool();
        sb = null;
        return s;
    }
}
