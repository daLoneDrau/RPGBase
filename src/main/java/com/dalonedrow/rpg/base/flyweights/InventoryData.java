/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.ScriptConsts;
import com.dalonedrow.rpg.base.systems.Script;

/**
 * @author Donald
 * @param <IO>
 * @param <SLOT>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class InventoryData<IO extends BaseInteractiveObject,
SLOT extends InventorySlot<IO>> {
	/** the IO associated with this {@link InventoryData}. */
	private IO		io;
	/** the number of inventory slots. */
	private int		numInventorySlots;
	/** the inventory slots. */
	private SLOT[]	slots;
	/**
	 * Sends messages between an item and its owner that it is now in inventory.
	 * @param invOwnerIO the owner
	 * @param itemIO the item
	 * @throws RPGException if an error occurs
	 */
	public final void ARX_INVENTORY_Declare_InventoryIn(
			final IO invOwnerIO,
			final IO itemIO) throws RPGException {
		if (itemIO != null) {
			// TODO - handle ignition
			// if (io->ignition > 0) {
			// if (ValidDynLight(io->ignit_light))
			// DynLight[io->ignit_light].exist = 0;

			// io->ignit_light = -1;

			// if (io->ignit_sound != ARX_SOUND_INVALID_RESOURCE) {
			// ARX_SOUND_Stop(io->ignit_sound);
			// io->ignit_sound = ARX_SOUND_INVALID_RESOURCE;
			// }

			// io->ignition = 0;
			// }

			// send event from item to inventory owner
			Script.getInstance().setEventSender(itemIO);
			Script.getInstance().sendIOScriptEvent(invOwnerIO,
					ScriptConsts.SM_002_INVENTORYIN, new Object[0], null);
			// send event from inventory owner to item
			Script.getInstance().setEventSender(invOwnerIO);
			Script.getInstance().sendIOScriptEvent(itemIO,
					ScriptConsts.SM_002_INVENTORYIN, new Object[0], null);
			// clear global event sender
			Script.getInstance().setEventSender(null);
		}
	}
	/**
	 * Action when a player attempts to identify an item.
	 * @param playerIO the player's {@link IO}
	 * @param itemIO the itme's {@link IO}
	 * @throws RPGException if an error occurs
	 */
	public final void ARX_INVENTORY_IdentifyIO(final IO playerIO,
			final IO itemIO) throws RPGException {
		if (playerIO != null
				&& playerIO.hasIOFlag(IoGlobals.IO_01_PC)
				&& playerIO.getPCData() != null
				&& itemIO != null
				&& itemIO.hasIOFlag(IoGlobals.IO_02_ITEM)
				&& itemIO.getItemData() != null
				&& itemIO.getItemData().getEquipitem() != null) {
			if (playerIO.getPCData().canIdentifyEquipment(
					itemIO.getItemData().getEquipitem())) {
				Script.getInstance().sendIOScriptEvent(
						itemIO, ScriptConsts.SM_69_IDENTIFY, null, "");
			}
		}
	}
	/**
	 * Determines if an item can be put in inventory.
	 * @param itemIO the item
	 * @return <tt>true</tt> if the item can be put in inventory; <tt>false</tt>
	 *         otherwise
	 * @throws RPGException if an error occurs
	 */
	public final boolean CanBePutInInventory(final IO itemIO)
			throws RPGException {
		boolean can = false;
		if (itemIO != null
				&& !itemIO.hasIOFlag(IoGlobals.IO_15_MOVABLE)) {
			if (itemIO.hasIOFlag(IoGlobals.IO_10_GOLD)
					&& io.hasIOFlag(IoGlobals.IO_01_PC)) {
				io.getPCData().adjustGold(itemIO.getItemData().getPrice());
				if (itemIO.isScriptLoaded()) {
					Interactive.getInstance().RemoveFromAllInventories(itemIO);
					Interactive.getInstance().releaseIO(itemIO);
				} else {
					itemIO.setShow(IoGlobals.SHOW_FLAG_KILLED);
					itemIO.removeGameFlag(IoGlobals.GFLAG_ISINTREATZONE);
				}
				can = true;
			}
			if (!can) {
				// first try to stack
				for (int i = numInventorySlots - 1; i >= 0; i--) {
					IO slotIO = (IO) slots[i].getIo();
					if (slotIO != null
							&& slotIO.getItemData().getStackSize() > 1
							&& Interactive.getInstance().isSameObject(itemIO,
									slotIO)) {
						// found a matching item - try to stack
						int slotCount = slotIO.getItemData().getCount();
						int itemCount = itemIO.getItemData().getCount();
						int slotMaxStack =
								slotIO.getItemData().getStackSize();
						if (slotCount < slotMaxStack) {
							// there's room to stack more - stack it
							slotIO.getItemData().adjustCount(itemCount);
							// check to see if too many are stacked
							slotCount = slotIO.getItemData().getCount();
							if (slotCount > slotMaxStack) {
								// remove excess from stack
								// and put it back into item io
								itemIO.getItemData().setCount(
										slotCount - slotMaxStack);
								slotIO.getItemData().setCount(slotMaxStack);
							} else {
								// no excess. remove count from item io
								itemIO.getItemData().setCount(0);
							}
							// was item count set to 0? release the IO
							if (itemIO.getItemData().getCount() == 0) {
								if (itemIO.isScriptLoaded()) {
									int inner = Interactive.getInstance()
											.getMaxIORefId();
									for (; inner >= 0; inner--) {
										if (Interactive.getInstance().hasIO(
												inner)) {
											IO innerIO = (IO)
													Interactive.getInstance()
													.getIO(inner);
											if (innerIO.equals(itemIO)) {
												Interactive.getInstance()
												.releaseIO(innerIO);
												innerIO = null;
											}
										}
									}
								} else {
									itemIO.setShow(IoGlobals.SHOW_FLAG_KILLED);
								}
							}
							// declare item in inventory
							ARX_INVENTORY_Declare_InventoryIn(io, slotIO);
							can = true;
							break;
						}
					}
				}
			}
			// cant stack the item? find an empty slot
			if (!can) {
				// find an empty slot for the item
				for (int i = numInventorySlots - 1; i >= 0; i--) {
					// got an empty slot - add it
					if (slots[i].getIo() == null) {
						slots[i].setIo(itemIO);
						slots[i].setShow(true);
						ARX_INVENTORY_Declare_InventoryIn(io, itemIO);
						can = true;
						break;
					}
				}
			}
		}
		return can;
	}
	/**
	 * UNTESTED DO NOT USE Replaces an item in an IO's inventory.
	 * @param itemIO the item being added
	 * @param old the item being replaced
	 * @throws RPGException if an error occurs
	 */
	public final void CheckForInventoryReplaceMe(final IO itemIO, final IO old)
			throws RPGException {
		if (itemIO != null
				&& old != null) {
			boolean handled = false;
			if (io.hasIOFlag(IoGlobals.IO_01_PC)) {
				if (IsInPlayerInventory(old)) {
					if (CanBePutInInventory(itemIO)) {
						handled = true;
					} else {
						PutInFrontOfPlayer(itemIO, true);
						handled = true;
					}
				}
			}
			if (!handled) {
				int i = Interactive.getInstance().getMaxIORefId();
				for (; i >= 0; i--) {
					IO io = (IO) Interactive.getInstance().getIO(i);
					if (io != null
							&& io.getInventory() != null) {
						InventoryData id = io.getInventory();
						if (id.IsInPlayerInventory(old)) {
							if (CanBePutInInventory(itemIO)) {
								handled = true;
								break;
							} else {
								this.PutInFrontOfPlayer(itemIO, true);
								handled = true;
								break;
							}
						}
						id = null;
					}
				}
			}
		}
	}
	/** Removes all items from inventory. */
	public final void CleanInventory() {
		for (int i = numInventorySlots - 1; i >= 0; i--) {
			slots[i].setIo(null);
			slots[i].setShow(true);
		}
	}
	/**
	 * Forces all items to be set at a specific level.
	 * @param level the level
	 */
	public final void ForcePlayerInventoryObjectLevel(final int level) {
		for (int i = numInventorySlots - 1; i >= 0; i--) {
			if (slots[i].getIo() != null) {
				slots[i].getIo().setLevel(level);
			}
		}
	}
	/**
	 * Gets the IO associated with this {@link InventoryData}.
	 * @return {@link IO}
	 */
	public final IO getIo() {
		return io;
	}
	/**
	 * Gets the number of inventory slots.
	 * @return <code>int</code>
	 */
	public final int getNumInventorySlots() {
		return numInventorySlots;
	}
	/**
	 * Gets the inventory slot at the specific index.
	 * @param index the slot index
	 * @return {@link SLOT}
	 */
	public final SLOT getSlot(final int index) {
		return slots[index];
	}
	/**
	 * Determines if an item is in inventory.
	 * @param io the item
	 * @return <tt>true</tt> if the item is in inventory; <tt>false</tt>
	 *         otherwise
	 */
	public final boolean IsInPlayerInventory(final IO io) {
		boolean is = false;
		for (int i = numInventorySlots - 1; i >= 0; i--) {
			IO ioo = (IO) slots[i].getIo();
			if (ioo != null
					&& ioo.equals(io)) {
				is = true;
				break;
			}
		}
		return is;
	}
	/**
	 * Puts an item in front of the player.
	 * @param itemIO the item
	 * @param doNotApplyPhysics if <tt>true</tt>, do not apply physics
	 */
	public abstract void PutInFrontOfPlayer(IO itemIO,
			boolean doNotApplyPhysics);
	/**
	 * Replaces an item in all inventories.
	 * @param oldItemIO the old item being replaced
	 * @param newItemIO the new item
	 * @throws RPGException if an error occurs
	 */
	public final void ReplaceInAllInventories(final IO oldItemIO,
			final IO newItemIO)
					throws RPGException {
		if (oldItemIO != null
				&& !oldItemIO.hasIOFlag(IoGlobals.IO_15_MOVABLE)
				&& newItemIO != null
						&& !newItemIO.hasIOFlag(IoGlobals.IO_15_MOVABLE)) {
			int oldIORefId = Interactive.getInstance().GetInterNum(oldItemIO);
			int newIORefId = Interactive.getInstance().GetInterNum(newItemIO);
			int i = Interactive.getInstance().getMaxIORefId();
			for (; i >= 0; i--) {
				if (i == oldIORefId
						|| i == newIORefId
						|| !Interactive.getInstance().hasIO(i)) {
					continue;
				}
				IO invOwner = (IO) Interactive.getInstance().getIO(i);
				if (invOwner.getInventory() != null) {
					InventoryData inv = invOwner.getInventory();
					for (int j = inv.numInventorySlots - 1; j >= 0; j--) {
						if (inv.slots[j].getIo().equals(oldItemIO)) {
							inv.slots[j].setIo(newItemIO);
						}
					}
				}
			}
		}
	}
	/**
	 * Sends a scripted command to an item in inventory.
	 * @param itemName the item name
	 * @param message the message
	 * @throws RPGException if an error occurs
	 */
	public final void SendInventoryObjectCommand(final String itemName,
			final int message) throws RPGException {
		if (itemName != null
				&& itemName.length() > 0) {
			for (int i = numInventorySlots - 1; i >= 0; i--) {
				IO slotIO = (IO) slots[i].getIo();
				if (slotIO != null
						&& slotIO.hasGameFlag(IoGlobals.GFLAG_INTERACTIVITY)
						&& slotIO.getItemData() != null) {
					String ioName =
							new String(slotIO.getItemData().getItemName());
					if (itemName.equalsIgnoreCase(ioName)) {
						Script.getInstance().sendIOScriptEvent(
								slotIO, message, null, "");
						slotIO = null;
						break;
					}
					ioName = null;
				}
				slotIO = null;
			}
		}
	}
	/**
	 * Sets the IO associated with the inventory.
	 * @param newIO the IO to set
	 */
	public final void setIo(final IO newIO) {
		this.io = newIO;
	}
	/**
	 * Sets the inventory slots.
	 * @param val the inventory slots
	 */
	protected final void setSlots(final SLOT[] val) {
		slots = val;
		numInventorySlots = slots.length;
	}
	/** flag indicating the left ring needs to be replaced. */
	private boolean	leftRing	= false;
	/**
	 * Sets the value of the flag indicating whether the left ring is the next
	 * one that needs to be switched.
	 * @param flag the new value to set
	 */
	public void setLeftRing(final boolean flag) {
		this.leftRing = flag;
	}
	/**
	 * Gets the flag indicating whether the left ring is the next one that needs
	 * to be switched.
	 * @return <tt>true</tt> if the left ring should be switched; <tt>false</tt>
	 * otherwise
	 */
	public boolean isLeftRing() {
		return leftRing;
	}
}
