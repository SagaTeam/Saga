package org.saga.constants;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.abilities.Ability;
import org.saga.attributes.Attribute;
import org.saga.attributes.Attribute.DisplayType;
import org.saga.professions.Profession;
import org.saga.professions.Profession.ProfessionType;

public class PlayerMessages {

	/**
	 * Info color.
	 */
	public static ChatColor errorColor= ChatColor.DARK_RED;
	
	/**
	 * Info color.
	 */
	public static ChatColor infoColor= ChatColor.YELLOW;
	
	/**
	 * Warning color.
	 */
	public static ChatColor warningColor= ChatColor.RED;
	
	/**
	 * Haven color.
	 */
	public static ChatColor havenColor= ChatColor.BLUE;
	
	/**
	 * Arena color.
	 */
	public static ChatColor arenaColor= ChatColor.RED;
	
	public static ChatColor normalColor = ChatColor.WHITE;
	
	public static ChatColor positiveHighlightColor = ChatColor.GREEN;
	
	public static ChatColor negativeHighlightColor = ChatColor.RED;
	
	public static ChatColor unavailableHighlightColor = ChatColor.DARK_GRAY;
	
	public static ChatColor normalTextColor1 = ChatColor.GOLD;
	
	public static ChatColor normalTextColor2 = ChatColor.YELLOW;
	
	public static ChatColor frameColor = ChatColor.DARK_GREEN;
	
	
	public static Effect ABILITY_ACTIVATE2_SOUND = Effect.CLICK1;
	
	public static Effect ABILITY_DEACTIVATE2_SOUND = Effect.CLICK2;
	
	/**
	 * First line of the error report to the player.
	 */
	public static String playerErrorMessage=errorColor+"Enouctered an error, please contact the owner!";
	
	/**
	 * Information will not be saved.
	 */
	public static String yourIformationWillNotBeSaved=errorColor+"Your player information will not be saved during this session.";
	
	
	
	/**
	 * Stamina lost in the normal way.
	 * 
	 * @param sagaPlayer
	 * @param usedStamina
	 */
	public static String staminaUsed(Double usedStamina, Double playerStamina, Double playerMaximumStamina) {

		return "Used "+usedStamina.intValue()+" stamina. ("+playerStamina.intValue()+"/"+playerMaximumStamina.intValue()+")";
		
	}
	
	public static String notEnoughStamina(Ability activatedAbility, Double playerStamina, Double playerMaximumStamina, Double staminaRequired) {

		return "You need "+staminaRequired.intValue()+" stamina to use "+activatedAbility.getAbilityName()+" ability. ("+playerStamina.intValue()+"/"+playerMaximumStamina.intValue()+")";
		
	}
	
	/**
	 * Stamina gained in the normal way.
	 * 
	 * @param sagaPlayer
	 * @param regeneratedStamina
	 */
	public static String staminaRegeneration(Double playerStamina, Double playerMaximumStamina) {

		return "Stamina regeneration. ("+playerStamina.intValue()+"/"+playerMaximumStamina.intValue()+")";
		
	}
	
	/**
	 * Health gained in the artificial way.
	 * 
	 * @param amount amount
	 */
	public static String healthGain(int amount) {

		return "Gained " + amount + " health.";
		
	}
	
	public static String abilitySelect(Ability selectedAbility) {

		return "Selected "+selectedAbility.getAbilityName()+" ability.";
				
	}
	
	public static String abilityActivate(Ability activatedAbility) {

		return "Activated "+activatedAbility.getAbilityName()+" ability.";
				
	}
	
	public static String invalidAbilityUse(Ability activatedAbility) {

		return "Can't use "+activatedAbility.getAbilityName()+" ability.";
				
	}
	
	public static String abilityDeactivate(Ability activatedAbility) {

		return "Deactivated "+activatedAbility.getAbilityName()+" ability.";
				
	}
	
	public static String abilitySelectNone() {

		return "No ability selected.";
				
	}
	
	public static String noAbilitiesAvailable(Ability ability) {
		
		return "Selected "+ability.getAbilityName()+" ability.";

	}
	
	public static String abilityAlreadyActive(Ability ability) {
		
		return ""+ability.getAbilityName().substring(0, 1).toUpperCase()+ability.getAbilityName().substring(1)+" ability is already active.";

	}
	
	public static String noAbilitiesAvailable() {
		
		return "You can't use any abilities yet.";

	}
	
