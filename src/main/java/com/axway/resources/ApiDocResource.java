package com.axway.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resource class to provide API Doc.
 */
@Path("/apidoc")
public class ApiDocResource {

    /**
     * Jackson object mapper for YAML conversion.
     */
    private ObjectMapper mapper;

    /**
     * Constructor.
     */
    public ApiDocResource() {
         mapper = new ObjectMapper(new YAMLFactory());
    }

    /**
     * Get the OAS doc in JSON format.
     *
     * @param uriInfo
     *      The request {@link UriInfo}.
     * @return
     *      The API doc as an object.
     * @throws IOException
     *      If there are problems generating the doc.
     */
    @GET
    @Path("/json")
    @Produces("application/json")
    public Map<String, Object> jsonApiDoc(@Context UriInfo uriInfo) throws IOException {
        return getRuntimeApiDoc(uriInfo);
    }

    /**
     * Get the OAS doc in YAML format.
     *
     * @param uriInfo
     *      The request {@link UriInfo}.
     * @return
     *      The API doc as a YAML string.
     * @throws IOException
     *      If there are problems generating the doc.
     */
    @GET
    @Path("/yaml")
    @Produces({"text/yaml","application/yaml", "application/x-yaml"})
    public String yamlApiDoc(@Context UriInfo uriInfo) throws IOException {
        Map<String, Object> doc = getRuntimeApiDoc(uriInfo);
        return mapper.writeValueAsString(doc);
    }

    /**
     * Get the API doc that represents the running service.
     *
     * @param uriInfo
     *      The request {@link UriInfo}.
     * @return
     *      The API doc with the runtime values set.
     * @throws IOException
     *      If there is a problem loading the static doc.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRuntimeApiDoc(UriInfo uriInfo) throws IOException {
        URI baseUri = uriInfo.getBaseUri();

        // Load the doc and inject the host
        Map<String, Object> doc = load();
        String server = ((List<Map<String, String>>) doc.get("servers")).get(0).get("url");

        URI runtimeUri = UriBuilder.fromUri(server)
                .scheme(baseUri.getScheme())
                .host(baseUri.getHost())
                .port(baseUri.getPort())
                .build();

        List<Map<String, String>> servers = new ArrayList<Map<String, String>>() {{
            add(new HashMap<String, String>() {{
                put("url", runtimeUri.toString());
            }});
        }};

        doc.put("servers", servers);

        return doc;
    }

    /**
     * Load the petstore API doc.
     *
     * @return
     *      The API doc as a {@link Map}.
     * @throws IOException
     *      If there are errors loading or parsing the doc.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> load() throws IOException {
        return mapper.readValue(getClass().getResourceAsStream("/petstore.yml"), HashMap.class);
    }
}
