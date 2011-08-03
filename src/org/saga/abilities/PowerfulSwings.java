package org.saga.abilities;


import org.bukkit.Material;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternBreakElement;
import org.saga.pattern.SagaPatternLogicElement;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternLogicElement.LogicAction;


public class PowerfulSwings extends AbilityFunction {

	
	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "powerful swings";

	/**
	 * Pattern.
	 */
	transient private static SagaPatternElement PATTERN = createPattern2();

	
	/**
	 * Used by gson.
	 * 
	 */
	public PowerfulSwings() {
		
		super(ABILITY_NAME);
		
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
	
	
	
	/**
	 * Uses the ability.
	 * 
	 * @param level level
	 * @param sagaPlayer saga player
	 * @param orthogonalFlip if true, there will be a orthogonal flip
	 */
	public void use(Short level, SagaPlayer sagaPlayer, boolean orthogonalFlip) {
		
		
		sagaPlayer.initiatePattern(PATTERN, (short) (level * calculateFunctionValue(level)), orthogonalFlip, 100);
		
		
		sagaPlayer.sendMessage(PlayerMessages.usedAbility(this));
		
		
	}
	

	/**
	 * Creates the pattern for the ability.
	 * 
	 * @return ability pattern
	 */
	private static SagaPatternElement createPattern(){
		
		
		SagaPatternElement[] elements = new SagaPatternElement[136];
		
		Material[] throughMaterials = new Material[]{Material.STONE, Material.AIR};
		
		// I-:
		elements[0] = new SagaPatternLogicElement(-2 , 3 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[1] = new SagaPatternBreakElement(-2 , 3 , 0, (short)16 , (short)-1 );
		
		// I:
		elements[2] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[3] = new SagaPatternBreakElement(0 , 0 , 1, (short)16 , (short)-1);
					
		// II-:
		elements[4] = new SagaPatternLogicElement(1 , -1 , -1, throughMaterials, LogicAction.NIGNORE);
		elements[5] = new SagaPatternBreakElement(1 , -1 , -1, (short)12 , (short)-1);
		elements[6] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[7] = new SagaPatternBreakElement(0 , 1 , 0, (short)18 , (short)-1);
		elements[8] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[9] = new SagaPatternBreakElement(-1 , 0 , 0, (short)16 , (short)-1);
		elements[10] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[11] = new SagaPatternBreakElement(0 , -1 , 0, (short)4 , (short)-1);
		elements[12] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[13] = new SagaPatternBreakElement(0 , -1 , 0, (short)2 , (short)-1);
		elements[14] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[15] = new SagaPatternBreakElement(1 , 1 , 0, (short)12 , (short)-1);
		
		// II:
		elements[16] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[17] = new SagaPatternBreakElement(0 , 0 , 1, (short)12 , (short)-1);
		elements[18] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[19] = new SagaPatternBreakElement(0 , 1 , 0, (short)18 , (short)-1);
		elements[20] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[21] = new SagaPatternBreakElement(-1 , 0 , 0, (short)16 , (short)-1);
		elements[22] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[23] = new SagaPatternBreakElement(0 , -1 , 0, (short)4 , (short)-1);
		elements[24] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[25] = new SagaPatternBreakElement(0 , -1 , 0, (short)2 , (short)-1);
		elements[26] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[27] = new SagaPatternBreakElement(1 , 1 , 0, (short)12 , (short)-1);
		
		// II+:
		elements[28] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[29] = new SagaPatternBreakElement(0 , 0 , 1, (short)7 , (short)-1);
		
		// III-:
		elements[30] = new SagaPatternLogicElement(1 , -1 , -2, throughMaterials, LogicAction.NIGNORE);
		elements[31] = new SagaPatternBreakElement(1 , -1 , -2);
		elements[32] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[33] = new SagaPatternBreakElement(1 , 1 , 0, (short)22 , (short)-1);
		elements[34] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[35] = new SagaPatternBreakElement(-1 , 0 , 0, (short)14 , (short)-1);
		elements[36] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[37] = new SagaPatternBreakElement(-1 , 0 , 0, (short)12 , (short)-1);
		elements[38] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[39] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[40] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[41] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[42] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[43] = new SagaPatternBreakElement(1 , 1 , 0);
		
		// III:
		elements[44] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[45] = new SagaPatternBreakElement(0 , 0 , 1);
		elements[46] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[47] = new SagaPatternBreakElement(1 , 1 , 0, (short)22 , (short)-1);
		elements[48] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[49] = new SagaPatternBreakElement(-1 , 0 , 0, (short)14 , (short)-1);
		elements[50] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[51] = new SagaPatternBreakElement(-1 , 0 , 0, (short)12 , (short)-1);
		elements[52] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[53] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[54] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[55] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[56] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[57] = new SagaPatternBreakElement(1 , 1 , 0);
		
		// III+:
		elements[58] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[59] = new SagaPatternBreakElement(0 , 0 , 1);
		elements[60] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[61] = new SagaPatternBreakElement(0 , 1 , 0, (short)13 , (short)-1);
		elements[62] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[63] = new SagaPatternBreakElement(-1 , 0 , 0, (short)7 , (short)-1);
		elements[64] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[65] = new SagaPatternBreakElement(0 , -1 , 0, (short)5 , (short)-1);
		elements[66] = new SagaPatternLogicElement(1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[67] = new SagaPatternBreakElement(1 , 0 , 0);
		
		// IV-:
		elements[68] = new SagaPatternLogicElement(1 , -1 , -2, throughMaterials, LogicAction.NIGNORE);
		elements[69] = new SagaPatternBreakElement(1 , -1 , -2);
		elements[70] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[71] = new SagaPatternBreakElement(1 , 1 , 0, (short)20 , (short)-1);
		elements[72] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[73] = new SagaPatternBreakElement(-1 , 0 , 0, (short)10 , (short)-1);
		elements[74] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[75] = new SagaPatternBreakElement(-1 , 0 , 0);
		elements[76] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[77] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[78] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[79] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[80] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[81] = new SagaPatternBreakElement(1 , 1 , 0);
		
		// IV:
		elements[82] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[83] = new SagaPatternBreakElement(0 , 0 , 1);
		elements[84] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[85] = new SagaPatternBreakElement(1 , 1 , 0, (short)20 , (short)-1);
		elements[86] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[87] = new SagaPatternBreakElement(-1 , 0 , 0, (short)10 , (short)-1);
		elements[88] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[89] = new SagaPatternBreakElement(-1 , 0 , 0);
		elements[90] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[91] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[92] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[93] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[94] = new SagaPatternLogicElement(1 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[95] = new SagaPatternBreakElement(1 , 1 , 0);
		
		// IV+:
		elements[96] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[97] = new SagaPatternBreakElement(0 , 0 , 1);
		elements[98] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[99] = new SagaPatternBreakElement(0 , 1 , 0, (short)19 , (short)-1);
		elements[100] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[101] = new SagaPatternBreakElement(-1 , 0 , 0);
		elements[102] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[103] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[104] = new SagaPatternLogicElement(1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[105] = new SagaPatternBreakElement(1 , 0 , 0);
		
		// V-:
		elements[106] = new SagaPatternLogicElement(1 , -1 , -2, throughMaterials, LogicAction.NIGNORE);
		elements[107] = new SagaPatternBreakElement(1 , -1 , -2, (short)6 , (short)-1);
		elements[108] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[109] = new SagaPatternBreakElement(0 , 1 , 0, (short)8 , (short)-1);
		elements[110] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[111] = new SagaPatternBreakElement(-1 , 0 , 0);
		elements[112] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[113] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[114] = new SagaPatternLogicElement(1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[115] = new SagaPatternBreakElement(1 , 0 , 0, (short)6 , (short)-1);
		
		// V:
		elements[116] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[117] = new SagaPatternBreakElement(0 , 0 , 1, (short)6 , (short)-1);
		elements[118] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[119] = new SagaPatternBreakElement(0 , 1 , 0, (short)8 , (short)-1);
		elements[120] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[121] = new SagaPatternBreakElement(-1 , 0 , 0);
		elements[122] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[123] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[124] = new SagaPatternLogicElement(1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[125] = new SagaPatternBreakElement(1 , 0 , 0, (short)6 , (short)-1);
		
		// V+:
		elements[126] = new SagaPatternLogicElement(0 , 0 , 1, throughMaterials, LogicAction.NIGNORE);
		elements[127] = new SagaPatternBreakElement(0 , 0 , 1, (short)9 , (short)-1);
		elements[128] = new SagaPatternLogicElement(0 , 1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[129] = new SagaPatternBreakElement(0 , 1 , 0, (short)15 , (short)-1);
		elements[130] = new SagaPatternLogicElement(-1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[131] = new SagaPatternBreakElement(-1 , 0 , 0);
		elements[132] = new SagaPatternLogicElement(0 , -1 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[133] = new SagaPatternBreakElement(0 , -1 , 0);
		elements[134] = new SagaPatternLogicElement(1 , 0 , 0, throughMaterials, LogicAction.NIGNORE);
		elements[135] = new SagaPatternBreakElement(1 , 0 , 0, (short)9 , (short)-1);
		
		
		// List:
		return new SagaPatternListElement(elements);
		
		
	}
	
	private static SagaPatternElement createPattern2(){
		
		
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
