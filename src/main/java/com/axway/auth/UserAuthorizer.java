package com.axway.auth;

import io.dropwizard.auth.Authorizer;

/**
 * Simple authorizer to ensure that users have the required role.
 */
public class UserAuthorizer implements Authorizer<User> {
    /**
     * Verify that the user has the required role.
     *
     * @param user
     *      the {@link User} to verify.
     * @param role
     *      the role the user is required to have.
     * @return
     *      true if the user has the required role.
     */
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles().contains(role);
    }
}