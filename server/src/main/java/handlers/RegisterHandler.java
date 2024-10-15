package handlers;

import com.google.gson.Gson;
import requests.RegisterRequest;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import services.UserService;
import services.ValidationException;
import spark.*;

public class RegisterHandler implements Route {

    @Override
    public Object handle(Request request, Response response) {
        UserService service = new UserService();
        ObjectSerializer<RegisterRequest> requestDeserializer = new ObjectSerializer<>();
        ObjectSerializer<BasicResponse> responseSerializer = new ObjectSerializer<>();

        RegisterRequest registerRequest = requestDeserializer.fromJson(request.body(), RegisterRequest.class);

        try {
            service.registerUser(registerRequest);
            String body = responseSerializer.toJson(new BasicResponse());
            response.status(200);
            response.body(body);
            return body;
        } catch(ValidationException exc) {
            String body = responseSerializer.toJson(new BasicResponse(exc.getMessage()));
            response.status(403);
            response.body(body);
            return body;
        }
    }
}
