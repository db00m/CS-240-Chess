package handlers;

import requests.InvalidRequestException;
import requests.RegisterRequest;
import responses.BasicResponse;
import serialize.ObjectSerializer;
import services.UserService;
import services.ValidationException;
import spark.*;

public class RegisterHandler implements Route {


    @Override
    public Object handle(Request request, Response response) {
        ObjectSerializer serializer = new ObjectSerializer();

        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);

        try {
            var service = new UserService();

            registerRequest.validate();
            service.registerUser(registerRequest);

            ResponseUtil.prepareResponse(new BasicResponse(), 200, serializer, response);
        } catch(ValidationException exc) {
            ResponseUtil.prepareResponse(new BasicResponse(exc.getMessage()), 401, serializer, response);
        } catch(InvalidRequestException exc) {
            ResponseUtil.prepareResponse(new BasicResponse(exc.getMessage()), 400, serializer, response);
        } catch(RuntimeException exc) {
            ResponseUtil.prepareResponse(new BasicResponse(exc.getMessage()), 500, serializer, response);
        }

        return response.body();
    }
}
