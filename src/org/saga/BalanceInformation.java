package org.saga;

import java.util.*;

import org.saga.abilities.*;

public class BalanceInformation {

	
	// Player:
	/**
	 * Maximum stamina.
	 */
	public Double maximumStamina;

	/**
	 * Stamina gain per second.
	 */
	public Double staminaPerSecond;

	// Profession general:
	/**
	 * Experience Intercept.
	 */
	public Integer experienceIntercept;

	/**
	 * Experience slope.
	 */
	public Integer experienceSlope;

	/**
	 * All abilities.
	 */
	public Hashtable<String, Ability> abilities = new Hashtable<String, Ability>();
	
	
	/**
	 * Sets defaults by checking integrity and adds abilities.
	 */
	public BalanceInformation() {
		
		
		// Add all abilities:
		Ability ability;
		// Counterattack:
		ability= new CounterattackAbility();
		abilities.put(ability.getClass().getSimpleName(), ability);
		
		// Disarm:
		ability= new DisarmAbility();
		abilities.put(ability.getClass().getSimpleName(), ability);
		
		// Set fields:
        maximumStamina= 100.0;
        staminaPerSecond= 0.1;
        staminaPerSecond= 0.1;

		
	}
	
	
	// Calculations:
	/**
	 * Returns the required experience for level up.
	 *
	 * @param pLevel
	 */
	public Integer calculateExperienceRequirement(Short pLevel) {

		return pLevel*experienceSlope*pLevel+experienceIntercept;

	}
	
	
}
