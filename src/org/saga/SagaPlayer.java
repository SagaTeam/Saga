package org.saga;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternInitiator;
import org.saga.professions.*;
import org.saga.professions.Profession.ProfessionType;
import org.saga.utility.WriterReader;
import org.saga.Clock.Ticker;
import org.saga.SagaPlayerListener.SagaPlayerProjectileShotEvent;
import org.saga.SagaPlayerListener.SagaPlayerProjectileShotEvent.ProjectileType;
import org.saga.abilities.Ability;
import org.saga.attributes.Attribute;
import org.saga.chunkGroups.ChunkGroup;
import org.saga.chunkGroups.ChunkGroupManager;
import org.saga.chunkGroups.ChunkGroupMessages;
import org.saga.chunkGroups.SagaChunk;
import org.saga.config.AttributeConfiguration;
import org.saga.config.BalanceConfiguration;
import org.saga.config.ProfessionConfiguration;
import org.saga.config.ProfessionConfiguration.InvalidProfessionException;
import org.saga.config.ProfessionDefinition;
import org.saga.constants.*;
import org.saga.factions.FactionMessages;
import org.saga.factions.SagaFaction;
import org.saga.factions.FactionManager;

import com.google.gson.JsonParseException;

public class SagaPlayer implements Ticker{

	
	// Wrapped:
	/**
	 * Minecraft player.
	 */
	transient private Player player;
	
	
	// Player information:
	/**
	 * Name.
	 */
	private String name;
	
	/**
	 * Stamina.
	 */
	private Double stamina;
	
	/**
	 * All professions.
	 */
	private ArrayList<Profession> professions;
	
	
	// Abilities:
	/**
	 * Ability manager.
	 */
	transient private PlayerAbilityManager abilityManager;
	
	
	// Attributes:
	/**
	 * Attributes. Key is the attribute simple class name and the value is attribute upgrade.
	 */
	private transient Hashtable<String, Short> attributes= new Hashtable<String, Short>();
	
	/**
	 * Temporary attribute raw names.
	 */
	transient private ArrayList<String> temporaryAttributeNames = new ArrayList<String>();
	
	/**
	 * Temporary attribute increase levels.
	 */
	transient private ArrayList<Short> temporaryAttributeUpgrades = new ArrayList<Short>();
	
	/**
	 * Temporary attribute increase times.
	 */
	transient private ArrayList<Integer> temporaryAttributeTimes = new ArrayList<Integer>();
	
	
	// Factions:
	/**
	 * Player factions IDs.
	 */
	private ArrayList<Integer> factionIds;
	
	/**
	 * All factions.
	 */
	transient private ArrayList<SagaFaction> registeredFactions = new ArrayList<SagaFaction>();
	
	/**
	 * Selected faction.
	 */
	transient private int selectedFaction = 0;
	
	
	// Chunk groups:
	/**
	 * Player settlements IDs.
	 */
	private ArrayList<Integer> chunkGroupIds;
	
	/**
	 * All registered chunk groups.
	 */
	transient private ArrayList<ChunkGroup> registeredChunkGroups = new ArrayList<ChunkGroup>();
	
	/**
	 * Last chunk the player was found on.
	 */
	transient private SagaChunk lastSagaChunk = null;
	
	/**
	 * Last chunk the player was found on.
	 */
	transient private Chunk lastBukkitChunk = null;
	
	
	// Invites:
	/**
	 * Invites to chunk groups.
	 */
	private ArrayList<Integer> chunkGroupInvites;
	
	/**
	 * Invites to factions.
	 */
	private ArrayList<Integer> factionInvites;
	
	
	// Control:
	/**
	 * Specifies if the player is online.
	 */
	transient private boolean isOnlinePlayer= false;
	
	/**
	 * Disables and enables player information saving.
	 */
	transient private boolean isSavingEnabled=true;
	
	/**
	 * Forced level. If above zero, the player can't be unforced.
	 */
	transient private int forcedLevel = 0;
	
	// Loading and initialization:
	/**
	 * Used by gson loader.
	 */
	public SagaPlayer() {
		
		
		
	}
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 */
	public void complete() {

		
		// Sets to offline by default.
		isOnlinePlayer = false;
		
		// Fields:
		if(name == null){
			name = PlayerDefaults.name;
			Saga.info("Setting default value for player name.", name);
		}
		if(stamina == null){
			stamina = PlayerDefaults.stamina;
			Saga.info("Setting default value for player stamina.", name);
		}
		if(factionIds == null){
			factionIds = new ArrayList<Integer>();
			Saga.info("Setting default value for factionIds.", name);
		}
		if(chunkGroupIds == null){
			chunkGroupIds = new ArrayList<Integer>();
			Saga.info("Setting default value for settlementIds.", name);
		}
		if(factionInvites == null){
			factionInvites = new ArrayList<Integer>();
			Saga.info("Setting default value for factionInvites.", name);
		}
		if(chunkGroupInvites == null){
			chunkGroupInvites = new ArrayList<Integer>();
			Saga.info("Setting default value for settlementInvites.", name);
		}
		

		// Professions:
		if(professions == null){
			Saga.info("Initializing an empty professions field.", name);
			professions = new ArrayList<Profession>();
		}
		
		for (int i = 0; i < professions.size(); i++) {
			Profession profession = professions.get(i);
			profession.setAccess(this);
			try {
				profession.complete();
			} catch (InvalidProfessionException e) {
				Saga.severe(profession.getName() + " is an invalid profession name. Disabling player information saving and removing the element.", getName());
				professions.remove(i);
				i--;
				setSavingEnabled(false);
			}
		}
		
		
		
		// Update ability manager:
		updateAbilityManager();
		
		// Add clock:
		Clock.getClock().registerEachSecondTick(this);
		
		
	}
	
	/**
	 * Updates the ability manager.
	 * 
	 */
	private void updateAbilityManager() {
		
		abilityManager = new PlayerAbilityManager(this, professions);
		
	}
	
	
	// Professions:
	/**
	 * Adds a profession.
	 * 
	 * @param professionName name
	 */
	public void addProfession(String professionName) {

		
		// Check if can be added:
		if(!canAddProfession(professionName)){
			Saga.severe("Cant add a profassion named " + professionName+ ".", getName());
			return;
		}
		
		// Create a profession.
		Profession profession = new Profession(professionName);
		profession.setAccess(this);
		try {
			profession.complete();
			
		} catch (InvalidProfessionException e) {
			Saga.severe("" + professionName+ " is an invalid profession name. Ignoring profession add.", getName());
			return;
		}
		
		// Add:
		professions.add(profession);
		
		// Update ability manager:
		updateAbilityManager();
		
		
	}

