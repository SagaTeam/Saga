package org.saga.professions;

import java.util.*;

import org.saga.abilities.Ability;

public class WoodcutterProfession extends Profession {

	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "woodcutter";
	
	
	public WoodcutterProfession() {
		
		super(PROFESSION_NAME, WoodcutterProfession.class.getName());
		
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
