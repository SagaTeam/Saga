package org.saga.chunkGroups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.chunkGroups.SagaChunk.ChunkSide;
import org.saga.factions.SagaFaction;
import org.saga.utility.WriterReader;

public class ChunkGroupManager {


	/**
	 * Instance
	 */
	private static ChunkGroupManager instance;
	
	/**
	 * Gets manager.
	 * 
	 * @return manager
	 */
	public static ChunkGroupManager getChunkGroupManager() {
		return instance;
	}
	
	
	/**
	 * Registered groups.
	 */
	transient  Hashtable<Integer, ChunkGroup> registeredGroups = new Hashtable<Integer, ChunkGroup>();
	
	/**
	 * All saga chunks.
	 */
	transient private Hashtable<String, Hashtable<Integer, Hashtable<Integer, SagaChunk>>> sagaChunks = new Hashtable<String, Hashtable<Integer,Hashtable<Integer,SagaChunk>>>();

	
	// Register:
	/**
	 * Registers all faction chunk groups.
	 * 
	 * @param sagaFaction saga faction
	 */
	public void factionRegisterAll(SagaFaction sagaFaction) {


		// Get all faction IDs:
		ArrayList<Integer> chunkGroupIds = sagaFaction.getChunkGroupIds();
		
		// Register all player factions:
		for (Integer groupId : chunkGroupIds) {
			
			// Retrieve the faction:
			ChunkGroup chunkGroup = registeredGroups.get(groupId);
			
			// No longer exists:
			if(chunkGroup == null){
				Saga.severe("Chunk group manager could not register " + sagaFaction + " faction, because the chunk group with id " + groupId + " was not found. Ignoring request.");
				continue;
			}
			
			if(!chunkGroup.hasFaction(sagaFaction.getId())){
				Saga.severe("Chunk group manager could not register " + sagaFaction + " faction, because the chunk group isn't on " + groupId + " factions chunk group list. Ignoring request.");
				continue;
			}
			
			// Register:
			chunkGroup.registerFaction(sagaFaction);
			
		}
		
	}
	
	/**
	 * Unregisters all faction chunk groups.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void factionUnregisterAll(SagaFaction sagaPlayer) {

		
		// Unregister all player factions:
		ArrayList<ChunkGroup> chunkGroups = sagaPlayer.getRegisteredChunkGroups();
		for (int i = 0; i < chunkGroups.size(); i++) {
			chunkGroups.get(i).unregisterFaction(sagaPlayer);
		}
		
		
	}
	
	/**
	 * Registers all player chunk groups.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void playerRegisterAll(SagaPlayer sagaPlayer) {


		// Get all faction IDs:
		ArrayList<Integer> chunkGroupIds = sagaPlayer.getChunkGroupIds();
		
		// Register all player factions:
		for (Integer groupId : chunkGroupIds) {
			
			// Retrieve the faction:
			ChunkGroup chunkGroup = registeredGroups.get(groupId);
			
			// No longer exists:
			if(chunkGroup == null){
				Saga.severe("Chunk group manager could not register " + sagaPlayer + " player, because the chunk group with id " + groupId + " was not found. Ignoring request.");
				continue;
			}
			
			if(!chunkGroup.hasPlayer(sagaPlayer.getName())){
				Saga.severe("Chunk group manager could not register " + sagaPlayer + " player, because the chunk group isn't on " + groupId + " factions chunk group list. Ignoring request.");
				continue;
			}
			
			// Register:
			chunkGroup.registerPlayer(sagaPlayer);
			
		}
		
	}
	
	/**
	 * Unregisters all player chunk groups.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void playerUnregisterAll(SagaPlayer sagaPlayer) {

		
		// Unregister all player factions:
		ArrayList<ChunkGroup> chunkGroups = sagaPlayer.getRegisteredChunkGroups();
		for (int i = 0; i < chunkGroups.size(); i++) {
			chunkGroups.get(i).unregisterPlayer(sagaPlayer);
		}
		
		
	}
	
	
	// Chunk group interaction:
	/**
	 * Gets the chunk groups associated with the player.
	 * 
	 * @param chunkGroupId group ID
	 * @return chunk group. null if not found
	 */
	public ChunkGroup getChunkGroup(Integer chunkGroupId) {

		
		return registeredGroups.get(chunkGroupId);
		
		
	}

