/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.logging.*;

//imports from this project
import org.sk89q.*;
import org.saga.utility.*;
import org.saga.exceptions.*;

//External Imports
import java.util.*;
import java.io.*;


import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;


/**
 *
 * @author Cory
 */
public class Saga extends JavaPlugin {

    //Static Members
    private static final Logger log = Logger.getLogger("Saga");
    private static boolean debugging = true;
    private static Saga instance;
    private static BalanceInformation balanceInformation;

    //Instance Members
    private static CommandsManager<Player> commandMap;
    private WorldsHolder worldsHolder;
    private boolean playerInformationLoadingDisabled;
    private boolean playerInformationSavingDisabled;

    private HashMap<String,SagaPlayer> loadedPlayers;
    private SagaPlayerListener playerListener;

    public Saga() {

    }

    static public Saga plugin() {
        return instance;
    }
    
    static public BalanceInformation balanceInformation() {
        return balanceInformation;
    }

    @Override
    public void onDisable() {

        // NOTE: All registered events are automatically unregistered when a plugin is disabled
    	
    	// Remove players:
    	removeAllPlayers();
        loadedPlayers = null;

        //Remove global instances
        Saga.instance = null;
        Saga.balanceInformation=null;
        
    	//Say Goodbye
        Saga.info("Saga Goodbye!");

    }

    @Override
    public void onEnable() {
        
        //Say Hello!
        Saga.info("Saga Hello!");

        //Set Global Plugin Instance Variable
        Saga.instance = this;

        //Allocate Instance Variables
        loadedPlayers = new HashMap<String,SagaPlayer>();

        // Add all already online players:
        Player[] onlinePlayers= getServer().getOnlinePlayers();
        for (int i = 0; i < onlinePlayers.length; i++) {
			addPlayer(onlinePlayers[i]);
		}
        
        PluginManager pluginManager = getServer().getPluginManager();
        Plugin test = null;

        //Test for specific plugins
        test = pluginManager.getPlugin("GroupManager");
        if ( test != null ) {
            Saga.info("Saga found Group Manager plugin!");
            worldsHolder = ((GroupManager)test).getWorldsHolder();
        }

        //Setup Command Manager
        commandMap = new CommandsManager<Player>() {
            @Override
            public boolean hasPermission(Player player, String perm) {

                if ( worldsHolder != null ) {
                    OverloadedWorldHolder world = worldsHolder.getWorldData(player);
                    User user = world.getUser(player.getName());
                    return world.getPermissionsHandler().checkUserPermission(user, perm);
                } else  {
                    return player.isOp();
                }

            }

        };

        // Read balance information:
        BalanceInformation balanceInformation;
        try {
        	balanceInformation = WriterReader.readBalanceInformation();
        } catch (FileNotFoundException e) {
            Saga.severe("Missing balance information. Loading defaults and generating default balance information file.");
            balanceInformation= new BalanceInformation();
            balanceInformation.checkIntegrity(new Vector<String>());
            // Write a new one:
            try {
				WriterReader.writeBalanceInformation(balanceInformation);
			} catch (Exception e2) {
				Saga.severe("Balance information file save failure.");
			}
        }catch (IOException e) {
            Saga.exception("Balance information load failure. Loading defaults.",e);
            balanceInformation= new BalanceInformation();
            balanceInformation.checkIntegrity(new Vector<String>());
        }
        
        // Set global balance information:
        Saga.balanceInformation= balanceInformation;

        //Create listeners
        playerListener = new SagaPlayerListener(this);

        // Register events
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);

