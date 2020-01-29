package com.axway.entitlements.model;

import java.util.Set;

import com.axway.entitlements.util.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {

  /**
   * Equality for subscriptions can be established based on their IDs. However, for brevity, neither
   * the ID property nor the equality relation are shown here.<br>
   * Nevertheless we use {@code Set} to emphasize the uniqueness property of the results.
   */
  public Set<Subscription> subscriptions;

  /** Presentation aid. */
  @Override
  public String toString() {
    return ToString.fromPublicFields(Organization.class, this);
  }
}
