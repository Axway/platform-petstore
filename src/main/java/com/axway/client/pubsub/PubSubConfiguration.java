package com.axway.client.pubsub;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * Configuration class for options related to the PubSub service.
 */
public class PubSubConfiguration extends Configuration {

    /**
     * The authentication key used to communicate with PubSub.
     */
    private String key;

    /**
     * The authentication secret used to communicate with PubSub.
     */
    private String secret;

    /**
     * The hostname of the remote PubSub instance to connect to.
     */
    private String hostname;

    /**
     * Retrieves the configured key value.
     *
     * @return a {@link String} key.
     */
    @JsonProperty("key")
    public String getKey() {
        return this.key;
    }

    /**
     * Sets the configured key value.
     *
     * @param key
     *      the key to configure internally.
     */
    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Retrieves the configured secret value.
     *
     * @return a {@link String} secret.
     */
    @JsonProperty("secret")
    public String getSecret() {
        return this.secret;
    }

    /**
     * Sets the configured secret value.
     *
     * @param secret
     *      the secret to configure internally.
     */
    @JsonProperty("secret")
    public void setSecret(String secret) {
        this.secret = secret;
    }

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
