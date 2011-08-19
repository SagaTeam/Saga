package org.saga.world;

import java.util.ArrayList;

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
    private String worldName;
    
    /**
     * Faction ID. -1 if invalid. 0 if none.
     */
    private Integer factionId;
    
    /**
     * Settlement ID. -1 if invalid. 0 if none.
     */
    private Integer settlementId;

    /**
     * All builders. First builder is considered as the owner.
     */
    private ArrayList<String> builders;

    
    // Initialization:
    /**
     * Sets everything.
     * 
     * @param x chunk x
     * @param z chunk z
     * @param worldName world name
     * @param factionId faction id
     * @param settlementId settlement id
     */
    public SagaChunk(int x, int z,String worldName, int factionId, int settlementId) {
        
    	
    	this.x = x;
        this.z = z;
        this.worldName = worldName;
        this.factionId = factionId;
        this.settlementId = settlementId;
        
        
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
		if(worldName == null){
			Saga.severe("Saga "+ chunk +" worldName not initialized. Setting default.");
			worldName = "nullworld";
			integrity = false;
		}
		if(factionId == null){
			Saga.severe("Saga "+ chunk +" factionId not initialized. Setting to not owned.");
			factionId = -1;
			integrity=false;
		}
		if(settlementId == null){
			Saga.severe("Saga "+ chunk +" settlementId not initialized. Setting to not owned.");
			settlementId = -1;
			integrity=false;
		}
		if(builders == null){
			Saga.severe("Saga "+ chunk +" builders not initialized. Setting to not owned.");
			builders = new ArrayList<String>();
			integrity=false;
		}
		for (int i = 0; i < builders.size(); i++) {
			if(builders.get(i) == null){
				Saga.severe("Saga "+ chunk +" builder(" + i + ") not initialized. Removing builder.");
				builders.remove(i);
				i--;
				integrity=false;
			}
		}
		
		return integrity;
		
		
	}

	
	
	// Interaction:
	/**
	 * Gets the factionId.
	 * 
	 * @return the factionId
	 */
	public Integer getFactionId() {
		return factionId;
	}

	
	/**
	 * Sets the factionId.
	 * 
	 * @param factionId the factionId to set
	 */
	public void setFactionId(Integer factionId) {
		this.factionId = factionId;
	}

	/**
	 * Gets the settlementId.
	 * 
	 * @return the settlementId
	 */
	public Integer getSettlementId() {
		return settlementId;
	}

	
	/**
	 * Sets the settlementId.
	 * 
	 * @param settlementId the settlementId to set
	 */
	public void setSettlementId(Integer settlementId) {
		this.settlementId = settlementId;
	}

	
	/**
	 * Gets the builders.
	 * 
	 * @return the builders
	 */
	public ArrayList<String> getBuilders() {
		return builders;
	}

	
	/**
	 * Checks if the player is a builder.
	 * 
	 * @param name name
	 * @return true if builder
	 */
	public boolean isBuilder(String name) {

		return builders.contains(name);
		
	}
	
	/**
	 * Sets the builders.
	 * 
	 * @param builders the builders to set
	 */
	public void setBuilders(ArrayList<String> builders) {
		this.builders = builders;
	}

	
	/**
	 * Removes a given builder
	 * 
	 * @param name player name
	 * @return true/false if removed
	 */
	public boolean removeBuilder(SagaPlayer name) {

		return builders.remove(name);
		
	}

	/**
	 * Adds a builder
	 * 
	 * @param name name
	 * @return true/false if ? (I don't have javadoc with me)
	 */
	public boolean addBuilder(String name) {

		return builders.add(name);
		
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
		return worldName;
	}
	
	
	
	
	
}
