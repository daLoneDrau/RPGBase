/**
 *
 */
package com.dalonedrow.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author drau
 */
@SuppressWarnings("unchecked")
public final class PooledList implements PoolableObject {
	/** the internal {@link List} instance. */
	@SuppressWarnings("rawtypes")
	private List		list;
	/** the pooled object's index. */
	private final int	poolIndex;
	/**
	 * Creates a new instance of {@link PooledList}.
	 * @param index the object's index
	 */
	public PooledList(final int index) {
		super();
		poolIndex = index;
	}
	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 * @param <T> the list type parameter
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws PooledException if the item was not locked
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	public <T> void add(final int index, final T element)
			throws PooledException {
		if (list == null) {
			list = new ArrayList<T>();
		}
		if (ListPool.getInstance().isItemLocked(this)) {
			list.add(index, element);
		} else {
			throw new PooledException("Item not locked for use!");
		}
	}
	/**
	 * Appends the specified element to the end of this list.
	 * @param <T> the list type parameter
	 * @param e element to be appended to this list
	 * @return <tt>true</tt>
	 * @throws PooledException if the item was not locked
	 */
	public <T> boolean add(final T e) throws PooledException {
		boolean added = false;
		if (list == null) {
			list = new ArrayList<T>();
		}
		if (ListPool.getInstance().isItemLocked(this)) {
			added = list.add(e);
		} else {
			throw new PooledException("Item not locked for use!");
		}
		return added;
	}
	/**
	 * Returns the element at the specified position in this list.
	 * @param <T> the list type parameter
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws PooledException if the item was not locked
	 */
	public <T> T get(final int index) throws PooledException {
		T t = null;
		if (list == null) {
			list = new ArrayList<T>();
		}
		if (ListPool.getInstance().isItemLocked(this)) {
			if (index >= 0
					&& index <= list.size()
					&& !list.isEmpty()) {
				t = (T) list.get(index);
			}
		} else {
			throw new PooledException("Item not locked for use!");
		}
		return t;
	}
	/**
	 * Gets the value for the object's index.
	 * @return {@link int}
	 */
	public int getPoolIndex() {
		return poolIndex;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void returnToPool() {
		if (list != null) {
			list.clear();
		}
		if (ListPool.getInstance().isItemLocked(this)) {
			ListPool.getInstance().unlockItem(this);
		}
	}
	/**
	 * Returns the number of elements in this list. If this list contains more
	 * than <tt>Integer.MAX_VALUE</tt> elements, returns
	 * <tt>Integer.MAX_VALUE</tt>.
	 * @return the number of elements in this list
	 */
	public int size() {
		return list.size();
	}
}
