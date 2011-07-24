package org.saga.abilities;

import java.util.*;

public class CounterattackAbility extends Ability{

	private final String TEST_FIELD="TESTGOESHERE";
	
	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME= "counterattack";
	
	
	public CounterattackAbility() {
		
            super(ABILITY_NAME, CounterattackAbility.class.getName());
		
	}


	@Override
	public boolean completeInheriting() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
