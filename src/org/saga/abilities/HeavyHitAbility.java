package org.saga.abilities;

import java.util.*;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;

public class HeavyHitAbility extends Ability{

	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME = "heavy hit";
	
	/**
	 * Multiplier function x1.
	 */
	transient private Short damageMultiplierFunctionX1;
	
	/**
	 * Multiplier function y1.
	 */
	private Double damageMultiplierFunctionY1;
	
	/**
	 * Multiplier function x2.
	 */
	private Short damageMultiplierFunctionX2;
	
	/**
	 * Multiplier function y2
	 */
	private Double damageMultiplierFunctionY2;
	
			
	// Initialization:
	public HeavyHitAbility() {
		
            super(ABILITY_NAME, HeavyHitAbility.class.getName());
		
	}

	@Override
	public boolean completeInheriting() {
		
		
		boolean integrity = true;
		// Fields:
		if(damageMultiplierFunctionY1==null){
			damageMultiplierFunctionY1 = 1.0;
			Saga.info("Setting default value for "+getAbilityName()+" ability damageMultiplierFunctionY1.");
			integrity = false;
		}
		if(damageMultiplierFunctionX2==null){
			damageMultiplierFunctionX2 = 1000;
			Saga.info("Setting default value for "+getAbilityName()+" ability damageMultiplierFunctionX2.");
			integrity = false;
		}
		if(damageMultiplierFunctionY2==null){
			damageMultiplierFunctionY2 = 1.0;
			Saga.info("Setting default value for "+getAbilityName()+" ability damageMultiplierFunctionY2.");
			integrity = false;
		}

		// Set fields:
		damageMultiplierFunctionX1 = minimumLevel();
		
		return integrity;

		
	}
	
	
	
	// Interaction:
	public boolean use(Short level, EntityDamageByEntityEvent event) {

		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		if(damager instanceof Player){
			((Player) damager).sendMessage(PlayerMessages.usedAbilityOnEntity(damaged, this));
		}
		if(damaged instanceof Player){
			((Player) damaged).sendMessage(PlayerMessages.entityUsedAbilityOn(damaged, this));
		}
		
		event.setDamage((new Double(event.getDamage()*calculateDamageMultiplier(level))).intValue());
		
		return true;
		
	}
	
	/**
	 * Calculates the damage multiplier for the given level.
	 * 
	 * @param level level
	 */
	private Double calculateDamageMultiplier(Short level) {
		System.out.println("level:"+level);
		if(level>damageMultiplierFunctionX2){
			level = damageMultiplierFunctionX2;
		}
		
		double k= (damageMultiplierFunctionY2 - damageMultiplierFunctionY1)/(damageMultiplierFunctionX2-damageMultiplierFunctionX1);
		double b= damageMultiplierFunctionY2 - k * damageMultiplierFunctionX2;
		System.out.println("k:"+k+ " b:"+b);
		return new Double(k * level + b);
		
		
	}
	
	
}
