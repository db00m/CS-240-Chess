package handlers;

import requests.InvalidRequestException;
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
        var serializer = new ObjectSerializer();

        LoginRequest loginRequest = serializer.fromJson(request.body(), LoginRequest.class);

        try {
            loginRequest.validate();
            
            var userService = new UserService();

            UUID authToken = userService.loginUser(loginRequest);
            ResponseUtil.prepareResponse(new LoginResponse(loginRequest.username(), authToken), 200, serializer, response);
        } catch(ValidationException exc) {
            ResponseUtil.prepareResponse(new LoginResponse("Error: " + exc.getMessage()), 401, serializer, response);
        } catch(InvalidRequestException exc) {
            ResponseUtil.prepareResponse(new LoginResponse("Error: " + exc.getMessage()), 400, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new LoginResponse(exc.getMessage()), 500, serializer, response);

        }

        return response.body();
    }
}
