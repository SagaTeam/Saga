package org.saga.abilities;


import org.bukkit.Material;
import org.saga.Saga;
import org.saga.SagaPlayer;
import org.saga.constants.PlayerMessages;
import org.saga.pattern.SagaPatternBrakeElement;
import org.saga.pattern.SagaPatternCheckElement;
import org.saga.pattern.SagaPatternElement;
import org.saga.pattern.SagaPatternListElement;
import org.saga.pattern.SagaPatternCheckElement.CheckAction;


public class PowerfulSwings extends AbilityFunction {

	/**
	 * Ability name.
	 */
	transient public static final String ABILITY_NAME = "powerful swings";

	/**
	 * Pattern.
	 */
	transient private static SagaPatternElement PATTERN = createPattern();

	
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
		
		
		sagaPlayer.initiatePattern(PATTERN, (short) (level * calculateFunctionValue(level)), orthogonalFlip);
		
		
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
		elements[0] = new SagaPatternCheckElement(-2 , 3 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[1] = new SagaPatternBrakeElement(-2 , 3 , 0, (short)16 , (short)-1 );
		
		// I:
		elements[2] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[3] = new SagaPatternBrakeElement(0 , 0 , 1, (short)16 , (short)-1);
					
		// II-:
		elements[4] = new SagaPatternCheckElement(1 , -1 , -1, throughMaterials, false, CheckAction.IGNORE);
		elements[5] = new SagaPatternBrakeElement(1 , -1 , -1, (short)12 , (short)-1);
		elements[6] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[7] = new SagaPatternBrakeElement(0 , 1 , 0, (short)18 , (short)-1);
		elements[8] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[9] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)16 , (short)-1);
		elements[10] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[11] = new SagaPatternBrakeElement(0 , -1 , 0, (short)4 , (short)-1);
		elements[12] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[13] = new SagaPatternBrakeElement(0 , -1 , 0, (short)2 , (short)-1);
		elements[14] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[15] = new SagaPatternBrakeElement(1 , 1 , 0, (short)12 , (short)-1);
		
		// II:
		elements[16] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[17] = new SagaPatternBrakeElement(0 , 0 , 1, (short)12 , (short)-1);
		elements[18] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[19] = new SagaPatternBrakeElement(0 , 1 , 0, (short)18 , (short)-1);
		elements[20] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[21] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)16 , (short)-1);
		elements[22] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[23] = new SagaPatternBrakeElement(0 , -1 , 0, (short)4 , (short)-1);
		elements[24] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[25] = new SagaPatternBrakeElement(0 , -1 , 0, (short)2 , (short)-1);
		elements[26] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[27] = new SagaPatternBrakeElement(1 , 1 , 0, (short)12 , (short)-1);
		
		// II+:
		elements[28] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[29] = new SagaPatternBrakeElement(0 , 0 , 1, (short)7 , (short)-1);
		
		// III-:
		elements[30] = new SagaPatternCheckElement(1 , -1 , -2, throughMaterials, false, CheckAction.IGNORE);
		elements[31] = new SagaPatternBrakeElement(1 , -1 , -2);
		elements[32] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[33] = new SagaPatternBrakeElement(1 , 1 , 0, (short)22 , (short)-1);
		elements[34] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[35] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)14 , (short)-1);
		elements[36] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[37] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)12 , (short)-1);
		elements[38] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[39] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[40] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[41] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[42] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[43] = new SagaPatternBrakeElement(1 , 1 , 0);
		
		// III:
		elements[44] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[45] = new SagaPatternBrakeElement(0 , 0 , 1);
		elements[46] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[47] = new SagaPatternBrakeElement(1 , 1 , 0, (short)22 , (short)-1);
		elements[48] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[49] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)14 , (short)-1);
		elements[50] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[51] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)12 , (short)-1);
		elements[52] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[53] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[54] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[55] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[56] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[57] = new SagaPatternBrakeElement(1 , 1 , 0);
		
		// III+:
		elements[58] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[59] = new SagaPatternBrakeElement(0 , 0 , 1);
		elements[60] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[61] = new SagaPatternBrakeElement(0 , 1 , 0, (short)13 , (short)-1);
		elements[62] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[63] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)7 , (short)-1);
		elements[64] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[65] = new SagaPatternBrakeElement(0 , -1 , 0, (short)5 , (short)-1);
		elements[66] = new SagaPatternCheckElement(1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[67] = new SagaPatternBrakeElement(1 , 0 , 0);
		
		// IV-:
		elements[68] = new SagaPatternCheckElement(1 , -1 , -2, throughMaterials, false, CheckAction.IGNORE);
		elements[69] = new SagaPatternBrakeElement(1 , -1 , -2);
		elements[70] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[71] = new SagaPatternBrakeElement(1 , 1 , 0, (short)20 , (short)-1);
		elements[72] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[73] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)10 , (short)-1);
		elements[74] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[75] = new SagaPatternBrakeElement(-1 , 0 , 0);
		elements[76] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[77] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[78] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[79] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[80] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[81] = new SagaPatternBrakeElement(1 , 1 , 0);
		
		// IV:
		elements[82] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[83] = new SagaPatternBrakeElement(0 , 0 , 1);
		elements[84] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[85] = new SagaPatternBrakeElement(1 , 1 , 0, (short)20 , (short)-1);
		elements[86] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[87] = new SagaPatternBrakeElement(-1 , 0 , 0, (short)10 , (short)-1);
		elements[88] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[89] = new SagaPatternBrakeElement(-1 , 0 , 0);
		elements[90] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[91] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[92] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[93] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[94] = new SagaPatternCheckElement(1 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[95] = new SagaPatternBrakeElement(1 , 1 , 0);
		
		// IV+:
		elements[96] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[97] = new SagaPatternBrakeElement(0 , 0 , 1);
		elements[98] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[99] = new SagaPatternBrakeElement(0 , 1 , 0, (short)19 , (short)-1);
		elements[100] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[101] = new SagaPatternBrakeElement(-1 , 0 , 0);
		elements[102] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[103] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[104] = new SagaPatternCheckElement(1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[105] = new SagaPatternBrakeElement(1 , 0 , 0);
		
		// V-:
		elements[106] = new SagaPatternCheckElement(1 , -1 , -2, throughMaterials, false, CheckAction.IGNORE);
		elements[107] = new SagaPatternBrakeElement(1 , -1 , -2, (short)6 , (short)-1);
		elements[108] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[109] = new SagaPatternBrakeElement(0 , 1 , 0, (short)8 , (short)-1);
		elements[110] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[111] = new SagaPatternBrakeElement(-1 , 0 , 0);
		elements[112] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[113] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[114] = new SagaPatternCheckElement(1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[115] = new SagaPatternBrakeElement(1 , 0 , 0, (short)6 , (short)-1);
		
		// V:
		elements[116] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[117] = new SagaPatternBrakeElement(0 , 0 , 1, (short)6 , (short)-1);
		elements[118] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[119] = new SagaPatternBrakeElement(0 , 1 , 0, (short)8 , (short)-1);
		elements[120] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[121] = new SagaPatternBrakeElement(-1 , 0 , 0);
		elements[122] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[123] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[124] = new SagaPatternCheckElement(1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[125] = new SagaPatternBrakeElement(1 , 0 , 0, (short)6 , (short)-1);
		
		// V+:
		elements[126] = new SagaPatternCheckElement(0 , 0 , 1, throughMaterials, false, CheckAction.IGNORE);
		elements[127] = new SagaPatternBrakeElement(0 , 0 , 1, (short)9 , (short)-1);
		elements[128] = new SagaPatternCheckElement(0 , 1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[129] = new SagaPatternBrakeElement(0 , 1 , 0, (short)15 , (short)-1);
		elements[130] = new SagaPatternCheckElement(-1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[131] = new SagaPatternBrakeElement(-1 , 0 , 0);
		elements[132] = new SagaPatternCheckElement(0 , -1 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[133] = new SagaPatternBrakeElement(0 , -1 , 0);
		elements[134] = new SagaPatternCheckElement(1 , 0 , 0, throughMaterials, false, CheckAction.IGNORE);
		elements[135] = new SagaPatternBrakeElement(1 , 0 , 0, (short)9 , (short)-1);
		
		
		// List:
		return new SagaPatternListElement(elements);
		
		
	}
	
	
	
	
	
}
