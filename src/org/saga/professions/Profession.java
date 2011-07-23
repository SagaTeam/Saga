package org.saga.professions;

import java.util.*;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.SagaPlayer;
import org.saga.abilities.Ability;
import org.saga.constants.PlayerDefaults;
import org.saga.constants.PlayerMessages;
import org.saga.*;

public abstract class Profession {

	
	/**
	 * Class name used by the loader
	 */
	private String _className;
	
	
	// General:
	/**
	 * Profession name.
	 */
	transient private String professionName;
	
	// Player information:
	/**
	 * Profession level.
	 */
	private Short level;

	/**
	 * Experience for the current level.
	 */
	private Integer levelExperience;

	
	// Calculated:
	/**
	 * Stamina drain for the current level.
	 */
	transient private Short[] staminaDrain;
	
	
	// Access:
	/**
	 * Minecraft player.
	 */
	transient private SagaPlayer sagaPlayer;
	
	
	// Main:
	/**
	 * Selected ability.
	 */
	transient short selectedAbility=0;
	
	/**
	 * Activated abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	
	// Initialization:
	/**
	 * Sets all default values. All extending classes must set all default values in a non-parameter constructor.
	 * 
	 * @param professionName profession name
	 * @param className class name
	 */
	public Profession(String professionName, String className) {
	
		
		// Set class name:
		_className = className;
 		
		
		// Force all extending classes to provide a name:
		this.professionName = professionName;
		
		// Set defaults:
		level= PlayerDefaults.level;
		levelExperience= PlayerDefaults.levelExperience;
		
		
	}
	
	public Profession() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Wraps all required variables.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void setAccess(SagaPlayer sagaPlayer) {
		
		
		this.sagaPlayer= sagaPlayer;
		
		
	}
	
	
	// Interaction:
	/**
	 * Returns the profession name.
	 * 
	 * @return the profession name
	 */
	public String getProfessionName(){
		
		return professionName;
		
	}
	
	private void selectNextAbility() {

		
		Ability[] professionAbilities= getAbilities();
		
		short selectedNew= (short) (selectedAbility+1);
		while (true) {
			if(selectedNew>=professionAbilities.length){
				selectedNew= 0;
				break;
			}
			if(professionAbilities[selectedNew].levelHighEnough(level)){
				break;
			}
			selectedNew++;
		}
		selectedAbility= selectedNew;
		
		// Send message:
		if(selectedNew==selectedAbility){
			sagaPlayer.sendMessage(PlayerMessages.noAbilitiesAvailable());
		}else{
			sagaPlayer.sendMessage(PlayerMessages.abilitySelect(professionAbilities[selectedNew]));
		}
		
		
	}
	
	/**
	 * Returns all abilities for the profession.
	 * 
	 * @return all abilities
	 */
	protected abstract Ability[] getAbilities();
	
	
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



	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Player placed a block event.
	 *
	 * @param pEvent event
	 */
	public void placedBlockEvent(BlockPlaceEvent pEvent) {



	}

	/**
	 * Player broke a block event.
	 *
	 * @param pEvent event
	 */
	public void brokeBlockEvent(BlockBreakEvent pEvent) {



	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {


	}

}
