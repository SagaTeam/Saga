package org.saga.professions;


import org.bukkit.Material;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.FocusedHitsAbility;
import org.saga.abilities.PowerfulSwings;
import org.saga.abilities.ResistLavaAbility;

public class MinerProfession extends Profession {

	
	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "miner";

	/**
	 * All abilities.
	 */
	transient private Ability[] ABILITIES = new Ability[]{Saga.balanceInformation().abilities.get(PowerfulSwings.class.getSimpleName()) , Saga.balanceInformation().abilities.get(ResistLavaAbility.class.getSimpleName()) ,  Saga.balanceInformation().abilities.get(FocusedHitsAbility.class.getSimpleName())};
	
	/**
	 * Ability scroll materials.
	 */
	transient private static Material[] ABILITY_SCROLL_MATERIALS = new Material[]{Material.WOOD_PICKAXE , Material.STONE_PICKAXE , Material.IRON_PICKAXE , Material.GOLD_PICKAXE , Material.DIAMOND_PICKAXE};
	
	
	// Main:
	/**
	 * State of the orthogonal flip.
	 */
	transient private boolean orthogonalFlipState=false;
	
	/**
	 * Active abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public MinerProfession() {
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
	public void damagedByEnvironmentEvent(EntityDamageEvent pEvent) {
		
		
		// Resist lava:
		if(activeAbilities[1]){
			((ResistLavaAbility)ABILITIES[1]).sustain(getLevel(), pEvent);
		}
		
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#damagedBlockEvent(org.bukkit.event.block.BlockDamageEvent)
	 */
	@Override
	public void damagedBlockEvent(BlockDamageEvent pEvent) {
		
		
		// Focused hits:
		if(activeAbilities[2] && isMaterialCorrect(pEvent.getPlayer().getItemInHand().getType())){
			((FocusedHitsAbility)ABILITIES[2]).use(getLevel(), sagaPlayer, pEvent);
		}
		
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#abilityActivateEvent(int)
	 */
	@Override
	protected void abilityActivateEvent(int ability) throws IndexOutOfBoundsException {

		
		// Heavy swings:
		if(ability == 0){
			((PowerfulSwings)ABILITIES[0]).use(getLevel(), sagaPlayer, orthogonalFlipState);
			orthogonalFlipState = !orthogonalFlipState;
			return;
		}
		
		// Resist lava:
		if(ability == 1){
			activeAbilities[ability] = true;
			((ResistLavaAbility)ABILITIES[1]).activate(getLevel(), sagaPlayer);
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