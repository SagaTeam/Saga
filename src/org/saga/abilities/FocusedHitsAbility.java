package org.saga.abilities;

import org.bukkit.Material;
import org.bukkit.event.block.BlockDamageEvent;
import org.saga.SagaPlayer;

public class FocusedHitsAbility extends Ability {

	
	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME = "focused hits";

	
	// Initialization:
	/**
	 * Sets the profession name.
	 * 
	 */
	public FocusedHitsAbility() {
		
		super(ABILITY_NAME);
		
	}
	
	
	/**
	 * Sets one hit brake.
	 * 
	 * @param level level
	 * @param sagaPlayer saga player
	 * @param event event
	 */
	public void use(Short level, SagaPlayer sagaPlayer, BlockDamageEvent event) {

		
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
		// TODO Auto-generated method stub
		return true;
	}

}
