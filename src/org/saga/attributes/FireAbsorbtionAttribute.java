package org.saga.attributes;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.saga.SagaPlayer;

public class FireAbsorbtionAttribute extends Attribute {

	
	/**
	 * Attribute name.
	 */
	transient private static String ATTRIBUTE_NAME = "fire absorbtion";
	
	
	/**
	 * Sets the name.
	 * 
	 */
	public FireAbsorbtionAttribute() {
		super(ATTRIBUTE_NAME);
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.attributes.Attribute#completeExtended()
	 */
	@Override
	protected boolean completeExtended() {
		return true;
	}
	
	
	/**
	 * Uses the attribute.
	 * 
	 */
	public void use(Short attributeLevel, SagaPlayer sagaPlayer, EntityDamageEvent event) {
		
		
		if(!event.getCause().equals(DamageCause.FIRE_TICK)){
			return;
		}
		int damage = floor(event.getDamage() - calculateValue(attributeLevel));
		if(damage < 0){
			sagaPlayer.regenerateHealth(damage);
			damage = 0;
		}
		event.setDamage(damage);
		System.out.println("!used "+ATTRIBUTE_NAME+" attribute!");
		
		
	}
	
	
}
