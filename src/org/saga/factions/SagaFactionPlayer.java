/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.factions;

import org.saga.*;

import java.util.*;
import org.bukkit.entity.*;

/**
 *
 * @author Cory
 */
public class SagaFactionPlayer {

    private String name;
    private int factionId;
    private HashMap<String,String> properties;

    public SagaFactionPlayer(String name) {
        this.name = name;
        factionId = 0;
        properties = new HashMap<String,String>();
    }

    public Player getBukkitPlayer() {
        return Saga.plugin().getServer().getPlayer(this.name);
    }

    public SagaPlayer getSagaPlayer() {
        return Saga.plugin().getPlayer(name);
    }

    public void setProperty(String property,String value) {
        properties.put(property, value);
    }

    public void setProperty(String property,boolean value) {
        properties.put(property, String.valueOf(value));
    }

    public boolean getProperty(String property) {

        String value = properties.get(property);

        if ( value == null ) {
            return false;
        }

        return Boolean.parseBoolean(value);

    }

    public boolean hasFaction() {

        if ( factionId != 0 ) {
            return true;
        }

        return false;

    }

    public SagaFaction getFaction() {
        return null;
    }

}
