package org.saga.pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SagaPatternCountElement extends SagaPatternCheckElement {

	
	/**
	 * Count type.
	 */
	private final CountType countType;
	
	/**
	 * Count number.
	 */
	private final int countNumber;
	
	/**
	 * Elements that will be counted.
	 */
	private final SagaPatternCheckElement[] countElements;
	
	/**
	 * Sets offsets, check materials, count type and count number.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param countMaterials materials
	 * @param countType count type
	 * @param countNumber count number
	 */
	public SagaPatternCountElement(SagaPatternCheckElement[] countElements, CountType countType, int countNumber) {
		super(0, 0, 0, new Material[0]);
		this.countType = countType;
		this.countNumber = countNumber;
		this.countElements = countElements;
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternCheckElement#check(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public boolean check(Block anchorBlock, SagaPatternInitiator initiator) {
		
		
		int count = 0;
		for (int i = 0; i < countElements.length; i++) {
			if(countElements[i].check(anchorBlock, initiator)){
				count++;
			}
		}
		
		if(countType.equals(CountType.BIGGER)){
			return count > countNumber;
		}
		if(countType.equals(CountType.BIGGER_EQUAL)){
			return count >= countNumber;
		}
		if(countType.equals(CountType.EQUAL)){
			return count == countNumber;
		}
		if(countType.equals(CountType.SMALLER_EQUAL)){
			return count == countNumber;
		}
		if(countType.equals(CountType.SMALLER)){
			return count == countNumber;
		}
		return false;
		
	}
	
	
	public enum CountType{
		
		BIGGER,
		BIGGER_EQUAL,
		EQUAL,
		SMALLER_EQUAL,
		SMALLER;
		
		
	}
	
}
