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
    private HashMap<String,SagaPlayer> sagaPlayers;
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

        sagaPlayers = null;

        //Remove Global Instance
        Saga.instance = null;

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
        sagaPlayers = new HashMap<String,SagaPlayer>();

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


    public SagaPlayer wrapPlayer(Player player) {

        if ( player == null ) {
            Saga.warning("Can't wrap null player");
            return null;
        }

        //First try to get from loaded player map data
        SagaPlayer wrappedPlayer = sagaPlayers.get(player.getName());

        if ( wrappedPlayer != null ) {
            return wrappedPlayer;
        }

        Saga.debug("wrapping player "+player.getName());

        //Second try, attempt to load data from file
        try {
            wrappedPlayer = SagaPlayer.load(player.getName());
        } catch( SagaPlayerNotFoundException e ) {
            //Player With New Data
            wrappedPlayer = new SagaPlayer(player,balanceProperties);
        }

        return wrappedPlayer;

    }

    public void addPlayer(Player player) {

    	// Wrap the player and add him:
    	sagaPlayers.put(player.getName(), wrapPlayer(player));
        Saga.debug("adding player "+player.getName());

    }

    public void removePlayer(Player player) {

        if ( player == null ) {
            Saga.warning("Cannot remove null player");
            return;
        }

        SagaPlayer sagaPlayer = sagaPlayers.remove(player.getName());

        if ( sagaPlayer == null ) {
            Saga.warning("SagaPlayer does not exist for player!",player);
            return;
        }

        //Try to save player data
        try {
            sagaPlayer.save();
        } catch ( IOException e ) {
            Saga.exception("Exception while writing player " + player.getName() + " data to disk.",e);
        }

        
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
                commandMap.execute(split, player, this, wrapPlayer(player));
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

	public void removeAllPlayers() {

        Iterator<String> i = sagaPlayers.keySet().iterator();

        //Save All Players
        while ( i.hasNext() ) {

            String player = i.next();
            SagaPlayer sagaPlayer = sagaPlayers.get(player);
            
            try {
                sagaPlayer.save();
            } catch ( IOException e ) {
                Saga.exception("Exception while writing player " + player + " data to disk.",e);
            }

        }

        //Empty the table
        sagaPlayers.clear();

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
