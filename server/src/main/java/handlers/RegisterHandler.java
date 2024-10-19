package handlers;

import requests.InvalidRequestException;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.BasicResponse;
import responses.LoginResponse;
import serialize.ObjectSerializer;
import services.UserService;
import services.ValidationException;
import spark.*;

import java.util.UUID;

public class RegisterHandler implements Route {


    @Override
    public Object handle(Request request, Response response) {
        ObjectSerializer serializer = new ObjectSerializer();

        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);

        try {
            var service = new UserService();

            registerRequest.validate();
            service.registerUser(registerRequest);

            UUID authToken = service.loginUser(new LoginRequest(registerRequest.username(), registerRequest.password()));

            ResponseUtil.prepareResponse(new LoginResponse(registerRequest.username(), authToken), 200, serializer, response);
        } catch(ValidationException exc) {
            ResponseUtil.prepareResponse(new LoginResponse("Error: " + exc.getMessage()), 403, serializer, response);
        } catch(InvalidRequestException exc) {
            ResponseUtil.prepareResponse(new LoginResponse("Error: " + exc.getMessage()), 400, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new LoginResponse("Error: " + exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
