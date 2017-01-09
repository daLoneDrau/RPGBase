/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import java.util.ArrayList;

import com.dalonedrow.utils.Watchable;
import com.dalonedrow.utils.Watcher;

/**
 * Base Inventory slot, containing two members - the interactive object the slot
 * contains and a flag indicating whether or not the object should be displayed.
 * @author DaLoneDrau
 * @param <IO> the interactive object associated with the class implementation
 */
@SuppressWarnings("rawtypes")
public class InventorySlot<IO extends BaseInteractiveObject>
		implements Watchable {
	/** the item occupying the inventory slot. */
	private IO							io;
	/** a flag indicating the item is showing and should be rendered. */
	private boolean						show;
	/**
	 * the list of {@link Watcher}s associated with this {@link IoPcData}.
	 */
	private final ArrayList<Watcher>	watchers	= new ArrayList<Watcher>();
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addWatcher(final Watcher watcher) {
		watchers.add(watcher);
	}
	/**
	 * Gets the item occupying the inventory slot.
	 * @return {@link IO}
	 */
	public final IO getIo() {
		return io;
	}
	/**
	 * Gets the flag indicating the item is showing and should be rendered.
	 * @return <code>boolean</code>
	 */
	public final boolean isShow() {
		return show;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void notifyWatchers() {
		for (int i = 0; i < watchers.size(); i++) {
			watchers.get(i).watchUpdated(this);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removeWatcher(final Watcher watcher) {
		watchers.remove(watcher);
	}
	/**
	 * Sets the item occupying the inventory slot.
	 * @param val the val to set
	 */
	public final void setIo(final IO val) {
		io = val;
		notifyWatchers();
	}
	/**
	 * Sets the flag indicating the item is showing and should be rendered.
	 * @param flag the show to set
	 */
	public final void setShow(final boolean flag) {
		show = flag;
		notifyWatchers();
	}
}