	public static String youUsedAbilityOnEntity(Entity abilityUsedOn, Ability ability) {
		
		
		String usedOnName;
		if(abilityUsedOn instanceof Player){
			usedOnName = ((Player)abilityUsedOn).getName();
		}else{
			usedOnName = abilityUsedOn.getClass().getSimpleName().replace("Craft", "");
		}
		
		return "You used " + ability.getAbilityName()+ " ability on "+ usedOnName + ".";
		
		
	}
	
	public static String entityUsedAbilityOnYou(Entity abilityUser, Ability ability) {
		
		String userOnName;
		if(abilityUser instanceof Player){
			userOnName = ((Player)abilityUser).getName();
		}else{
			userOnName = capitalize(abilityUser.getClass().getSimpleName().replace("Craft", ""));
		}
		
		return userOnName + " used " + ability.getAbilityName()+ " on you.";
		
		
	}
	
	public static String usedAbility(Ability ability) {
		
		return "Used " + ability.getAbilityName()+ " ability.";
		
	}
	
	public static String cantUseAbilityOn(Entity abilityUsedOn, Ability ability) {
		
		
		String usedOnName;
		if(abilityUsedOn instanceof Player){
			usedOnName = ((Player)abilityUsedOn).getName();
		}else{
			usedOnName = abilityUsedOn.getClass().getSimpleName().replace("Craft", "");
		}
		
		return "Failed to use " + ability.getAbilityName()+ " ability on "+ usedOnName + ".";
		
		
	}
	
	
	public static String attributeIncreasedTo(String attributeRawName, Short increase){
		
		return capitalize(attributeRawName) + " attribute increased by " + increase + ".";
		
	}
	
	public static String attributeDecreasedTo(String attributeRawName, Short decrease){
		
		return capitalize(attributeRawName) + " attribute decreased by " + decrease + ".";
		
	}
	
	
	
