package com.axway.client.platform;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * Configuration class for options related to the Platform.
 */
public class PlatformConfiguration extends Configuration {

    /**
     * The hostname of the remote Platform instance to connect to.
     */
    private String hostname;

    /**
     * Retrieves the configured hostname value.
     *
     * @return a {@link String} hostname.
     */
    @JsonProperty("hostname")
    public String getHostname() {
        return this.hostname;
    }

    /**
     * Sets the configured hostname value.
     *
     * @param hostname
     *      the hostname to configure internally.
     */
    @JsonProperty("hostname")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
