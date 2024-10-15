package handlers;

import requests.LoginRequest;
import responses.LoginResponse;
import serialize.ObjectSerializer;
import services.UserService;
import services.ValidationException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class LoginHandler implements Route {

    @Override
    public Object handle(Request request, Response response) {
        var requestDeserializer = new ObjectSerializer();
        var responseSerializer = new ObjectSerializer();

        LoginRequest loginRequest = requestDeserializer.fromJson(request.body(), LoginRequest.class);

        var userService = new UserService();

        try {
            UUID authToken = userService.loginUser(loginRequest);
            String responseBody = responseSerializer.toJson(new LoginResponse(loginRequest.username(), authToken));
            response.body(responseBody);
            return responseBody;

        } catch(ValidationException exc) {
            String responseBody = responseSerializer.toJson(new LoginResponse("Error: " + exc.getMessage()));
            response.body(responseBody);
            response.status(401);
            return responseBody;
        }
    }
}