	/**
	 * Checks if the profession can be added.
	 * 
	 * @param professionName profession name
	 * @return true if the profession can be added
	 */
	public boolean canAddProfession(String professionName){

		
		// Check if already exists:
		for (Profession profession : professions) {
			if(profession.getName().equals(professionName)){
				return false;
			}
		}
		
		// Retrieve definition and check if it exists:
		ProfessionDefinition definition = ProfessionConfiguration.getConfig().getDefinition(professionName);
		if(definition == null){
			return false;
		}
		
		// Check open slots:
		for (int i = 0; i < professions.size(); i++) {
			if (professions.get(i).getProfessionType().equals(definition.getType())) {
				return false;
			}
		}
		
		return true;
		
		
	}

	/**
	 * Removes a profession.
	 * 
	 * @param professionName name
	 */
	public void removeProfession(String professionName) {

		
		// Check if can be removed:
		if(!canRemoveProfession(professionName)){
			Saga.severe("Cant remove a profassion named " + professionName+ ".", getName());
			return;
		}
		
		// Remove element:
		int oldSize = professions.size();
		for (int i = 0; i < professions.size(); i++) {
			if(professions.get(i).getName().equals(professionName)){
				// Prepare for removal:
				professions.get(i).prepareForRemoval();
				// Remove:
				professions.remove(i);
				i--;
			}
		}
		if(professions.size() == oldSize){
			Saga.severe("Tried to remove a non existing profession named " + professionName+ ".", getName());
		}
		
		// Update ability manager:
		updateAbilityManager();
		
		
	}
	
	/**
	 * Checks if the profession can be removed.
	 * 
	 * @param professionName profession name
	 * @return true if can be removed.
	 */
	public boolean canRemoveProfession(String professionName) {
	
		
		// Check if exists:
		for (Profession profession : professions) {
			if(profession.getName().equals(professionName)){
				return true;
			}
		}
		return false;
		
		
	}
	
	/**
	 * Checks if the saga player has a profession with the given name.
	 * 
	 * @param professionName name
	 * @return true if the player has the profession
	 */
	public boolean hasProfession(String professionName) {

		
		for (int i = 0; i < professions.size(); i++) {
			if(professions.get(i).getName().equals(professionName)){
				return true;
			}
		}
		return false;
		
		
	}

	/**
	 * Gets all professions of given type.
	 * 
	 * @param type profession type
	 * @return professions. empty list if not found
	 */
	public ArrayList<Profession> getProfessions(ProfessionType type){
		

		ArrayList<Profession> filteredProfessions = new ArrayList<Profession>();
		for (Profession profession : professions) {
			if(profession.getProfessionType().equals(type)){
				filteredProfessions.add(profession);
			}
		}
		return filteredProfessions;
		
		
	}
	
	/**
	 * Gets a profession property for the given type.
	 * The value for the last profession that has the property is returned.
	 * 
	 * @param type profession type
	 * @param key property key
	 * @return property null it not found
	 */
	public String getProfessionProperty(ProfessionType type, String key) {


		ArrayList<Profession> professions = getProfessions(type);
		String value = null;
		for (Profession profession : professions) {
			String lValue = profession.getProperty(key);
			if(lValue != null) value = lValue;
		}
		return value;
		
	}
	
	/**
	 * Returns players professions.
	 * 
	 * @return players professions
	 */
	public ArrayList<Profession> getProfessions() {
		return professions;
	}
	
	/**
	 * Gets player level.
	 * Calculated as a sum of all professions divided by 4.
	 * 
	 */
	public int getLevel() {
		
		int sum = 0;
		for (int i = 0; i < professions.size(); i++) {
			sum += professions.get(i).getLevel();
		}
		return new Double(sum / 4).intValue();
		
	}
	
	
	// Player entity management:
	/**
	 * Returns player name.
	 * 
	 * @return player name
	 */
	public String getName() {
            return name;
	}
	
	/**
	 * Sets player name. Used when loading.
	 * 
	 * @param name player name
	 */
	private void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the player and changes status to online.
	 * Marks the saga player a existant player.
	 * 
	 * @param player player
	 */
	public void setPlayer(Player player) {
            this.player = player;
            isOnlinePlayer = true;
	}
	
	/**
	 * Sets the player and changes status to offline.
	 * 
	 */
	public void removePlayer() {
		this.player = null;
		isOnlinePlayer = false;
	}
	
	
	// Health:
	/**
	 * Returns player health.
	 * 
	 * @return player health, -1 if offline
	 */
	public Integer getHealth() {
		
		if(isOnlinePlayer){
			return player.getHealth();
		}else{
			return -1;
		}
		
	}

	/**
	 * Regenerates health. Doesen't send a message.
	 * 
	 * @param amount amount to regenerate
	 */
	public void gainHealth(int amount) {
		
		
		if(!isOnline()){
			return;
		}
		int health = player.getHealth();
		
		if(health >= PlayerDefaults.maximumHealth){
			return;
		}
		if(health +amount >  PlayerDefaults.maximumHealth){
			amount = PlayerDefaults.maximumHealth - health;
		}
		sendMessage(PlayerMessages.healthGain(amount));

		
	}
	
	/**
	 * Regenerates health. Sends a message.
	 * 
	 * @param amount amount to regenerate
	 */
	public void regenerateHealth(int amount) {
		
		
		if(!isOnline()){
			return;
		}
		int health = player.getHealth();
		
		if(health >= PlayerDefaults.maximumHealth){
			return;
		}
		health += amount;
		if(health >  PlayerDefaults.maximumHealth){
			health =  PlayerDefaults.maximumHealth;
		}

		
	}
	
	
	// Stamina:
	/**
	 * Gets stamina.
	 * 
	 * @return stamina
	 */
	public Double getStamina() {
		return stamina;
	}
	
	/**
	 * Gets players maximum stamina. Including bonuses.
	 * 
	 * @return player maximum stamina
	 */
	public double getMaximumStamina() {
		
		return BalanceConfiguration.getConfig().maximumStamina+0;

	}
	
	/**
	 * Check if there is enough stamina to drain.
	 * 
	 * @param drainAmount drain amount
	 * @return true if there is enough stamina.
	 */
	public boolean enoughStamina(double drainAmount) {

		return stamina >= drainAmount;
		
	}
	
	/**
	 * Uses stamina and sends a message. Can go below zero.
	 * 
	 * @param useAmount use amount
	 */
	public void useStamina(Double useAmount) {

		stamina -= useAmount;
		sendMessage(PlayerMessages.staminaUsed(useAmount, getStamina(), getMaximumStamina()));
		
	}
	
	/**
	 * Regenerates stamina.
	 */
	private void naturalStaminaRegenerate() {
		
		
		if(stamina >= getMaximumStamina()){
			return;
		}
		stamina += BalanceConfiguration.getConfig().staminaPerSecond;
		if(stamina > getMaximumStamina()){
			stamina = getMaximumStamina();
		}
		if(((int)(stamina - BalanceConfiguration.getConfig().staminaPerSecond)/10) != (int)(stamina/10)){
			sendMessage(PlayerMessages.staminaRegeneration(stamina, getMaximumStamina()));
		}

	}

	
	// Abilities:
	/**
	 * Deactivates an ability if possible.
	 * 
	 * @param ability ability index
	 */
	public void deactivateAbility(Ability ability){
		
		
		// Forward to ability manager:
		abilityManager.deactivateAbility(ability);
		
		
	}
	
