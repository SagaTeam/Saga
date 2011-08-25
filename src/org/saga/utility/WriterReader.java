package org.saga.utility;

import java.io.*;


import org.saga.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.NEW;

import org.saga.abilities.Ability;
import org.saga.abilities.AbilityDeserializer;
import org.saga.attributes.Attribute;
import org.saga.attributes.AttributeDeserializer;
import org.saga.chunkGroups.ChunkGroup;
import org.saga.chunkGroups.ChunkGroupDeserializer;
import org.saga.config.AttributeConfiguration;
import org.saga.config.BalanceConfiguration;
import org.saga.config.ExperienceConfiguration;
import org.saga.config.ProfessionConfiguration;
import org.saga.constants.*;
import org.saga.constants.IOConstants.ConfigType;
import org.saga.constants.IOConstants.WriteReadType;
import org.saga.factions.SagaFaction;

public class WriterReader {

	
	/**
	 * Reads the users player information.
	 * 
	 * @param playerName Player name
	 * 
	 * @return Player information
	 * 
	 * @throws IOException If an error occurred while reading
	 * @throws JsonParseException On parse failure
	 */
	public static SagaPlayer readPlayerInformation(String playerName) throws FileNotFoundException,IOException,JsonParseException {

		Gson gson = new Gson();
        return gson.fromJson(readConfig(WriteReadType.PLAYER_NORMAL, playerName + IOConstants.FILE_EXTENTENSION), SagaPlayer.class);
    
		
	}
	
	/**
	 * Writes the users player information.
	 * 
	 * @param playerName Player name
	 * @param playerInfo Player information
	 * 
	 * @return Player information
	 * 
	 * @throws IOException If an error occurred while writing.
	 */
	public static void writePlayerInformation(String playerName, SagaPlayer playerInfo) throws IOException {
		
		
		// TODO Rename method for parse (and read?) failure, to save corrupt information for recovery.	
		Gson gson = new Gson();
        writeConfig(gson.toJson(playerInfo), WriteReadType.PLAYER_NORMAL, playerName + IOConstants.FILE_EXTENTENSION);
        
            
	}
	
	/**
	 * Returns if the player exists.
	 * 
	 * @param playerName player name.
	 * @return true, if the player exists.
	 */
	public static boolean playerExists(String playerName){
		return (new File(WriteReadType.PLAYER_NORMAL.getDirectory() + playerName + IOConstants.FILE_EXTENTENSION)).exists();
		
	}

	
	/**
	 * Writes configuration.
	 * 
	 * @param config configuration String
	 * @param writeType write type
	 * @param configType configuration type
	 * @throws IOException thrown when read fails
	 */
	private static void writeConfig(String config, WriteReadType writeType,  String fileName) throws IOException {
		
		
		File directory = new File(writeType.getDirectory());
		File file = new File(writeType.getDirectory() + fileName);

		if(!directory.exists()){
			directory.mkdirs();
			Saga.info("Creating "+directory+" directory.");
		}
			
         if(!file.exists()){
         	file.createNewFile();
         	Saga.info("Creating "+file+" file.");
         }
         
         BufferedWriter out = new BufferedWriter(new FileWriter(file));
         out.write(config);
         out.close();

         
	}

	/**
	 * Reads configuration.
	 * 
	 * @param writeType write type
	 * @param configType configuration type
	 * @throws IOException thrown when read fails
	 */
	private static String readConfig(WriteReadType writeType, String fileName) throws IOException {
		
		
		// Add directory if missing:
		File directory = new File(writeType.getDirectory());
		if(!directory.exists()){
			directory.mkdirs();
			Saga.info("Creating "+directory+" directory.");
		}
		
		File file = new File(writeType.getDirectory() + fileName);
        int ch;
        StringBuffer strContent = new StringBuffer("");
        FileInputStream fin = null;
        fin = new FileInputStream(file);
        while ((ch = fin.read()) != -1){
                strContent.append((char) ch);
        }
        fin.close();

        return strContent.toString();

         
	}

	/**
	 * Writes configuration.
	 * 
	 * @param config configuration String
	 * @param writeType write type
	 * @param configType configuration type
	 * @throws IOException thrown when read fails
	 */
	public static void writeConfig(String config, WriteReadType writeType, ConfigType configType) throws IOException {
		
		writeConfig(config, writeType, configType.getFileName());
         
	}

