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
import org.saga.abilities.types.OnDamagedEntity;
import org.saga.abilities.types.OnGotDamagedByEntity;
import org.saga.abilities.types.OnLeftClick;
import org.saga.abilities.types.OnRightClick;
import org.saga.abilities.types.OnActivateAbility;
import org.saga.config.AttributeConfiguration;
import org.saga.config.BalanceConfiguration;
import org.saga.config.ProfessionConfiguration;
import org.saga.config.ProfessionConfiguration.InvalidProfessionException;
import org.saga.config.ProfessionConfiguration.ProfessionDefinition;
import org.saga.constants.PlayerDefaults;
import org.saga.constants.PlayerMessages;

public class Profession {

	
	// General:
	/**
	 * Class name used by the loader
	 */
	@SuppressWarnings("unused")
	private String _className;
	
	/**
	 * Profession name.
	 */
	private String professionName;

	/**
	 * Contains all information needed for the profession.
	 */
	transient private ProfessionDefinition professionDefinition;
	
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
	 * Active abilities.
	 */
	transient private Boolean[] activeAbilities;
	
	/**
	 * Damaged living entities abilities.
	 */
	transient private ArrayList<OnDamagedEntity> damagedEntityAbilities;
	
	/**
	 * Damaged living entities abilities.
	 */
	transient private ArrayList<OnGotDamagedByEntity> gotDamagedByEntityAbilites;
	
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
	@SuppressWarnings("unused")
	private Profession() {
	}
	
