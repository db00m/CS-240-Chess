import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerFacade {
    URL url;

    public ServerFacade(String urlString) throws IOException {
        url = new URL(urlString);
    }

    public void login(String username, String password) {

    }
}
