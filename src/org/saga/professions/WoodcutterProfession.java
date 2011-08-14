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

}
