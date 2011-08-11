package org.saga;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternInitiator;
import org.saga.professions.*;
import org.saga.utility.WriterReader;
import org.saga.abilities.Ability;
import org.saga.attributes.Attribute;
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
	
	
	// Abilities:
	/**
	 * Ability manager.
	 */
	transient private PlayerAbilityManager abilityManager;
	
	
	// Attributes:
	/**
	 * Attributes. Key is the attribute simple class name and the value is attribute upgrade.
	 */
	private transient Hashtable<String, Short> attributes= new Hashtable<String, Short>();
	
	/**
	 * Temporary attribute raw names.
	 */
	transient private ArrayList<String> temporaryAttributeNames = new ArrayList<String>();
	
	/**
	 * Temporary attribute increase levels.
	 */
	transient private ArrayList<Short> temporaryAttributeUpgrades = new ArrayList<Short>();
	
	/**
	 * Temporary attribute increase times.
	 */
	transient private ArrayList<Integer> temporaryAttributeTimes = new ArrayList<Integer>();
	
	
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
		
		// Initiate ability manager:
		abilityManager = new PlayerAbilityManager(this, professions);
		
		
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
	 * Regenerates health. Doesen't send a message.
	 * 
	 * @param amount amount to regenerate
	 */
	public void gainHealth(int amount) {
		
		
		if(!isOnlinePlayer()){
			return;
		}
		int health = player.getHealth();
		
		if(health >= PlayerDefaults.maximumHealth){
			return;
		}
		if(health +amount >  PlayerDefaults.maximumHealth){
			amount = PlayerDefaults.maximumHealth - health;
		}
		sendMessage(PlayerMessages.healthGain(amount));

		
	}
	
	/**
	 * Regenerates health. Sends a message.
	 * 
	 * @param amount amount to regenerate
	 */
	public void regenerateHealth(int amount) {
		
		
		if(!isOnlinePlayer()){
			return;
		}
		int health = player.getHealth();
		
		if(health >= PlayerDefaults.maximumHealth){
			return;
		}
		health += amount;
		if(health >  PlayerDefaults.maximumHealth){
			health =  PlayerDefaults.maximumHealth;
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
	 * 
	 * @param location location
	 */
	public void moveToCentered(Location location) {
		
		moveTo(location.add(0.5, 0, 0.5));
	}
	
	/**
	 * Puts a player on the given blocks center.
	 * 
	 * @param locationBlock block the player will be placed on
	 */
	public void moveToBlockCentered(Block locationBlock) {
		moveToCentered(locationBlock.getRelative(BlockFace.UP).getLocation());
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
	public int calculatePlayerHorizontalDirection(){
		
		
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
		
		
		// Forward to ability manager:
		abilityManager.deactivateAbility(ability);
		
		
	}
	
	/**
	 * Gets the time remaining for the ability.
	 * 
	 * @param ability ability
	 * @return time remaining. -1 if not found
	 */
	public Short getAbilityRemainingTime(Ability ability) {
		
		
		// Forward to ability manager:
		return abilityManager.getAbilityRemainingTime(ability);

		
	}
	
	/**
	 * Modifies an attribute. Should be only used for permanent modifications by professions.
	 * 
	 * @param attributeRawName attribute raw name
	 * @param amount amount to modify
	 */
	public void modifyAttributes(String attributeRawName, Short amount){
		
		
		Short oldValue = attributes.get(attributeRawName);
		if(oldValue == null){
			oldValue = 0;
		}
		attributes.put(attributeRawName, new Integer(oldValue+amount).shortValue());
		
		
	}
	
	/**
	 * Temporary modifies an attribute.
	 * 
	 * @param attributeRawName attribute raw name
	 * @param amount amount to modify
	 * @param time time to remain active
	 */
	public void modifyTemporaryAttribute(String attributeRawName, Short amount, Integer time) {

		
		// Modify the list:
		modifyAttributes(attributeRawName, amount);
		
		// Add to temporary lists:
		temporaryAttributeNames.add(attributeRawName);
		temporaryAttributeUpgrades.add(amount);
		temporaryAttributeTimes.add(time);
		
		
		// Send message:
		if(amount > 0){
			sendMessage(PlayerMessages.attributeIncreasedTo(attributeRawName, amount));
		}
		else if(amount < 0){
			sendMessage(PlayerMessages.attributeDecreasedTo(attributeRawName, amount));
		}
		
		
	}
	
	/**
	 * Gets the upgrade level for the attribute.
	 * 
	 * @param attributeName attribute name
	 * @return attribute upgrade, 0 if none
	 */
	public Short getAttributeUpgrade(String attributeName) {

		
		Short upgrade = attributes.get(attributeName);
		if(upgrade == null){
			upgrade = 0;
		}
		return upgrade;
		
		
	}
	
	/**
	 * Gets the temporary upgrade level for the attribute.
	 * 
	 * @param attributeName attribute name
	 * @return attribute level, 0 if not found
	 */
	public Short getAttributeTemporaryUpgrade(String attributeName) {
		
		
		Short upgradeSize = 0;
		for (int i = 0; i < temporaryAttributeNames.size(); i++) {
			if(temporaryAttributeNames.get(i).equals(attributeName)){
				upgradeSize = (short) ( upgradeSize + temporaryAttributeUpgrades.get(i));
			}
		}
		return upgradeSize;

		
	}
	
	/**
	 * Tries to dodge.
	 * 
	 * @return true if dodge was a success.
	 */
	public boolean tryDodge() {

		// Works, but is annoying.
		if(!isOnlinePlayer()){
			return false;
		}
		
		int direction = calculatePlayerHorizontalDirection();
		Random random= new Random();
		System.out.println(player.getVelocity());
		int firstCord;
		int oppositeCord;
		Block playerBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		float yaw = player.getLocation().getYaw();
		float pitch = player.getLocation().getPitch();
		
		if(random.nextBoolean()){
			firstCord = 1;
			oppositeCord = -1;
		}else{
			firstCord = -1;
			oppositeCord = 1;	
		}
		
		if(direction == 0 || direction == 2){
			
			// First attempt:
			if( player.getNearbyEntities(firstCord, 0, 0).size() == 0 && canJumpTo(playerBlock.getRelative(firstCord, 0, 0)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(firstCord, 0, 0).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			// Second attempt:
			if( player.getNearbyEntities(oppositeCord, 0, 0).size() == 0 && canJumpTo(playerBlock.getRelative(oppositeCord, 0, 0)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(oppositeCord, 0, 0).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			return false;
			
		}
		
		if(direction == 1 || direction == 3){
			
			// First attempt:
			if( player.getNearbyEntities(0, 0, firstCord).size() == 0 && canJumpTo(playerBlock.getRelative(0, 0, firstCord)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(0, 0, firstCord).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			// Second attempt:
			if( player.getNearbyEntities(0, 0, oppositeCord).size() == 0 && canJumpTo(playerBlock.getRelative(0, 0, oppositeCord)) ){
				Location newLocation = playerBlock.getRelative(BlockFace.UP).getRelative(0, 0, oppositeCord).getLocation();
				newLocation.setYaw(yaw);
				newLocation.setPitch(pitch);
				moveToCentered(newLocation);
				return true;
			}
			return false;
			
		}
		
		return false;
		
		
	}
	
	/**
	 * Pushed away an entity from the player
	 * 
	 * @param entity entity
	 * @param speed speed
	 */
	public void pushAwayEntity(Entity entity, double speed) {

		
		// Ignore if the player isn't online:
		if(!isOnlinePlayer()){
			return;
		}
		
		// Get velocity unit vector:
		Vector unitVector = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
		
		// Set speed and push entity:
		entity.setVelocity(unitVector.multiply(speed));
		
		
	}
	
	
	/**
	 * Plays an effect for the player.
	 * 
	 * @param effect effect
	 * @param value effect value
	 */
	public void playEffect(Effect effect, int value) {

		
		// Ignore if the player isn't online:
		if(!isOnlinePlayer()){
			return;
		}
		
		player.playEffect(player.getEyeLocation(), effect, value);
		
		
	}
	
	
	/**
	 * Checks if the location can be jumped to. Requires a surface under feet.
	 * 
	 * @param block the player will be standing on
	 * @return true if can be jumped
	 */
	private static boolean canJumpTo(Block block) {
		
		
		if(BlockConstants.isTransparent(block.getType())){
			return false;
		}
		if(!BlockConstants.isTransparent(block.getRelative(BlockFace.UP).getType())){
			return false;
		}
		if(!BlockConstants.isTransparent(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType())){
			return false;
		}
		return true;
		
		
	}
	
	// Events:
	/**
	 * Got damaged by living entity event, using melee.
	 *
	 * @param event event
	 */
	public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent event) {

		
		// Attributes:
		Attribute[] defenseAttributes = Saga.attributeInformation().defenseAttributes;
		for (int i = 0; i < defenseAttributes.length; i++) {
			String attributeName = defenseAttributes[i].getName();
			defenseAttributes[i].use(getAttributeUpgrade(attributeName), this, event);
		}
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].gotDamagedByLivingEntityEvent(event);
		}
		sendMessage(PlayerMessages.gotMeleeDamagedByEntity(event.getDamage(), event.getDamager()));
		
		
	}

	/**
	 * Damaged a living entity, using melee.
	 *
	 * @param event event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent event) {
		
		
		// Attributes:
		Attribute[] attackAtributes = Saga.attributeInformation().attackAttributes;
		for (int i = 0; i < attackAtributes.length; i++) {
			String attributeName = attackAtributes[i].getName();
			attackAtributes[i].use(getAttributeUpgrade(attributeName), this, event);
		}
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].damagedLivingEntityEvent(event);
		}
		sendMessage(PlayerMessages.meleeDamagedEntity(event.getDamage(), event.getEntity()));
		
		
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
	 * @param event event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent event) {
		
		
		// Send to ability manager:
		abilityManager.leftClickInteractEvent(event);
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].leftClickInteractEvent(event);
		}
		
		
	}

	/**
	 * Right clicked.
	 *
	 * @param event event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent event) {
		
		
		// Send to ability manager:
		abilityManager.rightClickInteractEvent(event);
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].rightClickInteractEvent(event);
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
	 * @param tick tick number
	 */
	public void clockTickEvent(int tick) {
		
		
		// Stamina regeneration:
		naturalStaminaRegenerate();
		
		// Attribute temporary modification:
		for (int i = 0; i < temporaryAttributeNames.size(); i++) {
			Integer newTime = temporaryAttributeTimes.get(i) -1;
			temporaryAttributeTimes.set(i, newTime);
			// Remove if the time is up:
			if(newTime <=0){
				String removedName = temporaryAttributeNames.remove(i);
				Short removedLevel = temporaryAttributeUpgrades.remove(i);
				temporaryAttributeTimes.remove(i);
				modifyAttributes(removedName, (short) -removedLevel);
				// Send a message:
				if(removedLevel > 0){
					sendMessage(PlayerMessages.attributeIncreasedTo(removedName, removedLevel));
				}
				else if(removedLevel < 0){
					sendMessage(PlayerMessages.attributeDecreasedTo(removedName, removedLevel));
				}
				
			}
		}
		
		// Forward to all professions:
		for (int i = 0; i < professions.length; i++) {
			professions[i].clockTickEvent(tick);
		}
		
		// Send to ability manager:
		abilityManager.clockTickEvent(tick);
		
		
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
