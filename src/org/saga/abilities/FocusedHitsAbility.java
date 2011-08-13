package org.saga.abilities;

import org.bukkit.Material;
import org.bukkit.event.block.BlockDamageEvent;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnBlockDamage;
import org.saga.professions.Profession;

public class FocusedHitsAbility extends Ability implements OnBlockDamage{

	
	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME = "focused hits";

	
	/**
	 * used by gson.
	 * 
	 */
	public FocusedHitsAbility() {
		
		super(ABILITY_NAME);
		
	}
	
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnBlockDamage#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.block.BlockDamageEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, BlockDamageEvent event) {

		
		// Set one hit brake:
		if(event.getBlock().getType().equals(Material.BEDROCK)){
			return;
		}
		event.setInstaBreak(true);
		
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#completeExtended()
	 */
	@Override
	public boolean completeExtended() {
		return true;
	}

	
}
