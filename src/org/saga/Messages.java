package org.saga;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.saga.abilities.Ability;

public class Messages {

	/**
	 * First line of the error report to the player.
	 */
	public static String PLAYER_ERROR_MESSAGE="Error, please contact somebody";
	
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
	public static void staminaUsed(SagaPlayer sagaPlayer, Double usedStamina) {

		sagaPlayer.getPlayer().sendMessage("Lost "+usedStamina+" stamina. ("+sagaPlayer.getStamina()+"/"+sagaPlayer.getMaximumStamina()+")");
		
	}
	
	/**
	 * Stamina gained in the normal way.
	 * 
	 * @param sagaPlayer
	 * @param regeneratedStamina
	 */
	public static void staminaRegeneration(SagaPlayer sagaPlayer, Double regeneratedStamina) {

		if(((int)(sagaPlayer.getStamina()/10))!=((int)((sagaPlayer.getStamina()-regeneratedStamina)/10)))
		sagaPlayer.getPlayer().sendMessage("Stamina regeneration. ("+sagaPlayer.getStamina()+"/"+sagaPlayer.getMaximumStamina()+")");
		
	}
	
	public static void abilitySelect(SagaPlayer sagaPlayer, Ability selectedAbility) {

		sagaPlayer.getPlayer().sendMessage("Selected "+selectedAbility.getAbilityName()+" ability.");
				
	}
	
	public static void noAbilitiesAvailable(SagaPlayer sagaPlayer, Ability ability) {
		
		sagaPlayer.getPlayer().sendMessage("Selected "+ability.getAbilityName()+" ability.");

	}
	
	public static void noAbilitiesAvailable(SagaPlayer sagaPlayer) {
		
		sagaPlayer.getPlayer().sendMessage("You dont have any abilities.");

	}
	
	
}
