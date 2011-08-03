package org.saga.professions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
	transient private static Material[] ABILITY_SCROLL_MATERIALS = new Material[]{Material.WOOD_SWORD , Material.STONE_SWORD , Material.IRON_SWORD , Material.GOLD_SWORD , Material.DIAMOND_SWORD};
	
	/**
	 * Active abilities.
	 */
	transient Boolean[] activeAbilities;
	
	// Initialization:
	/**
	 * Sets the profession name.
	 * 
	 */
	public FighterProfession() {
		
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
    @Override
	protected void abilityActivateEvent(int ability) {
    	activeAbilities[ability] = true;
	}
    
    @Override
	protected void abilityDeactivateEvent(int ability) {
    	activeAbilities[ability] = false;
	}
	
    @Override
    public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent event) {
    	

    	// Counterattack:
    	if(activeAbilities[1] && !event.getType().equals(Type.PROJECTILE_HIT)){
    		activeAbilities[1]=false;
    		if(!((CounterattackAbility)ABILITIES[1]).use(getLevel(), event)){
    			// Activate again on failure:
    			activeAbilities[1]=true;
    		}
    		
    	}
    	super.gotDamagedByLivingEntityEvent(event);
    	
    	
    }
    
    @Override
    public void damagedLivingEntityEvent(EntityDamageByEntityEvent event) {
    	
    	
    	// Heavy hit:
    	if(activeAbilities[0] && event.getDamager() instanceof Player && isMaterialCorrect(((Player) event.getDamager()).getItemInHand().getType())){
    		activeAbilities[0]=false;
    		if(((HeavyHitAbility)ABILITIES[0]).use(getLevel(), event));
    	}
    	// Disarm:
    	if(activeAbilities[2] && event.getDamager() instanceof Player && isMaterialCorrect(((Player) event.getDamager()).getItemInHand().getType())){
    		activeAbilities[2]=false;
    		((DisarmAbility)ABILITIES[2]).use(getLevel(), event);
    	}
    	
    	super.damagedLivingEntityEvent(event);
    	
    	
    }
    
    
    
    
}
