package com.axway.resources;

import com.axway.PetStoreConfiguration;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Rendering resource for the single page app in browser.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {

    /**
     * The internal index view being rendered to the UI.
     */
    private final IndexView index;

    /**
     * Constructs a new resource from the app configuration.
     *
     * @param configuration
     *      the main application configuration instance.
     */
    public IndexResource(PetStoreConfiguration configuration) {
        this.index = new IndexView(configuration);
    }

    /**
     * Renders the main index template.
     *
     * @return the internal {@link IndexView} instance.
     */
    @GET
    public IndexView get() {
        return this.index;
    }

    /**
     * Internal view class used to render the index template.
     */
    private static class IndexView extends View {

        /**
         * The internal configuration reference.
         */
        private final PetStoreConfiguration configuration;

        /**
         * Initializes a new index view from a configuration.
         *
         * @param configuration
         *      the main application configuration instance.
         */
        private IndexView(PetStoreConfiguration configuration) {
            super("/assets/index.mustache");
            this.configuration = configuration;
        }

        /**
         * Retrieves the platform hostname from the configuration.
         *
         * @return a {@link String} hostname to use when rendering.
         */
        public String getPlatformHostname() {
            return this.configuration.getPlatformConfiguration().getHostname();
        }

        /**
         * Retrieves the URI for the REST API.
         *
         * @return {@link String} the URI for the REST API.
         */
        public String getApiUrl() {
            String restUrl = this.configuration.getCentralConfiguration().getProxy().toString();
            if (restUrl.equals("")) {
                String contextPath = ((DefaultServerFactory) this.configuration.getServerFactory()).getApplicationContextPath();
                restUrl = UriBuilder.fromPath(contextPath)
                        .path("/api/v1")
                        .build()
                        .toString();
            }
            return restUrl;
        }
    }
}
