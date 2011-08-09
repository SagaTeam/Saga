package org.saga.pattern;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.saga.Saga;


public class SagaPatternInitiator {

	
	/**
	 * Blocks left that can be modified.
	 */
	private int blocksLeft;
	
	/**
	 * Direction
	 */
	transient private int direction = 0;
	
	/**
	 * Will be flipped orthogonally if set to true
	 */
	transient private boolean orthogonalFlip = false;
	
	/**
	 * Pattern level.
	 */
	transient private int patternLevel=0;
	
	/**
	 * Pattern element.
	 */
	private SagaPatternElement pattenElement;
	
	/**
	 * Initiates and executes the pattern.
	 * 
	 * @param blockLimit block limit
	 * @param currentBlock current block
	 * @param patternElement pattern element to execute on
	 */
	public SagaPatternInitiator(int blockLimit, SagaPatternElement patternElement) {
		
		blocksLeft = blockLimit;
		if(direction>3){
			Saga.warning(direction+ " is not a valid direction. Setting 0.");
			direction=0;
		}
		this.pattenElement = patternElement;

		
	}

	/**
	 * Initiates the pattern. 
	 * 
	 * @param anchorBlock block to initiate the pattern relative to
	 * @param direction direction
	 * @param tangentialFlip true if there should be a tangential flip
	 * @return true if there is a termination
	 */
	public boolean initiate(Block anchorBlock, int direction, boolean tangentialFlip) {
		
		
		this.direction = direction;
		this.orthogonalFlip = tangentialFlip;
		return pattenElement.execute(anchorBlock , this);
		
		
	}
	
	/**
	 * Initiates the pattern for a player.
	 * 
	 * @param anchorLocation location to initiate relative to
	 * @param direction direction
	 * @param patternLevel level of the pattern
	 * @param orthogonalFlip true if there should be a orthogonal flip
	 * @return true if there is a termination
	 */
	public boolean initiateForPlayer(Location anchorLocation, int direction, int patternLevel, boolean orthogonalFlip) {
		
		
		this.direction = direction;
		this.patternLevel = patternLevel;
		this.orthogonalFlip = orthogonalFlip;
		return pattenElement.execute(anchorLocation.getBlock() , this);
			
	}
	
	
	/**
	 * Should be called when a block is modified.
	 * 
	 */
	void blockModified() {
		blocksLeft--;
	}
	
	/**
	 * Check if block limit is reached.
	 * 
	 * @return true if block limit is not reached
	 */
	boolean canModify() {
		return blocksLeft > 0;
	}
	
	/**
	 * Gets the direction.
	 * 
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * True if there should be a orthogonal flip.
	 * 
	 * @return true if flipped
	 */
	public boolean isOrthogonalFlip() {
		return orthogonalFlip;
	}
	
	/**
	 * Gets pattern level.
	 * 
	 * @return pattern level
	 */
	public int getPatternLevel() {
		return patternLevel;
	}
	
	
	
	
}
