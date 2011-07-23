package org.saga.professions;

import java.util.*;

import org.saga.abilities.Ability;

public class FighterProfession extends Profession {
	
	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "fighter";

	
	public FighterProfession() {
		
		super(PROFESSION_NAME, FighterProfession.class.getName());
		
	}
	

	/* 
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#getProfessionName()
	 */
        @Override
	public String getProfessionName() {
		return PROFESSION_NAME;
	}

        @Override
	protected Ability[] getAbilities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
