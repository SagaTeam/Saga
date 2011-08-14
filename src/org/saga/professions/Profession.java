package org.saga.professions;


import java.util.ArrayList;
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
import org.saga.abilities.Ability.AbilityActivateType;
import org.saga.abilities.types.OnAbility;
import org.saga.abilities.types.OnBlockDamage;
import org.saga.abilities.types.OnDamagedLivingEntity;
import org.saga.abilities.types.OnGotDamagedByLivingEntity;
import org.saga.abilities.types.OnLeftClick;
import org.saga.abilities.types.OnRightClick;
import org.saga.abilities.types.OnActivateAbility;
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

	// Access:
	/**
	 * Saga player.
	 */
	transient protected SagaPlayer sagaPlayer= null;
	
	
	// Player information:
	/**
	 * Profession level.
	 */
	private Short level;

	/**
	 * Experience for the current level.
	 */
	private Integer levelExperience;

	
	// Abilities:
	/**
	 * All abilities.
	 */
	transient private Ability[] abilities;

	/**
	 * Active abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	/**
	 * Damaged living entities abilities.
	 */
	transient private ArrayList<OnDamagedLivingEntity> damagedLivingEntityAbilities;
	
	/**
	 * Damaged living entities abilities.
	 */
	transient private ArrayList<OnGotDamagedByLivingEntity> gotDamagedByLivingEntityAbilites;
	
	/**
	 * Left click interact abilities.
	 */
	transient private ArrayList<OnLeftClick> leftClickInteractAbilities;
	
	/**
	 * Right click interact abilities.
	 */
	transient private ArrayList<OnRightClick> rightClickInteractAbilities;
	
	/**
	 * Abilities that will be used on activate event.
	 */
	transient private ArrayList<OnActivateAbility> onActivateAbilities;
	
	/**
	 * Abilities that will be used on block damage.
	 */
	transient private ArrayList<OnBlockDamage> blockDamageAbilities;
	
	
	// Calculated:
	/**
	 * Experience requirement for the next level.
	 */
	transient private Integer experienceRequirement;

	
	// Attributes:
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
	
	/**
	 * Used by gson.
	 * 
	 */
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
		
		experienceRequirement = calculateExperienceRequirement(level);
		
		// Initiate attributes:
		modifyAttributeUpgrades(getRawLevel());
		
		// Retrieve abilities:
		abilities =  Saga.abilityInformation().getAbilities(getName());
		
		// Distribute abilities:
		damagedLivingEntityAbilities = new ArrayList<OnDamagedLivingEntity>();
		gotDamagedByLivingEntityAbilites = new ArrayList<OnGotDamagedByLivingEntity>();
		leftClickInteractAbilities = new ArrayList<OnLeftClick>();
		rightClickInteractAbilities = new ArrayList<OnRightClick>();
		onActivateAbilities = new ArrayList<OnActivateAbility>();
		blockDamageAbilities = new ArrayList<OnBlockDamage>();
		
		for (int i = 0; i < abilities.length; i++) {
			
			if(abilities[i] instanceof OnDamagedLivingEntity){
				damagedLivingEntityAbilities.add((OnDamagedLivingEntity) abilities[i]);
			}
			else if(abilities[i] instanceof OnGotDamagedByLivingEntity){
				gotDamagedByLivingEntityAbilites.add((OnGotDamagedByLivingEntity) abilities[i]);
			}
			else if(abilities[i] instanceof OnLeftClick){
				leftClickInteractAbilities.add((OnLeftClick) abilities[i]);
			}
			else if(abilities[i] instanceof OnRightClick){
				rightClickInteractAbilities.add((OnRightClick) abilities[i]);
			}
			else if(abilities[i] instanceof OnActivateAbility){
				onActivateAbilities.add((OnActivateAbility) abilities[i]);
			}
			else if(abilities[i] instanceof OnBlockDamage){
				blockDamageAbilities.add((OnBlockDamage) abilities[i]);
			}
			
		}
		
		// Active abilities:
		activeAbilities = new Boolean[abilities.length];
		for (int i = 0; i < activeAbilities.length; i++) {
			activeAbilities[i] = false;
		}
		
		
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
	public Ability[] getAbilities(){
		return abilities;
	}

	/**
	 * Checks if the ability is already active.
	 * 
	 * @param ability ability
	 * @return true, if active
	 */
	public boolean isAbilityActive(Ability ability){
		
		
		for (int i = 0; i < activeAbilities.length; i++) {
			if(abilities[i].equals(ability)){
				return activeAbilities[i];
			}
		}
		return false;
		
		
	}
	
	/**
	 * Checks if the ability is already active.
	 * 
	 * @param ability ability
	 * @return true, if active
	 */
	public boolean isAbilityActive(OnAbility ability){
		
		
		for (int i = 0; i < activeAbilities.length; i++) {
			if(abilities[i].equals(ability.getAbility())){
				return activeAbilities[i];
			}
		}
		return false;
		
		
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

		
		// Abilities:
		for (OnGotDamagedByLivingEntity ability : gotDamagedByLivingEntityAbilites) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Toggle or timed:
			else if(isAbilityActive(ability)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			
		}

		
	}

	/**
	 * Damaged a living entity.
	 *
	 * @param event event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent event) {

		
		// Abilities:
		for (OnGotDamagedByLivingEntity ability : gotDamagedByLivingEntityAbilites) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Toggle or timed:
			else if(isAbilityActive(ability)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			
		}

		
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

		
		// Abilities:
		for (OnLeftClick ability : leftClickInteractAbilities) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Toggle or timed:
			else if(isAbilityActive(ability)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			
		}

		
	}

	/**
	 * Right clicked.
	 *
	 * @param event event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent event) {

		
		// Abilities:
		for (OnRightClick ability : rightClickInteractAbilities) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Toggle or timed:
			else if(isAbilityActive(ability)){
				ability.use(getLevel(), sagaPlayer, this, event);
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

		
		// Abilities:
		for (OnBlockDamage ability : blockDamageAbilities) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Toggle or timed:
			else if(isAbilityActive(ability)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			
		}

		
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
	public void abilityActivateEvent(Ability ability2){

		
		// Activate if not a single use:
		if(!ability2.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
			for (int i = 0; i < abilities.length; i++) {
				if(abilities[i].equals(ability2)){
					activeAbilities[i] = true;
					return;
				}
			}
		}
		
		// Abilities:
		if(!(ability2 instanceof OnActivateAbility)){
			return;
		}
		OnActivateAbility ability = (OnActivateAbility) ability2;
		// Instantaneous:
		if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
			ability.use(getLevel(), sagaPlayer, this);
		}
		// Toggle or timed:
		else if(isAbilityActive(ability)){
			ability.use(getLevel(), sagaPlayer, this);
		}
			
			
		
	}
	
	/**
	 * Deactivates an ability.
	 * 
	 * @param ability ability index
	 */
	public void abilityDeactivateEvent(Ability ability){
		
		
		for (int i = 0; i < abilities.length; i++) {
			if(abilities[i].equals(ability)){
				activeAbilities[i] = false;
				return;
			}
		}
		
		
	}
	
	
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
