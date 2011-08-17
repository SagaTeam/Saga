/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.ArrayList;

import org.saga.config.BalanceConfiguration;
import org.saga.constants.PlayerMessages;
import org.saga.exceptions.SagaPlayerNotLoadedException;
import org.saga.professions.Profession;
import org.sk89q.*;

/**
 *
 * @author Cory
 */
public class SagaCommands {

    @Command(
        aliases = {"saga","sagaCommandAlias"},
        usage = "",
        flags = "",
        desc = "TODO: ",
        min = 0,
        max = 0
    )
    @CommandPermissions({"saga.common.admin"})
    public static void saga(CommandContext args, Saga plugin, SagaPlayer player) {


    }

    @Command(
        aliases = {"sagaAdmin","sagaAdminCommandAlias"},
        usage = "",
        flags = "",
        desc = "TODO: ",
        min = 0,
        max = 0
    )
    @CommandPermissions({"saga.admin.admin"})
    public static void admin(CommandContext args, Saga plugin, SagaPlayer player) {


    }
    
    @Command(
            aliases = {"stats"},
            usage = " or /stats ProfessionName",
            flags = "",
            desc = "Shows player stats.",
            min = 0,
            max = 1
        )
    @CommandPermissions({"saga.user"})
    public static void myStats(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

    	
    	if(args.argsLength()==0){
    		
    		sagaPlayer.sendMessage(PlayerMessages.playerStats(sagaPlayer));
    		
    	}else{
    		ArrayList<Profession> professions = sagaPlayer.getProfessions();
    		for (int i = 0; i < professions.size(); i++) {
				if(professions.get(i).getName().equalsIgnoreCase(args.getString(0))){
					sagaPlayer.sendMessage(PlayerMessages.professionStats(sagaPlayer, professions.get(i)));
					break;
				}
				if(i == professions.size() - 1){
					sagaPlayer.sendMessage(PlayerMessages.professionInvalid(args.getString(0)));
				}
			}
    		if(professions.size() == 0){
    			sagaPlayer.sendMessage(PlayerMessages.professionsNotAvailable());
    		}
    		
    	}

    	
    }

    @Command(
            aliases = {"statsother"},
            usage = " PlayerName or /statsother PlayerName ProfessionName",
            flags = "",
            desc = "Shows other player stats.",
            min = 1,
            max = 2
        )
    @CommandPermissions({"saga.admin.statsother"})
    public static void otherStats(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	// Check if the player is loaded and exists:
    	SagaPlayer otherSagaPlayer;
    	boolean loadedPlayer = false;
    	if(!plugin.isSagaPlayerLoaded(args.getString(0))){
    		if(!plugin.isSagaPlayerExistant(args.getString(0))){
    			sagaPlayer.sendMessage(PlayerMessages.nonExistantPlayer(args.getString(0)));
    			return;
    		}
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.loadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.loadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.loadSagaPlayer(args.getString(0));
    		loadedPlayer = true;
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use statsother command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}else{
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use statsother command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}
    	
    	
    	// Execute command:
    	if(args.argsLength()==1){
    		
    		sagaPlayer.sendMessage(PlayerMessages.playerStats(otherSagaPlayer));
    		
    	}else{
    		ArrayList<Profession> otherProfessions = otherSagaPlayer.getProfessions();
    		for (int i = 0; i < otherProfessions.size(); i++) {
				if(otherProfessions.get(i).getName().equalsIgnoreCase(args.getString(1))){
					sagaPlayer.sendMessage(PlayerMessages.professionStats(otherSagaPlayer, otherProfessions.get(i)));
					break;
				}
				if(i == otherProfessions.size() - 1){
					sagaPlayer.sendMessage(PlayerMessages.professionInvalid(args.getString(1)));
				}
			}
    		if(otherProfessions.size() == 0){
    			sagaPlayer.sendMessage(PlayerMessages.professionsNotAvailable());
    		}
    	}
    	
    	// Unload player if needed:
    	if(!otherSagaPlayer.isOnlinePlayer() && loadedPlayer){
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.unloadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.unloadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.unloadSagaPlayer(args.getString(0));
    	}
    	
    	
    	
    }

