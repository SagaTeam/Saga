package org.saga.abilities;

import java.util.*;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.Saga;
import org.saga.constants.PlayerMessages;

public class CounterattackAbility extends AbilityFunction{

	
	private final String TEST_FIELD="TESTGOESHERE";
	
	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME= "counterattack";
	
	
	// Initialization:
	/**
	 * Sets a name.
	 * 
	 */
	public CounterattackAbility() {
		
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
	/**
	 * Uses counterattack. Deactivate the ability before you use it, otherwise it will cause endless
	 * counterattack ping pong.
	 * 
	 * @param level level
	 * @param event event
	 * @return true, if the conditions were met for use
	 */
	public boolean use(Short level, EntityDamageByEntityEvent event){
		
		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		// Return if the damager can't be damaged back:
		if(!(damager instanceof LivingEntity)){
			return false;
		}
		
		if(damager instanceof Player){
			((Player) damager).sendMessage(PlayerMessages.entityUsedAbilityOnYou(damaged, this));
		}
		if(damaged instanceof Player){
			((Player) damaged ).sendMessage(PlayerMessages.youUsedAbilityOnEntity(damager, this));
		}
		
		((LivingEntity) damager).damage(new Double(calculateFunctionValue(level) * event.getDamage()).intValue(), damaged);
		
		return true;
		
		
	}
	
	
}