	/**
	 * Gets a saga chunk.
	 * 
	 * @param location location
	 * @return saga chunk. null if not found
	 */
	public SagaChunk getSagaChunk(Location location) {

		return getSagaChunk(location.getWorld().getChunkAt(location));
		
	}
	
	/**
	 * Gets a saga chunk.
	 * 
	 * @param chunk location chunk
	 * @return saga chunk. null if not found
	 */
	public SagaChunk getSagaChunk(Chunk chunk) {

		
		Hashtable<Integer, Hashtable<Integer, SagaChunk>> world = sagaChunks.get(chunk.getWorld().getName());
		if(world == null){
			return null;
		}
		
		Hashtable<Integer, SagaChunk> x = world.get(chunk.getX());
		if(x == null){
			return null;
		}
		
		return x.get(chunk.getZ());
		
		
	}
	
	/**
	 * Gets a saga chunk.
	 * 
	 * @param worldName world name
	 * @param x x
	 * @param z z
	 * @return saga chunk. null if not found
	 */
	public SagaChunk getSagaChunk(String worldName, int x, int z) {

		
		Hashtable<Integer, Hashtable<Integer, SagaChunk>> tworld = sagaChunks.get(worldName);
		if(tworld == null){
			return null;
		}
		
		Hashtable<Integer, SagaChunk> tX = tworld.get(x);
		if(tX == null){
			return null;
		}
		
		return tX.get(z);
		
		
	}
	
	/**
	 * Gets the adjacent chunk.
	 * 
	 * @param chunkSide chunk side
	 * @param bukkitChunk bukkit chunk
	 * @return saga chunk. null if not found
	 */
	public SagaChunk getAdjacentSagaChunk(ChunkSide chunkSide, Chunk bukkitChunk) {

		
		String worldName = bukkitChunk.getWorld().getName();
	   	int x = bukkitChunk.getX();
	   	int z = bukkitChunk.getZ();
		
		switch (chunkSide) {
		case FRONT: 
			return ChunkGroupManager.getChunkGroupManager().getSagaChunk(worldName, x - 1, z);
		case LEFT:
			return ChunkGroupManager.getChunkGroupManager().getSagaChunk(worldName, x, z + 1);
		case BACK:
			return ChunkGroupManager.getChunkGroupManager().getSagaChunk(worldName, x +1 , z);
		case RIGHT:
			return ChunkGroupManager.getChunkGroupManager().getSagaChunk(worldName, x, z - 1);	
		default:
			return null;
		}
		
		
	}
	
	/**
	 * Gets chunk group with the given name
	 * 
	 * @param name name
	 * @return chunk group. null if not found
	 */
	public ChunkGroup getChunkGroupWithName(String name) {

		
        Iterator<Integer> i = registeredGroups.keySet().iterator();

        while ( i.hasNext() ) {

            Integer id = i.next();
            ChunkGroup settlement = registeredGroups.get(id);
            if ( settlement.getName().equalsIgnoreCase(name) ) {
                return settlement;
            }

        }

        return null;
        
        
    }
	
	
	// Chunk updates for SagaChunkGroup:
	/**
	 * Removes a chunk.
	 * 
	 * @param sagaChunk saga chunk
	 */
	void removeChunk(SagaChunk sagaChunk) {

		
		Hashtable<Integer, Hashtable<Integer, SagaChunk>> world = sagaChunks.get(sagaChunk.getWorldName());
		
		if(world == null){
			Saga.severe("Tried to remove a non-existan " + sagaChunk + " chunk to group manager chunk shortcut.");
			return;
		}
		
		Hashtable<Integer, SagaChunk> x = world.get(sagaChunk.getX());
		
		if(x == null){
			Saga.severe("Tried to remove a non-existan " + sagaChunk + " chunk to group manager chunk shortcut.");
			return;
		}
		
		SagaChunk z = x.get(sagaChunk.getZ());
		
		if(z == null){
			Saga.severe("Tried to remove a non-existan " + sagaChunk + " chunk to group manager chunk shortcut.");
			return;
		}
		
		// Remove:
		x.remove(sagaChunk.getZ());
		
		// Clean up:
		if(x.isEmpty()){
			world.remove(sagaChunk.getX());
		}
		if(world.isEmpty()){
			sagaChunks.remove(sagaChunk.getWorldName());
		}

		
	}
	
