/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.factions;

import java.util.*;
import org.bukkit.*;

/**
 *
 * @author Cory
 */
public class SagaFaction {

    protected int factionId;
    protected String name;
    protected String prefix;

    //protected ArrayList<SagaFactionPlay> members;

    //Claimed Chunks
    protected int specialClaimPoints;

    //Settlements
    protected int specialSettlementPoints;
    protected ArrayList<Integer> settlements;

    protected Location spawn;
    protected ChatColor color;

    protected HashMap<String,String> properties;

    public String motd;


    public int getFactionId() {
        return factionId;
    }

    public ChatColor getColor() {
        return color;
    }

}
