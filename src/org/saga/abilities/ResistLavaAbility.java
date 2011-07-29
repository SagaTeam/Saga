package org.saga.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternCheckElement;
import org.saga.pattern.SagaPatternCheckElement.CheckAction;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;

public class ResistLavaAbility extends Ability {

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "resist lava";

	/**
	 * Activate check.
	 */
	transient private SagaPatternListElement ACTIVATE_CHECK_PATTERN = createActivateCheckPattern();;
	
	
	/**
	 * The level at which the extinguish effect kicks in.
	 */
	private Integer extinguishLevel = 18;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public ResistLavaAbility() {
		super(ABILITY_NAME);
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#completeExtended()
	 */
	@Override
	public boolean completeExtended() {
		
		
		boolean integrity = true;
		if(extinguishLevel==null){
			extinguishLevel = 100;
			Saga.info("Setting default value for "+getAbilityName()+" ability extinguishLevel.");
			integrity = false;
		}
		
		return integrity;
		
		
	}
	
	
	/**
	 * Activates the ability. If there is no lava then the ability is deactivated.
	 * 
	 * @param level level
	 * @param sagaPlayer saga player
	 */
	public void activate(Short level, SagaPlayer sagaPlayer) {
		
		
		sagaPlayer.sendMessage(PlayerMessages.abilityActivate(this));
		
		// Deactivate if there is no lava:
		if(!sagaPlayer.checkPattern(ACTIVATE_CHECK_PATTERN, (short)0, false)){
			sagaPlayer.sendMessage(PlayerMessages.abilityUseFailure(this));
			sagaPlayer.deactivateAbility(this);
			return;
		}
		

	}
	
	/**
	 * Used when the ability is active.
	 * 
	 * @param level level
	 * @param event event
	 */
	public void sustain(Short level, EntityDamageEvent event) {
		
		
		if(event.getCause().equals(DamageCause.LAVA) || event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)){
			event.setDamage(0);
		}

		
	}
	
	/**
	 * Creates the activate check pattern.
	 * 
	 * @return Activate check pattern.
	 */
	private static SagaPatternListElement createActivateCheckPattern() {
		
		
		SagaPatternCheckElement[] checks = new SagaPatternCheckElement[1];
		checks[0] = new SagaPatternCheckElement(0, -2, 0, new Material[]{Material.LAVA}, CheckAction.TERMINATE);
		return new SagaPatternListElement(checks);
		

	}
	
	
	
}
