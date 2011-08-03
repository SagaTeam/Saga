package org.saga.pattern;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SagaPatternLogicElement extends SagaPatternCheckElement{

	
	/**
	 * Element type.
	 */
	private LogicType logicType;
	
	/**
	 * Action taken by the element.
	 */
	private LogicAction logicAction;

	/**
	 * Current anchor block.
	 */
	transient private Block currentAnchorBlock = null;
	
	
	/**
	 * List of checks.
	 */
	private ArrayList<SagaPatternElement> componentList = new ArrayList<SagaPatternElement>();
	
	
	/**
	 * Creates an element.
	 * 
	 * @param logicAction action returned by the element
	 * @param logicType logic type
	 * @param minimumLevel minimum level
	 * @param maximumLevel maximum level
	 */
	public SagaPatternLogicElement(LogicAction logicAction, LogicType logicType, Short minimumLevel, Short maximumLevel) {
		super(0, 0, 0, minimumLevel, maximumLevel, new Material[0]);
		this.logicAction = logicAction;
		this.logicType = logicType;
	}
	
	/**
	 * Creates an element. Ignores levels.
	 * 
	 * @param logicAction action returned by the element
	 * @param logicType logic type
	 */
	public SagaPatternLogicElement(LogicAction logicAction, LogicType logicType) {
		this(logicAction, logicType, (short)-1, (short)-1);
	}

	/**
	 * Adds a single check element.
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param zOffset
	 * @param minimumLevel
	 * @param maximumLevel
	 * @param materials
	 * @param logicAction
	 */
	public SagaPatternLogicElement(int xOffset , int yOffset , int zOffset, short minimumLevel, short maximumLevel, Material[] materials, LogicAction logicAction) {

		this(logicAction, LogicType.AND);
		addElement(new SagaPatternCheckElement(xOffset, yOffset, zOffset, minimumLevel, maximumLevel, materials));
	
	}
	
	/**
	 * Adds a single element
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param zOffset
	 * @param materials
	 * @param logicAction
	 */
	public SagaPatternLogicElement(int xOffset, int yOffset, int zOffset, Material[] materials, LogicAction logicAction) {
		
		this(xOffset, yOffset, zOffset, (short)-1, (short)-1, materials, logicAction);
	
	}
	
	/**
	 * Adds an element to the list.
	 * 
	 * @param element
	 */
	public void addElement(SagaPatternElement element) {
		componentList.add(element);
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#execute(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public boolean execute(Block previousBlock, SagaPatternInitiator initiator) {

		
		boolean check = check(previousBlock, initiator);
		if(logicAction.equals(LogicAction.TERMINATE) && check){
			return true;
		}
		if(logicAction.equals(LogicAction.NTERMINATE) && !check){
			return true;
		}
		return false;
		
		
	}

	/**
	 * Checks if the block matches any one from the given material list.
	 * 
	 * @param anchorBlock previous block
	 * @param initiator initiator
	 * @return true if the check was positive
	 */
	@Override
	public boolean check(Block anchorBlock, SagaPatternInitiator initiator) {
//		System.out.println("logic.check method");
		
		Block currentAnchorBlock = anchorBlock;
		
		
		// And/nand element:
		if( logicType.equals(LogicType.AND) || logicType.equals(LogicType.NAND) ){
			for (int i = 0; i < componentList.size(); i++) {
				if( ( componentList.get(i) instanceof SagaPatternCheckElement || componentList.get(i) instanceof SagaPatternLogicElement) && !((SagaPatternCheckElement)componentList.get(i)).check(currentAnchorBlock, initiator) ){
					if(logicType.equals(LogicType.AND)){
//						System.out.println("AND: returning false for "+logicAction);
						return false;
					}else{
//						System.out.println("AND: returning true for "+logicAction);
						return true;
					}
					
				}
				currentAnchorBlock = componentList.get(i).shiftAnchorBlock(currentAnchorBlock, initiator);
			}
			if(logicType.equals(LogicType.AND)){
//				System.out.println("AND: returning true for "+logicAction);
				return true;
			}else{
//				System.out.println("AND: returning false for "+logicAction);
				return false;
			}
		}
		// Or/nor element:
		else{
			for (int i = 0; i < componentList.size(); i++) {
				if(componentList.get(i) instanceof SagaPatternCheckElement && ((SagaPatternCheckElement)componentList.get(i)).check(currentAnchorBlock, initiator) ){
					if(logicType.equals(LogicType.OR)){
//						System.out.println("OR: returning true for "+logicAction);
						return true;
					}else{
//						System.out.println("OR: returning false for "+logicAction);
						return false;
					}
				}
				currentAnchorBlock = componentList.get(i).shiftAnchorBlock(currentAnchorBlock, initiator);
			}
			if(logicType.equals(LogicType.OR)){
//				System.out.println("OR: returning false for "+logicAction);
				return false;
			}else{
//				System.out.println("OR: returning true for "+logicAction);
				return true;
			}
		}

		
	}
	
	/**
	 * Checks if the block matches any one from the given material list.
	 * 
	 * @param previousBlock previous block
	 * @param initiator initiator
	 * @return action that should be taken
	 */
	public LogicAction findAction(Block previousBlock, SagaPatternInitiator initiator) {
//		System.out.println("logic.findAction method");
		if(check(previousBlock, initiator)){
			return logicAction;
		}else{
			return LogicAction.invert(logicAction);
		}
		
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.pattern.SagaPatternElement#shiftAnchorBlock(org.bukkit.block.Block, org.saga.pattern.SagaPatternInitiator)
	 */
	@Override
	public Block shiftAnchorBlock(Block oldAnchorBlock, SagaPatternInitiator initiator) {
		
		return oldAnchorBlock;
		
	}
	
	
	/**
	 * gets logic type.
	 * 
	 * @return logic type
	 */
	public LogicType getLogicType() {
		return logicType;
	}
	

	/**
	 * Logic action.
	 *
	 */
	public static enum LogicType{
		
		AND,
		NAND,
		OR,
		NOR;
		
	}
	
	/**
	 * Action taken by the logic element.
	 * Ignore will skip the next element, break will stop the loop and terminate is used when checking if the pattern should not be run at all.
	 * 
	 *
	 */
	public static enum LogicAction{
		
		NONE,
		IGNORE,
		NIGNORE,
		BREAK,
		NBREAK,
		TERMINATE,
		NTERMINATE;
		
		public static LogicAction invert(LogicAction action){
			
			if(action.equals(IGNORE)) return NIGNORE;
			if(action.equals(NIGNORE)) return IGNORE;
			if(action.equals(BREAK)) return NBREAK;
			if(action.equals(NBREAK)) return BREAK;
			if(action.equals(TERMINATE)) return NTERMINATE;
			if(action.equals(NTERMINATE)) return TERMINATE;
			System.out.println("INVALID ACTION: "+action);
			return TERMINATE;
			
		}
		
	}
	

}
