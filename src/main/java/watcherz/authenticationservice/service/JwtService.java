package watcherz.authenticationservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import watcherz.authenticationservice.model.User;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service @RequiredArgsConstructor
public class JwtService
{
    private final String SECRET_KEY = "secret";
    private final String tokenPrefix = "Bearer ";
    private final String headerString = "Authorization";

    public String createToken(User user) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withIssuer("AuthenticationService")
                .withClaim("userId", user.getUserId().toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public String verifyToken(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("AuthenticationService")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userId").asString();
    }
}