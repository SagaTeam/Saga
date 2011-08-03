package org.saga.pattern;

import org.bukkit.block.Block;

public class SagaPatternSelectionMoveElement extends SagaPatternElement {

	public SagaPatternSelectionMoveElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel) {
		super(xOffset, yOffset, zOffset, minimumLevel, maximumLevel);
	}
	
	public SagaPatternSelectionMoveElement(int xOffset, int yOffset, int zOffset) {
		this(xOffset, yOffset, zOffset, (short)-1, (short)-1);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public boolean execute(Block previousBlock, SagaPatternInitiator initiator) {
		return false;
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#getNewCurrentBlock(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public Block shiftAnchorBlock(Block oldAnchorBlock, SagaPatternInitiator initiator) {
		
		if(isLevelCorrect(initiator)){
//			System.out.println("anchor shift to: ("+(oldAnchorBlock.getX()+getxOffset(initiator))+", "+(oldAnchorBlock.getY()+getyOffset(initiator))+", "+(oldAnchorBlock.getZ()+getzOffset(initiator))+")");
			return oldAnchorBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator));
		}else{
			return oldAnchorBlock;
		}
		
	}
	
	
}