	/**
	 * Gets the time remaining for the ability.
	 * 
	 * @param ability ability
	 * @return time remaining. -1 if not found
	 */
	public Short getAbilityRemainingTime(Ability ability) {
		
		
		// Forward to ability manager:
		return abilityManager.getAbilityRemainingTime(ability);

		
	}
	
	
	// Attributes:
	/**
	 * Modifies an attribute. Should be only used for permanent modifications by professions.
	 * 
	 * @param attributeRawName attribute raw name
	 * @param amount amount to modify
	 */
	public void modifyAttributes(String attributeRawName, Short amount){
		
		
		Short oldValue = attributes.get(attributeRawName);
		if(oldValue == null){
			oldValue = 0;
		}
		attributes.put(attributeRawName, new Integer(oldValue+amount).shortValue());
		
		
	}
	
	/**
	 * Temporary modifies an attribute.
	 * 
	 * @param attributeRawName attribute raw name
	 * @param amount amount to modify
	 * @param time time to remain active
	 */
	public void modifyTemporaryAttribute(String attributeRawName, Short amount, Integer time) {

		
		// Modify the list:
		modifyAttributes(attributeRawName, amount);
		
		// Add to temporary lists:
		temporaryAttributeNames.add(attributeRawName);
		temporaryAttributeUpgrades.add(amount);
		temporaryAttributeTimes.add(time);
		
		
		// Send message:
		if(amount > 0){
			sendMessage(PlayerMessages.attributeIncreasedTo(attributeRawName, amount));
		}
		else if(amount < 0){
			sendMessage(PlayerMessages.attributeDecreasedTo(attributeRawName, amount));
		}
		
		
	}
	
	/**
	 * Gets current upgrade level for the attribute.
	 * Includes temporary upgrade.
	 * 
	 * @param attributeName attribute name
	 * @return attribute upgrade, 0 if none
	 */
	public Short getAttributeUpgrade(String attributeName) {

		
		Short upgrade = attributes.get(attributeName);
		if(upgrade == null){
			upgrade = 0;
		}
		return upgrade;
		
		
	}
	
	/**
	 * Gets the temporary upgrade level for the attribute.
	 * 
	 * @param attributeName attribute name
	 * @return attribute level, 0 if not found
	 */
	public Short getAttributeTemporaryUpgrade(String attributeName) {
		
		
		Short upgradeSize = 0;
		for (int i = 0; i < temporaryAttributeNames.size(); i++) {
			if(temporaryAttributeNames.get(i).equals(attributeName)){
				upgradeSize = (short) ( upgradeSize + temporaryAttributeUpgrades.get(i));
			}
		}
		return upgradeSize;

		
	}
	
	
	// Factions:
	/**
	 * Registers a faction.
	 * Will not add faction permanently to the player.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void registerFaction(SagaFaction sagaFaction) {
		
		
		// Check if already on the list:
		if(registeredFactions.contains(sagaFaction)){
			Saga.severe("Tried to register an already registered " + sagaFaction.getId() + "(" + sagaFaction.getName() + ") faction" + ". Ignoring request.", getName());
			return;
		}
		
		// Add:
		registeredFactions.add(sagaFaction);
		
		
	}
	
	/**
	 * Unregisters a faction.
	 * Will not remove faction permanently to the player.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void unregisterFaction(SagaFaction sagaFaction) {
		
		
		// Check if not on the list:
		if(!registeredFactions.contains(sagaFaction)){
			Saga.severe("Tried to unregister an non-registered " + sagaFaction.getId() + "(" + sagaFaction.getName() + ") faction" + ". Ignoring request.", getName());
			return;
		}
		
		// Remove:
		registeredFactions.remove(sagaFaction);
		
		
	}
	
	/**
	 * Check if a faction is registered.
	 * 
	 * @param sagaFaction saga faction
	 * @return true if registered
	 */
	public boolean isFactionRegistered(SagaFaction sagaFaction) {

		return registeredFactions.contains(sagaFaction);
		
	}
	
	/**
	 * Gets selected factions.
	 * 
	 * @return selected factions. Empty if none is selected
	 */
	public ArrayList<SagaFaction> getSelectedFactions() {
		
		
		// No factions or none selected
		if(registeredFactions.size() == 0 || selectedFaction == -1){
			return new ArrayList<SagaFaction>();
		}
		
		// Correct selection:
		if(selectedFaction > registeredFactions.size()){
			selectedFaction = -1;
		}
		if(selectedFaction < -1){
			selectedFaction = -1;
		}
		
		// All selected:
		if(selectedFaction == registeredFactions.size()){
			ArrayList<SagaFaction> factions = new ArrayList<SagaFaction>();
			for (SagaFaction faction : registeredFactions) {
				factions.add(faction);
			}
		}
		
		// One selected:
		ArrayList<SagaFaction> factions = new ArrayList<SagaFaction>();
		factions.add(registeredFactions.get(selectedFaction));
		return factions;
		
		
	}

	/**
	 * Gets all factions.
	 * 
	 * @return all factions. empty if none
	 */
	public ArrayList<Integer> getFactionIds() {
		return factionIds;
	}
	
	/**
	 * Gets all registered factions.
	 * 
	 * @return registered factions
	 */
	public ArrayList<SagaFaction> getRegisteredFactions() {
		return registeredFactions;
	}
	
	/**
	 * Returns factions count.
	 * 
	 * @return factions count.
	 */
	public int getFactionCount() {

		return registeredFactions.size();
		
	}

	/**
	 * Adds a faction to the player.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void addFaction(SagaFaction sagaFaction) {

		
		// Check if already on the list:
		if(factionIds.contains(sagaFaction.getId())){
			Saga.severe("Tried to add an already existing " + sagaFaction.getId() + "(" + sagaFaction.getName() + ") faction to saga player." + " Ignoring request.", getName());
			return;
		}
		
		// Add:
		factionIds.add(sagaFaction.getId());
		
		
	}

	/**
	 * Removes a faction from the player.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void removeFaction(SagaFaction sagaFaction) {

		
		// Check if already on the list:
		if(!factionIds.contains(sagaFaction.getId())){
			Saga.severe("Tried to remove an non-existing " + sagaFaction.getId() + "(" + sagaFaction.getName() + ") faction from saga player." + " Ignoring request.", getName());
			return;
		}
		
		// Add:
		factionIds.remove(sagaFaction.getId());
		
		
	}

	/**
	 * Checks if the player is part of a faction.
	 * 
	 * @param sagaFaction saga faction
	 * @return true if part of a faction
	 */
	public boolean hasFaction(SagaFaction sagaFaction) {
		
		for (int i = 0; i < factionIds.size(); i++) {
			if(sagaFaction.getId().equals(factionIds.get(i))) return true;
		}
		return false;
		
	}

