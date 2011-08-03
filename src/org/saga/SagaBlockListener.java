package org.saga;

import java.util.ArrayList;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.exceptions.SagaPlayerNotLoadedException;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternInitiator;
import org.saga.pattern.SagaPatternListElement;

public class SagaBlockListener extends BlockListener{

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
		
		if(Saga.plugin().isSagaPlayerLoaded(event.getPlayer().getName())){
			try {
				Saga.plugin().getSagaPlayer(event.getPlayer().getName()).brokeBlockEvent(event);
			} catch (SagaPlayerNotLoadedException e) {
				e.printStackTrace();
			}
		}else{
			Saga.warning("Cant send an event for a not loaded player. Ignoring event.", event.getPlayer().getName());
		}
		
		
	}
	
	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		
		
		if(Saga.plugin().isSagaPlayerLoaded(event.getPlayer().getName())){
			try {
				Saga.plugin().getSagaPlayer(event.getPlayer().getName()).damagedBlockEvent(event);
			} catch (SagaPlayerNotLoadedException e) {
				e.printStackTrace();
			}
		}else{
			Saga.warning("Cant send an event for a not loaded player. Ignoring event.", event.getPlayer().getName());
		}
		
		
	}
	
}
