package org.saga;

import java.io.*;
import java.util.*;

import org.saga.abilities.*;
import org.saga.professions.*;

import com.google.gson.Gson;

public class BalanceInformation {

	
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
	 * Experience Intercept.
	 */
	public Integer experienceIntercept;

	/**
	 * Experience slope.
	 */
	public Integer experienceSlope;

	/**
	 * All abilities.
	 */
	public Hashtable<String, Ability> abilities = new Hashtable<String, Ability>();
	
	
	/**
	 * Sets defaults by checking integrity and adds abilities.
	 */
	public BalanceInformation() {
		
		
		// Add abilities:
		Ability ability;
		
		// Counterattack:
		ability= new CounterattackAbility();
		abilities.put(ability.getClass().getSimpleName(), ability);
		
		// Disarm:
		ability= new DisarmAbility();
		abilities.put(ability.getClass().getSimpleName(), ability);
		
		// Assign default values:
		checkIntegrity(new ArrayList<String>());
		
		
	}
	
	
	// Calculations:
	/**
	 * Returns the required experience for level up.
	 *
	 * @param pLevel
	 */
	public Integer calculateExperienceRequirement(Short pLevel) {


		return pLevel*experienceSlope*pLevel+experienceIntercept;


	}
	
	
	/**
	 * Checks the integrity of the balance information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	public Boolean checkIntegrity(ArrayList<String> problematicFields) {
		
		
		// All fields:
		if( maximumStamina == null ) {
                    maximumStamina= 100.0;
                    problematicFields.add("maximumStamina");
		}
		
		if( staminaPerSecond == null ){
                    staminaPerSecond= 0.1;
                    problematicFields.add("staminaPerSecond");
		}
		
		if( staminaPerSecond == null ){
                    staminaPerSecond= 0.1;
                    problematicFields.add("staminaPerSecond");
		}
		
		// All abilities:
		Enumeration<Ability> allAbilities= abilities.elements();
		while(allAbilities.hasMoreElements()){
                    allAbilities.nextElement().checkIntegrity(problematicFields);
		}
		
		return problematicFields.isEmpty();

	}

	
	public static void main(String[] args) {
		
		Gson gson = new Gson();
		SagaPlayer player = new SagaPlayer();
		ArrayList<String> integcheck = new ArrayList<String>();
		player.checkIntegrity(integcheck);
		for (int i = 0; i < integcheck.size(); i++) {
                    System.out.println(integcheck.get(i));
		}
		
		try {
                    write(gson.toJson(player));
		} catch (IOException e) {
                    e.printStackTrace();
		}
		
	}
	
	static void write(String towrit) throws IOException  {
	    
            try {
              // Create file
              FileWriter fstream = new FileWriter(new File("/home/andf/data/java/minecraft/plugins/SagaPlayer/test"));
              BufferedWriter out = new BufferedWriter(fstream);
              out.write(towrit);
              //Close the output stream
              out.close();
            } catch (Exception e) {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }

        }
	    
	 
	
}
