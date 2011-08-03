package org.saga.professions;


import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.ChopDownAbility;
import org.saga.abilities.TreeClimbAbility;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternCheckElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.pattern.SagaPatternLogicElement.LogicType;
import org.saga.pattern.SagaPatternSelectionMoveElement;

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

	
	// Interaction:
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
		return ABILITIES;
	}

	@Override
	protected Material[] getAbilityScrollMaterials() {
		return ABILITY_SCROLL_MATERIALS;
	}

	@Override
	public boolean isAbilityActive(int ability) {
		return activeAbilities[ability];
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
	protected void abilityActivateEvent(int ability) {
		activeAbilities[ability] = true;
	}
	
	@Override
	protected void abilityDeactivateEvent(int ability) {
		activeAbilities[ability] = false;
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
