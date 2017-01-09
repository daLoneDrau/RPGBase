/**
 * 
 */
package com.dalonedrow.engine.systems.base;

/**
 * @author Donald
 */
public final class Time {
	/** the singleton instance. */
	private static Time	instance;
	/**
	 * Gets the one and only instance of {@link Time}.
	 * @return {@link Time}
	 */
	public static Time getInstance() {
		if (Time.instance == null) {
			Time.instance = new Time();
		}
		return Time.instance;
	}
	private long	frameStart;
	/** the actual game time. */
	private long	gameTime		= 0;
	/** the length in ns that each game loop should take. */
	private long	idealLoopTime;
	private float	LastFrameTime;				// ARX: jycorbel
	/** the time the game was paused. */
	private long	timePaused		= 0;
	/** flag indicating whether the timer has been initialized. */
	private boolean	timerInit		= false;
	/** flag indicating whether the game is paused or not. */
	private boolean	timerPaused		= false;
	/** the time the timer was started. */
	private long	timerStart;
	/** the total time the game has been spent paused. */
	private long	totalTimePaused	= 0;
	/** Hidden constructor. */
	private Time() {
		super();
	}
	/**
	 * Resets the timer - possibly due to lag when between game screens.
	 * @param time the time to which the timers are being reset
	 */
	public void forceTimerReset(final long time) {
		long tim = getCurrentTime();
		totalTimePaused = tim - time;
		gameTime = time;
		timePaused = 0;
		timerPaused = false;
	}
	/**
	 * Gets the number of nanoseconds that have passed since the game was
	 * started.
	 * @return float
	 */
	private long getCurrentTime() {
		initializeTimer(); // initialize the timer if needed
		long now = System.nanoTime();
		return now - timerStart;
	}
	/**
	 * Gets the frameStart
	 * @return {@link float}
	 */
	public long getFrameStart() {
		return frameStart;
	}
	/**
	 * Gets the number of nanoseconds that have passed since the game was
	 * started, not including the time spent paused.
	 * @return long
	 */
	public long getGameTime() {
		return getGameTime(false);
	}
	/**
	 * Gets the number of nanoseconds that have passed since the game was
	 * started.
	 * @param usePause if true, the time returned include the time spent paused;
	 *            otherwise the time returned does not include paused time
	 * @return long
	 */
	public long getGameTime(final boolean usePause) {
		long tim = getCurrentTime();
		if (timerPaused && usePause) {
			gameTime = tim;
		} else {
			gameTime = tim - totalTimePaused;
		}
		return gameTime;
	}
	/**
	 * Gets the number of frames per second the game should run at.
	 * @return {@link int}
	 */
	public long getIdealLoopTime() {
		return idealLoopTime;
	}
	/** Initializes the game timer. */
	public void init() {
		initializeTimer();
		long tim = getCurrentTime();
		totalTimePaused = tim;
		gameTime = 0;
		timePaused = 0;
		timerPaused = false;
		// ARX_BEGIN: jycorbel (2010-07-19) - Add external vars for
		// resetting them on ARX_TIME_Init call.
		// Currently when ARX_TIME_Init
		// the substract FrameDiff = FrameTime - LastFrameTime
		// is negative because of resetting totalTimePaused.
		// This solution reinit FrameTime & LastFrameTime to get a min
		// frameDiff = 0 on ARX_TIME_Init.
		// frameStart = LastFrameTime = gameTime;
		// ARX_END: jycorbel (2010-07-19)
	}
	/** Hidden method to start the game timer. */
	private void initializeTimer() {
		if (!timerInit) {
			// if the timer hasn't been started, start it
			timerStart = System.nanoTime();
			timerInit = true;
			init();
		}
	}
	/**
	 * Determines if the game is paused.
	 * @return if true, the timer has been paused; false otherwise
	 */
	public boolean isTimerPaused() {
		return timerPaused;
	}
	/** Pauses the game, and sets the time at which the pause started. */
	public void pause() {
		if (!timerPaused) {
			// get the current time
			// store the time the pause started in a field
			timePaused = getCurrentTime();
			// set the paused flag
			timerPaused = true;
		}
	}
	/**
	 * Sets the number of frames per second the game should run at.
	 * @param val the fps to set
	 */
	public void setIdealLoopTime(final long val) {
		idealLoopTime = val;
	}
	public void startFrame() {
		frameStart = this.getGameTime();
	}
	/** Unpauses the game, and updates the time spent paused. */
	public void unpause() {
		if (timerPaused) {
			// get the current time
			// update the amount of time spent paused
			totalTimePaused += getCurrentTime() - timePaused;
			// remove the time the pause was started
			timePaused = 0;
			// remove the paused flag
			timerPaused = false;
		}
	}
	public long getGameRound() {
		return 1;
	}
}
