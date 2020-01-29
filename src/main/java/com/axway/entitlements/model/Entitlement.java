package com.axway.entitlements.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entitlement {

  public String name;
  public String value;

  public int intValue() {
    return Integer.parseInt(value);
  }

  /** Presentation aid. */
  @Override
  public String toString() {
    return String.format("%s:%s", name, value);
  }
}
