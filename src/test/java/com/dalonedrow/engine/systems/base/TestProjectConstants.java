package com.dalonedrow.engine.systems.base;

import com.dalonedrow.rpg.base.flyweights.RPGException;

public class TestProjectConstants extends ProjectConstants {
	/* (non-Javadoc)
     * @see com.dalonedrow.engine.systems.base.ProjectConstants#getDamageElementIndex()
     */
    @Override
    public int getDamageElementIndex() {
        // TODO Auto-generated method stub
        return 0;
    }
    public TestProjectConstants() {
		super.setInstance(this);
	}
	/* (non-Javadoc)
	 * @see com.dalonedrow.engine.systems.base.ProjectConstants#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.dalonedrow.engine.systems.base.ProjectConstants#getMaxEquipped()
	 */
	@Override
	public int getMaxEquipped() {
		// TODO Auto-generated method stub
		return 8;
	}

	/* (non-Javadoc)
	 * @see com.dalonedrow.engine.systems.base.ProjectConstants#getMaxSpells()
	 */
	@Override
	public int getMaxSpells() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.dalonedrow.engine.systems.base.ProjectConstants#getPlayer()
	 */
	@Override
	public int getPlayer() throws RPGException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getNumberEquipmentElements() {
		// TODO Auto-generated method stub
		return 2;
	}

}
