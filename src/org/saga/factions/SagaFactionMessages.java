package org.saga.factions;

import org.bukkit.ChatColor;

public class SagaFactionMessages {

public static ChatColor positiveHighlightColor = ChatColor.GREEN;
	

	// Colors:
	public static ChatColor neutralHighlightColor = ChatColor.YELLOW;
	
	public static ChatColor negativeHighlightColor = ChatColor.RED;
	
	public static ChatColor anouncmentColor = ChatColor.AQUA;
	

	// Factions:
	public static String prefixInvalid() {

		return negativeHighlightColor + "Prefix must be between "+ SagaFactionCommands.minimumPrefixLength + "-" + SagaFactionCommands.maximumPrefixLength +" characters and contain only letters and numbers.";
		
	}
	
	public static String nameInvalid() {

		return negativeHighlightColor + "Faction name must contain only letters and be between " + SagaFactionCommands.minimumNameLenght + "-" + SagaFactionCommands.maximumNameLength + " characters.";
		
	}
	
	public static String factionCantBeCreatedAlreadyInOne() {

		return negativeHighlightColor + "You must leave the current faction before you can create a new one.";
		
	}
	
	public static String factionCreated(String playerName, String factionName) {

		return anouncmentColor + playerName + " created " + factionName + " faction.";
		
	}
	
	public static String factionNewMemberJoined(ChatColor primaryColor, ChatColor secondaryColor, String name) {
		return primaryColor + name + " joined the faction.";
	}
	
	public static String factionJoinedNew(ChatColor primaryColor, ChatColor secondaryColor, String factionName) {
		return primaryColor + "You joined "+ factionName + " faction.";
	}
	
	
	
	
	
	
	
}
