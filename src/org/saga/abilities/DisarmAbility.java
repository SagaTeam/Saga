package org.saga.abilities;

import java.util.Vector;

public class DisarmAbility extends Ability{

	/**
	 * Ability name.
	 */
	private static final transient String ABILITY_NAME= "disarm";
	
	
	public DisarmAbility() {
		
		super(ABILITY_NAME);
		
	}

	
	@Override
	protected boolean checkExtensionIntegrity(Vector<String> problematicFields) {
		return true;
	}
	
}
