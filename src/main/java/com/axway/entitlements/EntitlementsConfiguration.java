package com.axway.entitlements;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Configuration class for the Entitlements Service */
public class EntitlementsConfiguration {

  private String apiUri;
  private String organizationId;
  private AxwayIdConfiguration axwayIdConfiguration;

  /**
   * Retrieves the URI at which entitlements are served by the Entitlements Service.
   *
   * @return e.g. {@literal https://host:port/api/v2}
   */
  @JsonProperty("apiUri")
  public String getApiUri() {
    return apiUri;
  }

  /**
   * Sets the URI at which entitlements are served by the Entitlements Service.
   *
   * @param apiUri
   * @see #getApiUri()
   */
  public void setApiUri(String apiUri) {
    this.apiUri = apiUri;
  }

  /**
   * Retrieves the configured ID of the organization whose entitlements are to be checked.
   *
   * @return the ID of an organization in the Entitlements Service.
   */
  @JsonProperty("organizationId")
  public String getOrganizationId() {
    return organizationId;
  }

  /**
   * Sets the configured organization ID.
   *
   * @param organizationId
   * @see #getOrganizationId()
   */
  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

  @JsonProperty("axwayId")
  public AxwayIdConfiguration getAxwayIdConfiguration() {
    return axwayIdConfiguration;
  }

  public void setAxwayIdConfiguration(AxwayIdConfiguration axwayIdConfiguration) {
    this.axwayIdConfiguration = axwayIdConfiguration;
  }
}
