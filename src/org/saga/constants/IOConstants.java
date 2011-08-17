/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.constants;

import java.io.File;


/**
 *
 * @author Cory
 */
public class IOConstants {

    
    public static final String WILDERNESS_STRING = "~Wilderness~";

    
    public static final String FILE_EXTENTENSION = ".json";

    
	public enum WriteReadType{
		
		
		PLUGIN_DIRECTORY("plugins" + File.separator + "Saga" + File.separator),
		NORMAL(PLUGIN_DIRECTORY.getDirectory() + "config" + File.separator),
		BACKUP(PLUGIN_DIRECTORY.getDirectory() + File.separator + "backup" + File.separator),
		DEFAULTS(PLUGIN_DIRECTORY.getDirectory() + "defaults" + File.separator),
		OTHER(PLUGIN_DIRECTORY.getDirectory() + "other" + File.separator),
		
		ABILITY_NORMAL(NORMAL.getDirectory() + "abilities" + File.separator),
		ABILITY_DEFAULTS(DEFAULTS.getDirectory() + "abilities" + File.separator),
		
		PLAYER_NORMAL(PLUGIN_DIRECTORY.getDirectory() + "players" + File.separator),
		PLAYER_BACKUP(PLUGIN_DIRECTORY.getDirectory() + "players backup" + File.separator);
		
		
		private String directory;
		
		
		private WriteReadType(String fileName) {
			this.directory = fileName;
		}
		
		
		public String getDirectory() {
			return directory;
		}
		
		
	}
	
	public enum ConfigType{
		
		
		EXPERIENCE("experience" + FILE_EXTENTENSION),
		ATTRIBUTE("attributes" + FILE_EXTENTENSION),
		PROFESSION("professions"+ FILE_EXTENTENSION),
		BALANCE("balance" + FILE_EXTENTENSION);
		
		
		private String fileName;
		
		
		private ConfigType(String fileName) {
			this.fileName = fileName;
		}
		
		
		public String getFileName() {
			return fileName;
		}
		
		
	}
	
    
    
    
    
}
