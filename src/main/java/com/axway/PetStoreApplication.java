package com.axway;

import com.axway.client.pubsub.PubSubClient;
import com.axway.db.PetDAO;
import com.axway.db.PetMemoryDAO;
import com.axway.health.PetStoreHealthCheck;
import com.axway.resources.EventResource;
import com.axway.resources.IndexResource;
import com.axway.resources.PetResource;
// tag::axway-id
import com.axway.keycloak.KeycloakAuthFilter;
import com.axway.keycloak.KeycloakBundle;
import com.axway.keycloak.KeycloakConfiguration;
import com.axway.keycloak.User;
// end::axway-id
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

/**
 * Main application class backing the Platform PetStore application.
 */
public class PetStoreApplication extends Application<PetStoreConfiguration> {

    /**
     * Simple main method to start the application.
     */
    public static void main(final String[] args) throws Exception {
        new PetStoreApplication().run(args);
    }

    /**
     * Returns the name of the application.
     *
     * @return a {@link String} name of the application.
     */
    @Override
    public String getName() {
        return "PetStore";
    }

    /**
     * Initialize any custom bundles required for the service.
     *
     * @param bootstrap
     *      the {@link Bootstrap} object used to attach bundles.
     */
    @Override
    public void initialize(Bootstrap<PetStoreConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
        // tag::axway-id
        bootstrap.addBundle(new KeycloakBundle<PetStoreConfiguration>() {
            @Override
            protected KeycloakConfiguration getKeycloakConfiguration(PetStoreConfiguration configuration) {
                return configuration.getKeycloakConfiguration();
            }
        });
        // end::axway-id
    }

    /**
     * Configures the service classes and starts the backing server.
     *
     * @param configuration
     *      the parsed application {@link PetStoreConfiguration} instance.
     * @param environment
     *      the Dropwizard {@link Environment} used to configure the backing service.
     */
    @Override
    public void run(PetStoreConfiguration configuration, Environment environment) {
        // TODO: Integrate PubSub capabilities
        PubSubClient pubsub = new PubSubClient(
            configuration.getPubSubConfiguration().getHostname(),
            configuration.getPubSubConfiguration().getKey(),
            configuration.getPubSubConfiguration().getSecret()
        );

        PetDAO petDAO = new PetMemoryDAO();

        environment.healthChecks().register("pet-store", new PetStoreHealthCheck());

        environment.jersey().register(new EventResource());
        environment.jersey().register(new IndexResource(configuration));
        environment.jersey().register(new PetResource(petDAO));
    }
}
