package org.saga.chunkGroups;


import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.saga.Saga;
import org.saga.SagaPlayer;

public class SagaChunk {

	
	/**
	 * Chunk x.
	 */
	private Integer x;
	
    /**
     * Chunk z.
     */
    private Integer z;
    
    /**
     * World name.
     */
    private String world;

    /**
     * Chunk group id.
     */
    private Integer chgrId;
    
    // Initialization:
    /**
     * Creates a saga chunk from a bukkit chunk.
     * 
     * @param chunk bukkit chunk
     */
    public SagaChunk(Chunk chunk){
    	
    	this.world = chunk.getWorld().getName();
    	this.x = chunk.getX();
    	this.z = chunk.getZ();
    	this.chgrId = -1;
    	
    }
    
    /**
     * Creates a saga chunk from a location.
     * 
     * @param location location
     */
    public SagaChunk(Location location){
    	
    	this(location.getWorld().getChunkAt(location));
    	
    }
    
	/**
	 * Completes the initialization.
	 * 
	 * @return integrity
	 */
	public boolean complete() {

		
		boolean integrity=true;
		String chunk = "chunk("+ x +"," + z + ")";
		if(x == null){
			Saga.severe("Saga "+ chunk +" x not initialized. Setting default.");
			x= 0;
			integrity = false;
		}
		if(z == null){
			Saga.severe("Saga "+ chunk +" z not initialized. Setting default.");
			z= 0;
			integrity = false;
		}
		if(world == null){
			Saga.severe("Saga "+ chunk +" world not initialized. Setting default.");
			world = "nullworld";
			integrity = false;
		}
		if(chgrId == null){
			Saga.severe("Saga "+ chunk +" chGroup not initialized. Setting default.");
			chgrId = -1;
			integrity = false;
		}
		
		return integrity;
		
		
	}
	
	
	// Interaction:
	/**
	 * Checks if the player can build on the chunk.
	 * 
	 * @param sagaPlayer saga player
	 * @return true if the player can build
	 */
	public boolean canBuild(SagaPlayer sagaPlayer) {
		
		
		// Ask from the chunk group for permission:
		ChunkGroup chunkGroup = ChunkGroupManager.getChunkGroupManager().getChunkGroup(chgrId);
		
		if(chunkGroup == null){
			return false;
		}
		
		return chunkGroup.canBuild(sagaPlayer);
		
		
	}
	
	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}
	
	/**
	 * Gets the z.
	 * 
	 * @return the z
	 */
	public Integer getZ() {
		return z;
	}
	
	/**
	 * Gets the worldName.
	 * 
	 * @return the worldName
	 */
	public String getWorldName() {
		return world;
	}
	
	/**
	 * Gets chunk group ID.
	 * 
	 * @return chunk group ID
	 */
	public Integer getChunkGroupId() {
		return chgrId;
	}
	
	/**
	 * Sets chunk group ID.
	 * 
	 * @param chunk group ID
	 */
	void setChunkGroupId(Integer chunkGroupId) {
		chgrId = chunkGroupId;
	}
	
	/**
	 * Gets chunk group associated with this saga chunk.
	 * 
	 * @return chunk group. null if not found
	 */
	public ChunkGroup getChunkGroup() {
		// TODO SagaChunk should remember a ChunkGroup.
		return ChunkGroupManager.getChunkGroupManager().getChunkGroup(chgrId);
	}
	
	/**
	 * Gets a bukkit chunk associated with the saga chunk.
	 * 
	 * @return bukkit chunk
	 */
	public Chunk getBukkitChunk() {
        return Saga.plugin().getServer().getWorld(world).getChunkAt(x, z);
    }
	
	/**
	 * Broadcasts a message to all entities on the chunk.
	 * 
	 * @param message message
	 */
	public void broadcast(String message) {

        Entity[] entities = getBukkitChunk().getEntities();

        for ( int i = 0; i < entities.length; i++ ) {

            if ( entities[i] instanceof Player ) {

                Player player = (Player)entities[i];
                player.sendMessage(message);

            }

        }


    }
	
	/**
	 * Checks if the bukkit chunk represents a saga chunk.
	 * 
	 * @param bukkitChunk bukkit chunk
	 * @return true if the bukkit chunk represents the saga chunk
	 */
	public boolean represents(Chunk bukkitChunk) {

		
		return bukkitChunk.getX() == x && bukkitChunk.getZ() == z || bukkitChunk.getWorld().getName().equals(world);
		
	}
	
	/**
	 * Sends a refresh for all entities on the chunk.
	 * 
	 */
	public void refresh() {
		
		
		Entity[] entities = getBukkitChunk().getEntities();
		ArrayList<SagaPlayer> sagaPlayers = new ArrayList<SagaPlayer>();
		for (int i = 0; i < entities.length; i++) {
			if(!(entities[i] instanceof Player)){
				continue;
			}
			SagaPlayer sagaPlayer = Saga.plugin().getSagaPlayer(((Player) entities[i]).getName());
			if(sagaPlayer != null){
				sagaPlayers.add(sagaPlayer);
			}
		}
		for (SagaPlayer sagaPlayer : sagaPlayers) {
			sagaPlayer.updateLastSagaChunk();
			System.out.println("update");
		}
		
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		
		if(!(obj instanceof SagaChunk)){
			return false;
		}
		
		if(!world.equals(((SagaChunk)obj).world)){
			return false;
		}
		
		if(x != ((SagaChunk)obj).x){
			return false;
		}
		
		if(z != ((SagaChunk)obj).z){
			return false;
		}
		
		if(chgrId != ((SagaChunk)obj).chgrId){
			return false;
		}
		return true;
		
		
	}
	
	@Override
	public String toString() {
		return "(" + world + " " + x + ", " + z + ")";
	}
	
	public enum ChunkSide{
		
		FRONT,
		LEFT,
		BACK,
		RIGHT,
		
	}
	
}
