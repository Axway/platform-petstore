package com.axway.client.axwayid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * Configuration class for options related to Axway ID.
 */
public class AxwayIdConfiguration {

    /**
     * The Axway ID URI to connect to for requests.
     */
    private URI uri;

    /**
     * The realm within Axway ID to use for token generation.
     */
    private String realm;

    /**
     * The Axway ID client identifier to use in requests.
     */
    private String clientId;

    /**
     * The Axway ID client secret to use in requests.
     */
    private String clientSecret;

    /**
     * Retrieves the URI of Axway ID.
     *
     * @return a {@link URI} used to connect to Axway ID.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return this.uri;
    }

    /**
     * Sets the URI of Axway ID.
     *
     * @param uri
     *      the URI loaded from the configuration.
     */
    @JsonProperty("uri")
    private void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Retrieves the realm of Axway ID.
     *
     * @return the realm used to connect to Axway ID.
     */
    @JsonProperty("realm")
    public String getRealm() {
        return this.realm;
    }

    /**
     * Sets the realm of Axway ID.
     *
     * @param realm
     *      the realm loaded from the configuration.
     */
    @JsonProperty("realm")
    private void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * Retrieves the client identifier to use with Axway ID.
     *
     * @return a client identifier from the configuration.
     */
    @JsonProperty("client_id")
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client id to use with Axway ID.
     *
     * @param id
     *      the id to set in the configuration.
     */
    @JsonProperty("client_id")
    private void setClientId(String id) {
        this.clientId = id;
    }

    /**
     * Retrieves the client secret to use with Axway ID.
     *
     * @return a client secret from the configuration.
     */
    @JsonProperty("client_secret")
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the client secret to use with Axway ID.
     *
     * @param secret
     *      the secret to set in the configuration.
     */
    @JsonProperty("client_secret")
    private void setClientSecret(String secret) {
        this.clientSecret = secret;
    }
}
