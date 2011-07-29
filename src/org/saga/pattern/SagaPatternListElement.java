package org.saga.pattern;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.saga.pattern.SagaPatternCheckElement.CheckAction;

public class SagaPatternListElement extends SagaPatternElement{

	
	/**
	 * Element list
	 */
	private final SagaPatternElement[] elementList;
	
	
	/**
	 * Sets elements.
	 * 
	 * @param elementList element array.
	 */
	public SagaPatternListElement(SagaPatternElement[] elementList) {
		super(calculatexUnalteredOffset(elementList) , calculateyUnalteredOffset(elementList) , calculatezUnalteredOffset(elementList));
		this.elementList = elementList;
		
	}
	
	/**
	 * Uses the array list instead.
	 * 
	 * @param elementList elements
	 */
	public SagaPatternListElement(ArrayList<SagaPatternElement> elementList) {

		this(elementList.toArray(new SagaPatternListElement[elementList.size()]));
	
	}
	
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#execute(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public boolean execute(Block previousBlock, SagaPatternInitiator initiator) {
		
		
		// Loop trough all blocks to get the last one, even if can't edit.
		Block currentBlock= previousBlock;
		boolean skipNext = false;
		for (int i = 0; i < elementList.length && initiator.canModify(); i++) {
			// Skip if the check says so:
			if(!skipNext){
				// Do a check or a execution:
				if(elementList[i] instanceof SagaPatternCheckElement){
					if(((SagaPatternCheckElement) elementList[i]).check(currentBlock, initiator)){
						if(((SagaPatternCheckElement) elementList[i]).getCheckAction().equals(CheckAction.BREAK)){
							break;
						}
						if(((SagaPatternCheckElement) elementList[i]).getCheckAction().equals(CheckAction.IGNORE)){
							skipNext = true;
						}
						if(((SagaPatternCheckElement) elementList[i]).getCheckAction().equals(CheckAction.TERMINATE)){
							return false;
						}
					}
				}else{
					elementList[i].execute(currentBlock, initiator);
				}
			}else{
				skipNext=false;
			}
			
			if(!(elementList[i] instanceof SagaPatternCheckElement)){
				currentBlock = elementList[i].getBlock(currentBlock, initiator);	
			}
			
			
		}
		
		return true;
		
		
	}

	
	/**
	 * Calculates unaltered x offset.
	 * 
	 * @param elements elements
	 * @return offset
	 */
	private static int calculatexUnalteredOffset(SagaPatternElement[] elements){
		
		int xOffset = 0;
		for (int i = 0; i < elements.length; i++) {
			xOffset += elements[i].getxUnalteredOffset();
		}
		return xOffset;
		
	}
	
	/**
	 * Calculates unaltered y offset.
	 * 
	 * @param elements elements
	 * @return offset
	 */
	private static int calculateyUnalteredOffset(SagaPatternElement[] elements){
		
		int yOffset = 0;
		for (int i = 0; i < elements.length; i++) {
			yOffset += elements[i].getyUnalteredOffset();
		}
		return yOffset;
		
	}
	
	/**
	 * Calculates unaltered z offset.
	 * 
	 * @param elements elements
	 * @return offset
	 */
	private static int calculatezUnalteredOffset(SagaPatternElement[] elements){
		
		int zOffset = 0;
		for (int i = 0; i < elements.length; i++) {
			zOffset += elements[i].getzUnalteredOffset();
		}
		return zOffset;
		
	}
	
}
