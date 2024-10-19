package handlers;

import requests.CreateGameRequest;
import requests.InvalidRequestException;
import responses.CreateGameResponse;
import serialize.ObjectSerializer;
import services.AuthorizationService;
import services.GameService;
import services.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer = new ObjectSerializer();

        try {
            AuthorizationService.authorize(request.headers("Authorization"));

            CreateGameRequest createRequest = serializer.fromJson(request.body(), CreateGameRequest.class);
            createRequest.validate();

            var service = new GameService();
            int gameID = service.createGame(createRequest.gameName());

            ResponseUtil.prepareResponse(new CreateGameResponse(gameID), 200, serializer, response);

        } catch(UnauthorizedException exc) {
            ResponseUtil.prepareResponse(new CreateGameResponse("Error: " + exc.getMessage()), 401, serializer, response);
        } catch(InvalidRequestException exc) {
            ResponseUtil.prepareResponse(new CreateGameResponse("Error: " + exc.getMessage()), 400, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new CreateGameResponse("Error: " + exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
