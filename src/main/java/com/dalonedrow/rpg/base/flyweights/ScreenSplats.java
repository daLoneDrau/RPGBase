/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.engine.sprite.base.SimpleVector3;

/**
 * @author Donald
 */
public class ScreenSplats {
	/** the time in nanoseconds for which the {@link ScreenSplats} exists. */
	private long			duration;
	/**
	 * flag indicating the {@link ScreenSplats} exists and should be rendered.
	 */
	private boolean			exist;
	private SimpleVector3	position;
	private float			size;
	/** the time the splat was started. */
	private long			starttime;
}
