package org.saga.abilities;

import org.saga.SagaPlayer;
import org.saga.abilities.types.OnActivateAbility;
import org.saga.professions.Profession;

public class FireballAbility extends AbilityFunction implements OnActivateAbility{

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "fireball";

	
	/**
	 * Used by gson.
	 * 
	 */
	public FireballAbility() {
		
        super(ABILITY_NAME, AbilityActivateType.INSTANTANEOUS);
	
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

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnActivateAbility#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession)
	 */
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession) {
		
		sagaPlayer.shootFireball(calculateFunctionValue(level));
		
	}
	
	
}
