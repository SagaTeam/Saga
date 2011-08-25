package org.saga.factions;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.chunkGroups.ChunkGroup;
import org.saga.chunkGroups.ChunkGroupManager;
import org.saga.chunkGroups.ChunkGroupMessages;
import org.saga.chunkGroups.SagaChunk;
import org.saga.constants.PlayerMessages;
import org.saga.exceptions.NonExistantSagaPlayerException;
import org.saga.professions.Profession.ProfessionType;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;




public class FactionCommands {

	// TODO Create faction config
	public static Integer maximumPrefixLength = 6;
	
	public static Integer minimumPrefixLength = 3;
	
	public static Integer maximumNameLength = 20;
	
	public static Integer minimumNameLenght = 4;
	
	public static Integer noDeleteMemberCount = 15;
	
	public static Integer noDeleteClaimCount = 5;
	
	
	// Properties keys:
//	public static String factionDeleteProperty = "faction_delete";
//	
//	public static String factionSettleProperty = "faction_settle";
	
	
	// Commands:
	@Command(
            aliases = {"factioncreate", "fcreate"},
            usage = "<faction name> <faction prefix>",
            flags = "",
            desc = "Create a new faction.",
            min = 2,
            max = 2
        )
	@CommandPermissions({"saga.user.faction.create"})
	public static void create(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
	    	

	    	// Already in faction:
	    	if(sagaPlayer.getFactionCount() > 0){
	    		sagaPlayer.sendMessage(FactionMessages.oneFactionAllowed());
	    		return;
	    	}
	    	
	    	// Validate prefix:
	    	if(!validatePrefix(args.getString(1))){
	    		sagaPlayer.sendMessage(FactionMessages.prefixInvalid());
	    		return;
	    	}

	    	// Check prefix:
	    	if( FactionManager.getFactionManager().factionWithPrefix(args.getString(1)) != null){
	    		sagaPlayer.sendMessage( FactionMessages.prefixInUse() );
	    		return;
	    	}
	    	
	    	// Validate name:
	    	if(!validateName(args.getString(0))){
	    		sagaPlayer.sendMessage(FactionMessages.invalidName());
	    		return;
	    	}
	    	
	    	// Check name:
	    	if( FactionManager.getFactionManager().factionWithName(args.getString(0) ) != null){
	    		sagaPlayer.sendMessage( FactionMessages.nameInUse() );
	    		return;
	    	}
	    	
	    	// Create faction:
	    	SagaFaction faction = FactionManager.getFactionManager().createFaction(args.getString(0), args.getString(1), sagaPlayer);
	    	
	    	// Broadcast:
	    	Saga.broadcast(FactionMessages.created(sagaPlayer, faction.getName()));
	    	
	    	
	    }

	@Command(
            aliases = {"factiondelete", "fdelete", "factiondisband", "fdisband"},
            usage = "",
            flags = "",
            desc = "Disbands the faction."
        )
	@CommandPermissions({"saga.user.faction.delete"}
	)
	public static void disband(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
		
		
		// Part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
		}
		
		// Faction selection:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Permission:
		if(!selectedFaction.canDelete(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.noPermission(selectedFaction));
			return;
		}
		
		// Delete faction:
		selectedFaction.delete();
		
		// Broadcast:
    	Saga.broadcast(FactionMessages.deleted(sagaPlayer, selectedFaction.getName()));
    	
		
	}
	
	 @Command(
	            aliases = {"factionlist","flist", "fwho","factionmembers"},
	            usage = "<page>",
	            flags = "",
	            desc = "List faction memebers.",
	            min = 0,
	            max = 1
	        )
	        @CommandPermissions({"legends.faction.list"})
	public static void list(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

		 
		// Check if part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
	 	}
		
		// Faction selection:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Send message:
