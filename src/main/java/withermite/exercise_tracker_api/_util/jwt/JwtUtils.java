package withermite.exercise_tracker_api._util.jwt;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtUtils {
    @Value("${spring.application.name}")
    private String appName;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final Duration duration;

    public JwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.durationDays}") Long days) {
        if (secret == null) {
            throw new RuntimeException("must provide a jwt.secret property");
        }
        if (days == null) {
            throw new RuntimeException("must provide a jwt.durationDays property");
        }
        this.algorithm = Algorithm.HMAC512(secret);
        this.duration = Duration.ofDays(days);
        this.verifier = JWT.require(this.algorithm).build();
    }

    public String generateToken(UserDetails user) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(appName)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(duration))
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // change if we ever care why a token fails
            return null;
        }
    }
}
