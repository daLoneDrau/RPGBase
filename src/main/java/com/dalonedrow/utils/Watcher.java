/**
 *
 */
package com.dalonedrow.utils;

/**
 * Interface for a Watcher class, that reacts to changes to the
 * {@link Watchable}.
 * @author DaLoneDrow
 */
public interface Watcher {
	/**
	 * Updates the view after a {@link Watchable} has been changed.
	 * @param data the data instance
	 */
	void watchUpdated(final Watchable data);
}
