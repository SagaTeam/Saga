package org.saga.factions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.chunkGroups.SagaChunk;
import org.saga.chunkGroups.ChunkGroup;
import org.saga.chunkGroups.ChunkGroupManager;
import org.saga.config.BalanceConfiguration;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.exceptions.NonExistantSagaPlayerException;
import org.saga.utility.WriterReader;

import com.google.gson.JsonParseException;

public class SagaFaction {

	
	/**
	 * Faction ID.
	 * -1 if none.
	 */
	private Integer id;
	
	/**
	 * Faction name.
	 */
	private String name;
	
	/**
	 * Faction prefix.
	 */
	private String prefix;
	
	/**
	 * Faction members.
	 */
	private ArrayList<String> members;
	
	/**
	 * Registered players.
	 */
	transient private ArrayList<SagaPlayer> registeredMembers = new ArrayList<SagaPlayer>();

	/**
	 * Primary color.
	 */
	private ChatColor primaryColor;
	
	/**
	 * Secondary color.
	 */
	private ChatColor secondaryColor;
	
	/**
	 * Chunk group IDs.
	 */
	private ArrayList<Integer> chunkGroups;
	
	/**
	 * Faction registered chunk groups.
	 */
	transient private ArrayList<ChunkGroup> registeredChunkGroups = new ArrayList<ChunkGroup>();
	
	// Control:
	/**
	 * If true then saving is enabled.
	 */
	transient private boolean isSavingEnabled = true;
	
	
	// Initialization:
	/**
	 * Used by gson.
	 * 
	 */
	public SagaFaction() {
	}
	
	/**
	 * Initializes.
	 * 
	 * @param factionId faction ID
	 * @param factionName faction name
	 * @param factionPrefix faction prefix
	 */
	public SagaFaction(Integer factionId, String factionName, String factionPrefix) {
		
		this.id = factionId;
		this.name = factionName;
		this.prefix = factionPrefix;
				
	}
	
	/**
	 * Completes the initialization.
	 * 
	 * @return integrity
	 */
	public boolean complete() {

		
		boolean integrity=true;
		
		String faction = id + "(" + name + ")";
		if(name == null){
			Saga.info("Faction "+ faction +" name not initialized. Setting unnamed.");
			name= "unnamed";
			integrity = false;
		}
		if(prefix == null){
			Saga.info("Faction "+ faction +" prefix not initialized. Setting unnamed.");
			prefix= "unnamed";
			integrity = false;
		}
		if(id == null){
			Saga.info("Faction "+ faction +" id not initialized. Setting -1.");
			id = -1;
			integrity = false;
		}
		if(members == null){
			Saga.info("Faction "+ faction +" memberNames not initialized. Initializing empty list.");
			members = new ArrayList<String>();
			integrity = false;
		}
		if(primaryColor == null){
			Saga.info("Faction "+ faction +" primaryColor not initialized. Setting white.");
			primaryColor = ChatColor.WHITE;
			integrity = false;
		}
		if(secondaryColor == null){
			Saga.info("Faction "+ faction +" secondaryColor not initialized. Setting primaryColor.");
			secondaryColor = primaryColor;
			integrity = false;
		}
		if(chunkGroups == null){
			Saga.info("Faction "+ faction +" settlementIds not initialized. Setting empty list.");
			chunkGroups = new ArrayList<Integer>();
			integrity = false;
		}
		
		return integrity;
		
		
	}
	
	
	// Faction:
	/**
	 * Deletes the faction.
	 * 
	 */
	public void delete() {

		
		// Log:
		Saga.info("Deleting " + getId() + "(" + getName() + ") faction.");
		
		// Remove all members:
		ArrayList<String> playerNames = getMembers();
		for (int i = 0; i < playerNames.size(); i++) {
			removeMember(playerNames.get(i));
		}
		
		// Update faction manager:
		FactionManager.getFactionManager().removeFaction(this);
		
		// Save last time:
		save();
		
		// Remove from disc:
		WriterReader.deleteFaction(getId().toString());
		
		
	}
	
