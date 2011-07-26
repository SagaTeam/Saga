package org.saga.abilities;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.saga.constants.PlayerMessages;

public class DisarmAbility extends Ability{

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
	
	
	public DisarmAbility() {
		
            super(ABILITY_NAME, DisarmAbility.class.getName());
		
	}

	
	public boolean use(Short level, EntityDamageByEntityEvent event){

		
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		// Return if the damaged can't be disarmed:
		if(!(damager instanceof Player)){
			((Player) damager).sendMessage(PlayerMessages.cantUseAbilityOn(damaged, this));
			return false;
		}
		
		PlayerInventory inventory = ((Player)damaged).getInventory();
		
		// Return if there is nothing to disarm:
		if(inventory.getItemInHand().getType().equals(Material.AIR)){
			System.out.println("nothing to disarm");
			return true;
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
		
		
		System.out.println("OK");
		return true;
		
		
	}
	
	/**
	 * Generates a random slot in the defined range.
	 * 
	 * @return random slot
	 */
	private Integer generateRandomSlot() {

		return new Double(SLOT_RANGE_MINIMUM + (SLOT_RANGE_MAXIMUM - SLOT_RANGE_MINIMUM) * random.nextDouble()).intValue();
		
	}
		
	@Override
	public boolean completeInheriting() {
		return true;
	}
	
	
}
