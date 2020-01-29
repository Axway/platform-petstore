package com.axway.client.mbaas;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * Configuration class for options related to the MBaaS service.
 */
public class MBaasConfiguration extends Configuration {

    /**
     * The application key in MBaaS.
     */
    private String key;

    /**
     * The user credentials to log into.
     */
    private String username;

    /**
     * The user password to log in using.
     */
    private String password;

    /**
     * The MBaaS hostname to connect to.
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
     * Retrieves the configured username value.
     *
     * @return a {@link String} username.
     */
    @JsonProperty("username")
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the configured username value.
     *
     * @param username
     *      the username to configure internally.
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the configured password value.
     *
     * @return a {@link String} password.
     */
    @JsonProperty("password")
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the configured password value.
     *
     * @param password
     *      the password to configure internally.
     */
    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
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
