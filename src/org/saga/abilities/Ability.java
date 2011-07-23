package org.saga.abilities;

import java.util.*;

public abstract class Ability {

	
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
	public Ability(String abilityName) {
		
		this.abilityName=abilityName;
		
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
	
	// Integrity check after load:
	/**
	 * Checks the integrity of the balance information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	public boolean checkIntegrity(ArrayList<String> problematicFields) {

            // Fields:
            if( abilityName == null ){
                abilityName = "null";
                problematicFields.add("abilityName");
            }

            if( levelStaminaFunctionX1 == null ) {
                levelStaminaFunctionX1 = 100;
                problematicFields.add("levelStaminaFunctionX1");
            }

            if( levelStaminaFunctionY1 == null ){
                levelStaminaFunctionY1 = 100.0;
                problematicFields.add("levelStaminaFunctionY1");
            }

            if( levelStaminaFunctionX2 == null ){
                levelStaminaFunctionX2 = 100;
                problematicFields.add("levelStaminaFunctionX2");
            }

            if( levelStaminaFunctionY2 == null ){
                levelStaminaFunctionY2 = 100.0;
                problematicFields.add("levelStaminaFunctionY2");
            }

            // Check extension:
            checkExtensionIntegrity(problematicFields);

            return problematicFields.isEmpty();
		
	}
	
	/**
	 * Checks the integrity of the balance information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	protected abstract boolean checkExtensionIntegrity(ArrayList<String> problematicFields);
	
	
	
}
