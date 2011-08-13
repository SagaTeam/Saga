package org.saga.abilities;


import java.util.Random;

import org.bukkit.Material;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnActivateAbility;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.professions.Profession;


public class PowerfulSwings extends AbilityFunction implements OnActivateAbility{

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "powerful swings";

	/**
	 * Pattern.
	 */
	transient private static SagaPatternElement PATTERN = createPattern();

	/**
	 * Random generator.
	 */
	transient final Random random = new Random();
	
	
	/**
	 * Used by gson.
	 * 
	 */
	public PowerfulSwings() {
		
		super(ABILITY_NAME, AbilityActivateType.INSTANTANEOUS);
		
	}

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#completeInheriting()
	 */
	@Override
	public boolean completeSecondExtended() {
		
		
		return true;
		
		
	}
	
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.types.OnActivateAbility#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession) {
		
		
		sagaPlayer.initiatePattern(PATTERN, (short) (level * calculateFunctionValue(level)), random.nextBoolean(), 100);
		
		
		sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
		
		
	}
	

	/**
	 * Creates a swing pattern.
	 * 
	 * @return swing pattern
	 */
	private static SagaPatternElement createPattern(){
		
		
		SagaPatternListElement pattern = new SagaPatternListElement();
		Material[] penetrateMaterials= new Material[]{Material.STONE};
		
		// 1:
		pattern.addElement(new SagaPatternLogicElement(-1, 3, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 3, 0, (short)20, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-1, 3, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 3, 1, (short)20, (short)-1));
		
		// 2:
		pattern.addElement(new SagaPatternLogicElement(-2, 2, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-2, 2, 0, (short)18, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-2, 2, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-2, 2, 1, (short)18, (short)-1));
		
		// 3:
		pattern.addElement(new SagaPatternLogicElement(-1, 2, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 2, 0, (short)10, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-1, 2, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 2, 1, (short)10, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-1, 2, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 2, 2, (short)25, (short)-1));
		
		// 4:
		pattern.addElement(new SagaPatternLogicElement(0, 3, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 3, 0, (short)22, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 3, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 3, 1, (short)22, (short)-1));
		
		// 5:
		pattern.addElement(new SagaPatternLogicElement(-2, 1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-2, 1, 0, (short)12, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-2, 1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-2, 1, 1, (short)12, (short)-1));
		
		// 6:
		pattern.addElement(new SagaPatternLogicElement(0, 2, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 2, 0, (short)8, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 2, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 2, 1, (short)8, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 2, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 2, 2, (short)33, (short)-1));
		
		// 7:
		pattern.addElement(new SagaPatternLogicElement(-1, 1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 1, 0, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-1, 1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 1, 1, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-1, 1, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 1, 2, (short)19, (short)-1));
		
		// 8:
		pattern.addElement(new SagaPatternLogicElement(0, 1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 1, 0, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 1, 1, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 1, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 1, 2, (short)27, (short)-1));
		
		// 9:
		pattern.addElement(new SagaPatternLogicElement(1, 2, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 2, 0, (short)24, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, 2, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 2, 1, (short)24, (short)-1));
		
		// 10:
		pattern.addElement(new SagaPatternLogicElement(-1, 0, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 0, 0, (short)14, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(-1, 0, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(-1, 0, 1, (short)14, (short)-1));
		
		// 11:
		pattern.addElement(new SagaPatternLogicElement(1, 1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 1, 0, (short)6, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, 1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 1, 1, (short)6, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, 1, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 1, 2, (short)35, (short)-1));
		
		// 12:
		pattern.addElement(new SagaPatternLogicElement(0, 0, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 0, 0, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 0, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 0, 1, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, 0, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, 0, 2, (short)21, (short)-1));
		
		// 13:
		pattern.addElement(new SagaPatternLogicElement(1, 0, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 0, 0, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, 0, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 0, 1, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, 0, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, 0, 2, (short)29, (short)-1));
		
		// 14:
		pattern.addElement(new SagaPatternLogicElement(2, 1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, 1, 0, (short)26, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(2, 1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, 1, 1, (short)26, (short)-1));
		
		// 15:
		pattern.addElement(new SagaPatternLogicElement(0, -1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, -1, 0, (short)16, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(0, -1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(0, -1, 1, (short)16, (short)-1));
		
		// 16:
		pattern.addElement(new SagaPatternLogicElement(2, 0, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, 0, 0, (short)4, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(2, 0, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, 0, 1, (short)4, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(2, 0, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, 0, 2, (short)37, (short)-1));
		
		// 17:
		pattern.addElement(new SagaPatternLogicElement(1, -1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, -1, 0, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, -1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, -1, 1, (short)0, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(1, -1, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(1, -1, 2, (short)23, (short)-1));
		
		// 18:
		pattern.addElement(new SagaPatternLogicElement(2, -1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, -1, 0, (short)2, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(2, -1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, -1, 1, (short)2, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(2, -1, 2, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(2, -1, 2, (short)31, (short)-1));
		
		// 19:
		pattern.addElement(new SagaPatternLogicElement(3, 0, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(3, 0, 0, (short)28, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(3, 0, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(3, 0, 1, (short)28, (short)-1));
		
		// 20:
		pattern.addElement(new SagaPatternLogicElement(3, -1, 0, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(3, -1, 0, (short)30, (short)-1));
		pattern.addElement(new SagaPatternLogicElement(3, -1, 1, penetrateMaterials, LogicAction.NIGNORE));
		pattern.addElement(new SagaPatternBreakElement(3, -1, 1, (short)30, (short)-1));
		
		return pattern;
		
	}
		
	
	
	
}
