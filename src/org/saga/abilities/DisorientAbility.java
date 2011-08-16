package org.saga.abilities;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnDamagedEntity;
import org.saga.constants.PlayerMessages;
import org.saga.professions.Profession;

public class DisorientAbility extends AbilityFunction implements OnDamagedEntity{

	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "disorient";

	/**
	 * Multiplier function x1.
	 */
	transient private Short disorientFunctionX1;
	
	/**
	 * Multiplier function y1.
	 */
	private Double disorientFunctionY1;
	
	/**
	 * Multiplier function x2.
	 */
	private Short disorientFunctionX2;
	
	/**
	 * Multiplier function y2
	 */
	private Double disorientFunctionY2;
	
	
	/**
	 * Used by gson.
	 * 
	 */
	public DisorientAbility() {
		
        super(ABILITY_NAME, AbilityActivateType.TRIGGER);
	
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.AbilityFunction#completeSecondExtended()
	 */
	@Override
	public boolean completeSecondExtended() {
		
		
		boolean integrity = true;
		
		// General fields:
		// Fields:
		if(disorientFunctionY1==null){
			disorientFunctionY1 = 1.0;
			Saga.info("Setting default value for "+getAbilityName()+" ability disorientFunctionY1.");
			integrity = false;
		}
		if(disorientFunctionX2==null){
			disorientFunctionX2 = 1000;
			Saga.info("Setting default value for "+getAbilityName()+" ability disorientFunctionX2.");
			integrity = false;
		}
		if(disorientFunctionY2==null){
			disorientFunctionY2 = 1.0;
			Saga.info("Setting default value for "+getAbilityName()+" ability disorientFunctionY2.");
			integrity = false;
		}
		
		// Set fields:
		disorientFunctionX1 = minimumLevel();
		
		return integrity;
		
		
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnActivateAbility#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession)
	 */
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, EntityDamageByEntityEvent event) {
		
		
		// Fail if no damage done:
		// event.isCancelled() || 
		if(event.getDamage() <= 0){
			sagaPlayer.sendMessage( PlayerMessages.abilityUseFailedOn(event.getEntity(), this) );
			return;
		}
		
		setRandomDirection(event.getEntity(), 90);
		
		// Modify damage:
		int damage = new Double(event.getDamage() + calculateFunctionValue(level)).intValue();
		if(damage < Saga.attributeInformation().minimumAttributeDamage){
			damage = Saga.attributeInformation().minimumAttributeDamage;
		}
		event.setDamage(damage);
		
		// Send messages:
		if(event.getEntity() instanceof Player){
			((Player) event.getEntity()).sendMessage(PlayerMessages.entityUsedNegativeAbilityOnYou(event.getDamager(), this));
		}
		sagaPlayer.sendMessage(PlayerMessages.youUsedAbilityOnEntity(event.getEntity(), this));
		
		
	}
	
	/**
	 * Gives the entity a disorientation. Yaw will have a maximum value, pitch will be random.
	 * 
	 * @param entity entity
	 * @param spreadRadius radius
	 */
	private void setRandomDirection(Entity entity, double spreadRadius) {
		
		
		// Get direction:
		Location location = entity.getLocation();
		
		// New yaw and pitch:
		Random random = new Random();
		double pitchRadius = (spreadRadius)/2 * random.nextDouble();
		double yawRadius = spreadRadius;
		if(random.nextBoolean()){
			pitchRadius *= -1;
		}
		if(random.nextBoolean()){
			yawRadius *= -1;
		}
		float yaw = new Double(location.getYaw() + pitchRadius).floatValue();
		float pitch = new Double(location.getPitch() + yawRadius).floatValue();
		
		// Correct yaw:
		if(yaw > 360){
			while(yaw > 360){
				yaw -= 360;
			}
		}else if(yaw < -360){
			while(yaw < -360){
				yaw += 360;
			}
		}
		
		// Correct pitch:
		if(pitch > 360){
			while(pitch > 360){
				pitch -= 360;
			}
		}else if(pitch < -360){
			while(pitch < -360){
				pitch += 360;
			}
		}
		
		// Set location:
		location.setYaw(yaw);
		location.setPitch(pitch);
		entity.teleport(location);
		
		
	}
	
	/**
	 * Calculates the disorient multiplier for the given level.
	 * 
	 * @param level level
	 */
	protected Double calculateDisorientFunctionValue(Short level) {
		if(level>disorientFunctionX2){
			level = disorientFunctionX2;
		}
		
		if(disorientFunctionX2-disorientFunctionX1==0){
			Saga.severe(getAbilityName() + " ability has an undefined or infinite slope. Returning disorientFunction value 1.0.");
			return 1.0;
		}
		
		double k= (disorientFunctionY2 - disorientFunctionY1)/(disorientFunctionX2-disorientFunctionX1);
		double b= disorientFunctionY1 - k * disorientFunctionX1;
		return new Double(k * level + b);
		
		
	}

	
}
