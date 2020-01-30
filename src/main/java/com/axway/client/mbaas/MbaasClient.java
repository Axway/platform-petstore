package com.axway.client.mbaas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Extremely basic client for working with MBaaS APIs.
 */
public class MbaasClient {

    /**
     * Singleton JSON mapper to use when translating JSON between containers.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * An internal HTTP client to use for all requests.
     */
    private final OkHttpClient client;

    /**
     * The internal session identifier used to sign requests.
     */
    private final String sessionId;

    /**
     * The hostname of the remote service to connect to.
     */
    private final String hostname;

    /**
     * The application key to work with on the backend.
     */
    private final String key;

    /**
     * Initializes a new MBaaS client using a configuration.
     *
     * @param configuration
     *      the MBaaS configuration provided at application startup.
     * @throws IOException
     *      on any issues connecting to the remote endpoints.
     */
    public MbaasClient(@Nonnull MBaasConfiguration configuration) throws IOException {
        this.client = new OkHttpClient();
        this.key = Objects.requireNonNull(configuration.getKey());
        this.hostname = Objects.requireNonNull(configuration.getHostname());

        RequestBody requestBody = new FormEncodingBuilder()
                .add("login", configuration.getUsername())
                .add("password", configuration.getPassword())
                .build();

        Request request = new Request.Builder()
                .url("https://" + this.hostname + "/v1/users/login.json?key=" + this.key)
                .post(requestBody)
                .build();

        Response response = this.client.newCall(request).execute();

        if (response.code() != 200) {
            throw new IOException("Unable to connect to MBaaS");
        }

        try (ResponseBody body = response.body()) {
            try (InputStream is = body.byteStream()) {
                this.sessionId = MAPPER.readTree(is).path("meta").path("session_id").asText();
            }
        }
    }

    /**
     * Calls a method/path pair and retrieves a response.
     *
     * @param method
     *      the HTTP method to use to call the MBaaS API.
     * @param path
     *      the API path to call on the MBaaS API.
     * @return
     *      a {@link Response} retrieved from the remote server.
     * @throws IOException
     *      on any issues connecting to the remote endpoints.
     */
    public Response call(String method, String path) throws IOException {
        return this.call(method, path, null);
    }

    /**
     * Calls a method/path pair with a body and retrieves a response.
     *
     * @param method
     *      the HTTP method to use to call the MBaaS API.
     * @param path
     *      the API path to call on the MBaaS API.
     * @param body
     *      the request body to send to the server.
     * @return
     *      a {@link Response} retrieved from the remote server.
     * @throws IOException
     *      on any issues connecting to the remote endpoints.
     */
    public Response call(String method, String path, RequestBody body) throws IOException {
        String url = "https://" + this.hostname + "/v1" + path + (path.contains("?") ? "" : "?") + "&ct=enterprise&key=" + this.key;

        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("Cookie", "_session_id=" + this.sessionId)
                .header("User-Agent", "Sample Java MBaaS Client");

        switch (method.toUpperCase()) {
            case "GET":
                builder = builder.get();
                break;
            case "POST":
                builder = builder.post(body);
                break;
            case "PUT":
                builder = builder.put(body);
                break;
            case "DELETE":
                if (body == null) {
                    builder = builder.delete();
                } else {
                    builder = builder.delete(body);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported request method");
        }

        return this.client.newCall(builder.build()).execute();
    }
}
