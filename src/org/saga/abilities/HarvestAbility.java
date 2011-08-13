package org.saga.abilities;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.saga.SagaPlayer;
import org.saga.abilities.types.OnActivateAbility;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;
import org.saga.professions.Profession;

public class HarvestAbility extends AbilityFunction implements OnActivateAbility{

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "harvest";

	/**
	 * Pattern.
	 */
	transient private static SagaPatternElement PATTERN = createPattern();

	
	/**
	 * Used by gson.
	 * 
	 */
	public HarvestAbility() {
		
        super(ABILITY_NAME, AbilityActivateType.INSTANTANEOUS);
	
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
	 * @see org.saga.abilities.types.OnActivateAbility#use(java.lang.Short, org.saga.SagaPlayer, org.saga.professions.Profession)
	 */
	@Override
	public void use(Short level, SagaPlayer sagaPlayer, Profession profession) {
		
		
		sagaPlayer.initiatePattern(PATTERN, (short) (level * calculateFunctionValue(level)), false, 100);

		sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
		
		
	}

	
	/**
	 * Creates the pattern for the ability.
	 * 
	 * @return ability pattern
	 */
	private static SagaPatternElement createPattern(){
		
		
		// Initiation:
		SagaPatternListElement initiationList = new SagaPatternListElement();
		
		SagaPatternListElement mainList = new SagaPatternListElement().disableAnchorShift();
		
		// Add elements:
		
		// 0:
		mainList.addElement(new SagaPatternLogicElement(-1, 0, 0, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(-1, -1, 0, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(-1, -2, 0, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -2, 0));
		
		mainList.addElement(new SagaPatternLogicElement(1, 0, 0, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(1, -1, 0, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(1, -2, 0, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -2, 0));
		
		mainList.addElement(new SagaPatternLogicElement(-1, 0, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(-1, -1, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(-1, -2, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -2, 1));
		
		mainList.addElement(new SagaPatternLogicElement(1, 0, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(1, -1, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(1, -2, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -2, 1));
		
		mainList.addElement(new SagaPatternLogicElement(0, 0, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(0, -1, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(0, -2, 1, (short)-1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -2, 1));
		
		// 1:
		mainList.addElement(new SagaPatternLogicElement(-2, 0, 0, (short)1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(-2, -1, 0, (short)1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(-2, -2, 0, (short)1, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -2, 0));
		
		// 2:
		mainList.addElement(new SagaPatternLogicElement(2, 0, 0, (short)2, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(2, -1, 0, (short)2, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(2, -2, 0, (short)2, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -2, 0));
		
		// 3:
		mainList.addElement(new SagaPatternLogicElement(-2, 0, 1, (short)3, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(-2, -1, 1, (short)3, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(-2, -2, 1, (short)3, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -2, 1));
		
		// 4:
		mainList.addElement(new SagaPatternLogicElement(2, 0, 1, (short)4, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(2, -1, 1, (short)4, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(2, -2, 1, (short)4, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -2, 1));
		
		// 5:
		mainList.addElement(new SagaPatternLogicElement(0, 0, 2, (short)5, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(0, -1, 2, (short)5, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(0, -2, 2, (short)5, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -2, 2));
		
		// 6:
		mainList.addElement(new SagaPatternLogicElement(-1, 0, 2, (short)6, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(-1, -1, 2, (short)6, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(-1, -2, 2, (short)6, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -2, 2));
		
		// 7:
		mainList.addElement(new SagaPatternLogicElement(1, 0, 2, (short)7, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(1, -1, 2, (short)7, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(1, -2, 2, (short)7, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -2, 2));
		
		// 8:
		mainList.addElement(new SagaPatternLogicElement(-3, 0, 0, (short)8, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(-3, -1, 0, (short)8, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(-3, -2, 0, (short)8, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -2, 0));
		
		// 9:
		mainList.addElement(new SagaPatternLogicElement(3, 0, 0, (short)9, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(3, -1, 0, (short)9, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(3, -2, 0, (short)9, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -2, 0));
		
		// 10:
		mainList.addElement(new SagaPatternLogicElement(-3, 0, 1, (short)10, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(-3, -1, 1, (short)10, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(-3, -2, 1, (short)10, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -2, 1));
		
		// 11:
		mainList.addElement(new SagaPatternLogicElement(3, 0, 1, (short)11, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(3, -1, 1, (short)11, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(3, -2, 1, (short)11, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -2, 1));
		
		// 12:
		mainList.addElement(new SagaPatternLogicElement(-2, 0, 2, (short)12, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(-2, -1, 2, (short)12, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(-2, -2, 2, (short)12, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -2, 2));
		
		// 13:
		mainList.addElement(new SagaPatternLogicElement(2, 0, 2, (short)13, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(2, -1, 2, (short)13, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(2, -2, 2, (short)13, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -2, 2));
		
		// 14:
		mainList.addElement(new SagaPatternLogicElement(0, 0, 3, (short)14, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, 0, 3));
		mainList.addElement(new SagaPatternLogicElement(0, -1, 3, (short)14, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -1, 3));
		mainList.addElement(new SagaPatternLogicElement(0, -2, 3, (short)14, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -2, 3));
		
		// 15:
		mainList.addElement(new SagaPatternLogicElement(-1, 0, 3, (short)15, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, 0, 3));
		mainList.addElement(new SagaPatternLogicElement(-1, -1, 3, (short)15, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -1, 3));
		mainList.addElement(new SagaPatternLogicElement(-1, -2, 3, (short)15, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -2, 3));
		
		// 16:
		mainList.addElement(new SagaPatternLogicElement(1, 0, 3, (short)16, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, 0, 3));
		mainList.addElement(new SagaPatternLogicElement(1, -1, 3, (short)16, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -1, 3));
		mainList.addElement(new SagaPatternLogicElement(1, -2, 3, (short)16, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -2, 3));
		
		// 17:
		mainList.addElement(new SagaPatternLogicElement(-4, 0, 0, (short)17, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-4, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(-4, -1, 0, (short)17, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-4, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(-4, -2, 0, (short)17, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-4, -2, 0));
		
		// 18:
		mainList.addElement(new SagaPatternLogicElement(4, 0, 0, (short)18, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(4, 0, 0));
		mainList.addElement(new SagaPatternLogicElement(4, -1, 0, (short)18, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(4, -1, 0));
		mainList.addElement(new SagaPatternLogicElement(4, -2, 0, (short)18, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(4, -2, 0));
		
		// 19:
		mainList.addElement(new SagaPatternLogicElement(-4, 0, 1, (short)19, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-4, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(-4, -1, 1, (short)19, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-4, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(-4, -2, 1, (short)19, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-4, -2, 1));
		
		// 20:
		mainList.addElement(new SagaPatternLogicElement(4, 0, 1, (short)20, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(4, 0, 1));
		mainList.addElement(new SagaPatternLogicElement(4, -1, 1, (short)20, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(4, -1, 1));
		mainList.addElement(new SagaPatternLogicElement(4, -2, 1, (short)20, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(4, -2, 1));
		
		// 21:
		mainList.addElement(new SagaPatternLogicElement(-3, 0, 2, (short)21, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(-3, -1, 2, (short)21, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(-3, -2, 2, (short)21, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -2, 2));
		
		// 22:
		mainList.addElement(new SagaPatternLogicElement(3, 0, 2, (short)22, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, 0, 2));
		mainList.addElement(new SagaPatternLogicElement(3, -1, 2, (short)22, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -1, 2));
		mainList.addElement(new SagaPatternLogicElement(3, -2, 2, (short)22, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -2, 2));
		
		// 23:
		mainList.addElement(new SagaPatternLogicElement(-2, 0, 3, (short)23, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, 0, 3));
		mainList.addElement(new SagaPatternLogicElement(-2, -1, 3, (short)23, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -1, 3));
		mainList.addElement(new SagaPatternLogicElement(-2, -2, 3, (short)23, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -2, 3));
		
		// 24:
		mainList.addElement(new SagaPatternLogicElement(2, 0, 3, (short)23, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, 0, 3));
		mainList.addElement(new SagaPatternLogicElement(2, -1, 3, (short)23, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -1, 3));
		mainList.addElement(new SagaPatternLogicElement(2, -2, 3, (short)23, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -2, 3));
		
		// 25:
		mainList.addElement(new SagaPatternLogicElement(0, 0, 4, (short)25, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, 0, 4));
		mainList.addElement(new SagaPatternLogicElement(0, -1, 4, (short)25, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -1, 4));
		mainList.addElement(new SagaPatternLogicElement(0, -2, 4, (short)25, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(0, -2, 4));
		
		// 26:
		mainList.addElement(new SagaPatternLogicElement(-1, 0, 4, (short)26, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, 0, 4));
		mainList.addElement(new SagaPatternLogicElement(-1, -1, 4, (short)26, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -1, 4));
		mainList.addElement(new SagaPatternLogicElement(-1, -2, 4, (short)26, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -2, 4));
		
		// 27:
		mainList.addElement(new SagaPatternLogicElement(1, 0, 4, (short)27, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, 0, 4));
		mainList.addElement(new SagaPatternLogicElement(1, -1, 4, (short)27, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -1, 4));
		mainList.addElement(new SagaPatternLogicElement(1, -2, 4, (short)27, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -2, 4));
		
		// 28:
		mainList.addElement(new SagaPatternLogicElement(-2, 0, -1, (short)28, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, 0, -1));
		mainList.addElement(new SagaPatternLogicElement(-2, -1, -1, (short)28, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -1, -1));
		mainList.addElement(new SagaPatternLogicElement(-2, -2, -1, (short)28, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-2, -2, -1));
		
		// 29:
		mainList.addElement(new SagaPatternLogicElement(2, 0, -1, (short)29, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, 0, -1));
		mainList.addElement(new SagaPatternLogicElement(2, -1, -1, (short)29, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -1, -1));
		mainList.addElement(new SagaPatternLogicElement(2, -2, -1, (short)29, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(2, -2, -1));
		
		// 30:
		mainList.addElement(new SagaPatternLogicElement(-3, 0, -1, (short)30, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, 0, -1));
		mainList.addElement(new SagaPatternLogicElement(-3, -1, -1, (short)30, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -1, -1));
		mainList.addElement(new SagaPatternLogicElement(-3, -2, -1, (short)30, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-3, -2, -1));
		
		// 31:
		mainList.addElement(new SagaPatternLogicElement(3, 0, -1, (short)31, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, 0, -1));
		mainList.addElement(new SagaPatternLogicElement(3, -1, -1, (short)31, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -1, -1));
		mainList.addElement(new SagaPatternLogicElement(3, -2, -1, (short)31, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(3, -2, -1));
		
		// 32:
		mainList.addElement(new SagaPatternLogicElement(-1, 0, -1, (short)32, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, 0, -1));
		mainList.addElement(new SagaPatternLogicElement(-1, -1, -1, (short)32, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -1, -1));
		mainList.addElement(new SagaPatternLogicElement(-1, -2, -1, (short)32, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(-1, -2, -1));
		
		// 33:
		mainList.addElement(new SagaPatternLogicElement(1, 0, -1, (short)33, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, 0, -1));
		mainList.addElement(new SagaPatternLogicElement(1, -1, -1, (short)33, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -1, -1));
		mainList.addElement(new SagaPatternLogicElement(1, -2, -1, (short)33, (short)-1, new MaterialData(Material.CROPS, (byte)0x7), LogicAction.NIGNORE));
		mainList.addElement(new SagaPatternBreakElement(1, -2, -1));
		
		
		initiationList.addElement(mainList);
		return initiationList;
		
		
	}

	
}
