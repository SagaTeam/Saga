/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.logging.*;

//imports from this project
import org.sk89q.*;
import org.saga.utility.*;
import org.saga.config.AttributeConfiguration;
import org.saga.config.BalanceConfiguration;
import org.saga.config.ExperienceConfiguration;
import org.saga.config.ProfessionConfiguration;
import org.saga.constants.PlayerMessages;
import org.saga.exceptions.SagaPlayerNotLoadedException;

//External Imports
import java.util.*;


import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    //Instance Members
    private static CommandsManager<Player> commandMap;
    private WorldsHolder worldsHolder;
    private boolean playerInformationLoadingDisabled;
    private boolean playerInformationSavingDisabled;

    private Hashtable<String,SagaPlayer> loadedPlayers;
    private static SagaPlayerListener playerListener;
    private static SagaEntityListener entityListener;
    private SagaBlockListener blockListener;

    static public Saga plugin() {
        return instance;
    }
    
    public static boolean debuging() {
    	return debugging;
	}
    
    public static SagaPlayerListener playerListener() {
    	return playerListener;
	}
    
    public static SagaEntityListener entityListener() {
    	return entityListener;
	}
    
    @Override
    public void onDisable() {

        // NOTE: All registered events are automatically unregistered when a plugin is disabled
    	
    	// Remove players:
    	removeAllPlayers();
        loadedPlayers = null;

        //Remove global instances
        Saga.instance = null;
        Saga.playerListener = null;
        Saga.entityListener = null;
        ExperienceConfiguration.unload();
        ProfessionConfiguration.unload();
        AttributeConfiguration.load();
        BalanceConfiguration.unload();
        
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
        loadedPlayers = new Hashtable<String, SagaPlayer>();

        
        //Test for specific plugins
        PluginManager pluginManager = getServer().getPluginManager();
        Plugin test = null;
        test = pluginManager.getPlugin("GroupManager");
        if ( test != null ) {
            Saga.info("Saga found Group Manager plugin!");
            worldsHolder = ((GroupManager)test).getWorldsHolder();
        } else {
            Saga.warning("Group Manager not found! Op only permissions!");
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

        // Configuration:
        BalanceConfiguration.load();
        AttributeConfiguration.load();
        ProfessionConfiguration.load();
        ExperienceConfiguration.load();
        
        // Add all already online players:
        Player[] onlinePlayers = getServer().getOnlinePlayers();
        for (int i = 0; i < onlinePlayers.length; i++) {
            addPlayer(onlinePlayers[i]);
        }
        
        //Add and activate clock:
      	getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Clock(), 200L, 20L);
        
        //Create listeners
      	playerListener = new SagaPlayerListener(this);
      	entityListener = new SagaEntityListener();
      	blockListener = new SagaBlockListener();

        // Register events
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.ENTITY_COMBUST, entityListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
        
        //Register Command Classes to the command map
        commandMap.register(SagaCommands.class);

        
    }
    
    
    // Player management:
    /**
     * Adds the player. Loads saga player if necessary.
	 * If sagaPlayer is already set to online, then the add will be ignored.
     * 
     * @param player player
     */
    public void addPlayer(Player player) {
    	
    	SagaPlayer sagaPlayer = loadedPlayers.get(player.getName());

    	// Load if saga player isn't already loaded:
    	if( sagaPlayer != null ) {
            sagaPlayer.setPlayer(player);
            return;
    	}

        //Player isn't loaded, so try loading
        loadSagaPlayer(player.getName());

        sagaPlayer = loadedPlayers.get(player.getName());

        if ( sagaPlayer == null ) {
            Saga.severe("Saga player was supposed to have been loaded!!");
        }

    	// Notify if saving is disabled:
    	if( !sagaPlayer.isSavingEnabled() ) {
            player.sendMessage(PlayerMessages.playerErrorMessage);
            player.sendMessage(PlayerMessages.yourIformationWillNotBeSaved);
        }
    	
    	// Check if online:
    	if( sagaPlayer.isOnlinePlayer() ) {
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
    public void loadSagaPlayer(String name) {
    	
    	SagaPlayer sagaPlayer;
    	
    	// Check if already loaded:
    	if( loadedPlayers.get(name) != null ){
            severe("Tried loading an already loaded saga player. Loading ignored.", name);
            return;
    	}

    	sagaPlayer = SagaPlayer.load(name);
    	loadedPlayers.put(name, sagaPlayer);
    	
    	
    }

    /**
     * Unwraps the player and unloads saga player from the list.
     * 
     * @param player
     */
    public void removePlayer(String name) {

    	SagaPlayer sagaPlayer;
    	// Check if loaded:
    	if( (sagaPlayer= loadedPlayers.get(name)) == null ) {
            severe("Cant remove player wrap form non-existant saga player. Player information not saved.", name);
            return;
    	}
    	
    	// Remove if online:
    	if( !sagaPlayer.isOnlinePlayer() ) {
            severe("Cant remove player wrap from an offline player.", name);
    	} else {
            sagaPlayer.removePlayer();
    	}

        // Unload saga player:
        unloadSagaPlayer(name);

    }
    
    /**
     * Unloads a saga player as offline.
     * Wrapped player must be removed first or this method will ignore unloading.
     * 
     * @param name player name
     */
    public void unloadSagaPlayer(String name) {
    	
    	// Check if already unloaded:
    	if( loadedPlayers.get(name) == null ) {
            severe("Tried unloading an already not loaded saga player.", name);
            return;
    	}
    	
    	// Check if the player is still wrapped:
    	if( loadedPlayers.get(name).isOnlinePlayer() ) {
            severe("Tried unloading a online saga player. Unloading ignored.", name);
            return;
    	}
    	
    	// Unload:
    	SagaPlayer sagaPlayer = loadedPlayers.remove(name);
    	
    	//Save player data:
        sagaPlayer.save();
        
    	
    }

    /**
     * Removes all players.
     */
    private void removeAllPlayers() {

    	
//        Iterator<String> i = loadedPlayers.keySet().iterator();

        Enumeration<String> keys= loadedPlayers.keys();
        
        // Remove all Players
        while ( keys.hasMoreElements() ) {
            removePlayer(keys.nextElement());
        }

        //Empty the table
        loadedPlayers.clear();

    }
    
    /**
     * Checks if the player is loaded.
     * 
     * @param name name
     * @return true if loaded
     */
    public boolean isSagaPlayerLoaded(String name) {
    	return loadedPlayers.get(name)!=null;
	}
    
    /**
     * Checks if the player exists by checking player information file.
     * 
     * @param playerName player name
     * @return true if the player exists
     */
    public boolean isSagaPlayerExistant(String playerName) {
    	return WriterReader.playerExists(playerName);
	}
    
    /**
     * Gets the saga player.
     * The player must be in the loaded list, before you can use this method.
     * 
     * @param player player
     * @return saga player
     * @throws SagaPlayerNotLoadedException  if saga player is not loaded
     */
    public SagaPlayer getSagaPlayer(String name) throws SagaPlayerNotLoadedException {
    	
    	
    	// Search from loaded list:
    	SagaPlayer sagaPlayer = loadedPlayers.get(name);
    	
    	// Throw an exception if player not loaded:
    	if(sagaPlayer == null){
    		throw new SagaPlayerNotLoadedException(name);
    	}
    	
        return sagaPlayer;
        
        
    }
    
    
    // Events:
    /**
	 * Got damaged by living entity event.
	 *
	 * @param playerName player name
	 * @param pEvent event
	 */
	public void gotDamagedByLivingEntityEvent(String playerName, EntityDamageByEntityEvent pEvent) {

		// Only send the event 

	}

	/**
	 * Damaged a living entity.
	 *
	 * @param pEvent event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent pEvent) {



	}

	/**
	 * Left clicked.
	 *
	 * @param pEvent event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Player placed a block event.
	 *
	 * @param pEvent event
	 */
	public void placedBlockEvent(BlockPlaceEvent pEvent) {



	}

	/**
	 * Player broke a block event.
	 *
	 * @param pEvent event
	 */
	public void brokeBlockEvent(BlockBreakEvent pEvent) {



	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {

		
		// Send to all online players:
		for (SagaPlayer sagaPlayer : loadedPlayers.values()) {
			if(sagaPlayer.isOnlinePlayer()){
				sagaPlayer.clockTickEvent(pTick);
			}
		}
		
		
	}
    
    
    //This code handles commands
    public boolean handleCommand(Player player, String[] split, String command) {

    	System.out.println("handling command");
    	
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
            	System.out.println("no command:"+split[0]);
                return false;
            }

            try {
                commandMap.execute(split, player, this, getSagaPlayer(player.getName()));
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

        string = "[DEBUG] (" + playerName + ") " + string;

        //Set to log
        log.info(string);

    }

}
