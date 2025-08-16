package com.soloval.tech.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class JsonUtil {
    private JsonUtil() {
    }

    private static final Gson gson;

    static {
        class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
            @Override
            public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return ZonedDateTime.parse(jsonElement.getAsString());
            }
            @Override
            public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(zonedDateTime.toString());
            }
        }
        //CONFIG GSON
        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
    }

    public static String writeValueAsString(final Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception var3) {
            return "";
        }
    }

    public static <T> T readValue(final String jsonString, final Class<T> valueType) {
        try {
            return gson.fromJson(jsonString, valueType);
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T, K> K deepClone(final T source, final Class<K> targetType) {
        try {
            String text = gson.toJson(source);
            return gson.fromJson(text, targetType);
        } catch (Exception var3) {
            return null;
        }
    }
}