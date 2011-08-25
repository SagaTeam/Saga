package org.saga.chunkGroups;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Chunk;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.chunkGroups.SagaChunk;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.exceptions.NonExistantSagaPlayerException;
import org.saga.factions.SagaFaction;
import org.saga.factions.FactionManager;
import org.saga.utility.WriterReader;

import com.google.gson.JsonParseException;

public class ChunkGroup {

	
	/**
	 * Class name. Used by gson.
	 */
	@SuppressWarnings("unused")
	private String _className;
	
	/**
	 * Group name ID.
	 * -1 if none.
	 */
	private Integer id;
	
	/**
	 * Group name.
	 */
	private String name;
	
	/**
	 * Players associated with the group.
	 */
	private ArrayList<String> players;
	
	/**
	 * Factions associated with the group.
	 */
	private ArrayList<Integer> factions;
	
	/**
	 * Group chunks.
	 */
	private ArrayList<SagaChunk> groupChunks;
	
	/**
	 * Registered players.
	 */
	transient private ArrayList<SagaPlayer> registeredPlayers = new ArrayList<SagaPlayer>();
	
	/**
	 * Registered factions.
	 */
	transient private ArrayList<SagaFaction> registeredFactions = new ArrayList<SagaFaction>();
	
	
	// Control:
	/**
	 * If true then saving is enabled.
	 */
	transient private Boolean isSavingEnabled;
	
	/**
	 * Chunk group manager.
	 */
	transient private ChunkGroupManager chunkGroupManager = null;
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	private ChunkGroup() {
	}
	
	/**
	 * Sets name and ID.
	 * 
	 * @param id ID
	 * @param name name
	 */
	public ChunkGroup(String name){
		this.name = name;
		this.id = ChunkGroupManager.getChunkGroupManager().getUnusedChunkGroupId();
		players = new ArrayList<String>();
		factions = new ArrayList<Integer>();
		groupChunks = new ArrayList<SagaChunk>();
		this._className = getClass().getName();
		isSavingEnabled = true;
	}
	
	/**
	 * Completes the initialization.
	 * 
	 * @return integrity
	 */
	public boolean complete() {

		
		boolean integrity=true;
		
		String group = id + "(" + name + ")";
		if(name == null){
			Saga.info("ChunkGroup "+ name +" name not initialized. Setting unnamed.");
			name = "unnamed";
			integrity = false;
		}
		if(id == null){
			Saga.info("ChunkGroup"+ group +" id not initialized. Setting -1.");
			id = -1;
			integrity = false;
		}
		if(players == null){
			Saga.info("ChunkGroup "+ group +" players not initialized. Initializing empty list.");
			players = new ArrayList<String>();
			integrity = false;
		}
		for (int i = 0; i < players.size(); i++) {
			if(players.get(i) == null){
				Saga.info("ChunkGroup "+ group +" players element not initialized. Removing element.");
				players.remove(i);
				i--;
				integrity = false;
			}
		}
		if(factions == null){
			Saga.info("ChunkGroup "+ group +" factions not initialized. Initializing empty list.");
			factions = new ArrayList<Integer>();
			integrity = false;
		}
		for (int i = 0; i < factions.size(); i++) {
			if(factions.get(i) == null){
				Saga.info("ChunkGroup "+ group +" factions element not initialized. Removing element.");
				factions.remove(i);
				i--;
				integrity = false;
			}
		}
		if(groupChunks == null){
			Saga.info("ChunkGroup "+ group +" groupChunks not initialized. Initializing empty list.");
			groupChunks = new ArrayList<SagaChunk>();
			integrity = false;
		}
		for (int i = 0; i < groupChunks.size(); i++) {
			SagaChunk coords= groupChunks.get(i);
			if(coords == null){
				Saga.info("ChunkGroup "+ group +" groupChunks element not initialized. Removing element.");
				groupChunks.remove(i);
				i--;
				continue;
			}
			coords.complete();
			if(!coords.getChunkGroupId().equals(getId())){
				Saga.info("ChunkGroup "+ group +" groupChunks element " + coords +" has an invalid ID. Setting chunk group ID.");
				coords.setChunkGroupId(getId());
			}
		}
		
		// Transient fields:
		registeredPlayers = new ArrayList<SagaPlayer>();
		registeredFactions = new ArrayList<SagaFaction>();
		isSavingEnabled = true;
	
		return integrity;
		
		
	}

	
	// Chunk group management:
	/**
	 * Adds a new chunk group.
	 * 
	 * @param chunkGroup chunk group.
	 */
	public static void newChunkGroup(ChunkGroup chunkGroup){

		
		// Log:
		Saga.info("Creating " + chunkGroup + " chunk group.");
		

		// Update chunk group manager:
		ChunkGroupManager.getChunkGroupManager().addChunkGroup(chunkGroup);
		
		// Do the first save:
		chunkGroup.save();
		
		// Refresh:
		ArrayList<SagaChunk> sagaChunks = chunkGroup.getGroupChunks();
		for (SagaChunk sagaChunk : sagaChunks) {
			sagaChunk.refresh();
		}
		
		
	}
	
