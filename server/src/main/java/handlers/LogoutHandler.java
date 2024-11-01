package handlers;

import dataaccess.DataAccessException;
import handlers.responsebuilder.ResponseBuilder;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import service.AuthorizationService;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class LogoutHandler implements Route {
    ObjectSerializer serializer = new ObjectSerializer();
    UserService service;

    public LogoutHandler() {
        try {
            service = new UserService();
        } catch(DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            String authTokenString = request.headers("Authorization");
            AuthorizationService.authorize(authTokenString);

            service.logoutUser(UUID.fromString(authTokenString));

            responseBuilder.prepareSuccessResponse(new BasicResponse());
        } catch(UnauthorizedException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(RuntimeException | DataAccessException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
