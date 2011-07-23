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
public class SagaFactionManager {

    static private SagaFactionManager instance;

    private HashMap<Integer,SagaFaction> factions;
    private HashMap<String,SagaFactionPlayer> players;

    private SagaFactionManager() {

    }

    static public SagaFactionManager instance() {

        if ( instance == null ) {
            instance = new SagaFactionManager();
        }

        return instance;

    }

    public int getUnusedFactoinId() {

        Random random = new Random();

        int newId = random.nextInt();

        while ( newId == 0 || factions.get(new Integer(newId)) != null ) {
            //Get another random id until we find one that isn't used
            // We also skip 0 because that is a special value that means no faction
            newId = random.nextInt();
        }

        return newId;

    }

    public SagaFactionPlayer getFactionPlayer(String name) {

        SagaFactionPlayer player = players.get(name);

        if ( player == null ) {

            //Createa  new saga player
            player = new SagaFactionPlayer(name);
            players.put(name, player);

        }

        return player;

    }

    public SagaFaction getFaction(int factionId) {

        return factions.get(factionId);

    }

    

}
