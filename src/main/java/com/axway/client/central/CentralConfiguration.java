package com.axway.client.central;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.net.URI;

/**
 * Configuration for the REST API proxy.
 */
public class CentralConfiguration extends Configuration {

    /**
     * The base URI of the proxy.
     */
    private URI proxy;

    /**
     * Retrieves the configured proxy uri value.
     *
     * @return {@link URI} the proxy uri.
     */
    @JsonProperty("proxy")
    public URI getProxy() {
        return this.proxy;
    }

    /**
     * Sets the configured proxy uri value.
     *
     * @param proxy the base proxy uri to configure internally.
     */
    @JsonProperty("proxy")
    public void setProxy(URI proxy) {
        this.proxy = proxy;
    }

}