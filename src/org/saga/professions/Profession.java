package org.saga.professions;


import java.util.Enumeration;
import java.util.Hashtable;

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

public abstract class Profession {

	
	// General:
	/**
	 * Class name used by the loader
	 */
	@SuppressWarnings("unused")
	private String _className;
	
	/**
	 * Profession name.
	 */
	transient private String professionName = "";

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
	 * All abilities.
	 */
	transient private Ability[] abilities;
	
	/**
	 * Attribute upgrades. Keeps a list of upgrade levels for the current level.
	 */
	transient private Hashtable<String, Short> attributeUpgrades = new Hashtable<String, Short>();
	
	
	// Initialization:
	/**
	 * Sets all default values. All extending classes must set all default values in a non-parameter constructor.
	 * 
	 * @param professionName profession name
	 */
	public Profession(String professionName) {
		_className = getClass().getName();
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
		
		// Initiate attributes:
		modifyAttributeUpgrades(getRawLevel());
		
		
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
	public String getName(){
		
		return professionName;
		
	}

	/**
	 * Returns profession type.
	 * 
	 * @return profession type
	 */
	public abstract ProfessionType getProfessionType();
	
	/**
	 * Returns all abilities for the profession.
	 * 
	 * @return all abilities
	 */
	public abstract Ability[] getAbilities();
	
	/**
	 * Return the ability name.
	 * 
	 * @param ability ability
	 * @return ability name
	 * @throws IndexOutOfBoundsException when the given ability index is out of bounds
	 */
	public String getAbilityName2222222(int ability) throws IndexOutOfBoundsException{
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
	 */
	public abstract boolean isAbilityActive(Ability ability) throws IndexOutOfBoundsException;
	
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
	public abstract Material[] getAbilityScrollMaterials();
	
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


		// Increase level:
		this.level = level;
		levelExperience = 0;
		experienceRequirement = calculateExperienceRequirement(level);
		PlayerMessages.levelUp(this, level);
		
		// Modify attributes:
		modifyAttributeUpgrades(level);
		
		
	}
	
	/**
	 * Calculates the level for the given attribute based on the levels.
	 * Negative attributeLevels decrease the level.
	 * 
	 * @param attributeUpgrades attribute upgrades
	 * @param professionLevel profession level
	 * @return attribute upgrade
	 */
	private Short calculateAttributeUpgrade(Short[] attributeUpgrades, Short professionLevel) {
	
		
		Short attributeLevel = 0;
		for (int i = 0; i < attributeUpgrades.length; i++) {
			if(attributeUpgrades[i] >= 0 && attributeUpgrades[i] <= professionLevel){
//				System.out.println(attributeUpgrades[i]);
				attributeLevel++;
			}else if(attributeUpgrades[i] <= 0 && attributeUpgrades[i] >= professionLevel){
				attributeLevel--;
			}
		}
		return attributeLevel;
		
		
	}
	
	/**
	 * Modifies the attribute upgrades so they match the given level.
	 * 
	 * @param newLevel level
	 */
	private void modifyAttributeUpgrades(Short newLevel) {
		
		
		// Retrieve attribute upgrades for the profession:
		Hashtable<String, Short[]> upgrades = Saga.attributeInformation().getAttributeUpgrades(getName());
		
		// Loop trough all retrieved attributes:
		Enumeration<String> allAttributeNames= upgrades.keys();
		while ( allAttributeNames.hasMoreElements() ) {
            String attributeName = allAttributeNames.nextElement();
            Short oldValue = attributeUpgrades.get(attributeName);
            Short newValue = calculateAttributeUpgrade(upgrades.get(attributeName), newLevel);
            if(oldValue == null){
            	oldValue = 0;
            }
            Short delta = (short) (newValue - oldValue);
            // Do a attribute modify if there is a difference between new and old value:
            if(delta!=0){
            	attributeUpgrades.put(attributeName, newValue);
            	sagaPlayer.modifyAttributes(attributeName, delta);
            }
        }
		
		
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
	 * Returns the raw level.
	 * 
	 * @return level
	 */
	public Short getRawLevel() {
		return level;
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

    /**
	 * Gets the time remaining for the ability.
	 * 
	 * @param ability ability
	 * @return time remaining. -1 if not found
	 */
	public Short getAbilityRemainingTime(Ability ability) {
		
		
		// Forward to saga player:
		return sagaPlayer.getAbilityRemainingTime(ability);

		
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
		
	}

	/**
	 * Right clicked.
	 *
	 * @param event event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent event) {
		
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

		
		
		

	}

	/**
	 * Activates an ability.
	 * 
	 * @param ability ability
	 */
	public abstract void abilityActivateEvent(Ability ability);
	
	/**
	 * Deactivates an ability.
	 * 
	 * @param ability ability index
	 */
	public abstract void abilityDeactivateEvent(Ability ability);
	
	
	/**
	 * Profession type.
	 * 
	 * @author andf
	 *
	 */
	public enum ProfessionType{
		
		NEITHER("neither"),
		PROFESSION("profession"),
		CLASS("class"),
		SPECIALIZATION("specialization"),
		ROLE("role");
		
		String name;
		
		/**
		 * Sets name.
		 * 
		 * @param name name
		 */
		private ProfessionType(String name) {
			this.name = name;
		}
		
		/**
		 * Returns the name for the type.
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}
		
		
	}
	
}
