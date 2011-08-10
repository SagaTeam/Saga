package org.saga;

import java.util.*;

import org.saga.abilities.*;
import org.saga.professions.FarmerProfession;
import org.saga.professions.FighterProfession;
import org.saga.professions.MinerProfession;
import org.saga.professions.Profession;
import org.saga.professions.WoodcutterProfession;

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
	 * Maximum level.
	 */
	public Short maximumLevel;
	
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
	 * Used by gson.
	 */
	public BalanceInformation() {
		
		
	}
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 * 
	 * @return true if everything was correct.
	 */
	public boolean complete() {
		
		
		// Fields:
		// Player general:
		boolean integrity=true;
		if(maximumStamina == null){
			Saga.warning("Setting default value for balance information maximumStamina.");
			maximumStamina= 100.0;
			integrity=false;
		}
		if(staminaPerSecond == null){
			Saga.warning("Setting default value for balance information staminaPerSecond.");
			staminaPerSecond= 0.1;
			integrity=false;
		}
		// Profession general:
		if(maximumLevel == null){
			Saga.warning("Setting default value for balance information maximumLevel.");
			maximumLevel= 1;
			integrity=false;
		}
		if(experienceIntercept == null){
			Saga.warning("Setting default value for balance information experienceIntercept.");
			experienceIntercept= 10000;
			integrity=false;
		}
		if(experienceSlope == null){
			Saga.warning("Setting default value for balance information experienceSlope.");
			experienceSlope= 10000;
			integrity=false;
		}
		
		
		// Complete abilities:
		Ability[] allAbilities = getAllAbilities();
		for (int i = 0; i < allAbilities.length; i++) {
			Ability ability = abilities.get(allAbilities[i].getClass().getSimpleName());
			if(ability == null){
				ability = allAbilities[i];
				Saga.warning("Adding "+allAbilities[i].getClass().getSimpleName()+" ability to balance information and setting default values.");
				abilities.put(ability.getClass().getSimpleName(), ability);
				integrity=false;
			}
			if(!ability.complete()){
				integrity = false;
			}
			
		}
		
		return integrity;
		
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
	
	/**
	 * Returns all available professions.
	 * 
	 * @return all professions
	 */
	public Profession[] getAllProfessions() {
		
		return new Profession[]{new FighterProfession(), new WoodcutterProfession(), new MinerProfession(), new FarmerProfession()};

	}
	
	/**
	 * Returns all abilities.
	 * 
	 * @return all abilities
	 */
	private Ability[] getAllAbilities() {

		return new Ability[]{new HeavyHitAbility(), new CounterattackAbility(), new DisarmAbility(), new PowerfulSwings(), new ResistLavaAbility(), new FocusedHitsAbility(), new ChopDownAbility(), new TreeClimbAbility(), new HarvestAbility()};
		
	}
	
}