        //Register Command Classes to the command map
        commandMap.register(SagaCommands.class);

    }

    
    // Adding and removing:
    /**
     * Adds the player. Loads saga player if necessary.
	 * If sagaPlayer is already set to online, then the add will be ignored.
     * 
     * @param player player
     */
    public void addPlayer(Player player) {
    	
    	
    	SagaPlayer sagaPlayer;
    	
    	// Load if saga player isn't already loaded:
    	if((sagaPlayer= loadedPlayers.get(player.getName()))!=null){
    		Saga.info("Saga player already loaded. Wrapping player.", player.getName());
    	}else{
    		loadSagaPlayer(player.getName());
    		sagaPlayer= loadedPlayers.get(player.getName());
    	}
    	
    	// Notify if saving is disabled:
    	if(!sagaPlayer.isSavingEnabled()){
    		player.sendMessage(Messages.PLAYER_ERROR_MESSAGE);
    		player.sendMessage("You player information will not be saved during this session!");
        }
    	
    	// Check if online:
    	if(sagaPlayer.isOnlinePlayer()){
    		severe("Cant wrap player, because sagaPlayer is already set to online. Wrapping ignored. ", player.getName());
    		return;
    	}
    	
    	// Add the player and set sagaPlayer status to online:
    	sagaPlayer.setPlayer(player);
    	

	}
    
    /**
     * Loads a saga player as offline.
     * 
     * @param name player name
     */
    private void loadSagaPlayer(String name) {
    	
    	
    	SagaPlayer sagaPlayer;
    	
    	// Check if already loaded:
    	if(loadedPlayers.get(name)!=null){
    		severe("Tried loading an already loaded saga player. Loading ignored.", name);
    		return;
    	}
    	sagaPlayer= SagaPlayer.load(name);
    	loadedPlayers.put(name, sagaPlayer);
    	
    	// Integrity check:
    	Vector<String> problematicFields= new Vector<String>();
    	sagaPlayer.checkIntegrity(problematicFields);
    	if(problematicFields.size()!=0){
    		String probString="";
    		for (String string : problematicFields) {
				if(probString.length()!=0){
					probString+=", ";
				}
				probString+=string;
			}
    		warning("Integrity check found problematic fields: "+probString+". Integrity check set defaults.", name);
    	}
    	
    	
	}

    /**
     * Unwraps the player and unloads saga player from the list.
     * 
     * @param player
     */
    public void removePlayer(String name) {


    	SagaPlayer sagaPlayer;
    	// Check if loaded:
    	if((sagaPlayer= loadedPlayers.get(name))==null){
    		severe("Cant remove player wrap form non-existant saga player. Player information not saved.", name);
    		return;
    	}
    	
    	// Remove if online:
    	if(!sagaPlayer.isOnlinePlayer()){
    		severe("Cant remove player wrap from an offline player.", name);
    	}else{
    		sagaPlayer.removePlayer();
    	}

        //Save player data:
        sagaPlayer.save();
        
        // Unload saga player:
        unloadSagaPlayer(name);

        
    }
    
    /**
     * Unloads a saga player as offline.
     * Wrapped player must be removed first or this method will ignore unloading.
     * 
     * @param name player name
     */
    private void unloadSagaPlayer(String name) {
    	
    	
    	// Check if already unloaded:
    	if(loadedPlayers.get(name)==null){
    		severe("Tried unloading an already not loaded saga player.", name);
    		return;
    	}
    	
    	// Check if the player is still wrapped:
    	if(loadedPlayers.get(name).isOnlinePlayer()){
    		severe("Tried unloading a online saga player. Unloading ignored.", name);
    		return;
    	}
    	
    	loadedPlayers.remove(name);
    	
    	
	}

    /**
     * Removes all players.
     */
    private void removeAllPlayers() {

        Iterator<String> i = loadedPlayers.keySet().iterator();

        //Save All Players
        while ( i.hasNext() ) {
            String name = i.next();
            removePlayer(name);
        }

        //Empty the table
        loadedPlayers.clear();

    }


    
    //This code handles commands
    public boolean handleCommand(Player player, String[] split, String command) {

        try {

            split[0] = split[0].substring(1);

            // Quick script shortcut
            if (split[0].matches("^[^/].*\\.js$")) {
                String[] newSplit = new String[split.length + 1];
                System.arraycopy(split, 0, newSplit, 1, split.length);
                newSplit[0] = "cs";
                newSplit[1] = newSplit[1];
                split = newSplit;
            }

            // No command found!
            if (!commandMap.hasCommand(split[0])) {
                return false;
            }

            try {
                commandMap.execute(split, player, this);
                String logString = "[Saga Command] " + player.getName() + ": " + command;
                Saga.info(logString);
            } catch (CommandPermissionsException e) {
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            } catch (MissingNestedCommandException e) {
                player.sendMessage(e.getUsage());
            } catch (CommandUsageException e) {
                player.sendMessage(e.getMessage());
                player.sendMessage(e.getUsage());
            } catch (WrappedCommandException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
                e.printStackTrace();
                throw e.getCause();
            } catch (UnhandledCommandException e) {
                player.sendMessage(ChatColor.RED + "Unhandled command exception");
                return false;
            } finally {

            }

        } catch (Throwable excp) {

            player.sendMessage("Problem handling command: " + command);
            player.sendMessage(excp.getMessage());
            excp.printStackTrace();
            return false;

        }

        return true;

    }

    /**
     * True, if player information loading is disabled.
     *
     * @return the playerInformationLoadingDisabled
     */
    public boolean isPlayerInformationLoadingDisabled() {
            return playerInformationLoadingDisabled;
    }
    

    /**
     * True, if player information saving is disabled.
     *
     * @return the playerInformationSavingDisabled
     */
    public boolean isPlayerInformationSavingDisabled() {
            return playerInformationSavingDisabled;
    }
    

    /**
     * Disables the loading and saving of player information.
     *
     */
    public void disablePlayerInformationSavingLoading() {

            if( playerInformationLoadingDisabled && playerInformationSavingDisabled ){
                    return;
            }

            playerInformationLoadingDisabled = true;
            playerInformationSavingDisabled = true;

            Saga.debug("Disabling player information saving and loading.");

    }

    //Debug/Log Output Functions
    
    static public void info(String string) {
        log.info(string);
    }

    
    static public void info(String string, String playerName) {
        string = "(" + playerName + ")" + string;
        log.info(string);
    }

    
    static public void severe(String string) {
        log.severe(string);
    }

    
    static public void severe(String string, String playerName) {
        string = "(" + playerName+ ")" + string;
        log.severe(string);
    }

    
    static public void warning(String string) {
        log.warning(string);
    }

    
    static public void warning(String string, String playerName) {
        string = "(" + playerName + ")" + string;
        log.warning(string);
    }

    
    static public void exception(String string, Exception e) {
        string = string + " [" + e.getClass().getSimpleName() + "]" + e.getMessage();
        log.severe(string);
    }

    
    static public void debug(String string) {

        if ( !debugging ) {
            return;
        }

        //TODO: Send to list of debuggers

        string = "[DEBUG] " + string;

        //Set to log
        log.info(string);

    }

    
    static public void debug(String string, String playerName) {

        if ( !debugging ) {
            return;
        }


        string = "[DEBUG] " + string;

        //Set to log
        log.info(string);

    }

}
