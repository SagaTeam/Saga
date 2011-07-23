package org.saga.professions;

import java.util.Vector;

import org.saga.abilities.Ability;

public class FighterProfession extends Profession {
	
	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "fighter";

        @Override
	protected boolean checkExtensionIntegrity(Vector<String> problematicFields) {
		// TODO Auto-generated method stub
		return true;
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
