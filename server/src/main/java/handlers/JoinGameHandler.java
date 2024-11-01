package handlers;

import dataaccess.DataAccessException;
import handlers.responsebuilder.ResponseBuilder;
import models.UserModel;
import requests.InvalidRequestException;
import requests.JoinGameRequest;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import service.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    ObjectSerializer serializer = new ObjectSerializer();
    GameService service;

    public JoinGameHandler() {
        try {
            service = new GameService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            UserModel user = AuthorizationService.authorize(request.headers("Authorization"));

            JoinGameRequest joinRequest = serializer.fromJson(request.body(), JoinGameRequest.class);
            joinRequest.validate();

            service.joinGame(joinRequest.gameID(), user.username(), joinRequest.playerColor());
            responseBuilder.prepareSuccessResponse(new BasicResponse());
        } catch(InvalidRequestException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 400);
        } catch(UnauthorizedException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(DataAccessException | ValidationException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 403);
        } catch(RuntimeException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
