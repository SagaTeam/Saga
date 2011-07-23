/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.factions;

import org.saga.utility.*;

import java.util.*;
import org.bukkit.*;

/**
 *
 * @author Cory
 */
public class SagaMap {
    protected HashMap<Integer,HashMap<Integer,SagaChunk>> map;
    protected String world;

    public SagaMap(String worldName) {

        map = new HashMap<Integer,HashMap<Integer,SagaChunk>>();
        this.world = worldName;

    }

    public SagaChunk getChunk(int intX, int intZ) {

        Integer x = Integer.valueOf(intX);
        Integer z = Integer.valueOf(intZ);

        HashMap<Integer,SagaChunk> submap = this.map.get(x);

        if ( submap == null ) {
            submap = new HashMap<Integer,SagaChunk>();
            this.map.put(x, submap);
        }

        SagaChunk chunk = submap.get(z);

        if ( chunk == null ) {
            chunk = new SagaChunk(intX,intZ,world);
            submap.put(z, chunk);
        }

        return chunk;

    }

    public SagaChunk getChunk(Location loc) {

        return getChunk(loc.getWorld().getChunkAt(loc));

    }

    public SagaChunk getChunk(Chunk chunk) {

        return getChunk(chunk.getX(),chunk.getZ());

    }

    /*
    public void prune() {

        Iterator<Integer> i = this.map.keySet().iterator();

        while ( i.hasNext() ) {

            Integer x = i.next();

            HashMap<Integer,LegendsChunk> submap = this.map.get(x);

            Iterator<Integer> j = submap.keySet().iterator();

            while ( j.hasNext() ) {

                Integer z = j.next();

                LegendsChunk chunk = submap.get(z);

                if ( !chunk.shouldSave() ) {
                      j.remove();
                }

            }

            if ( submap.isEmpty() ) {
                i.remove();
            }

        }

    }

    public void fix() {

        Iterator<Integer> i = this.map.keySet().iterator();

        while ( i.hasNext() ) {

            Integer x = i.next();

            HashMap<Integer,LegendsChunk> submap = this.map.get(x);

            Iterator<Integer> j = submap.keySet().iterator();

            while ( j.hasNext() ) {

                Integer z = j.next();

                LegendsChunk chunk = submap.get(z);

                if ( chunk.getSettlementId() != 0 ) {

                    LegendsSettlement settlement = chunk.getSettlement();

                    if ( settlement == null ) {

                        //Settlement missisng Remove settlementId
                        chunk.setSettlementId(0);

                    }

                }

            }

        }

    }*/

    public int size() {

        Iterator<Integer> i = this.map.keySet().iterator();

        int sum = 0;

        while ( i.hasNext() ) {

            Integer x = i.next();

            HashMap<Integer,SagaChunk> submap = this.map.get(x);

            sum += submap.size();

        }

        return sum;

    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String worldName) {
        this.world = worldName;
    }

    /*
    public boolean save() {

        String filename = world + "_Map.json";
        Saga.info("Saving " + filename);
        File file = new File(Legends.instance.getDataFolder(),filename);

        try {

            file.createNewFile();

            DataOutputStream out = new DataOutputStream(new FileOutputStream(file, false));

            out.writeUTF(world);

            int sum = 0;

            for ( Integer x : this.map.keySet() ) {

                //Write x coord
                out.writeInt(x.intValue());

                HashMap<Integer,LegendsChunk> submap = this.map.get(x);

                //Write size of z map
                out.writeInt(submap.size());

                for ( Integer z : submap.keySet() ) {

                    //Write Z value
                    out.writeInt(z.intValue());

                    //get chunk
                    LegendsChunk chunk = submap.get(z);
                    sum += 1;

                    //Write Owner
                    out.writeInt(0);
                    //Write Faction Id
                    out.writeInt(chunk.getFactionId());
                    out.writeInt(chunk.getSettlementId());
                    out.writeBoolean(chunk.build);

                }

            }

            Legends.instance.log.info("Wrote " + Integer.toString(sum) + " chunks!");

            out.close();

        } catch (IOException e) {
            Legends.instance.log.severe(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Legends.instance.log.severe(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean load() {

        String filename = this.world + "_Map.json";
        File file = new File(Legends.instance.getDataFolder(),filename);

        try {

            if ( !file.exists() ) {
                return false;
            }

            //BufferedReader in = new BufferedReader(new FileReader(file));

            DataInputStream in = new DataInputStream(new FileInputStream(file));

            this.map.clear();

            world = in.readUTF();

            int sum = 0;

            while( in.available() != 0 ) {

                int x = in.readInt();

                int size = in.readInt();

                for ( int i = 0; i < size; i++ ) {

                    int z = in.readInt();

                    LegendsChunk chunk = this.getChunkAt(x, z);
                    sum += 1;
                    int hasOwner = in.readInt();

                    if ( hasOwner == 1 ) {

                        //This shouldn't happen anymore
                        Legends.instance.log.warning("Read Has Owner!?!?!");
                        String owner = in.readUTF();
                        //chunk.setOwner(owner);

                    } else {

                        int factionId = in.readInt();
                        int settlementId = in.readInt();
                        boolean build = in.readBoolean();

                        chunk.setFactionId(factionId);
                        chunk.setSettlementId(settlementId);
                        chunk.build = build;

                    }

                }

            }

            Legends.instance.log.info("Read " + Integer.toString(sum) + " chunks!");

            in.close();

        } catch (IOException e) {
            Legends.instance.log.severe(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Legends.instance.log.severe(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;

    }*/


