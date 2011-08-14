package org.saga;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.saga.abilities.Ability;
import org.saga.abilities.ChopDownAbility;
import org.saga.abilities.CounterattackAbility;
import org.saga.abilities.DisarmAbility;
import org.saga.abilities.FireballAbility;
import org.saga.abilities.FocusedHitsAbility;
import org.saga.abilities.HarvestAbility;
import org.saga.abilities.HeavyHitAbility;
import org.saga.abilities.PowerfulSwings;
import org.saga.abilities.ResistLavaAbility;
import org.saga.abilities.TreeClimbAbility;
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
	 * Ability and profession name associations.
	 */
	private Hashtable<String, String[]> abilitiAssociations;
	
	// Quick access:
	/**
	 * Quick access ability pool.
	 */
	transient private Hashtable<String, Ability> abilityPool;
	
	
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
		
		// Ability associations:
		if(abilitiAssociations == null){
			abilitiAssociations = new Hashtable<String, String[]>();
			Saga.severe("Initializing abilitiAssociations field for profession information.");
			Saga.info("Adding two example profession ability associations.");
			abilitiAssociations.put("ProfessionName1", new String[]{"AbilityName1", "AbilityName2", "AbilityName3"});
			abilitiAssociations.put("ProfessionName2", new String[]{"AbilityName1", "AbilityName2"});
			integrity=false;
		}
		
		Enumeration<String> keys= abilitiAssociations.keys();
        while ( keys.hasMoreElements() ) {
        	String key = keys.nextElement();
        	String[] value = abilitiAssociations.get(key);
        	if(value == null){
        		Saga.severe("Profession ability associations has a null value. Setting empty value.");
        		value = new String[0];
        		abilitiAssociations.put(key, value);
        	}
        	for (int i = 0; i < value.length; i++) {
				if(value[i] == null){
					Saga.severe("Profession ability associations has a null value element. Setting empty element.");
	        		value[i] = "";
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
        
		// Fill ability pool:
        abilityPool = new Hashtable<String, Ability>();
		for (int i = 0; i < abilities.size(); i++) {
			abilityPool.put(abilities.get(i).getAbilityName(), abilities.get(i));
		}
        
		return integrity;
		
		
	}

	
	// Interaction:
	/**
	 * Gets abilities associated with the given profession name
	 * 
	 * @param professionName profession name
	 * @return all abilities associated. Empty if none
	 */
	public Ability[] getAbilities(String professionName) {
		
		
		String[] abilityNames = abilitiAssociations.get(professionName);
		
		// Return empty if no abilities found:
		if(abilityNames == null){
			return new Ability[0];
		}
		
		// Add abilities:
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for (int i = 0; i < abilityNames.length; i++) {
			Ability ability = abilityPool.get(abilityNames[i]);
			if(ability != null){
				abilities.add(ability);
			}
		}
		
		return abilities.toArray(new Ability[abilities.size()]); 
		
		
	}
	
	/**
	 * Returns all abilities.
	 * 
	 * @return all abilities
	 */
	private Ability[] getDefaultAbilities() {

		return new Ability[]{new HeavyHitAbility(), new CounterattackAbility(), new DisarmAbility(), new PowerfulSwings(), new ResistLavaAbility(), new FocusedHitsAbility(), new ChopDownAbility(), new TreeClimbAbility(), new HarvestAbility(), new FireballAbility()};
		
	}
	
	
	// Saving and loading:
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
	
	
}
