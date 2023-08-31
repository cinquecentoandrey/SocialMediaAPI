package com.cinquecento.smapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.time.ZonedDateTime;

@Component
@PropertySource("classpath:application.properties")
public class JWTUtil {

    @Value("${jwt.token.subject}")
    private String SUBJECT;
    @Value("${jwt.token.expired}")
    private int EXPIRED;

    @Value("${jwt.token.issuer}")
    private String ISSUER;

    @Value("${jwt.token.secret}")
    private String SECRET;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(EXPIRED).toInstant());

        return JWT
                .create()
                .withSubject(SUBJECT)
                .withClaim("username", username)
                .withIssuedAt(Instant.now())
                .withIssuer(ISSUER)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("username").asString();
    }
}
