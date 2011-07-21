package org.saga.professions;

import java.util.Vector;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.BalanceInformation;
import org.saga.PlayerDefaults;
import org.saga.Saga;
import org.saga.SagaPlayer;

public class ProfessionHolder {

	
	/**
	 * Prefix for integrity check.
	 */
	private static String INTEGRITY_CHECK_PREFIX= "profession:";
	

	// Fighter:
	/**
	 * Profession.
	 */
	private FighterProfession fighter;
	
	/**
	 * True if profession is selected.
	 */
	private Boolean fighterSelected;
	
	// Woodcutter:
	/**
	 * Profession.
	 */
	private WoodcutterProfession woodcutter;
	
	/**
	 * True if fighter is selected.
	 */
	private Boolean woodcutterSelected;
	
	
	
	/**
	 * All professions.
	 */
	transient public Profession[] allProfessions;

	/**
	 * All selected professions.
	 */
	transient public Boolean[] allSelectedProfessions;

	
	// Initialization:
	/**
	 * Used by gson.
	 */
	public ProfessionHolder() {
	}
	
	/**
	 * Sets access to all required variables.
	 * 
	 * @param plugin plugin
	 * @param sagaPlayer minecraft player
	 */
	public void setAccess(Saga plugin, BalanceInformation balanceInformation, SagaPlayer sagaPlayer) {
		
		
		// Add to all professions:
		for (int i = 0; i < allProfessions.length; i++) {
			allProfessions[i].setAccess(plugin, balanceInformation, sagaPlayer);
		}
		
		
	}
	
	
	// Events:
	/**
	 * Got damaged by living entity event.
	 *
	 * @param pEvent event
	 */
	public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent pEvent) {



	}

	/**
	 * Damaged a living entity.
	 *
	 * @param pEvent event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent pEvent) {



	}

	/**
	 * Left clicked.
	 *
	 * @param pEvent event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Player placed a block event.
	 *
	 * @param pEvent event
	 */
	public void placedBlockEvent(BlockPlaceEvent pEvent) {



	}

	/**
	 * Player broke a block event.
	 *
	 * @param pEvent event
	 */
	public void brokeBlockEvent(BlockBreakEvent pEvent) {



	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {


	}
	
	
	// Integrity check:
	/**
	 * Checks the integrity of the player information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	public Boolean checkIntegrity(Vector<String> problematicFields) {
		
		
		// Initialize professions:
		if(allProfessions==null){
			allProfessions= new Profession[2];
		}
		if(allSelectedProfessions==null){
			allSelectedProfessions= new Boolean[2];
		}
		
		// Fighter:
		if(fighter==null){
			fighter= new FighterProfession();
			problematicFields.add(INTEGRITY_CHECK_PREFIX+"fighter");
			fighter.checkIntegrity(new Vector<String>());
		}
		allProfessions[0]=fighter;
		if(fighterSelected==null){
			fighterSelected=true;
		}
		allSelectedProfessions[0]=fighterSelected;
		
		// Woodcutter:
		if(woodcutter==null){
			woodcutter= new WoodcutterProfession();
			problematicFields.add(INTEGRITY_CHECK_PREFIX+"woodcutter");
			woodcutter.checkIntegrity(new Vector<String>());
		}
		allProfessions[1]=woodcutter;
		if(woodcutterSelected==null){
			woodcutterSelected=true;
		}
		allSelectedProfessions[1]=woodcutterSelected;
		
		
		
		// Check all Professions:
		for (int i = 0; i < allProfessions.length; i++) {
			allProfessions[i].checkIntegrity(problematicFields);
		}
		
		
		return problematicFields.size()!=0;
		
		
		
	}
	
	
	
}
