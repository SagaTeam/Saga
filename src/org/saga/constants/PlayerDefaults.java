package org.saga.constants;

import java.util.ArrayList;

import org.saga.abilities.Ability;
import org.saga.professions.FighterProfession;
import org.saga.professions.Profession;
import org.saga.professions.WoodcutterProfession;

public class PlayerDefaults {

	
	/**
	 * Player name if the player has not yet been set.
	 */
	public static String name= "default~";
	
	/**
	 * Stamina.
	 */
	public static Double stamina=60.0;
	
	// Profession defaults:
	/**
	 * Profession level.
	 */
	public static Short level=0;

	/**
	 * Experience for the current level.
	 */
	public static Integer levelExperience=0;

	
	/**
	 * Returns all professions
	 * 
	 * @return all professions
	 */
	public static Profession[] allProfessions() {

		return new Profession[] {new FighterProfession(), new WoodcutterProfession()};
		
	}
	
	
}
