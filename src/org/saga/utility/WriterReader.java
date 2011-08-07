package org.saga.utility;

import java.io.*;
import java.util.Properties;

import java.util.Vector;

import org.saga.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.saga.abilities.Ability;
import org.saga.abilities.AbilityDeserializer;
import org.saga.attributes.Attribute;
import org.saga.attributes.AttributeDeserializer;
import org.saga.constants.*;
import org.saga.professions.Profession;
import org.saga.professions.ProfessionDeserializer;

public class WriterReader {

	
	/**
	 * File name for balance information.
	 */
	private static String BALANCE_INFORMATION_FILENAME="balanceinformation.json";
	
	/**
	 * File name for attribute information.
	 */
	private static String ATTRIBUTE_INFORMATION_FILENAME="attributeinformation.json";
	
	
	/**
	 * Suffix for default.
	 */
	public static String SUFFIX_DEFAULT=".default";
	
	/**
	 * Suffix for none.
	 */
	public static String SUFFIX_NONE="";
	
	
	
	/**
	 * Reads the users player information.
	 * 
	 * @param pPlayerName Player name
	 * 
	 * @return Player information
	 * 
	 * @throws IOException If an error occurred while reading
	 * @throws JsonParseException On parse failure
	 */
	public static SagaPlayer readPlayerInformation(String pPlayerName) throws FileNotFoundException,IOException,JsonParseException {

            String directory = General.PLAYER_DIRECTORY + pPlayerName + ".json";
        
            File file = new File(directory);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1){
                strContent.append((char) ch);
            }
            fin.close();

            GsonBuilder gsonBuilder= new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Profession.class, new ProfessionDeserializer());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(strContent.toString(), SagaPlayer.class);
		
		
	}

	// TODO Rename method for parse (and read?) failure, to save corrupt information for recovery.	
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
		
		
            File directory = new File(General.PLAYER_DIRECTORY);
            File file = new File(General.PLAYER_DIRECTORY + playerName + ".json");

            if( !directory.exists() ) {
                directory.mkdirs();
                Saga.info("Creating "+directory+" directory.");
            }

            if( !file.exists() ) {
            	file.createNewFile();
            	Saga.info("Creating "+file+" file.");
            }
            
            Gson gson = new Gson();
            
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(gson.toJson(playerInfo));
            out.close();
            
            
	}
	
	/**
	 * Returns if the player exists.
	 * 
	 * @param playerName player name.
	 * @return true, if the player exists.
	 */
	public static boolean playerExists(String playerName){
		
		return (new File(General.PLAYER_DIRECTORY+playerName+".json")).exists();
		
	}
	
	/**
	 * Reads attribute information
	 * 
	 * @return attribute information
	 * @throws IOException if an error occurred while reading
	 * @throws JsonParseException on parse failure
	 */
	public static AttributeInformation readAttributeInformation() throws IOException, JsonParseException{

		
            String fileDirectory = General.PLUGIN_DIRECTORY+ATTRIBUTE_INFORMATION_FILENAME;
		
            File file = new File(fileDirectory);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1){
                    strContent.append((char) ch);
            }
            fin.close();
//            System.out.println(strContent);

            GsonBuilder gsonBuilder= new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(strContent.toString(), AttributeInformation.class);
        
            
	}
	

	/**
	 * Writes the attribute information. Used to generate a default balance information file.
	 * 
	 * @param attributeInfo attribute information
	 * 
	 * @return Player information
	 * @param suffix suffix that will be added to the file name
	 * 
	 * @throws IOException If an error occurred while writing.
	 */
	public static void writeAttributeInformation(AttributeInformation attributeInfo, String suffix) throws IOException {

		
            File directory = new File(General.PLUGIN_DIRECTORY);
            File file = new File(General.PLUGIN_DIRECTORY+ATTRIBUTE_INFORMATION_FILENAME+suffix);

			if(!directory.exists()){
				directory.mkdirs();
            	Saga.info("Creating "+directory+" directory.");
            }
			
            if(!file.exists()){
            	file.createNewFile();
            	Saga.info("Creating "+file+" file.");
            }
            
            Gson gson= new Gson();
            
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(gson.toJson(attributeInfo));
            out.close();
		
            
	}
	
	

	/**
	 * Reads attribute information
	 * 
	 * @return attribute information information
	 * @throws IOException if an error occurred while reading
	 * @throws JsonParseException on parse failure
	 */
	public static BalanceInformation readBalanceInformation() throws IOException, JsonParseException{

            String fileDirectory = General.PLUGIN_DIRECTORY+BALANCE_INFORMATION_FILENAME;
		
            File file = new File(fileDirectory);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1){
                    strContent.append((char) ch);
            }
            fin.close();
//            System.out.println(strContent);

            GsonBuilder gsonBuilder= new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Ability.class, new AbilityDeserializer());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(strContent.toString(), BalanceInformation.class);
        
	}
	

	/**
	 * Writes the balance information. Used to generate a default balance information file.
	 * 
	 * @param balanceInfo Player information
	 * 
	 * @return Player information
	 * @param suffix suffix that will be added to the file name
	 * 
	 * @throws IOException If an error occurred while writing.
	 */
	public static void writeBalanceInformation(BalanceInformation balanceInfo, String suffix) throws IOException {

            File directory = new File(General.PLUGIN_DIRECTORY);
            File file = new File(General.PLUGIN_DIRECTORY+BALANCE_INFORMATION_FILENAME+suffix);

			if(!directory.exists()){
				directory.mkdirs();
            	Saga.info("Creating "+directory+" directory.");
            }
			
            if(!file.exists()){
            	file.createNewFile();
            	Saga.info("Creating "+file+" file.");
            }
            
            Gson gson= new Gson();
            
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(gson.toJson(balanceInfo));
            out.close();
		
            
	}
	
	
	
}
