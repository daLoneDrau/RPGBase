/**
 * TextureLoader takes the name of an images list file, and loads all images
 * from that list. images loaded can be of four different types: o - a single
 * image n - a numbered list of images s - a number of images loaded as a
 * graphics strip - such as a gif with several images in it g - a group of
 * images each with different names
 * @author Owner
 */
package com.dalonedrow.rpg.base.consoleui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author DaLoneDrau
 */
public final class ConsoleLogger {
    private static final int DEBUG = 0;
    private static final int ERROR = 3;
    private static final int INFO = 1;
    /** the one and only instance of the <code>TextLoader</code> class. */
    private static ConsoleLogger instance;
    private static final String LOG_FILE = "log.txt";
    private static final int WARN = 2;
    /**
     * Gives access to the singleton instance of {@link ConsoleLogger}.
     * @return {@link ConsoleLogger}
     */
    public static ConsoleLogger getInstance() {
        if (ConsoleLogger.instance == null) {
            ConsoleLogger.instance = new ConsoleLogger();
        }
        return ConsoleLogger.instance;
    }
    /**
     * Gives access to the singleton instance of {@link ConsoleLogger}.
     * @return {@link ConsoleLogger}
     */
    public static ConsoleLogger getInstance(final String folder) {
        if (ConsoleLogger.instance == null) {
            ConsoleLogger.instance = new ConsoleLogger();
        }
        ConsoleLogger.instance.setLibraryFolder(folder);
        return ConsoleLogger.instance;
    }
    private SimpleDateFormat formatter;
    private int level = ConsoleLogger.INFO;
    /** the folder containing the image library. */
    private String libraryFolder;
    private PrintWriter out;
    /**
     * Appends a debugging message to the log.
     * @param msg the message
     * @throws Exception
     */
    public void debug(final String msg) throws Exception {
        if (ConsoleLogger.DEBUG >= level) {
            if (out == null) {
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                sb.append(libraryFolder);
                sb.append("/");
                sb.append(ConsoleLogger.LOG_FILE);
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                URL resourceURL = cl.getResource(sb.toString());
                File file = new File(resourceURL.toURI());
                out = new PrintWriter(new BufferedWriter(
                        new FileWriter(file, false)), true);
                sb.returnToPool();
                sb = null;
                cl = null;
                resourceURL = null;
                file = null;
                formatter = new SimpleDateFormat("MMM d, yyyy H:MM:ss a z");
            }

            StackTraceElement[] stackTraceElements =
                    Thread.currentThread().getStackTrace();
            int index = 0;
            for (int len = stackTraceElements.length; index < len; index++) {
                if (stackTraceElements[index].getClassName()
                        .indexOf("com.dal") == 0
                        && stackTraceElements[index].getClassName().indexOf(
                                "ConsoleLogger") < 0) {
                    break;
                }
            }
            // out.printf("<%s> (%s:%s:%d) - %s\n",
            // formatter.format(new Date()),
            // stackTraceElements[index].getFileName(),
            // stackTraceElements[index].getMethodName(),
            // stackTraceElements[index].getLineNumber(),
            // msg);
            out.printf("DEBUG (%s:%s:%d) - %s\n", stackTraceElements[index]
                    .getFileName(), stackTraceElements[index].getMethodName(),
                    stackTraceElements[index].getLineNumber(), msg);
        }
    }
    /**
     * Gets the value of the level.
     * @return {@link int}
     */
    public int getLevel() {
        return level;
    }
    /**
     * Appends an informative message to the log.
     * @param msg the message
     * @throws Exception
     */
    public void info(final String msg) throws Exception {
        if (ConsoleLogger.INFO >= level) {
            if (out == null) {
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                sb.append(libraryFolder);
                sb.append("/");
                sb.append(ConsoleLogger.LOG_FILE);
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                URL resourceURL = cl.getResource(sb.toString());
                File file = new File(resourceURL.toURI());
                out = new PrintWriter(new BufferedWriter(
                        new FileWriter(file, false)), true);
                sb.returnToPool();
                sb = null;
                cl = null;
                resourceURL = null;
                file = null;
                formatter = new SimpleDateFormat("MMM d, yyyy H:MM:ss a z");
            }

            StackTraceElement[] stackTraceElements =
                    Thread.currentThread().getStackTrace();
            int index = 0;
            for (int len = stackTraceElements.length; index < len; index++) {
                if (stackTraceElements[index].getClassName()
                        .indexOf("com.dal") == 0
                        && stackTraceElements[index].getClassName().indexOf(
                                "ConsoleLogger") < 0) {
                    break;
                }
            }
            // out.printf("<%s> (%s:%s:%d) - %s\n",
            // formatter.format(new Date()),
            // stackTraceElements[index].getFileName(),
            // stackTraceElements[index].getMethodName(),
            // stackTraceElements[index].getLineNumber(),
            // msg);
            out.printf("INFO (%s:%s:%d) - %s\n", stackTraceElements[index]
                    .getFileName(), stackTraceElements[index].getMethodName(),
                    stackTraceElements[index].getLineNumber(), msg);
        }
    }
    /**
     * Sets the value of the level.
     * @param level the value to set
     */
    public void setLevel(final int level) {
        this.level = level;
    }
    /**
     * Sets the location of the library folder.
     * @param folder the location to set
     */
    public void setLibraryFolder(final String folder) {
        libraryFolder = folder;
    }
    /**
     * Appends a warning message to the log.
     * @param msg the message
     * @throws PooledException
     * @throws Exception
     */
    public void warn(final String msg) throws RPGException, PooledException {
        if (ConsoleLogger.WARN >= level) {
            if (out == null) {
                PooledStringBuilder sb =
                        StringBuilderPool.getInstance().getStringBuilder();
                sb.append(libraryFolder);
                sb.append("/");
                sb.append(ConsoleLogger.LOG_FILE);
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                URL resourceURL = cl.getResource(sb.toString());
                File file;
                try {
                    file = new File(resourceURL.toURI());
                    out = new PrintWriter(new BufferedWriter(
                            new FileWriter(file, false)), true);
                } catch (IOException e) {
                    throw new RPGException(ErrorMessage.ILLEGAL_ACCESS, e);
                } catch (URISyntaxException e) {
                    throw new RPGException(ErrorMessage.ILLEGAL_ACCESS, e);
                }
                sb.returnToPool();
                sb = null;
                cl = null;
                resourceURL = null;
                file = null;
                formatter = new SimpleDateFormat("MMM d, yyyy H:MM:ss a z");
            }

            StackTraceElement[] stackTraceElements =
                    Thread.currentThread().getStackTrace();
            int index = 0;
            for (int len = stackTraceElements.length; index < len; index++) {
                if (stackTraceElements[index].getClassName()
                        .indexOf("com.dal") == 0
                        && stackTraceElements[index].getClassName().indexOf(
                                "ConsoleLogger") < 0) {
                    break;
                }
            }
            // out.printf("<%s> (%s:%s:%d) - %s\n",
            // formatter.format(new Date()),
            // stackTraceElements[index].getFileName(),
            // stackTraceElements[index].getMethodName(),
            // stackTraceElements[index].getLineNumber(),
            // msg);
            out.printf("WARNING (%s:%s:%d) - %s\n", stackTraceElements[index]
                    .getFileName(), stackTraceElements[index].getMethodName(),
                    stackTraceElements[index].getLineNumber(), msg);
        }
    }
}
