package org.saga.chunkGroups;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.chunkGroups.SagaChunk.ChunkSide;
import org.saga.constants.PlayerMessages;
import org.saga.exceptions.NonExistantSagaPlayerException;
import org.saga.factions.FactionManager;
import org.saga.factions.FactionMessages;
import org.saga.factions.SagaFaction;
import org.saga.factions.FactionCommands;
import org.saga.factions.Settlement;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

public class ChunkGroupCommands {

	
	 @Command(
	            aliases = {"factionclaim", "fclaim","fc", "claim"},
	            usage = "<settlement name>",
	            flags = "",
	            desc = "Claim a chunk of land adjacent to a settlement and add it to that settlement.",
	            min = 0,
	            max = 0
	        )
		@CommandPermissions({"saga.user.faction.claim"})
	public static void claim(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

			
		// Check location:
	   	SagaChunk locationChunk = sagaPlayer.getSagaChunk();
	   	Location location = sagaPlayer.getLocation();
	   	if(location == null){
	   		Saga.severe("Saga player location is null. Ignoring command.", sagaPlayer.getName());
	   		sagaPlayer.severe(PlayerMessages.failedToRetrieveSagaPlayerLocation());
	   		return;
	   	}
	   	Chunk bukkitChunk = location.getWorld().getChunkAt(location);
	  
	   	// Already claimed:
	   	if(locationChunk != null){
			sagaPlayer.sendMessage(ChunkGroupMessages.chunkClaimed());
			return;
		}
		
	   	
		// Not adjacent:
		ArrayList<ChunkGroup> adjacentGroups = getAdjacentGroups(bukkitChunk);
	   	if(adjacentGroups.size() == 0){
			sagaPlayer.sendMessage(ChunkGroupMessages.chunkMustBeAdjacent());
			return;
		}
	   	
	   	// Permissions:
	   	ArrayList<Settlement> settlements = new ArrayList<Settlement>();
	   	for (ChunkGroup chunkGroup : adjacentGroups) {
			if(chunkGroup instanceof Settlement && chunkGroup.canClaim(sagaPlayer)){
				settlements.add((Settlement) chunkGroup);
				break;
			}
		}
	   	if(settlements.size() == 0){
	   		sagaPlayer.sendMessage(ChunkGroupMessages.noPermission());
	   		return;
	   	}
	   	
	   	// Claim points:
	   	ArrayList<Settlement> withClaims = new ArrayList<Settlement>();
	   	for (Settlement settlement : settlements) {
			if(settlement.getRemainingClaims() > 0){
				withClaims.add(settlement);
				break;
			}
		}
	   if(withClaims.size() == 0){
		   sagaPlayer.sendMessage(ChunkGroupMessages.notEnoughClaims());
		   return;
	   }
		
		// Add a new chunk to adjacent chunk group:
		SagaChunk sagaChunk = new SagaChunk(bukkitChunk);
		withClaims.get(0).addChunk(sagaChunk);
		
		// Inform:
		sagaChunk.broadcast(ChunkGroupMessages.claimedChunkBroadcast(sagaPlayer, withClaims.get(0)));
		
		
	}
	
	 @Command(
	            aliases = {"factionsettle", "fsettle"},
	            usage = "<settlement name>",
	            flags = "",
	            desc = "Create a new settlement.",
	            min = 1,
	            max = 1
	        )
		@CommandPermissions({"saga.user.faction.settlement.create"})
	public static void settle(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


		// Part of a faction:
		if(sagaPlayer.getFactionCount() == 0){
			sagaPlayer.sendMessage(FactionMessages.noFaction());
			return;
		}
		
		// One faction is selected:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Permission:
		if(!selectedFaction.canSettle(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.noPermission(selectedFaction));
			return;
		}
		
		// Location:
    	SagaChunk locationChunk = sagaPlayer.getSagaChunk();
    	Location location = sagaPlayer.getLocation();
    	if(location == null){
    		Saga.severe("Saga player location is null. Ignoring command.", sagaPlayer.getName());
    		sagaPlayer.severe(PlayerMessages.failedToRetrieveSagaPlayerLocation());
    		return;
    	}
    	
    	// Can settle:
    	if(!selectedFaction.canSettle(sagaPlayer)){
    		sagaPlayer.sendMessage(ChunkGroupMessages.noPermission());
			return;
    	}

    	// Already claimed:
		if(locationChunk != null){
			sagaPlayer.sendMessage(ChunkGroupMessages.chunkClaimed());
			return;
		}
		
		// Settles:
		if(selectedFaction.getRemainingSettles() <= 0){
			sagaPlayer.sendMessage(ChunkGroupMessages.notEnoughFactionSettles());
			return;
		}

		// Validate name:
    	if(!FactionCommands.validateName(args.getString(0))){
    		sagaPlayer.sendMessage(FactionMessages.invalidName());
    		return;
    	}
    	
    	// Check name:
    	if( ChunkGroupManager.getChunkGroupManager().getChunkGroupWithName(args.getString(0)) != null ){
    		sagaPlayer.sendMessage(FactionMessages.nameInUse());
    		return;
    	}
    	
		// Settle:
    	Settlement settlement = new Settlement(args.getString(0));
    	settlement.addChunk(new SagaChunk(location));
    	ChunkGroup.newChunkGroup(settlement, selectedFaction);
    	
    	// Inform:
    	Saga.broadcast(ChunkGroupMessages.foundedChunkGroupBroadcast(sagaPlayer, settlement));
		
    	
	}