    //----------------------------------------------//
    // Map generation
    //----------------------------------------------//

    /**
     * The map is relative to a coord and a faction
     * north is in the direction of decreasing x
     * east is in the direction of decreasing z
     */
    public ArrayList<String> getMap(SagaFactionPlayer player, Location location) {

        ArrayList<String> ret = new ArrayList<String>();

        int halfWidth = 38 / 2;
        int halfHeight = 8 / 2;

        double inDegrees = location.getYaw();

        SagaChunk chunkLoc = this.getChunk(location);
        SagaChunk topLeft = this.getChunk(chunkLoc.getX() - halfHeight,chunkLoc.getZ() + halfWidth);

        //ChunkLocation chunkLoc = new ChunkLocation(location);
        //ChunkLocation topLeft = new ChunkLocation(chunkLoc.getX() - halfHeight,chunkLoc.getZ() + halfWidth,world);

        ret.add(TextUtil.titleize("MAP " + chunkLoc.toString()));

        SagaFaction faction = player.getFaction();

        int width = halfWidth * 2 + 1;
        int height = halfHeight * 2 + 1;

        // For each row
        for (int dx = 0; dx < height; dx++) {
                // Draw and add that row
                String row = "";
                for (int dz = 0; dz > -width; dz--) {

                        SagaChunk chunk = this.getChunk(topLeft.getX()+dx, topLeft.getZ()+dz);
                        SagaSettlement settlement = null;

                        if ( chunk.isClaimed() ) {
                            settlement = chunk.getSettlement();
                        }

                        String color = "" + ChatColor.GRAY;
                        String symbol = "?";

                        if ( dx == halfHeight && dz == -halfWidth ) {
                            //You are here
                            symbol = "O";
                        }else if ( chunk.isFactionOwned() && (faction == null || faction.getFactionId() != chunk.getFactionId()) ) {
                            symbol = "+";
                        } else if ( chunk.isFactionOwned() && faction != null && faction.getFactionId() == chunk.getFactionId() ) {
                            symbol = "#";
                        } else if ( !chunk.isClaimed() ) {
                            symbol = "-";
                        }  else if ( settlement != null && settlement.getOwner() != null ) {

                            /*if ( settlement.getOwnerName().equalsIgnoreCase(player.getName()) ) {
                                symbol = "$";
                            } else {*/
                            symbol = settlement.getOwner().toUpperCase().substring(0, 1);
                            //}

                        }

                        if ( chunk.isFactionOwned() ) {
                            SagaFaction chunkFaction = chunk.getFaction();
                            color = "" + chunkFaction.getColor();
                        } else if ( chunk.isClaimed() ) {

                            //This is a user owned settlement
                            color = "" + ChatColor.GOLD;
                        }

                        row += color + symbol;

                }
                ret.add(row);
                
        }

        // Get the compass
        ArrayList<String> asciiCompass = AsciiCompass.getAsciiCompass(inDegrees, ChatColor.RED, ChatColor.GOLD);

        // Add the compass
        ret.set(1, asciiCompass.get(0)+ret.get(1).substring(3*3));
        ret.set(2, asciiCompass.get(1)+ret.get(2).substring(3*3));
        ret.set(3, asciiCompass.get(2)+ret.get(3).substring(3*3));

        return ret;
    }

}
