package com.dalonedrow.utils;

/**
 * Implements a class that is watchable by Watchers.
 * @author DaLoneDrow
 */
public interface Watchable {
	/**
	 * Adds a watcher for this instance.
	 * @param watcher the {@link Watcher}
	 */
	void addWatcher(final Watcher watcher);
	/** Notifies all {@link Watcher}s of any changes to this instance. */
	void notifyWatchers();
	/**
	 * Removes a watcher for this instance.
	 * @param watcher the {@link Watcher}
	 */
	void removeWatcher(final Watcher watcher);
}
