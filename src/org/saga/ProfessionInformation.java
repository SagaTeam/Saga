package org.saga;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.Material;
import org.saga.abilities.Ability;
import org.saga.abilities.ChopDownAbility;
import org.saga.abilities.CounterattackAbility;
import org.saga.abilities.DisarmAbility;
import org.saga.abilities.DischargeAbility;
import org.saga.abilities.FireballAbility;
import org.saga.abilities.FocusedHitsAbility;
import org.saga.abilities.HarvestAbility;
import org.saga.abilities.HeavyHitAbility;
import org.saga.abilities.PowerfulSwings;
import org.saga.abilities.ResistLavaAbility;
import org.saga.abilities.TreeClimbAbility;
import org.saga.professions.Profession.ProfessionType;
import org.saga.utility.WriterReader;
import org.saga.utility.WriterReader.WriteType;

import com.google.gson.JsonParseException;

public class ProfessionInformation {

	
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
	public ProfessionInformation() {
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
		for (int i = 0; i < abilityNames.size(); i++) {
			if(abilityNames.get(i) == null){
				Saga.severe("Ability name null. Setting empty.");
				abilityNames.set(i, "");
			}
		}
		
		// Read abilities based on the names:
		abilities = new ArrayList<Ability>();
		for (int i = 0; i < abilityNames.size(); i++) {
			String name = abilityNames.get(i);
			if(name.length() > 0){
				try {
					Ability ability = WriterReader.readAbilityInformation(name);
					abilities.add(ability);
				} catch (FileNotFoundException e) {
					Saga.severe(name + "ability file not found. Ignoring ability.");
					integrity = false;
				}catch (IOException e) {
					Saga.severe(name + "ability file read failure. Ignoring ability.");
					integrity = false;
				}catch (JsonParseException e) {
					Saga.severe(name + "ability file parse failure. Ignoring ability.");
					integrity = false;
				}
				
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
        
        // Ability integrity check:
        for (int i = 0; i < abilities.size(); i++) {
 			integrity = abilities.get(i).complete() && integrity;
 		}
        
		// Fill ability quick access:
        abilityPool = new Hashtable<String, Ability>();
		for (int i = 0; i < abilities.size(); i++) {
			abilityPool.put(abilities.get(i).getAbilityName(), abilities.get(i));
		}
		
		// Profession definitions:
		if(professionDefinitions == null){
			professionDefinitions = new ArrayList<ProfessionInformation.ProfessionDefinition>();
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
		professionDefinitionPool = new Hashtable<String, ProfessionInformation.ProfessionDefinition>();
		for (ProfessionDefinition definition : professionDefinitions) {
			professionDefinitionPool.put(definition.getName(), definition);
		}
        
		return integrity;
		
		
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
	 * Creates a filler for an invalid definition.
	 * 
	 * @param name name
	 * @return empty definition with {@link ProfessionType#INVALID} type
	 */
	public ProfessionDefinition createInvalidDefinition2(String name) {
		return new ProfessionDefinition(name, new Material[0], ProfessionType.INVALID, new String[0]);
	}
	
	/**
	 * Returns all abilities.
	 * 
	 * @return all abilities
	 */
	private Ability[] getDefaultAbilities() {

		return new Ability[]{new HeavyHitAbility(), new CounterattackAbility(), new DisarmAbility(), new PowerfulSwings(), new ResistLavaAbility(), new FocusedHitsAbility(), new ChopDownAbility(), new TreeClimbAbility(), new HarvestAbility(), new FireballAbility(), new DischargeAbility()};
		
	}
	
	/**
	 * Loads profession information.
	 * 
	 * @return profession information
	 */
	public static ProfessionInformation load(){
		
		
		boolean integrityCheck = true;
		
		// Load profession information:
		ProfessionInformation professionInformation;
		try {
			professionInformation = WriterReader.readProfessionInformation();
		} catch (FileNotFoundException e) {
			 Saga.severe("Missing profession information. Loading defaults.");
			 professionInformation= new ProfessionInformation();
			 integrityCheck = false;
		} catch (IOException e) {
			Saga.severe("Profession information load failure. Loading defaults.");
			professionInformation= new ProfessionInformation();
			integrityCheck = false;
		} catch (JsonParseException e) {
			Saga.severe("Profession information parse failure. Loading defaults.");
			professionInformation= new ProfessionInformation();
			integrityCheck = false;
		}
		
		// Integrity check and complete:
		integrityCheck = professionInformation.complete() && integrityCheck;
		
		// Write default if the integrity check failed:
		if (!integrityCheck) {
			Saga.severe("Integrity check failure.");
			Saga.info("Writing profession information with fixed default values. Edit and rename to use it.");
			try {
				WriterReader.writeProfessionInformation(professionInformation, WriteType.DEFAULTS);
			} catch (IOException e) {
				Saga.severe("Profession information write failure. Ignoring write.");
			}
			
			Saga.info("Writing abilities with fixed default values. Edit and rename to use them.");
			ArrayList<Ability> abilities = professionInformation.abilities;
			for (Ability ability : abilities) {
				try {
					WriterReader.writeAbilityInformation(ability, WriteType.DEFAULTS);
				} catch (IOException e) {
					Saga.severe("Writing failed for "+ability.getAbilityName() +" ability. Ignoring write.");
				}
			}
			
		}
		
		return professionInformation;
		
		
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
