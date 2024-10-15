package serialize;

import com.google.gson.Gson;

public class ObjectSerializer {
    private final Gson serializer = new Gson();

    public <T> String toJson(T object) {
        return serializer.toJson(object);
    }

    public <T> T fromJson(String jsonString, Class<T> object) {
        return serializer.fromJson(jsonString, object);
    }
}
