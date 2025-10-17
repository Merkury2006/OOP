package org.example.oop.Models;





import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AlarmInterfaceAdapter implements JsonSerializer<AlarmInterface>, JsonDeserializer<AlarmInterface> {
    private static final String CLASS_TYPE = "classType";

    @Override
    public AlarmInterface deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String className = jsonObject.get(CLASS_TYPE).getAsString();
        try {
            Class<?> clazz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonObject, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Неизвестный класс: " + className + e);
        }
    }

    @Override
    public JsonElement serialize(AlarmInterface alarmInterface, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = jsonSerializationContext.serialize(alarmInterface).getAsJsonObject();
        jsonObject.addProperty(CLASS_TYPE, alarmInterface.getClass().getName());
        return jsonObject;
    }
}
