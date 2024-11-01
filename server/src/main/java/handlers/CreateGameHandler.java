package handlers;

import dataaccess.DataAccessException;
import handlers.responsebuilder.ResponseBuilder;
import requests.CreateGameRequest;
import requests.InvalidRequestException;
import responses.CreateGameResponse;
import serialize.ObjectSerializer;
import service.AuthorizationService;
import service.GameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {

    private final ObjectSerializer serializer = new ObjectSerializer();
    private final GameService service;

    public CreateGameHandler() {
        try {
            service = new GameService();
        } catch (DataAccessException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            AuthorizationService.authorize(request.headers("Authorization"));

            CreateGameRequest createRequest = serializer.fromJson(request.body(), CreateGameRequest.class);
            createRequest.validate();

            int gameID = service.createGame(createRequest.gameName());

            responseBuilder.prepareSuccessResponse(new CreateGameResponse(gameID));

        } catch(UnauthorizedException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 401);
        } catch(InvalidRequestException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 400);
        } catch(RuntimeException | DataAccessException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
