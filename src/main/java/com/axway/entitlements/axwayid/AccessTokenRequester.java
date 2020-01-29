package com.axway.entitlements.axwayid;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.axway.entitlements.AxwayIdConfiguration;
import com.axway.entitlements.util.Trace;

/**
 * Responsible for obtaining access tokens from AxwayID, for querying the Entitlements Service.
 *
 * <p>Ideally, this class would be an injectable service.
 */
public class AccessTokenRequester {

  private final AxwayIdConfiguration configuration;

  public AccessTokenRequester(@Nonnull AxwayIdConfiguration configuration) {
    this.configuration = Objects.requireNonNull(configuration);
  }

  public AuthTokens getAuthTokens() {

    UriBuilder uriBuilder =
        UriBuilder.fromUri(configuration.getBaseUri())
            .path("auth/realms")
            .path(configuration.getRealm())
            .path("protocol/openid-connect/token");

    Entity<?> requestEntity =
        Entity.form(
            new Form()
                .param("client_id", configuration.getClientId())
                .param("client_secret", configuration.getClientSecret())
                .param("grant_type", "client_credentials")
                .asMap());

    AuthTokens authTokens =
        ClientBuilder.newClient()
            .target(uriBuilder)
            .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
            .post(requestEntity, AuthTokens.class);

    // have some fun with those access tokens nicely printed in the logs
    Trace.info(authTokens);

    return authTokens;
  }
}
