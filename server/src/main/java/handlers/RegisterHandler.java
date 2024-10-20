package handlers;

import requests.InvalidRequestException;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import serialize.ObjectSerializer;
import services.ResponseBuilder;
import services.UserService;
import services.ValidationException;
import spark.*;

import java.util.UUID;

public class RegisterHandler implements Route {


    @Override
    public Object handle(Request request, Response response) {
        var serializer = new ObjectSerializer();
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);
            registerRequest.validate();

            var service = new UserService();
            service.registerUser(registerRequest);

            UUID authToken = service.loginUser(new LoginRequest(registerRequest.username(), registerRequest.password()));

            responseBuilder.prepareSuccessResponse(new LoginResponse(registerRequest.username(), authToken));
        } catch(InvalidRequestException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 400);
        } catch(ValidationException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 403);
        } catch(RuntimeException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
