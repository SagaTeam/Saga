package org.saga.factions;

import org.saga.Saga;
import org.saga.SagaPlayer;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;



public class SagaFactionCommands {

	// TODO Create faction config
	public static Integer maximumPrefixLength = 6;
	
	public static Integer minimumPrefixLength = 3;
	
	public static Integer maximumNameLength = 20;
	
	public static Integer minimumNameLenght = 4;
	
	
	
	@Command(
            aliases = {"factioncreate", "fcreate"},
            usage = "<faction name> <faction prefix>",
            flags = "",
            desc = "Create a new faction.",
            min = 2,
            max = 2
        )
	@CommandPermissions({"saga.faction.create"})
	 public static void create(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {
	    	
	    	
	    	// Check prefix:
	    	if(!validatePrefix(args.getString(1))){
	    		sagaPlayer.sendMessage(SagaFactionMessages.prefixInvalid());
	    		return;
	    	}
	    	
	    	// Check name:
	    	if(!validateName(args.getString(0))){
	    		sagaPlayer.sendMessage(SagaFactionMessages.nameInvalid());
	    		return;
	    	}
	    	
	    	// Check if already in faction:
	    	if(sagaPlayer.getFactionCount() > 0){
	    		sagaPlayer.sendMessage(SagaFactionMessages.factionCantBeCreatedAlreadyInOne());
	    		return;
	    	}
	    	
	    	
	    	
	    	// Add faction:
	    	SagaFaction faction = SagaFactionManager.getFactionManager().createFaction(args.getString(0), args.getString(1));
	    	faction.addMember(sagaPlayer);
	    	SagaFactionManager.getFactionManager().addFaction(faction);
	    	Saga.broadcast(SagaFactionMessages.factionCreated(sagaPlayer.getName(), args.getString(0)));
	    	
	    	
	    }

	
	
	
	
	 // Validate:
	 public static boolean validatePrefix(String str) {

         if(org.saga.utility.TextUtil.getComparisonString(str).length() < minimumPrefixLength ) {
             return false;
         }

         if(str.length() > maximumPrefixLength) {
             return false;
         }

         for (char c : str.toCharArray()) {
                 if ( ! org.saga.utility.TextUtil.substanceChars.contains(String.valueOf(c))) {
                     return false;
                 }
         }

         return true;

 }

	 public static boolean validateName(String str) {

         if(org.saga.utility.TextUtil.getComparisonString(str).length() < minimumNameLenght ) {
             System.out.println("<");
        	 return false;
         }

         if(str.length() > maximumNameLength) {
        	 System.out.println(">");
        	 return false;
         }

         for (char c : str.toCharArray()) {
                 if ( ! org.saga.utility.TextUtil.substanceChars.contains(String.valueOf(c))) {
                	 System.out.println("?");
                	 return false;
                 }
         }

         return true;

 }
	
	



}
