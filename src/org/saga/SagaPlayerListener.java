/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import org.bukkit.event.block.Action;
import org.bukkit.event.Event;
import org.bukkit.event.player.*;
import org.saga.exceptions.SagaPlayerNotLoadedException;

/**
 *
 * @author Cory
 */
public class SagaPlayerListener extends PlayerListener {

    protected Saga plugin;

    public SagaPlayerListener(Saga plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        String[] split = event.getMessage().split(" ");

        if ( plugin.handleCommand(event.getPlayer(), split, event.getMessage()) ) {
            event.setCancelled(true);
        }

    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {

        plugin.addPlayer(event.getPlayer());

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

        plugin.removePlayer(event.getPlayer().getName());

    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {

    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {


    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	
    	

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    	
    	try {
			SagaPlayer sagaPlayer = Saga.plugin().getSagaPlayer(event.getPlayer().getName());
			//Left click:
			if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				sagaPlayer.leftClickInteractEvent(event);
			}
			//Right click:
			else if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				sagaPlayer.rightClickInteractEvent(event);
			}
		} catch (SagaPlayerNotLoadedException e) {
			Saga.severe("Tried to send interact event to a not loaded player. Send canceled.", event.getPlayer().getName());
		}
		
    	
    }

}
