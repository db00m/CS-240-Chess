package handlers;

import serialize.ObjectSerializer;
import spark.Response;

public class ResponseUtil {
    public static <T> void prepareResponse(T unserializedResponse, int status, ObjectSerializer serializer,  Response response) {
        String body = serializer.toJson(unserializedResponse);
        response.status(status);
        response.body(body);
    }
}
