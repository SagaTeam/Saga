package org.saga.config;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.saga.Saga;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.utility.WriterReader;

import com.google.gson.JsonParseException;

public class BalanceConfiguration {


	/**
	 * Instance of the configuration.
	 */
	transient private static BalanceConfiguration instance;
	
	/**
	 * Gets the instance.
	 * 
	 * @return instance
	 */
	public static BalanceConfiguration getConfig() {
		return instance;
	}
	
	
	// Player:
	/**
	 * Maximum stamina.
	 */
	public Double maximumStamina;

	/**
	 * Stamina gain per second.
	 */
	public Double staminaPerSecond;

	// Profession general:
	/**
	 * Maximum level.
	 */
	public Short maximumLevel;
	
	// Other
	/**
	 * Time in seconds that the an ability remains active.
	 */
	public Short abilitySelectedTime;
	
	/**
	 * Time in seconds that the an ability remains active.
	 */
	public Integer baseLightningDamage;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 */
	public BalanceConfiguration() {
		
	}
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 * 
	 * @return true if everything was correct.
	 */
	public boolean complete() {
		
		
		// Fields:
		// Player general:
		boolean integrity=true;
		if(maximumStamina == null){
			Saga.warning("Setting default value for balance information maximumStamina.");
			maximumStamina= 100.0;
			integrity=false;
		}
		if(staminaPerSecond == null){
			Saga.warning("Setting default value for balance information staminaPerSecond.");
			staminaPerSecond= 0.1;
			integrity=false;
		}
		// Profession general:
		if(maximumLevel == null){
			Saga.warning("Setting default value for balance information maximumLevel.");
			maximumLevel= 1;
			integrity=false;
		}
		// Other:
		if(abilitySelectedTime == null){
			Saga.warning("Setting default value for balance information abilitySelectedTime.");
			abilitySelectedTime= 3;
			integrity=false;
		}
		if(baseLightningDamage == null){
			Saga.warning("Setting default value for balance information baseLightningDamage.");
			baseLightningDamage= 1;
			integrity=false;
		}
		
		return integrity;
		
		
	}


	// Load unload:
	/**
	 * Loads the configuration.
	 * 
	 * @return experience configuration
	 */
	public static BalanceConfiguration load(){
		
		
		boolean integrityCheck = true;
		
		// Load:
		String configName = "balance configuration";
		BalanceConfiguration config;
		try {
			config = WriterReader.readBalanceConfig();
		} catch (FileNotFoundException e) {
			Saga.severe("Missing " + configName + ". Loading defaults.");
			config = new BalanceConfiguration();
			integrityCheck = false;
		} catch (IOException e) {
			Saga.severe("Failed to load " + configName + ". Loading defaults.");
			config = new BalanceConfiguration();
			integrityCheck = false;
		} catch (JsonParseException e) {
			Saga.severe("Failed to parse " + configName + ". Loading defaults.");
			Saga.info("Parse message :" + e.getMessage());
			config = new BalanceConfiguration();
			integrityCheck = false;
		}
		
		// Integrity check and complete:
		integrityCheck = config.complete() && integrityCheck;
		
		// Write default if integrity check failed:
		if (!integrityCheck) {
			Saga.severe("Integrity check failed for " + configName);
			Saga.info("Writing " + configName + " with fixed default values. Edit and rename to use it.");
			try {
				WriterReader.writeBalanceConfig(config, WriteReadType.DEFAULTS);
			} catch (IOException e) {
				Saga.severe("Profession information write failure. Ignoring write.");
			}
		}
		
		// Set instance:
		instance = config;
		
		return config;
		
		
	}
	
	/**
	 * Unloads the instance.
	 * 
	 */
	public static void unload(){
		instance = null;
	}
	
	
}