	/**
	 * Reads configuration.
	 * 
	 * @param writeType write type
	 * @param configType configuration type
	 * @throws IOException thrown when read fails
	 */
	public static String readConfig(WriteReadType writeType, ConfigType configType) throws IOException {

		return readConfig(writeType, configType.getFileName());
         
	}
	
	
	/**
	 * Reads configuration.
	 * 
	 * @return configuration
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static ExperienceConfiguration readExperienceConfig() throws JsonParseException, IOException {

		
         Gson gson = new Gson();
         return gson.fromJson(readConfig(WriteReadType.NORMAL, ConfigType.EXPERIENCE.getFileName()).toString(), ExperienceConfiguration.class);
     
		
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeExperienceConfig(ExperienceConfiguration config, WriteReadType writeType) throws IOException {

		
         Gson gson = new Gson();
         writeConfig(gson.toJson(config), writeType, ConfigType.EXPERIENCE.getFileName());
 		
		
	}

	
	/**
	 * Reads configuration.
	 * 
	 * @return configuration
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static BalanceConfiguration readBalanceConfig() throws JsonParseException, IOException {

		
         Gson gson = new Gson();
         return gson.fromJson(readConfig(WriteReadType.NORMAL, ConfigType.BALANCE.getFileName()).toString(), BalanceConfiguration.class);
     
		
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeBalanceConfig(BalanceConfiguration config, WriteReadType writeType) throws IOException {

		
         Gson gson = new Gson();
         writeConfig(gson.toJson(config), writeType, ConfigType.BALANCE.getFileName());
 		
		
	}

	
	/**
	 * Reads configuration.
	 * 
	 * @return configuration
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static AttributeConfiguration readAttributeConfig() throws JsonParseException, IOException {

		
		GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(readConfig(WriteReadType.NORMAL, ConfigType.ATTRIBUTE.getFileName()).toString(), AttributeConfiguration.class);
     
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeAttributeConfig(AttributeConfiguration config, WriteReadType writeType) throws IOException {

		
         Gson gson = new Gson();
         writeConfig(gson.toJson(config), writeType, ConfigType.ATTRIBUTE.getFileName());
 		
		
	}


	/**
	 * Reads configuration.
	 * 
	 * @return configuration
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static ProfessionConfiguration readProfessionConfig() throws JsonParseException, IOException {

		
         Gson gson = new Gson();
         return gson.fromJson(readConfig(WriteReadType.NORMAL, ConfigType.PROFESSION.getFileName()).toString(), ProfessionConfiguration.class);
     
		
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeProfessionConfig(ProfessionConfiguration config, WriteReadType writeType) throws IOException {

		
		GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Ability.class, new AbilityDeserializer());
        Gson gson = gsonBuilder.create();
        writeConfig(gson.toJson(config), writeType, ConfigType.PROFESSION.getFileName());
 		
		
	}


	/**
	 * Reads configuration.
	 * 
	 * @param name name
	 * @return configuration
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static Ability readAbilityConfig(String name) throws JsonParseException, IOException {

		
		GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Ability.class, new AbilityDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(readConfig(WriteReadType.ABILITY_NORMAL, name + IOConstants.FILE_EXTENTENSION), Ability.class);
     
		
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param name name
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeAbilityConfig(String name, Ability config, WriteReadType writeType) throws IOException {

		
		GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Ability.class, new AbilityDeserializer());
        Gson gson = gsonBuilder.create();
        writeConfig(gson.toJson(config), writeType, name + IOConstants.FILE_EXTENTENSION);
 		
		
	}


	/**
	 * Reads configuration.
	 * 
	 * @param factionId faction ID
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static SagaFaction readFaction(String factionId) throws JsonParseException, IOException {

		
		Gson gson = new Gson();
        return gson.fromJson(readConfig(WriteReadType.FACTION_NORMAL, factionId + IOConstants.FILE_EXTENTENSION), SagaFaction.class);
		
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param factionID faction ID
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeFaction(String factionID, SagaFaction config, WriteReadType writeType) throws IOException {

		
        Gson gson = new Gson();
        writeConfig(gson.toJson(config), writeType, factionID + IOConstants.FILE_EXTENTENSION);
 		
		
	}

	/**
	 * Moves the faction file to the folder for deleted factions.
	 * 
	 * @param factionID
	 */
	public static void deleteFaction(String factionID) {
		
		
		// Create folders:
		File deletedDirectory = new File(WriteReadType.FACTION_DELETED.getDirectory());
		File factionDirectory = new File(WriteReadType.FACTION_NORMAL.getDirectory());
		File deletedFile = new File(WriteReadType.FACTION_DELETED.getDirectory() + factionID + IOConstants.FILE_EXTENTENSION);
		File factionFile = new File(WriteReadType.FACTION_NORMAL.getDirectory() + factionID + IOConstants.FILE_EXTENTENSION);

		
		if(!deletedDirectory.exists()){
			deletedDirectory.mkdirs();
			Saga.info("Creating "+deletedDirectory+" directory.");
		}
		
		if(!factionDirectory.exists()){
			factionDirectory.mkdirs();
			Saga.info("Creating "+factionDirectory+" directory.");
		}
		
		// Check if exists.
		if(!factionFile.exists()){
			Saga.severe("Cant move "+factionFile+", because it doesent exist.");
		}
		
		// Rename if target exists:
		for (int i = 1; i < 1000; i++) {
			if (deletedFile.exists()) {
				deletedFile.renameTo(new File(WriteReadType.FACTION_DELETED.getDirectory() + factionID + "(" + i + ")" + IOConstants.FILE_EXTENTENSION));
			}else{
				break;
			}
			
		}
		
		// Move file to deleted folder:
		boolean success = factionFile.renameTo(deletedFile);
		
		// Notify on failure:
		if(success){
			Saga.info("Moved " + factionFile + " file to deleted factions folder.");
		}else{
			Saga.severe("Failed to move "+factionFile+" to deleted factions folder.");
		}
		

	}
	
