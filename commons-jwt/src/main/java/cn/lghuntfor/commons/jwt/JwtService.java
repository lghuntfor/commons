package cn.lghuntfor.commons.jwt;

import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * jwt工具类
 * 基于spring容器
 * @author liaogang
 * @date 2020/9/1 16:40
 */
@Configuration
public class JwtService {

    /**
     * 刷新jwt token的时间, 以分钟为单位
     * 即距离过期在多少时间段内才刷新token
     */
    @Value("${jwt.refresh.minute:30}")
    private Integer jwtRefreshMinute;

    /**
     * jwt的默认过期时间, 以分钟为单位
     */
    @Value("${jwt.expire.minute:120}")
    private Integer jwtExpireMinute;

    /**
     * 刷新jwt token的时间, 以分钟为单位
     * 即距离过期在多少时间段内才刷新token
     */
    @Value("${jwt.cache.capacity:500}")
    private Integer jwtCacheCapacity;

    /**
     * jwt 证书发行人
     */
    @Value("${jwt.certificate.issuer}")
    private String jwtCertificateIssuer;

    /**
     * 证书生成的算法密钥
     */
    @Value("${jwt.algorithm.secret}")
    private String jwtAlgorithmSecret;

    public static final String DEFAULT_KEY = "user-info";

    /**
     * 基于LRU的本地缓存,
     */
    private LRUCache<String, VerifyR> CACHE = null;

    private Algorithm algorithm = null;


    @PostConstruct
    public void init() {
        CACHE = new LRUCache<>(jwtCacheCapacity, jwtRefreshMinute * 60000);
        algorithm = Algorithm.HMAC256(jwtAlgorithmSecret);
    }

    /**
     * 创建jwt token
     * @author liaogang
     * @date 2020/9/1
     * @return java.lang.String
     */
    public String createToken(String value) {
        return createToken(jwtExpireMinute, jwtCertificateIssuer, DEFAULT_KEY, value);
    }

    /**
     * 创建jwt token
     * @author liaogang
     * @date 2020/9/1
     * @param expireMinute
     * @return java.lang.String
     */
    public String createToken(int expireMinute, String value) {
        return createToken(expireMinute, jwtCertificateIssuer, DEFAULT_KEY, value);
    }

    /**
     * 创建jwt token
     * @author liaogang
     * @date 2020/9/1
     * @param expireMinute
     * @return java.lang.String
     */
    public String createToken(int expireMinute, String key, String value) {
        return createToken(expireMinute, jwtCertificateIssuer, key, value);
    }

    /**
     * 创建jwt token
     * @author liaogang
     * @date 2020/9/1
     * @param expireMinute
     * @return java.lang.String
     */
    public String createToken(int expireMinute, String issuer, String key, String value) {
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(DateUtil.offsetMinute(new Date(), expireMinute))
                .withClaim(key, value)
                .sign(algorithm);
    }

    /**
     * 验证jwt token
     * @author liaogang
     * @date 2020/9/1
     * @param token
     * @return com.auth0.jwt.interfaces.DecodedJWT
     */
    public VerifyR verifyToken(String token) {
        return verifyToken(token, jwtCertificateIssuer, DEFAULT_KEY);
    }

    /**
     * 验证jwt token
     * @author liaogang
     * @date 2020/9/1
     * @param token
     * @return com.auth0.jwt.interfaces.DecodedJWT
     */
    public VerifyR verifyToken(String token, String issuer) {
        return verifyToken(token, issuer, DEFAULT_KEY);
    }

    /**
     * 验证jwt token
     * @author liaogang
     * @date 2020/9/1
     * @param token
     * @return com.auth0.jwt.interfaces.DecodedJWT
     */
    public VerifyR verifyToken(String token, String issuer, String key) {
        VerifyR verifyR = CACHE.get(token);
        if (verifyR != null) {
            return verifyR;
        }

        synchronized (token) {
            verifyR = CACHE.get(token);
            if (verifyR != null) {
                return verifyR;
            }

            verifyR = getVerifyResult(token, issuer, key);
            CACHE.put(token, verifyR);
        }
        return verifyR;
    }

    private VerifyR getVerifyResult(String token, String issuer, String key) {
        VerifyR verifyR;
        JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
        verifyR = new VerifyR();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            String result = decodedJWT.getClaim(key).asString();

            verifyR.setExpired(expiresAt.getTime() < System.currentTimeMillis());
            verifyR.setVerifyPass(true);
            verifyR.setDecodedJWT(decodedJWT);
            verifyR.setResult(result);
        } catch (Exception e) {
            verifyR.setVerifyPass(false);
        }
        return verifyR;
    }

}
