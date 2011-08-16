package org.saga.abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnGotDamagedByEntity;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class CounterattackAbility extends AbilityFunction implements OnGotDamagedByEntity{

	
	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME= "counterattack";
	
	
	/**
	 * Sets a name.
	 * 
	 */
	public CounterattackAbility() {
		
		super(ABILITY_NAME, AbilityActivateType.TRIGGER);
		
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
	 * @see org.saga.abilities.types.UseOnGotDamagedByLivingEntity#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.entity.EntityDamageByEntityEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, EntityDamageByEntityEvent event){
		
		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		// Ignore if the damager can't be damaged back:
		if(!(damager instanceof LivingEntity)){
			sagaPlayer.sendMessage(PlayerMessages.abilityUseFailedOn(damager, this));
			return;
		}
		
		// Ignore if the attack was ranged:
		if(event.getEntity().getLastDamageCause() instanceof EntityDamageByProjectileEvent){
			sagaPlayer.sendMessage(PlayerMessages.abilityUseFailedOn(damager, this));
			return;
		}
		
		// Ignore if elemental:
		if(!event.getCause().equals(DamageCause.ENTITY_ATTACK)){
			sagaPlayer.sendMessage(PlayerMessages.abilityUseFailedOn(damager, this));
			return;
		}
		
		// Ignore if last damage cause death:
		if(damaged instanceof LivingEntity && event.getDamage() >= ((LivingEntity) damaged).getHealth()){
			sagaPlayer.sendMessage(PlayerMessages.abilityUseFailedOn(damager, this));
			return;
		}
		
		if(damager instanceof Player){
			((Player) damager).sendMessage(PlayerMessages.entityUsedNegativeAbilityOnYou(damaged, this));
		}
		sagaPlayer.sendMessage(PlayerMessages.youUsedAbilityOnEntity(damager, this));
		
		// Calculate damage:
		int damage = new Double(calculateFunctionValue(level) * event.getDamage()).intValue();
		
		// Send event:
		EntityDamageByEntityEvent counterEvent = new EntityDamageByEntityEvent(damaged, damager, DamageCause.ENTITY_ATTACK, damage);
		Bukkit.getServer().getPluginManager().callEvent(counterEvent);
		
		// Damage:
		if(!counterEvent.isCancelled()){
			((LivingEntity) damager).damage(counterEvent.getDamage());
		}
		
		
	}
	
	
}