	/**
	 * Creates the faction.
	 * 
	 */
	public static void create(String factionName, String factionPrefix, SagaPlayer creator) {

		
		// Create:
		SagaFaction faction = new SagaFaction(FactionManager.getFactionManager().getUnusedFactoinId(), factionName, factionPrefix);
		faction.complete();
		
		// Log:
		Saga.info("Creating " + faction + " faction.");
		
		// Add the first member:
		faction.addMember(creator);
		
		// Update faction manager
		FactionManager.getFactionManager().addFaction(faction);
		
		// Save:
		faction.save();
		
		
	}
	
	
	// Register:
	/**
	 * Registers a member.
	 * Will not add player permanently to the faction list.
	 * Used by SagaPlayer to create a connection with the faction.
	 * Should not be used.
	 * 
	 * @param sagaPlayer saga player
	 */
	void registerMember(SagaPlayer sagaPlayer) {

		
		// Register player:
		if(registeredMembers.contains(sagaPlayer)){
			Saga.severe("Tried to register an already registered member for " + getId() + "(" + getName() + ") faction. Ignoring request.", sagaPlayer.getName());
			return;
		}
		
		registeredMembers.add(sagaPlayer);
		
		// Register faction:
		sagaPlayer.registerFaction(this);
		
		
	}
	
	/**
	 * Unregisters a member.
	 * Will not add player permanently to the faction list.
	 * Used by SagaPlayer to create a connection with the faction.
	 * Should not be used.
	 * 
	 * @param sagaPlayer saga player
	 */
	void unregisterMember(SagaPlayer sagaPlayer) {

		
		// Unregister player:
		boolean removed = registeredMembers.remove(sagaPlayer);
		if(!removed){
			Saga.severe("Tried to unregister a not registered member for " + getId() + "(" + getName() + ") faction.", sagaPlayer.getName());
		}

		// Unregister faction:
		sagaPlayer.unregisterFaction(this);
		
		
	}
	
	/**
	 * Checks if the payer is registered.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if registered
	 */
	public boolean isRegisteredMember(SagaPlayer sagaPlayer) {

		return registeredMembers.contains(sagaPlayer);
		
	}

	
	// Players:
	/**
	 * Adds a member.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void addMember(SagaPlayer sagaPlayer) {
		
		
		// Check if already in this faction:
		if(members.contains(sagaPlayer.getName())){
			Saga.severe("Tried to add an already existing member to " + id + "(" + name + ") faction. Ignoring request.", sagaPlayer.getName());
			return;
		}
		
		// Add member:
		members.add(sagaPlayer.getName());
		
		// Add faction:
		sagaPlayer.addFaction(this);
		
		// Register:
		registerMember(sagaPlayer);
		
		
	}
	
	/**
	 * Removes a member.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void removeMember(SagaPlayer sagaPlayer) {
		
		
		// Check if not in this faction:
		if(!members.contains(sagaPlayer.getName())){
			Saga.severe("Tried to remove a non-member " + sagaPlayer.getName() + " player from " + this +  "faction.");
			return;
		}
		
		// Remove member:
		members.remove(sagaPlayer.getName());

		// Remove faction:
		sagaPlayer.removeFaction(this);
		
		// Unregister member:
		unregisterMember(sagaPlayer);
		
		
	}
	
	/**
	 * Removes a member.
	 * 
	 * @param playerName player name
	 */
	public void removeMember(String playerName) {
		
		
		// Check if not in this faction:
		if(!members.contains(playerName)){
			Saga.severe("Tried to remove a non-member " + playerName + " player from " + this +  "faction.");
			return;
		}
		
		// Force member:
		SagaPlayer factionMember;
		try {
			factionMember = Saga.plugin().forceSagaPlayer(playerName);
		} catch (NonExistantSagaPlayerException e) {
			Saga.severe("Cant remove " + playerName + " player from " + this + "faction, because the player doesent exist.");
			return;
		}
		
		// Remove:
		removeMember(factionMember);
		
		// Unforce:
		Saga.plugin().unforceSagaPlayer(playerName);
		
		
	}
	
	/**
	 * Checks if the player is on the member list.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if on the list
	 */
	public boolean isMember(SagaPlayer sagaPlayer) {

		return members.contains(sagaPlayer.getName());
		
	}
	
