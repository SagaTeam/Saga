package org.saga.factions;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.saga.SagaPlayer;
import org.saga.chunkGroups.ChunkGroup;
import org.saga.constants.PlayerMessages;
import org.saga.constants.PlayerMessages.ColorCircle;

public class FactionMessages {

public static ChatColor positiveHighlightColor = ChatColor.GREEN;
	


	// Colors:
	public static ChatColor normalTextColor = ChatColor.YELLOW;
	
	public static ChatColor negativeHighlightColor = ChatColor.RED;
	
	public static ChatColor anouncmentColor = ChatColor.AQUA;
	


	// General restriction:
	public static String noPermission(SagaFaction faction){
		return faction.getSecondaryColor() + "You dont have permission to do that.";
	}

	public static String noFaction() {
		return negativeHighlightColor + "You don't have a faction.";
	}
	
	
	// Faction restriction:
	public static String mustSelectOneFaction() {
		return "You must have one faction selected.";
	}

	public static String oneFactionAllowed() {

		return negativeHighlightColor + "You can only have one faction.";
		
	}
	
	public static String nonExistantFaction(String factionName) {
		return negativeHighlightColor + factionName + " faction doesn't exist.";
	}
	
	public static String nonExistantFaction() {
		return negativeHighlightColor + "Faction doesn't exist.";
	}

	public static String alreadyInTheFaction(SagaPlayer sagaPlayer, SagaFaction faction) {
		return negativeHighlightColor + sagaPlayer.getName() + " is already in the faction.";
	}
	
	
	
	// Specific stats:
	public static String factionList(SagaFaction faction, ChatColor primaryColor, ChatColor secondaryColor) {

		
		
		
		return "";
	}
	
	public static String factionStats(SagaFaction faction) {

		
		StringBuffer rString = new StringBuffer();
		ColorCircle mainColors = (new ColorCircle()).addColor(faction.getPrimaryColor()).addColor(faction.getSecondaryColor());
		
		// Colors:
		rString.append(mainColors.nextColor());
		rString.append("Primary color:" + faction.getPrimaryColor().name());
		rString.append(" ");
		rString.append("Secondary color:" + faction.getSecondaryColor().name());
		
		rString.append("\n");
		
		// Claims and settles:
		rString.append(mainColors.nextColor());
		rString.append("" + faction.getTotalSettled() + "/" + faction.getTotalSettles() + " settlements");
		
		// Settlements:
		if(faction.getChunkGroupCount() > 0){
			
			rString.append("\n");
			
			rString.append(settlementElement(faction, mainColors.nextColor()));
			
		}
		
		return PlayerMessages.frame(faction.getName(), rString.toString(), faction.getPrimaryColor());
		
		
	}
	
	private static String settlementElement(SagaFaction faction, ChatColor messageColor) {
		
		
		// Initialize:
		ArrayList<ChunkGroup> settlements = faction.getRegisteredChunkGroups();
		StringBuffer rString = new StringBuffer();
		
		// All settlements:
		for (int i = 0; i < settlements.size(); i++) {
			if(i != 0){
				rString.append(", ");
			}
			rString.append(settlements.get(i).getName());
			rString.append("(" + settlements.get(i).getSize() + ")");
		}
		
		// Title:
		if(settlements.size() == 0){

		}else if(settlements.size() == 1){
			rString.insert(0, "Settlement: ");
		}else{
			rString.insert(0, "Settlements: ");
		}
		
		// Color:
		rString.insert(0, messageColor);
		
		return rString.toString();
		
		
	}
	
	

	// Create delete:
	public static String created(SagaPlayer owner, String factionName) {

		return anouncmentColor + owner.getName() + " formed " + factionName + " faction.";
		
	}

	public static String deleted(SagaPlayer owner, String factionName) {

		return anouncmentColor + owner.getName() + " disbanded " + factionName + " faction.";
		
	}
	
	

	// Invite join leave messages:
	public static String beenInvited(SagaPlayer sagaPlayer, SagaFaction faction) {
		return faction.getSecondaryColor() + "You have been invited to " + faction.getPrimaryColor() + faction.getName() + faction.getSecondaryColor() + " faction.";
	}
	
