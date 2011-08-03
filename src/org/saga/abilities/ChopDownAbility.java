package org.saga.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternCheckElement;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternSelectionMoveElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.pattern.SagaPatternLogicElement.LogicType;

public class ChopDownAbility extends AbilityFunction {

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "chop down";

	/**
	 * Pattern.
	 */
	transient private static SagaPatternElement PATTERN = createPattern2();

	
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
	

	/**
	 * Uses the ability.
	 * 
	 * @param level level
	 * @param sagaPlayer saga player
	 * @param orthogonalFlip if true, there will be a orthogonal flip
	 */
	public void use(Short level, SagaPlayer sagaPlayer, BlockBreakEvent event) {
		
		Location target = event.getBlock().getLocation();
		
		// Activate and check for termination:
		boolean termination = false;
		termination = sagaPlayer.initiatePatternTarget(target, PATTERN, (short) (level * calculateFunctionValue(level)), false, 50);
		sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
		if(termination){
			sagaPlayer.sendMessage(PlayerMessages.invalidAbilityUse(this));
		}
		
		
	}
	
	private static SagaPatternElement createPattern2(){
		
		
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
	 * Creates the pattern for the ability.
	 * 
	 * @return ability pattern
	 */
	private static SagaPatternElement createPattern(){
		
		
		// Initiation:
		SagaPatternListElement initiationList = new SagaPatternListElement();
//		initiationList.addElement(new SagaPatternLogicElement(0, 0, 0, new Material[]{Material.LOG}, LogicAction.NTERMINATE)); // Don't terminate if a log is found.
		
//		
//		
//		// SE leaves:
//		SagaPatternLogicElement leavesCheck = new SagaPatternLogicElement(LogicAction.NTERMINATE, LogicType.AND);
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(1, 0, 0, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(1, 0, 1, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(0, 0, 1, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(-1, 0, 1, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(-1, 0, 0, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(-1, 0, -1, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(0, 0, -1, new Material[]{Material.LEAVES})));
//		leavesCheck.addElement(createLeavesCheckLine(new SagaPatternCheckElement(1, 0, -1, new Material[]{Material.LEAVES})));
//		
//		// No logs surrounding at the bottom:
//		SagaPatternLogicElement eightLogsCheckElement = new SagaPatternLogicElement(LogicAction.NONE, LogicType.OR);
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(1, 0, 0, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(1, 0, 1, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(0, 0, 1, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(-1, 0, 1, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(-1, 0, 0, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(-1, 0, -1, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(0, 0, -1, new Material[]{Material.LOG}));
//		eightLogsCheckElement.addElement(new SagaPatternCheckElement(1, 0, -1, new Material[]{Material.LOG}));
//		
//		SagaPatternLogicElement trunkOutsideCheck = new SagaPatternLogicElement(LogicAction.TERMINATE, LogicType.OR);
//		trunkOutsideCheck.addElement(eightLogsCheckElement); // 0
//		trunkOutsideCheck.addElement(new SagaPatternSelectionMoveElement(0, 1, 0));
//		trunkOutsideCheck.addElement(eightLogsCheckElement); // 1
//		
		// Main list:
		SagaPatternListElement mainList = new SagaPatternListElement();
		mainList.addElement(new SagaPatternBreakElement(0, 0, 0));
		
		// Up:
		SagaPatternListElement upList = new SagaPatternListElement();
		upList.addElement(new SagaPatternLogicElement(0, 1, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
		upList.addElement(new SagaPatternSelectionMoveElement(0, 1, 0));
		upList.addElement(mainList);
		upList.addElement(new SagaPatternSelectionMoveElement(0, -1, 0));
		
//		// Up north:
//		SagaPatternListElement upNorthList = new SagaPatternListElement();
//		upNorthList.addElement(new SagaPatternLogicElement(-1, 1, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upNorthList.addElement(new SagaPatternSelectionMoveElement(-1, 1, 0));
//		upNorthList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upNorthList.addElement(mainList);
//		upNorthList.addElement(new SagaPatternSelectionMoveElement(1, -1, 0));
//		
//		// Up north-east:
//		SagaPatternListElement upNorthEastList = new SagaPatternListElement();
//		upNorthEastList.addElement(new SagaPatternLogicElement(-1, 1, -1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upNorthEastList.addElement(new SagaPatternSelectionMoveElement(-1, 1, -1));
//		upNorthEastList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upNorthEastList.addElement(mainList);
//		upNorthEastList.addElement(new SagaPatternSelectionMoveElement(1, -1, 1));
//		
//		// Up east:
//		SagaPatternListElement upEastList = new SagaPatternListElement();
//		upEastList.addElement(new SagaPatternLogicElement(0, 1, -1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upEastList.addElement(new SagaPatternSelectionMoveElement(0, 1, -1));
//		upEastList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upEastList.addElement(mainList);
//		upEastList.addElement(new SagaPatternSelectionMoveElement(0, -1, 1));
//		
//		// Up south-east:
//		SagaPatternListElement upSouthEastList = new SagaPatternListElement();
//		upSouthEastList.addElement(new SagaPatternLogicElement(1, 1, -1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upSouthEastList.addElement(new SagaPatternSelectionMoveElement(1, 1, -1));
//		upSouthEastList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upSouthEastList.addElement(mainList);
//		upSouthEastList.addElement(new SagaPatternSelectionMoveElement(-1, -1, 1));
//		
//		// Up south:
//		SagaPatternListElement upSouthList = new SagaPatternListElement();
//		upSouthList.addElement(new SagaPatternLogicElement(1, 1, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upSouthList.addElement(new SagaPatternSelectionMoveElement(1, 1, 0));
//		upSouthList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upSouthList.addElement(mainList);
//		upSouthList.addElement(new SagaPatternSelectionMoveElement(-1, -1, 0));
//		
//		// Up south-west:
//		SagaPatternListElement upSouthWestList = new SagaPatternListElement();
//		upSouthWestList.addElement(new SagaPatternLogicElement(1, 1, 1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upSouthWestList.addElement(new SagaPatternSelectionMoveElement(1, 1, 1));
//		upSouthWestList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upSouthWestList.addElement(mainList);
//		upSouthWestList.addElement(new SagaPatternSelectionMoveElement(-1, -1, -1));
//		
//		// Up west:
//		SagaPatternListElement upWestList = new SagaPatternListElement();
//		upWestList.addElement(new SagaPatternLogicElement(0, 1, 1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upWestList.addElement(new SagaPatternSelectionMoveElement(0, 1, 1));
//		upWestList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upWestList.addElement(mainList);
//		upWestList.addElement(new SagaPatternSelectionMoveElement(0, -1, -1));
//		
//		// Up north-west:
//		SagaPatternListElement upNorthWestList = new SagaPatternListElement();
//		upNorthWestList.addElement(new SagaPatternLogicElement(-1, 1, 1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		upNorthWestList.addElement(new SagaPatternSelectionMoveElement(-1, 1, 1));
//		upNorthWestList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		upNorthWestList.addElement(mainList);
//		upNorthWestList.addElement(new SagaPatternSelectionMoveElement(1, -1, -1));
//		
//		// North:
//		SagaPatternListElement northList = new SagaPatternListElement();
//		northList.addElement(new SagaPatternLogicElement(-1, 0, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		northList.addElement(new SagaPatternSelectionMoveElement(-1, 0, 0));
//		northList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		northList.addElement(mainList);
//		northList.addElement(new SagaPatternSelectionMoveElement(1, 0, 0));
//		
//		// North-east:
//		SagaPatternListElement northEastList = new SagaPatternListElement();
//		northEastList.addElement(new SagaPatternLogicElement(-1, 0, -1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		northEastList.addElement(new SagaPatternSelectionMoveElement(-1, 0, -1));
//		northEastList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		northEastList.addElement(mainList);
//		northEastList.addElement(new SagaPatternSelectionMoveElement(1, 0, 1));
//		
//		// East:
//		SagaPatternListElement eastList = new SagaPatternListElement();
//		eastList.addElement(new SagaPatternLogicElement(0, 0, -1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		eastList.addElement(new SagaPatternSelectionMoveElement(0, 0, -1));
//		eastList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		eastList.addElement(mainList);
//		eastList.addElement(new SagaPatternSelectionMoveElement(0, 0, 1));
//		
//		// South-east:
//		SagaPatternListElement southEastList = new SagaPatternListElement();
//		southEastList.addElement(new SagaPatternLogicElement(1, 0, -1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		southEastList.addElement(new SagaPatternSelectionMoveElement(1, 0, -1));
//		southEastList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		southEastList.addElement(mainList);
//		southEastList.addElement(new SagaPatternSelectionMoveElement(-1, 0, 1));
//		
//		// South:
//		SagaPatternListElement southList = new SagaPatternListElement();
//		southList.addElement(new SagaPatternLogicElement(1, 0, 0, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		southList.addElement(new SagaPatternSelectionMoveElement(1, 0, 0));
//		southList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		southList.addElement(mainList);
//		southList.addElement(new SagaPatternSelectionMoveElement(-1, 0, 0));
//		
//		// South-west:
//		SagaPatternListElement southWestList = new SagaPatternListElement();
//		southWestList.addElement(new SagaPatternLogicElement(1, 0, 1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		southWestList.addElement(new SagaPatternSelectionMoveElement(1, 0, 1));
//		southWestList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		southWestList.addElement(mainList);
//		southWestList.addElement(new SagaPatternSelectionMoveElement(-1, 0, -1));
//		
//		// West:
//		SagaPatternListElement westList = new SagaPatternListElement();
//		westList.addElement(new SagaPatternLogicElement(0, 0, 1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		westList.addElement(new SagaPatternSelectionMoveElement(0, 0, 1));
//		westList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		westList.addElement(mainList);
//		westList.addElement(new SagaPatternSelectionMoveElement(0, 0, -1));
//		
//		// North-west:
//		SagaPatternListElement northWestList = new SagaPatternListElement();
//		northWestList.addElement(new SagaPatternLogicElement(-1, 0, 1, new Material[]{Material.LOG}, LogicAction.NBREAK)); // Don't break if a log is found.
//		northWestList.addElement(new SagaPatternSelectionMoveElement(-1, 0, 1));
//		northWestList.addElement(new SagaPatternBreakElement(0, 0, 0));
//		northWestList.addElement(mainList);
//		northWestList.addElement(new SagaPatternSelectionMoveElement(1, 0, -1));
//		
		// Put everything together:
//		initiationList.addElement(trunkOutsideCheck);
//		initiationList.addElement(leavesCheck);
		initiationList.addElement(mainList);
		
//		mainList.addElement(northList);
//		mainList.addElement(northEastList);
//		mainList.addElement(eastList);
//		mainList.addElement(southEastList);
//		mainList.addElement(southList);
//		mainList.addElement(southWestList);
//		mainList.addElement(westList);
//		mainList.addElement(northWestList);
//		
//		mainList.addElement(upNorthList);
//		mainList.addElement(upNorthEastList);
//		mainList.addElement(upEastList);
//		mainList.addElement(upSouthEastList);
//		mainList.addElement(upSouthList);
//		mainList.addElement(upSouthWestList);
//		mainList.addElement(upWestList);
//		mainList.addElement(upNorthWestList);
		mainList.addElement(upList);
				
		return initiationList;
		
		
	}
	
	/**
	 * Support method for create pattern
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
