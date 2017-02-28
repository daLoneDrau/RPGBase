/**
 *
 */
package com.dalonedrow.rpg.base.consoleui;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author drau
 */
public abstract class ConsoleView {
    /** the error dialog width. */
    private static final int ERR_DLG_WIDTH = 50;
    public abstract void addErrorMessage(final String msg);
    public abstract void addMessage(final String text) throws RPGException;
    public abstract String getErrorMessage();
    public abstract String getMessage();
    /**
     * Gets a list of Strings and sorts them as columns.
     * @param cols
     * @param indented flag indicating the columns are indented
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    protected String getSelectionsAsColumns(final String[] cols,
            final boolean indented) throws RPGException {
        PooledStringBuilder sb =
                StringBuilderPool.getInstance().getStringBuilder();
        String text = null;
        int maxLen = 0;
        for (int i = 0, len = cols.length; i < len; i += 2) {
            maxLen = Math.max(maxLen, cols[i].length());
        }
        for (int i = 0, len = cols.length; i < len; i += 2) {
            try {
                if (indented) {
                    sb.append('\t');
                }
                sb.append(cols[i]);
                if (i + 1 < cols.length) {
                    for (int j = maxLen - cols[i].length(); j > 0; j--) {
                        sb.append(' ');
                    }
                    sb.append('\t');
                    sb.append(cols[i + 1]);
                    sb.append('\n');
                } else {
                    sb.append('\n');
                }
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
        }
        text = sb.toString();
        sb.returnToPool();
        sb = null;
        return text;
    }
    /**
     * Renders the view.
     * @throws RPGException if an error occurs
     */
    public abstract void render() throws RPGException;
    /**
     * Displays an error.
     * @param errDialog the error dialog markup
     * @param text the error text
     * @throws RPGException if an error occurs
     */
    protected final void showError(final String errDialog, final String text)
            throws RPGException {
        String err =
                TextProcessor.getInstance().wrapText(text,
                        ERR_DLG_WIDTH, false, false);
        err = TextProcessor.getInstance().processText(
                null,
                null,
                err,
                errDialog);
        OutputEvent.getInstance().print(err, this);
    }
    /**
     * Displays a message.
     * @param msgDialog the message dialog markup
     * @param text the message text
     * @throws RPGException if an error occurs
     */
    protected final void showMessage(final String msgDialog,
            final String text) throws RPGException {
        String msg = TextProcessor.getInstance().wrapText(
                text, ERR_DLG_WIDTH, false, false);
        msg = TextProcessor.getInstance().processText(
                null,
                null,
                msg,
                msgDialog);
        OutputEvent.getInstance().print(msg, this);
    }
}
