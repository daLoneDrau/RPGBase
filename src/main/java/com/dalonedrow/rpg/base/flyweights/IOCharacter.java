package com.dalonedrow.rpg.base.flyweights;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dalonedrow.engine.systems.base.Interactive;
import com.dalonedrow.engine.systems.base.ProjectConstants;
import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.constants.IoGlobals;
import com.dalonedrow.rpg.base.constants.MathGlobals;
import com.dalonedrow.utils.ArrayUtilities;
import com.dalonedrow.utils.Watchable;
import com.dalonedrow.utils.Watcher;

/**
 * @author 588648
 * @param <IO>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class IOCharacter<IO extends BaseInteractiveObject>
        implements Watchable {
    public abstract boolean calculateBackstab();
    public abstract boolean calculateCriticalHit();
    /** the set of attributes defining the PC. */
    private Map<String, Attribute> attributes;
    /**
     * the reference ids of all items equipped by the {@link IOCharacter},
     * indexed by equipment slot.
     */
    private int[] equippedItems;
    /**
     * the list of {@link Watcher}s associated with this {@link IOCharacter}.
     */
    private Watcher[] watchers = new Watcher[0];
    protected IOCharacter() throws RPGException {
        watchers = new Watcher[0];
        defineAttributes();
        initEquippedItems(ProjectConstants.getInstance().getMaxEquipped());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final void addWatcher(final Watcher watcher) {
        if (watcher != null) {
            int index = -1;
            for (int i = watchers.length - 1; i >= 0; i--) {
                if (watchers[i] == null) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                watchers[index] = watcher;
            } else {
                watchers = ArrayUtilities.getInstance().extendArray(
                        watcher, watchers);
            }
        }
    }
    /**
     * Adjusts the attribute modifier for a specific attribute.
     * @param attr the attribute name
     * @param val the modifier
     * @throws RPGException if the attribute name is missing or incorrect
     */
    public final void adjustAttributeModifier(final String attr,
            final float val) throws RPGException {
        if (attr == null) {
            throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT,
                    "Attribute name cannot be null");
        }
        if (!attributes.containsKey(attr)) {
            throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT,
                    "No such attribute " + attr);
        }
        attributes.get(attr).adjustModifier(val);
    }
    protected abstract void applyRulesModifiers() throws RPGException;
    protected abstract void applyRulesPercentModifiers();
    /**
     * Gets the total modifier for a specific element type from the equipment
     * the player is wielding.
     * @param elementType the type of element
     * @return {@link float}
     * @throws RPGException if an error occurs
     */
    public final float ARX_EQUIPMENT_Apply(final int elementType)
            throws RPGException {
        float toadd = 0;
        int i = ProjectConstants.getInstance().getMaxEquipped() - 1;
        for (; i >= 0; i--) {
            if (equippedItems[i] >= 0
                    && Interactive.getInstance().hasIO(equippedItems[i])) {
                IO toequip =
                        (IO) Interactive.getInstance().getIO(equippedItems[i]);
                if (toequip.hasIOFlag(IoGlobals.IO_02_ITEM)
                        && toequip.getItemData() != null
                        && toequip.getItemData().getEquipitem() != null) {
                    EquipmentItemModifier element =
                            toequip.getItemData().getEquipitem().getElement(
                                    elementType);
                    if (!element.isPercentage()) {
                        toadd += element.getValue();
                    }
                }
                toequip = null;
            }
        }
        return toadd;
    }
    /**
     * Gets the total percentage modifier for a specific element type from the
     * equipment the player is wielding.
     * @param elementType the type of element
     * @param trueval the true value
     * @return {@link float}
     * @throws RPGException if an error occurs
     */
    public final float ARX_EQUIPMENT_ApplyPercent(final int elementType,
            final float trueval) throws RPGException {
        float toadd = 0;
        int i = ProjectConstants.getInstance().getMaxEquipped() - 1;
        for (; i >= 0; i--) {
            if (equippedItems[i] >= 0
                    && Interactive.getInstance().hasIO(equippedItems[i])) {
                IO toequip =
                        (IO) Interactive.getInstance().getIO(equippedItems[i]);
                if (toequip.hasIOFlag(IoGlobals.IO_02_ITEM)
                        && toequip.getItemData() != null
                        && toequip.getItemData().getEquipitem() != null) {
                    EquipmentItemModifier element =
                            toequip.getItemData().getEquipitem().getElement(
                                    elementType);
                    if (element.isPercentage()) {
                        toadd += element.getValue();
                    }
                }
                toequip = null;
            }
        }
        return toadd * trueval * MathGlobals.DIV100;
    }
    public abstract void ARX_EQUIPMENT_RecreatePlayerMesh();
    /**
     * Releases an equipped IO.
     * @param id the IO's reference id
     * @throws RPGException if an error occurs
     */
    public final void ARX_EQUIPMENT_Release(final int id) throws RPGException {
        int i = ProjectConstants.getInstance().getMaxEquipped() - 1;
        for (; i >= 0; i--) {
            if (equippedItems[i] == id) {
                equippedItems[i] = -1;
            }
        }
    }
    /**
     * Removes all the player's equipment.
     * @throws PooledException if an error occurs
     * @throws RPGException if an error occurs
     */
    public final void ARX_EQUIPMENT_UnEquipAll()
            throws RPGException {
        int i = ProjectConstants.getInstance().getMaxEquipped() - 1;
        for (; i >= 0; i--) {
            if (equippedItems[i] >= 0) {
                if (!Interactive.getInstance().hasIO(equippedItems[i])) {
                    throw new RPGException(ErrorMessage.INVALID_DATA_TYPE,
                            "Equipped unregistered item in slot " + i);
                }
                IO itemIO = (IO) Interactive.getInstance().getIO(
                        equippedItems[i]);
                if (!itemIO.hasIOFlag(IoGlobals.IO_02_ITEM)) {
                    throw new RPGException(ErrorMessage.INVALID_DATA_TYPE,
                            "Equipped item without IO_02_ITEM in slot " + i);
                }
                if (itemIO.getItemData() == null) {
                    throw new RPGException(ErrorMessage.INVALID_DATA_TYPE,
                            "Equipped item with null item data in slot " + i);
                }
                itemIO.getItemData().ARX_EQUIPMENT_UnEquip(getIo(), false);
            }
        }
        computeFullStats();
    }
    /**
     * Clears the attribute modifier for a specific attribute.
     * @param attr the attribute name
     */
    public final void clearAttributeModifier(final String attr) {
        attributes.get(attr).clearModifier();
    }
    /** Clears all ability score modifiers. */
    public final void clearModAbilityScores() {
        if (attributes != null) {
            Iterator<String> iter = attributes.keySet().iterator();
            while (iter.hasNext()) {
                Attribute attr = attributes.get(iter.next());
                attr.clearModifier();
            }
        }
    }
    /**
     * Compute FULL versions of player stats including equipped items, spells,
     * and any other effect altering them.
     * @throws RPGException if an error occurs
     */
    public final void computeFullStats() throws RPGException {
        // clear mods
        clearModAbilityScores();
        // apply equipment modifiers
        Object[][] map = getAttributeMap();
        for (int i = map.length - 1; i >= 0; i--) {
            adjustAttributeModifier((String) map[i][0],
                    ARX_EQUIPMENT_Apply((int) map[i][2]));
        }
        // apply modifiers based on rules
        applyRulesModifiers();
        // apply percent modifiers
        for (int i = map.length - 1; i >= 0; i--) {
            float percentModifier = ARX_EQUIPMENT_ApplyPercent((int) map[i][2],
                    getBaseAttributeScore((String) map[i][0])
                            + getAttributeModifier((String) map[i][0]));
            adjustAttributeModifier((String) map[i][0], percentModifier);
        }
        // apply percent modifiers based on rules
        applyRulesPercentModifiers();
    }
    /**
     * Defines the PC's attributes.
     * @throws RPGException if an error occurs
     */
    protected final void defineAttributes() throws RPGException {
        attributes = new HashMap<String, Attribute>();
        Object[][] map = getAttributeMap();
        for (int i = map.length - 1; i >= 0; i--) {
            attributes.put((String) map[i][0],
                    new Attribute((String) map[i][0], (String) map[i][1]));
        }
        map = null;
    }
    /**
     * Gets a specific attribute.
     * @param abbr the attribute's abbreviation
     * @return {@link Attribute}
     */
    protected final Attribute getAttribute(final String abbr) {
        return attributes.get(abbr);
    }
    protected abstract Object[][] getAttributeMap();
    /**
     * Gets the attribute modifier for a specific attribute.
     * @param attr the attribute name
     * @return {@link float}
     */
    public final float getAttributeModifier(final String attr) {
        return attributes.get(attr).getModifier();
    }
    /**
     * Gets an attribute's display name.
     * @param attr the attribute's abbreviation
     * @return {@link String}
     */
    public final String getAttributeName(final String attr) {
        return new String(attributes.get(attr).getDisplayName());
    }
    /**
     * Gets all attributes.
     * @return {@link Map}<{@link String}, {@link Attribute}>
     */
    protected final Map<String, Attribute> getAttributes() {
        return attributes;
    }
    /**
     * Gets the base attribute score for a specific attribute.
     * @param attr the attribute name
     * @return {@link float}
     */
    public final float getBaseAttributeScore(final char[] attr) {
        return getBaseAttributeScore(new String(attr));
    }
    /**
     * Gets the base attribute score for a specific attribute.
     * @param attr the attribute name
     * @return {@link float}
     */
    public final float getBaseAttributeScore(final String attr) {
        return attributes.get(attr).getBase();
    }
    /**
     * Gets the reference id of the item the {@link IOCharacter} has equipped at
     * a specific equipment slot. -1 is returned if no item is equipped.
     * @param slot the equipment slot
     * @return <code>int</code>
     * @throws RPGException if the equipment slot was never defined
     */
    public final int getEquippedItem(final int slot) throws RPGException {
        int id = -1;
        if (slot < 0
                || slot >= equippedItems.length) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Error - equipment slot ");
                sb.append(slot);
                sb.append(" is outside array bounds.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(
                    ErrorMessage.BAD_PARAMETERS, sb.toString());
            sb.returnToPool();
            throw ex;
        }
        id = equippedItems[slot];
        return id;
    }
    /**
     * Gets the full attribute score for a specific attribute.
     * @param attr the attribute name
     * @return {@link float}
     */
    public final float getFullAttributeScore(final String attr) {
        return attributes.get(attr).getFull();
    }
    public abstract float getFullDamage();
    /**
     * Gets the IO associated with this {@link IOCharacter}.
     * @return {@link IO}
     */
    public abstract IO getIo();
    /**
     * Initializes the items the {@link IOCharacter} has equipped.
     * @param total the total number of equipment slots
     */
    private void initEquippedItems(final int total) {
        equippedItems = new int[total];
        for (int i = 0; i < equippedItems.length; i++) {
            equippedItems[i] = -1;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final void notifyWatchers() {
        for (int i = watchers.length - 1; i >= 0; i--) {
            watchers[i].watchUpdated(this);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeWatcher(final Watcher watcher) {
        int index = -1;
        for (int i = watchers.length - 1; i >= 0; i--) {
            if (watchers[i].equals(watcher)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            watchers =
                    ArrayUtilities.getInstance().removeIndex(index, watchers);
        }
    }
    /**
     * Sets the base attribute score for a specific attribute.
     * @param attr the attribute name
     * @param val the new base attribute score
     */
    public final void setBaseAttributeScore(final String attr,
            final float val) {
        attributes.get(attr).setBase(val);
    }
    /**
     * Sets the reference id of the item the {@link IOCharacter} has equipped at
     * a specific equipment slot.
     * @param slot the equipment slot
     * @param item the item being equipped
     * @throws RPGException if the equipment slot was never defined
     */
    public final void setEquippedItem(final int slot,
            final BaseInteractiveObject item)
            throws RPGException {
        if (slot < 0
                || slot >= equippedItems.length) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Error - equipment slot ");
                sb.append(slot);
                sb.append(" is outside array bounds.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(
                    ErrorMessage.BAD_PARAMETERS, sb.toString());
            sb.returnToPool();
            throw ex;
        }
        if (item == null) {
            equippedItems[slot] = -1;
        } else {
            equippedItems[slot] = item.getRefId();
        }
    }
    /**
     * Sets the reference id of the item the {@link IOCharacter} has equipped at
     * a specific equipment slot.
     * @param slot the equipment slot
     * @param id the item's reference id
     * @throws RPGException if the equipment slot was never defined
     */
    public final void setEquippedItem(final int slot, final int id)
            throws RPGException {
        if (slot < 0
                || slot >= equippedItems.length) {
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            try {
                sb.append("Error - equipment slot ");
                sb.append(slot);
                sb.append(" is outside array bounds.");
            } catch (PooledException e) {
                throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
            }
            RPGException ex = new RPGException(
                    ErrorMessage.BAD_PARAMETERS, sb.toString());
            sb.returnToPool();
            throw ex;
        }
        equippedItems[slot] = id;
    }
}
