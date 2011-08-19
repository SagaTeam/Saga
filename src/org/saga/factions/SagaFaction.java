package org.saga.factions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.IOConstants;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.utility.WriterReader;
import org.saga.world.SagaChunk;

import com.google.gson.JsonParseException;

public class SagaFaction {

	
	/**
	 * Faction ID.
	 * -1 if none.
	 */
	private Integer factionId;
	
	/**
	 * Faction name.
	 */
	private String factionName;
	
	/**
	 * Faction prefix.
	 */
	private String factionPrefix;
	
	/**
	 * Faction members.
	 */
	private ArrayList<String> memberNames;
	
	/**
	 * Registered players.
	 */
	transient private ArrayList<SagaPlayer> registeredMembers = new ArrayList<SagaPlayer>();
	
	/**
	 * Faction land.
	 */
	private Hashtable<Integer, Hashtable<Integer, SagaChunk>> ownedLand;
	
	/**
	 * Primary color.
	 */
	private ChatColor primaryColor;
	
	/**
	 * Secondary color.
	 */
	private ChatColor secondaryColor;
	
	
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
		
		this.factionId = factionId;
		this.factionName = factionName;
		this.factionPrefix = factionPrefix;
				
	}
	
	/**
	 * Completes the initialization.
	 * 
	 * @return integrity
	 */
	public boolean complete() {

		
		boolean integrity=true;
		
		String faction = factionId + "(" + factionName + ")";
		if(factionName == null){
			Saga.info("Faction "+ faction +" factionName not initialized. Setting unnamed.");
			factionName= "unnamed";
			integrity = false;
		}
		if(factionPrefix == null){
			Saga.info("Faction "+ faction +" factionPrefix not initialized. Setting unnamed.");
			factionName= "unnamed";
			integrity = false;
		}
		if(factionId == null){
			Saga.info("Faction "+ faction +" factionId not initialized. Setting -1.");
			factionId = -1;
			integrity = false;
		}
		if(memberNames == null){
			Saga.info("Faction "+ faction +" memberNames not initialized. Initializing empty list.");
			memberNames = new ArrayList<String>();
			integrity = false;
		}
		if(ownedLand == null){
			Saga.info("Faction "+ faction +" ownedLand not initialized. Initializing empty list.");
			ownedLand = new Hashtable<Integer, Hashtable<Integer,SagaChunk>>();
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
		
		return integrity;
		
		
	}
	
	
	// Player:
	/**
	 * Adds a member.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void addMember(SagaPlayer sagaPlayer) {
		
		
		// Check if already in this faction:
		if(memberNames.contains(sagaPlayer.getName())){
			Saga.severe("Tried to add an already existing member to the " + factionId + "(" + factionName + ") faction. Ignoring request.", sagaPlayer.getName());
			return;
		}
		
		// Send messages:
		if(memberNames.size() > 0){
			sendFactionMessage(SagaFactionMessages.factionNewMemberJoined(primaryColor, secondaryColor, sagaPlayer.getName()));
			sagaPlayer.sendMessage(SagaFactionMessages.factionJoinedNew(primaryColor, secondaryColor, factionName));
		}
		
		// Add member:
		memberNames.add(sagaPlayer.getName());
		sagaPlayer.registerFaction(this);
		
		
	}
	
	/**
	 * Registers a member.
	 * Will not add player permanently to the faction list.
	 * Used by SagaPlayer to create a connection with the faction.
	 * Should not be used.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void registerMember(SagaPlayer sagaPlayer) {

		
		// Check if already registered
		if(registeredMembers.contains(sagaPlayer)){
			Saga.severe("Tried to register an already registered member for " + getFactionId() + "(" + getFactionName() + "). Ignoring register.", sagaPlayer.getName());
			return;
		}
		
		registeredMembers.add(sagaPlayer);
		
		
	}
	
	/**
	 * Unregisters a member.
	 * Will not add player permanently to the faction list.
	 * Used by SagaPlayer to create a connection with the faction.
	 * Should not be used.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void unregisterMember(SagaPlayer sagaPlayer) {

		boolean removed = registeredMembers.remove(sagaPlayer);
		if(!removed){
			Saga.severe("Tried to unregister a not registered member for " + getFactionId() + "(" + getFactionName() + "). Ignoring unregister.", sagaPlayer.getName());
		}
		
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
	
	/**
	 * Checks if the player can build on faction the chunk.
	 * 
	 * @param sagaChunk saga chunk
	 * @param sagaPlayer saga player
	 * @return true if can build. If an invalid chunk is given, returns false
	 */
	public boolean canBuild(SagaChunk sagaChunk, SagaPlayer sagaPlayer) {

		
		// Return if the chunk insn't owned by the faction:
		if(!sagaChunk.getFactionId().equals(factionId)){
			return false;
		}
		
		// True if player is a builder on the chunk:
		if(sagaChunk.isBuilder(sagaPlayer.getName())){
			return true;
		}
		
		// Elder checks and stuff like that goes here:
		
		
		return false;
		
		
	}
	
	
	// Messages:
	/**
	 * Sends a faction message.
	 * 
	 * @param message message
	 */
	public void sendFactionMessage(String message) {
		
	}
	
	
	// Interaction:
	/**
	 * Gets the factionName.
	 * 
	 * @return the factionName
	 */
	public String getFactionName() {
		return factionName;
	}

	/**
	 * Sets the factionName.
	 * 
	 * @param factionName the factionName to set
	 */
	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}

	/**
	 * Gets the factionPrefix.
	 * 
	 * @return the factionPrefix
	 */
	public String getFactionPrefix() {
		return factionPrefix;
	}
	

	/**
	 * Sets the factionPrefix.
	 * 
	 * @param factionPrefix the factionPrefix to set
	 */
	public void setFactionPrefix(String factionPrefix) {
		this.factionPrefix = factionPrefix;
	}
	

	/**
	 * Gets the factionId.
	 * 
	 * @return the factionId
	 */
	public Integer getFactionId() {
		return factionId;
	}

	/**
	 * Sets the factionId.
	 * 
	 * @param factionId the factionId to set
	 */
	void setFactionId(Integer factionId) {
		this.factionId = factionId;
	}

	/**
	 * Gets the members.
	 * 
	 * @return the members
	 */
	public ArrayList<String> getMembers() {
		return memberNames;
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
		
		
		return config;
		
		
	}
	
	/**
	 * Saves faction to disc.
	 * 
	 */
	public void save() {

		
		if(!isSavingEnabled){
			Saga.warning("Saving disabled for "+ factionId + " (" +factionName + ") faction. Ignoring save request." );
			return;
		}
		
		String configName = "" + factionId + " faction";
		try {
			WriterReader.writeFaction(factionId.toString(), this, WriteReadType.FACTION_NORMAL);
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

		Saga.warning("Disabling saving for "+ factionId + " (" +factionName + ") faction." );
		isSavingEnabled = false;
		// TODO Add notify for faction saving disabled.
	}

	
	
}
