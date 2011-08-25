package org.saga.factions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.utility.WriterReader;


public class FactionManager {

	
	/**
	 * Instance
	 */
	private static FactionManager instance;
	
	/**
	 * Gets manager.
	 * 
	 * @return manager
	 */
	public static FactionManager getFactionManager() {
		return instance;
	}
	
	
	/**
	 * Factions.
	 */
	private Hashtable<Integer, SagaFaction> factions = new Hashtable<Integer, SagaFaction>();
	

	// Initialization:
	/**
	 * Initializes.
	 * 
	 */
	private FactionManager() {
	}

	
	// Interaction:
	/**
	 * Creates a new faction.
	 * 
	 * @param factionName faction name
	 * @param factionPrefix faction prefix
	 * @param creator faction creator
	 * @return created faction
	 */
	public SagaFaction createFaction(String factionName, String factionPrefix, SagaPlayer creator) {
		
		
		// Create:
		SagaFaction faction = new SagaFaction(getUnusedFactoinId(), factionName, factionPrefix);
		faction.complete();
		addFaction(faction);
		
		// Log:
		Saga.info("Creating " + faction.getId() + "(" + faction.getName() + ") faction.");
		
		// Add the first member:
		faction.addMember(creator);
		
		// Save:
		faction.save();
		
		return faction;
		
		
	}
	
	/**
	 * Gets a saga faction from the list.
	 * 
	 * @param factionId faction ID
	 * @return saga faction. null if not found
	 */
	public SagaFaction getFaction(Integer factionId) {

		return factions.get(factionId);
		
	}
	
	 /**
     * Finds a saga faction with the given prefix.
     * 
     * @param prefix prefix
     * @return saga faction. null if not found
     */
    public SagaFaction factionWithPrefix(String prefix) {

    	
        Iterator<Integer> ids = factions.keySet().iterator();

        while( ids.hasNext() ) {

            Integer id = ids.next();

            SagaFaction faction = factions.get(id);

            if ( faction.getPrefix().equals(prefix) ) {
                return faction;
            }

        }

        return null;

        
    }

    /**
     * Finds a saga faction with the given name.
     * 
     * @param name name
     * @return saga faction. null if not found
     */
    public SagaFaction factionWithName(String name) {

    	
        Iterator<Integer> ids = factions.keySet().iterator();

        while( ids.hasNext() ) {

            Integer id = ids.next();

            SagaFaction faction = factions.get(id);

            if ( faction.getName().equals(name) ) {
                return faction;
            }

        }

        return null;

        
    }
	
	
	// Player:
	/**
	 * Called when a player joins the server.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void playerRegisterAll(SagaPlayer sagaPlayer) {

		
		// Get all faction IDs:
		ArrayList<Integer> factionIds = sagaPlayer.getFactionIds();
		
		// Register all player factions:
		for (Integer factionId : factionIds) {
			
			// Retrieve the faction:
			SagaFaction faction = factions.get(factionId);
			
			// No longer exists:
			if(faction == null){
				Saga.severe("Faction manager could not register player, because the faction with id " + factionId + " was not found. Ignoring request.", sagaPlayer.getName());
				continue;
			}
			
			if(!faction.isMember(sagaPlayer)){
				Saga.severe("Faction manager could not register player, because the player isn't on " + factionId + " faction member list. Ignoring request.", sagaPlayer.getName());
				continue;
			}
			
			// Register:
			faction.registerMember(sagaPlayer);
			
		}
		
		
	}

	/**
	 * Called when a player leaves the server.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void playerUnregisterAll(SagaPlayer sagaPlayer) {

		
		// Unregister all player factions:
		ArrayList<SagaFaction> registeredFactions = sagaPlayer.getRegisteredFactions();
		for (int i = 0; i < registeredFactions.size(); i++) {
			registeredFactions.get(i).unregisterMember(sagaPlayer);
		}
		
		
	}

	
	// SagaFaction methods:
	/**
	 * Adds a faction.
	 * 
	 * @param faction
	 */
	void addFaction(SagaFaction faction) {
	
		
		// Add:
		SagaFaction oldFaction = factions.put(faction.getId(), faction);
		if(oldFaction != null){
			Saga.severe("Added an already existing faction " + oldFaction.getName() + "(" + oldFaction.getId() + ") to the faction list.");
		}
		
		
	}
	
	/**
	 * Removes a faction.
	 * 
	 * @param faction
	 */
	void removeFaction(SagaFaction faction) {
		
		
		// Remove:
		if(factions.remove(faction.getId()) == null){
			Saga.severe("Tried to remove a non-existing " + faction.getName() + "(" + faction.getId() + ") faction from the list.");
			return;
		}
		

	}

	/**
	 * Gets an unused faction ID.
	 * 
	 * @return unused faction ID. from 0(exclusive)
	 */
	int getUnusedFactoinId() {

        Random random = new Random();

        int newId = random.nextInt(Integer.MAX_VALUE);

        while ( newId == 0 || factions.get(new Integer(newId)) != null ) {
            //Get another random id until we find one that isn't used
            // We also skip 0 because that is a special value that means no faction
            newId = random.nextInt();
        }

        return newId;

    }
	
	
	// Loading unloading:
	/**
	 * Loads faction manager and loads factions.
	 * 
	 */
	public static void load() {

		
		FactionManager manager = new FactionManager();
		
		// Load factions:
		String[] ids = WriterReader.getAllFactionIds();
		for (int i = 0; i < ids.length; i++) {
			SagaFaction element = SagaFaction.load(ids[i]);
			// Ignore all invalid IDs:
			if(element.getId() < 0){
				Saga.severe("Can't load " + element + " faction, because it has an invalid ID. Ignoring request.");
				continue;
			}
			// Add to manager:
			manager.addFaction(element);
		}
		
		// Set instance:
		instance = manager;
		
		
	}
	
	/**
	 * Unloads faction manager and saves factions.
	 * 
	 */
	public static void unload() {

		
		// Save factions:
		Collection<SagaFaction> factions = getFactionManager().factions.values();
		for (SagaFaction sagaFaction : factions) {
			sagaFaction.save();
		}
		
		instance = null;
		
	}
	
}
