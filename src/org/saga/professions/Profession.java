package org.saga.professions;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.Saga;
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
	 * Saga player.
	 */
	transient private SagaPlayer sagaPlayer= null;
	
	
	// Main:
	/**
	 * Selected ability.
	 */
	transient short selectedAbility=-1;
	
	/**
	 * Activated abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	
	// Initialization:
	/**
	 * Sets all default values. All extending classes must set all default values in a non-parameter constructor.
	 * 
	 * @param professionName profession name
	 */
	public Profession(String professionName) {
	
		
		// Set class name:
		_className = getClass().getName();
 		
		
		// Force all extending classes to provide a name:
		this.professionName = professionName;
		
		
		
	}
	
	public Profession() {
	}
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 */
	public void complete() {
		
		// Fields:
		if(level==null){
			level = PlayerDefaults.level;
			Saga.info("Setting default value for profession level.", sagaPlayer.getName());
		}
		if(levelExperience==null){
			levelExperience = PlayerDefaults.levelExperience;
			Saga.info("Setting default value for profession levelExperience.", sagaPlayer.getName());
		}
		
		// Inheriting class:
		completeInheriting();
		

	}
	
	/**
	 * Does a complete for all inheriting classes.
	 */
	public abstract void completeInheriting();
	
	
	/**
	 * Wraps all required variables.
	 * 
	 * @param s2agaPlayer saga player
	 */
	public void setAccess(SagaPlayer s2agaPlayer) {
		
		this.sagaPlayer= s2agaPlayer;
		
		
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
	
	/**
	 * Selects next available ability. 
	 * 
	 * @param materialInHand material of the item held
	 */
	private void selectNextAbility(Material materialInHand) {

		
		// Check if the material is correct:
		Material[] scrollMaterials = getAbilityScrollMaterials();
		for (int i = 0; i < scrollMaterials.length; i++) {
			if(scrollMaterials[i].equals(materialInHand)){
				break;
			}
			if(i == scrollMaterials.length-1){
				return;
			}
		}
		
		Ability[] professionAbilities= getAbilities();
		
		short selectedNew = (short) (selectedAbility+1);
		while (true) {
			if(selectedNew>=professionAbilities.length){
				selectedNew= -1;
				break;
			}
			if(professionAbilities[selectedNew].levelHighEnough(level)){
				break;
			}
			selectedNew++;
		}
		
		
		// Send message:
		if(selectedNew==-1 && selectedAbility==-1){
			sagaPlayer.sendMessage(PlayerMessages.noAbilitiesAvailable());
		}else if(selectedNew==-1){
			sagaPlayer.sendMessage(PlayerMessages.abilitySelectNone());
		}else{
			sagaPlayer.sendMessage(PlayerMessages.abilitySelect(professionAbilities[selectedNew]));
		}
		
		// Set selected ability:
		selectedAbility= selectedNew;
		
	}
	
	/**
	 * Returns all abilities for the profession.
	 * 
	 * @return all abilities
	 */
	protected abstract Ability[] getAbilities();
	
	/**
	 * Returns all ability scroll materials.
	 * 
	 * @return ability scroll materials
	 */
	protected abstract Material[] getAbilityScrollMaterials();
	
	
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

		pEvent.getPlayer().sendMessage("LCLICK");

	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {

		
		// Ability scroll:
		selectNextAbility(pEvent.getPlayer().getItemInHand().getType());

		
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
