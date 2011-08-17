package org.saga.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.saga.Saga;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.utility.WriterReader;

import com.google.gson.JsonParseException;

public class ExperienceConfiguration {

	
	/**
	 * Instance of the configuration.
	 */
	transient private static ExperienceConfiguration instance;
	
	/**
	 * Gets the instance.
	 * 
	 * @return instance
	 */
	public static ExperienceConfiguration getConfig() {
		return instance;
	}
	
	/**
	 * Experience table for blocks with special block states.
	 */
	public Hashtable<Material, Hashtable<Byte, Integer>> blockBrakeExperienceTable;

	/**
	 * Entity kill experience table.
	 */
	public Hashtable<String, Integer> entityKillExperienceTable;
	
	
	/**
	 * Used by gson.
	 * 
	 */
	public ExperienceConfiguration() {
	}
	
	/**
	 * Completes.
	 * 
	 * @return integrity check
	 */
	public boolean complete() {
		

		boolean integrity = true;
		
		// Set instance:
		instance = this;
		
		// Check tables:
		if(blockBrakeExperienceTable == null){
			blockBrakeExperienceTable = new Hashtable<Material, Hashtable<Byte,Integer>>();
			Saga.severe("Experience information blockBrakeExperienceTable field is not initialized. Adding tto examples.");
			Hashtable<Byte, Integer> element1 = new Hashtable<Byte, Integer>();
			Hashtable<Byte, Integer> element2 = new Hashtable<Byte, Integer>();
			element1.put((byte) 0, 2);
			element1.put((byte) 2, 1);
			element2.put((byte)1, 2);
			element2.put((byte)2, 1);
			blockBrakeExperienceTable.put(Material.WALL_SIGN, element1);
			blockBrakeExperienceTable.put(Material.BED, element2);
			integrity = false;
		}
		if(entityKillExperienceTable == null){
			entityKillExperienceTable = new Hashtable<String, Integer>();
			Saga.severe("Experience information entityKillExperienceTable field is not initialized. Adding tto examples.");
			entityKillExperienceTable.put("EntityName1", 1);
			entityKillExperienceTable.put("MrSssss", 1);
			integrity = false;
		}
		
		// Set instance:
		instance = this;
		
		return integrity;
		
		
	}
	

	/**
	 * Gets experience configuration instance.
	 * 
	 * @return experience information instance.
	 */
	public static ExperienceConfiguration getExperienceConfig() {
		return instance;
	}
	
	
	/**
	 * Gets experience for the given material.
	 * 
	 * @param material material
	 * @return experience. 0 if not found
	 */
	public Integer getBlockBrakeExperience(Material material) {
		
		
		Hashtable<Byte, Integer> element = blockBrakeExperienceTable.get(material);
		if(element == null){
			return 0;
		}
		Integer exp = element.get(0);
		if(exp == null){
			return 0;
		}
		return exp;

		
	}
	
	/**
	 * Gets experience for the given material.
	 * 
	 * @param material material
	 * @return experience. 0 if not found
	 */
	public Integer getBlockBrakeExperience(MaterialData material) {
		
		
		Hashtable<Byte, Integer> element = blockBrakeExperienceTable.get(material.getItemType());
		if(element == null){
			return 0;
		}
		Integer exp = element.get(material.getItemType());
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
		
		
		Integer exp = entityKillExperienceTable.get(name);
		if(exp == null){
			return 0;
		}
		return exp;
		
		
	}

	
	/**
	 * Loads configuration.
	 * 
	 * @return experience configuration
	 */
	public static ExperienceConfiguration load(){
		
		
		boolean integrityCheck = true;
		
		// Load:
		String configName = "experience configuration";
		ExperienceConfiguration config;
		try {
			config = WriterReader.readExperienceConfig();
		} catch (FileNotFoundException e) {
			Saga.severe("Missing " + configName + ". Loading defaults.");
			config = new ExperienceConfiguration();
			integrityCheck = false;
		} catch (IOException e) {
			Saga.severe("Failed to load " + configName + ". Loading defaults.");
			config = new ExperienceConfiguration();
			integrityCheck = false;
		} catch (JsonParseException e) {
			Saga.severe("Failed to parse " + configName + ". Loading defaults.");
			config = new ExperienceConfiguration();
			integrityCheck = false;
		}
		
		// Integrity check and complete:
		integrityCheck = config.complete() && integrityCheck;
		
		// Write default if integrity check failed:
		if (!integrityCheck) {
			Saga.severe("Integrity check failed for " + configName);
			Saga.info("Writing " + configName + " with fixed default values. Edit and rename to use it.");
			try {
				WriterReader.writeExperienceConfig(config, WriteReadType.DEFAULTS);
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
	
	
}