//		sagaPlayer.sendMessage(SagaFactionMessages.factionList());
		
     
     }
	
	@Command(
	            aliases = {"factionstats", "fstats"},
	            usage = "",
	            flags = "",
	            desc = "Shows faction stats.",
	            min = 0,
	            max = 0)
	@CommandPermissions({"saga.user.faction.stats"})
	public static void stats(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
		 

		// Part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
		}
			
		// Faction selection:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
			
		sagaPlayer.sendMessage(FactionMessages.factionStats(selectedFaction));
		
		
	}

	@Command(
            aliases = {"factioninvite", "finvite"},
            usage = "<player name>",
            flags = "",
            desc = "Invite a player to join your faction.",
            min = 1,
            max = 1)
	@CommandPermissions({"saga.user.faction.invite"})
	public static void invite(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


		// Part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
		}
		
		// Faction selection:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Permission:
		if(!selectedFaction.canInvite(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.noPermission(selectedFaction));
			return;
		}
		
		// Force player:
		SagaPlayer invitedPlayer;
		try {
			invitedPlayer = Saga.plugin().forceSagaPlayer(args.getString(0));
		} catch (NonExistantSagaPlayerException e) {
			sagaPlayer.sendMessage(PlayerMessages.playerNonexistant(args.getString(0)));
			return;
		}
		
		// Already in faction:
		if(invitedPlayer.hasFaction(selectedFaction)){
			sagaPlayer.sendMessage(FactionMessages.alreadyInTheFaction(invitedPlayer, selectedFaction));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Invited yourself:
		if(invitedPlayer.equals(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.cantInviteYourself(sagaPlayer, selectedFaction));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Invite:
		invitedPlayer.factionInvite(selectedFaction);
		
		// Unforce:
		Saga.plugin().unforceSagaPlayer(args.getString(0));
		
		// Inform:
		selectedFaction.broadcast(FactionMessages.invitedPlayer(invitedPlayer, selectedFaction));
		invitedPlayer.sendMessage(FactionMessages.beenInvited(invitedPlayer, selectedFaction));
		
		
	}
	
	@Command(
            aliases = {"fdeclineall", "fdecline"},
            usage = "",
            flags = "",
            desc = "Decline all faction join invitations.",
            min = 0,
            max = 0)
	@CommandPermissions({"saga.user.faction.decline"})
	public static void decline(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


    	// Decline every invitation:
    	ArrayList<Integer> factionIds = sagaPlayer.getFactionInvites();
    	for (int i = 0; i < factionIds.size(); i++) {
			sagaPlayer.removeFactionInvite(factionIds.get(i));
		}
    	
    	// Inform:
    	sagaPlayer.sendMessage(FactionMessages.declinedInvites());
		
		
	}
	
	@Command(
            aliases = {"factionaccpet", "faccept"},
            usage = "<faction name>",
            flags = "",
            desc = "Accept a faction join invitation.",
            min = 0,
            max = 1)
	@CommandPermissions({"saga.user.faction.accept"})
	public static void accept(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


		// In faction:
    	if(sagaPlayer.getFactionCount() > 0){
    		sagaPlayer.sendMessage(FactionMessages.cantAcceptInvitations());
    		return;
    	}
    	
    	// No invites:
    	if(sagaPlayer.getFactionInvites().size() == 0){
    		sagaPlayer.sendMessage(FactionMessages.noInvites());
    		return;
    	}
    	
    	// Find faction:
    	SagaFaction sagaFaction = null;
    	ArrayList<Integer> invitationIds = sagaPlayer.getFactionInvites();
    	// No parameters:
    	if(args.argsLength() == 0 && invitationIds.size() > 0){
    		sagaFaction = FactionManager.getFactionManager().getFaction(invitationIds.get( invitationIds.size() -1 ));
    	}
    	// Faction name parameter:
    	else if(args.argsLength() == 1){
    		for (int i = 0; i < invitationIds.size(); i++) {
				SagaFaction faction = FactionManager.getFactionManager().getFaction(invitationIds.get(i));
				if( faction != null && faction.getName().equals(args.getString(0)) ){
					sagaFaction = faction;
					break;
				}
			}
    	}
    	
    	// Faction doesn't exist:
    	if(sagaFaction == null && args.argsLength() == 1){
    		sagaPlayer.sendMessage(FactionMessages.nonExistantFaction(args.getString(0)));
    		return;
    	}else if(sagaFaction == null){
    		sagaPlayer.sendMessage( FactionMessages.nonExistantFaction() );
    		return;
    	}
    	
    	// Inform:
    	sagaFaction.broadcast(FactionMessages.playerJoined(sagaPlayer, sagaFaction));
		sagaPlayer.sendMessage(FactionMessages.joinedFaction(sagaPlayer, sagaFaction));

    	// Add to faction:
    	sagaFaction.addMember(sagaPlayer);
    	
    	// Remove all invitations:
    	for (int i = 0; i < invitationIds.size(); i++) {
			sagaPlayer.removeFactionInvite(invitationIds.get(i));
		}
    	
    	
	}
	
	@Command(
            aliases = {"factionquit","fquit"},
            usage = "",
            flags = "",
            desc = "Quit current faction.",
            min = 0,
            max = 0
		)
	@CommandPermissions({"saga.user.faction.quit"})
	public static void quit(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
		

		// Part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
		}
		
		// Faction selection:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Quit:
		selectedFaction.removeMember(sagaPlayer);
		
		// Inform:
		selectedFaction.broadcast(FactionMessages.playerQuit(sagaPlayer, selectedFaction));
		sagaPlayer.sendMessage(FactionMessages.quitFaction(sagaPlayer, selectedFaction));
		
		
	}
	
	@Command(
            aliases = {"factionkick","fkick"},
            usage = "<player name>",
            flags = "",
            desc = "Kick a member out of the faction.",
            min = 1,
            max = 1
		)
	@CommandPermissions({"saga.user.faction.kick"})
	public static void kick(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
		

		// Part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
		}
		
		// Faction selection:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Permission:
		if(!selectedFaction.canKick(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.noPermission(selectedFaction));
			return;
		}

		// Force player:
		SagaPlayer kickedPlayer;
		try {
			kickedPlayer = Saga.plugin().forceSagaPlayer(args.getString(0));
		} catch (NonExistantSagaPlayerException e) {
			sagaPlayer.sendMessage(PlayerMessages.playerNonexistant(args.getString(0)));
			return;
		}
		
		// Not in the faction:
		if(!selectedFaction.isMember(kickedPlayer)){
			sagaPlayer.sendMessage(FactionMessages.notFactionMember(kickedPlayer, selectedFaction));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Kicked yourself:
		if(kickedPlayer.equals(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.cantKickYourself(sagaPlayer, selectedFaction));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Kick:
		selectedFaction.removeMember(kickedPlayer);
		
		// Unforce:
		Saga.plugin().unforceSagaPlayer(args.getString(0));
		
		// Inform:
		selectedFaction.broadcast(FactionMessages.playerKicked(kickedPlayer, selectedFaction));
		kickedPlayer.sendMessage(FactionMessages.kickedFromFaction(kickedPlayer, selectedFaction));
		
		
	}
	
	// Validate:
	public static boolean validatePrefix(String str) {

         if(org.saga.utility.TextUtil.getComparisonString(str).length() < minimumPrefixLength ) {
             return false;
         }

         if(str.length() > maximumPrefixLength) {
             return false;
         }

         for (char c : str.toCharArray()) {
                 if ( ! org.saga.utility.TextUtil.substanceChars.contains(String.valueOf(c))) {
                     return false;
                 }
         }

         return true;

 }

	public static boolean validateName(String str) {

         if(org.saga.utility.TextUtil.getComparisonString(str).length() < minimumNameLenght ) {
        	 return false;
         }

         if(str.length() > maximumNameLength) {
        	 return false;
         }

         for (char c : str.toCharArray()) {
                 if ( ! org.saga.utility.TextUtil.substanceChars.contains(String.valueOf(c))) {
                	 return false;
                 }
         }

         return true;

 }
	
	



}