	@Command(
	            aliases = {"factionabandon", "fabandon","fab", "funclaim", "abandon", "ab", "unclaim"},
	            usage = "",
	            flags = "",
	            desc = "Abandon the chunk of settlement land you are currently standing on. Delete if no land is left.",
	            min = 0,
	            max = 0
	        )
	        @CommandPermissions({"saga.user.settlement.abandon"})
	public static void abandon(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

			
		// Check location:
	   	SagaChunk locationChunk = sagaPlayer.getSagaChunk();
	   	Location location = sagaPlayer.getLocation();
	   	if(location == null){
	   		Saga.severe("Saga player location is null. Ignoring command.", sagaPlayer.getName());
	   		sagaPlayer.severe(PlayerMessages.failedToRetrieveSagaPlayerLocation());
	   		return;
	   	}
//	   	Chunk bukkitChunk = location.getWorld().getChunkAt(location);
	  
	   	
	   	// Unclaimed:
	   	if(locationChunk == null){
			sagaPlayer.sendMessage(ChunkGroupMessages.chunkNotClaimed());
			return;
		}
	   	
	   	// Chunk group:
	   	ChunkGroup chunkGroup = locationChunk.getChunkGroup();
	   	if (chunkGroup == null) {
			sagaPlayer.severe( PlayerMessages.chunkGroupNotFound() );
	   		Saga.severe("Could not find saga chunk group associated with saga chunk " + locationChunk + ".", sagaPlayer.getName());
	   		return;
		}
	   	
	   	// Delete if last element:
	   	if( chunkGroup.getSize() == 1 && chunkGroup.canAbandon(sagaPlayer)){
	   		sagaPlayer.severe( ChunkGroupMessages.noPermission() );
	   		return;
	   	}
	   	
	   	// Permissions:
	   	if(!chunkGroup.canAbandon(sagaPlayer)){
	   		sagaPlayer.sendMessage(ChunkGroupMessages.noPermission());
	   		return;
	   	}
	   	
		// Remove chunk from the chunk group:
		chunkGroup.removeChunk(locationChunk);
		
		// Inform:
		locationChunk.broadcast(ChunkGroupMessages.abandonedChunkBroadcast(sagaPlayer, chunkGroup));
		
		// Delete if none left:
		if( chunkGroup.getSize() == 0 ){
			chunkGroup.delete();
			Saga.broadcast(ChunkGroupMessages.deletedChunkGroupBroadcast(sagaPlayer, chunkGroup));
		}
		
	}
	
	@Command(
            aliases = {"disolvesettlement", "disolve", "dis"},
            usage = "",
            flags = "",
            desc = "Delete the settlement.",
            min = 0,
            max = 0
        )
        @CommandPermissions({"saga.admin.settlement.delete"})
	public static void disolve(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

		
		// Check location:
		SagaChunk locationChunk = sagaPlayer.getSagaChunk();
	   	Location location = sagaPlayer.getLocation();
	   	if(location == null){
	   		Saga.severe("Saga player location is null. Ignoring command.", sagaPlayer.getName());
	   		sagaPlayer.severe(PlayerMessages.failedToRetrieveSagaPlayerLocation());
	   		return;
	   	}
	   	
	   	// Unclaimed:
	   	if(locationChunk == null){
			sagaPlayer.sendMessage(ChunkGroupMessages.chunkNotClaimed());
			return;
		}
	   	
	   	// Chunk group:
	   	ChunkGroup chunkGroup = locationChunk.getChunkGroup();
	   	if (chunkGroup == null) {
			sagaPlayer.severe( PlayerMessages.chunkGroupNotFound() );
	   		Saga.severe("Could not find saga chunk group associated with saga chunk " + locationChunk + ".", sagaPlayer.getName());
	   		return;
		}
	   	
	   	// Permissions:
	   	if(!chunkGroup.canDelete(sagaPlayer)){
	   		sagaPlayer.sendMessage(ChunkGroupMessages.noPermission());
	   		return;
	   	}

		// Delete:
		chunkGroup.delete();
			
		// Inform:
		Saga.broadcast(ChunkGroupMessages.deletedChunkGroupBroadcast(sagaPlayer, chunkGroup));
		
	
	}

