package org.saga;

import org.saga.professions.ProfessionHolder;

public class PlayerDefaults {

	/**
	 * Player name if the player has not yet been set.
	 */
	public static String name= "default~";

	/**
	 * Stamina.
	 */
	public static Double stamina=60.0;


	public static Double STAMINA_REGENERATION_PER_SECOND= 0.1;

	public static Short STAMINA_FUNCTION_LEVEL_FIRST= 100;

	public static Short STAMINA_FUNCTION_STAMINA_FIRST= 10000;

	public static Short STAMINA_FUNCTION_LEVEL_SECOND= 101;

	public static Short STAMINA_FUNCTION_STAMINA_SECOND= 10000;

	public static Integer EXPERIENCE_SLOPE= 1000000;

	public static Integer EXPERIENCE_INTERCEPT= 1000000;


	// Player:
	public static Double STAMINA= 50.0;

	public static Short LEVEL= 0;

	public static Short SELECTED_ABILITY= 0;



	/**
	 * Experience for the current level.
	 */
	public static Integer levelExperience=0;




}
