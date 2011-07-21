package org.saga.abilities;

import java.util.Vector;

public class AbilityHolder {

	// Constants:
	/**
	 * Prefix for integrity check.
	 */
	private static String INTEGRITY_CHECK_PREFIX= "profession:";

	
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
	public boolean checkIntegrity(Vector<String> problematicFields) {
		
		
		// Check abilities:
		if(allAbilities==null){
			allAbilities= new Ability[2];
		}
		
		// Counterattack:
		if(counterattack==null){
			counterattack= new CounterattackAbility();
			problematicFields.add(INTEGRITY_CHECK_PREFIX+ "counterattack");
			counterattack.checkIntegrity(new Vector<String>());
		}
		allAbilities[0]=counterattack;
		
		// Counterattack:
		if(disarm==null){
			disarm= new DisarmAbility();
			problematicFields.add(INTEGRITY_CHECK_PREFIX+ "disarm");
			disarm.checkIntegrity(new Vector<String>());
		}
		allAbilities[1]=disarm;
		
		
		// All abilities:
		for (int i = 0; i < allAbilities.length; i++) {
			allAbilities[i].checkIntegrity(problematicFields);
		}
		
		
		return problematicFields.size()==0;
		
	}
	
	
}