	public static String meleeDamagedEntity(int damage, Entity entity) {

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getName();
		}else{
			name = entity.getClass().getSimpleName().toLowerCase().replace("craft", "");
		}
		return "You melee damaged " + name + " for " + new Double(new Double(damage)/2) + " damage.";
		
	}
	
	public static String gotMeleeDamagedByEntity(int damage, Entity entity) {

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getName();
		}else{
			name = entity.getClass().getSimpleName().toLowerCase().replace("craft", "");
		}
		return "You got melee damaged for " + new Double(new Double(damage)/2) + " by " + name + ".";
		
	}
	
	public static String projectileDamagedEntity(int damage, Entity entity) {

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getName();
		}else{
			name = entity.getClass().getSimpleName().toLowerCase().replace("craft", "");
		}
		return "You projectile damaged " + name + " for " + new Double(new Double(damage)/2) + " damage.";
		
	}
	
	public static String gotProjectileDamagedByEntity(int damage, Entity entity) {

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getName();
		}else{
			name = entity.getClass().getSimpleName().toLowerCase().replace("craft", "");
		}
		return "You got projectile damaged for " + new Double(new Double(damage)/2) + " by " + name + ".";
		
	}
	
	public static String experienceGain(Profession profession, int gain, int current, int required) {
		
		return capitalize(profession.getName()) + " profession got " + gain + " experience. (" +current+"/" + required+")";
		
	}

	public static String levelUp(Profession profession, int level) {
		
		return capitalize(profession.getName()) + " is now level " + level + ".";
		
	}
	
	private static String capitalize(String string) {

		if(string.length()>=1){
			return string.substring(0, 1).toUpperCase() + string.substring(1);
		}else{
			return string.toUpperCase();
		}
		
	}
	
	// Stats:
	public static String playerStats(SagaPlayer sagaPlayer) {
		
		
		ChatColor messageColor = PlayerMessages.normalColor;
		StringBuffer rString = new StringBuffer();
		ChatColor professionsColor = normalTextColor1;
		ChatColor attributesColor = normalTextColor2;
		
		// Physical:
		rString.append(ChatColor.DARK_RED);
		rString.append(new Double(sagaPlayer.getHealth()/2) + "/10.0hp");
		rString.append(messageColor);
		rString.append(" ");
		rString.append(ChatColor.DARK_GREEN);
		rString.append(new Double(sagaPlayer.getStamina().intValue()) + "/" + sagaPlayer.getMaximumStamina() + "st");
		rString.append(messageColor);
		
		rString.append("\n");
		
		// Professions:
		rString.append(professionShortenedStats(sagaPlayer, professionsColor));
		rString.append(messageColor);
		
		rString.append("\n");
		
		// Attributes:
		rString.append(getAttributesStats(sagaPlayer, attributesColor));
		rString.append(messageColor);
		
		// Add main color:
		rString.insert(0, messageColor);
		
		// Add frame:
		return frame("Player information", rString.toString(), messageColor);
		
		
	}
	
	private static String professionShortenedStats(SagaPlayer sagaPlayer, ChatColor messageColor) {
		
		
		StringBuffer rString = new StringBuffer();
		Profession[] allProfessions = sagaPlayer.getProfessions();
		ArrayList<String> classes = new ArrayList<String>();
		ArrayList<String> professions = new ArrayList<String>();
		ArrayList<String> specializations = new ArrayList<String>();
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> neither = new ArrayList<String>();
		ArrayList<String> currentProfs;
		String notChosen = "not chosen";
		
		// Add main color:
		rString.append(messageColor);
		
		// Loop trough and add all professions:
		for (int i = 0; i < allProfessions.length; i++) {
			if(allProfessions[i].getProfessionType().equals(ProfessionType.CLASS)){
				classes.add(getProfessionElement(allProfessions[i], messageColor));
			}else if(allProfessions[i].getProfessionType().equals(ProfessionType.PROFESSION)){
				professions.add(getProfessionElement(allProfessions[i], messageColor));
			}else if(allProfessions[i].getProfessionType().equals(ProfessionType.SPECIALIZATION)){
				specializations.add(getProfessionElement(allProfessions[i], messageColor));
			}else if(allProfessions[i].getProfessionType().equals(ProfessionType.ROLE)){
				roles.add(getProfessionElement(allProfessions[i], messageColor));
			}else{
				neither.add(getProfessionElement(allProfessions[i], messageColor));
			}
		}
		
		// Add classes:
		currentProfs = classes;
		if(currentProfs.size() == 1){
			rString.append("Class: ");
		}else if(currentProfs.size() > 1){
			rString.append("Classes: ");
		}else{
			rString.append("Class: " + notChosen);
		}
		for (int i = 0; i < currentProfs.size(); i++) {
			if(i != 0){
				rString.append(", ");
			}
			rString.append(currentProfs.get(i));
		}
		
		rString.append(" ");
		
		// Add professions:
		currentProfs = professions;
		if(currentProfs.size() == 1){
			rString.append("Profession: ");
		}else if(currentProfs.size() > 1){
			rString.append("Professions: ");
		}else{
			rString.append("Profession: " + notChosen);
		}
		for (int i = 0; i < currentProfs.size(); i++) {
			if(i != 0){
				rString.append(", ");
			}
			rString.append(currentProfs.get(i));
		}
		
		rString.append("\n");
		
		// Add Specializations:
		currentProfs = specializations;
		if(currentProfs.size() == 1){
			rString.append("Specializarion: ");
		}else if(currentProfs.size() > 1){
			rString.append("Specializarions: ");
		}else{
			rString.append("Specializarion: " + notChosen);
		}
		for (int i = 0; i < currentProfs.size(); i++) {
			if(i != 0){
				rString.append(", ");
			}
			rString.append(currentProfs.get(i));
		}
		
		rString.append(" ");
		
		// Add roles:
		currentProfs = roles;
		if(currentProfs.size() == 1){
			rString.append("Role: ");
		}else if(currentProfs.size() > 1){
			rString.append("Roles: ");
		}else{
			rString.append("Role: " + notChosen);
		}
		for (int i = 0; i < currentProfs.size(); i++) {
			if(i != 0){
				rString.append(", ");
			}
			rString.append(currentProfs.get(i));
		}
		
		// Add neither:
		if(neither.size() > 0){
			
			rString.append("\n");
			
			currentProfs = neither;
			if(currentProfs.size() == 1){
				rString.append("Other: ");
			}else if(currentProfs.size() > 1){
				rString.append("Other: ");
			}else{
				rString.append("Other: " + notChosen);
			}
			for (int i = 0; i < currentProfs.size(); i++) {
				if(i != 0){
					rString.append(", ");
				}
				rString.append(currentProfs.get(i));
			}
		}
		
		return rString.toString();
		
		
	}
	
	private static String getProfessionElement(Profession profession, ChatColor messageColor){
		
		return messageColor + profession.getName()+"("+profession.getLevel()+")";
		
	}

	private static String getAttributesStats(SagaPlayer sagaPlayer, ChatColor elementColor) {
		
		
		StringBuffer rString = new StringBuffer();
		ArrayList<String> offence = new ArrayList<String>();
		ArrayList<String> defense = new ArrayList<String>();
		ArrayList<String> other = new ArrayList<String>();
		Attribute[] attributes;
		ArrayList<String> distrAttributes;
		String attributeElement = "";
		
		// Loop trough attack:
		attributes = Saga.attributeInformation().attackAttributes;
		for (int i = 0; i < attributes.length; i++) {
			if(attributes[i].getDisplayType().equals(DisplayType.OFFENCE)){
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					offence.add(attributeElement);
			}else if(attributes[i].getDisplayType().equals(DisplayType.DEFENSE)){
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					defense.add(attributeElement);
			}else if(attributes[i].getDisplayType().equals(DisplayType.HIDDEN)){
				
				
			}else{
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					other.add(attributeElement);
			}
		}
		
		// Loop trough defense:
		attributes = Saga.attributeInformation().defenseAttributes;
		for (int i = 0; i < attributes.length; i++) {
			if(attributes[i].getDisplayType().equals(DisplayType.OFFENCE)){
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					offence.add(attributeElement);
			}else if(attributes[i].getDisplayType().equals(DisplayType.DEFENSE)){
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					defense.add(attributeElement);
			}else if(attributes[i].getDisplayType().equals(DisplayType.HIDDEN)){
				
				
			}else{
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					other.add(attributeElement);
			}
		}
		
		// Loop trough projectile shot:
		attributes = Saga.attributeInformation().projectileShotAttributes;
		for (int i = 0; i < attributes.length; i++) {
			if(attributes[i].getDisplayType().equals(DisplayType.OFFENCE)){
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					offence.add(attributeElement);
			}else if(attributes[i].getDisplayType().equals(DisplayType.DEFENSE)){
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					defense.add(attributeElement);
			}else if(attributes[i].getDisplayType().equals(DisplayType.HIDDEN)){
				
				
			}else{
				attributeElement = getAttributeElement(sagaPlayer, attributes[i], elementColor);
				if(attributeElement.length() != 0)
					other.add(attributeElement);
			}
		}
		
		// Add offense:
		distrAttributes = offence;
		if(distrAttributes.size() != 0 || true){
			if(rString.length() != 0){
				rString.append("\n");
			}
			rString.append("Offence: ");
			for (int i = 0; i < distrAttributes.size(); i++) {
				if(i != 0){
					rString.append(", ");
				}
				rString.append(distrAttributes.get(i));
			}
		}
		
		// Add defense:
		distrAttributes = defense;
		if(distrAttributes.size() != 0 || true){
			if(rString.length() != 0){
				rString.append("\n");
			}
			rString.append("Defense: ");
			for (int i = 0; i < distrAttributes.size(); i++) {
				if(i != 0){
					rString.append(", ");
				}
				rString.append(distrAttributes.get(i));
			}
		}
		
		// Add other:
		distrAttributes = other;
		if(distrAttributes.size() != 0){
			if(rString.length() != 0){
				rString.append("\n");
			}
			rString.append("Other: ");
			for (int i = 0; i < distrAttributes.size(); i++) {
				if(i != 0){
					rString.append(", ");
				}
				rString.append(distrAttributes.get(i));
			}
		}
		
		// Add main color:
		rString.insert(0, elementColor);
		
		return rString.toString();
		
		
	}

	private static String getAttributeElement(SagaPlayer sagaPlayer, Attribute attribute, ChatColor messageColor){
		
		
		Short upgrade = sagaPlayer.getAttributeUpgrade(attribute.getName());
		Short temporaryUpgrade =  sagaPlayer.getAttributeTemporaryUpgrade(attribute.getName());;
		ChatColor attributeColor = normalColor;
		
		// Ignore if the attribute is not present:
		if(temporaryUpgrade == 0 && upgrade == 0){
			return "";
		}
		
		// Highlight:
		if(temporaryUpgrade < 0){
			attributeColor = negativeHighlightColor;
		}else if(temporaryUpgrade > 0){
			attributeColor = positiveHighlightColor;
		}else{
			attributeColor = messageColor;
		}
		
		return attributeColor + attribute.getName(upgrade, temporaryUpgrade) + messageColor;
		
		
	}

	public static String professionStats(Profession profession) {

		
		StringBuffer rString = new StringBuffer();
		ChatColor messageColor = PlayerMessages.normalColor;
		ChatColor levelColor = ChatColor.GOLD;
		ChatColor abilitiesColor = ChatColor.YELLOW;
		
		// Level:
		rString.append(levelColor);
		rString.append("lvl"+profession.getLevel());
		rString.append(" ");
		rString.append(profession.getLevelExperience() + "/" + profession.getExperienceRequirement() + "exp");
		rString.append(messageColor);
		
		rString.append("\n");
		
		// Abilities:
		Ability[] abilities = profession.getAbilities();
		for (int i = 0; i < abilities.length; i++) {
			if(i != 0){
				rString.append("\n");
			}
			rString.append(getAbilityElement(profession, abilities[i], abilitiesColor));
		}
		rString.append(messageColor);
		
		// Add frame:
		return frame(profession.getName() + " " + profession.getProfessionType().getName(), rString.toString(), messageColor);
		
		
	}
	
	private static String getAbilityElement(Profession profession, Ability ability, ChatColor messageColor) {

		
		StringBuffer rString = new StringBuffer();
		ChatColor abilityColor = messageColor;
		
		// Add name:
		rString.append(ability.getAbilityName());
		
		// Add stamina if level is high enough, requirement otherwise:
		Short requiredLevel = ability.getLevelRequirement();
		Short level = profession.getLevel();
		if(level >= requiredLevel){
			rString.append("("+ability.calculateStaminaUse(level)+"st)");
		}else{
			rString.append("(lvl"+requiredLevel+")");
			abilityColor = unavailableHighlightColor;
		}
		
		return abilityColor + rString.toString() + messageColor;
		
		
	}
	
	// Professions:
	
	public static String invalidProfession(String profession){
		
		return profession +" is not a valid profession.";
		
	}
	
	// Attributes:
	public static String attackWasBlocked(){
		return "Your attack was blocked.";
	}
	
	public static String blockedAttack(){
		return "You blocked an attack.";
	}
	
	public static String attackWasDodged(){
		return "Your attack was dodged.";
	}
	
	public static String dodgedAttack(){
		return "You dodged an attack.";
	}
	
	// Administrator only:
	/**
	 * Message when unloading player information.
	 * Should be used only when debuging is true.
	 * Should also log the information.
	 * 
	 * @param playerName player name
	 * @return player message
	 */
	public static String loadingPlayerInformation(String playerName){
		return "Loading " + playerName +" player information.";
	}
	
	/**
	 * Message when unloading player information.
	 * Should be used only when debuging is true.
	 * Should also log the information.
	 * 
	 * @param playerName player name
	 * @return player message
	 */
	public static String unloadingPlayerInformation(String playerName){
		return "Unloading " + playerName +" player information.";
	}
	
	public static String nonExistantPlayer(String playerName){
		return "Player " + playerName +" doesn't exist.";
	}
	
	public static String invalidSetLevel(String level){
		return level + " is an invalid level. Level must be a number in the range 0-" + Saga.balanceInformation().maximumLevel + ".";
	}
	
	public static String setLevelTo(String playerName, Profession profession , Short level){
		return playerName + " "+ profession.getName() +" profession level set to " + level + ".";
	}
	
	public static String levelLimitReached(Short level) {

		
		if(level >= Saga.balanceInformation().maximumLevel){
			return "Maximum level reached. You can't go above "+Saga.balanceInformation().maximumLevel+".";
		}
		if(level <= 0){
			return "Minimum level reached. You can't go below 0.";
		}
		return "Level is in the bounds. Invalid method call for levelLimitReached.";
		
		
	}
	
	// Utility:
	public static void sendMultipleLines(String message, Player player) {

		
		String[] arrayMessage = Pattern.compile("\n").split(message);
		
		// Add lost colors after line brake:
		int lastColorIndex = -1;
		String colorString= "\u00A7";
		String color = "";
		for (int i = 1; i < arrayMessage.length; i++) {
			lastColorIndex = arrayMessage[i-1].lastIndexOf(colorString);
			if(lastColorIndex != -1 && (arrayMessage[i-1].length() - lastColorIndex - colorString.length()) >= 1 ){
				color = arrayMessage[i-1].substring(lastColorIndex, lastColorIndex + colorString.length()+1);
			}
			if(color.length() != 0){
				arrayMessage[i] = color + arrayMessage[i];
			}
			
		}
		
		for (int i = 0; i < arrayMessage.length; i++) {
			player.sendMessage(arrayMessage[i]);
		}
		
		
	}
	
	private static String frame(String frameName, String message, ChatColor messageColor) {

		
		int frameShift = 2;
		String frameHorisontal = "----------------------------------";
		
		StringBuffer rString = new StringBuffer();
		rString.append(frameColor);
		rString.append(frameHorisontal.substring(0, frameShift) + capitalize(frameName) + frameHorisontal.substring(frameShift + frameName.length()-2));
		rString.append("\n");
		rString.append(messageColor);
		rString.append(message);
		rString.append("\n");
		rString.append(frameColor);
		rString.append(frameHorisontal);
		rString.append(messageColor);
		
		return rString.toString();
		
		
	}
	
	
	
	
}
