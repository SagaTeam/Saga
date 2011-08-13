package org.saga.attributes;

import org.bukkit.event.Event;
import org.saga.Saga;
import org.saga.SagaPlayer;

public abstract class Attribute {

	
	/**
	 * Class name used by the loader
	 */
	@SuppressWarnings("unused")
	private final String _className;
	
	
	// General variables:
	/**
	 * Ability name.
	 */
	private String name;
	
	/**
	 * Multiplier function x1.
	 */
	private Short functionX1;
	
	/**
	 * Multiplier function y1.
	 */
	private Double functionY1;
	
	/**
	 * Multiplier function x2.
	 */
	private Short functionX2;
	
	/**
	 * Multiplier function y2
	 */
	private Double functionY2;
	
	
	// Initiation:
	/**
	 * Forces a name.
	 * 
	 * @param name name
	 */
	public Attribute(String name) {
		this._className = getClass().getName();
		this.name = name;
	}
	
	/**
	 * Completes the ability.
	 * 
	 * @return true if integrity check is positive
	 */
	public final boolean complete() {
		
		
		boolean integrity = true;

		if(name==null){
			name = "null";
			Saga.info("Setting default value for attribute name.");
			integrity = false;
		}
		
		if(functionX1==null){
			functionX1 = 0;
			Saga.info("Setting default value for "+getName()+" attribute functionX1.");
			integrity = false;
		}
		if(functionY1==null){
			functionY1 = 0.0;
			Saga.info("Setting default value for "+getName()+" attribute functionY1.");
			integrity = false;
		}
		if(functionX2==null){
			functionX2 = 10;
			Saga.info("Setting default value for "+getName()+" attribute functionX2.");
			integrity = false;
		}
		if(functionY2==null){
			functionY2 = 0.0;
			Saga.info("Setting default value for "+getName()+" attribute functionY2.");
			integrity = false;
		}


		
		
		// Complete extended and return:
		return completeExtended() && integrity;
		
		
	}
	
	/**
	 * Completes the extending class.
	 * 
	 * @return true if integrity check is positive
	 */
	protected abstract boolean completeExtended();
	

	// Interaction:
	/**
	 * Uses the attribute by mutating the event.
	 * 
	 * @param attributeUpgrade attribute upgrade level
	 * @param sagaPlayer saga player
	 * @param event event
	 */
	public abstract void use(Short attributeUpgrade, SagaPlayer sagaPlayer, Event event);
	
	/**
	 * Calculates the attribute value for the given upgraade level.
	 * 
	 * @param upgrade upgrade level
	 */
	public Double calculateValue(Short upgrade) {

		
		if(upgrade > functionX2){
			upgrade = functionX2;
		}
		
		if(functionX2 - functionX1 == 0){
			Saga.severe(getName() + " attribute has an undefined or infinite slope. Returning function value 0.0.");
			return 0.0;
		}
		
		double k= (functionY2 - functionY1) / (functionX2 - functionX1);
		double b= functionY2 - k * functionX2;
		return k * upgrade + b;
		
		
	}
	
	/**
	 * Returns the attribute raw name.
	 * 
	 * @return ability raw name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the name how it should be show to the player
	 * 
	 * @param upgradeLevel level
	 * @param temporaryUpgradeLevel temporary upgrade level
	 * @return ability name
	 */
	public String getName(Short upgradeLevel, Short temporaryUpgradeLevel) {
		return name+"(x" + upgradeLevel + ")";
	}
	
	/**
	 * Rounds to ceiling.
	 * 
	 * @param value value
	 * @return rounded integer
	 */
	protected static Integer ceiling(Double value){
		
		return new Double(Math.ceil(value)).intValue();
		
	}
	
	/**
	 * Rounds to floor.
	 * 
	 * @param value value
	 * @return rounded integer
	 */
	protected static Integer floor(Double value){
		
		return new Double(Math.floor(value)).intValue();
		
	}
	
	
}
