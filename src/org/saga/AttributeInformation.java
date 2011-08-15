package org.saga;

import java.util.Hashtable;

import org.bukkit.Material;
import org.saga.SagaPlayerListener.SagaPlayerProjectileShotEvent.ProjectileType;
import org.saga.attributes.Attribute;
import org.saga.attributes.Attribute.DisplayType;
import org.saga.attributes.DamageChangeAttribute;
import org.saga.attributes.DamageChangeAttribute.AttackType;
import org.saga.attributes.DamageChangeAttribute.EntityType;
import org.saga.attributes.ProjectileShotAttribute;

public class AttributeInformation {

	/**
	 * Minimum damage that an attribute can reduce to.
	 */
	public Integer minimumAttributeDamage;
	
	/**
	 * Attack attributes.
	 */
	public DamageChangeAttribute[] attackAttributes;
	
	/**
	 * Defense attributes.
	 */
	public DamageChangeAttribute[] defenseAttributes;
	
	/**
	 * Resistance attributes.
	 */
	public ProjectileShotAttribute[] projectileShotAttributes;
	
	
	/**
	 * Resistance attributes.
	 */
	public Attribute[] resistanceAttributes;
	
	
	
	/**
	 * Profession attribute gain levels.
	 * First key is profession name, second is attribute raw name.
	 */
	private Hashtable<String, Hashtable<String, Short[]>> attributeGainLevels;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public AttributeInformation() {
	}
	
	/**
	 * Completes construction.
	 * 
	 */
	public boolean complete() {
		
		
		boolean integrity = true;
		
		// General fields:
		if(minimumAttributeDamage==null){
			minimumAttributeDamage = 1;
			Saga.info("minimumAttributeDamage field not defined. Setting default.");
			integrity = false;
		}
		
		// Attack attributes:
		if(attackAttributes==null){
			attackAttributes = new DamageChangeAttribute[2];
			Saga.info("Initializing attackAttributes. Adding two example attributes.");
			integrity = false;
			// Add examples:
			Saga.info("Adding two example attributes to attackAttributes.");
			attackAttributes[0] = new DamageChangeAttribute(false, "ExampleAttackAttribute1", new Material[]{Material.STONE, Material.DIRT}, EntityType.ALL, EntityType.ALL, AttackType.ALL, DisplayType.DEFENSE);
			attackAttributes[1] = new DamageChangeAttribute(false, "ExampleAttackAttribute2", new Material[]{Material.WOOD, Material.DIRT}, EntityType.ALL, EntityType.ALL, AttackType.MELEE, DisplayType.DEFENSE);
		}
		for (int i = 0; i < attackAttributes.length; i++) {
			integrity = attackAttributes[i].complete() && integrity;
		}
		
		// Defense attributes:
		if(defenseAttributes==null){
			defenseAttributes = new DamageChangeAttribute[2];
			Saga.info("Initializing defenseAttributes. Adding two example attributes.");
			integrity = false;
			// Add examples:
			Saga.info("Adding two example attributes to defenseAttributes.");
			defenseAttributes[0] = new DamageChangeAttribute(true, "ExampleAttackAttribute1", new Material[]{Material.STONE, Material.DIRT}, EntityType.ALL, EntityType.ALL, AttackType.ALL, DisplayType.DEFENSE);
			defenseAttributes[1] = new DamageChangeAttribute(true, "ExampleAttackAttribute2", new Material[]{Material.WOOD, Material.DIRT}, EntityType.ALL, EntityType.ALL, AttackType.MELEE, DisplayType.DEFENSE);
		}
		for (int i = 0; i < defenseAttributes.length; i++) {
			integrity = defenseAttributes[i].complete() && integrity;
		}
		
		// Projectile shot attributes:
		if(projectileShotAttributes==null){
			projectileShotAttributes = new ProjectileShotAttribute[2];
			Saga.info("Initializing projectileShotAttributes. Adding two example attributes.");
			integrity = false;
			// Add examples:
			Saga.info("Adding two example attributes to attackAttributes.");
			projectileShotAttributes[0] = new ProjectileShotAttribute( "ExampleProjectileShotAttributeAttribute1", ProjectileType.ARROW);
			projectileShotAttributes[1] = new ProjectileShotAttribute( "ExampleProjectileShotAttributeAttribute2", ProjectileType.FIREBALL);
		}
		for (int i = 0; i < projectileShotAttributes.length; i++) {
			integrity = projectileShotAttributes[i].complete() && integrity;
		}
		
		// Gain levels:
		if(attributeGainLevels == null){
			attributeGainLevels = new Hashtable<String, Hashtable<String,Short[]>>();
			Saga.info("Initializing attribute gain levels table.");
			integrity = false;
			// Add examples:
			Saga.info("Adding two example attributes to the table.");
			Hashtable<String, Short[]> exampleAttributeGain = new Hashtable<String, Short[]>();
			exampleAttributeGain.put("AttributeName1", new Short[]{3,6,40,40,48});
			exampleAttributeGain.put("AttributeName2", new Short[]{6,8,33,40,41});
			attributeGainLevels.put("ExampleProfession1", exampleAttributeGain);
			attributeGainLevels.put("ExampleProfession2", exampleAttributeGain);
		}
		
		return integrity;
		
		
	}
		
	
	// Interaction:
	/**
	 * Gets attribute upgrades for a certain profession
	 * 
	 * @param professionName profession name
	 * @return attribute upgrades, empty table if not found
	 */
	public Hashtable<String, Short[]> getAttributeUpgrades(String professionName) {
	
		
		Hashtable<String, Short[]> professionGainLevels = attributeGainLevels.get(professionName);
		if(professionGainLevels == null){
			return new Hashtable<String, Short[]>();
		}
		return professionGainLevels;
		
		
		
	}


}
