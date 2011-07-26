package org.saga.professions;

import java.util.*;

import org.bukkit.Material;
import org.saga.abilities.Ability;

public class WoodcutterProfession extends Profession {

	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "woodcutter";
	
	
	public WoodcutterProfession() {
		
		super(PROFESSION_NAME);
		
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
		return new Ability[0];
	}


	@Override
	protected Material[] getAbilityScrollMaterials() {
		// TODO Auto-generated method stub
		return new Material[0];
	}


	@Override
	public void completeInheriting() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void abilityActivateEvent(int ability) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isAbilityActive(int ability) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
