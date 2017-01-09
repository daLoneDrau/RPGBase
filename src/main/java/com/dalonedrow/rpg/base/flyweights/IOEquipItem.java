/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.engine.systems.base.ProjectConstants;

/**
 * @author Donald
 */
public final class IOEquipItem {
    /** the list of equipment modifiers. */
    private final EquipmentItemModifier[] elements;
    /**
     * Creates a new instance of {@link IOEquipItem}. 
     * @throws RPGException
     */
    public IOEquipItem() {
    	int numElements =
    			ProjectConstants.getInstance().getNumberEquipmentElements();
        elements = new EquipmentItemModifier[numElements];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new EquipmentItemModifier();
        }
    }
	/** Frees all resources. */
	public void free() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
	}
	/**
	 * Gets the element.
	 * @param element the element
	 * @return {@link EquipmentItemModifier}
	 */
	public EquipmentItemModifier getElement(final int element) {
		return elements[element];
	}
	/** Resets all modifiers. */
	public void reset() {
		for (int i = 0; i < elements.length; i++) {
			elements[i].clearData();
		}
	}
}
