package com.axway.entitlements;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration class for the Entitlements Service, specifically the part for AxwayID connectivity.
 *
 * <p>For this presentation, we use a service account and the Client Credentials flow.
 */
public class AxwayIdConfiguration {

  private String baseUri;
  private String realm;
  private String clientId;
  private String clientSecret;

  /**
   * Retrieves the configured base URI of AxwayID, e.g.{@literal https://login-preprod.axway.com}.
   *
   * @return
   */
  @JsonProperty("baseUri")
  public String getBaseUri() {
    return baseUri;
  }

  /**
   * Sets the configured base URI of AxwayID, e.g.{@literal https://login-preprod.axway.com}.
   *
   * @param baseUri
   */
  public void setBaseUri(String baseUri) {
    this.baseUri = baseUri;
  }

  /**
   * Retrieves the configured AxwayID realm. For the Platform, it is almost always 'Broker' (without
   * the quotes).
   *
   * @param realm
   */
  public void setRealm(String realm) {
    this.realm = realm;
  }

  /**
   * Sets the configured AxwayID realm.
   *
   * @return
   * @see #getRealm()
   */
  @JsonProperty("realm")
  public String getRealm() {
    return realm;
  }
  /**
   * Retrieves the configured client ID for obtaining access tokens from AxwayID.
   *
   * @return a service account client ID.
   */
  @JsonProperty("clientId")
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets the configured client ID.
   *
   * @param clientId
   * @see #getClientId()
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Retrieves the configured client secret.
   *
   * @return a client secret
   */
  @JsonProperty("clientSecret")
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * Sets the configured client secret.
   *
   * @param clientSecret
   * @see #getClientSecret()
   */
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
