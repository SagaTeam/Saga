package org.saga;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.abilities.Ability;
import org.saga.abilities.Ability.AbilityActivateType;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class PlayerAbilityManager {

	private Hashtable<Material, ArrayList<Ability>> materialsAbilities = new Hashtable<Material, ArrayList<Ability>>();
	
	private Hashtable<Ability, Profession> abilityProfessions = new Hashtable<Ability, Profession>();
	
	private Hashtable<Ability, Short> abilityTimers = new Hashtable<Ability, Short>();
	
	private Material lastScrollMaterial = null;
	
	private short selectedAbility = -1;
	
	private short abilitySelectedFor = 0;
	
	private SagaPlayer sagaPlayer;
	
	
	/**
	 * Sets saga player. Associates materials with abilities and abilities with professions.
	 * 
	 * @param sagaPlayer saga player
	 * @param activeProfessions active professions
	 */
	public PlayerAbilityManager(SagaPlayer sagaPlayer, Profession[] activeProfessions) {
	
		
		this.sagaPlayer = sagaPlayer;
		
		for (int i = 0; i < activeProfessions.length; i++) {
			
			Ability[] abilities = activeProfessions[i].getAbilities();
			Material[] materials = activeProfessions[i].getAbilityScrollMaterials(); 
			
			// Associate each material with an ability:
			for (int j = 0; j < materials.length; j++) {
				ArrayList<Ability> materialAssociation = materialsAbilities.get(materials[j]);
				if(materialAssociation == null){
					materialAssociation = new ArrayList<Ability>();
					materialsAbilities.put(materials[j], materialAssociation);
				}
				for (int k = 0; k < abilities.length; k++) {
					materialAssociation.add(abilities[k]);
				}
			}
			
			// Associate each ability with a profession:
			for (int j = 0; j < abilities.length; j++) {
				abilityProfessions.put(abilities[j], activeProfessions[i]);
			}
			
		}

	
	}
	
	
	private void scroll(Material scrollMaterial) {

		
		// Reset selection if the old and new materials aren't the same.
		if(!scrollMaterial.equals(lastScrollMaterial)){
			resetSelection();
			lastScrollMaterial = scrollMaterial;
		}
		
		// Get abilities associated with a material:
		ArrayList<Ability> abilities = materialsAbilities.get(scrollMaterial);
		
		// Return if there is no association:
		if(abilities == null){
			return;
		}
		
		short selectedNew = (short) (selectedAbility+1);
		while (true) {
			if(selectedNew >= abilities.size()){
				selectedNew= -1;
				break;
			}
			Short level = abilityProfessions.get(abilities.get(selectedNew)).getLevel();
			if(abilities.get(selectedNew).levelHighEnough(level)){
				break;
			}
			selectedNew++;
		}
		
		// Send message:
		if(selectedNew==-1 && selectedAbility==-1){
			sagaPlayer.sendMessage(PlayerMessages.noAbilitiesAvailable());
		}else if(selectedNew==-1){
			sagaPlayer.sendMessage(PlayerMessages.abilitySelectNone());
		}else{
			sagaPlayer.sendMessage(PlayerMessages.abilitySelect(abilities.get(selectedNew)));
		}
		
		// Set selected ability:
		selectedAbility= selectedNew;
		
		
	}
	
	/**
	 * Resets the selection.
	 * 
	 */
	private void resetSelection() {
		
		
		lastScrollMaterial = null;
		selectedAbility = -1;
		
		
	}
	
	public void activateAbility(Material itemInHand) {

		
		// Ignore if an invalid item is held:
		if(!itemInHand.equals(lastScrollMaterial)){
			resetSelection();
			return;
		}
		
		// Ignore if no ability is selected:
		if(selectedAbility == -1){
			return;
		}
		
		// Get variables:
		Ability ability = materialsAbilities.get(lastScrollMaterial).get(selectedAbility);
		Profession abilityProfession = abilityProfessions.get(ability);
		Short level = abilityProfession.getLevel();
		
		
		
		// Check if there is enough stamina:
		Double staminaUse = ability.calculateStaminaUse(level);
		if(!sagaPlayer.enoughStamina(staminaUse)){
			sagaPlayer.sendMessage(PlayerMessages.notEnoughStamina(ability, sagaPlayer.getStamina(), sagaPlayer.getMaximumStamina(), staminaUse));
			resetSelection();
			return;
		}
		
		// Add timer:
		if(ability.getActivateType().equals(AbilityActivateType.TIMER)){
			abilityTimers.put(ability, ability.calculateAbilityActiveTime(abilityProfession.getLevel()));
		}
		
		// Use required resources:
		sagaPlayer.useStamina(staminaUse);
		
		// Notify the player:
		sagaPlayer.sendMessage(PlayerMessages.abilityActivate(ability));
		
		
		// Activate or toggle:
		if(ability.getActivateType().equals(AbilityActivateType.TOGGLE) && abilityProfession.isAbilityActive(ability)){
			deactivateAbility(ability);
		}else{
			abilityProfession.abilityActivateEvent(ability);
			resetSelection();
		}
		
		
	}
	
	/**
	 * Deactivates an ability if possible.
	 * 
	 * @param ability ability index
	 */
	public void deactivateAbility(Ability ability){
		

		Profession profession = abilityProfessions.get(ability);
		
		// Ignore if none found:
		if(profession == null){
			return;
		}
		
		// Reset timer:
		abilityTimers.remove(ability);
		
		// Notify player:
		sagaPlayer.sendMessage(PlayerMessages.abilityDeactivate(ability));
		
		// Send a deactivate event:
		profession.abilityDeactivateEvent(ability);
		
		
		
	}
	
	/**
	 * Gets the time remaining for the ability.
	 * 
	 * @param ability ability
	 * @return time remaining. -1 if not found
	 */
	public Short getAbilityRemainingTime(Ability ability) {
		
		
		Short time = abilityTimers.get(ability);
		if(time == null){
			return -1;
		}
		return time;
		
		
	}
	
	// Events:
	/**
	 * Left clicked.
	 *
	 * @param event event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent event) {
		
		
		// Activate
		activateAbility(event.getPlayer().getItemInHand().getType());
		
		
	}
	
	/**
	 * Right clicked.
	 *
	 * @param event event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent event) {
	
		
		// Scroll:
		scroll(event.getPlayer().getItemInHand().getType());
		
		
	}
	
	/**
	 * Sends a clock tick.
	 *
	 * @param tick tick number
	 */
	public void clockTickEvent(int tick) {
		
		
		/// Check timers:
		Enumeration<Ability> keys= abilityTimers.keys();
        
        // Decrease timers:
        Ability key;
        Short value;
        while ( keys.hasMoreElements() ) {
        	key = keys.nextElement();
        	value = (short) (abilityTimers.get(key)-1);
        	if(value<=0){
        		deactivateAbility(key);
        		abilityTimers.remove(key);
        	}else{
        		abilityTimers.put(key, value);
        	}
        }
		abilitySelectedFor++;
		if(abilitySelectedFor >= Saga.balanceInformation().abilitySelectedTime){
			abilitySelectedFor = 0;
			resetSelection();
		}
        
        
		
	}
	
}
