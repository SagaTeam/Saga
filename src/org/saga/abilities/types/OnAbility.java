package org.saga.abilities.types;

import org.saga.abilities.Ability;
import org.saga.abilities.Ability.AbilityActivateType;

public interface OnAbility {

	
	/**
	 * Gets ability activate type.
	 * 
	 * @return activate type
	 */
	public AbilityActivateType getActivateType();
	
	/**
	 * Gets the ability associated.
	 * 
	 * @return ability.
	 */
	public Ability getAbility();
	
	
}
