package org.example.oop.Models;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AlarmInterfaceAdapterTest {

    private AlarmInterfaceAdapter adapter;
    private Gson gson;
    private JsonSerializationContext serializationContext;
    private JsonDeserializationContext deserializationContext;

    @BeforeEach
    void setUp() {
        adapter = new AlarmInterfaceAdapter();

        gson = new GsonBuilder()
                .registerTypeAdapter(AlarmInterface.class, adapter)
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        serializationContext = mock(JsonSerializationContext.class);
        deserializationContext = mock(JsonDeserializationContext.class);
    }

    private static class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalTime.parse(json.getAsString(), formatter);
        }

        @Override
        public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }
    }

    @Test
    @DisplayName("Should serialize RegularAlarm with class type")
    void shouldSerializeRegularAlarmWithClassType() {
        AlarmInterface alarm = new RegularAlarm(1L, LocalTime.of(8, 0), true, "melody.mp3", "Morning Alarm");

        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("id", 1L);
        expectedJson.addProperty("time", "08:00");
        expectedJson.addProperty("active", true);
        expectedJson.addProperty("melody", "melody.mp3");
        expectedJson.addProperty("name", "Morning Alarm");
        expectedJson.addProperty("classType", RegularAlarm.class.getName());

        when(serializationContext.serialize(alarm)).thenReturn(expectedJson);

        JsonElement result = adapter.serialize(alarm, AlarmInterface.class, serializationContext);

        assertTrue(result.isJsonObject());
        JsonObject resultObj = result.getAsJsonObject();
        assertEquals(RegularAlarm.class.getName(), resultObj.get("classType").getAsString());
    }

    @Test
    @DisplayName("Should deserialize RegularAlarm correctly")
    void shouldDeserializeRegularAlarmCorrectly() {
        String jsonString = """
            {
                "classType": "org.example.oop.Models.RegularAlarm",
                "id": 1,
                "time": "08:00",
                "active": true,
                "melody": "melody.mp3",
                "name": "Morning Alarm"
            }
            """;

        JsonElement jsonElement = JsonParser.parseString(jsonString);
        RegularAlarm expectedAlarm = new RegularAlarm(1L, LocalTime.of(8, 0), true, "melody.mp3", "Morning Alarm");

        when(deserializationContext.deserialize(any(JsonElement.class), eq(RegularAlarm.class)))
                .thenReturn(expectedAlarm);

        AlarmInterface result = adapter.deserialize(jsonElement, AlarmInterface.class, deserializationContext);

        assertNotNull(result);
        assertTrue(result instanceof RegularAlarm);
        verify(deserializationContext).deserialize(any(JsonElement.class), eq(RegularAlarm.class));
    }

    @Test
    @DisplayName("Should throw JsonParseException for unknown class")
    void shouldThrowJsonParseExceptionForUnknownClass() {
        String jsonString = """
            {
                "classType": "org.example.oop.Models.UnknownAlarm",
                "id": 1,
                "time": "08:00"
            }
            """;

        JsonElement jsonElement = JsonParser.parseString(jsonString);

        JsonParseException exception = assertThrows(JsonParseException.class,
                () -> adapter.deserialize(jsonElement, AlarmInterface.class, deserializationContext));

        assertTrue(exception.getMessage().contains("Неизвестный класс"));
    }

    @Test
    @DisplayName("Should handle missing classType field with NullPointerException")
    void shouldHandleMissingClassTypeFieldWithNullPointerException() {
        String jsonString = """
            {
                "id": 1,
                "time": "08:00"
            }
            """;

        JsonElement jsonElement = JsonParser.parseString(jsonString);

        assertThrows(NullPointerException.class,
                () -> adapter.deserialize(jsonElement, AlarmInterface.class, deserializationContext));
    }


    @Test
    @DisplayName("Should handle empty JSON object")
    void shouldHandleEmptyJsonObject() {
        String jsonString = "{}";
        JsonElement jsonElement = JsonParser.parseString(jsonString);

        assertThrows(NullPointerException.class,
                () -> adapter.deserialize(jsonElement, AlarmInterface.class, deserializationContext));
    }
}