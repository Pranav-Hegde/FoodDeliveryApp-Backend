package FoodDelivery.auth.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey12"; // 32+ chars

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✅ Generate JWT with role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey()) // ✅ FIXED (non-deprecated)
                .compact();
    }
}