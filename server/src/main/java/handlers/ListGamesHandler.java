package handlers;

import models.ChessGameModel;
import responses.GameListResponse;
import serialize.ObjectSerializer;
import services.AuthorizationService;
import services.GameService;
import services.ResponseBuilder;
import services.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;

public class ListGamesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer  = new ObjectSerializer();
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            AuthorizationService.authorize(request.headers("Authorization"));

            var service = new GameService();

            Collection<ChessGameModel> games = service.listGames();
            responseBuilder.prepareSuccessResponse(new GameListResponse(games));
        } catch(UnauthorizedException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(RuntimeException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }
        return response.body();
    }
}
