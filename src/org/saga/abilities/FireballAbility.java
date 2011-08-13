package org.saga.abilities;

import org.saga.SagaPlayer;

public class FireballAbility extends AbilityFunction {

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "fireball";

	
	/**
	 * Used by gson.
	 * 
	 */
	public FireballAbility() {
		
        super(ABILITY_NAME, AbilityActivateType.SINGLE_USE);
	
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.AbilityFunction#completeSecondExtended()
	 */
	@Override
	public boolean completeSecondExtended() {
		// TODO Auto-generated method stub
		return false;
	}

	
	/**
	 * Uses the ability
	 * 
	 * @param level level
	 * @param sagaPlayer saga player
	 */
	public void use(Short level, SagaPlayer sagaPlayer) {
		
		sagaPlayer.shootFireball(calculateFunctionValue(level));
		
	}
	
	
}