	/**
	 * Gets all faction IDs in String form for reading.
	 * 
	 * @return all filenames
	 */
	public static String[] getAllFactionIds() {

		
		File directory = new File(WriteReadType.FACTION_NORMAL.getDirectory());
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(IOConstants.FILE_EXTENTENSION);
			}
		};
		
		if(!directory.exists()){
			directory.mkdirs();
			Saga.info("Creating "+directory+" directory.");
		}
		
		String[] names = directory.list(filter);
		
		if(names == null){
			Saga.severe("Could not retrieve faction names.");
			names = new String[0];
		}
		
		// Remove extensions:
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].replaceAll(IOConstants.FILE_EXTENTENSION, "");
		}
		
		return names;

		
	}


	/**
	 * Reads configuration.
	 * 
	 * @param settlementId faction ID
	 * @throws JsonSyntaxException when parse fails
	 * @throws IOException when read fails
	 */
	public static ChunkGroup readChunkGroup(String settlementId) throws JsonParseException, IOException {

		
		GsonBuilder gsonBuilder= new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChunkGroup.class, new ChunkGroupDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(readConfig(WriteReadType.SETTLEMENT_NORMAL, settlementId + IOConstants.FILE_EXTENTENSION), ChunkGroup.class);
		
		
	}
	
	/**
	 * Writes configuration.
	 * 
	 * @param groupId faction ID
	 * @param writeType write type
	 * @throws IOException when write fails
	 */
	public static void writeChunkGroup(String groupId, ChunkGroup config, WriteReadType writeType) throws IOException {

		
        Gson gson = new Gson();
        writeConfig(gson.toJson(config), writeType, groupId + IOConstants.FILE_EXTENTENSION);
 		
		
	}

	/**
	 * Moves the chunk group file to the folder for deleted factions.
	 * 
	 * @param groupId
	 */
	public static void deleteChunkGroup(String groupId) {
		
		
		// Create folders:
		File deletedDirectory = new File(WriteReadType.SETTLEMENT_DELETED.getDirectory());
		File factionDirectory = new File(WriteReadType.SETTLEMENT_NORMAL.getDirectory());
		File deletedFile = new File(WriteReadType.SETTLEMENT_DELETED.getDirectory() + groupId + IOConstants.FILE_EXTENTENSION);
		File factionFile = new File(WriteReadType.SETTLEMENT_NORMAL.getDirectory() + groupId + IOConstants.FILE_EXTENTENSION);

		
		if(!deletedDirectory.exists()){
			deletedDirectory.mkdirs();
			Saga.info("Creating "+deletedDirectory+" directory.");
		}
		
		if(!factionDirectory.exists()){
			factionDirectory.mkdirs();
			Saga.info("Creating "+factionDirectory+" directory.");
		}
		
		// Check if exists.
		if(!factionFile.exists()){
			Saga.severe("Cant move "+factionFile+", because it doesent exist.");
		}
		
		// Rename if target exists:
		for (int i = 1; i < 1000; i++) {
			if (deletedFile.exists()) {
				deletedFile.renameTo(new File(WriteReadType.SETTLEMENT_DELETED.getDirectory() + groupId + "(" + i + ")" + IOConstants.FILE_EXTENTENSION));
			}else{
				break;
			}
			
		}
		
		// Move file to deleted folder:
		boolean success = factionFile.renameTo(deletedFile);
		
		// Notify on failure:
		if(success){
			Saga.info("Moved " + factionFile + " file to deleted factions folder.");
		}else{
			Saga.severe("Failed to move "+factionFile+" to deleted factions folder.");
		}
		

	}

	/**
	 * Gets all settlement IDs in String form for reading.
	 * 
	 * @return all filenames
	 */
	public static String[] getAllChunkGroupIds() {

		
		File directory = new File(WriteReadType.SETTLEMENT_NORMAL.getDirectory());
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(IOConstants.FILE_EXTENTENSION);
			}
		};
		
		if(!directory.exists()){
			directory.mkdirs();
			Saga.info("Creating "+directory+" directory.");
		}
		
		String[] names = directory.list(filter);
		
		if(names == null){
			Saga.severe("Could not retrieve faction names.");
			names = new String[0];
		}
		
		// Remove extensions:
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].replaceAll(IOConstants.FILE_EXTENTENSION, "");
		}
		
		return names;

		
	}

	
	
}
