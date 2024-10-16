package handlers;

import responses.BasicResponse;
import serialize.ObjectSerializer;
import services.UnauthorizedException;
import services.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var serializer  = new ObjectSerializer();

        try {
            var userService = new UserService();

            UUID authToken = UUID.fromString(request.headers("Authorization"));
            userService.logoutUser(authToken);

            ResponseUtil.prepareResponse(new BasicResponse(), 200, serializer, response);
        } catch(UnauthorizedException exc) {
            ResponseUtil.prepareResponse(new BasicResponse(exc.getMessage()), 401, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new BasicResponse(exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
