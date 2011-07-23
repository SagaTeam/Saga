package org.saga;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.abilities.Ability;
import org.saga.professions.FighterProfession;
import org.saga.professions.Profession;
import org.saga.professions.ProfessionHolder;
import org.saga.professions.WoodcutterProfession;
import org.saga.utility.WriterReader;

import com.google.gson.JsonParseException;

public class SagaPlayer{

	
	// Player information:
	/**
	 * Name.
	 */
	private String name=null;
	
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
		isOnlinePlayer=false;
		
		
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
		isOnlinePlayer=true;
	}
	
	/**
	 * Sets the player and changes status to offline.
	 * 
	 */
	public void removePlayer() {
		this.player = null;
		isOnlinePlayer=false;
	}
	
	/**
	 * Drains stamina and sends a message. Can go below zero.
	 * 
	 * @param drainAmount drain amount
	 */
	public void drainStamina(Double drainAmount) {

		stamina-=drainAmount;
		sendMessage(Messages.staminaUsed(drainAmount, getStamina(), getMaximumStamina()));
		
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
                    sagaPlayer.checkIntegrity(new Vector<String>());

		} catch (IOException e) {

			Saga.severe("Player information file not found. Loading default and disabling saving.", playerName);
			sagaPlayer= new SagaPlayer();
			sagaPlayer.setSavingEnabled(false);
			sagaPlayer.checkIntegrity(new Vector<String>());
		} catch (JsonParseException e) {
			Saga.severe("Player information file parse failure. Loading default information and disabling saving.", playerName);
			sagaPlayer= new SagaPlayer();
			sagaPlayer.setSavingEnabled(false);
			sagaPlayer.checkIntegrity(new Vector<String>());
//			if(player!=null){
//				player.sendMessage(Messages.PLAYER_ERROR_MESSAGE);
//				player.sendMessage("Can't read player information. Your progress will not be saved for this session!");
//				}
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
		
		
		if(isSavingEnabled()){
			try {
				WriterReader.writePlayerInformation(getName(), this);
			} catch (Exception e) {
				Saga.severe("Player information save failure.", getName());
			}
		}else{
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
	public Boolean checkIntegrity(Vector<String> problematicFields) {
		
		
		// Professions field:
		if(professions==null){
			professions= new ProfessionHolder();
			problematicFields.add("professions");
			professions.checkIntegrity(new Vector<String>());
		}
		
		// Professions:
		professions.checkIntegrity(problematicFields);
		
		// All fields:
		if(name==null){
			name= PlayerDefaults.name;
			problematicFields.add("name");
		}
		if(stamina==null){
			stamina= PlayerDefaults.stamina;
			problematicFields.add("stamina");
		}
		
		
		return problematicFields.size()==0;

		
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
	
}
