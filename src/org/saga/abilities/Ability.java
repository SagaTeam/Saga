package org.saga.abilities;

import org.saga.Saga;

public abstract class Ability {

	
	/**
	 * Class name used by the loader
	 */
	@SuppressWarnings("unused")
	private final String _className;
	
	// General variables:
	/**
	 * Ability name.
	 */
	private String abilityName;
	
	/**
	 * Stamina function x1.
	 */
	private Short staminaFunctionX1;
	
	/**
	 * Stamina function y1.
	 */
	private Double staminaFunctionY1;
	
	/**
	 * Stamina function x2.
	 */
	private Short staminaFunctionX2;
	
	/**
	 * Stamina function y2.
	 */
	private Double staminaFunctionY2;
	
	
	/**
	 * Ability active for function x1.
	 */
	transient private Short activeForFunctionX1;
	
	/**
	 * Ability active for function y1.
	 */
	private Short activeForFunctionY1;
	
	/**
	 * Ability active for function x2.
	 */
	private Short activeForFunctionX2;
	
	/**
	 * Ability active for function y2.
	 */
	private Short activeForFunctionY2;
	
	
	
	// Initialization:
	/**
	 * Forces to give an ability name.
	 * 
	 * @param abilityName ability name
	 */
	public Ability(String abilityName) {
		
		this.abilityName=abilityName;
		
		// Class name:
		_className = getClass().getName();
		
		// Set defaults:
        abilityName = "null";
        staminaFunctionX1 = 100;
        staminaFunctionY1 = 100.0;
        staminaFunctionX2 = 100;
        staminaFunctionY2 = 100.0;
		
        
        
		
	}
	
	/**
	 * Used by gson.
	 * 
	 */
	public Ability() {
		_className = getClass().getName();
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
		if(staminaFunctionX1==null){
			staminaFunctionX1 = 10;
			Saga.info("Setting default value for ability staminaFunctionX1.");
			integrity = false;
		}
		if(staminaFunctionY1==null){
			staminaFunctionY1 = 1000.0;
			Saga.info("Setting default value for ability staminaFunctionY1.");
			integrity = false;
		}
		if(staminaFunctionX2==null){
			staminaFunctionX2 = 100;
			Saga.info("Setting default value for ability staminaFunctionX2.");
			integrity = false;
		}
		if(staminaFunctionY2==null){
			staminaFunctionY2 = 1000.0;
			Saga.info("Setting default value for ability staminaFunctionY2.");
			integrity = false;
		}
		
		activeForFunctionX1 = staminaFunctionX1;
		
		
		if(activeForFunctionY1==null){
			activeForFunctionY1 = 10;
			Saga.info("Setting default value for ability activeForFunctionY1.");
			integrity = false;
		}
		if(activeForFunctionX2==null){
			activeForFunctionX2 = 1000;
			Saga.info("Setting default value for ability activeForFunctionX2.");
			integrity = false;
		}
		if(activeForFunctionY2==null){
			activeForFunctionY2 = 10;
			Saga.info("Setting default value for ability activeForFunctionY2.");
			integrity = false;
		}
		
		
		// Extended class:
		if(!completeExtended()){
			integrity = false;
		}
		
		return integrity;
		

	}
	
	/**
	 * Does a complete for all extending classes.
	 * 
	 * @return true if everything was correct.
	 */
	public abstract boolean completeExtended();
	
	/**
	 * Gets the minimum required level.
	 * 
	 * @return minimum level to use
	 */
	public Short minimumLevel() {
		return staminaFunctionX1;
	}
	
	
	// Interaction:
	/**
	 * Calculates the stamina drain for the given level.
	 * 
	 * @param level level
	 */
	public Double calculateStaminaUse(Short level) {

		if(level>staminaFunctionX2){
			level = staminaFunctionX2;
		}
		double k= (staminaFunctionY2-staminaFunctionY1)/(staminaFunctionX2-staminaFunctionX1);
		double b= staminaFunctionY2 - k*staminaFunctionX2;
		return k * level + b;
		
	}
	
	/**
	 * Calculates the time the ability should be active for.
	 * 
	 * @param level level
	 * @return ability active time
	 */
	public Short calculateAbilityActiveTime(Short level) {

		if(level>activeForFunctionX2){
			level = activeForFunctionX2;
		}
		double k= (activeForFunctionY2-activeForFunctionY1)/(activeForFunctionX2-activeForFunctionX1);
		double b= activeForFunctionY2 - k*activeForFunctionX2;
		return new Double(k * level + b).shortValue();
		
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
		return staminaFunctionX1;
	}
	
	/**
	 * Checks if the level is high enough to use the ability.
	 * 
	 * @param level level.
	 * 
	 * @return true if the level is high enough
	 */
	public boolean levelHighEnough(Short level) {
            return staminaFunctionX1 <= level;
	}
	
	
}
