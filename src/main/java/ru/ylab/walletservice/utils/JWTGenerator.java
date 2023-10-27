package ru.ylab.walletservice.utils;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.util.List;

/**
 * This class generate an RSA key pair, which will be used for signing and verification
 * of the JWT, wrapped in a JWK
 * Give the JWK a Key ID (kid), which is just the polite thing to do
 */
public class JWTGenerator {

    private static final JWTGenerator JWT_GENERATOR = new JWTGenerator();

    private JWTGenerator() {}

    public static JWTGenerator getInstance() {
        return JWT_GENERATOR;
    }

    public RsaJsonWebKey getRsaJsonWebKey() throws JoseException {

        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

        rsaJsonWebKey.setKeyId("k1");
        return rsaJsonWebKey;
    }

    /**
     * Method Create the Claims, which will be the content of the JWT
     * @param userName String
     * @param roles List
     * @return JwtClaims
     */
    public JwtClaims getJwtClaims(String userName, List<String> roles) {

        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Issuer");
        claims.setAudience("Audience");
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject("subject");
        claims.setClaim("username", userName);
        claims.setStringListClaim("roles", roles);
        return claims;
    }

    /**
     * A JWT is a JWS and/or a JWE with JSON claims as the payload, create a JsonWebSignature object.
     * Set the signature algorithm on the JWT/JWS that will integrity protect the claims
     * @param rsaJsonWebKey
     * @param claims
     * @return JwtSignature
     * @throws JoseException
     */
    public String getJwtSignature(RsaJsonWebKey rsaJsonWebKey, JwtClaims claims) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    /**
     *
     * @param rsaJsonWebKey
     * @return
     */
    public JwtConsumer getJwtConsumer(RsaJsonWebKey rsaJsonWebKey) {

        return new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer("Issuer")
                .setExpectedAudience("Audience")
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();
    }

    /**
     * Validate the JWT and process it to the Claims
     * @param jwt String
     * @param jwtConsumer JwtConsumer
     * @throws MalformedClaimException
     */
    public void validate(String jwt, JwtConsumer jwtConsumer) throws MalformedClaimException {
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! " + jwtClaims);
        } catch (InvalidJwtException e) {
            System.out.println("Invalid JWT! " + e);
            if (e.hasExpired()) {
                System.out.println("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
            }
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                System.out.println("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
            }
        }
    }
}