	/**
	 * Adds a new chunk group.
	 * 
	 * @param chunkGroup chunk group.
	 * @param owner owner
	 */
	public static void newChunkGroup(ChunkGroup chunkGroup, SagaPlayer owner){
		
		
		// Add owner:
		chunkGroup.addPlayer(owner);
		
		// Forward:
		newChunkGroup(chunkGroup);
		 
		
	}
	
	/**
	 * Adds a new chunk group.
	 * 
	 * @param chunkGroup chunk group.
	 * @param owner owner
	 */
	public static void newChunkGroup(ChunkGroup chunkGroup, SagaFaction owner){
		
		
		// Add owner:
		chunkGroup.addFaction(owner);
		
		// Forward:
		newChunkGroup(chunkGroup);
		
		
	}
	
	/**
	 * Deletes a chunk group
	 * 
	 * @param groupName group name
	 */
	public void delete() {


		// Log:
		Saga.info("Deleting " + this + " chunk group.");
		
		
		// Remove all players:
		ArrayList<String> players = getPlayers();
		for (int i = 0; i < players.size(); i++) {
			removePlayer(players.get(i));
		}

		// Remove all factions:
		ArrayList<Integer> factions = getFactions();
		for (int i = 0; i < factions.size(); i++) {
			removeFaction(factions.get(i));
		}
		
		// Update chunk group manager:
		if(chunkGroupManager != null){
			chunkGroupManager.removeChunkGroup(this);
		}
		
		// Save one last time:
		save();
		
		// Remove from disc:
		WriterReader.deleteChunkGroup(getId().toString());
		
		
	}
	
	/**
	 * Adds a chunk.
	 * Needs to be done by chunk group manager, to update chunk shortcuts.
	 * 
	 * @param sagaChunk saga chunk
	 */
	public void addChunk(SagaChunk sagaChunk) {

		
		// Check if already on the list:
		if(groupChunks.contains(sagaChunk)){
			Saga.severe("Tried to add an already existing chunk " + sagaChunk + " to a chunk group. Ignoring add.");
			return;
		}
		
		// Set chunk settlement Id:
		sagaChunk.setChunkGroupId(getId());
		
		// Add:
		groupChunks.add(sagaChunk);
		
		// Update chunk group manager:
		if(chunkGroupManager != null){
			chunkGroupManager.addChunk(sagaChunk);
		}
		
		// Refresh:
		sagaChunk.refresh();
		
		
	}
	
	/**
	 * Removes a chunk.
	 * Needs to be done by chunk group manager, to update chunk shortcuts.
	 * 
	 * @param sagaChunk saga chunk
	 */
	public void removeChunk(SagaChunk sagaChunk) {

		
		// Check if not in this group:
		if(!groupChunks.contains(sagaChunk)){
			Saga.severe("Tried to remove a non-existant chunk from " + getId() + " chunk group.");
			
			System.out.println(sagaChunk+ " vs:");
			for (SagaChunk iterable_element : groupChunks) {
				System.out.println(iterable_element + " equa:"+iterable_element.equals(sagaChunk));
			}
			System.out.println("------------");
			
			return;
		}
		
		// Remove member:
		groupChunks.remove(sagaChunk);

		// Update chunk group manager:
		if(chunkGroupManager != null){
			chunkGroupManager.removeChunk(sagaChunk);
		}

		// Refresh:
		sagaChunk.refresh();
		
		
	}

	/**
	 * Checks if the ID is on the list.
	 * 
	 * @param id ID
	 * @return true if on the list
	 */
	public boolean hasFaction(Integer id){
		return factions.contains(id);
	}
	
