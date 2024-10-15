package dataaccess;

import models.UserModel;
import org.eclipse.jetty.server.Authentication;

import java.util.UUID;

public interface AuthTokenDAO {
    void addAuth(UUID token, UserModel user);
    UserModel getUserByToken(UUID authToken);
    void clearTokens();
}
