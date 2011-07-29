package org.saga.pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SagaPatternCheckElement extends SagaPatternElement {

	
	/**
	 * Action taken by the check.
	 */
	private final CheckAction checkAction;
	
	/**
	 * Materials to check.
	 */
	private final Material[] checkMaterials;
	
	/**
	 * The boolean value returned by the check
	 */
	private final boolean checkReturn;
	
	/**
	 * Sets offsets and level.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param minimumLevel minimum level. if -1 then the check will be ignored
	 * @param maximumLevel maximum level. if -1 then the check will be ignored
	 * @param checkAction action taken
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Short minimumLevel, Short maximumLevel, Material[] checkMaterials, boolean checkReturn, CheckAction checkAction) {
		super(xOffset, yOffset, zOffset, minimumLevel, maximumLevel);
		this.checkMaterials = checkMaterials;
		this.checkReturn = checkReturn;
		this.checkAction = checkAction;
	}
	
	/**
	 * Sets offsets. Levels are set to ignored. Check return is set to true.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param checkMaterials materials to check
	 * @param checkAction action taken
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Material[] checkMaterials, CheckAction checkAction) {
		
		this(xOffset , yOffset , zOffset , (short)(-1) , (short)(-1), checkMaterials, true, checkAction);
		
	}
	
	/**
	 * Sets offsets. Levels are set to ignored. Check return is set to true.
	 * 
	 * @param xOffset x offset
	 * @param yOffset y offset
	 * @param zOffset z offset
	 * @param checkMaterials materials to check
	 * @param checkReturn return of the check
	 * @param checkAction action taken
	 */
	public SagaPatternCheckElement(int xOffset, int yOffset, int zOffset, Material[] checkMaterials, boolean checkReturn, CheckAction checkAction) {
		
		this(xOffset , yOffset , zOffset , (short)(-1) , (short)(-1), checkMaterials, checkReturn, checkAction);
		
	}
	

	@Override
	public boolean execute(Block previousBlock, SagaPatternInitiator initiator) {
		
		
		if(checkAction.equals(CheckAction.TERMINATE) && check(previousBlock, initiator)){
			return false;
		}
		return true;
		
	}
	
	/**
	 * Checks if the block matches any one from the given material list.
	 * 
	 * @param previousBlock previous block
	 * @param initiator initiator
	 * @return checkReturn if at least one block matches
	 */
	public boolean check(Block previousBlock, SagaPatternInitiator initiator) {
		
		for (int i = 0; i < checkMaterials.length; i++) {
//			System.out.println(previousBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator)).getType()+" vs "+checkMaterials[i]);
			if(previousBlock.getRelative(getxOffset(initiator), getyOffset(initiator), getzOffset(initiator)).getType().equals(checkMaterials[i])){
//				System.out.println("returning: "+ checkReturn);
				return checkReturn;
			}
		}
//		System.out.println("returning: "+ !checkReturn);
		
		return !checkReturn;

		
	}
	
	
	/**
	 * Returns the action that needs to be performed when the check returns true.
	 * 
	 * @return action to be performed
	 */
	public CheckAction getCheckAction() {
		return checkAction;
	}
	
	/**
	 * Action taken by the check.
	 * Ignore will skip the next element, break will stop the loop and terminate is used when checking if the pattern should be run at all.
	 * 
	 *
	 */
	public static enum CheckAction{
		
		IGNORE,
		BREAK,
		TERMINATE;
		
	}
	

}
