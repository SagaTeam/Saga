package org.saga.abilities;

import org.saga.Saga;

public abstract class AbilityFunction extends Ability {

	
	/**
	 * Multiplier function x1.
	 */
	transient private Short functionX1;
	
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
	
	
	/**
	 * Forwards to first constructor.
	 * 
	 * @param name ability name
	 */
	public AbilityFunction(String name) {
		super(name);
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#completeExtended()
	 */
	@Override
	public boolean completeExtended() {
		
		
		boolean integrity = true;
		// Fields:
		if(functionY1==null){
			functionY1 = 1.0;
			Saga.info("Setting default value for "+getAbilityName()+" ability functionY1.");
			integrity = false;
		}
		if(functionX2==null){
			functionX2 = 1000;
			Saga.info("Setting default value for "+getAbilityName()+" ability functionX2.");
			integrity = false;
		}
		if(functionY2==null){
			functionY2 = 1.0;
			Saga.info("Setting default value for "+getAbilityName()+" ability functionY2.");
			integrity = false;
		}

		// Set fields:
		functionX1 = minimumLevel();
		
		return integrity;
		
		
	}
	
	/**
	 * Does a complete on the second extended class.
	 * 
	 * @return integrity check
	 */
	protected abstract boolean completeSecondExtended();
	
	
	/**
	 * Calculates the damage multiplier for the given level.
	 * 
	 * @param level level
	 */
	protected Double calculateFunctionValue(Short level) {
		System.out.println("level:"+level);
		if(level>functionX2){
			level = functionX2;
		}
		
		if(functionX2-functionX1==0){
			Saga.severe(getAbilityName() + " ability has an undefined or infinite slope. Returning function value 1.0.");
			return 1.0;
		}
		
		double k= (functionY2 - functionY1)/(functionX2-functionX1);
		double b= functionY1 - k * functionX1;
		System.out.println("k:"+k+ " b:"+b);
		return new Double(k * level + b);
		
		
	}
	
	

}
