package org.saga.pattern;

import org.bukkit.block.Block;

public abstract class SagaPatternElement {

	
	/**
	 * x offset.
	 */
	private int xOffset;
	
	/**
	 * y offset.
	 */
	private int yOffset;
	
	/**
	 * z offset.
	 */
	private int zOffset;

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
	 * @param anchorBlock block to check relative to
	 * @param initiator initiator instance
	 * @return true if a termination was encountered
	 */
	public abstract boolean execute(Block anchorBlock , SagaPatternInitiator initiator);
	
	
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
	 * Gets a new anchor block. Used by special elements that need to move the anchor.
	 * Others should return the same block.
	 * 
	 * @param oldAnchorBlock previous block
	 * @param initiator initiator
	 * @return
	 */
	public Block shiftAnchorBlock(Block oldAnchorBlock, SagaPatternInitiator initiator) {
		
		return oldAnchorBlock;
		
	}
	
	
	

	/**
	 * Returns unaltered x offset.
	 * 
	 * @return x offset
	 */
	protected final int getxUnalteredOffset() {
		
		return xOffset;
		
	}
	
	/**
	 * Returns unaltered y offset.
	 * 
	 * @return y offset
	 */
	protected final int getyUnalteredOffset() {
		
		return yOffset;
		
	}
	
	/**
	 * Returns unaltered z offset.
	 * 
	 * @return z offset
	 */
	protected final int getzUnalteredOffset() {
		
		return zOffset;
		
	}
	
	
	/**
	 * Adds an offset. Used by list element.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 */
	protected void addOffset(int xOffset, int yOffset, int zOffset) {

		this.xOffset += xOffset;
		this.yOffset += yOffset;
		this.zOffset += zOffset;
		
	}
	
	
	
}
