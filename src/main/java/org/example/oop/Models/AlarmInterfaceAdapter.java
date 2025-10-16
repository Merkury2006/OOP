package org.example.oop.Models;



import com.google.gson.*;

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
