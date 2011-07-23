/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.factions;

import org.saga.*;
import org.saga.defaults.*;

import org.bukkit.*;
import org.bukkit.entity.*;


/**
 *
 * @author Cory
 */
public class SagaChunk {
    
    private int x;
    private int z;
    private String world;
    private int factionId;
    private int settlementId;
    private boolean build;

    public SagaChunk(int x, int z,String world) {
        this.x = x;
        this.z = z;
        this.world = world;
        boolean build = false;
    }

    public SagaChunk(Chunk chunk) {
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.world = chunk.getWorld().getName();
        boolean build = false;
    }

    public SagaChunk(Location loc) {
        Chunk chunk = loc.getWorld().getChunkAt(loc);
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.world = chunk.getWorld().getName();
        boolean build = false;
    }

    public boolean shouldSave() {

        if ( factionId != 0 || settlementId != 0 ) {
            return true;
        }

        return false;

    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    //public ChunkLocation getLocation() {
    //    return new ChunkLocation(this);
    //}

    public Chunk getChunk() {
        return Saga.plugin().getServer().getWorld(world).getChunkAt(x, z);
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

    public int getFactionId() {
        return factionId;
    }

    public int getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(int settlementId) {
        this.settlementId = settlementId;
    }

    public boolean isFactionOwned() {

        if ( factionId == 0 ) {
            return false;
        }

        return true;

    }

    public SagaSettlement getSettlement() {

        if ( settlementId == 0 ) {
            return null;
        }

        return SagaSettlementManager.instance().getSettlement(settlementId);

    }

    public SagaFaction getFaction() {
        return SagaFactionManager.instance().getFaction(factionId);
    }

    public boolean isClaimed() {

        if ( settlementId == 0 ) {
            return false;
        }

        return true;

    }

    public boolean sameOwner(SagaChunk chunk) {

        //Both chunks must be claimed for this to return true
        if ( !chunk.isClaimed() || !this.isClaimed() ) {
            return false;
        }

        //Check if we're faction or player owned
        if ( chunk.isFactionOwned() ) {

            if ( chunk.factionId == this.factionId ) {
                return true;
            }

        } else {

            if ( this.settlementId == chunk.settlementId ) {
                return true;
            }

        }

        return false;

    }

    public boolean sameSettlement(SagaChunk chunk) {

        if ( this.getSettlementId() == 0 ) {
            return false;
        }

        if ( chunk.getSettlementId() == this.getSettlementId() ) {
            return true;
        }

        return false;

    }

    public String getEnterMessage() {

        if ( settlementId != 0 ) {
            SagaSettlement settlement = SagaSettlementManager.instance().getSettlement(settlementId);
            return settlement.getName();
        } else {
            return Constants.WILDERNESS_STRING;
        }

    }

    public String getFormattedEnterMessage() {

        ChatColor color = ChatColor.YELLOW;

        if ( settlementId != 0 ) {

            SagaSettlement settlement = SagaSettlementManager.instance().getSettlement(settlementId);

            if ( settlement.getProperty("haven") ) {
                color = Config.havenColor;
            } else if ( settlement.getProperty("arena") ) {
                color = Config.arenaColor;
            }

            return color + settlement.getName();

        } else {
            return Constants.WILDERNESS_STRING;
        }

    }

    public Chunk getBukkitChunk() {
        return Saga.plugin().getServer().getWorld(world).getChunkAt(x, z);
    }

    public void broadcast(String message) {

        Entity[] entities = getBukkitChunk().getEntities();

        for ( int i = 0; i < entities.length; i++ ) {

            if ( entities[i] instanceof Player ) {

                Player player = (Player)entities[i];
                player.sendMessage(message);

            }

        }


    }


}
