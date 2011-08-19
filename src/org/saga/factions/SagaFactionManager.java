package org.saga.factions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.saga.Saga;
import org.saga.utility.WriterReader;


public class SagaFactionManager {

	
	/**
	 * Instance
	 */
	private static SagaFactionManager instance;
	
	/**
	 * Gets manager.
	 * 
	 * @return manager
	 */
	public static SagaFactionManager getFactionManager() {
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
	private SagaFactionManager() {
	}
	
	// Interact:
	/**
	 * Adds a faction.
	 * 
	 * @param faction
	 */
	public void addFaction(SagaFaction faction) {
	
		
		SagaFaction oldFaction = factions.put(faction.getFactionId(), faction);
		if(oldFaction != null){
			Saga.severe("Overwrite a " + oldFaction.getFactionName() + "(" + oldFaction.getFactionId() + ") faction in the faction list.");
		}

		
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
	 * Gets an unused faction ID.
	 * 
	 * @return unused faction ID. from 0(exclusive)
	 */
	public int getUnusedFactoinId() {

        Random random = new Random();

        int newId = random.nextInt(Integer.MAX_VALUE);

        while ( newId == 0 || factions.get(new Integer(newId)) != null ) {
            //Get another random id until we find one that isn't used
            // We also skip 0 because that is a special value that means no faction
            newId = random.nextInt();
        }

        return newId;

    }
	
	/**
	 * Creates a new faction.
	 * 
	 * @param factionName faction name
	 * @param factionPrefix faction prefix
	 * @return new faction
	 */
	public SagaFaction createFaction(String factionName, String factionPrefix) {

		
		SagaFaction faction = new SagaFaction(getUnusedFactoinId(), factionName, factionPrefix);
		faction.complete();
		
		Saga.info("Creating " + faction.getFactionId() + "(" + faction.getFactionName() + ") faction.");
		
		return faction;
		
		
	}
	
	// Loading unloading:
	/**
	 * Loads faction manager and loads factions.
	 * 
	 */
	public static void load() {

		
		SagaFactionManager factionManager = new SagaFactionManager();
		
		// Load factions:
		String[] factionIds = WriterReader.getAllFactionIds();
		ArrayList<SagaFaction> invalidIDs = new ArrayList<SagaFaction>();
		for (int i = 0; i < factionIds.length; i++) {
			SagaFaction faction = SagaFaction.load(factionIds[i]);
			// Add for IF fix:
			if(faction.getFactionId() == -1){
				invalidIDs.add(faction);
			}
			factionManager.addFaction(faction);
		}
		
		// Fix IDs:
		for (SagaFaction sagaFaction : invalidIDs) {
			sagaFaction.setFactionId(factionManager.getUnusedFactoinId());
		}
		
		// Set instance:
		instance = factionManager;
		
		
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
