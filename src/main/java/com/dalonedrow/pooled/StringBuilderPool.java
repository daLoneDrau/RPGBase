/**
 *
 */
package com.dalonedrow.pooled;

import java.util.ArrayList;

/**
 * Pooled string builder assets list.
 * @author DaLoneDrow
 */
public final class StringBuilderPool {
	/**
	 * the one and only instance of the <code>StringBuilderPool</code> class.
	 */
	private static StringBuilderPool instance;
	/**
	 * Gives access to the singleton instance of {@link StringBuilderPool}.
	 * @return {@link StringBuilderPool}
	 */
	public static StringBuilderPool getInstance() {
		final int initialLength = 5;
		if (StringBuilderPool.instance == null) {
			StringBuilderPool.instance = new StringBuilderPool(initialLength);
		}
		return StringBuilderPool.instance;
	}
	/** the flags for each pool item indicating whether it is locked or not. */
	private final ArrayList<Boolean>				locked;
	/** the pool of {@link PooledStringBuilder}s. */
	private final ArrayList<PooledStringBuilder>	pool;
	/**
	 * Creates the pool of {@link PooledStringBuilder}s.
	 * @param initialCapacity the initial pool capacity
	 */
	private StringBuilderPool(final int initialCapacity) {
		// create an initial list
		pool = new ArrayList<PooledStringBuilder>(initialCapacity);
		locked = new ArrayList<Boolean>(initialCapacity);
		// populate the list and set all items to unlocked
		for (int i = 0; i < initialCapacity; i++) {
			pool.add(new PooledStringBuilder(i));
			locked.add(false);
		}
	}
	/**
	 * Retrieves a {@link PooledStringBuilder} from the pool and locks it for
	 * use.
	 * @return {@link PooledStringBuilder}
	 */
	public PooledStringBuilder getStringBuilder() {
		int freeIndex = 0;
		for (; freeIndex < locked.size(); freeIndex++) {
			// current index is free
			if (!locked.get(freeIndex)) {
				// lock this item
				locked.set(freeIndex, true);
				break;
			}
		}
		if (freeIndex >= locked.size()) {
			// we got here because all items are in use
			// create a new item and add it to the list
			pool.add(new PooledStringBuilder(freeIndex));
			// lock the item
			locked.add(true);
		}
		// return the item at the free index
		return pool.get(freeIndex);
	}
	/**
	 * Determines if an item is locked.
	 * @param item the {@link PooledStringBuilder} instance
	 * @return true if the asset is locked, false if it is free and ready for
	 *         use
	 */
	public boolean isItemLocked(final PooledStringBuilder item) {
		return locked.get(item.getPoolIndex());
	}
	/**
	 * Returns an item to the pool.
	 * @param item the {@link PooledStringBuilder} being returned
	 */
	public void returnObject(final PooledStringBuilder item) {
		// remove the lock
		locked.set(item.getPoolIndex(), false);
		// tell the item it's been returned to the pool
		item.returnToPool();
	}
	/**
	 * Unlocks the assets, readying it for use again.
	 * @param item the {@link PooledStringBuilder} asset
	 */
	void unlockItem(final PooledStringBuilder item) {
		locked.set(item.getPoolIndex(), false);
	}
}