    @Command(
            aliases = {"setlevel"},
            usage = " PlayerName ProfessionName LevelValue",
            flags = "",
            desc = "Sets other player profession level.",
            min = 3,
            max = 3
        )
    @CommandPermissions({"saga.admin.setlevel"})
    public static void setLevel(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	// Return if level not correct:
    	Short level;
    	try {
    		level = new Integer(args.getInteger(2)).shortValue();
		} catch (NumberFormatException e) {
			sagaPlayer.sendMessage(PlayerMessages.invalidSetLevel(args.getString(2)));
			return;
		}
    	
    	if(level > BalanceConfiguration.getConfig().maximumLevel || level < 0){
			sagaPlayer.sendMessage(PlayerMessages.invalidSetLevel(level + ""));
    		return;
		}
    	
    	
    	// Check if the player is loaded and exists:
    	SagaPlayer otherSagaPlayer;
    	boolean loadedPlayer = false;
    	if(!plugin.isSagaPlayerLoaded(args.getString(0))){
    		if(!plugin.isSagaPlayerExistant(args.getString(0))){
    			sagaPlayer.sendMessage(PlayerMessages.nonExistantPlayer(args.getString(0)));
    			return;
    		}
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.loadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.loadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.loadSagaPlayer(args.getString(0));
    		loadedPlayer = true;
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use statsother command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}else{
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use statsother command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}
    	
    	// Execute the command:
    	ArrayList<Profession> otherProfessions = otherSagaPlayer.getProfessions();
    	for (int i = 0; i < otherProfessions.size(); i++) {
			if(otherProfessions.get(i).getName().equalsIgnoreCase(args.getString(1))){
				otherProfessions.get(i).setLevel(level);
				sagaPlayer.sendMessage(PlayerMessages.setLevelTo(otherSagaPlayer.getName(), otherProfessions.get(i), level));
				Saga.info(PlayerMessages.setLevelTo(otherSagaPlayer.getName(), otherProfessions.get(i), level) , sagaPlayer.getName());
				break;
			}
			if(i == otherProfessions.size() - 1){
				sagaPlayer.sendMessage(PlayerMessages.professionInvalid(args.getString(1)));
			}
		}
    	if(otherProfessions.size() == 0){
			sagaPlayer.sendMessage(PlayerMessages.professionsNotAvailable());
		}
    	
    	// Unload player if needed:
    	if(!otherSagaPlayer.isOnlinePlayer() && loadedPlayer){
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.unloadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.unloadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.unloadSagaPlayer(args.getString(0));
    	}
    	
    	
    	
    }
    
    @Command(
            aliases = {"levelup"},
            usage = " profession",
            flags = "",
            desc = "Levels up a profession.",
            min = 1,
            max = 1
        )
    @CommandPermissions({"saga.admin.setlevel"})
    public static void levelup(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	ArrayList<Profession> professions = sagaPlayer.getProfessions();
    	for (int i = 0; i < professions.size(); i++) {
			if(professions.get(i).getName().equalsIgnoreCase(args.getString(0))){
				Short level = (short) (professions.get(i).getLevel() + 1);
				if(level < 0 || level > BalanceConfiguration.getConfig().maximumLevel){
					sagaPlayer.sendMessage(PlayerMessages.levelLimitReached(level));
					return;
				}
				professions.get(i).setLevel(level);
				sagaPlayer.sendMessage(PlayerMessages.setLevelTo(sagaPlayer.getName(), professions.get(i), level));
				Saga.info(PlayerMessages.setLevelTo(sagaPlayer.getName(), professions.get(i), level) , sagaPlayer.getName());
				break;
			}
			if(i == professions.size() - 1){
				sagaPlayer.sendMessage(PlayerMessages.professionInvalid(args.getString(0)));
			}
		}
    	if(professions.size() == 0){
			sagaPlayer.sendMessage(PlayerMessages.professionsNotAvailable());
		}

	}
    
    @Command(
            aliases = {"leveldown"},
            usage = " profession",
            flags = "",
            desc = "Levels down a profession.",
            min = 1,
            max = 1
        )
    @CommandPermissions({"saga.admin.setlevel"})
    public static void leveldown(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	ArrayList<Profession> professions = sagaPlayer.getProfessions();
    	for (int i = 0; i < professions.size(); i++) {
			if(professions.get(i).getName().equalsIgnoreCase(args.getString(0))){
				Short level = (short) (professions.get(i).getLevel() - 1);
				if(level < 0 || level > BalanceConfiguration.getConfig().maximumLevel){
					sagaPlayer.sendMessage(PlayerMessages.levelLimitReached(level));
					return;
				}
				professions.get(i).setLevel(level);
				sagaPlayer.sendMessage(PlayerMessages.setLevelTo(sagaPlayer.getName(), professions.get(i), level));
				Saga.info(PlayerMessages.setLevelTo(sagaPlayer.getName(), professions.get(i), level) , sagaPlayer.getName());
				break;
			}
			if(i == professions.size()-1){
				sagaPlayer.sendMessage(PlayerMessages.professionInvalid(args.getString(0)));
			}
		}
    	if(professions.size() == 0){
			sagaPlayer.sendMessage(PlayerMessages.professionsNotAvailable());
		}
    	

	}
    
