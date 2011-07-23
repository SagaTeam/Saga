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
public class SagaSettlementManager {

    static private SagaSettlementManager instance;
    protected HashMap<Integer,SagaSettlement> settlements;

    private SagaSettlementManager() {
        settlements = new HashMap<Integer,SagaSettlement>();
    }

    static public SagaSettlementManager instance() {

        if ( instance == null ) {
            instance = new SagaSettlementManager();
        }

        return instance;

    }

    public SagaSettlement getSettlement(int settlementId) {

        return settlements.get(settlementId);

    }

    public int getUnusedSettlementId() {

        Random random = new Random();

        int newId = random.nextInt();

        while ( newId == 0 || settlements.get(newId) != null ) {
            //Get another random id until we find one that isn't used
            // We also skip 0 because that is a special value that means no settlement
            newId = random.nextInt();
        }

        return newId;

    }

}
