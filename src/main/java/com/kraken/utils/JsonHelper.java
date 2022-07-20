package com.kraken.utils;

import javax.json.*;
import java.io.StringReader;

public class JsonHelper {

    private static final String EVENT_FIELD = "event";

    public static JsonObject getJsonObjectFromString(String message) {
        return Json.createReader(new StringReader(message)).readObject();
    }

    public static JsonArray getJsonArrayFromString(String message) {
        return Json.createReader(new StringReader(message)).readArray();
    }


    public static String getEventValueFromJsonObject(JsonObject message) {
        return message.getString(EVENT_FIELD);
    }

    public static String getEventValueFromJsonArray(JsonArray message) {
        String event = message.getString(2);

        if (event == null) {
            throw new IllegalStateException("Field event is null in incoming message. Complete message = " + message);
        }

        if (event.startsWith("ohlc")) {
            return "ohlc";
        }

        if (event.startsWith("book")) {
            return "book";
        }

        return event;
    }

    public static String getEventFromMessage(String message) {
        JsonStructure jsonStructure = Json.createReader(new StringReader(message)).read();
        JsonValue.ValueType type = jsonStructure.getValueType();

        if (type == JsonValue.ValueType.ARRAY) {
            return getEventValueFromJsonArray(jsonStructure.asJsonArray());
        }

        if (type == JsonValue.ValueType.OBJECT) {
            return getEventValueFromJsonObject(jsonStructure.asJsonObject());
        }
        throw new IllegalStateException("Message Type not handled:" + message);
    }
}
