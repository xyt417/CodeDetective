package com.cdd.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Data
@Component
@ConfigurationProperties(prefix = "jwt") // 通过 @Autowired 才能注入
public class JwtUtils {
    private String secret;
    private long ttl;
    private String header;
    public void printConfig() {
        System.out.println("secret: " + secret);
        System.out.println("ttl: " + ttl);
        System.out.println("header: " + header);
    }
    public String generateToken(String userId) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000* ttl); // s to ms
        MacAlgorithm alg = Jwts.SIG.HS512;
        byte[] keyBytes = Base64.decodeBase64(secret);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder().header().add("type", "JWT").and()
                .issuer("CodeDetective")
                .issuedAt(nowDate)
                .expiration(expireDate)
                .subject(userId)
                .id(UUID.randomUUID().toString()) // 生成唯一JWT
                .signWith(key, alg)
                .compact();
    }
    public Claims getPayloadByToken(String token) throws SignatureException, ExpiredJwtException {
        byte[] keyBytes = Base64.decodeBase64(secret);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}
