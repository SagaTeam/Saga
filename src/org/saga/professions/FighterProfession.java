package org.saga.professions;

import java.util.*;

import org.bukkit.Material;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.CounterattackAbility;
import org.saga.abilities.DisarmAbility;
import org.saga.abilities.HeavyHitAbility;

public class FighterProfession extends Profession {
	
	
	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "fighter";

	/**
	 * All abilities.
	 */
	transient private Ability[] ABILITIES = new Ability[]{Saga.balanceInformation().abilities.get(HeavyHitAbility.class.getSimpleName()) , Saga.balanceInformation().abilities.get(CounterattackAbility.class.getSimpleName()) , Saga.balanceInformation().abilities.get(DisarmAbility.class.getSimpleName())};
	
	/**
	 * Ability scroll materials.
	 */
	transient private static Material[] ABILITY_SCROLL_MATERIALS = new Material[]{Material.WOOD_SWORD , Material.STONE_SWORD , Material.GOLD_SWORD , Material.DIAMOND_SWORD};
	
	
	public FighterProfession() {
		
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

    /* (non-Javadoc)
     * @see org.saga.professions.Profession#getAbilities()
     */
    @Override
	protected Ability[] getAbilities() {
		return ABILITIES;
	}
	
    /* (non-Javadoc)
     * @see org.saga.professions.Profession#getAbilitesScrollMaterials()
     */
    @Override
    protected Material[] getAbilityScrollMaterials() {
    	return ABILITY_SCROLL_MATERIALS;
    }


	@Override
	public void completeInheriting() {
		// TODO Auto-generated method stub
		
	}
	
}
