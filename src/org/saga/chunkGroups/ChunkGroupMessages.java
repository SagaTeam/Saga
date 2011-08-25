package org.saga.chunkGroups;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.saga.SagaPlayer;
import org.saga.factions.SagaFaction;

public class ChunkGroupMessages {


	// Colors:
	public static ChatColor veryPositiveHighlightColor = ChatColor.DARK_GREEN; // DO NOT OVERUSE.
	
	public static ChatColor positiveHighlightColor = ChatColor.GREEN;
	
	public static ChatColor negativeHighlightColor = ChatColor.RED;
	
	public static ChatColor veryNegativeHighlightColor = ChatColor.RED; // DO NOT OVERUSE.
	
	public static ChatColor unavailableHighlightColor = ChatColor.DARK_GRAY;
	
	public static ChatColor anouncmentColor = ChatColor.AQUA;
	
	public static ChatColor normalTextColor1 = ChatColor.GOLD;
	
	public static ChatColor normalTextColor2 = ChatColor.YELLOW;
	
	public static ChatColor frameColor = ChatColor.DARK_GREEN;
	
	
	
	// General restriction:
	public static String noPermission(){
		return negativeHighlightColor + "You dont have permission to do that.";
	}
	
	public static String notInChunkGroup(){
		return negativeHighlightColor + "You aren't in a settlement.";
	}
	
	public static String notChunkGroupMember(ChunkGroup chunkGroup){
		return negativeHighlightColor + "You aren't a part of " + chunkGroup.getName() + " settlement.";
	}

	
	
	// Settle and claim messages:
	public static String settlesRemaining(Short settles) {
		

		ChatColor settlesColor = positiveHighlightColor;
		if(settles == 0){
			settlesColor = negativeHighlightColor;
		}
		return normalTextColor1 + "You have " + settlesColor + settles + normalTextColor1 + " settlement points remaining.";
		
		
	}
	
	public static String claimsRemaining(Short claims) {
		
		
		ChatColor claimsColor = positiveHighlightColor;
		if(claims == 0){
			claimsColor = negativeHighlightColor;
		}
		return normalTextColor1 + "You have " + claimsColor + claims + normalTextColor1 + " claim points remaining.";
		
		
	}
	
	
	
	// Enter leave messages:
	public static String enterChunkGroup(ChunkGroup chunkGroup) {
		
		
		ChatColor messageColor = normalTextColor1;
		StringBuffer rString = new StringBuffer();
		ArrayList<SagaFaction> factions = chunkGroup.getRegisteredFactions();
		ArrayList<SagaPlayer> players = chunkGroup.getRegisteredPlayers();
		
		rString.append(chunkGroup.getName());
		
		if(factions.size() != 0 || players.size() != 0 ){
			rString.append(", owned by ");
		}
			
		if(factions.size() != 0){
			SagaFaction sagaFaction = factions.get(0);
			rString.append(sagaFaction.getPrimaryColor() + sagaFaction.getName() + messageColor+ " faction");
		}
		
		if(players.size() != 0){
			if(factions.size() != 0){
				rString.append(" and ");
			}
			SagaPlayer sagaPlayer = players.get(0);
			rString.append(normalTextColor1 + sagaPlayer.getName());
		}
		
		return messageColor + rString.toString();
		
		
	}
	
	public static String leaveChunkGroup() {
		return normalTextColor1 + "Wilderness";
	}
	
	
	
	// Found delete claim abandon messages:
	public static String foundedChunkGroupBroadcast(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return anouncmentColor + sagaPlayer.getName() + " has founded the new settlement " + settlement.getName() + ".";
	}

	public static String deletedChunkGroupBroadcast(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return anouncmentColor + settlement.getName() + " settlement was disolved by " + sagaPlayer.getName();
	}
	
	public static String claimedChunkBroadcast(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return anouncmentColor +  "This chunk was claimed for " + settlement.getName() + " settlement.";
	}
	
	public static String abandonedChunkBroadcast(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return anouncmentColor + "This chunk has been abandoned from " + settlement.getName() + " settlement.";
	}
	
	

	// Found delete claim abandon restrictions:
	public static String notEnoughFactionSettles() {
		return negativeHighlightColor + "The faction doesn't have any settlement points.";
	}
	
	public static String notEnoughSettles() {
		return negativeHighlightColor + "You don't have any settle points.";
	}
	
	public static String notEnoughClaims() {
		return negativeHighlightColor + "Settlemen doesn't have any claim points.";
	}

	public static String chunkClaimed(){
		return negativeHighlightColor + "This chunk of land has already been claimed.";
	}
	
