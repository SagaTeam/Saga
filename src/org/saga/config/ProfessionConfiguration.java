package org.saga.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.Material;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.ChopDownAbility;
import org.saga.abilities.CounterattackAbility;
import org.saga.abilities.DisarmAbility;
import org.saga.abilities.DischargeAbility;
import org.saga.abilities.DisorientAbility;
import org.saga.abilities.FireballAbility;
import org.saga.abilities.FocusedHitsAbility;
import org.saga.abilities.HarvestAbility;
import org.saga.abilities.HeavyHitAbility;
import org.saga.abilities.LeapAbility;
import org.saga.abilities.PowerfulSwings;
import org.saga.abilities.ResistLavaAbility;
import org.saga.abilities.TreeClimbAbility;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.professions.Profession.ProfessionType;
import org.saga.utility.WriterReader;

import com.google.gson.JsonParseException;

public class ProfessionConfiguration {
	

	/**
	 * Instance of the configuration.
	 */
	transient private static ProfessionConfiguration instance;
	
	/**
	 * Gets the instance.
	 * 
	 * @return instance
	 */
	public static ProfessionConfiguration getConfig() {
		return instance;
	}
	
	
	// Profession Information.
	/**
	 * Ability names that will be loaded.
	 */
	private ArrayList<String> abilityNames;
	
	/**
	 * Abilities.
	 */
	transient private ArrayList<Ability> abilities;

	/**
	 * Profession definitions.
	 */
	private ArrayList<ProfessionDefinition> professionDefinitions;

	// Quick access:
	/**
	 * Quick access ability pool.
	 */
	transient private Hashtable<String, Ability> abilityPool;
	
	/**
	 * Quick access profession definitions.
	 */
	transient private Hashtable<String, ProfessionDefinition> professionDefinitionPool;

	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public ProfessionConfiguration() {
	}
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 * Abilities are completed separately.
	 * 
	 * @return true if everything was correct.
	 */
	private boolean complete() {
		
		
		boolean integrity=true;

		// Load abilities:
		if(abilityNames == null){
			abilityNames = new ArrayList<String>();
			Saga.severe("profession information abilityNames field is not initialized.");
			integrity=false;
		}
		if(abilities == null){
			abilities = new ArrayList<Ability>();
			Saga.severe("profession information abilities field is not initialized.");
			integrity=false;
		}
		for (int i = 0; i < abilityNames.size(); i++) {
			if(abilityNames.get(i) == null){
				Saga.severe("Ability name null. Setting empty.");
				abilityNames.set(i, "");
			}
		}
		
        // Add default abilities if they don't exist:
        Ability[] defaultAbilities = getDefaultAbilities();
        for (int i = 0; i < defaultAbilities.length; i++) {
        	boolean addAbility = true;
			for (int j = 0; j < abilities.size(); j++) {
				if(defaultAbilities[i].getClass().equals(abilities.get(j).getClass())){
					addAbility = false;
					break;
				}
			}
			if(addAbility){
				Saga.warning(defaultAbilities[i].getAbilityName() + " default ability not loaded from disk. Adding defined default.");
				abilities.add(defaultAbilities[i]);
				abilityNames.add(defaultAbilities[i].getAbilityName());
				integrity = false;
			}
			
		}
        
		// Fill ability quick access:
        abilityPool = new Hashtable<String, Ability>();
		for (int i = 0; i < abilities.size(); i++) {
			abilityPool.put(abilities.get(i).getAbilityName(), abilities.get(i));
		}
		
		// Profession definitions:
		if(professionDefinitions == null){
			professionDefinitions = new ArrayList<ProfessionConfiguration.ProfessionDefinition>();
			Saga.severe("profession information professionDefinitions field is not initialized. Adding two example definitions.");
			professionDefinitions.add(new ProfessionDefinition("ProfessionName1", new Material[]{Material.WOOD_AXE, Material.INK_SACK}, ProfessionType.CLASS, new String[]{"Ability1", "Ability2", "Ability3"}));
			professionDefinitions.add(new ProfessionDefinition("ProfessionName2", new Material[]{Material.LADDER, Material.SAND}, ProfessionType.CLASS, new String[]{"Ability1", "Ability2", "Ability3"}));
			integrity=false;
		}
				
		// Check definitions for null and integrity:		
		for (int i = 0; i < professionDefinitions.size(); i++) {
			if(professionDefinitions.get(i) == null){
				Saga.severe("Found an undefined element in profession information professionDefinitions field. Removing element.");
				professionDefinitions.remove(i);
				i--;
			}else{
				integrity = professionDefinitions.get(i).complete() && integrity;
			}
		}
				
		// Add abilities to definitions:
		for (ProfessionDefinition definition : professionDefinitions) {
			
			String[] abilityNames = definition.abilityNames;
			ArrayList<Ability> abilities = new ArrayList<Ability>();
			for (int i = 0; i < abilityNames.length; i++) {
				Ability ability = abilityPool.get(abilityNames[i]);
				if(ability == null){
					Saga.severe("Could not find an ability named " + abilityNames[i] + ". Ignoring ability.");
				}else{
					abilities.add(ability);
				}
			}
			definition.addAbilities(abilities.toArray(new Ability[abilities.size()]));
			
		}
		
		// Add definitions to quick access:
		professionDefinitionPool = new Hashtable<String, ProfessionConfiguration.ProfessionDefinition>();
		for (ProfessionDefinition definition : professionDefinitions) {
			professionDefinitionPool.put(definition.getName(), definition);
		}
        
		return integrity;
		
		
	}
	
