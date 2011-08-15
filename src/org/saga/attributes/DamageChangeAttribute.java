package org.saga.attributes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.saga.Saga;
import org.saga.SagaPlayer;

public class DamageChangeAttribute extends Attribute {


	/**
	 * Attack attribute if true.
	 */
	private Boolean isAttack;

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
	private EntityType attackerType;

	/**
	 * Attacked type.
	 */
	private EntityType attackedType;
	
	/**
	 * Attack type.
	 */
	private AttackType attackType;

	
	/**
	 * Forwards the name and sets use materials.
	 * 
	 * @param isAttack sets attack if true defense otherwise
	 * @param name name
	 * @param useMaterials materials that are required for use.
	 * @param attackerType attacker type
	 * @param attackedType attacked type
	 * @param attackType attack type
	 * @param displayType display type
	 */
	public DamageChangeAttribute(Boolean isAttack, String name, Material[] useMaterials, EntityType attackerType, EntityType attackedType, AttackType attackType, DisplayType displayType) {
		
		
		super(name, DisplayType.DEFENSE);
		this.isAttack = isAttack;
		this.useMaterials = useMaterials;
		this.attackerType = attackerType;
		this.attackedType = attackerType;
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
		
		if(attackerType==null){
			attackerType = EntityType.ALL;
			Saga.info("Setting default value for "+getName()+" attribute attackerType.");
			integrity = false;
		}
		
		if(attackedType==null){
			attackedType = EntityType.ALL;
			Saga.info("Setting default value for "+getName()+" attribute attackedType.");
			integrity = false;
		}
		if(isAttack==null){
			isAttack = false;
			Saga.info("Setting default value for "+getName()+" attribute isAttack.");
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
		
		
		// Check event:
		if(!(event instanceof EntityDamageByEntityEvent)){
			return;
		}
		
		// Pick attack or attacked and modify damage:
		Entity user = ((EntityDamageByEntityEvent) event).getEntity();
		Entity other = ((EntityDamageByEntityEvent) event).getDamager();
		if(isAttack){
			Entity temp = user;
			user = other;
			other = temp;
		}
		modifyDamage(attributeLevel, sagaPlayer, (EntityDamageByEntityEvent) event, isAttack);	
			
		
	}
	
	/**
	 * Modifies the damage.
	 * 
	 * @param attributeLevel attribute level
	 * @param sagaPlayer saga player
	 * @param event event
	 * @param user attribute user
	 * @param other other entity
	 * @param oppositeSign damage sign will be opposite if true
	 */
	private void modifyDamage(Short attributeLevel, SagaPlayer sagaPlayer, EntityDamageByEntityEvent event, boolean oppositeSign) {

		
		// Level:
		if(attributeLevel == 0){
			return;
		}
		
		// Attacker:
		if( !(checkEntity(event.getDamager(), attackerType)) ){
			return;
		}
		
		// Attacked:
		if( !(checkEntity(event.getEntity(), attackedType)) ){
			return;
		}
		
		// Material:
		if( !(checkMaterial(event.getDamager())) ){
			return;
		}
		
		// Attack type:
		if( !(checkAttack(event.getEntity(), event.getCause())) ){
			return;
		}
		
		double damageChange = calculateValue(attributeLevel);
		if(oppositeSign){
			damageChange *= -1;
		}
		
		// Increase to minimum damage:
		int damage = floor(((EntityDamageByEntityEvent) event).getDamage() - damageChange);
		if(damage < Saga.attributeInformation().minimumAttributeDamage){
			damage = Saga.attributeInformation().minimumAttributeDamage;
		}
		
		event.setDamage(damage);
		System.out.println("!"+sagaPlayer.getName()+" used "+getName()+" attribute!");
		sagaPlayer.sendMessage(ChatColor.AQUA + "you used "+getName()+" attribute! damaged="+event.getEntity()+" damager="+event.getDamager());

		
	}
	
	/**
	 * Checks if the material is correct.
	 * 
	 * @param material material
	 * @return true, if correct
	 */
	private boolean checkMaterial(Entity entity) {

		
		if(acceptAllMaterials){
			return true;
		}
		
		// Entity must be a player:
		if(!(entity instanceof Player)){
			return false;
		}
		Material itemInHand = ((Player) entity).getItemInHand().getType();
		
		for (int i = 0; i < useMaterials.length; i++) {
			if(useMaterials[i].equals(itemInHand)){
				return true;
			}
		}
		return false;
		
		
	}

	/**
	 * Checks if the entity is correct.
	 * 
	 * @param attacker entity
	 * @param checkAgainst type to that the entity will be checked against
	 * @return true if check success
	 */
	private boolean checkEntity(Entity entity, EntityType checkAgainst) {
		
		
		if(checkAgainst.equals(EntityType.ALL)){
			return true;
		}
		
		if(checkAgainst.equals(EntityType.PLAYER)){
			return entity instanceof Player;
		}
		
		if(checkAgainst.equals(EntityType.MONSTER)){
			return !(entity instanceof Monster);
		}
		
		return false;
		
		
	}
	
	/**
	 * Checks the attack
	 * 
	 * @param damaged damaged
	 * @return true if correct
	 */
	private boolean checkAttack(Entity damaged, DamageCause damageCause) {

		
		if(attackType.equals(AttackType.ALL)){
			return true;
		}
		
		Projectile projectile = null;
		if(damaged.getLastDamageCause() instanceof EntityDamageByProjectileEvent){
			projectile = ((EntityDamageByProjectileEvent) damaged.getLastDamageCause()).getProjectile();
		}
		
		
		// Melee:
		if(projectile == null && attackType.equals(AttackType.MELEE) && damageCause.equals(DamageCause.ENTITY_ATTACK)){
			return true;
		}
		
		// Elemental all:
		if( attackType.equals(AttackType.ELEMENTAL_ALL) && (damageCause.equals(DamageCause.FIRE) || damageCause.equals(DamageCause.LIGHTNING)) ){
			return true;
		}
		
		// Fire:
		if( attackType.equals(AttackType.ELEMENTAL_FIRE) && damageCause.equals(DamageCause.FIRE) ){
			return true;
		}
		
		// Lightning:
		if( attackType.equals(AttackType.ELEMENTAL_LIGHTNING) && damageCause.equals(DamageCause.LIGHTNING) ){
			return true;
		}
		
		// Fireball:
		if(attackType.equals(AttackType.ELEMENTAL_FIRE) && projectile!=null && projectile instanceof Fireball){
			return true;
		}
		
		// Projectiles:
		if(projectile == null){
			return false;
		}
		
		// All projectiles:
		if(attackType.equals(AttackType.PROJECTILE_ALL)){
			return true;
		}
		
		// Arrow:
		if(attackType.equals(AttackType.PROJECTILE_ARROW) && projectile instanceof Arrow){
			return true;
		}
		
		// Fireball:
		if(attackType.equals(AttackType.PROJECTILE_FIREBALL) && projectile instanceof Fireball){
			return true;
		}
		
		
		// Invalid:
		return false;
		
		
	}
	
	
	public enum EntityType{
		
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
		PROJECTILE_FIREBALL,
		ELEMENTAL_ALL,
		ELEMENTAL_FIRE,
		ELEMENTAL_LIGHTNING;
		
		
	}
	
}
