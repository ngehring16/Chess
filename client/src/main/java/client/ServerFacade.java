package client;

import com.google.gson.Gson;
import model.chessrecords.*;
import exception.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public RegisterResult register(UserData user){
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public RegisterResult login(LoginRequest login){
        var request = buildRequest("POST", "/session", login);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public void logout(String authToken){
        var path = String.format("/session/%s", authToken);
        var request = buildRequest("DELETE", path, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void list(String authToken){
        var path = String.format("/game/%s", authToken);
        var request = buildRequest("GET", path, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public CreateResult create(CreateRequest create, String authToken){
        var path = String.format("/game/%s", authToken);
        var request = buildRequest("POST", path, create);
        var response = sendRequest(request);
        return handleResponse(response, CreateResult.class);
    }

    public void join(JoinRequest join, String authToken){
        var path = String.format("/game/%s", authToken);
        var request = buildRequest("PUT", path, join);
        var response = sendRequest(request);
        handleResponse(response, null);

    }

    public void clear(){
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException("Failed to send request");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new ResponseException(body);
            }

            throw new ResponseException("other failure");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
