package org.saga.pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class SagaPatternCheckElement extends SagaPatternElement {

	
	/**
	 * Materials to check.
	 */
	private final MaterialData[] checkMaterialData;

	
	/**
	 * Sets offsets, levels and materials. Offsets returned are zero by default.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param minimumLevel minimum level. if -1 then the check will be ignored
	 * @param maximumLevel maximum level. if -1 then the check will be ignored
	 * @param checkMaterialData material data
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel, MaterialData[] checkMaterialData) {
		
		
		super(xOffset, yOffset, zOffset, minimumLevel, maximumLevel);
		this.checkMaterialData = checkMaterialData;
		
		
	}
	
	/**
	 * Sets offsets, levels and materials. Offsets returned are zero by default.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param minimumLevel minimum level. if -1 then the check will be ignored
	 * @param maximumLevel maximum level. if -1 then the check will be ignored
	 * @param checkMaterials materials
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel, Material[] checkMaterials) {
		
		
		super(xOffset, yOffset, zOffset, minimumLevel, maximumLevel);
		this.checkMaterialData = new MaterialData[checkMaterials.length];
		for (int i = 0; i < checkMaterials.length; i++) {
			checkMaterialData[i] = new MaterialData(checkMaterials[i]);
		}
		
		
	}
	
	/**
	 * Sets offsets and a material.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Material checkMaterial) {
		
		this(xOffset , yOffset , zOffset , (short)(-1) , (short)(-1), new Material[]{checkMaterial});
		
	}

	/**
	 * Sets offsets and materiala.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Material[] checkMaterials) {
		
		this(xOffset , yOffset , zOffset , (short)(-1) , (short)(-1), checkMaterials);
		
	}
	
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#execute(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public boolean execute(Block anchorBlock, SagaPatternInitiator initiator) {

		return false;
		
	}
	
	/**
	 * Checks if the block matches any one from the given material list.
	 * 
	 * @param anchorBlock block to check relative to
	 * @param initiator initiator
	 * @return checkReturn if at least one block matches
	 */
	public boolean check(Block anchorBlock, SagaPatternInitiator initiator) {
		
		
		for (int i = 0; i < checkMaterialData.length; i++) {
			MaterialData metadata = anchorBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator)).getState().getData();
			if(metadata.equals(checkMaterialData[i])){
				return true;
			}
		}
		return false;

		
	}
	

}
