package org.saga.world;

import java.util.Hashtable;

public class SagaChunkManager {

	
	/**
	 * All maps.
	 */
	transient private static SagaChunkManager[] maps;
	
	private static void getMap(String worldName) {

		for (int i = 0; i < maps.length; i++) {
//			if(maps[i].g)
		}
		
	}
	
	/**
	 * Map name.
	 */
	transient private String worldName;
	
	/**
	 * Map containing all the claimed saga chunks.
	 */
	private Hashtable<Integer, Hashtable<Integer, SagaChunk>> sagaMap = new Hashtable<Integer, Hashtable<Integer,SagaChunk>>();
	
	
	// Initializations:
	/**
	 * Initializes.
	 * 
	 */
	public SagaChunkManager(String worldName) {
		
		this.worldName = worldName;
		
	}
	
	/**
	 * Completes the initialization.
	 * 
	 * @return
	 */
	public boolean complete() {

		return true;
		
	}
	
	
	// Interaction:
	
	
	// Load unload:
	/**
	 * Loads the map.
	 * 
	 */
	public static void load(){
		
		
		
		
		
	}
	
	/**
	 * Unloads the map.
	 * 
	 */
	public void unload() {

		
		
	}

	
}
