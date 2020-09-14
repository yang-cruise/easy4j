package cn.easy4j.admin.core.security;

import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.framework.util.ApplicationUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author ChenYichen
 */
@Slf4j
public class JwtUtil {

    private static final String SECRET = "94b6fdb8-b6f6-4c6b-a725-3e2010cbc4b9";
    private static final String ISSUER = "easy4j.security";

    private JwtUtil() {}

    public static String createToken(Long userId, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(String.valueOf(userId))
                    .withAudience(username)
                    .withExpiresAt(DateUtils.addHours(new Date(), 6))
                    .withIssuedAt(new Date());
            return builder.sign(algorithm);
        } catch (Exception e) {
            log.error("生成Token失败：{}", e.getMessage());
            throw new BusinessException("生成token失败");
        }
    }

    public static Map<String, String> verifyToken(String token) {
        Algorithm algorithm;
        Map<String, Claim> map;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            map = jwt.getClaims();
        } catch (Exception e) {
            HttpServletRequest request = ApplicationUtil.getRequest();
            log.error("请求路径：【{}】，认证失败：{}", Objects.isNull(request) ? "" : request.getRequestURI(), e.getMessage());
            throw new PreAuthenticatedCredentialsNotFoundException("认证失败，请重新登录", e);
        }
        Map<String, String> resultMap = new HashMap<>(map.size());
        map.forEach((k, v) -> resultMap.put(k, v.asString()));
        checkExpire(map);
        return resultMap;
    }

    public static void checkExpire(Map<String, Claim> map) {
        Long exp = map.get("exp").asLong();
        if (exp.compareTo(System.currentTimeMillis()) > 0) {
            throw new CredentialsExpiredException("凭证已过期，请重新登录");
        }
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (token == null) {
            token = request.getHeader("token");
        } else if (token.contains(tokenHead)) {
            token = token.substring(tokenHead.length());
        }
        if ("".equals(token)) {
            token = null;
        }
        return token;
    }
}
