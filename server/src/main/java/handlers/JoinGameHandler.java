package handlers;

import dataaccess.DataAccessException;
import models.UserModel;
import requests.InvalidRequestException;
import requests.JoinGameRequest;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import services.AuthorizationService;
import services.GameService;
import services.UnauthorizedException;
import services.ValidationException;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer = new ObjectSerializer();

        try {
            UserModel user = AuthorizationService.authorize(request.headers("Authorization"));

            JoinGameRequest joinRequest = serializer.fromJson(request.body(), JoinGameRequest.class);
            joinRequest.validate();

            var service = new GameService();
            service.joinGame(joinRequest.gameID(), user.username(), joinRequest.playerColor());
            ResponseUtil.prepareResponse(new BasicResponse(), 200, serializer, response);
        } catch(UnauthorizedException exc) {
            ResponseUtil.prepareResponse(new BasicResponse("Error: " + exc.getMessage()), 401, serializer, response);
        } catch(InvalidRequestException exc) {
            ResponseUtil.prepareResponse(new BasicResponse("Error: " + exc.getMessage()), 400, serializer, response);
        } catch(DataAccessException | ValidationException exc) {
            ResponseUtil.prepareResponse(new BasicResponse("Error: " + exc.getMessage()), 403, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new BasicResponse("Error: " + exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
