package org.saga.chunkGroups;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.saga.SagaPlayer;
import org.saga.factions.SagaFaction;
import org.saga.utility.AsciiCompass;
import org.saga.utility.TextUtil;



public class SagaMap {

	
	/**
     * The map is relative to a coord and a faction
     * north is in the direction of decreasing x
     * east is in the direction of decreasing z
     */
    public static ArrayList<String> getMap(SagaPlayer player, SagaFaction sagaFaction, Location location) {

        ArrayList<String> ret = new ArrayList<String>();

        int halfWidth = 38 / 2;
        int halfHeight = 8 / 2;

        double inDegrees = location.getYaw();

        Chunk locationChunk = location.getWorld().getChunkAt(location);
        int topLeftX = locationChunk.getX() - halfHeight;
        int topLeftZ = locationChunk.getZ() + halfWidth;
        
        
        ret.add(TextUtil.titleize("MAP " + locationChunk.toString()));

        int width = halfWidth * 2 + 1;
        int height = halfHeight * 2 + 1;

        // For each row
        for (int dx = 0; dx < height; dx++) {
                // Draw and add that row
                String row = "";
                for (int dz = 0; dz > -width; dz--) {

                		SagaChunk chunk = ChunkGroupManager.getChunkGroupManager().getSagaChunk(location.getWorld().getName(), topLeftX + dx, topLeftZ + dz);
                        ChunkGroup settlement = null;

                        if ( chunk != null ) {
                            settlement = chunk.getChunkGroup();
                        }

                        String color = "" + ChatColor.GRAY;
                        String symbol = "?";
                        
                        if(chunk == null){
                        	symbol = "-";
                		}
                        if( dx == halfHeight && dz == -halfWidth ) {
                            //You are here
                            symbol = "O";
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
