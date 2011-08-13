package org.saga.attributes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
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
	 * Attacked type.
	 */
	private AttackedType attackedType;

	/**
	 * Attack type.
	 */
	private AttackType attackType;

	
	/**
	 * Forwards the name and sets use materials.
	 * 
	 * @param name name
	 * @param useMaterials materials that are required for use.
	 * @param attackedType attacked type
	 * @param attackType attack type
	 */
	public AttackAttribute(String name, Material[] useMaterials, AttackedType attackedType, AttackType attackType) {
		
		
		super(name);
		this.useMaterials = useMaterials;
		this.attackedType = attackedType;
		this.attackType = attackType;
		
		
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
		
		if(attackType==null){
			attackType = AttackType.NONE;
			Saga.info("Setting default value for "+getName()+" attribute attackType.");
			integrity = false;
		}
		
		return integrity;
		
		
	}
	
	
	/**
	 * Uses the attribute.
	 * 
	 */
	public void use(Short attributeLevel, SagaPlayer sagaPlayer, Event event) {
		
		
		if(attributeLevel == 0){
			return;
		}
		if( !(event instanceof EntityDamageByEntityEvent)){
			return;
		}
		if( !(checkAttacked(((EntityEvent) event).getEntity())) ){
			return;
		}
		if( ((EntityEvent) event).getEntity() instanceof Player && !(checkMaterial(((Player) ((EntityDamageByEntityEvent) event).getEntity()).getItemInHand().getType())) ){
			return;
		}
		if( !(checkAttack((EntityDamageByEntityEvent) event))){
			return;
		}
		
		((EntityDamageEvent) event).setDamage(floor(((EntityDamageEvent) event).getDamage() + calculateValue(attributeLevel)));
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
	 * Checks the attack
	 * 
	 * @param event event
	 * @return true if correct
	 */
	private boolean checkAttack(EntityDamageByEntityEvent event) {

		
		if(attackType.equals(AttackType.ALL)){
			return true;
		}
		
		
		
		EntityDamageByProjectileEvent projectileEvent = null;
		if(event.getDamager().getLastDamageCause() instanceof EntityDamageByProjectileEvent){
			projectileEvent = (EntityDamageByProjectileEvent) event.getDamager().getLastDamageCause();
		}
		
		// Melee or projectile:
		if(projectileEvent == null){
			if(attackType.equals(AttackType.MELEE)){
				return true;
			}
			return false;
		}
		
		// All projectiles:
		if(attackType.equals(AttackType.PROJECTILE_ALL)){
			return true;
		}
		
		// Arrow:
		if(attackType.equals(AttackType.PROJECTILE_ARROW) && projectileEvent.getProjectile() instanceof Arrow){
			return true;
		}
		
		// Fireball:
		if(attackType.equals(AttackType.PROJECTILE_FIREBALL) && projectileEvent.getProjectile() instanceof Fireball){
			return true;
		}
		
		// Invalid projectile:
		return false;
		
		
	}
	
	
	public enum AttackedType{
		
		
		PLAYER,
		MONSTER,
		ALL;
		
		
	}
	
	public enum AttackType{
		
		
		NONE,
		ALL,
		MELEE,
		PROJECTILE_ALL,
		PROJECTILE_ARROW,
		PROJECTILE_FIREBALL;
		
		
	}
	
	
}
