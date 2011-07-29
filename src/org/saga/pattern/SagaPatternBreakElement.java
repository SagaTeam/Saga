package org.saga.pattern;

import org.bukkit.block.Block;

public class SagaPatternBreakElement extends SagaPatternElement {

	/**
	 * Sets offsets.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param minimumLevel minimum level. if -1 then the check will be ignored
	 * @param maximumLevel maximum level. if -1 then the check will be ignored
	 */
	public SagaPatternBreakElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel) {
		super(xOffset, yOffset, zOffset, minimumLevel, maximumLevel);
	}
	
	/**
	 * Sets offsets. Levels are set to ignored.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 */
	public SagaPatternBreakElement(int xOffset, int yOffset, int zOffset) {
		this(xOffset , yOffset , zOffset , (short)(-1) , (short)(-1));
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaSingleElement#execute(org.bukkit.block.Block)
	 */
	@Override
	public boolean execute(Block previousBlock, SagaPatternInitiator initiator) {
		
		Block currentBlock = previousBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator));
		
		// Ignore if can't edit blocks anymore:
		if(!initiator.canModify()) return true;
		
		
		// Edit only when the pattern level is correct:
		if(isLevelCorrect(initiator)){
			SagaPatternInitiator.destroyBlock(currentBlock, 1);
			initiator.blockModified();
		}
		
		return true;
		
		
	}


	
}
