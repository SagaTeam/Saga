package org.saga.abilities.types;

import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.SagaPlayer;
import org.saga.professions.Profession;

public interface OnLeftClick extends OnAbility{

	
	/**
	 * Uses the ability
	 * 
	 * @param level level
	 * @param sagaPlayer saga player
	 * @param profession profession
	 * @param event event
	 */
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, PlayerInteractEvent event);

	
}
