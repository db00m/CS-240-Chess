package handlers;

import models.ChessGameModel;
import responses.GameListResponse;
import serialize.ObjectSerializer;
import services.AuthorizationService;
import services.GameService;
import services.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.UUID;

public class ListGamesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer  = new ObjectSerializer();

        try {
            AuthorizationService.authorize(request.headers("Authorization"));

            var service = new GameService();

            Collection<ChessGameModel> games = service.listGames();
            ResponseUtil.prepareResponse(new GameListResponse(games), 200, serializer, response);
        } catch(UnauthorizedException exc) {
            ResponseUtil.prepareResponse(new GameListResponse(exc.getMessage()), 401, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new GameListResponse(exc.getMessage()), 500, serializer, response);
        }
        return response.body();
    }
}
