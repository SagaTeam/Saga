package org.saga.abilities;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnDamagedEntity;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class DisarmAbility extends Ability implements OnDamagedEntity{

	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "disarm";

	/**
	 * Disarm slot range minimum value.
	 */
	transient public static final Short SLOT_RANGE_MINIMUM = 0;
	
	/**
	 * Disarm slot range maximum value.
	 */
	transient public static final Short SLOT_RANGE_MAXIMUM = 35;
	
	/**
	 * Random generator.
	 */
	transient final Random random = new Random();
	
	
	/**
	 * Used by gson.
	 * 
	 */
	public DisarmAbility() {
		
            super(ABILITY_NAME);
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#completeExtended()
	 */
	@Override
	public boolean completeExtended() {
		return true;
	}
	
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnDamagedLivingEntity#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.entity.EntityDamageByEntityEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, EntityDamageByEntityEvent event){

		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		// Return if the damaged can't be disarmed:
		if(!(damaged instanceof Player)){
			((Player) damager).sendMessage(PlayerMessages.abilityUseFailedOn(damaged, this));
			return;
		}
		
		PlayerInventory inventory = ((Player)damaged).getInventory();
		
		// Return if there is nothing to disarm:
		if(inventory.getItemInHand().getType().equals(Material.AIR)){
			System.out.println("nothing to disarm");
			return;
		}
		
		
		Integer handSlot = inventory.getHeldItemSlot();
		Integer newSlot = generateRandomSlot();
		ItemStack handSlotItem = inventory.getItem(handSlot);
		ItemStack newSlotItem = inventory.getItem(newSlot);
		// Don't set air(null)
		if(!newSlotItem.getType().equals(Material.AIR)){
			inventory.setItem(handSlot, newSlotItem);
		}else{
			inventory.clear(handSlot);
		}
		if(!handSlotItem.getType().equals(Material.AIR)){
			inventory.setItem(newSlot, handSlotItem);
		}else{
			inventory.clear(newSlot);
		}
		
		if(damager instanceof Player){
			((Player) damager).sendMessage(PlayerMessages.youUsedAbilityOnEntity(damaged, this));
		}
		if(damaged instanceof Player){
			((Player) damaged ).sendMessage(PlayerMessages.entityUsedAbilityOnYou(damager, this));
		}
		
		
		return;
		
		
	}
	
	/**
	 * Generates a random slot in the defined range.
	 * 
	 * @return random slot
	 */
	private Integer generateRandomSlot() {

		return new Double(SLOT_RANGE_MINIMUM + (SLOT_RANGE_MAXIMUM - SLOT_RANGE_MINIMUM) * random.nextDouble()).intValue();
		
	}
	
}
