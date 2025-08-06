package dao;

import com.google.gson.*;
import java.lang.reflect.Type;

import java.time.LocalTime;




/* ---------- LocalTime ---------- */
public final class LocalTimeAdapter
        implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    @Override                                   // HH:mm:ss
    public JsonElement serialize(LocalTime src,
                                  Type t,
                                  JsonSerializationContext c) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public LocalTime deserialize(JsonElement je,
                                 Type t,
                                 JsonDeserializationContext c) {
        return LocalTime.parse(je.getAsString());
    }
}