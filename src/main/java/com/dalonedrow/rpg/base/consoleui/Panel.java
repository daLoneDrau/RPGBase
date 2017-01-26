package com.dalonedrow.rpg.base.consoleui;

import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * @author drau
 */
public abstract class Panel {
    /** direction bottom. */
    public static final int BOTTOM = 3;
    /** direction center. */
    public static final int CENTER = 4;
    /** direction left. */
    public static final int LEFT = 0;
    /** the left-side padding. */
    public static final int LEFT_PADDING = 2;
    /** direction right. */
    public static final int RIGHT = 1;
    /** the right-side padding. */
    public static final int RIGHT_PADDING = 2;
    /** direction top. */
    public static final int TOP = 2;
    /** flag indicating whether the panel is bordered. */
    private final boolean bordered;
    /** the text displayed. */
    private char[] content;
    /** the text displayed in the UI. */
    private String displayText;
    /** the number of lines in the panel, including borders. */
    private final int height;
    /** if bordered, the panel's title. */
    private final char[] title;
    /** the number of characters in the panel, including borders and padding. */
    private final int width;
    /**
     * Creates a new instance of {@link Panel}.
     * @param w the number of characters in the panel, including borders and
     *            padding
     * @param b flag indicating whether the panel is bordered
     * @param h the number of lines in the panel, including borders
     */
    public Panel(final int w, final boolean b, final int h) {
        this(w, b, h, (char[]) null, null);
    }
    /**
     * Creates a new instance of {@link Panel}.
     * @param w the number of characters in the panel, including borders and
     *            padding
     * @param b flag indicating whether the panel is bordered
     * @param h the number of lines in the panel, including borders
     * @param t the text displayed
     */
    public Panel(final int w, final boolean b, final int h, final char[] t) {
        this(w, b, h, t, null);
    }
    /**
     * Creates a new instance of {@link Panel}.
     * @param w the number of characters in the panel, including borders and
     *            padding
     * @param b flag indicating whether the panel is bordered
     * @param h the number of lines in the panel, including borders
     * @param txt the text displayed
     * @param ttl if bordered, the panel's title
     */
    public Panel(final int w, final boolean b, final int h, final char[] txt,
            final char[] ttl) {
        width = w;
        bordered = b;
        height = h;
        content = txt;
        title = ttl;
    }
    /**
     * Creates a new instance of {@link Panel}.
     * @param w the number of characters in the panel, including borders and
     *            padding
     * @param b flag indicating whether the panel is bordered
     * @param h the number of lines in the panel, including borders
     * @param t the text displayed
     */
    public Panel(final int w, final boolean b, final int h, final String t) {
        this(w, b, h, t.toCharArray(), null);
    }
    /**
     * Creates a new instance of {@link Panel}.
     * @param w the number of characters in the panel, including borders and
     *            padding
     * @param b flag indicating whether the panel is bordered
     * @param h the number of lines in the panel, including borders
     * @param txt the text displayed
     * @param ttl if bordered, the panel's title
     */
    public Panel(final int w, final boolean b, final int h, final String txt,
            final String ttl) {
        this(w, b, h, txt.toCharArray(), ttl.toCharArray());
    }
    /**
     * Gets the value for the content.
     * @return {@link char[]}
     */
    public String getContent() {
        return new String(content);
    }
    /**
     * Gets the text displayed in the UI.
     * @return {@link String}
     * @throws RPGException
     */
    public String getDisplayText() throws RPGException {
        if (displayText == null) {
            processText();
        }
        return displayText;
    }
    /**
     * Gets the panel's title.
     * @return {@link String}
     */
    public String getTitle() {
        return new String(title);
    }
    protected abstract String getTitledTableMarkup();
    protected abstract String getUnTitledTableMarkup();
    /**
     * Joins two panels.
     * @param other the second panel
     * @param direction the direction in which this panel is placed next to the
     *            second panel
     * @param align the direction in which the panels are aligned, if they are
     *            of different sizes
     * @throws RPGException if an error occurs
     */
    public void join(final Panel other, final int direction, final int align)
            throws RPGException {
        if ((direction == LEFT
                || direction == RIGHT)
                && (align == LEFT
                        || align == RIGHT)) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Cannot join two panels left and right and anchor them "
                            + "left or right also - must be TOP, BOTTOM,"
                            + " or CENTER");
        } else if ((direction == TOP
                || direction == BOTTOM)
                && (align == TOP
                        || align == BOTTOM)) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "Cannot join two panels top and bottom and anchor them "
                            + "top or bottom also - must be LEFT, RIGHT,"
                            + " or CENTER");
        }
        // validate the join
        String[] t1 = getDisplayText().split("\n");
        String[] t2 = other.getDisplayText().split("\n");
        switch (direction) {
        case Panel.LEFT:
        case Panel.RIGHT:
            int maxWidth = 0;
            for (int i = Math.max(t1.length, t2.length) - 1; i >= 0; i--) {
                int lineWidth = 0;
                if (i < t1.length) {
                    lineWidth += t1[i].length();
                }
                if (i < t2.length) {
                    lineWidth += t2[i].length();
                }
                maxWidth = Math.max(maxWidth, lineWidth);
            }
            // remove one space for border removal
            if (bordered && other.bordered) {
                maxWidth--;
            }
            if (maxWidth > ProjectConstants.getInstance().getConsoleWidth()) {
                throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT,
                        "Joined columns are too wide for screen");
            }
            break;
        case Panel.TOP:
        case Panel.BOTTOM:
            int maxHeight = t1.length + t2.length;
            // remove one space for border removal
            if (bordered && other.bordered) {
                maxHeight--;
            }
            if (maxHeight > ProjectConstants.getInstance().getConsoleHeight()) {
                throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT,
                        "Joined columns are too high for screen");
            }
            break;
        default:
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                    "invalid join direction");
        }
        // join the two panels
        switch (direction) {
        case Panel.LEFT:
            if (bordered && other.bordered) {
                // the other panel joins this one on its left side
                joinLeftorRightBordered(other, align, true);
            } else {
                // the other panel joins this one on its left side
                joinLeftOrRight(other, align, true);
            }
            break;
        case Panel.RIGHT:
            if (bordered && other.bordered) {
                // the other panel joins this one on its left side
                joinLeftorRightBordered(other, align, false);
            } else {
                // the other panel joins this one on its left side
                joinLeftOrRight(other, align, false);
            }
            break;
        case Panel.TOP:
            if (bordered && other.bordered) {
                // the other panel joins this one on its left side
                joinTopOrBottomBordered(other, align, true);
            } else {
                // the other panel joins this one on its left side
                joinTopOrBottom(other, align, true);
            }
            break;
        case Panel.BOTTOM:
            if (bordered && other.bordered) {
                // the other panel joins this one on its left side
                joinTopOrBottomBordered(other, align, false);
            } else {
                // the other panel joins this one on its left side
                joinTopOrBottom(other, align, false);
            }
            break;
        }
    }
    /**
     * Joins two panels on the left or right side.
     * @param p the second panel
     * @param align the direction in which the panels are aligned, if they are
     *            of different sizes
     * @param isLeftJoin if <tt>true</tt>, this panel is to the left of the
     *            second panel; otherwise it is on the right
     * @throws RPGException if an error occurs
     */
    private void joinLeftOrRight(final Panel p, final int align,
            final boolean isLeftJoin) throws RPGException {
        String[] t1, t2;
        if (isLeftJoin) {
            t1 = getDisplayText().split("\n");
            t2 = p.getDisplayText().split("\n");
        } else {
            t2 = getDisplayText().split("\n");
            t1 = p.getDisplayText().split("\n");
        }
        int leftWidth = 0, rightWidth = 0, totalWidth = 0;
        for (int i = t1.length - 1; i >= 0; i--) {
            leftWidth = Math.max(leftWidth, t1[i].length());
        }
        for (int i = t2.length - 1; i >= 0; i--) {
            rightWidth = Math.max(rightWidth, t2[i].length());
        }
        totalWidth = leftWidth + rightWidth;
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        String[] lines = new String[0];
        int maxLines = Math.max(t1.length, t2.length);
        try {
            switch (align) {
            case TOP:
                for (int i = 0, len = maxLines; i < len; i++) {
                    sb.setLength(0);
                    if (i < t1.length) {
                        sb.append(t1[i]);
                    }
                    for (int j =
                            leftWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    if (i < t2.length) {
                        sb.append(t2[i]);
                    }
                    for (int j =
                            totalWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    lines = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), lines);
                }
                break;
            case BOTTOM:
                for (int i = 0, len = maxLines; i < len; i++) {
                    sb.setLength(0);
                    if (t1.length - 1 - i >= 0) {
                        sb.append(t1[t1.length - 1 - i]);
                    }
                    for (int j =
                            leftWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    if (t2.length - 1 - i >= 0) {
                        sb.append(t2[t2.length - 1 - i]);
                    }
                    for (int j =
                            totalWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    lines = ArrayUtilities.getInstance().prependArray(
                            sb.toString(), lines);
                }
                break;
            case CENTER:
                int off1 = (maxLines - t1.length) / 2;
                int off2 = (maxLines - t2.length) / 2;
                for (int i = maxLines - 1; i >= 0; i--) {
                    sb.setLength(0);
                    if (i - off1 < t1.length
                            && i - off1 >= 0) {
                        sb.append(t1[i - off1]);
                    }
                    for (int j =
                            leftWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    if (i - off2 < t2.length
                            && i - off2 >= 0) {
                        sb.append(t2[i - off2]);
                    }
                    for (int j =
                            totalWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    lines = ArrayUtilities.getInstance().prependArray(
                            sb.toString(), lines);
                }
                break;
            default:
                throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                        "invalid anchor (" + align
                                + ") for join direction LEFT or RIGHT");
            }
            sb.setLength(0);
            for (int i = 0, len = lines.length; i < len; i++) {
                sb.append(lines[i]);
                sb.append('\n');
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        displayText = sb.toString();
        sb.returnToPool();
        sb = null;
        lines = null;
    }
    /**
     * Joins two bordered panels on the left or right side.
     * @param p the second panel
     * @param align the direction in which the panels are aligned, if they are
     *            of different sizes
     * @param isLeftJoin if <tt>true</tt>, this panel is to the left of the
     *            second panel; otherwise it is on the right
     * @throws RPGException if an error occurs
     */
    private void joinLeftorRightBordered(final Panel p, final int align,
            final boolean isLeftJoin) throws RPGException {
        String[] t1, t2;
        if (isLeftJoin) {
            t1 = TextProcessor.getInstance().wrapText(
                    getContent(), width).split("\n");
            t2 = TextProcessor.getInstance().wrapText(
                    p.getContent(), p.width).split("\n");
        } else {
            t1 = TextProcessor.getInstance().wrapText(
                    p.getContent(), width).split("\n");
            t2 = TextProcessor.getInstance().wrapText(
                    getContent(), p.width).split("\n");
        }
        int leftWidth = 0, rightWidth = 0;
        for (int i = t1.length - 1; i >= 0; i--) {
            leftWidth = Math.max(leftWidth, t1[i].length());
        }
        for (int i = t2.length - 1; i >= 0; i--) {
            rightWidth = Math.max(rightWidth, t2[i].length());
        }
        // break text into 2 columns of equal height
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        int maxLines = Math.max(t1.length, t2.length);
        try {
            String[] leftCol = new String[0];
            String[] rightCol = new String[0];
            switch (align) {
            case TOP:
                for (int i = 0, len = maxLines; i < len; i++) {
                    // left column
                    sb.setLength(0);
                    if (i < t1.length) {
                        sb.append(t1[i]);
                    }
                    for (int j =
                            leftWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    leftCol = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), leftCol);
                    // right column
                    sb.setLength(0);
                    if (i < t2.length) {
                        sb.append(t2[i]);
                    }
                    for (int j =
                            rightWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    rightCol = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), rightCol);
                }
                break;
            case BOTTOM:
                for (int i = 0, len = maxLines; i < len; i++) {
                    // left column
                    sb.setLength(0);
                    if (t1.length - 1 - i >= 0) {
                        sb.append(t1[t1.length - 1 - i]);
                    }
                    for (int j =
                            leftWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    leftCol = ArrayUtilities.getInstance().prependArray(
                            sb.toString(), leftCol);
                    // right column
                    sb.setLength(0);
                    if (t2.length - 1 - i >= 0) {
                        sb.append(t2[t2.length - 1 - i]);
                    }
                    for (int j =
                            rightWidth - sb.toString().length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    rightCol = ArrayUtilities.getInstance().prependArray(
                            sb.toString(), rightCol);
                }
                break;
            case CENTER:
                int off1 = (maxLines - t1.length) / 2;
                int off2 = (maxLines - t2.length) / 2;
                for (int i = maxLines - 1; i >= 0; i--) {
                    sb.setLength(0);
                    if (i - off1 < t1.length
                            && i - off1 >= 0) {
                        sb.append(t1[i - off1]);
                    }
                    for (int j = leftWidth - sb.toString().length();
                            j > 0; j--) {
                        sb.append(' ');
                    }
                    leftCol = ArrayUtilities.getInstance().prependArray(
                            sb.toString(), leftCol);
                    // right column
                    sb.setLength(0);
                    if (i - off2 < t2.length
                            && i - off2 >= 0) {
                        sb.append(t2[i - off2]);
                    }
                    for (int j = rightWidth - sb.toString().length();
                            j > 0; j--) {
                        sb.append(' ');
                    }
                    rightCol = ArrayUtilities.getInstance().prependArray(
                            sb.toString(), rightCol);
                }
                break;
            default:
                throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                        "invalid anchor (" + align
                                + ") for join direction LEFT");
            }
            sb.setLength(0);
            // turn left and right columns into bordered tables.
            String left, right;
            if (title != null) {
                left = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { new String(title),
                                String.join("\n", leftCol) },
                        getTitledTableMarkup());
            } else {
                left = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { String.join("\n", leftCol) },
                        getUnTitledTableMarkup());
            }
            if (p.title != null) {
                right = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { new String(p.title),
                                String.join("\n", rightCol) },
                        getTitledTableMarkup());
            } else {
                right = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { String.join("\n", rightCol) },
                        getUnTitledTableMarkup());
            }
            // line up left and right columns, removing one border side
            leftCol = left.split("\n");
            rightCol = right.split("\n");
            for (int i = 0, len = leftCol.length; i < len; i++) {
                sb.append(leftCol[i]);
                sb.append(rightCol[i].substring(1));
                sb.append('\n');
            }
            left = null;
            right = null;
            leftCol = null;
            rightCol = null;
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        displayText = sb.toString();
        sb.returnToPool();
        sb = null;
    }
    /**
     * Joins two panels on the left or right side.
     * @param p the second panel
     * @param align the direction in which the panels are aligned, if they are
     *            of different sizes
     * @param isTopJoin if <tt>true</tt>, this panel is above the second panel;
     *            otherwise it is below
     * @throws RPGException if an error occurs
     */
    private void joinTopOrBottom(final Panel p, final int align,
            final boolean isTopJoin) throws RPGException {
        String[] t1, t2;
        if (isTopJoin) {
            t1 = getDisplayText().split("\n");
            t2 = p.getDisplayText().split("\n");
        } else {
            t2 = getDisplayText().split("\n");
            t1 = p.getDisplayText().split("\n");
        }
        int maxWidth = 0;
        for (int i = t1.length - 1; i >= 0; i--) {
            maxWidth = Math.max(maxWidth, t1[i].length());
        }
        for (int i = t2.length - 1; i >= 0; i--) {
            maxWidth = Math.max(maxWidth, t2[i].length());
        }
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        String[] lines = new String[0];
        try {
            switch (align) {
            case LEFT:
                for (int i = 0, len = t1.length; i < len; i++) {
                    sb.setLength(0);
                    sb.append(t1[i]);
                    // pad right side with spaces
                    int lineWidth = t1[i].length();
                    for (int j = maxWidth - lineWidth; j > 0; j--) {
                        sb.append(' ');
                    }
                    lines = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), lines);
                }
                for (int i = 0, len = t2.length; i < len; i++) {
                    lines = ArrayUtilities.getInstance().extendArray(
                            t2[i], lines);
                }
                break;
            case RIGHT:
                for (int i = 0, len = t1.length; i < len; i++) {
                    sb.setLength(0);
                    int lineWidth = t1[i].length();
                    // pad left side with spaces
                    for (int j = maxWidth - lineWidth; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t1[i]);
                    lines = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), lines);
                }
                for (int i = 0, len = t2.length; i < len; i++) {
                    sb.setLength(0);
                    int lineWidth = t2[i].length();
                    // pad left side with spaces
                    for (int j = maxWidth - lineWidth; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t2[i]);
                    lines = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), lines);
                }
                break;
            case CENTER:
                for (int i = 0, len = t1.length; i < len; i++) {
                    sb.setLength(0);
                    int offsetLeft = (maxWidth - t1[i].length()) / 2;
                    int offsetRight = maxWidth - t1[i].length() - offsetLeft;
                    // pad each side with spaces
                    for (int j = offsetLeft; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t1[i]);
                    for (int j = offsetRight; j > 0; j--) {
                        sb.append(' ');
                    }
                    lines = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), lines);
                }
                for (int i = 0, len = t2.length; i < len; i++) {
                    sb.setLength(0);
                    int offsetLeft = (maxWidth - t2[i].length()) / 2;
                    int offsetRight = maxWidth - t2[i].length() - offsetLeft;
                    // pad each side with spaces
                    for (int j = offsetLeft; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t2[i]);
                    for (int j = offsetRight; j > 0; j--) {
                        sb.append(' ');
                    }
                    lines = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), lines);
                }
                break;
            default:
                throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                        "invalid anchor (" + align
                                + ") for join direction TOP or BOTTOM");
            }
            sb.setLength(0);
            for (int i = 0, len = lines.length; i < len; i++) {
                sb.append(lines[i]);
                sb.append('\n');
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        displayText = sb.toString();
        sb.returnToPool();
        sb = null;
        lines = null;
    }
    /**
     * Joins two bordered panels on the left or right side.
     * @param p the second panel
     * @param align the direction in which the panels are aligned, if they are
     *            of different sizes
     * @param isTopJoin if <tt>true</tt>, this panel is above of the second
     *            panel; otherwise it is below
     * @throws RPGException if an error occurs
     */
    private void joinTopOrBottomBordered(final Panel p, final int align,
            final boolean isTopJoin) throws RPGException {
        String[] t1, t2;
        if (isTopJoin) {
            t1 = TextProcessor.getInstance().wrapText(
                    getContent(), width).split("\n");
            t2 = TextProcessor.getInstance().wrapText(
                    p.getContent(), p.width).split("\n");
        } else {
            t1 = TextProcessor.getInstance().wrapText(
                    p.getContent(), width).split("\n");
            t2 = TextProcessor.getInstance().wrapText(
                    getContent(), p.width).split("\n");
        }
        int maxWidth = 0;
        for (int i = t1.length - 1; i >= 0; i--) {
            maxWidth = Math.max(maxWidth, t1[i].length());
        }
        for (int i = t2.length - 1; i >= 0; i--) {
            maxWidth = Math.max(maxWidth, t2[i].length());
        }
        // break text into 2 columns of equal height
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        try {
            String[] topRow = new String[0];
            String[] bottomRow = new String[0];
            switch (align) {
            case LEFT:
                for (int i = 0, len = t1.length; i < len; i++) {
                    // top row
                    sb.setLength(0);
                    sb.append(t1[i]);
                    for (int j = maxWidth - t1[i].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    topRow = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), topRow);
                }
                for (int i = 0, len = t2.length; i < len; i++) {
                    // bottom row
                    sb.setLength(0);
                    sb.append(t2[i]);
                    for (int j = maxWidth - t2[i].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    bottomRow = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), bottomRow);
                }
                break;
            case RIGHT:
                for (int i = 0, len = t1.length; i < len; i++) {
                    // top row
                    sb.setLength(0);
                    for (int j = maxWidth - t1[i].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t1[i]);
                    topRow = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), topRow);
                }
                for (int i = 0, len = t2.length; i < len; i++) {
                    // bottom row
                    sb.setLength(0);
                    for (int j = maxWidth - t2[i].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t2[i]);
                    bottomRow = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), bottomRow);
                }
                break;
            case CENTER:
                for (int i = 0, len = t1.length; i < len; i++) {
                    // top row
                    sb.setLength(0);
                    int offsetLeft = (maxWidth - t1[i].length()) / 2;
                    int offsetRight =
                            maxWidth - t1[i].length() - offsetLeft;
                    for (int j = offsetLeft; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t1[i]);
                    for (int j = offsetRight; j > 0; j--) {
                        sb.append(' ');
                    }
                    topRow = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), topRow);
                }
                for (int i = 0, len = t2.length; i < len; i++) {
                    // bottom row
                    sb.setLength(0);
                    int offsetLeft = (maxWidth - t2[i].length()) / 2;
                    int offsetRight =
                            maxWidth - t2[i].length() - offsetLeft;
                    for (int j = offsetLeft; j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append(t2[i]);
                    for (int j = offsetRight; j > 0; j--) {
                        sb.append(' ');
                    }
                    bottomRow = ArrayUtilities.getInstance().extendArray(
                            sb.toString(), bottomRow);
                }
                break;
            default:
                throw new RPGException(ErrorMessage.BAD_PARAMETERS,
                        "invalid anchor (" + align
                                + ") for join direction TOP or BOTTOM");
            }
            sb.setLength(0);
            // turn left and right columns into bordered tables.
            String top, bottom;
            if (title != null) {
                top = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { new String(title),
                                String.join("\n", topRow) },
                        getTitledTableMarkup());
            } else {
                top = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { String.join("\n", topRow) },
                        getUnTitledTableMarkup());
            }
            if (p.title != null) {
                bottom = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { new String(p.title),
                                String.join("\n", bottomRow) },
                        getTitledTableMarkup());
            } else {
                bottom = TextProcessor.getInstance().processText(
                        null,
                        null,
                        new String[] { String.join("\n", bottomRow) },
                        getUnTitledTableMarkup());
            }
            // line up left and right columns, removing one border side
            topRow = top.split("\n");
            bottomRow = bottom.split("\n");
            for (int i = 0, len = topRow.length - 1; i < len; i++) {
                sb.append(topRow[i]);
                sb.append('\n');
            }
            for (int i = 0, len = bottomRow.length; i < len; i++) {
                sb.append(bottomRow[i]);
                sb.append('\n');
            }
            top = null;
            bottom = null;
            topRow = null;
            bottomRow = null;
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        displayText = sb.toString();
        sb.returnToPool();
        sb = null;
    }
    /**
     * Generates the panel text as it would be displayed in the console.
     * @throws RPGException if an error occurs
     */
    private void processText() throws RPGException {
        String s = null;
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        try {
            // wrap text
            int innerWidth = width;
            if (bordered) {
                innerWidth -= RIGHT_PADDING + LEFT_PADDING;
            }
            s = TextProcessor.getInstance().wrapText(
                    new String(content), innerWidth);
            // count # of lines
            int innerHeight = height;
            if (bordered) {
                innerHeight -= 2;
            }
            String[] split = s.split("\n");
            if (split.length > innerHeight) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR,
                        "Supplied text is larger than allowed height.");
            }
            // add lines to be written
            for (int outerLoop = 0; outerLoop < innerHeight; outerLoop++) {
                s = "";
                if (outerLoop <= split.length - 1) {
                    s = split[outerLoop];
                }
                sb.append(s);
                for (int inner = innerWidth; inner > s.length(); inner--) {
                    sb.append(' ');
                }
                sb.append('\n');
            }
            if (bordered) {
                if (title != null) {
                    s = TextProcessor.getInstance().processText(
                            null,
                            null,
                            new String[] { new String(title), sb.toString() },
                            getTitledTableMarkup());
                } else {
                    s = TextProcessor.getInstance().processText(
                            null,
                            null,
                            new String[] { sb.toString() },
                            getUnTitledTableMarkup());
                }
            } else {
                s = sb.toString();
            }
        } catch (PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        displayText = s;
        sb.returnToPool();
        sb = null;
        s = null;
    }
    /**
     * Sets the value of the text.
     * @param t the new value to set
     */
    public void setContent(final String t) {
        content = t.toCharArray();
        displayText = null;
    }
}
