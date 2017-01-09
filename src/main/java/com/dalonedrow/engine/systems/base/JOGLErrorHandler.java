package com.dalonedrow.engine.systems.base;

public class JOGLErrorHandler {
	/** the singleton instance of <code>ErrorHandler</code>. */
	private static JOGLErrorHandler	instance;

	/**
	 * Gets the singleton instance of <code>ErrorHandler</code>.
	 * @return {@link JOGLErrorHandler}
	 */
	public static JOGLErrorHandler getInstance() {
		if (JOGLErrorHandler.instance == null) {
			JOGLErrorHandler.instance = new JOGLErrorHandler();
		}
		return JOGLErrorHandler.instance;
	}
	/** Hidden constructor. */
	private JOGLErrorHandler() { }
	/**
	 * Processes a fatal error and stops the game.
	 * @param ex the error
	 */
	public void fatalError(final Exception ex) {
		ex.printStackTrace();
	    System.exit(0);
	}
}