	/**
	 * Adds a chunk.
	 * 
	 * @param sagaChunk saga chunk
	 */
	void addChunk(SagaChunk sagaChunk) {

		
		Hashtable<Integer, Hashtable<Integer, SagaChunk>> world = sagaChunks.get(sagaChunk.getWorldName());
		
		if(world == null){
			world = new Hashtable<Integer, Hashtable<Integer,SagaChunk>>();
			sagaChunks.put(sagaChunk.getWorldName(), world);
		}
		
		Hashtable<Integer, SagaChunk> x = world.get(sagaChunk.getX());
		
		if(x == null){
			x = new Hashtable<Integer, SagaChunk>();
			world.put(sagaChunk.getX(), x);
		}
		
		SagaChunk z = x.get(sagaChunk.getZ());
		
		if(z != null){
			Saga.severe("Tried to add an aready existing " + sagaChunk + " to chunk group managers chunk shortcut. Ignoring request.");
			return;
		}
		
		// Add:
		x.put(sagaChunk.getZ(), sagaChunk);
		
		
	}
	
	/**
	 * Removes a chunk group.
	 * 
	 * @param chunkGroup chunk group
	 */
	void removeChunkGroup(ChunkGroup chunkGroup) {

		
		// Remove group:
		if(registeredGroups.remove(chunkGroup.getId()) == null){
			Saga.severe("Tried to remove a non-exsiatant chunk group " + chunkGroup.getId() + ".");
		}
		
		// Unregister chunk group manager:
		chunkGroup.unregisterChunkGroupManager();

		// Remove chunk shortcuts:
		ArrayList<SagaChunk> groupChunks = chunkGroup.getGroupChunks();
		for (int i = 0; i < groupChunks.size(); i++) {
			removeChunk(groupChunks.get(i));
		}
		
		
	}
	
	/**
	 * Adds a chunk group.
	 * 
	 * @param chunkGroup chunk group
	 */
	void addChunkGroup(ChunkGroup chunkGroup) {

		
		// Add group:
		if(sagaChunks.contains(chunkGroup)){
			Saga.severe("Tried to add an already exsiatant chunk group " + chunkGroup.getId() + ".");
		}else{
			registeredGroups.put(chunkGroup.getId(), chunkGroup);
		}

		// Register chunk group manager:
		chunkGroup.registerChunkGroupManager(this);
		
		// Add chunk shortcuts:
		ArrayList<SagaChunk> groupChunks = chunkGroup.getGroupChunks();
		for (int i = 0; i < groupChunks.size(); i++) {
			addChunk(groupChunks.get(i));
		}
		
		
	}

	/**
	 * Gets an unused chunk group ID.
	 * 
	 * @return unused chunk group ID. from 0(exclusive)
	 */
	int getUnusedChunkGroupId() {

		
        Random random = new Random();

        int newId = random.nextInt(Integer.MAX_VALUE);

        while ( newId == 0 || registeredGroups.get(new Integer(newId)) != null ) {
            //Get another random id until we find one that isn't used
            // We also skip 0 because that is a special value that means no faction
            newId = random.nextInt();
        }

        return newId;

        
    }
	
	
	// Load unload:
	/**
	 * Loads the map.
	 * 
	 */
	public static void load(){
		
		ChunkGroupManager manager = new ChunkGroupManager();
		
		// Load:
		String[] ids = WriterReader.getAllChunkGroupIds();
		for (int i = 0; i < ids.length; i++) {
			ChunkGroup element = ChunkGroup.load(ids[i]);
			// Ignore all invalid IDs:
			if(element.getId() < 0){
				Saga.severe("Can't load " + element + " chunk group, because it has an invalid ID. Ignoring request.");
				continue;
			}
			// Add to manager:
			manager.addChunkGroup(element);
		}
		
		// Set instance:
		instance = manager;
		
		
	}
	
	/**
	 * Unloads the map.
	 * 
	 */
	public static void unload() {


		// Save:
		Collection<ChunkGroup> elements = getChunkGroupManager().registeredGroups.values();
		for (ChunkGroup element : elements) {
			element.save();
		}
		
		instance = null;
		
		
	}

	
}
