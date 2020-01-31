package com.axway.client.entitlements;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.net.URI;

/**
 * Configuration class for options related to the entitlements service.
 */
public class EntitlementsConfiguration extends Configuration {

    /**
     * The configured organization identifier.
     */
    private String org;

    /**
     * The URI used to connect to the entitlements service.
     */
    private URI uri;

    /**
     * Retrieves the configured service URI.
     *
     * @return a {@link URI} of the entitlements service.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return this.uri;
    }

    /**
     * Sets the configured service URI.
     *
     * @param uri
     *      the URI to set in the configuration.
     */
    @JsonProperty("uri")
    private void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Retrieves the configured organization identifier.
     *
     * @return a {@link String} organization identifier.
     */
    @JsonProperty("org")
    public String getOrg() {
        return this.org;
    }

    /**
     * Sets the configured organization identifier.
     *
     * @param org
     *      the org to set in the configuration.
     */
    @JsonProperty("org")
    private void setOrg(String org) {
        this.org = org;
    }
}
