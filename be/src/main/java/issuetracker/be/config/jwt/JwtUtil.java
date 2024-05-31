package issuetracker.be.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final int EXPIRATION_TIME = 60 * 60 * 1000; // 1시간
  private final Environment env;

  @Autowired
  public JwtUtil(Environment env) {
    this.env = env;
  }

  public String createToken(String userId) {
    return Jwts.builder()
        .setHeaderParams(createJwtHeader())
        .claim("login_id", userId)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(Keys.hmacShaKeyFor(env.getProperty("jwt.secretKey").getBytes()), SignatureAlgorithm.HS512)
        .compact();
  }

  private Map<String, Object> createJwtHeader() {
    Map<String, Object> headers = new HashMap<>();
    headers.put("typ", "JWT");
    headers.put("alg", "HS512");
    return headers;
  }
}
