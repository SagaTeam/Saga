/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.chunkGroups;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 *
 * @author Cory
 */
public class ChunkGroupDeserializer implements  JsonSerializer<ChunkGroup>, JsonDeserializer<ChunkGroup> {

       public ChunkGroup deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {

    	   
            //If this JsonElement is not an object we cannot create a profession
            if ( !je.isJsonObject() ) {
                throw new JsonParseException("JsonElement is not JsonObject!");
            }

            JsonObject jo = (JsonObject)je;
            JsonElement classElement = jo.get("_className");
            String className = classElement.getAsString();
            
            // Ignore if _className doesen't exist.
            if(className == null){
            	type = ChunkGroup.class;
            	return jdc.deserialize(je, type);
            }
            

            //Try to get class
            try {
                type = Class.forName(className);
            } catch ( ClassNotFoundException e ) {
                throw new JsonParseException("Class " + className + " not found!");
            }

            return jdc.deserialize(je, type);

        }

        public JsonElement serialize(ChunkGroup t, Type type, JsonSerializationContext jsc) {

            JsonObject jo = (JsonObject)jsc.serialize(t, t.getClass());

            jo.addProperty("_className", t.getClass().getName());
            
            return jo;

        }

}