	/**
	 * Checks if shares a faction with a player.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if is a part of the same faction
	 */
	public boolean sharesFaction(SagaPlayer sagaPlayer) {

		if(sagaPlayer.equals(this)){
			return true;
		}
		for (int i = 0; i < registeredFactions.size(); i++) {
			if(registeredFactions.get(i).isMember(sagaPlayer)) return true;
		}
		return false;
		
	}
	
	// Chunk groups:
	/**
	 * Registers a chunk group.
	 * Will not add faction permanently to the player.
	 * 
	 * @param chunkGroup saga chunk group
	 */
	public void registerChunkGroup(ChunkGroup chunkGroup) {
		
		
		// Check if already on the list:
		if(registeredChunkGroups.contains(chunkGroup)){
			Saga.severe("Tried to register an already registered " + chunkGroup.getId() + "(" + chunkGroup.getName() + ") settlement" + ". Ignoring request.", getName());
			return;
		}
		
		// Add:
		registeredChunkGroups.add(chunkGroup);
		
		
	}
	
	/**
	 * Unregisters a chunk group.
	 * Will not remove faction permanently to the player.
	 * 
	 * @param chunkGroup saga chunk group
	 */
	public void unregisterChunkGroup(ChunkGroup chunkGroup) {
		
		
		// Check if not on the list:
		if(!registeredChunkGroups.contains(chunkGroup)){
			Saga.severe("Tried to unregister an non-registered " + chunkGroup.getId() + "(" + chunkGroup.getName() + ") settlement" + ". Ignoring request.", getName());
			return;
		}
		
		// Remove:
		registeredChunkGroups.remove(chunkGroup);
		
		
	}
	
	/**
	 * Check if a chunk group is registered.
	 * 
	 * @param chunkGroup saga chunk group
	 * @return true if registered
	 */
	public boolean isChunkGroupRegistered(ChunkGroup chunkGroup) {

		return registeredChunkGroups.contains(chunkGroup);
		
	}
	
	
	/**
	 * Adds a chunk group to the player.
	 * 
	 * @param chunkGroup saga chunk group
	 */
	public void addChunkGroup(ChunkGroup chunkGroup) {

		
		// Check if already on the list:
		if(chunkGroupIds.contains(chunkGroup.getId())){
			Saga.severe("Tried to add an already existing " + chunkGroup + " chunk group to saga player." + " Ignoring request.", getName());
			return;
		}
		
		// Add:
		chunkGroupIds.add(chunkGroup.getId());
		
		
	}

	/**
	 * Removes a chunk group from the player.
	 * 
	 * @param chunkGroup saga chunk group
	 */
	public void removeChunkGroup(ChunkGroup chunkGroup) {

		
		// Check if already on the list:
		if(!chunkGroupIds.contains(chunkGroup.getId())){
			Saga.severe("Tried to remove an non-existing " + chunkGroup + " chunk group from saga player." + " Ignoring request.", getName());
			return;
		}
		
		// Add:
		chunkGroupIds.remove(chunkGroup.getId());
		
		
	}

	/**
	 * Chunk group count.
	 * 
	 * @return chunk group count
	 */
	public int getChunkGroupCount() {
		return chunkGroupIds.size();
	}
	
	/**
	 * Gets the chunk group IDs.
	 * 
	 * @return the chunk group IDs
	 */
	public ArrayList<Integer> getChunkGroupIds() {
		return chunkGroupIds;
	}

	/**
	 * Gets the registeredSettlements.
	 * 
	 * @return the registeredSettlements
	 */
	public ArrayList<ChunkGroup> getRegisteredChunkGroups() {
		return registeredChunkGroups;
	}


	/**
	 * Checks if the player is part of a chunk group.
	 * 
	 * @param chunkGroup chunk group
	 * @return true if part of a chunk group
	 */
	public boolean hasChunkGroup(ChunkGroup chunkGroup) {
		
		for (int i = 0; i < chunkGroupIds.size(); i++) {
			if(chunkGroup.getId().equals(chunkGroupIds.get(i))) return true;
		}
		return false;
		
	}

	
	// Invitations:
	/**
	 * Adds a chunk group invite.
	 * 
	 * @param factionId faction ID
	 */
	public void addFactionInvite(Integer factionId) {
		

		// Ignore invite if already exists:
		if(factionInvites.contains(factionId)){
			return;
		}
		
		// Add invite:
		factionInvites.add(factionId);
		
		
	}
	
	/**
	 * Removes a faction invite.
	 * 
	 * @param factionId faction ID
	 */
	public void removeFactionInvite(Integer factionId) {
		

		// Ignore invite if doesn't exists:
		if(!factionInvites.contains(factionId)){
			return;
		}

		// Remove invite:
		factionInvites.remove(factionId);
		
		
	}
	
	/**
	 * Adds a chunk group invite.
	 * 
	 * @param groupId chunk group ID
	 */
	public void addChunkGroupInvite(Integer groupId) {
		
		
		// Ignore invite if already exists:
		if(chunkGroupInvites.contains(groupId)){
			return;
		}
		
		// Add invite:
		chunkGroupInvites.add(groupId);
		
		
	}
	
	/**
	 * Removes a chunk group invite.
	 * 
	 * @param chunkGroupId chunk group ID
	 */
	public void removeChunkGroupInvite(Integer chunkGroupId) {
		
		
		// Ignore invite if doesn't exists:
		if(!chunkGroupInvites.contains(chunkGroupId)){
			return;
		}
		
		// Remove invite:
		chunkGroupInvites.remove(chunkGroupId);
		
		
	}
	
	/**
	 * Gets faction invites.
	 * 
	 * @return faction invites
	 */
	public ArrayList<Integer> getFactionInvites() {
		return factionInvites;
	}
	
	/**
	 * Gets chunk group invites.
	 * 
	 * @return chunk group invites
	 */
	public ArrayList<Integer> getChunkGroupInvites() {
		return chunkGroupInvites;
	}
	
	/**
	 * Invites the player to the given faction.
	 * 
	 * @param faction faction
	 */
	public void factionInvite(SagaFaction faction) {
		
		// Add invite:
		addFactionInvite(faction.getId());
		
	}

	/**
	 * Invites the player to the given chunk group.
	 * 
	 * @param chunkGroup chunk group
	 */
	public void chunkGroupInvite(ChunkGroup chunkGroup) {
		
		// Add invite:
		addChunkGroupInvite(chunkGroup.getId());
		
	}
	
	
	// Player notification:
	/**
	 * Sends the player a message.
	 * 
	 * @param message message
	 */
	public void sendMessage(String message) {
		
            if(isOnline()){
            	PlayerMessages.sendMultipleLines(message, player);
            }

	}

