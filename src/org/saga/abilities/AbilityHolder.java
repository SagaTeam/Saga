package org.saga.abilities;

import java.util.*;
import org.saga.*;

public class AbilityHolder {

	// Constants:
	/**
	 * Prefix for integrity check.
	 */
	private static String INTEGRITY_CHECK_PREFIX = "profession:";

	
	// Abilities:
	/**
	 * Counterattack ability.
	 */
	private CounterattackAbility counterattack;
	
	/**
	 * Disarm ability.
	 */
	private DisarmAbility disarm;
	
	/**
	 * All abilities
	 */
	private Ability[] allAbilities;
	
	
	// Initialization:
	/**
	 * Used during load. Should be left empty.
	 */
	public AbilityHolder() {
	}
	
	/**
	 * Checks the integrity of the balance information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	public boolean checkIntegrity(ArrayList<String> problematicFields) {
		
		// Check abilities:
		if( allAbilities == null ){
                    allAbilities = new Ability[2];
		}
		
		// Counterattack:
		if( counterattack == null ){
                    counterattack = new CounterattackAbility();
                    problematicFields.add(INTEGRITY_CHECK_PREFIX + "counterattack");
                    counterattack.checkIntegrity(problematicFields);
		}
		allAbilities[0] = counterattack;
		
		// Counterattack:
		if( disarm == null ) {
                    disarm = new DisarmAbility();
                    problematicFields.add(INTEGRITY_CHECK_PREFIX + "disarm");
                    disarm.checkIntegrity(problematicFields);
		}
		allAbilities[1] = disarm;
		
		
		// All abilities:
		for (int i = 0; i < allAbilities.length; i++) {
                    allAbilities[i].checkIntegrity(problematicFields);
		}
		
		return problematicFields.isEmpty();
		
	}
	
        public boolean checkIntegrity() {

            ArrayList<String> problematicFields = new ArrayList<String>();

            if ( this.checkIntegrity(problematicFields) == true) {
                return true;
            }

            for ( String field : problematicFields ) {

                Saga.warning(field + " data invalid! Loaded default.");

            }

            return false;

        }

}
