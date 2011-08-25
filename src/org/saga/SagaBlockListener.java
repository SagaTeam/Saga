package org.saga;


import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.saga.exceptions.SagaPlayerNotLoadedException;

public class SagaBlockListener extends BlockListener{

	@Override
	public void onBlockBreak(BlockBreakEvent event) {

		
		SagaPlayer loadedPlayer;
		try {
			loadedPlayer = Saga.plugin().getLoadedSagaPlayer(event.getPlayer().getName());
		} catch (SagaPlayerNotLoadedException e) {
			
			return;
		}
		
		if(loadedPlayer != null){
			loadedPlayer.brokeBlockEvent(event);
		}else{
			Saga.warning("Cant send an event for a not loaded player. Ignoring event.", event.getPlayer().getName());
		}
		
		
	}
	
	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		
		
		SagaPlayer loadedPlayer;
		try {
			loadedPlayer = Saga.plugin().getLoadedSagaPlayer(event.getPlayer().getName());
		} catch (SagaPlayerNotLoadedException e) {
			
			return;
		}
		
		if(loadedPlayer != null){
			loadedPlayer.damagedBlockEvent(event);
		}else{
			Saga.warning("Cant send an event for a not loaded player. Ignoring event.", event.getPlayer().getName());
		}
		
		
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {

		
		SagaPlayer sagaPlayer = Saga.plugin().getSagaPlayer(event.getPlayer().getName());
		if(sagaPlayer == null){
			Saga.warning("Cant send an event for a not loaded player. Ignoring event.", event.getPlayer().getName());
		}
		
		// Placed block:
		sagaPlayer.placedBlockEvent(event);
		
	
	}
	
	
}
