package org.saga.utility;

import java.io.*;
import java.util.Properties;

import java.util.Vector;

import org.saga.*;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class WriterReader {

	/**
	 * Main directory for plugin files.
	 */
	private static String MAIN_DIRECTORY="plugins"+File.separator+"Saga";
	
	/**
	 * Player information directory.
	 */
//	private static String PLAYER_INFORMATION_DIRECTORY="/home/andf/data/java/minecraft/plugins/SagaPlayer/players/";
	private static String PLAYER_INFORMATION_DIRECTORY=MAIN_DIRECTORY+File.separator+"players"+File.separator;
	
	/**
	 * Balance information location.
	 */
//	private static String BALANCE_INFORMATION_DIRECTORY="/home/andf/data/java/minecraft/plugins/SagaPlayer/balanceinformation";
	private static String BALANCE_INFORMATION_DIRECTORY=MAIN_DIRECTORY+File.separator;
	
	/**
	 * File name for balance information.
	 */
	private static String BALANCE_INFORMATION_FILENAME="balanceinformation.json";
	
	
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

            String directory= PLAYER_INFORMATION_DIRECTORY+pPlayerName+".json";
        
            File file = new File(directory);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1){
                    strContent.append((char) ch);
            }
            fin.close();
        //        System.out.println(strContent);

            Gson gson= new Gson();
            return gson.fromJson(strContent.toString(), SagaPlayer.class);
		
		
	}

	// TODO Rename method for parse (and read?) failure.	
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
		
            File directory = new File(PLAYER_INFORMATION_DIRECTORY);
            File file = new File(PLAYER_INFORMATION_DIRECTORY+playerName+".json");

            if( !directory.exists() ) {
                directory.mkdirs();
                Saga.info("Creating "+directory+" directory.");
            }

            if(!file.exists()){
            	file.createNewFile();
            	Saga.info("Creating "+file+" file.");
            }
            
            Gson gson= new Gson();
            
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(gson.toJson(playerInfo));
            out.close();
            
	}
	
	/**
	 * Reads balance information
	 * 
	 * @return Balance information
	 * @throws IOException if an error occurred while reading
	 * @throws JsonParseException on parse failure
	 */
	public static BalanceInformation readBalanceInformation() throws IOException, JsonParseException{

            String fileDirectory= BALANCE_INFORMATION_DIRECTORY+BALANCE_INFORMATION_FILENAME;
		
            File file = new File(fileDirectory);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1){
                    strContent.append((char) ch);
            }
            fin.close();
            System.out.println(strContent);

            Gson gson= new Gson();
            return gson.fromJson(strContent.toString(), BalanceInformation.class);
        
	}
	

	/**
	 * Writes the balance information. Used to generate a default balance information file.
	 * 
	 * @param balanceInfo Player information
	 * 
	 * @return Player information
	 * 
	 * @throws IOException If an error occurred while writing.
	 */
	public static void writeBalanceInformation(BalanceInformation balanceInfo) throws IOException {

            File directory = new File(BALANCE_INFORMATION_DIRECTORY);
            File file = new File(BALANCE_INFORMATION_DIRECTORY+BALANCE_INFORMATION_FILENAME);

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
	
	
	
	public static void main(String[] args) {
		
		SagaPlayer defaultPlayer = new SagaPlayer();
		defaultPlayer.checkIntegrity(new Vector<String>());
		
		
		try {
//			writePlayerInformation("oiman", defaultPlayer);
			SagaPlayer oiMain= readPlayerInformation("oiman");
			Vector<String> intch= new Vector<String>();
			System.out.println(oiMain.checkIntegrity(intch));
			for (int i = 0; i < intch.size(); i++) {
				System.out.println(intch.get(i));
			}
			System.out.println("------------------");
			intch= new Vector<String>();
			BalanceInformation balancInfDef= new BalanceInformation();
			balancInfDef.checkIntegrity(intch);
			for (int i = 0; i < intch.size(); i++) {
				System.out.println(intch.get(i));
			}
			writeBalanceInformation(balancInfDef);
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