	/**
	 * Sets abilities.
	 * 
	 * @param abilities abilities
	 */
	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}

	
	// Interaction:
	/**
	 * Gets a profession definition.
	 * 
	 * @param name profession name.
	 * @return definition. null if not found
	 */
	public ProfessionDefinition getDefinition(String name){
		
		
		ProfessionDefinition definition = professionDefinitionPool.get(name);
		if(definition == null){
			return null;
		}
		return definition;
		
		
	}
	
	/**
	 * Returns all abilities.
	 * 
	 * @return all abilities
	 */
	private Ability[] getDefaultAbilities() {

		return new Ability[]{new HeavyHitAbility(), new CounterattackAbility(), new DisarmAbility(), new PowerfulSwings(), new ResistLavaAbility(), new FocusedHitsAbility(), new ChopDownAbility(), new TreeClimbAbility(), new HarvestAbility(), new FireballAbility(), new DischargeAbility(), new LeapAbility(), new DisorientAbility()};
		
	}

	/**
	 * Defines a profession.
	 * 
	 * @author andf
	 *
	 */
	public static class ProfessionDefinition{
		
		
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
		private boolean complete() {

			
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
			
			abilities = new Ability[0];
			
			return integrity;
			
			
		}
		
		
		/**
		 * Adds abilities.
		 * 
		 * @param abilities abilities
		 */
		private void addAbilities(Ability[] abilities) {
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
		
		
	}

	// Load unload:
	/**
	 * Loads profession information.
	 * 
	 * @return profession information
	 */
	public static ProfessionConfiguration load(){
		
		
		boolean integrityCheck = true;
		
		// Load:
		String configName = "profession configuration";
		ProfessionConfiguration config;
		try {
			config = WriterReader.readProfessionConfig();
		} catch (FileNotFoundException e) {
			Saga.severe("Missing " + configName + ". Loading defaults.");
			config = new ProfessionConfiguration();
			integrityCheck = false;
		} catch (IOException e) {
			Saga.severe("Failed to load " + configName + ". Loading defaults.");
			config = new ProfessionConfiguration();
			integrityCheck = false;
		} catch (JsonParseException e) {
			Saga.severe("Failed to parse " + configName + ". Loading defaults.");
			Saga.info("Parse message :" + e.getMessage());
			config = new ProfessionConfiguration();
			integrityCheck = false;
		}
		
		// Load abilities:
		if(config.abilityNames != null){
			config.setAbilities(loadAbilities(config.abilityNames));
		}
		
		// Integrity check and complete:
		integrityCheck = config.complete() && integrityCheck;
		
		// Write default if integrity check failed:
		if (!integrityCheck) {
			Saga.severe("Integrity check failed for " + configName);
			Saga.info("Writing " + configName + " with fixed default values. Edit and rename to use it.");
			try {
				WriterReader.writeProfessionConfig(config, WriteReadType.DEFAULTS);
			} catch (IOException e) {
				Saga.severe("Profession information write failure. Ignoring write.");
			}
		}
		
		// Set instance:
		instance = config;
		
		return config;
		
		
	}
	
	/**
	 * Unloads configuration.
	 * 
	 */
	public static void unload(){
		instance = null;
	}
	
	/**
	 * Loads and completes abilities.
	 * 
	 * @param abilityNames ability names
	 * @return abilities. Failed ones will be ignored
	 */
	private static ArrayList<Ability> loadAbilities(ArrayList<String> abilityNames) {

		
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for (int i = 0; i < abilityNames.size(); i++) {
			
			
			boolean integrityCheck = true;
			// Load:
			String configName = abilityNames.get(i) + " ability configuration";
			Ability config = null;
			try {
				config = WriterReader.readAbilityConfig(abilityNames.get(i));
				abilities.add(config);
			} catch (FileNotFoundException e) {
				Saga.severe("Missing " + configName + ". Ignoring ability.");
				integrityCheck = false;
				continue;
			} catch (IOException e) {
				Saga.severe("Failed to load " + configName + ". Ignoring ability.");
				integrityCheck = false;
				continue;
			} catch (JsonParseException e) {
				Saga.severe("Failed to parse " + configName + ". Ignoring ability.");
				Saga.info("Parse message :" + e.getMessage());
				integrityCheck = false;
				continue;
			}
			
			// Integrity check and complete:
			integrityCheck = config.complete() && integrityCheck;
			
			// Write default if integrity check failed:
			if (!integrityCheck) {
				Saga.severe("Integrity check failed for " + configName);
				Saga.info("Writing " + configName + " with fixed default values. Edit and rename to use it.");
				try {
					WriterReader.writeAbilityConfig(abilityNames.get(i) ,config, WriteReadType.ABILITY_DEFAULTS);
				} catch (IOException e) {
					Saga.severe("Profession information write failure. Ignoring write.");
				}
				
			}
			
			
		}
		
		return abilities;
		
	}
	
	
	// Other:
	/**
	 * Used when an invalid profession reqest is made.
	 * 
	 * @author andf
	 *
	 */
	public static class InvalidProfessionException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Sets a profession name.
		 * 
		 * @param name name
		 */
		public InvalidProfessionException(String name) {
			super("profession name="+name);
		}
		
		
	}
	
	
}
