package handlers;

import dataaccess.DataAccessException;
import handlers.responsebuilder.ResponseBuilder;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import service.DBService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearDBHandler implements Route {

    ObjectSerializer serializer = new ObjectSerializer();
    DBService service = new DBService();

    @Override
    public Object handle(Request request, Response response) {
        var responseBuilder = new ResponseBuilder(serializer, response);

        try {
            service.clearDB();
            responseBuilder.prepareSuccessResponse(new BasicResponse());
        } catch(RuntimeException | DataAccessException exc) {
            responseBuilder.prepareErrorResponse(exc.getMessage(), 500);
        }

        return response.body();
    }
}
