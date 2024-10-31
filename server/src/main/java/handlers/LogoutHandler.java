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
    @Override
    public Object handle(Request request, Response response) {
        var serializer  = new ObjectSerializer();
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            var userService = new UserService();
            String authTokenString = request.headers("Authorization");
            AuthorizationService.authorize(authTokenString);

            userService.logoutUser(UUID.fromString(authTokenString));

            responseBuilder.prepareSuccessResponse(new BasicResponse());
        } catch(UnauthorizedException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(RuntimeException | DataAccessException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