	/**
	 * Plays an effect for the player.
	 * 
	 * @param effect effect
	 * @param value effect value
	 */
	public void playEffect(Effect effect, int value) {

		
		// Ignore if the player isn't online:
		if(!isOnline()){
			return;
		}
		
		player.playEffect(player.getEyeLocation(), effect, value);
		
		
	}

	
	// Teleport and patterns:
	/**
	 * Moves the player to the given location.
	 * Must be used when the teleport is part of an ability.
	 * 
	 * @param location location
	 */
	public void moveTo(Location location) {

		if(isOnline()){
        	player.teleport(location);
        }
		
	}
	
	/**
	 * Centers the location to the block and moves the player there.
	 * 
	 * @param location location
	 */
	public void moveToCentered(Location location) {
		
		moveTo(location.add(0.5, 0, 0.5));
	}
	
	/**
	 * Puts a player on the given blocks center.
	 * 
	 * @param locationBlock block the player will be placed on
	 */
	public void moveToBlockCentered(Block locationBlock) {
		moveToCentered(locationBlock.getRelative(BlockFace.UP).getLocation());
	}
	
	/**
	 * Initiates a pattern. Location is set to player eye location.
	 * 
	 * @param patternElement pattern element
	 * @param patternLevel pattern level
	 * @param orthogonalFlip if true, then the pattern will have a flip orthogonal to where the player is facing
	 * @param blockLimit block limit to not let things out of control
	 * @return true if there is a termination
	 */
	public boolean initiatePattern(SagaPatternElement patternElement, Short patternLevel, boolean orthogonalFlip, int blockLimit) {
		

		// Initiate only of the player is online:
		if(isOnlinePlayer){
			SagaPatternInitiator initiator = new SagaPatternInitiator(blockLimit, patternElement);
			return initiator.initiateForPlayer(player.getEyeLocation(), calculatePlayerHorizontalDirection(), patternLevel, orthogonalFlip);
		}
		return true;
		
		
	}
	
	/**
	 * Initiates a pattern from a target block.
	 * 
	 * @param location target
	 * @param patternElement pattern element
	 * @param patternLevel pattern level
	 * @param orthogonalFlip if true, then the pattern will have a flip orthogonal to where the player is facing
	 * @param blockLimit block limit to not let things out of control
	 * @return true if there is a termination
	 * 
	 */
	public boolean initiatePatternTarget(Location location, SagaPatternElement patternElement, Short patternLevel, boolean orthogonalFlip, int blockLimit) {
		

		// Initiate only of the player is online:
		if(isOnlinePlayer){
			SagaPatternInitiator initiator = new SagaPatternInitiator(blockLimit, patternElement);
			return initiator.initiateForPlayer(location, 0*calculatePlayerHorizontalDirection(), patternLevel, orthogonalFlip);
		}
		return true;
		
		
	}
	
	/**
	 * Calculates the player horizontal facing direction.
	 * 
	 * @return facing direction. 0 if not online
	 */
	public int calculatePlayerHorizontalDirection(){
		
		
		if(!isOnline()){
			return 0;
		}
		
		Location playerLocation = player.getEyeLocation();
		double yaw = playerLocation.getYaw();
		if( (yaw >= 315.0 && yaw <= 45.0) || (yaw >= -45.0 && yaw <= -315.0) ){
			return 0;
		}
		if( (yaw >= 45.0 && yaw <= 135.0) || (yaw >= -315.0 && yaw <= -225.0) ){
			return 1;
		}
		if( (yaw >= 135.0 && yaw <= 225.0) || (yaw >= -225.0 && yaw <= -135.0) ){
			return 2;
		}if( (yaw >= 225.0 && yaw <= 315.0) || (yaw >= -135.0 && yaw <= -45.0) ){
			return 3;
		}
		return 0;

		
	}
	
	/**
	 * Calculates player vertical facing direction.
	 * 
	 * @return true if facing up or not online
	 */
	public boolean calculatePlayerVerticalDirection() {
		
		
		if(!isOnline()){
			return true;
		}
		
		float pitch = player.getLocation().getPitch();
		if(pitch>0){
			return false;
		}else{
			return true;
		}
		
		
	}
	
	
	// Projectile ability and attribute use:
	/**
	 * Shoots a fireball.
	 * 
	 * @param accuracy accuracy. Can be in the range 0-10
	 */
	public void shootFireball(Double accuracy) {

		
		// Ignore if the player isn't online:
		if(!isOnline()){
			return;
		}
		
		// Limit the accuracy to 0-10:
		if(accuracy < 0.0){
			accuracy = 0.0;
		}
		if(accuracy > 10.0){
			accuracy = 10.0;
		}
		
		EntityLiving shooter = ((CraftPlayer)player).getHandle();
		WorldServer serverWorld = ((CraftWorld) player.getWorld()).getHandle();
		Location shootLocation = player.getEyeLocation();

		double startDistance = 2;
		Vector aimVector = shootLocation.getDirection();
		
		shootLocation.add(aimVector.getX() * startDistance, aimVector.getY() * startDistance, aimVector.getZ() * startDistance);
		EntityFireball fireball = new EntityFireball(serverWorld, shooter, aimVector.getX() * accuracy, aimVector.getY() * accuracy, aimVector.getZ() * accuracy);
		fireball.getBukkitEntity().teleport(shootLocation);
		serverWorld.addEntity(fireball);
		
		// Send the event:
		SagaPlayerProjectileShotEvent event = new SagaPlayerProjectileShotEvent(this, ProjectileType.FIREBALL, 0);
		Saga.playerListener().onSagaPlayerProjectileShot(event);
		
		// Cancel the event if needed:
		if(event.isCancelled()){
			fireball.getBukkitEntity().remove();
			return;
		}
		
		// Limit the speed to 0-4:
		double speed = event.getSpeed();
		if(speed < 0){
			speed = 0;
		}
		if(speed > 4){
			speed = 4;
		}
		
		// Set speed:
		fireball.getBukkitEntity().setVelocity(aimVector.multiply(speed));
		
		
	}
	
	/**
	 * Shoots lightning at the target.
	 * 
	 * @param targetLocation target
	 * @param selfDamage if true, the shooter may also be damaged
	 */
	public void shootLightning(Location targetLocation, boolean selfDamage) {

		
		// Ignore if the player isn't online:
		if(!isOnline()){
			return;
		}
		Entity damager = player;
		
		// Ground location:
		targetLocation = BlockConstants.groundLocation(targetLocation);
		
		// Get the craftworld.
		CraftWorld craftWorld = (CraftWorld) player.getWorld();
		
		// Strike lightning effect and get the resulting entity:
		Entity craftLightning = craftWorld.strikeLightningEffect(targetLocation);
		
		// Send damage events:
		java.util.List<Entity> damagedEntities = craftLightning.getNearbyEntities(1, 5, 1);
		ArrayList<EntityDamageByEntityEvent> damageEvents = new ArrayList<EntityDamageByEntityEvent>();
		for (int i = 0; i < damagedEntities.size(); i++) {
			EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagedEntities.get(i), DamageCause.LIGHTNING, BalanceConfiguration.getConfig().baseLightningDamage);
			// Don't add self if damage self is false:
			if(selfDamage || !damagedEntities.get(i).equals(damager)){
				Bukkit.getServer().getPluginManager().callEvent(event);
			damageEvents.add(event);
			}
		}
		
