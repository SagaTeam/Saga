package org.saga;

import org.bukkit.event.entity.EntityListener;


import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
		
		// Send events to saga players if possible:
		Entity damaged= pEvent.getEntity();
		if(pEvent instanceof EntityDamageByEntityEvent && damaged instanceof LivingEntity && ((LivingEntity) damaged).getNoDamageTicks()<=((LivingEntity) damaged).getMaximumNoDamageTicks()/2F && ((LivingEntity) damaged).getHealth()>0){
			Entity damager = ((EntityDamageByEntityEvent)pEvent).getDamager();
			
			if(damager instanceof Player && damaged instanceof LivingEntity){
				if(Saga.plugin().isSagaPlayerLoaded(((Player) damager).getName())){
					try {
						Saga.plugin().getSagaPlayer(((Player) damager).getName()).damagedLivingEntityEvent((EntityDamageByEntityEvent) pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(damaged instanceof Player && damager instanceof LivingEntity){
				if(Saga.plugin().isSagaPlayerLoaded(((Player) damaged).getName())){
					try {
						Saga.plugin().getSagaPlayer(((Player) damaged).getName()).gotDamagedByLivingEntityEvent((EntityDamageByEntityEvent) pEvent);
					} catch (SagaPlayerNotLoadedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		super.onEntityDamage(pEvent);
		
		
	}

	
	
}
