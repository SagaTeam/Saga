package org.saga.abilities;

import java.util.*;

public abstract class Ability {

	
	/**
	 * Class name used by the loader
	 */
	private String _className;
	
	// General variables:
	/**
	 * Ability name.
	 */
	private String abilityName;
	
	/**
	 * Stamina function x1.
	 */
	private Short levelStaminaFunctionX1;
	
	/**
	 * Stamina function y1.
	 */
	private Double levelStaminaFunctionY1;
	
	/**
	 * Stamina function x2.
	 */
	private Short levelStaminaFunctionX2;
	
	/**
	 * Stamina function y2
	 */
	private Double levelStaminaFunctionY2;
	
	
	// Initialization:
	/**
	 * Forces to give an ability name.
	 * 
	 * @param abilityName ability name
	 */
	public Ability(String abilityName, String className) {
		
		this.abilityName=abilityName;
		
		// Class name:
		_className = className;
		
		// Set defaults:
        abilityName = "null";
        levelStaminaFunctionX1 = 100;
        levelStaminaFunctionY1 = 100.0;
        levelStaminaFunctionX2 = 100;
        levelStaminaFunctionY2 = 100.0;
		
        
        
		
	}
	
	public Ability() {
		// TODO Auto-generated constructor stub
	}
	
	
	// Interaction:
	/**
	 * Calculates the stamina drain for the given level.
	 * 
	 * @param level level
	 */
	public void calculateStaminaDrain(int level) {

		// TODO Finish stamina drain calculation
		
	}
	
	/**
	 * Returns the ability name.
	 * 
	 * @return ability name
	 */
	public String getAbilityName() {
		return abilityName;
	}
	
	/**
	 * Checks if the level is high enough to use the ability.
	 * 
	 * @param level level.
	 * 
	 * @return true if the level is high enough
	 */
	public boolean levelHighEnough(Short level) {

            return levelStaminaFunctionX1 >= level;
		
	}
	
	
	
}
