package githappens.hh.project_management_app.security;

import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

 // Generate JWT TOKEN
public String generateToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}
// Get username from JWT Token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


// Validate JWT Token
public boolean validateJwtToken(String token) {
    try {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
    } catch (SecurityException e) {
        System.out.println("Invalid JWT Signature" + e.getMessage());
    } catch (MalformedJwtException e) {
        System.out.println("Invalid JWT Token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
        System.out.println("JWT token in expired " + e.getMessage());
    } catch (UnsupportedJwtException e) {
        System.out.println("JWT token is unsupported " + e.getMessage());
    } catch (IllegalArgumentException e) {
        System.out.println("JWT claims string is empty " + e.getMessage());
    }
    return false;

}
}