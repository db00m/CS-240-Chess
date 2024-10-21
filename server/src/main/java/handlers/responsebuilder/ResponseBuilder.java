package handlers.responsebuilder;

import responses.BasicResponse;
import serialize.ObjectSerializer;
import spark.Response;

public class ResponseBuilder {

    private final ObjectSerializer serializer;
    private final Response response;

    public ResponseBuilder(ObjectSerializer serializer, Response response) {
        this.serializer = serializer;
        this.response = response;
    }

    public <T> void prepareSuccessResponse(T unserializedResponse) {
        String body = serializer.toJson(unserializedResponse);
        response.status(200);
        response.body(body);
    }

    public void prepareErrorResponse(String errorMessage, int status) {
        String body  = serializer.toJson(new BasicResponse("Error: " + errorMessage));
        response.status(status);
        response.body(body);
    }
}
