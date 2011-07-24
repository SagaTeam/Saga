package org.saga;

import java.io.*;
import java.util.Hashtable;

import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.professions.*;
import org.saga.utility.WriterReader;
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
	private Profession[] professions;
	
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
		
		// Professions field wrong length:
		if(professions.length != allProfessions.length){
			Saga.info("Initializing new player professions field.", name);
			Profession[] professionsCorected = new Profession[allProfessions.length];
			for (int i = 0; i < professionsCorected.length; i++) {
				professionsCorected[i] = professions[i];
			}
		}
		
		// All professions:
		for (int i = 0; i < allProfessions.length; i++) {
			Profession profession = professions[i];
			if(profession == null || (!profession.getClass().equals(allProfessions[i].getClass()))){
				professions[i]= allProfessions[i];
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
	 * Returns player name.
	 * 
	 * @return player name
	 */
	public Player getPlayer() {
            return player;
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
	 * Drains stamina and sends a message. Can go below zero.
	 * 
	 * @param drainAmount drain amount
	 */
	public void drainStamina(Double drainAmount) {

		stamina -= drainAmount;
		this.info(PlayerMessages.staminaUsed(drainAmount, getStamina(), getMaximumStamina()));
		
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
	 * Sends the player a message if he is online.
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		
            if(isOnlinePlayer()){
                    player.sendMessage(message);
            }

	}
	
	// Events:
	/**
	 * Got damaged by living entity event.
	 *
	 * @param pEvent event
	 */
	public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent pEvent) {

	}

	/**
	 * Damaged a living entity.
	 *
	 * @param pEvent event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent pEvent) {

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
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {
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
