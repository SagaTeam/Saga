package org.saga;

import org.bukkit.event.entity.EntityListener;


import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.saga.exceptions.SagaPlayerNotLoadedException;

public class SagaEntityListener extends EntityListener{

	
	/**
	 * Constructs the listener.
	 * 
	 * @param pMainPlugin Main plugin.
	 */
	public SagaEntityListener() {

	
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.event.entity.EntityListener#onEntityDamage(org.bukkit.event.entity.EntityDamageEvent)
	 */
	@Override
	public void onEntityDamage(EntityDamageEvent pEvent) {
		
		
		// Damaged by another entity:
		Entity damaged= pEvent.getEntity();
		if(pEvent instanceof EntityDamageByEntityEvent && damaged instanceof LivingEntity && ((LivingEntity) damaged).getNoDamageTicks()<=((LivingEntity) damaged).getMaximumNoDamageTicks()/2F && ((LivingEntity) damaged).getHealth()>0){
			Entity damager = ((EntityDamageByEntityEvent)pEvent).getDamager();
			
			if(damager instanceof Player){
				if(Saga.plugin().isSagaPlayerLoaded(((Player) damager).getName())){
					try {
						Saga.plugin().getLoadedSagaPlayer(((Player) damager).getName()).damagedEntityEvent((EntityDamageByEntityEvent) pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}else{
					Saga.warning("Can't send an event for a not loaded player. Ignoring event.", ((HumanEntity) damager).getName());
				}
			}
			
			if(damaged instanceof Player){
				if(Saga.plugin().isSagaPlayerLoaded(((Player) damaged).getName())){
					try {
						Saga.plugin().getLoadedSagaPlayer(((Player) damaged).getName()).gotDamagedByEntityEvent((EntityDamageByEntityEvent) pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}else{
					Saga.warning("Can't send an event for a not loaded player. Ignoring event.", ((HumanEntity) damaged).getName());
				}
			}
			
		}
		
		
		// Environment damage to the player:
		if(!(pEvent instanceof EntityDamageByEntityEvent) && damaged instanceof Player ){
			Player damagedPlayer = (Player)(damaged);
			
			
			
			// Fire exposure:
			if(pEvent.getCause().equals(DamageCause.FIRE_TICK) && damagedPlayer.getFireTicks() <= damagedPlayer.getMaxFireTicks()/2F){
				if(Saga.plugin().isSagaPlayerLoaded(damagedPlayer.getName())){
					try {
						Saga.plugin().getLoadedSagaPlayer(damagedPlayer.getName()).damagedByEnvironmentEvent(pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}else{
					Saga.warning("Can't send an event for a not loaded player. Ignoring event.", ((HumanEntity) damaged).getName());
				}
			}
			
			// Lava (needs -1 to no damage ticks)	
			else if(pEvent.getCause().equals(DamageCause.LAVA) && damagedPlayer.getNoDamageTicks()-1 <= damagedPlayer.getMaximumNoDamageTicks()/2F){
				if(Saga.plugin().isSagaPlayerLoaded(damagedPlayer.getName())){
					try {
						Saga.plugin().getLoadedSagaPlayer(damagedPlayer.getName()).damagedByEnvironmentEvent(pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}else{
					Saga.warning("Can't send an event for a not loaded player. Ignoring event.", ((HumanEntity) damaged).getName());
				}	
			}
			
			// Normal:
			if(damagedPlayer.getNoDamageTicks() <= damagedPlayer.getMaximumNoDamageTicks()/2F){
				if(Saga.plugin().isSagaPlayerLoaded(damagedPlayer.getName())){
					try {
						Saga.plugin().getLoadedSagaPlayer(damagedPlayer.getName()).damagedByEnvironmentEvent(pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}else{
					Saga.warning("Can't send an event for a not loaded player. Ignoring event.", ((HumanEntity) damaged).getName());
				}
			}
			
			
//			System.out.println(((LivingEntity) damaged).getNoDamageTicks()+" <= "+((LivingEntity) damaged).getMaximumNoDamageTicks()+ " "+ ((LivingEntity) damaged).getMaxFireTicks());
			
		}

		
	}
	
	
	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		// TODO Auto-generated method stub
		super.onProjectileHit(event);
	}

	
}
