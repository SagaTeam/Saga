package org.saga.constants;

import org.bukkit.ChatColor;
import org.saga.abilities.Ability;

public class PlayerMessages {

	/**
	 * Info color.
	 */
	public static ChatColor errorColor= ChatColor.DARK_RED;
	
	/**
	 * Info color.
	 */
	public static ChatColor infoColor= ChatColor.YELLOW;
	
	/**
	 * Warning color.
	 */
	public static ChatColor warningColor= ChatColor.RED;
	
	/**
	 * Haven color.
	 */
	public static ChatColor havenColor= ChatColor.BLUE;
	
	/**
	 * Arena color.
	 */
	public static ChatColor arenaColor= ChatColor.RED;
	
	/**
	 * First line of the error report to the player.
	 */
	public static String playerErrorMessage=errorColor+"Enouctered a problem, please contact the owner!";
	
	
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
	
	public static String abilitySelectNone() {

		return "No ability selected.";
				
	}
	
	public static String noAbilitiesAvailable(Ability ability) {
		
		return "Selected "+ability.getAbilityName()+" ability.";

	}
	
	public static String noAbilitiesAvailable() {
		
		return "There are no abilities avaliable.";

	}
	
	
}