	/**
	 * Checks if the name is on the list.
	 * 
	 * @param name name
	 * @return true if on the list
	 */
	public boolean hasPlayer(String name){
		return players.contains(name);
	}
	
	/**
	 * Checks if the given bukkit chunk is adjacent to the chunk group.
	 * 
	 * @param bukkitChunk bukkit chunk
	 * @return true if adjacent
	 */
	public boolean isAdjacent(Chunk bukkitChunk) {

		String bWorld = bukkitChunk.getWorld().getName();
		int bX = bukkitChunk.getX();
		int bZ = bukkitChunk.getZ();
		
		for (int i = 0; i < groupChunks.size(); i++) {
			SagaChunk sChunk = groupChunks.get(i);
			// World:
			if(!sChunk.getWorldName().equals(bWorld)){
				continue;
			}
			// North from saga chunk:
			if( (sChunk.getX() == bX + 1) && (sChunk.getZ() == bZ) ){
				return true;
			}
			// East from saga chunk:
			if( (sChunk.getX() == bX) && (sChunk.getZ() == bZ + 1) ){
				return true;
			}
			// South from saga chunk:
			if( (sChunk.getX() == bX - 1) && (sChunk.getZ() == bZ) ){
				return true;
			}
			// West from saga chunk:
			if( (sChunk.getX() == bX) && (sChunk.getZ() == bZ - 1) ){
				return true;
			}
		}
		return false;
		
		
	}
	
	
	// Player and faction:
	/**
	 * Adds a player.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void addPlayer(SagaPlayer sagaPlayer) {

		
		// Check if already on the list:
		if(players.contains(sagaPlayer.getName())){
			Saga.severe("Tried to add an already existing player " + sagaPlayer + " to a chunk group. Ignoring add.");
			return;
		}
		
		// Add player:
		players.add(sagaPlayer.getName());
		
		// Add chunk group:
		sagaPlayer.addChunkGroup(this);
		
		// Register:
		registerPlayer(sagaPlayer);
		
	}
	
	/**
	 * Removes a player.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void removePlayer(SagaPlayer sagaPlayer) {

		
		// Check if not in this faction:
		if(!players.contains(sagaPlayer.getName())){
			Saga.severe("Tried to remove a non-existant player from " + getId() + " chunk group.");
			return;
		}
		
		// Remove member:
		players.remove(sagaPlayer.getName());
		
		// Remove chunk group:
		sagaPlayer.removeChunkGroup(this);
		
		// Unregister:
		unregisterPlayer(sagaPlayer);
				
		
	}

	/**
	 * Removes a player.
	 * 
	 * @param playerName player name
	 */
	public void removePlayer(String playerName) {
		
		
		// Check if not in this faction:
		if(!players.contains(playerName)){
			Saga.severe("Tried to remove a non-member " + playerName + " player from " + this +  "chunk group.");
			return;
		}
		
		// Force member:
		SagaPlayer groupMember;
		try {
			groupMember = Saga.plugin().forceSagaPlayer(playerName);
		} catch (NonExistantSagaPlayerException e) {
			Saga.severe("Cant remove " + playerName + " player from " + this + "chunk group, because the player doesent exist.");
			return;
		}
		
		// Remove player
		removePlayer(groupMember);
		
		// Unforce player:
		Saga.plugin().unforceSagaPlayer(playerName);
		
		
	}
	
	/**
	 * Adds a faction.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void addFaction(SagaFaction sagaFaction) {

		
		// Check if already on the list:
		if(factions.contains(sagaFaction.getId())){
			Saga.severe("Tried to add an already existing faction " + sagaFaction + " to a chunk group. Ignoring add.");
			return;
		}
		
		// Add faction:
		factions.add(sagaFaction.getId());
		
		// Add chunk group:
		sagaFaction.addChunkGroup(this);
		
		// Register:
		registerFaction(sagaFaction);
		
		
	}
	
	/**
	 * Removes a faction.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void removeFaction(SagaFaction sagaFaction) {

		
		// Check if not in this faction:
		if(!factions.contains(sagaFaction.getId())){
			Saga.severe("Tried to remove a non-existant faction from " + getId() + " chunk group.");
			return;
		}
		
		// Remove faction:
		factions.remove(sagaFaction.getId());

		// Remove chunk group:
		sagaFaction.removeChunkGroup(this);
		
		
		// Unregister:
		unregisterFaction(sagaFaction);
		
		
	}

	/**
	 * Removes a player.
	 * 
	 * @param playerName player name
	 */
	public void removeFaction(Integer factionId) {
		
		
		// Check if not in this faction:
		if(!factions.contains(factionId)){
			Saga.severe("Tried to remove a non-member " + factionId + " faction from " + this +  "chunk group.");
			return;
		}
		
		// Force load member:
		SagaFaction groupMember = FactionManager.getFactionManager().getFaction(factionId);
		if(groupMember == null){
			Saga.severe("Cant remove " + groupMember + " faction from " + this + "chunk group, because the player doesent exist.");
			return;
		}
		
		removeFaction(groupMember);
		
		
	}
	
	
	// Permissions:
	/**
	 * Checks if the player can build.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if the player can build
	 */
	public boolean canBuild(SagaPlayer sagaPlayer) {
		return false;
	}
	