	/**
	 * Checks if the player can build on faction the chunk.
	 * 
	 * @param sagaChunk saga chunk
	 * @param sagaPlayer saga player
	 * @return true if can build. If an invalid chunk is given, returns false
	 */
	boolean canBuild(SagaChunk sagaChunk, SagaPlayer sagaPlayer) {

		
		// True if player is a builder on the chunk:
			return true;
		
		// Elder checks and stuff like that goes here:
		
		
		
		
	}

	
	// Chunk groups:
	/**
	 * Registers a chunk group.
	 * Will not add faction permanently to the player.
	 * 
	 * @param sagaSettlement saga faction
	 */
	public void registerChunkGroup(ChunkGroup sagaSettlement) {
		
		
		// Check if already on the list:
		if(registeredChunkGroups.contains(sagaSettlement)){
			Saga.severe("Tried to register an already registered " + sagaSettlement.getId() + "(" + sagaSettlement.getName() + ") settlement" + ". Ignoring request.");
			return;
		}
		
		// Add:
		registeredChunkGroups.add(sagaSettlement);
		
		
	}
	
	/**
	 * Unregisters a chunk group.
	 * Will not remove faction permanently to the player.
	 * 
	 * @param sagaSettlement saga faction
	 */
	public void unregisterChunkGroup(ChunkGroup sagaSettlement) {
		
		
		// Check if not on the list:
		if(!registeredChunkGroups.contains(sagaSettlement)){
			Saga.severe("Tried to unregister an non-registered " + sagaSettlement.getId() + "(" + sagaSettlement.getName() + ") settlement" + ". Ignoring request.");
			return;
		}
		
		// Remove:
		registeredChunkGroups.remove(sagaSettlement);
		
		
	}

	/**
	 * Adds a chunk group to the faction.
	 * 
	 * @param chunkGroup saga chunk group
	 */
	public void addChunkGroup(ChunkGroup chunkGroup) {

		
		// Check if already on the list:
		if(chunkGroups.contains(chunkGroup.getId())){
			Saga.severe("Tried to add an already existing " + chunkGroup + " chunk group to " + this + " faction." + " Ignoring request.", getName());
			return;
		}
		
		// Add:
		chunkGroups.add(chunkGroup.getId());
		
		
	}

	/**
	 * Removes a chunk group from the faction.
	 * 
	 * @param chunkGroup saga chunk group
	 */
	public void removeChunkGroup(ChunkGroup chunkGroup) {

		
		// Check if already on the list:
		if(!chunkGroups.contains(chunkGroup.getId())){
			Saga.severe("Tried to remove an non-existing " + chunkGroup + " chunk group from " + this + " faction." + " Ignoring request.", getName());
			return;
		}
		
		// Add:
		chunkGroups.remove(chunkGroup.getId());
		
		
	}

	/**
	 * Gets the number of chunk groups.
	 * 
	 * @return chunk group count.
	 */
	public int getChunkGroupCount() {
		return chunkGroups.size();
	}
	
	/**
	 * Gets the chunk group IDs.
	 * 
	 * @return the chunk group IDs
	 */
	public ArrayList<Integer> getChunkGroupIds() {
		return chunkGroups;
	}

	/**
	 * Gets registered chunk groups.
	 * 
	 * @return the registered chunk groups
	 */
	public ArrayList<ChunkGroup> getRegisteredChunkGroups() {
		return registeredChunkGroups;
	}
	
	
	// Messages:
	/**
	 * Sends a faction message.
	 * 
	 * @param message message
	 */
	public void sendFactionMessage(String message) {
		
		for (int i = 0; i < registeredMembers.size(); i++) {
			registeredMembers.get(i).sendMessage(message);
		}
		
	}
	
	
	// Interaction:
	/**
	 * Gets the factionName.
	 * 
	 * @return the factionName
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the factionName.
	 * 
	 * @param factionName the factionName to set
	 */
	public void setName(String factionName) {
		this.name = factionName;
	}

	/**
	 * Gets the factionPrefix.
	 * 
	 * @return the factionPrefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the factionPrefix.
	 * 
	 * @param factionPrefix the factionPrefix to set
	 */
	public void setPrefix(String factionPrefix) {
		this.prefix = factionPrefix;
	}

	/**
	 * Gets the factionId.
	 * 
	 * @return the factionId
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the factionId.
	 * 
	 * @param factionId the factionId to set
	 */
	void setId(Integer factionId) {
		this.id = factionId;
	}

	/**
	 * Gets the primaryColor.
	 * 
	 * @return the primaryColor
	 */
	public ChatColor getPrimaryColor() {
		return primaryColor;
	}

