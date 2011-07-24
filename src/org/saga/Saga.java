/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.logging.*;

//imports from this project
import org.sk89q.*;
import org.saga.utility.*;
import org.saga.constants.PlayerMessages;
import org.saga.exceptions.SagaPlayerNotLoadedException;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;

import com.google.gson.JsonParseException;


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
        Saga.balanceInformation = null;
        
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

        // Read balance information:
        BalanceInformation balanceInformation;
        boolean writeDefaultBalanceInfo = false;
        try {

            balanceInformation = WriterReader.readBalanceInformation();

        } catch (FileNotFoundException e) {

            Saga.severe("Missing balance information. Loading defaults.");

            balanceInformation = new BalanceInformation();
            writeDefaultBalanceInfo = true;
            
        }catch (IOException e) {

            Saga.exception("Balance information load failure. Loading defaults.",e);
            balanceInformation = new BalanceInformation();
            writeDefaultBalanceInfo = true;

        }catch (JsonParseException e) {
        	
        	Saga.exception("Balance information parse failure. Loading defaults.",e);
            balanceInformation = new BalanceInformation();
            writeDefaultBalanceInfo = true;

        }
        
        // Complete balance information:
        if(!balanceInformation.complete()){
        	Saga.severe("Balance information integrity check failed. Update balance information file.");
        	writeDefaultBalanceInfo = true;
        }
        
        // Generate default balance information:
        if(writeDefaultBalanceInfo){
            try {
            	Saga.info("Generating default balance information file. Edit and rename to use it.");
                WriterReader.writeBalanceInformation(balanceInformation, WriterReader.SUFFIX_DEFAULT);
            } catch (Exception e2) {
                Saga.severe("Balance information file generation failure.");
            }

        }
        
        // Set global balance information:
        Saga.balanceInformation = balanceInformation;

        // Add all already online players:
        Player[] onlinePlayers = getServer().getOnlinePlayers();
        for (int i = 0; i < onlinePlayers.length; i++) {
            addPlayer(onlinePlayers[i]);
        }
        
        //Create listeners
        playerListener = new SagaPlayerListener(this);

        // Register events
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);

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
    	if( loadedPlayers.get(name) == null ) {
            severe("Tried unloading an already not loaded saga player.", name);
            return;
    	}
    	
    	// Check if the player is still wrapped:
    	if( loadedPlayers.get(name).isOnlinePlayer() ) {
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
    
    /**
     * Gets the saga player.
     * The player must be in the loaded list, before you can use this method.
     * 
     * @param player player
     * @return saga player
     * @throws SagaPlayerNotLoadedException  if saga player is not loaded
     */
    public SagaPlayer getPlayer(String name) throws SagaPlayerNotLoadedException {
    	
    	
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

        string = "[DEBUG] (" + playerName + ") " + string;

        //Set to log
        log.info(string);

    }

    /*
    public static void main(String[] args) {

        ArrayList<Profession> professionList = new ArrayList<Profession>();

        //Load up some professions
        professionList.add(new WoodcutterProfession());
        professionList.add(new FighterProfession());

        GsonBuilder gsonBuilder  = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Profession.class, new ProfessionDeserializer());

        Gson gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<Profession>>(){}.getType();
        String json = gson.toJson(professionList,type);

        //Read the json data
        professionList = gson.fromJson(json, type);

        if ( professionList == null ) {
            info("List is null!");
            return;
        }

        for ( int i = 0; i < professionList.size(); i++ ) {

            Profession prof = professionList.get(i);

            if ( prof == null ) {
                info("prof is null : " + i);
            } else {
                info("Got Class " + prof.getClass().getName());
            }

            //info("Got class: " + prof.getClass().getSimpleName() );

        }

    }*/

}
