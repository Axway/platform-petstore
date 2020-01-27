package com.axway.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Health check class used to verify service health.
 */
public class PetStoreHealthCheck extends HealthCheck {

    /**
     * Checks whether the service is healthy.
     *
     * TODO: Implement third party service checks.
     *
     * @return a {@link Result} determining service health.
     */
    @Override
    protected Result check() {
        return Result.healthy();
    }
}