	/**
	 * Goes trough all the fields and makes sure everything has been set after gson load.
	 * If not, it fills the field with defaults.
	 * @throws InvalidProfessionException thrown when the profession has an invalid name
	 */
	public void complete() throws InvalidProfessionException {
		
		
		// Fields:
		if(level==null){
			level = PlayerDefaults.level;
			Saga.info("Setting default value for profession level.", sagaPlayer.getName());
		}
		if(levelExperience==null){
			levelExperience = PlayerDefaults.levelExperience;
			Saga.info("Setting default value for profession levelExperience.", sagaPlayer.getName());
		}
		
		// Initialize dependent fields:
		experienceRequirement = calculateExperienceRequirement(level);
		
		// Initiate attributes:
		modifyAttributeUpgrades(getRawLevel());
		
		// Retrieve definition and abilities:
		professionDefinition = ProfessionConfiguration.getConfig().getDefinition(getName());
		if(professionDefinition == null){
			throw new InvalidProfessionException(getName());
		}
		
		// Distribute abilities:
		damagedEntityAbilities = new ArrayList<OnDamagedEntity>();
		gotDamagedByEntityAbilites = new ArrayList<OnGotDamagedByEntity>();
		leftClickInteractAbilities = new ArrayList<OnLeftClick>();
		rightClickInteractAbilities = new ArrayList<OnRightClick>();
		onActivateAbilities = new ArrayList<OnActivateAbility>();
		blockDamageAbilities = new ArrayList<OnBlockDamage>();
		
		Ability[] abilities = professionDefinition.getAbilities();
		for (int i = 0; i < abilities.length; i++) {
			
			if(abilities[i] instanceof OnDamagedEntity){
				damagedEntityAbilities.add((OnDamagedEntity) abilities[i]);
			}
			else if(abilities[i] instanceof OnGotDamagedByEntity){
				gotDamagedByEntityAbilites.add((OnGotDamagedByEntity) abilities[i]);
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
	 * Wraps all required variables.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void setAccess(SagaPlayer sagaPlayer) {
		
		this.sagaPlayer= sagaPlayer;
		
		
	}
	
	/**
	 * Prepares for the removal of the profession from the player.
	 * 
	 */
	public void prepareForRemoval() {

		
		// Remove upgrades:
		Enumeration<String> allAttributeNames= attributeUpgrades.keys();
		while ( allAttributeNames.hasMoreElements() ) {
            String attributeName = allAttributeNames.nextElement();
            Short attributeUpgrade = (short) -attributeUpgrades.get(attributeName);
            sagaPlayer.modifyAttributes(attributeName, attributeUpgrade);
		}
            
            
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
	public ProfessionType getProfessionType(){
		return professionDefinition.getType();
	}
	
	/**
	 * Returns all abilities for the profession.
	 * 
	 * @return all abilities
	 */
	public Ability[] getAbilities(){
		return professionDefinition.getAbilities();
	}

	/**
	 * Checks if the ability is already active.
	 * 
	 * @param ability ability
	 * @return true, if active
	 */
	public boolean isAbilityActive(Ability ability){
		
		
		for (int i = 0; i < activeAbilities.length; i++) {
			if(professionDefinition.getAbilities()[i].equals(ability)){
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
			if(professionDefinition.getAbilities()[i].equals(ability.getAbility())){
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
	public Material[] getAbilityScrollMaterials(){
		return professionDefinition.getMaterials();
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
		Hashtable<String, Short[]> upgrades = AttributeConfiguration.getConfig().getAttributeUpgrades(getName());
		
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

		return level * BalanceConfiguration.getConfig().experienceSlope * level + BalanceConfiguration.getConfig().experienceIntercept;

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
	public void gotDamagedByEntityEvent(EntityDamageByEntityEvent event) {

		
		// Abilities:
		for (OnGotDamagedByEntity ability : gotDamagedByEntityAbilites) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Trigger:
			else if(ability.getActivateType().equals(AbilityActivateType.TRIGGER) && isAbilityActive(ability)){
				sagaPlayer.deactivateAbility(ability.getAbility());
				ability.use(level, sagaPlayer, this, event);
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
	public void damagedEntityEvent(EntityDamageByEntityEvent event) {

		
		// Abilities:
		for (OnDamagedEntity ability : damagedEntityAbilities) {
			
			// Check if active:
			// Instantaneous:
			if(ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
				ability.use(getLevel(), sagaPlayer, this, event);
			}
			// Trigger:
			else if(ability.getActivateType().equals(AbilityActivateType.TRIGGER) && isAbilityActive(ability)){
				sagaPlayer.deactivateAbility(ability.getAbility());
				ability.use(level, sagaPlayer, this, event);
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
			// Trigger:
			else if(ability.getActivateType().equals(AbilityActivateType.TRIGGER) && isAbilityActive(ability)){
				sagaPlayer.deactivateAbility(ability.getAbility());
				ability.use(level, sagaPlayer, this, event);
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
			// Trigger:
			else if(ability.getActivateType().equals(AbilityActivateType.TRIGGER) && isAbilityActive(ability)){
				sagaPlayer.deactivateAbility(ability.getAbility());
				ability.use(level, sagaPlayer, this, event);
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
			// Trigger:
			else if(ability.getActivateType().equals(AbilityActivateType.TRIGGER) && isAbilityActive(ability)){
				sagaPlayer.deactivateAbility(ability.getAbility());
				ability.use(level, sagaPlayer, this, event);
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
	 * @param tick tick number
	 */
	public void clockTickEvent(int tick) {

		
		
		

	}

	/**
	 * Activates an ability.
	 * 
	 * @param ability ability
	 */
	public void abilityActivateEvent(Ability ability){

		
		// Activate if not a single use:
		if(!ability.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
			for (int i = 0; i < professionDefinition.getAbilities().length; i++) {
				if(professionDefinition.getAbilities()[i].equals(ability)){
					activeAbilities[i] = true;
					return;
				}
			}
		}
		
		// Abilities:
		if(!(ability instanceof OnActivateAbility)){
			return;
		}
		OnActivateAbility onActivateAbility = (OnActivateAbility) ability;
		// Instantaneous:
		if(onActivateAbility.getActivateType().equals(AbilityActivateType.INSTANTANEOUS)){
			onActivateAbility.use(getLevel(), sagaPlayer, this);
		}
		// Trigger:
		else if(onActivateAbility.getActivateType().equals(AbilityActivateType.TRIGGER)){
			sagaPlayer.deactivateAbility(onActivateAbility.getAbility());
			onActivateAbility.use(level, sagaPlayer, this);
		}
		// Toggle or timed:
		else if(isAbilityActive(onActivateAbility)){
			onActivateAbility.use(getLevel(), sagaPlayer, this);
		}
			
			
		
	}
	
	/**
	 * Deactivates an ability.
	 * 
	 * @param ability ability index
	 */
	public void abilityDeactivateEvent(Ability ability){
		
		
		for (int i = 0; i < professionDefinition.getAbilities().length; i++) {
			if(professionDefinition.getAbilities()[i].equals(ability)){
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
		
		INVALID("invalid"),
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
