package org.saga.abilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnDamagedLivingEntity;
import org.saga.attributes.DamageChangeAttribute.AttackType;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class HeavyHitAbility extends AbilityFunction implements OnDamagedLivingEntity{

	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME = "heavy hit";

	
	/**
	 * Used by gson.
	 * 
	 */
	public HeavyHitAbility() {
		
		super(ABILITY_NAME, AbilityActivateType.TIMER);
		
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
	
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnDamagedLivingEntity#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.entity.EntityDamageByEntityEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, EntityDamageByEntityEvent event) {

		
		System.out.println("use");
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		sagaPlayer.sendMessage(PlayerMessages.youUsedAbilityOnEntity(damaged, this));
		
		if(damaged instanceof Player){
			((Player) damaged).sendMessage(PlayerMessages.entityUsedAbilityOnYou(damager, this));
		}
		
		event.setDamage((new Double(event.getDamage()*calculateFunctionValue(level))).intValue());
		
		return;
		
		
	}

	
}
