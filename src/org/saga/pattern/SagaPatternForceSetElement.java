package org.saga.pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SagaPatternForceSetElement extends SagaPatternElement {

	/**
	 * material to be set.
	 */
	private Material setMaterial;
	
	
	/**
	 * Sets all requirements and material.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param minimumLevel minimum level. if -1 then the check will be ignored
	 * @param maximumLevel maximum level. if -1 then the check will be ignored
	 * @param setMaterial material to be set
	 */
	public SagaPatternForceSetElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel, Material setMaterial) {
		super(xOffset, yOffset, zOffset, minimumLevel, maximumLevel);
		this.setMaterial = setMaterial;
	}
	
	/**
	 * Sets the levels to be ignored.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param setMaterial material to be set
	 */
	public SagaPatternForceSetElement(int xOffset, int yOffset, int zOffset, Material setMaterial) {
		this(xOffset, yOffset, zOffset, (short)-1, (short)-1, setMaterial);
	}
	
	
	@Override
	public boolean execute(Block previousBlock, SagaPatternInitiator initiator) {
		
		
		// Stop if the limit was reached:
		if(!initiator.canModify()){
			return true;
		}
		
		// Tell that a block has been modified:
		initiator.blockModified();
		
		// Force set block:
		System.out.println("setting");
		previousBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator)).setType(setMaterial);

		return true;
		
		
	}

}