	/**
	 * Gets the secondaryColor.
	 * 
	 * @return the secondaryColor
	 */
	public ChatColor getSecondaryColor() {
		return secondaryColor;
	}
	
	/**
	 * Gets the members.
	 * 
	 * @return the members
	 */
	public ArrayList<String> getMembers() {
		return members;
	}

	/**
	 * Gets all registered members.
	 * 
	 * @return registered members
	 */
	public ArrayList<SagaPlayer> getRegisteredMembers() {
		return registeredMembers;
	}
	
	
	// Permissions:
	/**
	 * Checks if the player has permission to settle.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if can settle
	 */
	public boolean canSettle(SagaPlayer sagaPlayer) {
		return true;
	}
	
	/**
	 * Checks if the player has permission to join the faction with a settlement.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if can join with a settlement
	 */
	public boolean canJoinSettlement(SagaPlayer sagaPlayer) {
		return true;
	}
	
	
	/**
	 * Checks if the player has permission to delete the faction.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if can delete
	 */
	public boolean canDelete(SagaPlayer sagaPlayer) {
		return true;
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
	
	
	// Settling:
	/**
	 * Gets faction level.
	 * Calculated from member count divided 0.8.
	 * 
	 */
	public Short getLevel() {
		
		return new Double(members.size() / 0.8).shortValue();
		
	}
	
	/**
	 * Gets owned chunks count.
	 * 
	 * @return owned chunks
	 */
	public Short getTotalSettled() {
		return (short) registeredChunkGroups.size();
	}
	
	/**
	 * Gets the settles the faction has in total.
	 * 
	 * @return settles in total.
	 */
	public Short getTotalSettles() {
		return BalanceConfiguration.getConfig().calculateFactionSettles(getLevel()).shortValue();
	}
	
	/**
	 * Gets remaining settles.
	 * 
	 * @return remaining settles
	 */
	public Short getRemainingSettles() {
		return ( new Integer(getTotalSettles() - getTotalSettled()) ).shortValue();
	}
	
	/**
	 * Sends a message to all registered players.
	 * 
	 * @param message message
	 */
	public void broadcast(String message) {

		for (int i = 0; i < registeredMembers.size(); i++) {
			registeredMembers.get(i).sendMessage(message);
		}
		
	}
	
	
	// Other:
	@Override
	public String toString() {
		return getId() + "(" + getName() + ")";
	}
	
	
	// Load save
	/**
	 * Loads and a faction from disc.
	 * 
	 * @param factionId faction ID in String form
	 * @return saga faction
	 */
	public static SagaFaction load(String factionId) {

		
		// Load:
		String configName = "" + factionId + " faction";
		SagaFaction config;
		try {
			config = WriterReader.readFaction(factionId.toString());
		} catch (FileNotFoundException e) {
			Saga.info("Missing " + configName + ". Creating a new one.");
			config = new SagaFaction();
		} catch (IOException e) {
			Saga.severe("Failed to load " + configName + ". Loading defaults.");
			config = new SagaFaction();
			config.disableSaving();
		} catch (JsonParseException e) {
			Saga.severe("Failed to parse " + configName + ". Loading defaults.");
			Saga.info("Parse message :" + e.getMessage());
			config = new SagaFaction();
			config.disableSaving();
		}
		
		// Complete:
		config.complete();
		
		// Register chunk groups:
		ChunkGroupManager.getChunkGroupManager().factionRegisterAll(config);
		
		return config;
		
		
	}
	
	/**
	 * Saves faction to disc.
	 * 
	 */
	public void save() {

		
		if(!isSavingEnabled){
			Saga.warning("Saving disabled for "+ id + " (" +name + ") faction. Ignoring save request." );
			return;
		}
		
		String configName = "" + id + " faction";
		try {
			WriterReader.writeFaction(id.toString(), this, WriteReadType.FACTION_NORMAL);
		} catch (IOException e) {
			Saga.severe("Failed to write "+ configName +". Ignoring write.");
		}
		
		
	}
	
	
	// Control:
	/**
	 * Disables saving.
	 * 
	 */
	private void disableSaving() {

		Saga.warning("Disabling saving for "+ id + " (" +name + ") faction." );
		isSavingEnabled = false;
		// TODO Add notify for faction saving disabled.
	}

	
	
}
