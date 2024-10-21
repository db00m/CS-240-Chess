package handlers;

import handlers.responsebuilder.ResponseBuilder;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import service.DBService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearDBHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer = new ObjectSerializer();
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            new DBService().clearDB();
            responseBuilder.prepareSuccessResponse(new BasicResponse());
        } catch(RuntimeException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
