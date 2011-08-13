/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.professions;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 *
 * @author Cory
 */
public class ProfessionDeserializer implements  JsonSerializer<Profession>, JsonDeserializer<Profession> {

       public Profession deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {

    	   
            //If this JsonElement is not an object we cannot create a profession
            if ( !je.isJsonObject() ) {
                throw new JsonParseException("ProfessionDeserializer JsonElement is not JsonObject!");
            }

            JsonObject jo = (JsonObject)je;
            JsonElement classElement = jo.get("_className");
            String className = classElement.getAsString();

            Profession profession = null;

            //Try to get class
            try {
                type = Class.forName(className);
            } catch ( ClassNotFoundException e ) {
                throw new JsonParseException("Class " + className + " not found!");
            }

            profession = jdc.deserialize(je, type);

            return profession;

        }

        public JsonElement serialize(Profession t, Type type, JsonSerializationContext jsc) {

            JsonObject jo = (JsonObject)jsc.serialize(t, t.getClass());

            jo.addProperty("_className", t.getClass().getName());
            
            return jo;

        }

}