    @Command(
            aliases = {"addprofessionother", "addprofother"},
            usage = " PlayerName ProfessionName",
            flags = "",
            desc = "Adds a profession to a player.",
            min = 2,
            max = 2
        )
    @CommandPermissions({"saga.admin.addProfessionOther"})
    public static void addProfessionOther(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	// Check if the player is loaded and exists:
    	SagaPlayer otherSagaPlayer;
    	boolean loadedPlayer = false;
    	if(!plugin.isSagaPlayerLoaded(args.getString(0))){
    		if(!plugin.isSagaPlayerExistant(args.getString(0))){
    			sagaPlayer.sendMessage(PlayerMessages.nonExistantPlayer(args.getString(0)));
    			return;
    		}
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.loadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.loadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.loadSagaPlayer(args.getString(0));
    		loadedPlayer = true;
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use otherAddProfession command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}else{
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use otherAddProfession command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}
    	
    	// Execute command:
    	String name = args.getString(1);
    	if(otherSagaPlayer.canAddProfession(name)){
    		otherSagaPlayer.addProfession(name);
    		sagaPlayer.sendMessage(PlayerMessages.professionsAdded(name));
    		Saga.info("Added a profession named " + name + " to " + otherSagaPlayer.getName() + ".", sagaPlayer.getName());
    	}else{
    		sagaPlayer.sendMessage(PlayerMessages.professionCantBeAdded(name));
    	}
    	
    	// Unload player if needed:
    	if(!otherSagaPlayer.isOnlinePlayer() && loadedPlayer){
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.unloadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.unloadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.unloadSagaPlayer(args.getString(0));
    	}
    	
    	
    	
    }
    
    @Command(
            aliases = {"addprofession", "addprof"},
            usage = " ProfessionName",
            flags = "",
            desc = "Adds a profession.",
            min = 1,
            max = 1
        )
    @CommandPermissions({"saga.user.addProfessionOther"})
    public static void addProfession(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	// Execute command:
    	String name = args.getString(0);
    	if(sagaPlayer.canAddProfession(name)){
    		sagaPlayer.addProfession(name);
    		sagaPlayer.sendMessage(PlayerMessages.professionsAdded(name));
    		Saga.info("Added a profession named " + name + " to " + sagaPlayer.getName() + ".", sagaPlayer.getName());
    	}else{
    		sagaPlayer.sendMessage(PlayerMessages.professionCantBeAdded(name));
    	}
    	
    	
    }
    
    @Command(
            aliases = {"removeprofessionother", "remprofother"},
            usage = " PlayerName ProfessionName",
            flags = "",
            desc = "Removes a profession from a player.",
            min = 2,
            max = 2
        )
    @CommandPermissions({"saga.admin.addProfessionOther"})
    public static void removeProfessionOther(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	// Check if the player is loaded and exists:
    	SagaPlayer otherSagaPlayer;
    	boolean loadedPlayer = false;
    	if(!plugin.isSagaPlayerLoaded(args.getString(0))){
    		if(!plugin.isSagaPlayerExistant(args.getString(0))){
    			sagaPlayer.sendMessage(PlayerMessages.nonExistantPlayer(args.getString(0)));
    			return;
    		}
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.loadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.loadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.loadSagaPlayer(args.getString(0));
    		loadedPlayer = true;
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use otherAddProfession command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}else{
    		try {
				otherSagaPlayer = plugin.getSagaPlayer(args.getString(0));
			} catch (SagaPlayerNotLoadedException e) {
				Saga.severe("Failed to use otherAddProfession command because the target player was not loaded.", args.getString(0));
				return;
			}
    	}
    	
    	// Execute command:
    	String name = args.getString(1);
    	if(otherSagaPlayer.canRemoveProfession(name)){
    		otherSagaPlayer.removeProfession(name);
    		sagaPlayer.sendMessage(PlayerMessages.professionsRemoved(name));
    		Saga.info("Removed a profession named " + name + " from " + otherSagaPlayer.getName() + ".", sagaPlayer.getName());
    	}else{
    		sagaPlayer.sendMessage(PlayerMessages.professionCantBeRemoved(name));
    	}
    	
    	// Unload player if needed:
    	if(!otherSagaPlayer.isOnlinePlayer() && loadedPlayer){
    		if(Saga.debuging()) sagaPlayer.sendMessage(PlayerMessages.unloadingPlayerInformation(args.getString(0)));
    		Saga.info(PlayerMessages.unloadingPlayerInformation(args.getString(0)), sagaPlayer.getName());
    		plugin.unloadSagaPlayer(args.getString(0));
    	}
    	
    	
    	
    }
    
    @Command(
            aliases = {"removeprofession", "remprof"},
            usage = " PlayerName",
            flags = "",
            desc = "Removes a profession.",
            min = 1,
            max = 1
        )
    @CommandPermissions({"saga.user.addProfessionOther"})
    public static void removeProfession(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	// Execute command:
    	String name = args.getString(0);
    	if(sagaPlayer.canRemoveProfession(name)){
    		sagaPlayer.removeProfession(name);
    		sagaPlayer.sendMessage(PlayerMessages.professionsRemoved(name));
    		Saga.info("Removed a profession named " + name + " from himself.", sagaPlayer.getName());
    	}else{
    		sagaPlayer.sendMessage(PlayerMessages.professionCantBeRemoved(name));
    	}
    	
    	
    }
    
    
    
}
