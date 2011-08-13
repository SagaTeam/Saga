package org.saga.professions;

import org.bukkit.Material;
import org.saga.Saga;
import org.saga.abilities.Ability;
import org.saga.abilities.CounterattackAbility;
import org.saga.abilities.DisarmAbility;
import org.saga.abilities.FireballAbility;
import org.saga.abilities.HeavyHitAbility;

public class MageProfession extends Profession {

	
	/**
	 * Profession name
	 */
	transient private static final String PROFESSION_NAME= "mage";
	
	/**
	 * All abilities.
	 */
	transient private Ability[] ABILITIES = new Ability[]{Saga.balanceInformation().abilities.get(FireballAbility.class.getSimpleName())};
	
	/**
	 * Ability scroll materials.
	 */
	transient private static Material[] ABILITY_SCROLL_MATERIALS = new Material[]{Material.PAPER};
	
	
	/**
	 * Used by gson.
	 * 
	 */
	public MageProfession() {
		
		super(PROFESSION_NAME);
		
	}
	
	@Override
	public void completeExtended() {
		// TODO Auto-generated method stub

	}

	@Override
	public Ability[] getAbilities() {
		return ABILITIES;
	}

	@Override
	public boolean isAbilityActive(Ability ability) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Material[] getAbilityScrollMaterials() {
		// TODO Auto-generated method stub
		return ABILITY_SCROLL_MATERIALS;
	}

	@Override
	public void abilityActivateEvent(Ability ability) {

		// Fireball:
		if(ability.equals(ABILITIES[0])){
			((FireballAbility) ABILITIES[0]).use(getLevel(), sagaPlayer);
		}
		
	}

	@Override
	public void abilityDeactivateEvent(Ability ability)
			throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

}
