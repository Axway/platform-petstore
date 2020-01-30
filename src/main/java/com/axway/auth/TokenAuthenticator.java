package com.axway.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.util.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

/**
 * This is a demo token authenticator, it does not validate the signature of the
 * token. The incoming token payload format is:
 * {code}
 * {
 *   "sub": "John Doe",
 *   "roles": ["TESTER", "ADMIN"]
 * }
 * {code}
 */
public class TokenAuthenticator implements Authenticator<String, User> {
    /**
     * Authenticate the incoming credential. In this example it parses out the "sub"
     * as the user name.
     *
     * @param credentials
     *      the JWT token to validate.
     * @return
     *      the authenticated {@link User}.
     * @throws AuthenticationException
     *      if there are issues parsing the token.
     */
    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
        try {
            int i = credentials.lastIndexOf('.');
            String withoutSignature = credentials.substring(0, i + 1);
            Claims claims = Jwts.parser().parseClaimsJwt(withoutSignature).getBody();
            String sub = claims.getSubject();
            ArrayList<String> roles = claims.get("roles", ArrayList.class);

            if (!Strings.isNullOrEmpty(sub)) {
                System.out.println("Authenticated as: " + sub);
                return Optional.of(new User(sub, new HashSet<String>(roles)));
            }

        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
        return Optional.empty();
    }
}
