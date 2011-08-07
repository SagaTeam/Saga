/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import org.saga.constants.PlayerMessages;
import org.saga.exceptions.SagaPlayerNotLoadedException;
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
    		for (int i = 0; i < sagaPlayer.getProfessionCount(); i++) {
				if(sagaPlayer.getProfessions(i).getProfessionName().equalsIgnoreCase(args.getString(0))){
					sagaPlayer.sendMessage(PlayerMessages.professionStats(sagaPlayer.getProfessions(i)));
					break;
				}
				if(i==sagaPlayer.getProfessionCount()-1){
					sagaPlayer.sendMessage(PlayerMessages.invalidProfession(args.getString(0)));
				}
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
    		for (int i = 0; i < otherSagaPlayer.getProfessionCount(); i++) {
				if(otherSagaPlayer.getProfessions(i).getProfessionName().equalsIgnoreCase(args.getString(1))){
					sagaPlayer.sendMessage(PlayerMessages.professionStats(otherSagaPlayer.getProfessions(i)));
					break;
				}
				if(i==otherSagaPlayer.getProfessionCount()-1){
					sagaPlayer.sendMessage(PlayerMessages.invalidProfession(args.getString(1)));
				}
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
    	
    	if(level > Saga.balanceInformation().maximumLevel || level < 0){
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
    	for (int i = 0; i < otherSagaPlayer.getProfessionCount(); i++) {
			if(otherSagaPlayer.getProfessions(i).getProfessionName().equalsIgnoreCase(args.getString(1))){
				otherSagaPlayer.getProfessions(i).setLevel(level);
				sagaPlayer.sendMessage(PlayerMessages.setLevelTo(otherSagaPlayer.getName(), otherSagaPlayer.getProfessions(i), level));
				Saga.info(PlayerMessages.setLevelTo(otherSagaPlayer.getName(), otherSagaPlayer.getProfessions(i), level) , sagaPlayer.getName());
				break;
			}
			if(i==otherSagaPlayer.getProfessionCount()-1){
				sagaPlayer.sendMessage(PlayerMessages.invalidProfession(args.getString(1)));
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
            aliases = {"levelup"},
            usage = " profession",
            flags = "",
            desc = "Levels up a profession.",
            min = 1,
            max = 1
        )
    @CommandPermissions({"saga.admin.setlevel"})
    public static void levelup(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
    	
    	
    	for (int i = 0; i < sagaPlayer.getProfessionCount(); i++) {
			if(sagaPlayer.getProfessions(i).getProfessionName().equalsIgnoreCase(args.getString(0))){
				Short level = (short) (sagaPlayer.getProfessions(i).getLevel() + 1);
				if(level<0 || level>Saga.balanceInformation().maximumLevel){
					sagaPlayer.sendMessage(PlayerMessages.levelLimitReached(level));
					return;
				}
				sagaPlayer.getProfessions(i).setLevel(level);
				sagaPlayer.sendMessage(PlayerMessages.setLevelTo(sagaPlayer.getName(), sagaPlayer.getProfessions(i), level));
				Saga.info(PlayerMessages.setLevelTo(sagaPlayer.getName(), sagaPlayer.getProfessions(i), level) , sagaPlayer.getName());
				break;
			}
			if(i==sagaPlayer.getProfessionCount()-1){
				sagaPlayer.sendMessage(PlayerMessages.invalidProfession(args.getString(0)));
			}
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
    	
    	
    	for (int i = 0; i < sagaPlayer.getProfessionCount(); i++) {
			if(sagaPlayer.getProfessions(i).getProfessionName().equalsIgnoreCase(args.getString(0))){
				Short level = (short) (sagaPlayer.getProfessions(i).getLevel() - 1);
				if(level<0 || level>Saga.balanceInformation().maximumLevel){
					sagaPlayer.sendMessage(PlayerMessages.levelLimitReached(level));
					return;
				}
				sagaPlayer.getProfessions(i).setLevel(level);
				sagaPlayer.sendMessage(PlayerMessages.setLevelTo(sagaPlayer.getName(), sagaPlayer.getProfessions(i), level));
				Saga.info(PlayerMessages.setLevelTo(sagaPlayer.getName(), sagaPlayer.getProfessions(i), level) , sagaPlayer.getName());
				break;
			}
			if(i==sagaPlayer.getProfessionCount()-1){
				sagaPlayer.sendMessage(PlayerMessages.invalidProfession(args.getString(0)));
			}
		}
    	

	}
    
}
