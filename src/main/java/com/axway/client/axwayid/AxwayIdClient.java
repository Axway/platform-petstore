package com.axway.client.axwayid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import io.dropwizard.jackson.Jackson;

import javax.annotation.Nonnull;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

/**
 * Basic Axway ID client class used to to fetch tokens.
 */
public class AxwayIdClient {

    /**
     * Basic JSON mapper used to convert responses into domain objects.
     */
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    /**
     * The internal Axway ID configuration provided at startup.
     */
    private final AxwayIdConfiguration configuration;

    /**
     * The internal client instance used to make HTTP requests.
     */
    private final OkHttpClient client;

    /**
     * Constructs a new client instance from a configuration.
     *
     * @param configuration
     *      the configuration provided at application startup.
     */
    public AxwayIdClient(@Nonnull AxwayIdConfiguration configuration) {
        this.configuration = Objects.requireNonNull(configuration);
        this.client = new OkHttpClient();
    }

    /**
     * Retrieves an access token instance from Axway ID.
     *
     * @return a {@link String} token value after validation.
     */
    public String getToken() throws IOException {
        URI uri = UriBuilder.fromUri(this.configuration.getUri())
                .path("auth/realms")
                .path(this.configuration.getRealm())
                .path("protocol/openid-connect/token")
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add("client_id", this.configuration.getClientId())
                .add("client_secret", this.configuration.getClientSecret())
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(uri.toURL())
                .post(requestBody)
                .build();

        Response response = this.client.newCall(request).execute();

        try (ResponseBody body = response.body()) {
            try (InputStream is = body.byteStream()) {
                return MAPPER.readTree(is).path("access_token").asText();
            }
        }
    }
}
