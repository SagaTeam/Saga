package org.saga.abilities;

import java.util.*;

public class CounterattackAbility extends Ability{

	private final String TEST_FIELD="TESTGOESHERE";
	
	/**
	 * Ability name.
	 */
	private static final transient String ABILITY_NAME= "counterattack";
	
	
	public CounterattackAbility() {
		
            super(ABILITY_NAME);
		
	}


	@Override
	protected boolean checkExtensionIntegrity(ArrayList<String> problematicFields) {
            return true;
	}

	
}
