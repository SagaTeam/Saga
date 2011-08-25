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
	
	
	// Settlement claim:
	/**
	 * Settlement claim function X1;
	 */
	private Short settlementClaimFunctionX1;
	
	/**
	 * Settlement claim function Y1;
	 */
	private Short settlementClaimFunctionY1;
	
	/**
	 * Settlement claim function X2;
	 */
	private Short settlementClaimFunctionX2;
	
	/**
	 * Settlement claim function Y2;
	 */
	private Short settlementClaimFunctionY2;
	

	// Faction settle:
	/**
	 * Faction settle function X1;
	 */
	private Short factionSettleFunctionX1;
	
	/**
	 * Faction settle function Y1;
	 */
	private Short factionSettleFunctionY1;
	
	/**
	 * Faction settle function X2;
	 */
	private Short factionSettleFunctionX2;
	
	/**
	 * Faction settle function Y2;
	 */
	private Short factionSettleFunctionY2;
	

	// Player settle:
	/**
	 * Player settle function X1;
	 */
	private Short playerSettleFunctionX1;
	
	/**
	 * Player settle function Y1;
	 */
	private Short playerSettleFunctionY1;
	
	/**
	 * Player settle function X2;
	 */
	private Short playerSettleFunctionX2;
	
	/**
	 * Player settle function Y2;
	 */
	private Short playerSettleFunctionY2;
	
	
	
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
		
		
		// Player:
		boolean integrity=true;
		String config = "balance configuration";
		if(maximumStamina == null){
			Saga.warning("Failed to initialize maximumStamina field for " + config + ". Setting default");
			maximumStamina= 100.0;
			integrity=false;
		}
		if(staminaPerSecond == null){
			Saga.warning("Failed to initialize staminaPerSecond field for " + config + ". Setting default");
			staminaPerSecond= 0.1;
			integrity=false;
		}
		
		// Profession:
		if(maximumLevel == null){
			Saga.warning("Failed to initialize staminaPerSecond field for " + config + ". Setting default");
			maximumLevel= 1;
			integrity=false;
		}
		
		// Other:
		if(abilitySelectedTime == null){
			Saga.warning("Failed to initialize abilitySelectedTime field for " + config + ". Setting default");
			abilitySelectedTime= 3;
			integrity=false;
		}
		if(baseLightningDamage == null){
			Saga.warning("Failed to initialize baseLightningDamage field for " + config + ". Setting default");
			baseLightningDamage= 1;
			integrity=false;
		}
		
		// Settlement claim:
		if(settlementClaimFunctionX1 == null){
			Saga.warning("Failed to initialize settlementClaimFunctionX1 field for " + config + ". Setting default");
			settlementClaimFunctionX1= 0;
			integrity=false;
		}
		if(settlementClaimFunctionY1 == null){
			Saga.warning("Failed to initialize settlementClaimFunctionY1 field for " + config + ". Setting default");
			settlementClaimFunctionY1= 0;
			integrity=false;
		}
		if(settlementClaimFunctionX2 == null){
			Saga.warning("Failed to initialize settlementClaimFunctionX2 field for " + config + ". Setting default");
			settlementClaimFunctionX2= maximumLevel;
			integrity=false;
		}
		if(settlementClaimFunctionY2 == null){
			Saga.warning("Failed to initialize settlementClaimFunctionY2 field for " + config + ". Setting default");
			settlementClaimFunctionY2= 0;
			integrity=false;
		}
		

		// Faction settle:
		if(factionSettleFunctionX1 == null){
			Saga.warning("Failed to initialize factionSettleFunctionX1 field for " + config + ". Setting default");
			factionSettleFunctionX1= 0;
			integrity=false;
		}
		if(factionSettleFunctionY1 == null){
			Saga.warning("Failed to initialize factionSettleFunctionY1 field for " + config + ". Setting default");
			factionSettleFunctionY1= 0;
			integrity=false;
		}
		if(factionSettleFunctionX2 == null){
			Saga.warning("Failed to initialize factionSettleFunctionX2 field for " + config + ". Setting default");
			factionSettleFunctionX2= maximumLevel;
			integrity=false;
		}
		if(factionSettleFunctionY2 == null){
			Saga.warning("Failed to initialize factionSettleFunctionY2 field for " + config + ". Setting default");
			factionSettleFunctionY2= 0;
			integrity=false;
		}
		
		// Player settle:
		if(playerSettleFunctionX1 == null){
			Saga.warning("Failed to initialize playerSettleFunctionX1 field for " + config + ". Setting default");
			playerSettleFunctionX1= 0;
			integrity=false;
		}
		if(playerSettleFunctionY1 == null){
			Saga.warning("Failed to initialize playerSettleFunctionY1 field for " + config + ". Setting default");
			playerSettleFunctionY1= 0;
			integrity=false;
		}
		if(playerSettleFunctionX2 == null){
			Saga.warning("Failed to initialize playerSettleFunctionX2 field for " + config + ". Setting default");
			playerSettleFunctionX2= maximumLevel;
			integrity=false;
		}
		if(playerSettleFunctionY2 == null){
			Saga.warning("Failed to initialize playerSettleFunctionY2 field for " + config + ". Setting default");
			playerSettleFunctionY2= 0;
			integrity=false;
		}
		
		return integrity;
		
		
	}

	// Calculation:
	/**
	 * Calculates claim points the settlement has.
	 * 
	 * @param level level
	 */
	public Double calculateSettlementClaims(Short level) {

		
		if(level > settlementClaimFunctionX2){
			level = settlementClaimFunctionX2;
		}
		
		if(settlementClaimFunctionX2 - settlementClaimFunctionX1 == 0){
			Saga.severe("Settlement claim function has an undefined or infinite slope. Returning function value 1.");
			return 1.0;
		}
		
		double k = (double)(settlementClaimFunctionY2 - settlementClaimFunctionY1) / (double)( settlementClaimFunctionX2 - settlementClaimFunctionX1);
		double b = (double)settlementClaimFunctionY2 - k * settlementClaimFunctionX2;
		return k * level + b;
		
		
	}

	/**
	 * Calculates settle points the faction has.
	 * 
	 * @param level level
	 */
	public Double calculateFactionSettles(Short level) {

		
		if(level > factionSettleFunctionX2){
			level = factionSettleFunctionX2;
		}
		
		if(factionSettleFunctionX2 - factionSettleFunctionX1 == 0){
			Saga.severe("Faction settle function has an undefined or infinite slope. Returning function value 1.");
			return 1.0;
		}
		
		double k= (double)(factionSettleFunctionY2 - factionSettleFunctionY1) / (double)( factionSettleFunctionX2 - factionSettleFunctionX1);
		double b= (double)factionSettleFunctionY2 - k * factionSettleFunctionX2;
		return k * level + b;
		
		
	}

	/**
	 * Calculates settle points the player has.
	 * 
	 * @param level level
	 */
	public Double calculatePlayerSettles(Short level) {

		
		if(level > playerSettleFunctionX2){
			level = playerSettleFunctionX2;
		}
		
		if(playerSettleFunctionX2 - playerSettleFunctionX1 == 0){
			Saga.severe("player settle function has an undefined or infinite slope. Returning function value 1.");
			return 1.0;
		}
		
		double k= (double)(playerSettleFunctionY2 - playerSettleFunctionY1) / (double)( playerSettleFunctionX2 - playerSettleFunctionX1);
		double b= (double)playerSettleFunctionY2 - k * playerSettleFunctionX2;
		return k * level + b;
		
		
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
