package org.saga;

import java.io.*;
import java.util.*;

import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.abilities.*;
import org.saga.professions.*;
import org.saga.utility.WriterReader;
import org.saga.defaults.*;

import com.google.gson.JsonParseException;

public class SagaPlayer{

	
	// Player information:
	/**
	 * Name.
	 */
	private String name = null;
	
	/**
	 * Stamina.
	 */
	private Double stamina;

	/**
	 * Determines which professions are selected for the player. 
	 */
	private Boolean[] selectedProfessions;

	
	// Wrapped:
	/**
	 * Minecraft player.
	 */
	transient private Player player;
	
	
	// Main fields:
	/**
	 * Wrapped professions.
	 */
	private ProfessionHolder professions;
	
	
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
		
            // Sets to offline by default.
            isOnlinePlayer = false;
		
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
		this.info(Messages.staminaUsed(drainAmount, getStamina(), getMaximumStamina()));
		
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
            // Forward to wrapped professions:
            professions.leftClickInteractEvent(pEvent);
	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {
            // Forward to wrapped professions:
            professions.rightClickInteractEvent(pEvent);
	}

	/**
	 * Player placed a block event.
	 *
	 * @param pEvent event
	 */
	public void placedBlockEvent(BlockPlaceEvent pEvent) {
            // Forward to wrapped professions:
            professions.placedBlockEvent(pEvent);
	}

	/**
	 * Player broke a block event.
	 *
	 * @param pEvent event
	 */
	public void brokeBlockEvent(BlockBreakEvent pEvent) {
            // Forward to wrapped professions:
            professions.brokeBlockEvent(pEvent);
	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {
            // Forward to wrapped professions:
            professions.clockTickEvent(pTick);
	}

	
	// Saving and loading:
	/**
	 * Loads a offline saga player.
	 * Needs an integrity check.
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
                sagaPlayer.checkIntegrity();

            } catch (IOException e) {

                Saga.severe("Player information file not found. Loading default and disabling saving.", playerName);
                sagaPlayer= new SagaPlayer();
                sagaPlayer.setSavingEnabled(false);
                sagaPlayer.checkIntegrity();

            } catch (JsonParseException e) {

                Saga.severe("Player information file parse failure. Loading default information and disabling saving.", playerName);
                sagaPlayer= new SagaPlayer();
                sagaPlayer.setSavingEnabled(false);
                sagaPlayer.checkIntegrity();
                Saga.severe("Can't read player information. Loading default and disabling saving.", playerName);
                // TODO Rename user information file to recover the corrupt data

            }

            // Set name:
            sagaPlayer.setName(playerName);

            // Add access:

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
	
	
	// Integrity check:
	/**
	 * Checks the integrity of the player information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	public boolean checkIntegrity(ArrayList<String> problematicFields) {
		
		
		// Professions field:
		if( professions == null ) {
                    professions= new ProfessionHolder();
                    problematicFields.add("professions");
                    professions.checkIntegrity(new ArrayList<String>());
		}
		
		// Professions:
		professions.checkIntegrity(problematicFields);
		
		// All fields:
		if( name == null ){
                    name = PlayerDefaults.name;
                    problematicFields.add("name");
		}

		if( stamina == null ){
                    stamina = PlayerDefaults.stamina;
                    problematicFields.add("stamina");
		}
		
		
		return problematicFields.isEmpty();

		
	}

        public boolean checkIntegrity() {

            ArrayList<String> problematicFields = new ArrayList<String>();

            if ( this.checkIntegrity(problematicFields) == true) {
                return true;
            }

            for ( String field : problematicFields ) {

                Saga.warning(field + " data invalid! Loaded default.", this.name);

            }

            return false;

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
                this.player.sendMessage(Config.infoColor + message);
            }

        }

        public void warning(String message) {

            if ( isOnlinePlayer() ) {
                this.player.sendMessage(Config.warningColor + message);
            }

        }

        public void severe(String message) {

            if ( isOnlinePlayer() ) {
                this.player.sendMessage(Config.warningColor + message);
            }

        }

}
