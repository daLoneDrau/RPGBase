package com.dalonedrow.rpg.base.consoleui;

import java.lang.reflect.Method;

import com.dalonedrow.rpg.base.systems.ConsoleInterface;

/**
 * 
 * @author drau
 *
 */
public final class InputProcessor {
    /** the singleton instance of {@link InputProcessor}. */
    private static InputProcessor instance;
    /**
     * Gets the one and only instance of {@link InputProcessor}.
     * @return {@link InputProcessor}
     */
    public static InputProcessor getInstance() {
        if (InputProcessor.instance == null) {
            InputProcessor.instance =
                    new InputProcessor();
        }
        return InputProcessor.instance;
    }
    /** arguments. */
    private Object[] args;
    /** the method. */
    private Method   method;
    /** the object. */
    private Object   obj;
    /**
     * Creates a new instance of {@link InputProcessor}.
     */
    private InputProcessor() {
        super();
    }
    /** Accept input. */
    public void acceptInput() {
        if (obj != null) {
            String s = ConsoleInterface.getInstance().getScanIn().nextLine();
            if (args == null) {
                InputEvent.getInstance().addAction(
                        obj, // object
                        method,
                        new String[] { s }
                        );
            } else {
                InputEvent.getInstance().addAction(
                        obj, // object
                        method,
                        args
                        );
            }
            obj = null;
            method = null;
            args = null;
        }
    }
    /**
     * Set the input action.
     * @param o the object
     * @param m the method
     * @param a the arguments
     */
    public void setInputAction(final Object o, final Method m,
            final Object[] a) {
        obj = o;
        method = m;
        args = a;
    }
}
