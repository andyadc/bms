package com.andyadc.bms.security.auth.jwt;

import com.andyadc.bms.security.auth.JwtAuthenticationToken;
import com.andyadc.bms.security.config.JwtSettings;
import com.andyadc.bms.security.model.UserContext;
import com.andyadc.bms.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link com.andyadc.bms.security.model.token.JwtToken} to perform authentication.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();

        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserContext context = UserContext.create(subject, authorities);

        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return (JwtAuthenticationToken.class.isAssignableFrom(clazz));
    }
}
