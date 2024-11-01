package handlers;

import dataaccess.DataAccessException;
import handlers.responsebuilder.ResponseBuilder;
import requests.InvalidRequestException;
import requests.LoginRequest;
import responses.LoginResponse;
import serialize.ObjectSerializer;
import service.UserService;
import service.ValidationException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class LoginHandler implements Route {

    ObjectSerializer serializer = new ObjectSerializer();
    UserService service;

    public LoginHandler() {
        try {
            service = new UserService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        var responseBuilder = new ResponseBuilder(serializer, response);

        LoginRequest loginRequest = serializer.fromJson(request.body(), LoginRequest.class);

        try {
            loginRequest.validate();

            UUID authToken = service.loginUser(loginRequest);
            responseBuilder.prepareSuccessResponse(new LoginResponse(loginRequest.username(), authToken));
        } catch(InvalidRequestException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 400);
        } catch(ValidationException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(RuntimeException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
