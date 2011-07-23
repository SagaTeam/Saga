package org.saga.professions;

import java.util.Vector;

import org.saga.Messages;
import org.saga.PlayerDefaults;
import org.saga.SagaPlayer;
import org.saga.abilities.Ability;

public abstract class Profession {

	
	// General:
	/**
	 * Profession name.
	 */
	private final String professionName= getProfessionName();
	
	// Player information:
	/**
	 * Profession level.
	 */
	private Short level=null;

	/**
	 * Experience for the current level.
	 */
	private Integer levelExperience=null;

	
	// Calculated:
	/**
	 * Stamina drain for the current level.
	 */
	transient private Short[] staminaDrain;

	/**
	 * Activated abilities.
	 */

	transient private Boolean[] activeAbilities;

	
	// Access:
	/**
	 * Minecraft player.
	 */
	transient private SagaPlayer sagaPlayer;
	
	
	// Main:
	/**
	 * Selected ability.
	 */
	transient short selectedAbility=0;
	
	
	
	// Initialization:
	public Profession() {
		
	}
	
	/**
	 * Wraps all required variables.
	 * 
	 * @param sagaPlayer saga player
	 */
	public void setAccess(SagaPlayer sagaPlayer) {
		
		
		this.sagaPlayer= sagaPlayer;
		
		
	}
	
	
	
	
	// Interaction:
	/**
	 * Returns the profession name.
	 * 
	 * @return the profession name
	 */
	public abstract String getProfessionName();
	
	private void selectNextAbility() {

		
		Ability[] professionAbilities= getAbilities();
		
		short selectedNew= (short) (selectedAbility+1);
		while (true) {
			if(selectedNew>=professionAbilities.length){
				selectedNew= 0;
				break;
			}
			if(professionAbilities[selectedNew].levelHighEnough(level)){
				break;
			}
			selectedNew++;
		}
		selectedAbility= selectedNew;
		
		// Send message:
		if(selectedNew==selectedAbility){
			sagaPlayer.sendMessage(Messages.noAbilitiesAvailable());
		}else{
			sagaPlayer.sendMessage(Messages.abilitySelect(professionAbilities[selectedNew]));
		}
		
		
	}
	
	/**
	 * Returns all abilities for the profession.
	 * 
	 * @return all abilities
	 */
	protected abstract Ability[] getAbilities();

	
	// Integrity check after load:
	/**
	 * Checks the integrity of the player information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	public boolean checkIntegrity(Vector<String> problematicFields) {
		
		
		// All fields:
		if(level==null){
			level= PlayerDefaults.level;
			problematicFields.add(getProfessionName()+":"+"level");
		}
		if(levelExperience==null){
			levelExperience= PlayerDefaults.levelExperience;
			problematicFields.add(getProfessionName()+":"+"levelExperience");
		}
		
		// Check extension:
		checkExtensionIntegrity(problematicFields);
		
		return problematicFields.size()==0;

		
	}
	
	/**
	 * Checks the integrity of the player information.
	 * Adds variable names that where problematic.
	 * 
	 * @param problematicFields Vector containing all problematic field names.
	 * @return true, if everything is ok
	 */
	protected abstract boolean checkExtensionIntegrity(Vector<String> problematicFields);
	

}
