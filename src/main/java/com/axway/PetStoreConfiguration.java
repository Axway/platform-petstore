package com.axway;

import com.axway.client.platform.PlatformConfiguration;
import com.axway.client.pubsub.PubSubConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration class for options related to the running application.
 */
public class PetStoreConfiguration extends Configuration {

    /**
     * The internal Platform configuration instance.
     */
    private PlatformConfiguration platform = new PlatformConfiguration();

    /**
     * The internal PubSub configuration instance.
     */
    private PubSubConfiguration pubsub = new PubSubConfiguration();

    /**
     * JSON getter for the Platform configuration.
     *
     * @return a {@link PlatformConfiguration} instance.
     */
    @JsonProperty("platform")
    public PlatformConfiguration getPlatformConfiguration() {
        return this.platform;
    }

    /**
     * JSON setter for the Platform configuration.
     *
     * @param configuration
     *      the {@link PlatformConfiguration} for PubSub connectivity.
     */
    @JsonProperty("platform")
    public void setPlatformConfiguration(PlatformConfiguration configuration) {
        this.platform = configuration;
    }

    /**
     * JSON getter for the PubSub configuration.
     *
     * @return a {@link PubSubConfiguration} instance.
     */
    @JsonProperty("pubsub")
    public PubSubConfiguration getPubSubConfiguration() {
        return this.pubsub;
    }

    /**
     * JSON setter for the PubSub configuration.
     *
     * @param configuration
     *      the {@link PubSubConfiguration} for PubSub connectivity.
     */
    @JsonProperty("pubsub")
    public void setPubSubConfiguration(PubSubConfiguration configuration) {
        this.pubsub = configuration;
    }
}
