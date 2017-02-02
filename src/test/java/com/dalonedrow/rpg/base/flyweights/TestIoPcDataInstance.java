package com.dalonedrow.rpg.base.flyweights;

/**
 * Test class.
 * @author 588648
 *
 */
public class TestIoPcDataInstance extends IoPcData {
	public TestIoPcDataInstance() throws RPGException {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void adjustMana(final float dmg) {
		super.setBaseAttributeScore("MN",
				super.getBaseAttributeScore("MN") + dmg);
        if (super.getBaseAttributeScore(
        		"LF") > super.getFullAttributeScore("MMN")) {
        	super.setBaseAttributeScore("MN",
        			super.getFullAttributeScore("MMN"));
        }
		if (super.getBaseAttributeScore("MN") < 0f) {
			super.setBaseAttributeScore("MN", 0f);
		}
	}
	@Override
	public void ARX_EQUIPMENT_RecreatePlayerMesh() {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean canIdentifyEquipment(final IOEquipItem equipitem) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected float getBaseLife() {
		return this.getBaseAttributeScore("LF");
	}
	@Override
	protected float getBaseMana() {
		return this.getBaseAttributeScore("MN");
	}
	@Override
	public boolean isInCombat() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected String getLifeAttribute() {
		return "LF";
	}
	@Override
	protected void applyRulesPercentModifiers() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void applyRulesModifiers() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected Object[][] getAttributeMap() {
		return new Object[][] {
			{ "ST", "Strength", 0 },
			{ "LF", "Life", 0 },
			{ "MLF", "Max Life", 1 },
			{ "MN", "Mana", 1 },
			{ "MMN", "Max Mana", 1 }
		};
	}
    @Override
    public boolean calculateBackstab() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean calculateCriticalHit() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public float getFullDamage() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public float getMaxLife() {
        // TODO Auto-generated method stub
        return 0;
    }
}
