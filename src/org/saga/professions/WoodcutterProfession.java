package org.saga.professions;

import java.util.Vector;

import org.saga.abilities.Ability;

public class WoodcutterProfession extends Profession {

	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "woodcutter";
	
	/* 
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#getProfessionName()
	 */
	@Override
	public String getProfessionName() {
		return PROFESSION_NAME;
	}
	
	
	@Override
	protected boolean checkExtensionIntegrity(Vector<String> problematicFields) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	protected Ability[] getAbilities() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
