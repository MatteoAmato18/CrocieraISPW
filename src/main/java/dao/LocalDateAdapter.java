package dao;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;


/* ---------- LocalDate ---------- */
public final class LocalDateAdapter
        implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override                                   // yyyy-MM-dd
    public JsonElement serialize(LocalDate src,
                                  Type t,
                                  JsonSerializationContext c) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement je,
                                 Type t,
                                 JsonDeserializationContext c) {
        return LocalDate.parse(je.getAsString());
    }
}


