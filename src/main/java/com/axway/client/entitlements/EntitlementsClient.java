package com.axway.client.entitlements;

import com.axway.client.axwayid.AxwayIdClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import io.dropwizard.jackson.Jackson;

import javax.annotation.Nonnull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Basic client instance used for interactions with the Entitlements Service.
 */
public class EntitlementsClient {

    /**
     * Basic JSON mapper used to convert responses into domain objects.
     */
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    /**
     * The client instance used to talk to Axway ID for authentication.
     */
    private final AxwayIdClient axwayId;

    /**
     * The internal ES configuration provided at startup.
     */
    private final EntitlementsConfiguration configuration;

    /**
     * The internal client instance used for HTTP requests.
     */
    private final OkHttpClient client;

    /**
     * Constructs a new entitlements client.
     *
     * @param axwayId
     *      an Axway ID client instance for token retrieval.
     * @param configuration
     *      the entitlements configuration provided at application startup.
     */
    public EntitlementsClient(@Nonnull AxwayIdClient axwayId, @Nonnull EntitlementsConfiguration configuration) {
        this.axwayId = Objects.requireNonNull(axwayId);
        this.configuration = Objects.requireNonNull(configuration);
        this.client = new OkHttpClient();
    }

    /**
     * Retrieves the entitlements mapping for the configured organization.
     *
     * @return a {@link Map} containing entitlements mapped to their limits.
     */
    public Map<String, Long> getEntitlements() throws IOException {
        String token = this.axwayId.getToken();

        URI uri = UriBuilder.fromUri(this.configuration.getUri())
                .path(this.configuration.getOrg())
                .queryParam("embed", "subscriptions.[entitlements, offerings.entitlements]")
                .build();

        Request request = new Request.Builder()
                .url(uri.toURL())
                .get()
                .header("Accept", MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .build();

        Response response = this.client.newCall(request).execute();

        try (ResponseBody body = response.body()) {
            try (InputStream is = body.byteStream()) {
                Map<String, Long> entitlements = new HashMap<>();

                for (JsonNode subscription : MAPPER.readTree(is).path("subscriptions")) {
                    for (JsonNode offering : subscription.path("offerings")) {
                        for (JsonNode entitlement : offering.path("entitlements")) {
                            entitlements.put(
                                entitlement.path("name").asText().toLowerCase(),
                                entitlement.path("value").asLong()
                            );
                        }
                    }
                }

                return entitlements;
            }
        }
    }
}