	public static String invitedPlayer(SagaPlayer sagaPlayer, SagaFaction faction) {
		return faction.getSecondaryColor() + "" + sagaPlayer.getName() + " was invited to the faction.";
	}
	
	
	public static String joinedFaction(SagaPlayer sagaPlayer, SagaFaction faction) {
		return faction.getSecondaryColor() + "You joined " + faction.getPrimaryColor() + faction.getName() + faction.getSecondaryColor() + " faction.";
	}
	
	public static String playerJoined(SagaPlayer sagaPlayer, SagaFaction faction) {
		return faction.getSecondaryColor() + sagaPlayer.getName() + " has joined the faction.";
	}
	
	
	public static String quitFaction(SagaPlayer sagaPlayer, SagaFaction sagaFaction) {
		return sagaFaction.getSecondaryColor() + "You have quit your faction.";
	}
	
	public static String playerQuit(SagaPlayer sagaPlayer, SagaFaction sagaFaction) {
		return sagaFaction.getSecondaryColor() + sagaPlayer.getName() + " has quit the faction.";
	}

	
	public static String kickedFromFaction(SagaPlayer sagaPlayer, SagaFaction sagaFaction) {
		return sagaFaction.getSecondaryColor() + "You have been kicked out of your faction.";
	}
	
	public static String playerKicked(SagaPlayer sagaPlayer, SagaFaction sagaFaction) {
		return sagaFaction.getSecondaryColor() + sagaPlayer.getName() + " has been kicked from the faction.";
	}
	
	
	public static String cantKickYourself(SagaPlayer sagaPlayer, SagaFaction faction) {
		return negativeHighlightColor + "Can't kick yourself from the faction.";
	}
	
	public static String notFactionMember(SagaPlayer sagaPlayer, SagaFaction faction) {
		return negativeHighlightColor + sagaPlayer.getName() + " isn't part of the faction.";
	}
	
	
	
	// Invite join  leave restrictions:
	public static String noInvites() {
		return negativeHighlightColor + "You don't have a faction invitation.";
	}
	
	public static String noInvites(String factionName) {
		return negativeHighlightColor + "You don't have an invitation to " + factionName + " faction.";
	}
	
	public static String cantAcceptInvitations() {

		return negativeHighlightColor + "You can't accept faction invitations.";
		
	}
	
	public static String declinedInvites() {
		return normalTextColor + "Declined all faction invitations.";
	}

	public static String pendingInvitations(SagaPlayer sagaPlayer, ArrayList<SagaFaction> factions) {
		
		
		StringBuffer rString = new StringBuffer();
		ChatColor messageColor = positiveHighlightColor;
		
		if(factions.size() == 0){
			return messageColor + "You don't have a pending faction invitation.";
		}
		
		rString.append(messageColor);
		
		rString.append("You have");
		
		if(factions.size() == 1){
			rString.append(" a pending invitation from ");
		}else{
			rString.append(" pending invitations from ");
		}
		
		for (int i = 0; i < factions.size(); i++) {
			if( i != 0 ) rString.append(", ");
			rString.append(factions.get(i).getPrimaryColor() + factions.get(i).getName() + messageColor);
		}
		
		if(factions.size() == 1){
			rString.append(" faction.");
		}else{
			rString.append(" factions.");
		}
		
		return rString.toString();
		
		
	}
	
	public static String cantInviteYourself(SagaPlayer sagaPlayer, SagaFaction faction) {
		return negativeHighlightColor + "Yo dawg. I herd you like " + faction.getName() + " faction. So we invited you to your own faction, so you can be in your faction, while you are in your faction.";
	}

	

	// Other:
	public static String prefixInvalid() {

		return negativeHighlightColor + "Prefix must be between "+ FactionCommands.minimumPrefixLength + "-" + FactionCommands.maximumPrefixLength +" characters and contain only letters and numbers.";
		
	}
	
	public static String invalidName() {
		
		return negativeHighlightColor + "Name must be " + FactionCommands.minimumNameLenght + "-" + FactionCommands.maximumNameLength + " characters, letters and numbers only.";
		
	}

	public static String prefixInUse() {
		return negativeHighlightColor + "Prefix already in use.";
	}
	
	public static String nameInUse() {
		return negativeHighlightColor + "Name already in use.";
	}
	
	
}
