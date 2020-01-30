package com.axway;


import com.axway.auth.TokenAuthenticator;
import com.axway.auth.UserAuthorizer;
import com.axway.auth.User;
import com.axway.client.mbaas.MbaasClient;
import com.axway.client.pubsub.PubSubClient;
import com.axway.client.socket.WebSocketClient;
import com.axway.db.PetDAO;
import com.axway.db.PetMemoryDAO;
import com.axway.health.PetStoreHealthCheck;
import com.axway.resources.EventResource;
import com.axway.resources.IndexResource;
import com.axway.resources.PetResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import io.dropwizard.websockets.WebsocketBundle;

import java.io.IOException;

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
        bootstrap.addBundle(new WebsocketBundle(WebSocketClient.class));
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
    public void run(PetStoreConfiguration configuration, Environment environment) throws IOException {


        // A PubSub connected client used for publishing events to PubSub
        // start:pubsub
        // PubSubClient pubsub = new PubSubClient(
        //    configuration.getPubSubConfiguration().getHostname(),
        //    configuration.getPubSubConfiguration().getKey(),
        //    configuration.getPubSubConfiguration().getSecret()
        // );
        // end:pubsub

        // Our Pet storage instance
        PetDAO petDAO = null;

        // Select the storage mechanism for this service by uncommenting one of
        // the following lines to construct a PetDAO implementation.
        //
        petDAO = new PetMemoryDAO();

        // Authentication / Authorization
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new TokenAuthenticator())
                        .setAuthorizer(new UserAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        PetDAO petDAO = new PetMemoryDAO();
        // An MBaaS connected client used for calling the MBaaS APIs
        // start:mbaas
        // MbaasClient mbaas = new MbaasClient(configuration.getMbaasConfiguration());
        // petDAO = new PetMbaasDAO(mbaas, pubsub);
        // end:mbaas

        // Register a basic health check for our service using the Dropwizard APIs
        environment.healthChecks().register("pet-store", new PetStoreHealthCheck());

        // Attach all of our resource instances against the Jersey servlet
        environment.jersey().register(new EventResource());
        environment.jersey().register(new IndexResource(configuration));
        environment.jersey().register(new PetResource(petDAO));
    }
}
