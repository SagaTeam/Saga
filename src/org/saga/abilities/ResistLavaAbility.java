package org.saga.abilities;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.pattern.SagaPatternLogicElement.LogicType;

public class ResistLavaAbility extends Ability {

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "resist lava";

	/**
	 * Activate check.
	 */
	transient private SagaPatternLogicElement ACTIVATE_CHECK_PATTERN = createActivateCheckPattern();;
	
	
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
		
		
		// Deactivate if there is no lava:
		boolean termination = false;
		termination = sagaPlayer.initiatePattern(ACTIVATE_CHECK_PATTERN, (short)0, false, 10);
		if(termination){
			sagaPlayer.sendMessage(PlayerMessages.invalidAbilityUse(this));
			sagaPlayer.deactivateAbility(this);
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
	private static SagaPatternLogicElement createActivateCheckPattern() {
		
		
		SagaPatternLogicElement checks = new SagaPatternLogicElement(LogicAction.TERMINATE, LogicType.NOR);
		checks.addElement(new SagaPatternLogicElement(0, -2, 0, new Material[]{Material.STATIONARY_LAVA}, LogicAction.NONE));
		checks.addElement(new SagaPatternLogicElement(0, -1, 0, new Material[]{Material.STATIONARY_LAVA}, LogicAction.NONE));
		checks.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.STATIONARY_LAVA}, LogicAction.NONE));
		checks.addElement(new SagaPatternLogicElement(0, -2, 0, new Material[]{Material.LAVA}, LogicAction.NONE));
		checks.addElement(new SagaPatternLogicElement(0, -1, 0, new Material[]{Material.LAVA}, LogicAction.NONE));
		checks.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LAVA}, LogicAction.NONE));
		return checks;
		

	}
	
	
	
}
