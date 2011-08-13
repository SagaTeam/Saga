package org.saga.professions;


import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.ChopDownAbility;
import org.saga.abilities.TreeClimbAbility;

public class WoodcutterProfession extends Profession {

	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "woodcutter";
	
	/**
	 * All abilities.
	 */
	transient private Ability[] ABILITIES = new Ability[]{Saga.balanceInformation().abilities.get(ChopDownAbility.class.getSimpleName()), Saga.balanceInformation().abilities.get(TreeClimbAbility.class.getSimpleName())};
	
	/**
	 * Ability scroll materials.
	 */
	transient private static Material[] ABILITY_SCROLL_MATERIALS = new Material[]{Material.WOOD_AXE , Material.STONE_AXE , Material.IRON_AXE , Material.GOLD_AXE , Material.DIAMOND_AXE};
	
	
	/**
	 * Active abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public WoodcutterProfession() {
		
		super(PROFESSION_NAME);
		
	}

	@Override
	public void completeExtended() {
		
		
		// Initialize:
		activeAbilities = new Boolean[ABILITIES.length];
		for (int i = 0; i < activeAbilities.length; i++) {
			activeAbilities[i] = false;
		}
		
		
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.professions.Profession#getProfessionType()
	 */
	@Override
	public ProfessionType getProfessionType() {
		return ProfessionType.PROFESSION;
	}
	
	@Override
	public Ability[] getAbilities() {
		return ABILITIES;
	}

	@Override
	public Material[] getAbilityScrollMaterials() {
		return ABILITY_SCROLL_MATERIALS;
	}

	@Override
	public boolean isAbilityActive(Ability ability) {
		
		
		for (int i = 0; i < activeAbilities.length; i++) {
			if(ABILITIES[i].equals(ability)){
				return activeAbilities[i];
			}
		}
		return false;
		
		
	}
	

	// Events:
	public void brokeBlockEvent(BlockBreakEvent pEvent){
		
		
		// Chop down:
		if(activeAbilities[0] && pEvent.getBlock().getType().equals(Material.LOG)){
			((ChopDownAbility) ABILITIES[0]).use(getLevel(), sagaPlayer, pEvent);
			activeAbilities[0] = false;
		}
		
		
	}
	
	@Override
	public void abilityActivateEvent(Ability ability) {
		
		
		for (int i = 0; i < activeAbilities.length; i++) {
			if(ABILITIES[i].equals(ability)){
				activeAbilities[i] = true;
				return;
			}
		}
		
		
	}
	
	@Override
	public void abilityDeactivateEvent(Ability ability) {
		
		
		for (int i = 0; i < activeAbilities.length; i++) {
			if(ABILITIES[i].equals(ability)){
				activeAbilities[i] = false;
				return;
			}
		}
		
		
	}
	
	@Override
	public void damagedBlockEvent(BlockDamageEvent event) {
		
		
		// Climb a tree:
		if(activeAbilities[1] && ( event.getBlock().getType().equals(Material.LOG) || event.getBlock().getType().equals(Material.LEAVES) ) ){
			((TreeClimbAbility) ABILITIES[1]).use(getLevel(), sagaPlayer, event);
			activeAbilities[1] = false;
		}
		
		
	}


}
