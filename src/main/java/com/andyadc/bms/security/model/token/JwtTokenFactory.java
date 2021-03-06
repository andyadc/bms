package com.andyadc.bms.security.model.token;

import com.andyadc.bms.security.config.JwtSettings;
import com.andyadc.bms.security.model.Scopes;
import com.andyadc.bms.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory implements InitializingBean {
    private final JwtSettings settings;
    private Key key;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = settings.getTokenSigningKey().getBytes();
        key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    /**
     * Factory method for issuing new JWT Tokens.
     */
    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();

//        Key key = new SecretKeySpec(settings.getTokenSigningKey().getBytes(), SignatureAlgorithm.HS512.getJcaName());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(
                        currentTime.plusMinutes(settings.getTokenExpirationTime())
                                .atZone(ZoneId.systemDefault()).toInstant())
                )
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .signWith(key)
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(settings.getRefreshTokenExpTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .signWith(key)
                .compact();

        return new AccessJwtToken(token, claims);
    }

}
