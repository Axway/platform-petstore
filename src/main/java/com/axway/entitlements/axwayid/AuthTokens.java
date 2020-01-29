package com.axway.entitlements.axwayid;

import com.axway.entitlements.util.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result from a successful request to AxwayID's token endpoint.
 *
 * <ul>
 *   Typically, we get
 *   <li>an access token in terms of a signed JWT
 *   <li>a refresh token
 *   <li>... as well as some other related information.
 * </ul>
 */
public class AuthTokens {

  @JsonProperty("access_token")
  public String accessToken;

  @JsonProperty("refresh_token")
  public String refreshToken;

  @JsonProperty("expires_in")
  public long accessTokenExpiry;

  @JsonProperty("refresh_expires_in")
  public long refreshTokenExpiry;

  /** For instance, bearer token. */
  @JsonProperty("token_type")
  public String typeOfUse;

  /**
   * A single fields containing multiple space-delimited scopes. Generally ignorable with AxwayID.
   * For role-based access control, e.g. between Platform services, refer to the JWT contents.
   */
  @JsonProperty("scope")
  public String scopes;

  @JsonProperty("session_state")
  public String sessionState;

  @JsonProperty("not-before-policy")
  public long notBefore;

  /**
   * Presentation aid. Don't do this at home.<br>
   * Redact your tokens, or avoid including them in toString().
   */
  @Override
  public String toString() {
    return ToString.fromPublicFields(AuthTokens.class, this, "\n");
  }
}
