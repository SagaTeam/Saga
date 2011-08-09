package org.saga.attributes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.saga.Saga;
import org.saga.SagaPlayer;

public class AttackAttribute extends Attribute {

	
	

	/**
	 * The materials that the player should be holding to use the attribute.
	 */
	private Material[] useMaterials;
	
	/**
	 * If true, then all materials will be accepted.
	 */
	private Boolean acceptAllMaterials;
	
	/**
	 * True, if a projectile attack must be considered.
	 */
	private Boolean mustBeProjectile;
	
	/**
	 * Attacked type.
	 */
	private AttackedType attackedType;

	
	/**
	 * Forwards the name and sets use materials.
	 * 
	 * @param name name
	 * @param useMaterials materials that are required for use.
	 * @param attackedType attacked type
	 * @param mustBeProjectile true is the attack must be a projectile attack
	 */
	public AttackAttribute(String name, Material[] useMaterials, AttackedType attackedType, Boolean mustBeProjectile) {
		
		
		super(name);
		this.useMaterials = useMaterials;
		this.attackedType = attackedType;
		this.mustBeProjectile = mustBeProjectile;
		
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.attributes.Attribute#completeExtended()
	 */
	@Override
	protected boolean completeExtended() {

		
		boolean integrity = true;

		if(useMaterials==null){
			useMaterials = new Material[]{};
			Saga.info("Setting default value for "+getName()+" attribute useMaterials.");
			integrity = false;
		}
		
		if(acceptAllMaterials==null){
			acceptAllMaterials = false;
			Saga.info("Setting default value for "+getName()+" attribute acceptAllMaterials.");
			integrity = false;
		}
		
		if(attackedType==null){
			attackedType = AttackedType.ALL;
			Saga.info("Setting default value for "+getName()+" attribute attackerType.");
			integrity = false;
		}
		
		if(mustBeProjectile==null){
			mustBeProjectile = false;
			Saga.info("Setting default value for "+getName()+" attribute mustBeProjectile.");
			integrity = false;
		}
		
		return integrity;
		
		
	}
	
	
	/**
	 * Uses the attribute.
	 * 
	 */
	public void use(Short attributeLevel, SagaPlayer sagaPlayer, EntityDamageEvent event) {
		
		
		if(attributeLevel == 0){
			return;
		}
		if( !(event instanceof EntityDamageByEntityEvent)){
			return;
		}
		if( !(checkAttacked(event.getEntity())) ){
			return;
		}
		if( event.getEntity() instanceof Player && !(checkMaterial(((Player) ((EntityDamageByEntityEvent) event).getEntity()).getItemInHand().getType())) ){
			return;
		}
		if( !(checkProjectile(event))){
			return;
		}
		
		event.setDamage(floor(event.getDamage() + calculateValue(attributeLevel)));
		System.out.println("!"+sagaPlayer.getName()+" used "+getName()+" attribute!");
		sagaPlayer.sendMessage(ChatColor.AQUA + "You used "+getName()+" attribute!");
		
		
	}
	
	/**
	 * Checks if the material is correct.
	 * 
	 * @param material material
	 * @return true, if correct
	 */
	private boolean checkMaterial(Material material) {

		
		if(acceptAllMaterials){
			return true;
		}
		
		for (int i = 0; i < useMaterials.length; i++) {
			if(useMaterials[i].equals(material)){
				return true;
			}
		}
		return false;
		
		
	}

	/**
	 * Checks if the entity is correct.
	 * 
	 * @param attacked attacked entity
	 * @return true is the attacked is correct.
	 */
	private boolean checkAttacked(Entity attacked) {
		
		
		if(attackedType.equals(AttackedType.ALL)){
			return true;
		}
		
		if(attackedType.equals(AttackedType.PLAYER)){
			return attacked instanceof Player;
		}
		
		if(attackedType.equals(AttackedType.MONSTER)){
			return !(attacked instanceof Monster);
		}
		
		return false;
		
		
	}
	
	/**
	 * Does a projectile check.
	 * 
	 * @param event event
	 * @return true if the projectile check is correct and the use method can proceed
	 */
	private boolean checkProjectile(EntityDamageEvent event) {
		
		
		if( event.getEntity().getLastDamageCause() instanceof EntityDamageByProjectileEvent ){
			return mustBeProjectile;
		}
		return !mustBeProjectile;
		
		
	}
	
	
	public enum AttackedType{
		
		PLAYER,
		MONSTER,
		ALL;
		
	}
	
	
}
