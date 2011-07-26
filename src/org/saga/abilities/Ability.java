package org.saga.abilities;

import java.util.*;

import org.saga.Saga;

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
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 * 
	 * @return true if everything was correct.
	 */
	public boolean complete() {
		
		
		boolean integrity = true;
		
		// Fields:
		if(levelStaminaFunctionX1==null){
			levelStaminaFunctionX1 = 10;
			Saga.info("Setting default value for ability levelStaminaFunctionX1.");
			integrity = false;
		}
		if(levelStaminaFunctionY1==null){
			levelStaminaFunctionY1 = 1000.0;
			Saga.info("Setting default value for ability levelStaminaFunctionY1.");
			integrity = false;
		}
		if(levelStaminaFunctionX2==null){
			levelStaminaFunctionX2 = 100;
			Saga.info("Setting default value for ability levelStaminaFunctionX2.");
			integrity = false;
		}
		if(levelStaminaFunctionY2==null){
			levelStaminaFunctionY2 = 1000.0;
			Saga.info("Setting default value for ability levelStaminaFunctionY2.");
			integrity = false;
		}
		
		// Inheriting class:
		if(!completeInheriting()){
			integrity = false;
		}
		
		return integrity;
		

	}
	
	/**
	 * Does a complete for all inheriting classes.
	 * 
	 * @return true if everything was correct.
	 */
	public abstract boolean completeInheriting();
	
	/**
	 * Gets the minimum required level.
	 * 
	 * @return minimum level to use
	 */
	public Short minimumLevel() {
		return levelStaminaFunctionX1;
	}
	
	
	// Interaction:
	/**
	 * Calculates the stamina drain for the given level.
	 * 
	 * @param level level
	 */
	public Double calculateStaminaUse(Short level) {

		if(level>levelStaminaFunctionX2){
			level = levelStaminaFunctionX2;
		}
		double k= (levelStaminaFunctionY2-levelStaminaFunctionY1)/(levelStaminaFunctionX2-levelStaminaFunctionX1);
		double b= levelStaminaFunctionY2 - k*levelStaminaFunctionX2;
		return k * level + b;
		
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
	 * Gets the level requirement for the ability.
	 * 
	 * 
	 * @return level requirement
	 */
	public Short getLevelRequirement() {
		return levelStaminaFunctionX1;
	}
	
	/**
	 * Checks if the level is high enough to use the ability.
	 * 
	 * @param level level.
	 * 
	 * @return true if the level is high enough
	 */
	public boolean levelHighEnough(Short level) {
            return levelStaminaFunctionX1 <= level;
	}
	
	
}
