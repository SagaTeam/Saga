package org.saga;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.saga.abilities.Ability;

public class Messages {

	/**
	 * Low priority status messages like stamina regeneration.
	 */
	private static ChatColor WARNING= ChatColor.DARK_RED;
	
	/**
	 * First line of the error report to the player.
	 */
	public static String PLAYER_ERROR_MESSAGE=WARNING+"Enouctered a problem, please contact the owner!";
	
	
	/**
	 * Low priority status messages like stamina regeneration.
	 */
	private static ChatColor LOW_PRIORITY_STATUS_UPDATE= ChatColor.YELLOW;
	
	/**
	 * High priority status messages like ability activation.
	 */
	private static ChatColor HIGH_PRIORITY_STATUS_UPDATE= ChatColor.GREEN;
	
	
	/**
	 * Stamina lost in the normal way.
	 * 
	 * @param sagaPlayer
	 * @param usedStamina
	 */
	public static String staminaUsed(Double usedStamina, Double playerStamina, Double playerMaximumStamina) {

		return "Lost "+usedStamina+" stamina. ("+playerStamina+"/"+playerMaximumStamina+")";
		
	}
	
	/**
	 * Stamina gained in the normal way.
	 * 
	 * @param sagaPlayer
	 * @param regeneratedStamina
	 */
	public static String staminaRegeneration(Double playerStamina, Double playerMaximumStamina) {

		return "Stamina regeneration. ("+playerStamina+"/"+playerMaximumStamina+")";
		
	}
	
	public static String abilitySelect(Ability selectedAbility) {

		return "Selected "+selectedAbility.getAbilityName()+" ability.";
				
	}
	
	public static String noAbilitiesAvailable(Ability ability) {
		
		return "Selected "+ability.getAbilityName()+" ability.";

	}
	
	public static String noAbilitiesAvailable() {
		
		return "There are no abilities avaliable.";

	}
	
	
}
