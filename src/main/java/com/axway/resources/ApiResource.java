package com.axway.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.jackson.Jackson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;

/**
 * Resource class to provide API Doc.
 */
@Path("/api/v1")
public class ApiResource {

    /**
     * Jackson object mapper for YAML conversion.
     */
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper(new YAMLFactory());

    /**
     * The OAS documentation loaded as a JSON node.
     */
    private JsonNode documentation;

    /**
     * Constructs a new resource, loading the documentation at start.
     */
    public ApiResource() {
        try {
            this.documentation = MAPPER.readTree(getClass().getResourceAsStream("/petstore.yml"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid API documentation provided!");
        }
    }

    /**
     * Get the OAS doc in JSON format.
     *
     * @param uriInfo
     *      The request {@link UriInfo}.
     * @return
     *      The API doc as an object.
     */
    @GET
    @Produces("application/json")
    public JsonNode retrieveJson(@Context UriInfo uriInfo) {
        URI baseUri = uriInfo.getBaseUri();

        JsonNode cloned = this.documentation.deepCopy();
        ObjectNode server = (ObjectNode) cloned.path("servers").path(0);
        String currentServer = server.path("url").asText();

        URI runtimeUri = UriBuilder.fromUri(currentServer)
                .scheme(baseUri.getScheme())
                .host(baseUri.getHost())
                .port(baseUri.getPort())
                .build();

        server.put("url", runtimeUri.toString());

        return cloned;
    }
}
