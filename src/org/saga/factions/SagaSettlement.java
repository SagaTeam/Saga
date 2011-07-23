/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.factions;

import java.util.*;

/**
 *
 * @author Cory
 */
public class SagaSettlement {

    private int settlementId;
    private int factionId;
    private String owner;
    private String name;
    private HashMap<String,String> properties;
    private HashMap<String,String> builders;

    public SagaSettlement() {
        settlementId = 0;
        factionId = 0;
        properties = new HashMap<String,String>();
        builders = new HashMap<String,String>();
        name = "Unknown Settlement";
        owner = null;
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

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return owner;
    }

}
