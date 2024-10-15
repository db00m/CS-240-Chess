package serialize;

import com.google.gson.Gson;

public class ObjectSerializer<T> {
    private final Gson serializer = new Gson();

    public String toJson(T object) {
        return serializer.toJson(object);
    }

    public T fromJson(String jsonString, Class<T> object) {
        return serializer.fromJson(jsonString, object);
    }
}
