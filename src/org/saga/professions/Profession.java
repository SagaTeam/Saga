package org.saga.professions;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.abilities.Ability;
import org.saga.constants.PlayerDefaults;
import org.saga.constants.PlayerMessages;
import org.saga.*;

public abstract class Profession {

	
	// General:
	/**
	 * Class name used by the loader
	 */
	private String _className;
	
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
	 * Experience requirement for the next level.
	 */
	transient private Integer experienceRequirement;
	
	// Access:
	/**
	 * Saga player.
	 */
	transient protected SagaPlayer sagaPlayer= null;
	
	
	// Main:
	/**
	 * Selected ability.
	 */
	transient private short selectedAbility=-1;
	
	/**
	 * All abilities.
	 */
	transient private Ability[] abilities;
	
	/**
	 * Ability timers.
	 */
	transient private short[] abilityTimers;
	
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
		completeExtended();

		
		// Initialize dependent fields:
		abilities = getAbilities();
		experienceRequirement = calculateExperienceRequirement(level);
		abilityTimers = new short[abilities.length];

	}
	
	/**
	 * Does a complete for an extended classes.
	 */
	public abstract void completeExtended();
	
	
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
	
	/**
	 * Selects next available ability. 
	 * 
	 */
	private void selectNextAbility() {

		short selectedNew = (short) (selectedAbility+1);
		while (true) {
			if(selectedNew>=abilities.length){
				selectedNew= -1;
				break;
			}
			if(abilities[selectedNew].levelHighEnough(level)){
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
			sagaPlayer.sendMessage(PlayerMessages.abilitySelect(abilities[selectedNew]));
		}
		
		// Set selected ability:
		selectedAbility= selectedNew;
		
		
	}
	
	/**
	 * Resets the ability selection.
	 */
	private void resetAbilitySelection() {
		selectedAbility = -1;
		// Message here, if needed
	}
	
	/**
	 * Returns all abilities for the profession.
	 * 
	 * @return all abilities
	 */
	protected abstract Ability[] getAbilities();
	
	/**
	 * Return the ability name.
	 * 
	 * @param ability ability
	 * @return ability name
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	public String getAbilityName(int ability) throws IndexOutOfBoundsException{
		return abilities[ability].getAbilityName();
	}
	
	/**
	 * Gets the level requirement for the ability.
	 * 
	 * @param ability ability
	 * @return level requirement
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	public Short getAbilityLevelRequirement(int ability) throws IndexOutOfBoundsException{
		return abilities[ability].getLevelRequirement();
	}
	
	/**
	 * Gets the stamina use for the ability.
	 * 
	 * @param ability ability
	 * @return stamina requirement
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	public Double getAbilityStaminaUse(int ability) throws IndexOutOfBoundsException{
		return abilities[ability].calculateStaminaUse(getLevel());
	}
	
	
	/**
	 * Checks if the ability is already active.
	 * 
	 * @param ability ability
	 * @return true, if active
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	public abstract boolean isAbilityActive(int ability) throws IndexOutOfBoundsException;
	
	/**
	 * Returns the ability count.
	 * 
	 * @return ability count
	 */
	public int getAbilityCount() {
		return abilities.length;
	}
	
	/**
	 * Returns all ability scroll materials.
	 * 
	 * @return ability scroll materials
	 */
	protected abstract Material[] getAbilityScrollMaterials();
	
	/**
	 * Deactivates an ability if possible.
	 * 
	 * @param ability ability index
	 */
	public void deactivateAbility(Ability ability){
		for (int i = 0; i < abilities.length; i++) {
			if(abilities[i].equals(ability)){
				abilityDeactivateEvent(i);
				sagaPlayer.sendMessage(PlayerMessages.abilityDeactivate(abilities[i]));
			}
		}
	}
	
	/**
	 * Adds experience
	 * 
	 * @param amount amount to add
	 */
	public void gainExperience(Integer amount) {
		
		levelExperience+=amount;
		if(levelExperience >= experienceRequirement){
			levelUp();
		}
		
	}
	
	/**
	 * Levels up the profession.
	 * 
	 */
	public void levelUp() {
		
		setLevel((short) (level+1));
		
	}
	
	/**
	 * Sets level.
	 * 
	 * @param level level to set
	 */
	public void setLevel(Short level) {


		this.level = level;
		levelExperience = 0;
		experienceRequirement = calculateExperienceRequirement(level);
		PlayerMessages.levelUp(this, level);
		
		
	}
	
	/**
	 * Calculates the required experience for level up experience.
	 * 
	 * @param level level
	 */
	private Integer calculateExperienceRequirement(Short level) {

		return level*Saga.balanceInformation().experienceSlope * level + Saga.balanceInformation().experienceIntercept;

	}
	
	/**
	 * Returns the level.
	 * 
	 * @return level
	 */
	public Short getLevel() {
		return level;
	}
	
	/**
	 * Returns profession experience.
	 * 
	 * @return level level
	 */
	public Integer getLevelExperience() {
		return levelExperience;
	}
	
	/**
	 * Returns experience required for level up for this level.
	 * 
	 * @return level level
	 */
	public Integer getExperienceRequirement() {
		return experienceRequirement;
	}
	
	/**
     * Check if the given material is correct.
     * 
     * @param material material
     * @return true if correct
     */
    public boolean isMaterialCorrect(Material material) {

    	
    	Material[] abilityScrollMaterials = getAbilityScrollMaterials();
    	for (int i = 0; i < abilityScrollMaterials.length; i++) {
			if(abilityScrollMaterials[i].equals(material))
				return true;
		}
    	return false;
    	
    	
	}

    
	// Events:
	/**
	 * Got damaged by living entity event.
	 *
	 * @param event event
	 */
	public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent event) {



	}

	/**
	 * Damaged a living entity.
	 *
	 * @param event event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent event) {



	}
	
	/**
	 * Damaged by the environment.
	 *
	 * @param event event
	 */
	public void damagedByEnvironmentEvent(EntityDamageEvent event) {
		
		
		
	}

	/**
	 * Left clicked.
	 *
	 * @param event event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent event) {

		
		// Check if the material is correct and try activating an ability:
		Material[] scrollMaterials = getAbilityScrollMaterials();
		for (int i = 0; i < scrollMaterials.length && selectedAbility!=-1; i++) {
			
			if(scrollMaterials[i].equals(event.getPlayer().getItemInHand().getType())){
				// Check if there is enough stamina:
				Double staminaUse = abilities[selectedAbility].calculateStaminaUse(level);
				if(sagaPlayer.enoughStamina(staminaUse)){
					// Check if already active:
					if(!isAbilityActive(selectedAbility)){
						sagaPlayer.sendMessage(PlayerMessages.abilityActivate(abilities[selectedAbility]));
						abilityActivateEvent(selectedAbility);
						abilityTimers[selectedAbility] = abilities[selectedAbility].calculateAbilityActiveTime(getLevel());
						sagaPlayer.useStamina(staminaUse);
						resetAbilitySelection();
					}else{
						sagaPlayer.sendMessage(PlayerMessages.abilityAlreadyActive(abilities[selectedAbility]));
						resetAbilitySelection();
					}
					
				}else{
					sagaPlayer.sendMessage(PlayerMessages.notEnoughStamina(abilities[selectedAbility], sagaPlayer.getStamina(), sagaPlayer.getMaximumStamina(), staminaUse));
					resetAbilitySelection();
				}
				break;
			}
			
		}

		
	}

	/**
	 * Right clicked.
	 *
	 * @param event event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent event) {

		
		// Check if the material is correct for an ability scroll:
		Material[] scrollMaterials = getAbilityScrollMaterials();
		for (int i = 0; i < scrollMaterials.length; i++) {
			if(scrollMaterials[i].equals(event.getPlayer().getItemInHand().getType())){
				selectNextAbility();
				break;
			}
		}
		
		
		
	}

	/**
	 * Player placed a block event.
	 *
	 * @param event event
	 */
	public void placedBlockEvent(BlockPlaceEvent event) {



	}

	/**
	 * Player broke a block event.
	 *
	 * @param event event
	 */
	public void brokeBlockEvent(BlockBreakEvent event) {


	}
	
	/**
	 * Player damaged a block event.
	 *
	 * @param event event
	 */
	public void damagedBlockEvent(BlockDamageEvent event) {


	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {

		
		// Ability deactivate timer:
		for (int i = 0; i < abilities.length; i++) {
			if(isAbilityActive(i)){
				abilityTimers[i]--;
				if(abilityTimers[i]<=0){
					abilityDeactivateEvent(i);
					deactivateAbility(abilities[i]);
				}
			}
		}
		

	}

	/**
	 * Activates an ability.
	 * 
	 * @param ability ability index
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	protected abstract void abilityActivateEvent(int ability) throws IndexOutOfBoundsException;
	
	/**
	 * Deactivates an ability.
	 * 
	 * @param ability ability index
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	protected abstract void abilityDeactivateEvent(int ability) throws IndexOutOfBoundsException;
	
	
	
	
}
