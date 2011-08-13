package org.saga;

import java.util.Hashtable;

import org.bukkit.Material;
import org.saga.SagaPlayerListener.SagaPlayerProjectileShotEvent.ProjectileType;
import org.saga.attributes.AttackAttribute;
import org.saga.attributes.AttackAttribute.AttackType;
import org.saga.attributes.AttackAttribute.AttackedType;
import org.saga.attributes.Attribute;
import org.saga.attributes.DefenseAttribute;
import org.saga.attributes.DefenseAttribute.AttackerType;
import org.saga.attributes.ProjectileShotAttribute;

public class AttributeInformation {

	
	/**
	 * Attack attributes.
	 */
	public AttackAttribute[] attackAttributes;
	
	/**
	 * Defense attributes.
	 */
	public DefenseAttribute[] defenseAttributes;
	
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
		
		// Attack attributes:
		if(attackAttributes==null){
			attackAttributes = new AttackAttribute[2];
			Saga.info("Initializing attackAttributes. Adding two example attributes.");
			integrity = false;
			// Add examples:
			Saga.info("Adding two example attributes to attackAttributes.");
			attackAttributes[0] = new AttackAttribute( "ExampleAttackAttribute1", new Material[]{Material.STONE, Material.DIRT}, AttackedType.ALL, AttackType.ALL );
			attackAttributes[1] = new AttackAttribute( "ExampleAttackAttribute2", new Material[]{}, AttackedType.MONSTER,  AttackType.ALL );
		}
		for (int i = 0; i < attackAttributes.length; i++) {
			integrity = attackAttributes[i].complete() && integrity;
		}
		
		// Defense attributes:
		if(defenseAttributes==null){
			defenseAttributes = new DefenseAttribute[2];
			Saga.info("Initializing defenseAttributes. Adding two example attributes.");
			integrity = false;
			// Add examples:
			Saga.info("Adding two example attributes to defenseAttributes.");
			defenseAttributes[0] = new DefenseAttribute( "ExampleDefenseAttribute1", new Material[]{Material.WOOD, Material.AIR}, AttackerType.MONSTER,  AttackType.ALL );
			defenseAttributes[1] = new DefenseAttribute( "ExampleDefenseAttribute2", new Material[]{}, AttackerType.PLAYER,  AttackType.ALL );
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