	public static String chunkNotClaimed(){
		return negativeHighlightColor + "This chunk of land isn't claimed.";
	}

	public static String chunkMustBeAdjacent(){
		return negativeHighlightColor + "You can only claim chunks adjacent to an existing settlement.";
	}

	
	

	// Invite join leave messages:
	public static String beenInvited(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + "You have been invited to " + settlement.getName() + " settlement.";
	}
	
	public static String invitedPlayer(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + sagaPlayer.getName() + " was invited to the settlement.";
	}
	
	
	public static String joinedChunkGroup(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + "You joined " +settlement.getName() + " settlement.";
	}
	
	public static String playerJoined(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + sagaPlayer.getName() + " has joined the settlement.";
	}
	
	
	public static String quitChunkGroup(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + "You left from " + settlement.getName() + " settlement.";
	}
	
	public static String playerQuit(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + sagaPlayer.getName() + " has left the settlement.";
	}

	
	public static String kickedFromChunkGroup(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + "You have been kicked out of " + settlement.getName() + " settlement.";
	}
	
	public static String playerKicked(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return normalTextColor1 + sagaPlayer.getName() + " has been kicked from the settlement.";
	}
	
	
	public static String notInTheChunkGroup(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return negativeHighlightColor + sagaPlayer.getName() + " isn't part of the settlement.";
	}
	

	public static String declinedInvites() {
		return normalTextColor1 + "Declined all settlement invitations.";
	}

	public static String pendingInvitations(SagaPlayer sagaPlayer, ArrayList<ChunkGroup> groups) {
		
		
		StringBuffer rString = new StringBuffer();
		ChatColor messageColor = positiveHighlightColor;
		
		if(groups.size() == 0){
			return messageColor + "You don't have a pending settlement invitation.";
		}
		
		rString.append(messageColor);
		
		rString.append("You have");
		
		if(groups.size() == 1){
			rString.append(" a pending invitation from ");
		}else{
			rString.append(" pending invitations from ");
		}
		
		for (int i = 0; i < groups.size(); i++) {
			if( i != 0 ) rString.append(", ");
			rString.append(groups.get(i).getName());
		}
		
		if(groups.size() == 1){
			rString.append(" settlement.");
		}else{
			rString.append(" settlements.");
		}
		
		return rString.toString();
		
		
	}
	

	
	
	// Invite join  leave restrictions:
	public static String noFactionInvites() {
		return negativeHighlightColor + "The faction doesn't have a settlement invitation.";
	}
	
	public static String noFactionInvites(String factionName) {
		return negativeHighlightColor + "The factions doesn't have an invitation to " + factionName + " settlement.";
	}
	
	public static String noInvites() {
		return negativeHighlightColor + "You don't have a settlement invitation.";
	}
	
	public static String noInvites(String factionName) {
		return negativeHighlightColor + "You don't have an invitation to " + factionName + " settlement.";
	}
	
	public static String cantAcceptInvitations() {

		return negativeHighlightColor + "You can't accept settlement invitations.";
		
	}

	public static String cantInviteYourself(SagaPlayer sagaPlayer, ChunkGroup chunkGroup) {
		return negativeHighlightColor + "You can't invite yourself.";
	}

	public static String cantKickYourself(SagaPlayer sagaPlayer, ChunkGroup settlement) {
		return negativeHighlightColor + "You can't kick yourself from the settlement.";
	}

	public static String nonExistantChunkGroup(String groupName) {
		return negativeHighlightColor + groupName + " settlement doesn't exist.";
	}
	
	public static String nonExistantChunkGroup() {
		return negativeHighlightColor + "Settlement doesn't exist.";
	}
	
	public static String nonExistantPlayer(String playerName) {
		return negativeHighlightColor + playerName + " doesn't exist.";
	}
	
	public static String nonExistantFaction(String factionName) {
		return negativeHighlightColor + factionName + " faction doesn't exist.";
	}
	
	public static String factionAlreadyInTheChunkGroup(SagaFaction faction, ChunkGroup group) {
		return negativeHighlightColor + faction.getName() + " faction is already a part of the settlement.";
	}
	
	public static String alreadyInTheChunkGroup(SagaPlayer sagaPlayer, ChunkGroup group) {
		return negativeHighlightColor + sagaPlayer.getName() + " is already a part of the settlement.";
	}

	public static String playerNotChunkGroupMember(SagaPlayer sagaPlayer, ChunkGroup chunkGroup) {
		return negativeHighlightColor + sagaPlayer.getName() + " isn't part of the settlement.";
	}
	
	
	
}
