package org.saga.constants;

import org.bukkit.Material;

public class BlockConstants {

	public static final Material[] TRANSPARENT_MATERIALS = new Material[]{Material.AIR, Material.LONG_GRASS, Material.WATER};
	
	
	/**
	 * Checks if the material is transparent.
	 * 
	 * @param material
	 * @return true if the material is transparent
	 */
	public static boolean isTransparent(Material material) {
		
		for (int i = 0; i < TRANSPARENT_MATERIALS.length; i++) {
			if(TRANSPARENT_MATERIALS[i].equals(material)){
				return true;
			}
		}
		return false;
		
	}
	
}
