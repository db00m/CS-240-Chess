package serverconnection;

import responses.BasicResponse;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ConnectionManager {
    String root;


    public ConnectionManager(String rootURLString) {
        this.root = rootURLString;
    }



    public String doGet(String path, Map<String, String> headers) throws IOException, HTTPResponseException {
        return doRequestWithoutBody(path, headers, "GET");
    }

    public String doPost(String path, String postBody, Map<String, String> headers) throws IOException, HTTPResponseException {
        return doRequestWithBody(path, postBody, headers, "POST");
    }

    public String doPut(String path, String putBody, Map<String, String> headers) throws IOException, HTTPResponseException {
        return doRequestWithBody(path, putBody, headers, "PUT");
    }

    public void doDelete(String path, Map<String, String> headers) throws IOException, HTTPResponseException {
        doRequestWithoutBody(path, headers, "DELETE");
    }



    private String doRequestWithoutBody(String path, Map<String, String> headers, String method) throws IOException, HTTPResponseException {
        HttpURLConnection connection = getConnection(path);

        configureConnection(connection, headers);
        connection.setRequestMethod(method);

        connection.connect();
        checkResponseStatus(connection);

        return getResponseBody(connection.getInputStream());
    }

    private String doRequestWithBody(String path, String postBody, Map<String, String> headers, String method)
            throws IOException, HTTPResponseException {
        HttpURLConnection connection = getConnection(path);

        configureConnection(connection, headers);
        connection.setRequestMethod(method);
        setRequestBody(postBody, connection);

        connection.connect();
        checkResponseStatus(connection);

        return getResponseBody(connection.getInputStream());
    }




    private HttpURLConnection getConnection(String path) throws IOException {
        URL requestURL = URI.create(root + path).toURL();

        return (HttpURLConnection) requestURL.openConnection();
    }

    private void configureConnection(HttpURLConnection connection, Map<String, String> headers) {
        connection.setConnectTimeout(5000);
        connection.setDoInput(true);
        setHeaders(headers, connection);
    }

    private void setHeaders(Map<String, String> headers, HttpURLConnection connection) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    private void setRequestBody(String body, HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);

        try(var bodyWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            bodyWriter.write(body);
            bodyWriter.flush();
        }
    }

    private String getResponseBody(InputStream bodyStream) throws IOException {
        try(var reader = new BufferedReader(new InputStreamReader(bodyStream))) {
            var response = new StringBuilder();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                response.append(line);
            }

            return response.toString();
        }

    }

    private void checkResponseStatus(HttpURLConnection connection) throws HTTPResponseException, IOException {
        int httpStatus = connection.getResponseCode();
        if (httpStatus > 300) {
            throw new HTTPResponseException(getResponseBody(connection.getErrorStream()));
        }
    }
}
