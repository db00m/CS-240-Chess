package handlers;

import dataaccess.DataAccessException;
import handlers.responsebuilder.ResponseBuilder;
import models.ChessGameModel;
import responses.GameListResponse;
import serialize.ObjectSerializer;
import service.AuthorizationService;
import service.GameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;

public class ListGamesHandler implements Route {
    ObjectSerializer serializer = new ObjectSerializer();
    GameService service;

    public ListGamesHandler() {
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
            AuthorizationService.authorize(request.headers("Authorization"));

            Collection<ChessGameModel> games = service.listGames();
            responseBuilder.prepareSuccessResponse(new GameListResponse(games));
        } catch(UnauthorizedException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(RuntimeException | DataAccessException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }
        return response.body();
    }
}
