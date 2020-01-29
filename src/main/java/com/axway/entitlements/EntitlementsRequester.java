package com.axway.entitlements;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.axway.entitlements.axwayid.AccessTokenRequester;
import com.axway.entitlements.axwayid.AuthTokens;
import com.axway.entitlements.model.Entitlement;
import com.axway.entitlements.model.Offering;
import com.axway.entitlements.model.Organization;
import com.axway.entitlements.model.Subscription;
import com.axway.entitlements.util.Trace;

/**
 * Responsible for retrieving entitlements from the Entitlements Service.
 *
 * <p>Ideally, this class would be an injectable service.
 */
public class EntitlementsRequester {

  private final EntitlementsConfiguration configuration;
  private final AccessTokenRequester accessTokenRequester;

  public EntitlementsRequester(@Nonnull EntitlementsConfiguration configuration) {
    this.configuration = Objects.requireNonNull(configuration);
    this.accessTokenRequester = new AccessTokenRequester(configuration.getAxwayIdConfiguration());
  }

  public Set<Entitlement> getEntitlements() {

    UriBuilder uriBuilder =
        UriBuilder.fromUri(configuration.getApiUri())
            .path(configuration.getOrganizationId())
            .queryParam("embed", "subscriptions.[entitlements, offerings.entitlements]");

    Organization organization =
        ClientBuilder.newClient()
            .target(uriBuilder)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + getBearerToken())
            .get(Organization.class);

    // make sure we see some action in the log
    Trace.info(organization);

    return entitlementsOf(organization);
  }

  private String getBearerToken() {
    AuthTokens authTokens = accessTokenRequester.getAuthTokens();
    if (!"bearer".contentEquals(authTokens.typeOfUse)) {
      throw new RuntimeException("Got a token of unknown type from AxwayID " + authTokens);
    }
    return authTokens.accessToken;
  }

  private static Set<Entitlement> entitlementsOf(Organization organization) {
    // preserve topology, in case client code needs it
    Set<Entitlement> allEntitlements = new LinkedHashSet<>();

    Set<Subscription> subscriptions = organization.subscriptions;
    if (subscriptions != null) {

      for (Subscription subscription : subscriptions) {
        Set<Offering> offerings = subscription.offerings;
        if (offerings != null) {

          for (Offering offering : offerings) {
            Set<Entitlement> entitlements = offering.entitlements;
            if (entitlements != null) {

              allEntitlements.addAll(entitlements);
            }
          }
        }
      }
    }
    return allEntitlements;
  }
}
