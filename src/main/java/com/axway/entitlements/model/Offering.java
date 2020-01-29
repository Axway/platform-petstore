package com.axway.entitlements.model;

import java.util.Set;

import com.axway.entitlements.util.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Offering {

  /**
   * Equality for entitlements can be established based on their IDs. However, for brevity, neither
   * the ID property nor the equality relation are implemented here.<br>
   * Nevertheless we use {@code Set} to emphasize the uniqueness property of the results.
   */
  public Set<Entitlement> entitlements;

  /** Presentation aid. */
  @Override
  public String toString() {
    return ToString.fromPublicFields(Offering.class, this);
  }
}
