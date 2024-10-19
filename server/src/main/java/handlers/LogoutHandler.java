package handlers;

import models.UserModel;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import services.AuthorizationService;
import services.UnauthorizedException;
import services.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer  = new ObjectSerializer();

        try {
            var userService = new UserService();
            String authTokenString = request.headers("Authorization");
            AuthorizationService.authorize(authTokenString);

            userService.logoutUser(UUID.fromString(authTokenString));

            ResponseUtil.prepareResponse(new BasicResponse(), 200, serializer, response);
        } catch(UnauthorizedException exc) {
            ResponseUtil.prepareResponse(new BasicResponse("Error: " + exc.getMessage()), 401, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new BasicResponse("Error: " + exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
