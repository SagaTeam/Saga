package org.saga.abilities;

import java.util.*;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;

public class HeavyHitAbility extends AbilityFunction{

	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME = "heavy hit";

	
	// Initialization:
	public HeavyHitAbility() {
		
		super(ABILITY_NAME);
		
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.AbilityFunction#completeSecondExtended()
	 */
	@Override
	public boolean completeSecondExtended() {
		
		
		return true;

		
	}
	
	
	// Interaction:
	public boolean use(Short level, EntityDamageByEntityEvent event) {

		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		if(damager instanceof Player){
			((Player) damager).sendMessage(PlayerMessages.youUsedAbilityOnEntity(damaged, this));
		}
		if(damaged instanceof Player){
			((Player) damaged).sendMessage(PlayerMessages.entityUsedAbilityOnYou(damager, this));
		}
		
		event.setDamage((new Double(event.getDamage()*calculateFunctionValue(level))).intValue());
		
		return true;
		
		
	}

	
}
