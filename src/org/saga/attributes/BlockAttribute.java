package org.saga.attributes;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;

public class BlockAttribute extends Attribute {

	
	/**
	 * Attribute name.
	 */
	transient private static String ATTRIBUTE_NAME = "block";
	
	
	/**
	 * Materials that will be blocked.
	 */
	private Material[] blockMaterials = new Material[]{Material.DIAMOND_SWORD ,Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_AXE ,Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE};
	
	/**
	 * Random generator:
	 */
	transient private Random randomGenerator = new Random();
	
	/**
	 * Multiplier for ranged block chance.
	 */
	private Double rangedChanceMultiplier;
	
	
	/**
	 * Sets the name.
	 * 
	 */
	public BlockAttribute() {
		super(ATTRIBUTE_NAME);
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.attributes.Attribute#completeExtended()
	 */
	@Override
	protected boolean completeExtended() {
		
		
		boolean integrity = true;
		if(blockMaterials==null){
			blockMaterials = new Material[]{};
			Saga.info("Setting default value for blockMaterials.");
			integrity = false;
		}
		if(rangedChanceMultiplier==null){
			rangedChanceMultiplier = 0.0;
			Saga.info("Setting default value for rangedChanceMultiplier.");
			integrity = false;
		}
		
		return integrity;
		
		
	}
	
	
	/**
	 * Uses the attribute.
	 * 
	 */
	public void use(Short attributeLevel, SagaPlayer sagaPlayer, EntityDamageEvent event) {
		
		
		// Cast to entity by entity:
		if(!(event instanceof EntityDamageByEntityEvent)){
			return;
		}
		EntityDamageByEntityEvent entityByEntityEvent = (EntityDamageByEntityEvent) event;
		
		// Check if possible to block:
		if(!canBlock(entityByEntityEvent)){
			return;
		}
		
		Double blockChance = calculateValue(attributeLevel);
		if(entityByEntityEvent.getEntity().getLastDamageCause() instanceof EntityDamageByProjectileEvent){
			blockChance = blockChance * rangedChanceMultiplier;
		}
		
		// Random block:
		if(blockChance >= randomGenerator.nextDouble()){
			if(entityByEntityEvent.getDamager() instanceof Player){
				((Player) entityByEntityEvent.getDamager()).sendMessage(PlayerMessages.attackWasBlocked());
			}
			sagaPlayer.sendMessage(PlayerMessages.blockedAttack());
			entityByEntityEvent.setDamage(0);
		}
		System.out.println("!used "+ATTRIBUTE_NAME+" attribute!");
		
		
	}
	
	
	/**
	 * Check if block is allowed.
	 * 
	 * @param event event
	 * @return true if allowed
	 */
	private boolean canBlock(EntityDamageByEntityEvent event) {

		
		// Check if creature:
		if(!(event.getDamager() instanceof Player)){
			return true;
		}
		
		// Check item:
		for (int i = 0; i < blockMaterials.length; i++) {
			if(blockMaterials[i].equals(((Player) event.getDamager()).getItemInHand().getType())){
				return true;
			}
		}
		return false;
		
		
	}
	
}
