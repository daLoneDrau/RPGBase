package com.dalonedrow.rpg.base.systems;

import java.util.Scanner;

import javax.swing.text.View;

import com.dalonedrow.rpg.base.consoleui.ConsoleView;
import com.dalonedrow.rpg.base.flyweights.RPGException;

public abstract class ConsoleInterface {
    /** the one and only instance of the <code>ConsoleInterface</code> class. */
    private static ConsoleInterface instance;
    /**
     * Gives access to the singleton instance of {@link ConsoleInterface}.
     * @return {@link ConsoleInterface}
     */
    public static final ConsoleInterface getInstance() {
        return ConsoleInterface.instance;
    }
    public static final void setInstance(final ConsoleInterface i) {
        instance = i;
    }
    /** the current view. */
    private ConsoleView   currentView;
    /** flag indicating debugging is on. */
    private boolean       debug;
    /** the system scanner. */
    private final Scanner scanIn = new Scanner(System.in);
    /**
     * Gets the system scanner.
     * @return {@link Scanner}
     */
    public Scanner getScanIn() {
        return scanIn;
    }
    /**
     * Prepares the {@link View} for the rendering loop.
     * @throws RPGException if an error occurs
     */
    public abstract void prepareForRendering() throws RPGException;
}
