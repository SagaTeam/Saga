/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.*;
import org.saga.chunkGroups.ChunkGroupManager;
import org.saga.chunkGroups.SagaChunk;
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

        SagaPlayer sagaPlayer = plugin.getSagaPlayer(event.getPlayer().getName());
    	if(sagaPlayer == null){
    		Saga.warning("Can't continue with onPlayerJoin, because the saga player isn't loaded.", event.getPlayer().getName());
    		return;
    	}
        sagaPlayer.playerJoinEvent(event);
        
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

    	
    	SagaPlayer sagaPlayer = plugin.getSagaPlayer(event.getPlayer().getName());
    	if(sagaPlayer == null){
    		Saga.warning("Can't continue with onPlayerMove, because the saga player isn't loaded.", event.getPlayer().getName());
    		return;
    	}
    	
    	sagaPlayer.playerMoveEvent(event);
    	
        
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	
    	

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    	
    	try {
			SagaPlayer sagaPlayer = Saga.plugin().getLoadedSagaPlayer(event.getPlayer().getName());
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

    
    
    
    
    /**
     * Called by the saga player when one of projectile shoot methods are called.
     * 
     * @param event event
     */
    public void onSagaPlayerProjectileShot(SagaPlayerProjectileShotEvent event) {

    	
    	// Send back to saga player:
    	event.getSagaPlayer().sagaPlayerProjectileShotEvent(event);
    	
    	
	}
    
   
    
    /**
     * Used when a saga player uses its projectile shoot methods.
     * 
     * @author andf
     *
     */
    public static class SagaPlayerProjectileShotEvent extends Event implements Cancellable{

    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Projectile type.
		 */
		private ProjectileType projectileType;
		
		/**
    	 * Projectile speed.
    	 */
    	private double speed;
    	
    	/**
    	 * Saga player.
    	 */
    	private final SagaPlayer sagaPlayer;
    	
    	/**
    	 * If true, the event will be canceled.
    	 */
    	private boolean isCanceled = false;
    	
    	
		/**
		 * Sets a projectile and a saga player.
		 * 
		 * @param who saga player
		 * @param projectileType projectile type
		 * @param speed projectile speed
		 */
		public SagaPlayerProjectileShotEvent(SagaPlayer who, ProjectileType projectileType, double speed) {
			
			
			super("saga player projectile shot");
			this.speed = speed;
			this.projectileType = projectileType;
			this.sagaPlayer = who;
			
			
		}
		
		
		/**
		 * Returns the projectile type.
		 * 
		 * @return projectile type
		 */
		public ProjectileType getProjectileType() {
			return projectileType;
		}
		
		/**
		 * Gets projectile speed.
		 * 
		 * @return projectile speed
		 */
		public double getSpeed() {
			return speed;
		}
		
		/**
		 * Sets projectile speed
		 * 
		 * @param speed projectile speed
		 */
		public void setSpeed(double speed) {
			this.speed = speed;
		}
		
		/**
		 * Increases projectile speed
		 * 
		 * @param amount amount
		 */
		public void increaseSpeed(double amount) {
			this.speed += amount;
		}
		
		/**
		 * Decrease projectile speed
		 * 
		 * @param amount amount
		 */
		public void decreaseSpeed(double amount) {
			this.speed -= amount;
		}

		/**
		 * Gets the saga player that shot the event.
		 * 
		 * @return saga player
		 */
		public SagaPlayer getSagaPlayer() {
			return sagaPlayer;
		}
		
		/* 
		 * (non-Javadoc)
		 * 
		 * @see org.bukkit.event.Cancellable#isCancelled()
		 */
		@Override
		public boolean isCancelled() {
			return isCanceled;
		}

		/* 
		 * (non-Javadoc)
		 * 
		 * @see org.bukkit.event.Cancellable#setCancelled(boolean)
		 */
		@Override
		public void setCancelled(boolean cancel) {
			isCanceled = cancel;
		}
    	
		
		 /**
		 * Check if the event launches a projectile.
		 * 
		 * @param event
		 * @return
		 */
		public static boolean checkIfProjectile(PlayerInteractEvent event){
		    	
		    	
		    	Player player = event.getPlayer();
		    	Material itemInHand = player.getItemInHand().getType();
		    	if(itemInHand.equals(Material.BOW) && player.getInventory().contains(Material.ARROW)){
		    		System.out.println("OI BOW AND ARROW");
		    		return true;
		    	}
		    	
		    	return false;
		    	
		    	
		    }
		
		
		/**
		 * Projectile type.
		 * 
		 * @author andf
		 *
		 */
		public enum ProjectileType{
			
			NONE,
			ARROW,
			FIREBALL,
			
		}
    	
		
    }

    
}