	/**
	 * Checks if the player can claim a chunk.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if the player can claim
	 */
	public boolean canClaim(SagaPlayer sagaPlayer) {
		return false;
	}
	
	/**
	 * Checks if the player can abandon a chunk.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if the player can abandon
	 */
	public boolean canAbandon(SagaPlayer sagaPlayer) {
		return false;
	}
	
	/**
	 * Checks if the player can delete the group.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if the player can delete
	 */
	public boolean canDelete(SagaPlayer sagaPlayer) {
		return false;
	}

	/**
	 * Checks if the player has permission to invite.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if can invite
	 */
	public boolean canInvite(SagaPlayer sagaPlayer) {
		return true;
	}

	/**
	 * Checks if the player has permission to kick.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if can kick
	 */
	public boolean canKick(SagaPlayer sagaPlayer) {
		return true;
	}
	
	
	// Registration:
	/**
	 * Registers a player.
	 * 
	 * @param sagaPlayer saga player
	 */
	void registerPlayer(SagaPlayer sagaPlayer) {

		
		// Add to registered list:
		if(registeredPlayers.contains(sagaPlayer)){
			Saga.severe("Tried to register an already registered member for " + this + " chunk group. Ignoring request.");
			return;
		}
		
		// Register player:
		registeredPlayers.add(sagaPlayer);
		
		// Register chunk group:
		sagaPlayer.registerChunkGroup(this);
		
		
	}
	
	/**
	 * Unregisters a player.
	 * 
	 * @param sagaPlayer saga player
	 */
	void unregisterPlayer(SagaPlayer sagaPlayer) {

		
		// Remove from registered list:
		if(!registeredPlayers.contains(sagaPlayer)){
			Saga.severe("Tried to unregister a non-registered player for " + this + " chunk group.");
			return;
		}

		// Register player:
		registeredPlayers.remove(sagaPlayer);
		
		// Register chunk group:
		sagaPlayer.unregisterChunkGroup(this);
		
		
	}
	
	/**
	 * Registers a faction.
	 * 
	 * @param sagaFaction saga faction
	 */
	void registerFaction(SagaFaction sagaFaction) {

		
		// Remove from registered list:
		if(registeredFactions.contains(sagaFaction)){
			Saga.severe("Tried to register an already registered faction for " + this + " chunk group. Ignoring request.");
			return;
		}
		
		// Register faction:
		registeredFactions.add(sagaFaction);
		
		// Register chunk group:
		sagaFaction.registerChunkGroup(this);

		
	}
	
	/**
	 * Unregisters a faction.
	 * Will not add player permanently to the faction list.
	 * Used by SagaPlayer to create a connection with the faction.
	 * Should not be used.
	 * 
	 * @param sagaFaction saga faction
	 */
	void unregisterFaction(SagaFaction sagaFaction) {


		if(!registeredFactions.contains(sagaFaction)){
			Saga.severe("Tried to unregister a non-registered faction for " + this + " chunk group.");
			return;
		}

		// Unregister faction:
		registeredFactions.remove(sagaFaction);
		
		// Unregister chunk group:
		sagaFaction.unregisterChunkGroup(this);
		
		
		
	}
	
	/**
	 * Registers chunk group manager.
	 * 
	 * @param chunkGroupManager chunk group manager
	 */
	void registerChunkGroupManager(ChunkGroupManager chunkGroupManager) {
		this.chunkGroupManager = chunkGroupManager;
	}

