package org.saga;

import java.io.*;
import java.util.Hashtable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternInitiator;
import org.saga.professions.*;
import org.saga.utility.WriterReader;
import org.saga.abilities.Ability;
import org.saga.constants.*;

import com.google.gson.JsonParseException;

public class SagaPlayer{

	
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
	public Profession[] professions;
	
	/**
	 * Professions that the player can interact with.
	 */
	private Boolean[] selectedProfessions;
	
	
	// Control:
	/**
	 * Specifies if the player is online.
	 */
	transient private boolean isOnlinePlayer= false;
	
	/**
	 * Disables and enables player information saving.
	 */
	transient private boolean isSavingEnabled=true;
	
	
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
		
		// Professions field is null:
		Profession[] allProfessions = Saga.balanceInformation().getAllProfessions();
		if(professions == null){
			Saga.info("Initializing new player professions field.", name);
			professions = allProfessions;
		}
		
		// Selected professions field is null or wrong length:
		if(selectedProfessions == null || selectedProfessions.length!=allProfessions.length){
			Saga.info("Initializing new player selected professions field.", name);
			selectedProfessions = new Boolean[allProfessions.length];
			for (int i = 0; i < selectedProfessions.length; i++) {
				selectedProfessions[i] = true; // TODO Profession filter here
			}
		}
		
		// Professions field wrong length:
		if(professions.length != allProfessions.length){
			Saga.info("Initializing new player professions field.", name);
			Profession[] professionsCorected = new Profession[allProfessions.length];
			for (int i = 0; i < professions.length; i++) {
				professionsCorected[i] = professions[i];
			}
			professions = professionsCorected;
		}
		
