package com.axway;

// start:entitlements
// import com.axway.client.axwayid.AxwayIdClient;
// import com.axway.client.entitlements.EntitlementsClient;
// end:entitlements
// start:pubsub
// import com.axway.client.pubsub.PubSubClient;
// end:pubsub
// start:mbaas
// import com.axway.client.mbaas.MbaasClient;
// end:mbaas
import com.axway.client.socket.WebSocketClient;
// start:entitlements
// import com.axway.core.entitlements.Entitlements;
// import com.axway.core.entitlements.EntitlementsValidator;
// end:entitlements
import com.axway.db.PetDAO;
import com.axway.db.PetMemoryDAO;
import com.axway.health.PetStoreHealthCheck;
import com.axway.resources.ApiResource;
import com.axway.resources.EventResource;
import com.axway.resources.IndexResource;
import com.axway.resources.PetResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.websockets.WebsocketBundle;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.IOException;
import java.util.EnumSet;

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
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

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

        // Select the storage mechanism for this service
        PetDAO petDAO = new PetMemoryDAO();

        // An MBaaS connected client used for calling the MBaaS APIs
        // start:mbaas
        // MbaasClient mbaas = new MbaasClient(configuration.getMbaasConfiguration());
        // petDAO = new PetMbaasDAO(mbaas, pubsub);
        // end:mbaas

        // An AxwayID client used to retrieve tokens for use when querying entitlements
        // start:entitlements
        // AxwayIdClient axwayId = new AxwayIdClient(configuration.getAxwayIdConfiguration());
        // EntitlementsClient entitlements = new EntitlementsClient(axwayId, configuration.getEntitlementsConfiguration());
        // end:entitlements

        // Register a basic health check for our service using the Dropwizard APIs
        environment.healthChecks().register("pet-store", new PetStoreHealthCheck());

        // Attach all of our resource instances against the Jersey servlet
        environment.jersey().register(new ApiResource());
        environment.jersey().register(new EventResource());
        environment.jersey().register(new IndexResource(configuration));
        environment.jersey().register(new PetResource(petDAO));

        // start:entitlements
        // environment.jersey().register(new EntitlementsValidator(entitlements, petDAO));
        // end:entitlements

        // Setup CORS to restrict requests
        enableCorsHeaders(environment.servlets());
    }

    /**
     * Enables CORS options on the API.
     *
     * @param servlet
     *      the Dropwizard {@link ServletEnvironment} to configure for CORS.
     */
    private void enableCorsHeaders(ServletEnvironment servlet) {
        FilterRegistration.Dynamic cors = servlet.addFilter("CORS", CrossOriginFilter.class);

        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
