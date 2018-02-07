package com.dalonedrow.rpg.base.flyweights;

public class TestNpcData extends IoNpcData<TestInteractiveObject> {

	public TestNpcData() throws RPGException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void adjustLife(float dmg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void adjustMana(float dmg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ARX_NPC_ManagePoison() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void awardXpForNpcDeath(int xp, TestInteractiveObject killerIO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void damageNonLivingNPC(float dmg, int srcIoid, boolean isSpellDamage) throws RPGException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getBaseLife() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBaseMana() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPoisonned() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean hasLifeRemaining() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void moveToInitialPosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void restoreLifeToMax() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stopActiveAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stopIdleAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void applyRulesModifiers() throws RPGException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void applyRulesPercentModifiers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ARX_EQUIPMENT_RecreatePlayerMesh() {
		// TODO Auto-generated method stub
		
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
	protected Object[][] getAttributeMap() {
		// TODO Auto-generated method stub
		return null;
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