		// Damage the puny eldar if the event is not canceled:
		for (EntityDamageByEntityEvent entityDamageByEntityEvent : damageEvents) {
			if(!entityDamageByEntityEvent.isCancelled()){
				Entity damaged = entityDamageByEntityEvent.getEntity();
				if(damaged instanceof LivingEntity){
					((LivingEntity) damaged).damage(entityDamageByEntityEvent.getDamage());
				}
			}
		}
		
		
	}
	

	/**
	 * Shoots lightning relative to the player.
	 * 
	 * @param relativeLocation location relative to the player
	 * @param selfDamage if true, the shooter may also be damaged
	 */
	public void shootLightning(Vector relativeLocation, boolean selfDamage) {

		
		// Ignore if the player isn't online:
		if(!isOnline()){
			return;
		}
		Entity damager = player;
		
		Location target = damager.getLocation().clone().add(relativeLocation.getX(), 0, relativeLocation.getZ());
		
		shootLightning(target, selfDamage);
		
		
	}
	
	
	// Player entity interaction:
	/**
	 * Returns players yaw.
	 * 
	 * @return yaw. 0 if not online
	 */
	public double getYaw() {
		
		
		// Ignore if not online:
		if(!isOnline()){
			return 0.0;
		}
		return player.getLocation().getYaw();
		
		
	}
	
	/**
	 * Returns players pitch.
	 * 
	 * @return pitch. 0 if not online
	 */
	public double getPitch() {
		
		
		// Ignore if not online:
		if(!isOnline()){
			return 0.0;
		}
		return player.getLocation().getPitch();
		
		
	}
	
	/**
	 * Changes pitch and yaw.
	 * 
	 * @param yaw yaw 
	 * @param pitch pitch
	 */
	public void setLookingAt(float yaw, float pitch) {
		
		
		// Ignore if not online:
		if(!isOnline()){
			return;
		}
		Entity entity = player;
		
		// Correct yaw:
		if(yaw > 360){
			while(yaw > 360){
				yaw -= 360;
			}
		}else if(yaw < 360){
			while(yaw < 360){
				yaw += 360;
			}
		}
		
		// Correct pitch:
		if(pitch > 360){
			while(pitch > 360){
				pitch -= 360;
			}
		}else if(pitch < 360){
			while(pitch < 360){
				pitch += 360;
			}
		}
		
		// Change direction:
		Location location = entity.getLocation();
		location.setPitch(pitch);
		location.setYaw(yaw);
		player.teleport(location);
		
		
	}
	
	/**
	 * Sets player horizontal speed. Direction is where the player is looking at.
	 * 
	 * @param speed speed
	 */
	public void addLeapHorizontalSpeed(double speed) {
		
		
		// Ignore if not online:
		if(!isOnline()){
			return;
		}
		
		Vector direction = player.getLocation().getDirection();
		direction.setY(0);
		direction.normalize();
		direction.multiply(speed);
		
		Vector velocity = player.getVelocity();
		velocity.add(direction);
		player.setVelocity(velocity);
		
		
	}
	
	/**
	 * Sets player vertical speed.
	 * 
	 * @param speed speed
	 */
	public void addLeapVerticalSpeed(double speed) {
		
		
		// Ignore if not online:
		if(!isOnline()){
			return;
		}
		
		Vector velocity = player.getVelocity();
		velocity.add(new Vector(0, speed, 0));
		player.setVelocity(velocity);
		
		
	}
	
	/**
	 * Returns the player distance to a location
	 * 
	 * @param location location
	 * @return distance. 0 if player not online
	 */
	public Double getDistance(Location location) {
		
		
		// Zero loaction if the player isn't online:
		if(!isOnline()){
			return 0.0;
		}
		
		return player.getLocation().distance(location);

		
	}
	
	/**
	 * Gets nearby entities
	 * 
	 * @param x x radius
	 * @param y y radius
	 * @param z z radius
	 * @return nearby entities. emplty if player isn't online.
	 */
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		
		
		// Ignore if the player isn't online:
		if(!isOnline()){
			return new ArrayList<Entity>();
		}
		
		return player.getNearbyEntities(x, y, z);

		
	}
	
	/**
	 * Tries to dodge.
	 * 
	 * @return true if dodge was a success.
	 */
	public boolean tryDodge() {

		// Works, but is annoying.
		if(!isOnline()){
			return false;
		}
		
		int direction = calculatePlayerHorizontalDirection();
		Random random= new Random();
		System.out.println(player.getVelocity());
		int firstCord;
		int oppositeCord;
		Block playerBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		float yaw = player.getLocation().getYaw();
		float pitch = player.getLocation().getPitch();
		
		if(random.nextBoolean()){
			firstCord = 1;
			oppositeCord = -1;
		}else{
			firstCord = -1;
			oppositeCord = 1;	
		}
		
		if(direction == 0 || direction == 2){
			
			// First attempt:
			if( player.getNearbyEntities(firstCord, 0, 0).size() == 0 && canJumpTo(playerBlock.getRelative(firstCord, 0, 0)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(firstCord, 0, 0).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			// Second attempt:
			if( player.getNearbyEntities(oppositeCord, 0, 0).size() == 0 && canJumpTo(playerBlock.getRelative(oppositeCord, 0, 0)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(oppositeCord, 0, 0).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			return false;
			
		}
		
		if(direction == 1 || direction == 3){
			
			// First attempt:
			if( player.getNearbyEntities(0, 0, firstCord).size() == 0 && canJumpTo(playerBlock.getRelative(0, 0, firstCord)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(0, 0, firstCord).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			// Second attempt:
			if( player.getNearbyEntities(0, 0, oppositeCord).size() == 0 && canJumpTo(playerBlock.getRelative(0, 0, oppositeCord)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(0, 0, oppositeCord).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			return false;
			
		}
		
		return false;
		
		
	}
	
	/**
	 * Pushed away an entity from the player
	 * 
	 * @param entity entity
	 * @param speed speed
	 */
	public void pushAwayEntity(Entity entity, double speed) {

		
		// Ignore if the player isn't online:
		if(!isOnline()){
			return;
		}
		
		// Get velocity unit vector:
		Vector unitVector = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
		
		// Set speed and push entity:
		entity.setVelocity(unitVector.multiply(speed));
		
		
	}

	/**
	 * Checks if the location can be jumped to. Requires a surface under feet.
	 * 
	 * @param block the player will be standing on
	 * @return true if can be jumped
	 */
	private static boolean canJumpTo(Block block) {
		
		
		if(BlockConstants.isTransparent(block.getType())){
			return false;
		}
		if(!BlockConstants.isTransparent(block.getRelative(BlockFace.UP).getType())){
			return false;
		}
		if(!BlockConstants.isTransparent(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType())){
			return false;
		}
		return true;
		
		
	}

	/**
	 * Gets the saga chunk the player is standing on.
	 * 
	 * @return saga chunk. null if not found
	 */
	public SagaChunk getSagaChunk(){
		
		
		if(!isOnlinePlayer){
			return null;
		}
		
		// Check last chunk:
		Chunk bukkitChunk = player.getLocation().getWorld().getChunkAt(player.getLocation());
		
		return getSagaChunk(bukkitChunk);
		
				
	}
	
	/**
	 * Gets the saga chunk that represents the bukkit chunk.
	 * Only retrieves the new saga chunk when the chunk has changed.
	 * 
	 * @param bukkitChunk bukkit chunk
	 * @return saga chunk represented by the bukkit chunk
	 */
	private SagaChunk getSagaChunk(Chunk bukkitChunk) {
	
		
		// Check if the chunk changed:
		if(bukkitChunk.equals(lastBukkitChunk)){
			return lastSagaChunk;
		}
		
		return ChunkGroupManager.getChunkGroupManager().getSagaChunk(bukkitChunk);
		
		
	}

	/**
	 * Gets the player location.
	 * 
	 * @return player location. null if no location
	 */
	public Location getLocation() {

		
		if(!isOnlinePlayer){
			return null;
		}
		return player.getLocation();
		
		
	}
	

	// Chunk groups:
	/**
	 * Checks if the player can build.
	 * 
	 * @param bukkitChunk bukkit chunk
	 * @return true if can build
	 */
	private Boolean canBuild(Chunk bukkitChunk) {

		
		SagaChunk newSagaChunk = getSagaChunk(bukkitChunk);
		
		if(newSagaChunk == null){
			return true;
		}
		
		ChunkGroup chunkGroup = newSagaChunk.getChunkGroup();
		if(chunkGroup == null){
			severe(PlayerMessages.chunkGroupNotFound());
			Saga.severe("Could not check chunk group for building permission, because the group doesent exist.", getName());
			return false;
		}
		return chunkGroup.canBuild(this);
		
		
	}
	
	/**
	 * Checks if the chunk ID changed. Accepts null parameters.
	 * 
	 * @param previousChunk first chunk
	 * @return true if no change
	 */
	private static boolean idsChanged(SagaChunk sagaChunk1, SagaChunk sagaChunk2) {

		
		// From null to null:
		if(sagaChunk1 == null && sagaChunk2 == null){
			return false;
		}
		// From null to non-null or non-null to null:
		if(sagaChunk1 == null || sagaChunk2 == null){
			return true;
		}
		
		// non-null to non-null:
		return !sagaChunk1.getChunkGroupId().equals(sagaChunk2.getChunkGroupId());
		
		
	}

	/**
	 * Forces an update on last saga chunk.
	 * 
	 */
	public void updateLastSagaChunk() {
		
		
		if(!isOnlinePlayer){
			return;
		}
		
		lastSagaChunk = ChunkGroupManager.getChunkGroupManager().getSagaChunk(player.getLocation());
		
		
	}
	
	// Events:
	/**
	 * Got damaged by living entity event, using melee.
	 *
	 * @param event event
	 */
	public void gotDamagedByEntityEvent(EntityDamageByEntityEvent event) {


		// Attributes:
		Attribute[] defenseAttributes = AttributeConfiguration.getConfig().defenseAttributes;
		for (int i = 0; i < defenseAttributes.length; i++) {
			String attributeName = defenseAttributes[i].getName();
			defenseAttributes[i].use(getAttributeUpgrade(attributeName), this, event);
		}
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.gotDamagedByEntityEvent(event);
		}
		sendMessage(PlayerMessages.gotDamagedByEntity(event.getDamage(), event.getDamager()));
		
		
	}

	/**
	 * Damaged a living entity, using melee.
	 *
	 * @param event event
	 */
	public void damagedEntityEvent(EntityDamageByEntityEvent event) {
		
		
		// Attributes:
		Attribute[] attackAtributes = AttributeConfiguration.getConfig().attackAttributes;
		for (int i = 0; i < attackAtributes.length; i++) {
			String attributeName = attackAtributes[i].getName();
			attackAtributes[i].use(getAttributeUpgrade(attributeName), this, event);
		}
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.damagedEntityEvent(event);
		}
		sendMessage(PlayerMessages.damagedEntity(event.getDamage(), event.getEntity()));
		
		
	}
	
	/**
	 * Damaged by the environment.
	 *
	 * @param event event
	 */
	public void damagedByEnvironmentEvent(EntityDamageEvent event) {
		
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.damagedByEnvironmentEvent(event);
		}
		
		
	}

	/**
	 * Left clicked.
	 *
	 * @param event event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent event) {
		
		
		// Send to ability manager:
		abilityManager.leftClickInteractEvent(event);
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.leftClickInteractEvent(event);
		}
		
		
	}

	/**
	 * Right clicked.
	 *
	 * @param event event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent event) {
		
		
		// Send to ability manager:
		abilityManager.rightClickInteractEvent(event);
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.rightClickInteractEvent(event);
		}
		
		
	}

	/**
	 * Player placed a block event.
	 *
	 * @param event event
	 */
	public void placedBlockEvent(BlockPlaceEvent event) {
	

		// Build protection:
		Chunk bukkitChunk = event.getBlock().getChunk();
		if(!canBuild(bukkitChunk)){
			event.setCancelled(true);
			sendMessage(ChunkGroupMessages.noPermission());
			return;
		}
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.placedBlockEvent(event);
		}
		
		
	}

	/**
	 * Player broke a block event.
	 *
	 * @param event event
	 */
	public void brokeBlockEvent(BlockBreakEvent event) {
		
		
		// Build protection:
		Chunk bukkitChunk = event.getBlock().getChunk();
		if(!canBuild(bukkitChunk)){
			event.setCancelled(true);
			sendMessage(ChunkGroupMessages.noPermission());
			return;
		}
		
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.brokeBlockEvent(event);
		}
		
		
	}
	
	/**
	 * Player damaged a block event.
	 *
	 * @param event event
	 */
	public void damagedBlockEvent(BlockDamageEvent event) {
		// Forward to all professions:
		for (Profession profession : professions) {
			profession.damagedBlockEvent(event);
		}
	}

	/**
	 * Saga player shoots a projectile.
	 * 
	 * @param event event
	 */
	public void sagaPlayerProjectileShotEvent(SagaPlayerProjectileShotEvent event) {
		
		
		// Attributes:
		Attribute[] projectileShotAttributes = AttributeConfiguration.getConfig().projectileShotAttributes;
		for (int i = 0; i < projectileShotAttributes.length; i++) {
			String attributeName = projectileShotAttributes[i].getName();
			projectileShotAttributes[i].use(getAttributeUpgrade(attributeName), this, event);
		}
		
		
	}
	
	/**
	 * Player moved event.
	 * 
	 * @param event event
	 */
	public void playerMoveEvent(PlayerMoveEvent event) {

		
		// Old chunks:
		SagaChunk oldSagaChunk = lastSagaChunk;
		
		// Chunk change:
		Location to = event.getTo();
		Chunk newBukkitChunk = to.getWorld().getChunkAt(to);
		SagaChunk newSagaChunk = getSagaChunk(newBukkitChunk);

		// Update last chunks:
		lastBukkitChunk = newBukkitChunk;
		lastSagaChunk = newSagaChunk;
		
		// Only if last chunk group changed:
		if(idsChanged(oldSagaChunk, newSagaChunk)){
			
			// Enter chunk group:
			if(newSagaChunk != null){
				ChunkGroup toGroup = newSagaChunk.getChunkGroup();
				if(toGroup != null){
					sendMessage(ChunkGroupMessages.enterChunkGroup(toGroup));
				}else{
					Saga.severe(PlayerMessages.chunkGroupNotFound(), getName());
					severe(PlayerMessages.chunkGroupNotFound());
				}
			}
			// Enter wilderness:
			else{
				sendMessage(ChunkGroupMessages.leaveChunkGroup());
			}
			
		}
		
		
		
		
		
		
	}

	/**
	 * Player joined event.
	 * 
	 * @param event event
	 */
	public void playerJoinEvent(PlayerJoinEvent event) {
		
		
		// Faction invites:
		if(factionInvites.size() > 0){
			ArrayList<SagaFaction> factions = new ArrayList<SagaFaction>();
			for (int i = 0; i < factionInvites.size(); i++) {
				SagaFaction faction = FactionManager.getFactionManager().getFaction(factionInvites.get(i));
				if( faction != null ){
					factions.add(faction);
				}else{
					Saga.info("SagaPlayer is invited to a non-existing faction. Removing invite.", getName());
					factionInvites.remove(i);
					i--;
				}
			}
			sendMessage(FactionMessages.pendingInvitations(this, factions));
		}
		

		// Chunk group invites:
		if(chunkGroupInvites.size() > 0){
			ArrayList<ChunkGroup> groups = new ArrayList<ChunkGroup>();
			for (int i = 0; i < chunkGroupInvites.size(); i++) {
				ChunkGroup chunkGroup = ChunkGroupManager.getChunkGroupManager().getChunkGroup(chunkGroupInvites.get(i));
				if( chunkGroup != null ){
					groups.add(chunkGroup);
				}else{
					Saga.info("SagaPlayer is invited to a non-existing chunk group. Removing invite.", getName());
					chunkGroupInvites.remove(i);
					i--;
				}
			}
			sendMessage(ChunkGroupMessages.pendingInvitations(this, groups));
		}
		
		
	}
	
	/**
	 * Sends a clock tick.
	 *
	 * @param tick tick number
	 */
	@Override
	public void clockTick() {
		
		
		// Stamina regeneration:
		naturalStaminaRegenerate();
		
		// Attribute temporary modification:
		for (int i = 0; i < temporaryAttributeNames.size(); i++) {
			Integer newTime = temporaryAttributeTimes.get(i) -1;
			temporaryAttributeTimes.set(i, newTime);
			// Remove if the time is up:
			if(newTime <=0){
				String removedName = temporaryAttributeNames.remove(i);
				Short removedLevel = temporaryAttributeUpgrades.remove(i);
				temporaryAttributeTimes.remove(i);
				modifyAttributes(removedName, (short) -removedLevel);
				// Send a message:
				if(removedLevel > 0){
					sendMessage(PlayerMessages.attributeIncreasedTo(removedName, removedLevel));
				}
				else if(removedLevel < 0){
					sendMessage(PlayerMessages.attributeDecreasedTo(removedName, removedLevel));
				}
				
			}
		}
		
		
	}


	// Saving and loading:
	/**
	 * Loads a offline saga player.
	 * 
	 * @param playerName player name
	 * @param player minecraft player
	 * @param plugin plugin instance for access
	 * @param balanceInformation balance information
	 * @return saga player
	 */
	public static SagaPlayer load(String playerName){
		
            // Try loading:
            SagaPlayer sagaPlayer;

            // Try loading:
            try {

                sagaPlayer = WriterReader.readPlayerInformation(playerName);

            } catch (FileNotFoundException e) {

                Saga.info("Player information file not found. Loading default information.", playerName);
                sagaPlayer = new SagaPlayer();

            } catch (IOException e) {

                Saga.severe("Player information file load failure. Loading default and disabling saving.", playerName);
                sagaPlayer= new SagaPlayer();
                sagaPlayer.setSavingEnabled(false);

            } catch (JsonParseException e) {

                Saga.severe("Player information file parse failure. Loading default information and disabling saving.", playerName);
                sagaPlayer= new SagaPlayer();
                sagaPlayer.setSavingEnabled(false);
                Saga.severe("Player information parse failure. Loading default and disabling saving.", playerName);
                // TODO Rename user information file to recover the corrupt data

            }

            // Set name:
            sagaPlayer.setName(playerName);

            // Complete:
            sagaPlayer.complete();

            return sagaPlayer;
				

	}
	
	/**
	 * Saves player information. Will not save if {@link #isSavingEnabled()} is false.
	 */
	public void save() {
		
		
            if( isSavingEnabled() ) {

                try {
                    WriterReader.writePlayerInformation(getName(), this);
                } catch (Exception e) {
                    Saga.severe("Player information save failure.", getName());
                }

            } else {
                Saga.info("Player information saving denied.", getName());
            }

		
	}
	
		
	// Control:
	/**
	 * True if the player is online.
	 * 
	 * @return true if the player is online
	 */
	public boolean isOnline() {
            return isOnlinePlayer;
	}
	
	/**
	 * True if the player information should be saved.
	 * 
	 * @return true if player information should be saved
	 */
	public boolean isSavingEnabled() {
            return isSavingEnabled;
	}
	
	/**
	 * Disables or enables player information saving.
	 * 
	 * @param savingDisabled true if player information should be disabled 
	 */
	public void setSavingEnabled(boolean savingDisabled) {
            this.isSavingEnabled = savingDisabled;
	}

	/**
	 * Increases player force level.
	 * 
	 */
	public void increaseForceLevel() {

		forcedLevel++;
		
	}
	
	/**
	 * Decreases player force level.
	 * 
	 */
	public void decreaseForceLevel() {

		forcedLevel--;
		
	}
	
	/**
	 * Check if the player can be unforced.
	 * 
	 * @return true if can be unforced.
	 */
	public boolean isForced() {
		return forcedLevel > 0;
	}
	
	
	// Debuging:
	public void info(String message) {

    	
        if ( isOnline() ) {
            this.player.sendMessage(PlayerMessages.normalTextColor1 + message);
        }

        
    }

    public void warning(String message) {

        if ( isOnline() ) {
            this.player.sendMessage(PlayerMessages.negativeHighlightColor + message);
        }

    }

    public void severe(String message) {

        if ( isOnline() ) {
            this.player.sendMessage(PlayerMessages.veryNegativeHighlightColor + message);
        }

    }

	
	
}
