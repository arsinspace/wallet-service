package ru.ylab.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Singleton class for convert JSON to object and object to JSON
 */
public class JsonConverter {

    private static JsonConverter instance;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private JsonConverter(){

    }
    public static JsonConverter getInstance(){
        if (instance == null)
            instance = new JsonConverter();
        return instance;
    }

    /**
     *  Method convert JSON to object
     * @param clazz class to be converted to
     * @param jsonString JSON String
     * @return Converted class
     * @param <T> Class
     */
    public static <T> T convertToObject(Class<T> clazz,String jsonString) {
        try {
            return (T)mapper.readValue(jsonString, clazz);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Method convert object to JSON
     * @param object Object
     * @return JSON String
     */
    public static String convertToJSON(Object object) {
        String json;
        try {
            json = writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return convertToJSON(e);
        }
        return json;
    }
}
