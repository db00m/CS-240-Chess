package handlers;

import responses.BasicResponse;
import serialize.ObjectSerializer;
import services.DBService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearDBHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        var serializer = new ObjectSerializer();
        try {
            new DBService().clearDB();
            ResponseUtil.prepareResponse(new BasicResponse(), 200, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new BasicResponse(exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