	@Command(
            aliases = {"settlementinvite", "sinvite"},
            usage = "<player name>",
            flags = "",
            desc = "Invite a player to join your settlement.",
            min = 1,
            max = 1)
	@CommandPermissions({"saga.user.settlement.invite"})
	public static void invite(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


		// Chunk group selection:
		SagaChunk selectedChunk = sagaPlayer.getSagaChunk();
		ChunkGroup selectedChunkGroup = null;
		if(selectedChunk != null) selectedChunkGroup = selectedChunk.getChunkGroup();
		if(selectedChunkGroup == null){
			sagaPlayer.sendMessage( ChunkGroupMessages.notInChunkGroup() );
			return;
		}

		// Not a member:
		if( !selectedChunkGroup.isPlayerRegistered(sagaPlayer) ){
			sagaPlayer.sendMessage(ChunkGroupMessages.notChunkGroupMember(selectedChunkGroup));
			return;
		}
		
		// Permission:
		if( !selectedChunkGroup.canInvite(sagaPlayer) ){
			sagaPlayer.sendMessage(ChunkGroupMessages.noPermission());
			return;
		}
		
		// Force player:
		SagaPlayer invitedPlayer;
		try {
			invitedPlayer = Saga.plugin().forceSagaPlayer(args.getString(0));
		} catch (NonExistantSagaPlayerException e) {
			sagaPlayer.sendMessage(ChunkGroupMessages.nonExistantPlayer(args.getString(0)));
			return;
		}
		
		// Already in chunk group:
		if( invitedPlayer.hasChunkGroup(selectedChunkGroup) ){
			sagaPlayer.sendMessage(ChunkGroupMessages.alreadyInTheChunkGroup(invitedPlayer, selectedChunkGroup));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Invited yourself:
		if(invitedPlayer.equals(sagaPlayer)){
			sagaPlayer.sendMessage(ChunkGroupMessages.cantInviteYourself(sagaPlayer, selectedChunkGroup));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Invite:
		invitedPlayer.chunkGroupInvite(selectedChunkGroup);
		
		// Unforce:
		Saga.plugin().unforceSagaPlayer(args.getString(0));
		
		// Inform:
		selectedChunkGroup.broadcast( ChunkGroupMessages.invitedPlayer(invitedPlayer, selectedChunkGroup) );
		invitedPlayer.sendMessage( ChunkGroupMessages.beenInvited(invitedPlayer, selectedChunkGroup) );
		
		
	}
	
	@Command(
            aliases = {"settlementaccpet", "saccept"},
            usage = "<settlement name>",
            flags = "",
            desc = "Accept a settlement invitation.",
            min = 0,
            max = 1)
	@CommandPermissions({"saga.user.settlement.accept"})
	public static void accept(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


    	// No invites:
    	if(sagaPlayer.getChunkGroupInvites().size() == 0){
    		sagaPlayer.sendMessage(ChunkGroupMessages.noInvites());
    		return;
    	}
    	
    	// Find chunk group:
    	ChunkGroup chunkGroup = null;
    	ArrayList<Integer> invitationIds = sagaPlayer.getChunkGroupInvites();
    	// No parameters:
    	if(args.argsLength() == 0 && invitationIds.size() > 0){
    		chunkGroup = ChunkGroupManager.getChunkGroupManager().getChunkGroup(invitationIds.get(invitationIds.size() -1 ));
    	}
    	// Chunk group name parameter:
    	else if(args.argsLength() == 1){
    		for (int i = 0; i < invitationIds.size(); i++) {
    			ChunkGroup group = ChunkGroupManager.getChunkGroupManager().getChunkGroup(invitationIds.get(i));
				if( group != null && group.getName().equals(args.getString(0)) ){
					chunkGroup = group;
					break;
				}
			}
    	}
    	
    	// Chunk group doesn't exist:
    	if(chunkGroup == null && args.argsLength() == 1){
    		sagaPlayer.sendMessage(ChunkGroupMessages.nonExistantChunkGroup(args.getString(0)));
    		return;
    	}else if(chunkGroup == null){
    		sagaPlayer.sendMessage( ChunkGroupMessages.nonExistantChunkGroup() );
    		return;
    	}
    	
    	// Inform:
    	chunkGroup.broadcast(ChunkGroupMessages.playerJoined(sagaPlayer, chunkGroup));
		sagaPlayer.sendMessage(ChunkGroupMessages.joinedChunkGroup(sagaPlayer, chunkGroup));

    	// Add to chunk group:
		chunkGroup.addPlayer(sagaPlayer);
    	
    	
	}

	@Command(
            aliases = {"sdeclineall", "sdecline"},
            usage = "",
            flags = "",
            desc = "Decline all settlement join invitations.",
            min = 0,
            max = 0)
	@CommandPermissions({"saga.user.settlement.decline"})
	public static void decline(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


    	// Decline every invitation:
    	ArrayList<Integer> chunkGroupIds = sagaPlayer.getChunkGroupInvites();
    	for (int i = 0; i < chunkGroupIds.size(); i++) {
			sagaPlayer.removeChunkGroupInvite(chunkGroupIds.get(i));
		}
    	
    	// Inform:
    	sagaPlayer.sendMessage(ChunkGroupMessages.declinedInvites());
		
		
	}
	
	
	@Command(
            aliases = {"settlementquit","squit"},
            usage = "",
            flags = "",
            desc = "Quit current settlement.",
            min = 0,
            max = 0
		)
	@CommandPermissions({"saga.user.settlement.quit"})
	public static void quit(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
		

		// Chunk group selection:
		SagaChunk selectedChunk = sagaPlayer.getSagaChunk();
		ChunkGroup selectedChunkGroup = null;
		if(selectedChunk != null) selectedChunkGroup = selectedChunk.getChunkGroup();
		if(selectedChunkGroup == null){
			sagaPlayer.sendMessage( ChunkGroupMessages.notInChunkGroup() );
			return;
		}

		// Not a member:
		if( !selectedChunkGroup.isPlayerRegistered(sagaPlayer) ){
			sagaPlayer.sendMessage(ChunkGroupMessages.notChunkGroupMember(selectedChunkGroup));
			return;
		}
		
		// Quit:
		selectedChunkGroup.removePlayer(sagaPlayer);
		
		// Inform:
		selectedChunkGroup.broadcast(ChunkGroupMessages.playerQuit(sagaPlayer, selectedChunkGroup));
		sagaPlayer.sendMessage(ChunkGroupMessages.quitChunkGroup(sagaPlayer, selectedChunkGroup));
		
		
	}
	
	@Command(
            aliases = {"settlementkick","skick"},
            usage = "<player name>",
            flags = "",
            desc = "Kick a member out of the faction.",
            min = 1,
            max = 1
		)
	@CommandPermissions({"saga.user.settlement.kick"})
	public static void kick(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
		

		// Chunk group selection:
		SagaChunk selectedChunk = sagaPlayer.getSagaChunk();
		ChunkGroup selectedChunkGroup = null;
		if(selectedChunk != null) selectedChunkGroup = selectedChunk.getChunkGroup();
		if(selectedChunkGroup == null){
			sagaPlayer.sendMessage( ChunkGroupMessages.notInChunkGroup() );
			return;
		}

		// Not a member:
		if( !selectedChunkGroup.isPlayerRegistered(sagaPlayer) ){
			sagaPlayer.sendMessage(ChunkGroupMessages.notChunkGroupMember(selectedChunkGroup));
			return;
		}
		
		// Permission:
		if(!selectedChunkGroup.canKick(sagaPlayer)){
			sagaPlayer.sendMessage(ChunkGroupMessages.noPermission());
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
		if(!selectedChunkGroup.hasPlayer(kickedPlayer.getName())){
			sagaPlayer.sendMessage(ChunkGroupMessages.playerNotChunkGroupMember(kickedPlayer, selectedChunkGroup));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Kicked yourself:
		if(kickedPlayer.equals(sagaPlayer)){
			sagaPlayer.sendMessage(ChunkGroupMessages.cantKickYourself(sagaPlayer, selectedChunkGroup));
			// Unforce:
			Saga.plugin().unforceSagaPlayer(args.getString(0));
			return;
		}
		
		// Kick:
		selectedChunkGroup.removePlayer(kickedPlayer);
		
		// Unforce:
		Saga.plugin().unforceSagaPlayer(args.getString(0));
		
		// Inform:
		selectedChunkGroup.broadcast(ChunkGroupMessages.playerKicked(kickedPlayer, selectedChunkGroup));
		kickedPlayer.sendMessage(ChunkGroupMessages.kickedFromChunkGroup(kickedPlayer, selectedChunkGroup));
		
		
	}
	
	
	
	
	@Command(
            aliases = {"factionsettlementaccpet", "fsaccept"},
            usage = "<settlement name>",
            flags = "",
            desc = "Accept a settlement join invitation.",
            min = 2,
            max = 1)
	@CommandPermissions({"saga.user.faction.accept"})
	public static void factionAccept(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {


		// One faction is selected:
		ArrayList<SagaFaction> selectedFactions = sagaPlayer.getSelectedFactions();
		if(selectedFactions.size() != 1){
			sagaPlayer.sendMessage( FactionMessages.mustSelectOneFaction() );
			return;
		}
		SagaFaction selectedFaction = selectedFactions.get(0);
		
		// Permission:
		if(!selectedFaction.canJoinSettlement(sagaPlayer)){
			sagaPlayer.sendMessage(FactionMessages.noPermission(selectedFaction));
			return;
		}
		
		// Settles:
		if(selectedFaction.getRemainingSettles() <= 0){
			sagaPlayer.sendMessage(ChunkGroupMessages.notEnoughFactionSettles());
			return;
		}

    	// No invites:
    	if(sagaPlayer.getFactionInvites().size() == 0){
    		sagaPlayer.sendMessage(ChunkGroupMessages.noFactionInvites());
    		return;
    	}
    	
    	
    	
		
//    	// Inform:
//    	sagaFaction.broadcast(FactionMessages.playerJoined(sagaPlayer, sagaFaction));
//		sagaPlayer.sendMessage(FactionMessages.joinedFaction(sagaPlayer, sagaFaction));
//
//    	// Add to faction:
//    	sagaFaction.addMember(sagaPlayer);
//    	
//    	// Remove all invitations:
//    	for (int i = 0; i < invitationIds.size(); i++) {
//			sagaPlayer.removeFactionInvite(invitationIds.get(i));
//		}
    	
    	
	}
	
	
	/**
	 * Gets adjacent groups to the given chunk.
	 * 
	 * @param bukkitChunk bukkit chunk
	 * @return adjacent chunk groups. empty array if not found
	 */
	private static ArrayList<ChunkGroup> getAdjacentGroups(Chunk bukkitChunk) {

		
		// Check adjacent chunks:
	   	SagaChunk adjacentChunk;
	   	ArrayList<ChunkGroup> adjacentGroups = new ArrayList<ChunkGroup>();
	   	

	   	// Front:
	   	adjacentChunk = ChunkGroupManager.getChunkGroupManager().getAdjacentSagaChunk(ChunkSide.FRONT, bukkitChunk);
	   	if(adjacentChunk != null){
	   		ChunkGroup adjacentGroup = adjacentChunk.getChunkGroup();
	   		if(adjacentGroup != null){
	   			adjacentGroups.add(adjacentGroup);
	   		}
	   	}

	   	// Left:
	   	adjacentChunk = ChunkGroupManager.getChunkGroupManager().getAdjacentSagaChunk(ChunkSide.LEFT, bukkitChunk);
	   	if(adjacentChunk != null){
	   		ChunkGroup adjacentGroup = adjacentChunk.getChunkGroup();
	   		if(adjacentGroup != null){
	   			adjacentGroups.add(adjacentGroup);
	   		}
	   	}

		// Back:
	   	adjacentChunk = ChunkGroupManager.getChunkGroupManager().getAdjacentSagaChunk(ChunkSide.BACK, bukkitChunk);
	   	if(adjacentChunk != null){
	   		ChunkGroup adjacentGroup = adjacentChunk.getChunkGroup();
	   		if(adjacentGroup != null){
	   			adjacentGroups.add(adjacentGroup);
	   		}
	   	}
	   	
	   	// Right:
	   	adjacentChunk = ChunkGroupManager.getChunkGroupManager().getAdjacentSagaChunk(ChunkSide.RIGHT, bukkitChunk);
	   	if(adjacentChunk != null){
	   		ChunkGroup adjacentGroup = adjacentChunk.getChunkGroup();
	   		if(adjacentGroup != null){
	   			adjacentGroups.add(adjacentGroup);
	   		}
	   	}
		
	   	return adjacentGroups;
		
	}
	
	
	
	
}