	/**
	 * Unregisters chunk group manager.
	 * 
	 */
	void unregisterChunkGroupManager() {
		this.chunkGroupManager = null;
	}

	/**
	 * Checks if the player is registered.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if registered
	 */
	public boolean isPlayerRegistered(SagaPlayer sagaPlayer) {
		
		boolean registered = registeredPlayers.contains(sagaPlayer);
		if(registered){
			return true;
		}
		for (int i = 0; i < registeredFactions.size(); i++) {
			if(registeredFactions.get(i).isRegisteredMember(sagaPlayer)) return true;
		}
		return false;
		
	}
	
	/**
	 * Checks if the faction is registered.
	 * 
	 * @param sagaFaction saga faction
	 * @return true if registered
	 */
	public boolean isFactionRegistered(SagaFaction sagaFaction) {
		return registeredFactions.contains(sagaFaction);
	}
	
	
	// Members:
	/**
	 * Broadcast a message to all members.
	 * 
	 * @param message message
	 */
	public void broadcast(String message){
		
		for (int i = 0; i < registeredPlayers.size(); i++) {
			registeredPlayers.get(i).sendMessage(message);
		}
		for (int i = 0; i < registeredFactions.size(); i++) {
			registeredFactions.get(i).broadcast(message);
		}
		
	}
	
	// Getters:
	/**
	 * Gets chunk group ID.
	 * 
	 * @return ID
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Sets the ID.
	 * 
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets factions associated.
	 * 
	 * @return factions
	 */
	public ArrayList<Integer> getFactions() {
		return factions;
	}
	
	/**
	 * Gets players associated.
	 * 
	 * @return players
	 */
	public ArrayList<String> getPlayers() {
		return players;
	}

	/**
	 * Gets the registered players.
	 * 
	 * @return the registered players
	 */
	public ArrayList<SagaPlayer> getRegisteredPlayers() {
		return registeredPlayers;
	}

	
	/**
	 * Gets the registered factions.
	 * 
	 * @return the registered factions
	 */
	public ArrayList<SagaFaction> getRegisteredFactions() {
		return registeredFactions;
	}

	/**
	 * Gets all chunks from the group.
	 * 
	 * @return group chunks
	 */
	public ArrayList<SagaChunk> getGroupChunks() {
		return groupChunks;
	}
	
	/**
	 * Returns settlement size in chunks.
	 * 
	 * @return settlement size
	 */
	public int getSize() {
		return groupChunks.size();
	}
	
	@Override
	public String toString() {
		return getId() + "(" + getName() + ")";
	}
	
	
	// Control:
	/**
	 * Disables saving.
	 * 
	 */
	private void disableSaving() {

		Saga.warning("Disabling saving for "+ id + " (" +name + ") faction." );
		isSavingEnabled = false;
		// TODO Add notify for settlement saving disabled.
		
	}

	
	// Load save
	/**
	 * Loads and a faction from disc.
	 * 
	 * @param chunkGroupId faction ID in String form
	 * @return saga faction
	 */
	public static ChunkGroup load(String chunkGroupId) {

		
		// Load:
		String configName = "" + chunkGroupId + " chunk group";
		ChunkGroup config;
		try {
			config = WriterReader.readChunkGroup(chunkGroupId.toString());
		} catch (FileNotFoundException e) {
			Saga.info("Missing " + configName + ". Creating a new one.");
			config = new ChunkGroup();
		} catch (IOException e) {
			Saga.severe("Failed to load " + configName + ". Loading defaults.");
			config = new ChunkGroup();
			config.disableSaving();
		} catch (JsonParseException e) {
			Saga.severe("Failed to parse " + configName + ". Loading defaults.");
			Saga.info("Parse message :" + e.getMessage());
			config = new ChunkGroup();
			config.disableSaving();
		}
		
		// Complete:
		config.complete();
		
		
		return config;
		
		
	}

	/**
	 * Saves faction to disc.
	 * 
	 */
	public void save() {

		
		String configName = "" + id + " chunk group";
		if(!isSavingEnabled){
			Saga.warning("Saving disabled for "+ configName + ". Ignoring save request." );
			return;
		}
		
		try {
			WriterReader.writeChunkGroup(id.toString(), this, WriteReadType.SETTLEMENT_NORMAL);
		} catch (IOException e) {
			Saga.severe("Failed to write "+ configName +". Ignoring write.");
		}
		
		
	}
	


}
