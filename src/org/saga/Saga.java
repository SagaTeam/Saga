/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.logging.*;

//imports from this project
import org.sk89q.*;
import org.saga.utility.*;
import org.saga.chunkGroups.ChunkGroupCommands;
import org.saga.chunkGroups.ChunkGroupManager;
import org.saga.config.AttributeConfiguration;
import org.saga.config.BalanceConfiguration;
import org.saga.config.ExperienceConfiguration;
import org.saga.config.ProfessionConfiguration;
import org.saga.constants.PlayerMessages;
import org.saga.exceptions.NonExistantSagaPlayerException;
import org.saga.exceptions.SagaPlayerNotLoadedException;
import org.saga.factions.FactionCommands;
import org.saga.factions.FactionManager;

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
    	
    	// Remove instances:
    	loadedPlayers = null;

        //Remove global instances
        Clock.unload(); // Needs access to Saga.pluging()
        Saga.instance = null;
        Saga.playerListener = null;
        Saga.entityListener = null;
        ExperienceConfiguration.unload();
        ProfessionConfiguration.unload();
        AttributeConfiguration.load();
        BalanceConfiguration.unload();
        ChunkGroupManager.unload();
        FactionManager.unload(); // Needs access to chunk group manager.
        
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
        ExperienceConfiguration.load();
        AttributeConfiguration.load();
        ProfessionConfiguration.load(); // Needs access to experience info.
        ChunkGroupManager.load();
        FactionManager.load(); // Needs access to chunk group manager.
        Clock.load(); // Needs access to Saga.pluging()
        
        // Add all already online players:
        Player[] onlinePlayers = getServer().getOnlinePlayers();
        for (int i = 0; i < onlinePlayers.length; i++) {
            addPlayer(onlinePlayers[i]);
        }
        
        //Add and activate clock:
      	
        
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
        pluginManager.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        
        //Register Command Classes to the command map
        commandMap.register(SagaCommands.class);
        commandMap.register(FactionCommands.class);
        commandMap.register(ChunkGroupCommands.class);

        
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
    	if( sagaPlayer.isOnline() ) {
            severe("Cant wrap player, because sagaPlayer is already set to online. Wrapping ignored. ", player.getName());
            return;
    	}
    	
    	// Add the player and set sagaPlayer status to online:
    	sagaPlayer.setPlayer(player);

    	// Send a join event:
    	
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
    	if( !sagaPlayer.isOnline() ) {
            severe("Cant remove player wrap from an offline player.", name);
    	} else {
            sagaPlayer.removePlayer();
    	}

        // Unload saga player:
        unloadSagaPlayer(name);

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
     * Loads a saga player. The minecraft player needs to be set separately.
     * If no player exists, then a new one is created.
     * 
     * @param name player name
     * @return loaded player
     */
    public SagaPlayer loadSagaPlayer(String name) {
    	
    	
    	SagaPlayer sagaPlayer = loadedPlayers.get(name);
    	
    	// Check if already loaded:
    	if( sagaPlayer != null ){
            severe("Tried loading an already loaded saga player. Loading ignored.", name);
            return sagaPlayer;
    	}

    	// Load from disc:
    	sagaPlayer = SagaPlayer.load(name);
    	Saga.info("Loading saga player.", name);
    	loadedPlayers.put(name, sagaPlayer);
    	
    	// Register factions:
    	FactionManager.getFactionManager().playerRegisterAll(sagaPlayer);
    	
    	// Register chunk groups:
    	ChunkGroupManager.getChunkGroupManager().playerRegisterAll(sagaPlayer);
    	
    	
    	return sagaPlayer;
    	
    	
    }

    /**
     * Unloads a saga player as offline.
     * Wrapped player must be removed first or this method will ignore unloading.
     * 
     * @param name player name
     */
    public void unloadSagaPlayer(String name) {
    	
    	
    	SagaPlayer sagaPlayer = loadedPlayers.get(name);
    	
    	// Ignore if already unloaded:
    	if( sagaPlayer == null ) {
            severe("Tried unloading an non-loaded saga player.", name);
            return;
    	}
    	
    	// Ignore if still online:
    	if( sagaPlayer.isOnline() ) {
            severe("Tried unloading an online saga player. Unloading ignored.", name);
            return;
    	}
    	
    	// Ignore if forced:
    	if( sagaPlayer.isForced() ){
    		info("Can't unload saga player because he is forced. Unloading ignored.", name);
            return;
    	}
    	
    	// Unload:
    	Saga.info("Unloading saga player.", name);
    	loadedPlayers.remove(name);
    	
    	// Unregister factions:
    	FactionManager.getFactionManager().playerUnregisterAll(sagaPlayer);
    	
    	// Register chunk groups:
    	ChunkGroupManager.getChunkGroupManager().playerUnregisterAll(sagaPlayer);
    	
    	//Save player data:
        sagaPlayer.save();
        
    	
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
     * Gets a loaded saga player.
     * 
     * @param player player
     * @return saga player
     * @throws SagaPlayerNotLoadedException  if saga player is not loaded
     */
    @Deprecated
    public SagaPlayer getLoadedSagaPlayer(String name) throws SagaPlayerNotLoadedException {
    	
    	
    	// Search from loaded list:
    	SagaPlayer sagaPlayer = loadedPlayers.get(name);
    	
    	// Throw an exception if player not loaded:
    	if(sagaPlayer == null){
    		throw new SagaPlayerNotLoadedException(name);
    	}
    	
        return sagaPlayer;
        
        
    }
    
    
    /**
     * Forces the player to get loaded in the forced list.
     * Loads if necessary.
     * 
     * @param name player name
     * @throws NonExistantSagaPlayerException if the player doesn't exist.
     */
    public SagaPlayer forceSagaPlayer(String name) throws NonExistantSagaPlayerException {

    	
    	SagaPlayer sagaPlayer;
    	
    	// Check in loaded list:
    	sagaPlayer = loadedPlayers.get(name);
    	if(sagaPlayer != null){
    		Saga.info("Forcing saga player.", name);
    		sagaPlayer.increaseForceLevel();
    		return sagaPlayer;
    	}
    	
    	// Check if the player exists:
    	if(!isSagaPlayerExistant(name)){
    		throw new NonExistantSagaPlayerException(name);
    	}
    	
    	// Load:
    	sagaPlayer = loadSagaPlayer(name);
    	Saga.info("Forcing saga player.", name);
    	sagaPlayer.increaseForceLevel();
		return sagaPlayer;
    	
    	
	}

    /**
     * Unforces the player to get loaded in the forced list.
     * Saves if necessary.
     * 
     * @param name player name
     * @throws NonExistantSagaPlayerException if the player doesn't exist.
     */
    public void unforceSagaPlayer(String name) {

    	
    	// Check in loaded list:
    	SagaPlayer sagaPlayer = loadedPlayers.get(name);
    	if(sagaPlayer == null){
    		Saga.severe("Tried to unforce a non-loaded player.", name);
    		return;
    	}
    	
    	// Decrease force level:
    	Saga.info("Unforcing saga player.", name);
		sagaPlayer.decreaseForceLevel();
    	
    	// Unload if possible:
		if(!sagaPlayer.isForced() && !sagaPlayer.isOnline()){
			unloadSagaPlayer(name);
		}
    	
    	
	}
    
    /**
     * Gets a saga player from the loaded list.
     * 
     * @param name name
     * @return saga player. null if not loaded
     */
    public SagaPlayer getSagaPlayer(String name) {
    	return loadedPlayers.get(name);
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
            	System.out.println("no command:"+split[0]);
                return false;
            }

            try {
                commandMap.execute(split, player, this, getLoadedSagaPlayer(player.getName()));
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

    // Messages:
    /**
     * Broadcast a message.
     * 
     * @param message message
     */
    public static void broadcast(String message){
    	
    	Saga.plugin().getServer().broadcastMessage(message);
    	
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
