package org.saga.abilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnActivateAbility;
import org.saga.professions.Profession;


public class DischargeAbility extends AbilityFunction implements OnActivateAbility{

	
	/**
	 * Ability name.
	 */
	public static final transient String ABILITY_NAME = "discharge";
	
	/**
	 * Discharge radius.
	 */
	private Integer radius;

	/**
	 * Spread of a single strike.
	 */
	private Double spread;
	
	/**
	 * Random generator.
	 */
	private transient Random random;
	
	
	/**
	 * used by gson.
	 * 
	 */
	public DischargeAbility() {
		
		super(ABILITY_NAME, AbilityActivateType.INSTANTANEOUS);
		
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#completeExtended()
	 */
	@Override
	public boolean completeSecondExtended() {
		
		
		boolean integrity = true;
		
		// General fields:
		if(radius==null){
			radius = 3;
			Saga.info(getAbilityName() + " ability radius field not initialized. Setting default.");
			integrity = false;
		}
		if(spread==null){
			spread = 2.0;
			Saga.info(getAbilityName() + " ability spread field not initialized. Setting default.");
			integrity = false;
		}
		
		random = new Random();
		
		return integrity;
		
		
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnBlockDamage#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.block.BlockDamageEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession) {

		
		// Retrieve nearby entities:
		List<Entity> nearby = sagaPlayer.getNearbyEntities(radius, 5, radius);
		ArrayList<Entity> nearbyCorrected = new ArrayList<Entity>();
		for (Entity entity : nearby) {
			if(sagaPlayer.getDistance(entity.getLocation()) <= radius && entity instanceof LivingEntity){
				nearbyCorrected.add(entity);
			}
		}
		
		// Get shoot count:
		Double shootCount = calculateFunctionValue(level);
		Integer shootCorrected = shootCount.intValue();
		
		// If the last shoot is a fraction add random based on it:
		double fractionPart = shootCount - shootCount.intValue();
		if( random.nextDouble() <= fractionPart ){
			shootCorrected++;
		}
		
//		// Ignore if there are no place for strikes:
//		if(radius <= calmRadius && calmZoneIgnoreChance >= 1){
//			return;
//		}
		
		// Sink everything into the user if no other entities found:
		if(nearbyCorrected.size() == 0){
			for (int i = 0; i < shootCorrected; i++) {
				sagaPlayer.shootLightning(randomDisplacement(spread), true);
			}
			return;
		}
		
		// Shoot everyone on the list:
		for (int i = 0; i < shootCorrected; i++) {
			int nextTarget = new Double(random.nextDouble()*nearbyCorrected.size()).intValue();
			sagaPlayer.shootLightning(displacedLocation(nearbyCorrected.get(nextTarget).getLocation(), spread), false);
		}
		
		
	}

	/**
	 * Creates a new randomly displaced location based on the old one.
	 * 
	 * @param location location
	 * @param spread displacement spread
	 * @return location displaced location
	 */
	private Location displacedLocation(Location location, double spread) {

		
		Vector relVect = new Vector(random.nextDouble()-0.5, 0, random.nextDouble()-0.5);
		relVect.normalize();
		double randomRadius = spread * random.nextDouble();
		relVect.multiply(randomRadius);
		return location.clone().add(relVect.getX(), 0, relVect.getY());
		
		
	}
	
	/**
	 * Creates a new randomly displaced location based on the old one.
	 * 
	 * @param location location
	 * @param spread displacement spread
	 * @return random displacement vector
	 */
	private Vector randomDisplacement(double spread) {

		
		Vector relVect = new Vector(random.nextDouble()-0.5, 0, random.nextDouble()-0.5);
		relVect.normalize();
		double randomRadius = spread * random.nextDouble();
		relVect.multiply(randomRadius);
		return new Vector(relVect.getX(), 0, relVect.getY());
		
		
	}
	

}
