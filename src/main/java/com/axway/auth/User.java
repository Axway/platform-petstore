package com.axway.auth;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple user.
 */
public class User implements Principal {
    /**
     * User name.
     */
    private final String name;

    /**
     * User roles.
     */
    private final Set<String> roles;

    /**
     * Constructor for user with no roles.
     * @param name
     *      the user name.
     */
    public User(String name) {
        this.name = name;
        this.roles = new HashSet<String>();
    }

    /**
     * Constructor.
     *
     * @param name
     *      the user name.
     * @param roles
     *      the user roles.
     */
    public User(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    /**
     * Get the user name.
     * @return the user name.
     */
    public int getId() {
        return name.hashCode();
    }

    /**
     * The user roles.
     * @return the user roles.
     */
    public Set<String> getRoles() {
        return roles;
    }
}