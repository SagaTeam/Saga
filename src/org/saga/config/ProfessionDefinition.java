package org.saga.config;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.professions.Profession.ProfessionType;


/**
 * Defines a profession.
 * 
 * @author andf
 *
 */
public class ProfessionDefinition{
	
	
	/**
	 * Profession name.
	 */
	private String name;
	
	/**
	 * Profession materials.
	 */
	private Material[] materials;
	
	/**
	 * Profession type.
	 */
	private ProfessionType type;
	
	/**
	 * Ability names.
	 */
	private String[] abilityNames;
	
	
	/**
	 * Abilities.
	 */
	transient private Ability[] abilities;
	
	/**
	 * Profession properties.
	 */
	private Properties properties;
	
	
	// Experience:
	/**
	 * Block brake experience triggers.
	 */
	private Hashtable<Material, Hashtable<Byte, Integer>> blockBrakeExpTable;
	
	/**
	 * Entity brake experience triggers.
	 */
	private Hashtable<String, Integer> entityKillExpTable;
	
	
	// Initialization:
	/**
	 * Udes by gson.
	 * 
	 */
	public ProfessionDefinition() {
	}

	/**
	 * Creates definition.
	 * 
	 * @param name name
	 * @param materials materials
	 * @param type type
	 * @param abilityNames ability names
	 */
	public ProfessionDefinition(String name, Material[] materials, ProfessionType type, String[] abilityNames) {
		this.name = name;
		this.materials = materials;
		this.type = type;
		this.abilityNames = abilityNames;
	}
	
	/**
	 * Completes the definition. Abilities need to be added.
	 * 
	 * @return integrity.
	 */
	boolean complete() {

		
		boolean integrity=true;
		
		if(name == null){
			name = "";
			Saga.severe("Profession definition name field not initialized. Setting empty String.");
			integrity=false;
		}
		if(materials == null){
			materials = new Material[0];
			Saga.severe("Profession definition type field not initialized. Setting empty array.");
			integrity=false;
		}
		if(type == null){
			type = ProfessionType.INVALID;
			Saga.severe("Profession definition type field not initialized. Setting NEITHER.");
			integrity=false;
		}
		if(abilityNames == null){
			abilityNames = new String[0];
			Saga.severe("Profession definition abilityNames field not initialized. Setting empty array.");
			integrity=false;
		}
		if(properties == null){
			properties = new Properties();
			Saga.severe("Profession definition properties field not initialized. Setting empty list.");
			integrity=false;
		}
		if(blockBrakeExpTable == null){
			blockBrakeExpTable = new Hashtable<Material, Hashtable<Byte,Integer>>(2);
			Hashtable<Byte, Integer> element1 = new Hashtable<Byte, Integer>();
			Hashtable<Byte, Integer> element2 = new Hashtable<Byte, Integer>();
			element1.put((byte) 0, 1);
			element1.put((byte) 7, 4);
			element2.put((byte) 0, -1);
			element2.put((byte) 3, 5);
			blockBrakeExpTable.put(Material.BED, element1);
			blockBrakeExpTable.put(Material.BOOKSHELF, element2);
			Saga.severe("Profession definition blockBrakeExperienceTable field not initialized. Adding two examples.");
			integrity=false;
		}
		if(entityKillExpTable == null){
			entityKillExpTable = new Hashtable<String, Integer>();
			entityKillExpTable.put("MrSssss", 20);
			entityKillExpTable.put("Player", 20);
			Saga.severe("Profession definition entityKillExperienceTable field not initialized. Adding two examples.");
			integrity=false;
		}
		
		abilities = new Ability[0];
		
		// Set instance
		
		// Fill experience:
		fillExperience();
		
		return integrity;
		
		
	}
	
	/**
	 * Retrieves experience configuration and fills in profession tables.
	 * 
	 */
	private void fillExperience() {

		
		// Fill block brake:
		Enumeration<Material> brakeMaterials = blockBrakeExpTable.keys();
        while ( brakeMaterials.hasMoreElements() ) {
        	Material material = brakeMaterials.nextElement();
        	Hashtable<Byte, Integer> experiences = blockBrakeExpTable.get(material);
        	Enumeration<Byte> itemDatas = experiences.keys(); 
        	while ( itemDatas.hasMoreElements() ) {
        		Byte data = itemDatas.nextElement();
        		Integer exp = experiences.get(data);
        		exp = exp + ExperienceConfiguration.getConfig().getBlockBrakeExperience(material, data);
        		experiences.put(data, exp);
        	}
        }
		
        // Fill entity damage:
        Enumeration<String> entityNames = entityKillExpTable.keys();
        while ( entityNames.hasMoreElements() ) {
        	String entityName = entityNames.nextElement();
        	Integer exp = entityKillExpTable.get(entityName);
        	exp = exp + ExperienceConfiguration.getConfig().getEntityKillExperience(entityName);
        	entityKillExpTable.put(entityName, exp);
        }
        
		
	}
	
	
	// Interaction:
	/**
	 * Adds abilities.
	 * 
	 * @param abilities abilities
	 */
	void addAbilities(Ability[] abilities) {
		this.abilities = abilities;
	}
	
	/**
	 * Gets abilities.
	 * 
	 * @return abilities.
	 */
	public Ability[] getAbilities() {
		return abilities;
	}
	
	/**
	 * Gets ability names.
	 * 
	 * @return ability names
	 */
	public String[] getAbilityNames() {
		return abilityNames;
	}
	
	/**
	 * Gets a property.
	 * 
	 * @param key property key
	 * @return property. null if not found
	 */
	public String getProperty(String key) {
		
		return properties.getProperty(key);
		
	}
	
	/**
	 * Gets ability name.
	 * 
	 * @return ability name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets materials.
	 * 
	 * @return materials
	 */
	public Material[] getMaterials() {
		return materials;
	}
	
	/**
	 * Gets profession type.
	 * 
	 * @return profession type
	 */
	public ProfessionType getType() {
		return type;
	}
	
	
	// Experience:
	/**
	 * Gets experience for the given material.
	 * 
	 * @param material material
	 * @return experience. 0 if not found
	 */
	public Integer blockBrakeExperience(Block block) {

		
		Hashtable<Byte, Integer> element = blockBrakeExpTable.get(block.getType());
		if(element == null){
			return 0;
		}
		Integer exp = element.get(block.getData());
		if(exp == null){
			return 0;
		}
		return exp;
		
		
	}
	
	/**
	 * Gets entity kill experience.
	 * 
	 * @return experience. 0 if not found
	 */
	public Integer getEntityKillExperience(String name) {
		
		
		Integer exp = entityKillExpTable.get(name);
		if(exp == null){
			return 0;
		}
		return exp;
		
		
	}
	
	
}
