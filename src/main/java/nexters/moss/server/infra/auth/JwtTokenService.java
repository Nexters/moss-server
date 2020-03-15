package nexters.moss.server.infra.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nexters.moss.server.domain.model.Token;
import nexters.moss.server.domain.service.HabikeryTokenService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;

@Service
public class JwtTokenService implements HabikeryTokenService {
    private String key;

    // TODO: export to config file
    public JwtTokenService() {
        this.key = "secret";
    }

    @Override
    public String createToken(Long userId, String accessToken) {
       return Jwts.builder()
                .setSubject(userId.toString())
                .claim("accessToken", accessToken)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    @Override
    public Token recoverToken(String habikeryToken) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(habikeryToken);

        Claims body = claims.getBody();

        return new Token(
                Long.parseLong(body.getSubject()),
                body.get("accessToken", String.class),
                habikeryToken
        );
    }
}
