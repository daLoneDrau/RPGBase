/**
 *
 */
package com.dalonedrow.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Donald
 */
public class ListPool {
	/** the initial pool capacity is 5. */
	private static final int	INITIAL_CAPACITY	= 5;
	/** the one and only instance of the <code>StringListPool</code> class. */
	private static ListPool		instance;
	/**
	 * Gives access to the singleton instance of {@link ListPool}.
	 * @return {@link ListPool}
	 */
	public static ListPool getInstance() {
		if (ListPool.instance == null) {
			ListPool.instance = new ListPool(INITIAL_CAPACITY);
		}
		return ListPool.instance;
	}
	/** the flags for each pool item indicating whether it is locked or not. */
	private final List<Boolean>		locked;
	/** the pool of {@link PooledStringBuilder}s. */
	private final List<PooledList>	pool;
	/**
	 * Creates the pool of {@link PooledList}s.
	 * @param initialCapacity the initial pool capacity
	 */
	private ListPool(final int initialCapacity) {
		// create an initial list
		pool = new ArrayList<PooledList>(initialCapacity);
		locked = new ArrayList<Boolean>(initialCapacity);
		// populate the list and set all items to unlocked
		for (int i = 0; i < initialCapacity; i++) {
			pool.add(new PooledList(i));
			locked.add(false);
		}
	}
	/**
	 * Retrieves a {@link PooledList} from the pool and locks it for use.
	 * @return {@link PooledList}
	 */
	public PooledList getList() {
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
			pool.add(new PooledList(freeIndex));
			// lock the item
			locked.add(true);
		}
		// return the item at the free index
		return pool.get(freeIndex);
	}
	/**
	 * Determines if an item is locked.
	 * @param item the {@link PooledList} instance
	 * @return true if the asset is locked, false if it is free and ready for
	 *         use
	 */
	public boolean isItemLocked(final PooledList item) {
		return locked.get(item.getPoolIndex());
	}
	/**
	 * Returns an item to the pool.
	 * @param list the {@link PooledList} being returned
	 */
	public void returnObject(final PooledList list) {
		// tell the item it's been returned to the pool
		list.returnToPool();
		// remove the lock
		locked.set(list.getPoolIndex(), false);
	}
	/**
	 * Unlocks the assets, readying it for use again.
	 * @param item the {@link PooledStringBuilder} asset
	 */
	void unlockItem(final PooledList item) {
		locked.set(item.getPoolIndex(), false);
	}
}
