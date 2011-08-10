package org.saga.professions;


import org.bukkit.Material;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.HarvestAbility;

public class FarmerProfession extends Profession {

	
	// Constants:
	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "farmer";

	/**
	 * All abilities.
	 */
	transient private Ability[] ABILITIES = new Ability[]{Saga.balanceInformation().abilities.get(HarvestAbility.class.getSimpleName()) };
	
	/**
	 * Ability scroll materials.
	 */
	transient private static Material[] ABILITY_SCROLL_MATERIALS = new Material[]{Material.WOOD_HOE , Material.STONE_HOE , Material.IRON_HOE , Material.GOLD_HOE , Material.DIAMOND_HOE};
	
	
	// Main:
	/**
	 * Active abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public FarmerProfession() {
		super(PROFESSION_NAME);
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#completeInheriting()
	 */
	@Override
	public void completeExtended() {

		
		// Initialize:
		activeAbilities = new Boolean[ABILITIES.length];
		for (int i = 0; i < activeAbilities.length; i++) {
			activeAbilities[i] = false;
		}
		
		
	}

	
	// Interaction:
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#getAbilities()
	 */
	@Override
	protected Ability[] getAbilities() {
		return ABILITIES;
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#isAbilityActive(int)
	 */
	@Override
	public boolean isAbilityActive(int ability) throws IndexOutOfBoundsException {

		
		if(ability == 0){
			return false;
		}
		return activeAbilities[ability];
		
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#getAbilityScrollMaterials()
	 */
	@Override
	protected Material[] getAbilityScrollMaterials() {
		return ABILITY_SCROLL_MATERIALS;
	}

	
	// Events:
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#abilityActivateEvent(int)
	 */
	@Override
	protected void abilityActivateEvent(int ability) throws IndexOutOfBoundsException {

		
		// Heavy swings:
		if(ability == 0){
			((HarvestAbility)ABILITIES[0]).use(getLevel(), sagaPlayer);
			return;
		}
		
		// All others:
		activeAbilities[ability] = true;
		
		
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#abilityDeactivateEvent(int)
	 */
	@Override
	protected void abilityDeactivateEvent(int ability) {
    	
		
		if(ability!=0){
    		activeAbilities[ability] = false;
    	}
		
		
	}


}
