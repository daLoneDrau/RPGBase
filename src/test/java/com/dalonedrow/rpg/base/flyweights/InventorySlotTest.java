package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dalonedrow.utils.ArrayUtilities;
import com.dalonedrow.utils.Watchable;
import com.dalonedrow.utils.Watcher;

public class InventorySlotTest {
	InventorySlot			data;
	BaseInteractiveObject	io;
	Watcher					watcher0;
	Watcher					watcher1;
	String[]				watcherNotes	= new String[0];
	@Before
	public void beforeTests() {
		data = new InventorySlot() {

		};
		io = new BaseInteractiveObject(0) {

		};
		watcher0 = new Watcher() {
			@Override
			public void watchUpdated(final Watchable data) {
				watcherNotes = ArrayUtilities.getInstance().extendArray(
						"watcher0", watcherNotes);
			}
		};
		watcher1 = new Watcher() {
			@Override
			public void watchUpdated(final Watchable data) {
				watcherNotes = ArrayUtilities.getInstance().extendArray(
						"watcher1", watcherNotes);
			}
		};
	}
	@Test
	public void canAddWatchers() throws RPGException {
		data.addWatcher(watcher0);
		data.addWatcher(watcher1);
		data.notifyWatchers();
		data.removeWatcher(watcher0);
		data.removeWatcher(watcher1);
	}
	@Test
	public void canSetFields() throws RPGException {
		assertNull("null IO", data.getIo());
		assertFalse("no show", data.isShow());
		data.setIo(io);
		data.setShow(true);
		assertEquals("has io", data.getIo(), io);
		assertTrue("show", data.isShow());
	}
}
