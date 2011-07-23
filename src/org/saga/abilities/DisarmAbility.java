package org.saga.abilities;

import java.util.*;

public class DisarmAbility extends Ability{

	/**
	 * Ability name.
	 */
	private static final transient String ABILITY_NAME = "disarm";
	
	
	public DisarmAbility() {
		
            super(ABILITY_NAME, DisarmAbility.class.getName());
		
	}
	
	
}
