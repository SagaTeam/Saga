package org.saga.constants;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.abilities.Ability;
import org.saga.professions.Profession;

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
	
	public static String abilitySelect(Ability selectedAbility) {

		return "Selected "+selectedAbility.getAbilityName()+" ability.";
				
	}
	
	public static String abilityActivate(Ability activatedAbility) {

		return "Activated "+activatedAbility.getAbilityName()+" ability.";
				
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
	
	public static String usedAbilityOnEntity(Entity abilityUsedOn, Ability ability) {
		
		
		String usedOnName;
		if(abilityUsedOn instanceof Player){
			usedOnName = ((Player)abilityUsedOn).getName();
		}else{
			usedOnName = abilityUsedOn.getClass().getSimpleName().replace("Craft", "");
		}
		
		return "Used " + ability.getAbilityName()+ " on "+ usedOnName + ".";
		
		
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
	
	public static String entityUsedAbilityOn(Entity abilityUser, Ability ability) {
		
		String userOnName;
		if(abilityUser instanceof Player){
			userOnName = ((Player)abilityUser).getName();
		}else{
			userOnName = abilityUser.getClass().getSimpleName().replace("Craft", "");
		}
		userOnName = userOnName.substring(0, 1).toUpperCase() + userOnName.substring(1);
		
		return userOnName + " used " + ability.getAbilityName()+ " on you.";
		
		
	}
	
	public static String damagedEntity(int damage, Entity entity) {

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getName();
		}else{
			name = entity.getClass().getSimpleName().toLowerCase().replace("craft", "");
		}
		return "You damaged " + name + " for " + new Double(new Double(damage)/2) + " damage.";
		
	}
	
	public static String gotDamagedByEntity(int damage, Entity entity) {

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getName();
		}else{
			name = entity.getClass().getSimpleName().toLowerCase().replace("craft", "");
		}
		return "You got damaged for " + new Double(new Double(damage)/2) + " by " + name + ".";
		
	}
	
	public static String experienceGain(Profession profession, int gain, int current, int required) {
		
		return capitalize(profession.getProfessionName()) + " profession got " + gain + " experience. (" +current+"/" + required+")";
		
	}

	public static String levelUp(Profession profession, int level) {
		
		return capitalize(profession.getProfessionName()) + " is now level " + level + ".";
		
	}
	
	private static String capitalize(String string) {

		if(string.length()>=1){
			return string.substring(0, 1).toUpperCase() + string.substring(1);
		}else{
			return string.toUpperCase();
		}
		
	}
	
	public static String playerStats(SagaPlayer sagaPlayer) {
		
		ChatColor defaultColor = ChatColor.WHITE;
		
		String frameHorisontal = "--------------------------------";
		
		String rString = "Player information";
		int frameShift = 2;
		rString = frameHorisontal.substring(0, frameShift) + rString + frameHorisontal.substring(frameShift + rString.length()-2)+ "\n";
		rString += new Double(sagaPlayer.getHealth())/2 + "/10hp " + sagaPlayer.getStamina() + "/" + sagaPlayer.getMaximumStamina() + "st"+ "\n";
		rString += "Professions: ";
		for (int i = 0; i < sagaPlayer.getProfessionCount(); i++) {
			if(sagaPlayer.isProfessionSelected(i)){
				if(i!=0){
					rString += ", ";
				}
				rString += "lvl" + sagaPlayer.getProfessions(i).getLevel() + " " + sagaPlayer.getProfessions(i).getProfessionName();
			}
		}
		rString += "\n" + frameHorisontal;
		
		return defaultColor + rString;
		
		
	}

	
	public static String professionStats(Profession profession) {

		ChatColor defaultColor = ChatColor.WHITE;
		ChatColor levelNotHighEnoghColor = ChatColor.DARK_GRAY;
		ChatColor abilityActiveColor = ChatColor.DARK_GREEN;
		String frameHorisontal = "--------------------------------";
		
		String rString = "";
		rString += capitalize(profession.getProfessionName() + " profession");
		int frameShift = 2;
		rString = frameHorisontal.substring(0, frameShift) + rString + frameHorisontal.substring(frameShift + rString.length()-2);
		
		
		
		Short level = profession.getLevel();
		rString += "\n"+"lvl" + level +" with " + profession.getLevelExperience() + "/" + profession.getExperienceRequirement() + "exp";
		for (int i = 0; i < profession.getAbilityCount(); i++) {
			rString += "\n";
			
			String profLine = profession.getAbilityName(i);
			ChatColor activeColor = defaultColor;
			
			if(profession.isAbilityActive(i)){
				activeColor = abilityActiveColor;
			}
			
			if(profession.getAbilityLevelRequirement(i) > level){
				activeColor = levelNotHighEnoghColor;
				profLine += " (requires level "+profession.getAbilityLevelRequirement(i)+")";
			}else{
				profLine += ": " + Math.ceil(profession.getAbilityStaminaUse(i))+"st";
			}
			rString += activeColor + profLine;
			
		}
		rString += defaultColor;
		rString += "\n"+frameHorisontal;
		
		return rString;
		
		
	}
	
	public static String invalidProfession(String profession){
		
		return profession +" is not a valid profession.";
		
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
		return playerName + " "+ profession.getProfessionName() +" profession level set to " + level + ".";
	}
	
	// Utility:
	public static void sendMultipleLines(String message, Player player) {

		
		String[] arrayMessage = Pattern.compile("\n").split(message);
		for (int i = 0; i < arrayMessage.length; i++) {
			player.sendMessage(arrayMessage[i]);
		}
		
		
	}
	
	
	
	
	
	
}
