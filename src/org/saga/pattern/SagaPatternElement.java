package org.saga.pattern;

import org.bukkit.block.Block;

public abstract class SagaPatternElement {

	
	/**
	 * x offset.
	 */
	private final int xOffset;
	
	/**
	 * y offset.
	 */
	private final int yOffset;
	
	/**
	 * z offset.
	 */
	private final int zOffset;

	/**
	 * Minimum level to not be ignored.
	 */
	private final Short minimumLevel;
	
	/**
	 * Maximum level to not be ignored.
	 */
	private final Short maximumLevel;
	
	
	/**
	 * Sets offsets and level.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param minimumLevel minimum level. if -1 then the check will be ignored
	 * @param maximumLevel maximum level. if -1 then the check will be ignored
	 */
	public SagaPatternElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.maximumLevel = maximumLevel;
		this.minimumLevel = minimumLevel;
	}
	
	/**
	 * Sets offsets. Levels are set to ignored.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 */
	public SagaPatternElement(int xOffset, int yOffset, int zOffset) {
		this(xOffset , yOffset , zOffset , (short)(-1) , (short)(-1));
	}
	
	
	/**
	 * Executes the pattern. Returns if no termination was encountered.
	 * 
	 * @param previousBlock previous block
	 * @param initiator initiator instance
	 * @return true if a termination not encountered
	 */
	public abstract boolean execute(Block previousBlock , SagaPatternInitiator initiator);
	
	
	/**
	 * Checks if the defined level is in the correct range.
	 * Ignores a check if a level is set to -1.
	 * 
	 * @param initiator initiator
	 * @return true if the level is in the range
	 */
	public boolean isLevelCorrect(SagaPatternInitiator initiator) {
		
		
		if(minimumLevel != -1 && minimumLevel > initiator.getPatternLevel()){
			System.out.println(minimumLevel + " vs " +initiator.getPatternLevel());
			return false;
		}
		if(maximumLevel != -1 && maximumLevel < initiator.getPatternLevel()){
			return false;
		}
		return true;
			
			
	}
	
	/**
	 * Returns rotated x offset.
	 * 
	 * @param initiator initiator instance
	 * @return x offset with rotation
	 */
	public final int getxOffset(SagaPatternInitiator initiator) {
		
		int xOffset= this.xOffset;
		if(initiator.isOrthogonalFlip()){
			xOffset = -xOffset;
		}
		
		if(initiator.getDirection() == 0){
			return xOffset;
		}else if(initiator.getDirection() == 1){
			return -zOffset;
		}else if(initiator.getDirection() == 2){
			return -xOffset;
		}else{
			return zOffset;
		}
		
		
	}
	
	/**
	 * Returns rotated y offset.
	 * 
	 * @param initiator initiator instance
	 * @return y offset with rotation
	 */
	public final int getyOffset(SagaPatternInitiator initiator) {
		return yOffset;
	}
	
	/**
	 * Returns rotated z offset.
	 * 
	 * @param initiator initiator instance
	 * @return z offset with rotation
	 */
	public final int getzOffset(SagaPatternInitiator initiator) {
		
		
		int xOffset= this.xOffset;
		if(initiator.isOrthogonalFlip()){
			xOffset = -xOffset;
		}
		
		if(initiator.getDirection() == 0){
			return zOffset;
		}else if(initiator.getDirection() == 1){
			return xOffset;
		}else if(initiator.getDirection() == 2){
			return -zOffset;
		}else{
			return -xOffset;
		}
		
		
	}
	

	/**
	 * Returns unaltered x offset.
	 * 
	 * @return x offset
	 */
	public final int getxUnalteredOffset() {
		return xOffset;
	}
	
	/**
	 * Returns unaltered y offset.
	 * 
	 * @return y offset
	 */
	public final int getyUnalteredOffset() {
		return yOffset;
	}
	
	/**
	 * Returns unaltered z offset.
	 * 
	 * @return z offset
	 */
	public final int getzUnalteredOffset() {
		return zOffset;
	}
	
	
	
	/**
	 * Gets the block based on previous one.
	 * 
	 * @param previousBlock previous block
	 * @param initiator initiator instance
	 * @return this block
	 */
	public final Block getBlock(Block previousBlock , SagaPatternInitiator initiator){
		return previousBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator));
	}
	
	
	
	
	
}
