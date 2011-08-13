package org.saga.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockDamageEvent;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnBlockDamage;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternCheckElement;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternSelectionMoveElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.pattern.SagaPatternLogicElement.LogicType;
import org.saga.professions.Profession;

public class ChopDownAbility extends AbilityFunction implements OnBlockDamage{

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "chop down";

	/**
	 * Pattern.
	 */
	transient private static SagaPatternElement PATTERN = createPattern();

	
	/**
	 * Used by gson.
	 * 
	 */
	public ChopDownAbility() {
		
        super(ABILITY_NAME);
	
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.AbilityFunction#completeSecondExtended()
	 */
	@Override
	protected boolean completeSecondExtended() {
		return true;
	}
	

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.UseOnBlockDamage#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession, org.bukkit.event.block.BlockDamageEvent)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession, BlockDamageEvent event) {
		
		Location target = event.getBlock().getLocation();
		
		// Activate and check for termination:
		boolean termination = false;
		termination = sagaPlayer.initiatePatternTarget(target, PATTERN, (short) (level * calculateFunctionValue(level)), false, 50);
		sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
		if(termination){
			sagaPlayer.sendMessage(PlayerMessages.abilityInvalidUse(this));
		}
		
		
	}
	
	/**
	 * Creates the pattern for the ability.
	 * 
	 * @return ability pattern
	 */
	private static SagaPatternElement createPattern(){
		
		
		// At least eight leaves leaves:
		SagaPatternLogicElement leavesCheck = new SagaPatternLogicElement(LogicAction.NTERMINATE, LogicType.AND);
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(1, 0, 0, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(1, 0, 1, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(0, 0, 1, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(-1, 0, 1, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(-1, 0, 0, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(-1, 0, -1, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(0, 0, -1, new Material[]{Material.LEAVES})));
		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(1, 0, -1, new Material[]{Material.LEAVES})));
		
		// Log one y higher that the broken one:
		SagaPatternLogicElement trunkInsideCheck = new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NTERMINATE);
		
		// Logs surrounding at the bottom:
		SagaPatternLogicElement eightLogsCheckElement = new SagaPatternLogicElement(LogicAction.NONE, LogicType.OR);
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(1, 0, 0, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(1, 0, 1, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(0, 0, 1, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(-1, 0, 1, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(-1, 0, 0, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(-1, 0, -1, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(0, 0, -1, new Material[]{Material.LOG}));
		eightLogsCheckElement.addElement(new SagaPatternCheckElement(1, 0, -1, new Material[]{Material.LOG}));
		SagaPatternLogicElement trunkOutsideCheck = new SagaPatternLogicElement(LogicAction.TERMINATE, LogicType.OR);
		trunkOutsideCheck.addElement(eightLogsCheckElement); // 0
		trunkOutsideCheck.addElement(new SagaPatternSelectionMoveElement(0, 1, 0));
		trunkOutsideCheck.addElement(eightLogsCheckElement); // 1
		
		SagaPatternListElement initiationList = new SagaPatternListElement();
		initiationList.addElement(trunkInsideCheck);
		initiationList.addElement(trunkOutsideCheck);
		initiationList.addElement(leavesCheck);
		
		// Main list:
		SagaPatternListElement mainList = new SagaPatternListElement();
		initiationList.addElement(mainList);	
		
		// Move to top:
		SagaPatternListElement toTopList = new SagaPatternListElement();
		toTopList.addElement(new SagaPatternLogicElement(0, 1, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
		toTopList.addElement(new SagaPatternSelectionMoveElement(0, 1, 0));
		toTopList.addElement(toTopList);
		
		// Additional move to compensate for top list ending at a log, instead of air
		mainList.addElement(new SagaPatternSelectionMoveElement(0, -1, 0));
		
		// Move down and chop trunk logs:
		SagaPatternListElement toBottomList = new SagaPatternListElement();
		toBottomList.addElement(new SagaPatternLogicElement(0, -1, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
		toBottomList.addElement(new SagaPatternSelectionMoveElement(0, -1, 0));
		toBottomList.addElement(new SagaPatternBreakElement());
		
		// Check north:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(-1, 1, 0)));
		
		// Check north-east:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(-1, 1, -1)));
		
		// Check east:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(0, 1, -1)));
		
		// Check south-east:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(1, 1, -1)));
		
		// Check south:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(1, 1, 0)));
		
		// Check south-west:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(1, 1, 1)));
				
		// Check west:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(0, 1, 1)));
		
		// Check north-west:
		toBottomList.addElement(createBranchRemove(new SagaPatternSelectionMoveElement(-1, 1, 1)));
				
		// Put everything together:
		toBottomList.addElement(toBottomList);
		mainList.addElement(toTopList);
		mainList.addElement(new SagaPatternSelectionMoveElement(0, 1, 0)); // Compensates for up ending at a the tip instead of air.
		mainList.addElement(toBottomList);
		
		return initiationList;
		
		
	}

	/**
	 * Support method for create pattern.
	 * 
	 * @param move
	 * @return
	 */
	private static SagaPatternElement createBranchRemove(SagaPatternSelectionMoveElement move){
		
		
		SagaPatternListElement branchRemove = new SagaPatternListElement().disableAnchorShift();
		branchRemove.addElement(move);
		
		// Break if no log is found:
		branchRemove.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NBREAK));
		branchRemove.addElement(new SagaPatternBreakElement());
		
		// Recursion:
		SagaPatternListElement recursionElement = new SagaPatternListElement().disableAnchorShift(); 
		branchRemove.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternBreakElement());
		
		// Terminate if the log still remains for some reason:
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.TERMINATE));
		
		// North up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 1, 0));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, -1, 0));
		
		// North-east up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 1, -1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, -1, 1));
		
		// East up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 1, -1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, -1, 1));
		
		// South-east up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 1, -1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, -1, 1));
		
		// South up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 1, 0));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, -1, 0));
		
		// South-west up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 1, 1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, -1, -1));
		
		// West up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 1, 1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, -1, -1));
		
		// North-west up:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 1, 1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, -1, -1));
		
		// North:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 0, 0));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 0, 0));
		
		// North-east:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 0, -1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 0, 1));
		
		// East:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 0, -1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 0, 1));
		
		// South-east:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 0, -1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 0, 1));
		
		// South:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 0, 0));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 0, 0));
		
		// South-west:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 0, 1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 0, -1));
		
		// West:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 0, 1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 0, -1));
		
		// North-west:
		recursionElement.addElement(new SagaPatternSelectionMoveElement(-1, 0, 1));
		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
		recursionElement.addElement(recursionElement);
		recursionElement.addElement(new SagaPatternSelectionMoveElement(1, 0, -1));
		
		// Up:
//		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, 1, 0));
//		recursionElement.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NIGNORE));
//		recursionElement.addElement(recursionElement);
//		recursionElement.addElement(new SagaPatternSelectionMoveElement(0, -1, 0));
//		
		
		
		return branchRemove;
		
		
	}
	
	/**
	 * Support method for create pattern.
	 * 
	 * @param scrollElement scroll element
	 * @return leaves stripe
	 */
	private static SagaPatternLogicElement createLeavesCheckLine(SagaPatternCheckElement scrollElement) {
		
		// Leaves:
		SagaPatternSelectionMoveElement upMove = new SagaPatternSelectionMoveElement(0, 1, 0);
		
		// S leaves:
		SagaPatternLogicElement leavesCheckElement = new SagaPatternLogicElement(LogicAction.NONE, LogicType.OR);
		leavesCheckElement.addElement(scrollElement); // 1
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 2
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 3
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 4
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 5
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 6
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 7
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 8
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 9
		leavesCheckElement.addElement(upMove);
		leavesCheckElement.addElement(scrollElement); // 10
		return leavesCheckElement;
		

	}
	
	
}
