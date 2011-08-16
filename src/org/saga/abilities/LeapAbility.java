package org.saga.abilities;

import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnActivateAbility;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class LeapAbility extends AbilityFunction implements OnActivateAbility{

	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "leap";

	/**
	 * Leap height.
	 */
	private Double leapHeightVelocity;
	
	/**
	 * Used by gson.
	 * 
	 */
	public LeapAbility() {
		
        super(ABILITY_NAME, AbilityActivateType.INSTANTANEOUS);
	
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.AbilityFunction#completeSecondExtended()
	 */
	@Override
	public boolean completeSecondExtended() {
		
		
		boolean integrity = true;
		
		// General fields:
		if(leapHeightVelocity==null){
			leapHeightVelocity = 0.5;
			Saga.info(getAbilityName() + " ability leapHeightVelocity field not initialized. Setting default.");
			integrity = false;
		}
		
		return integrity;
		
		
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnActivateAbility#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession)
	 */
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession) {
		
		System.out.println("leap");
		
		// Set speeds:
		sagaPlayer.addLeapHorizontalSpeed(calculateFunctionValue(level));
		sagaPlayer.addLeapVerticalSpeed(leapHeightVelocity);
		
		// Notify:
		sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
		
		
	}
	

}
