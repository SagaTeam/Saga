package org.saga.pattern;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;

public class SagaPatternListElement extends SagaPatternElement{

	
	/**
	 * Element list
	 */
	private final ArrayList<SagaPatternElement> elementList = new ArrayList<SagaPatternElement>();
	
	/**
	 * Current anchor block.
	 */
	transient private Block currentAnchorBlock = null;
	
	/**
	 * If true then shift anchor will give the shifted version
	 */
	private boolean anchorShiftEnabled = true;
	
	// Initialization:
	/**
	 * Sets elements.
	 * 
	 * @param elementList element array.
	 */
	public SagaPatternListElement(SagaPatternElement[] elementList) {
		this();
		for (int i = 0; i < elementList.length; i++) {
			addElement(elementList[i]);
		}
	}
	
	/**
	 * Uses the array list instead.
	 * 
	 * @param elementList elements
	 */
	public SagaPatternListElement() {

		super(0, 0, 0);
	
	}
	
	/**
	 * Adds an element to the list.
	 * 
	 * @param element
	 */
	public void addElement(SagaPatternElement element) {
		elementList.add(element);
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#execute(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public boolean execute(Block anchorBlock, SagaPatternInitiator initiator) {
		
		
		// Loop trough all blocks to get the last one, even if can't edit.
		boolean skipNext = false;
		currentAnchorBlock = anchorBlock;
		for (int i = 0; i < elementList.size(); i++) {
			// Skip if the check says so:
			if(!skipNext){
				// Check element:
				if(elementList.get(i) instanceof SagaPatternLogicElement){
					LogicAction action = ((SagaPatternLogicElement) elementList.get(i)).findAction(currentAnchorBlock, initiator);
					if(action.equals(LogicAction.IGNORE)){
//						System.out.println("skip");
						skipNext = true;
					}
					else if(action.equals(LogicAction.BREAK)){
//						System.out.println("break");
						break;
					}
					else if(action.equals(LogicAction.TERMINATE)){
						System.out.println("terminate");
						return true;
					}
				// Execution element:	
				}else{
					try {
						boolean terminate = elementList.get(i).execute(currentAnchorBlock, initiator);
						if(terminate){
							System.out.println("execute terminate");
							return true;
						}
					} catch (Throwable e) {
//						System.out.println("EXCEPTION:"+e.getClass().getSimpleName());
						return true;
					}
					
				}
				// Shift anchor block:
				currentAnchorBlock = elementList.get(i).shiftAnchorBlock(currentAnchorBlock, initiator);
			}else{
				skipNext=false;
			}
			
		}
		
		return false;
		
		
	}

	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#shiftAnchorBlock(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public Block shiftAnchorBlock(Block oldAnchorBlock, SagaPatternInitiator initiator) {
		
		if(currentAnchorBlock == null || !anchorShiftEnabled){
			return oldAnchorBlock;
		}
		return currentAnchorBlock;
	}

	/**
	 * Disables anchor shift, so that other lists anchor will not be touched by this one.
	 * 
	 * @return this instance
	 */
	public SagaPatternListElement disableAnchorShift() {
		anchorShiftEnabled = false;
		return this;
	}


}
