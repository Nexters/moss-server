package nexters.moss.server.infra.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nexters.moss.server.domain.user.HabikeryTokenService;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService implements HabikeryTokenService {
    private String key;
    private String subject;
    private String claimName;

    // TODO: export to config file
    public JwtTokenService() {
        this.key = "secret";
        this.subject = "habikeryToken";
        this.claimName = "userId";
    }

    @Override
    public String createToken(Long userId) {
        return Jwts.builder()
                .setSubject(this.subject)
                .claim(this.claimName, userId)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    @Override
    public long recoverToken(String habikeryToken) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(habikeryToken);

        Claims body = claims.getBody();

        return body.get(this.claimName, Long.class);
    }
}
