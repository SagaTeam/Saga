package org.saga.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDamageEvent;
import org.saga.SagaPlayer;
import org.saga.constants.BlockConstants;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternCheckElement;
import org.saga.pattern.SagaPatternCountElement;
import org.saga.pattern.SagaPatternCountElement.CountType;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternSelectionMoveElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.pattern.SagaPatternLogicElement.LogicType;

public class TreeClimbAbility extends AbilityFunction {

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "tree climb";

	/**
	 * Activate check.
	 */
	transient private SagaPatternLogicElement ACTIVATE_CHECK_PATTERN_UP = createActivateCheckPattern(true);
	
	/**
	 * Activate check.
	 */
	transient private SagaPatternLogicElement ACTIVATE_CHECK_PATTERN_DOWN = createActivateCheckPattern(false);
	
	
	public TreeClimbAbility() {
		super(ABILITY_NAME, AbilityActivateType.SINGLE_USE);
	}
	
	@Override
	protected boolean completeSecondExtended() {
		return true;
	}

	public void use(Short level, SagaPlayer sagaPlayer, BlockDamageEvent event) {

		
		int blockLimit=60;
		Block targetedBlock = event.getBlock();
		Location location = null;
		Short patternLevel = new Double(level * calculateFunctionValue(level)).shortValue();
		boolean playerLookingUp = sagaPlayer.calculatePlayerVerticalDirection();
		
		
		// Check:
		if(location == null){
			location = getTeleportLocation(targetedBlock, sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.NORTH), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.NORTH_EAST), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.EAST), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.SOUTH_EAST), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.SOUTH), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.SOUTH_WEST), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.WEST), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		if(location == null){
			location = getTeleportLocation(targetedBlock.getFace(BlockFace.NORTH_WEST), sagaPlayer, blockLimit, patternLevel, playerLookingUp);
		}
		
		// Teleport if the location is correct:
		if(location != null){
			sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
			sagaPlayer.moveToCentered(location);
		}else{
			sagaPlayer.sendMessage(PlayerMessages.invalidAbilityUse(this));
		}
		
		
		
		
		
	}

	/**
	 * Returns the teleport location block location.
	 * 
	 * @param targetedBlock targeted block
	 * @param sagaPlayer saga player
	 * @param blockLimit block limit
	 * @param level pattern level
	 * @return location or null if not found
	 */
	private Location getTeleportLocation(Block targetedBlock, SagaPlayer sagaPlayer, int blockLimit, short level, boolean lookingUp) {

		
		SagaPatternLogicElement check;
		if(lookingUp){
			System.out.println("looking up");
			check = ACTIVATE_CHECK_PATTERN_UP;
		}else{
			System.out.println("looking down");
			check = ACTIVATE_CHECK_PATTERN_DOWN;
		}
		if(!sagaPlayer.initiatePatternTarget(targetedBlock.getLocation(), check, level, false, blockLimit)){
			System.out.println("PATTERN CHECK OK");
			if(lookingUp){
				for (int i = 1; i <= blockLimit; i++) {
					if(BlockConstants.isTransparent(targetedBlock.getRelative(0, i, 0).getType()) && BlockConstants.isTransparent(targetedBlock.getRelative(0, i+1, 0).getType())){
						System.out.println("found up location");
						return targetedBlock.getRelative(0, i, 0).getLocation();
					}
				}
			}else{
				System.out.println("looking for down");
				for (int i = 1; i <= blockLimit; i++) {
					if(BlockConstants.isTransparent(targetedBlock.getRelative(0, -i, 0).getType()) && BlockConstants.isTransparent(targetedBlock.getRelative(0, -i-1, 0).getType())){
						System.out.println("found down location");
						return targetedBlock.getRelative(0, -i-1, 0).getLocation();
					}
				}
			}
			
		}
		System.out.println("NO LOCATION");
		return null;
		
		
	}
	
	
	/**
	 * Creates the activate check pattern.
	 * 
	 * @return Activate check pattern.
	 */
	private static SagaPatternLogicElement createActivateCheckPattern(boolean moveUp) {
		
		
		SagaPatternLogicElement mainElement = new SagaPatternLogicElement(LogicAction.NTERMINATE, LogicType.AND);
		
		// Check for leaves:
		mainElement.addElement(createCheckLine(Material.LEAVES,moveUp));
		
		// Check for logs:
		mainElement.addElement(createCheckLine(Material.LOG, moveUp));
		
		// Add climb check:
		SagaPatternLogicElement climbCheck= new SagaPatternLogicElement(LogicAction.NTERMINATE, LogicType.OR);
		int minimumClimb=5;
		int maximumClimb= 50;
		for (int i = 0; i < maximumClimb; i++) {
			int climbLevel= i - minimumClimb;
			if(climbLevel < 0){
				climbLevel = 0;
			}
			SagaPatternLogicElement climbCheckElement = new SagaPatternLogicElement(LogicAction.NONE, LogicType.AND);
			if(moveUp){
				climbCheckElement.addElement(new SagaPatternCheckElement(0, i, 0, (short)(climbLevel), (short)-1, new Material[]{Material.LOG, Material.LEAVES}));
				climbCheckElement.addElement(new SagaPatternCheckElement(0, i+1, 0, (short)(climbLevel), (short)-1, BlockConstants.TRANSPARENT_MATERIALS));
				climbCheckElement.addElement(new SagaPatternCheckElement(0, i+2, 0, (short)(climbLevel), (short)-1, BlockConstants.TRANSPARENT_MATERIALS));
			}else{
				climbCheckElement.addElement(new SagaPatternCheckElement(0, -i, 0, (short)(climbLevel), (short)-1, new Material[]{Material.LOG, Material.LEAVES}));
				climbCheckElement.addElement(new SagaPatternCheckElement(0, -i-1, 0, (short)(climbLevel), (short)-1, BlockConstants.TRANSPARENT_MATERIALS));
				climbCheckElement.addElement(new SagaPatternCheckElement(0, -i-2, 0, (short)(climbLevel), (short)-1, BlockConstants.TRANSPARENT_MATERIALS));
			}
			climbCheck.addElement(climbCheckElement);
		}
		mainElement.addElement(climbCheck);
		
		return mainElement;
		
		
	}
	
	/**
	 * Support method for create pattern
	 * 
	 * @param checkMaterial material to check
	 * @param moveUp true if the check goes up
	 * @return leaves stripe
	 */
	private static SagaPatternLogicElement createCheckLine(Material checkMaterial, boolean moveUp) {
		
		
		int elementCounts=1;
		if(checkMaterial.equals(Material.LEAVES)){
			elementCounts=2;
		}
		
		SagaPatternCheckElement[] counts = new SagaPatternCheckElement[9];
		counts[0] = new SagaPatternCheckElement(-1, 0, 1, new Material[]{checkMaterial});
		counts[1] = new SagaPatternCheckElement(-1, 0, 0, new Material[]{checkMaterial});
		counts[2] = new SagaPatternCheckElement(-1, 0, -1, new Material[]{checkMaterial});
		counts[3] = new SagaPatternCheckElement(0, 0, -1, new Material[]{checkMaterial});
		counts[4] = new SagaPatternCheckElement(1, 0, -1, new Material[]{checkMaterial});
		counts[5] = new SagaPatternCheckElement(1, 0, 0, new Material[]{checkMaterial});
		counts[6] = new SagaPatternCheckElement(1, 0, 1, new Material[]{checkMaterial});
		counts[7] = new SagaPatternCheckElement(0, 0, 1, new Material[]{checkMaterial});
		counts[8] = new SagaPatternCheckElement(0, 0, 0, new Material[]{checkMaterial});
		SagaPatternCountElement countElement = new SagaPatternCountElement(counts, CountType.BIGGER_EQUAL, elementCounts);
		
		SagaPatternSelectionMoveElement move;
		if(moveUp){
			move = new SagaPatternSelectionMoveElement(0, 1, 0);
		}else{
			move = new SagaPatternSelectionMoveElement(0, -1, 0);
		}
		
		// Leaves:
		SagaPatternLogicElement mainList = new SagaPatternLogicElement(LogicAction.NTERMINATE, LogicType.OR);
		mainList.addElement(countElement); // 1
		mainList.addElement(move);
		mainList.addElement(countElement); // 2
		mainList.addElement(move);
		mainList.addElement(countElement); // 3
		mainList.addElement(move);
		mainList.addElement(countElement); // 4
		mainList.addElement(move);
		mainList.addElement(countElement); // 5
		mainList.addElement(move);
		mainList.addElement(countElement); // 6
		mainList.addElement(move);
		mainList.addElement(countElement); // 7
		mainList.addElement(move);
		mainList.addElement(countElement); // 8
		mainList.addElement(move);
		mainList.addElement(countElement); // 9
		mainList.addElement(move);
		mainList.addElement(countElement); // 10
		mainList.addElement(move);
		mainList.addElement(countElement); // 11
		mainList.addElement(move);
		mainList.addElement(countElement); // 12
		mainList.addElement(move);
		mainList.addElement(countElement); // 13
		mainList.addElement(move);
		mainList.addElement(countElement); // 14
		
		return mainList;
		

	}
	
}