		// All professions:
		for (int i = 0; i < allProfessions.length; i++) {
			Profession profession = professions[i];
			if(profession == null || (!profession.getClass().equals(allProfessions[i].getClass()))){
				profession = allProfessions[i];
				professions[i] = profession;
				Saga.info("Adding "+profession.getClass().getSimpleName() + " player profession and setting default values.", name);
			}
			profession.setAccess(this);
			profession.complete();
			
			
		}
		
		
	}
	
	// Interaction:
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
	
	/**
	 * Checks if there is enough stamina.
	 * 
	 * @param staminaSubs Stamina to be subtracted.
	 * @return true if there is enough stamina
	 */
	public boolean enoughStamina(Double staminaSubtr) {
		return stamina>=staminaSubtr;
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
	 * Gets stamina.
	 * 
	 * @return stamina
	 */
	public Double getStamina() {
		return stamina;
	}
	
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
	 * Gets players maximum stamina. Including bonuses.
	 * 
	 * @return player maximum stamina
	 */
	public double getMaximumStamina() {
		
		return Saga.balanceInformation().maximumStamina+0;

	}
	
	/**
	 * Check if there is enough stamina to drain.
	 * 
	 * @param drainAmount drain amount
	 * @return true if there is enough stamina.
	 */
	public boolean enoughStamina(double drainAmount) {

		return stamina>=drainAmount;
		
	}
	
	/**
	 * Regenerates stamina.
	 */
	private void naturalStaminaRegenerate() {
		
		
		if(stamina >= getMaximumStamina()){
			return;
		}
		stamina += Saga.balanceInformation().staminaPerSecond;
		if(stamina > getMaximumStamina()){
			stamina = getMaximumStamina();
		}
		if(((int)(stamina - Saga.balanceInformation().staminaPerSecond)/10) != (int)(stamina/10)){
			sendMessage(PlayerMessages.staminaRegeneration(stamina, getMaximumStamina()));
		}

	}
	
	/**
	 * Sends the player a message if he is online.
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		
            if(isOnlinePlayer()){
            	PlayerMessages.sendMultipleLines(message, player);
            }

	}
	
	/**
	 * Moves the player to the given location.
	 * Must be used when the teleport is part of an ability.
	 * 
	 * @param location location
	 */
	public void moveTo(Location location) {

		if(isOnlinePlayer()){
        	player.teleport(location);
        }
		
	}
	
	/**
	 * Centers the location to the block and moves the player there.
	 * Must be used when the teleport is part of an ability.
	 * 
	 * @param location location
	 */
	public void moveToCentered(Location location) {
		System.out.println("pitch:"+ player.getLocation().getPitch());
		moveTo(new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5));
		
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
	private int calculatePlayerHorizontalDirection(){
		
		
		if(!isOnlinePlayer()){
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
		
		
		if(!isOnlinePlayer()){
			return true;
		}
		
		float pitch = player.getLocation().getPitch();
		if(pitch>0){
			return false;
		}else{
			return true;
		}
		
		
	}
	
	/**
	 * Gets a profession.
	 * 
	 * @param profession profession index
	 * @return profession
	 * @throws IndexOutOfBoundsException if the give index is out of bounds.
	 */
	public Profession getProfessions(int profession) throws IndexOutOfBoundsException{
		return professions[profession];
	}
	
	/**
	 * Gets a if the profession is selected.
	 * 
	 * @param profession profession index
	 * @return true if the profession is selected
	 * @throws IndexOutOfBoundsException if the give index is out of bounds.
	 */
	public boolean isProfessionSelected(int profession) throws IndexOutOfBoundsException{
		return selectedProfessions[profession];
	}
	
	/**
	 * Returns the number of professions.
	 * 
	 * @return the number of professions
	 */
	public int getProfessionCount() {
		return professions.length;
	}
	
	/**
	 * Deactivates an ability if possible.
	 * 
	 * @param ability ability index
	 */
	public void deactivateAbility(Ability ability){
		
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].deactivateAbility(ability);
		}
		
		
	}
	
	
	// Events:
	/**
	 * Got damaged by living entity event.
	 *
	 * @param pEvent event
	 */
	public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent pEvent) {

		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].gotDamagedByLivingEntityEvent(pEvent);
		}
		sendMessage(PlayerMessages.gotDamagedByEntity(pEvent.getDamage(), pEvent.getDamager()));
		
	}

	/**
	 * Damaged a living entity.
	 *
	 * @param pEvent event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent pEvent) {
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].damagedLivingEntityEvent(pEvent);
		}
		sendMessage(PlayerMessages.damagedEntity(pEvent.getDamage(), pEvent.getEntity()));
		
	}
	
	/**
	 * Damaged by the environment.
	 *
	 * @param pEvent event
	 */
	public void damagedByEnvironmentEvent(EntityDamageEvent pEvent) {
		
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].damagedByEnvironmentEvent(pEvent);
		}
		
		
	}
	

	/**
	 * Left clicked.
	 *
	 * @param pEvent event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent pEvent) {
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].leftClickInteractEvent(pEvent);
		}
	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].rightClickInteractEvent(pEvent);
		}
	}

	/**
	 * Player placed a block event.
	 *
	 * @param pEvent event
	 */
	public void placedBlockEvent(BlockPlaceEvent pEvent) {
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].placedBlockEvent(pEvent);
		}
	}

	/**
	 * Player broke a block event.
	 *
	 * @param pEvent event
	 */
	public void brokeBlockEvent(BlockBreakEvent pEvent) {
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].brokeBlockEvent(pEvent);
		}
	}
	
	/**
	 * Player damaged a block event.
	 *
	 * @param pEvent event
	 */
	public void damagedBlockEvent(BlockDamageEvent pEvent) {
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].damagedBlockEvent(pEvent);
		}
	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {
		
		
		// Stamina regeneration:
		naturalStaminaRegenerate();
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].clockTickEvent(pTick);
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
	public boolean isOnlinePlayer() {
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

        public void info(String message) {

            if ( isOnlinePlayer() ) {
                this.player.sendMessage(PlayerMessages.infoColor + message);
            }

        }

        public void warning(String message) {

            if ( isOnlinePlayer() ) {
                this.player.sendMessage(PlayerMessages.warningColor + message);
            }

        }

        public void severe(String message) {

            if ( isOnlinePlayer() ) {
                this.player.sendMessage(PlayerMessages.warningColor + message);
            }

        }

}
