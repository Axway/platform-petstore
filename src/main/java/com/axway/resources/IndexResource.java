package com.axway.resources;

import com.axway.PetStoreConfiguration;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// tag::axway-id
import io.dropwizard.auth.Auth;
import javax.annotation.security.RolesAllowed;
import com.axway.keycloak.User;
// end::axway-id

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
    // tag::axway-id
    @RolesAllowed("axway_employee")
    // end::axway-id
    @GET
    public IndexView get(
            // tag::axway-id
            @Auth User auth
            // end::axway-id
    ) {
        // tag::axway-id
        this.index.setUser(auth);
        // end::axway-id
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

        // tag::axway-id
        /**
         * Current user
         */
        private User user;

        /**
         * Set the current user
         * @param user
         */
        public void setUser(User user) {
            this.user = user;
        }

        /**
         * Retrieves the current user
         *
         * @return
         */
        public User getUser() {
            return this.user;
        }
        // end::axway-id
    }
}
