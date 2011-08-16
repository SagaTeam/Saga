package org.saga.abilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnGotDamagedByEntity;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class CounterattackAbility extends AbilityFunction implements OnGotDamagedByEntity{

	
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
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.UseOnGotDamagedByLivingEntity#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.entity.EntityDamageByEntityEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, EntityDamageByEntityEvent event){
		
		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		// Return if the damager can't be damaged back:
		if(!(damager instanceof LivingEntity)){
			return;
		}
		
		if(damager instanceof Player){
			((Player) damager).sendMessage(PlayerMessages.entityUsedAbilityOnYou(damaged, this));
		}
		if(damaged instanceof Player){
			((Player) damaged ).sendMessage(PlayerMessages.youUsedAbilityOnEntity(damager, this));
		}
		
		((LivingEntity) damager).damage(new Double(calculateFunctionValue(level) * event.getDamage()).intValue(), damaged);
		
		
	}
	
	
}
