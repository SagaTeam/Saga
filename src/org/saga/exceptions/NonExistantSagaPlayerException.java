/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.exceptions;

/**
 *
 * @author Cory
 */
public class NonExistantSagaPlayerException extends Exception {

    private String playerName;

    public NonExistantSagaPlayerException(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

}
