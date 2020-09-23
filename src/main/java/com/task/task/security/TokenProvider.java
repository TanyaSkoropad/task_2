package com.task.task.security;

import com.task.task.repository.BlackListsJWTRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This class implements useful functions:
 * 1. Generate a JWT token
 * 2. Validate a JWT token
 * 3. Parse username from JWT token
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {


    @Value("${jwt.tokenSecret}")
    private String secret;

    @Value("${jwt.accessTokenExpiration}")
    private int expirationTime;

    private final BlackListsJWTRepository blackListsJWTRepository;

    public String generateToken(Authentication authentication) {

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String jwt =  Jwts.builder()
                .setSubject((userPrinciple.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return SecurityConstant.BEARER + jwt;

    }

    public boolean validateJwtToken(String authToken) {
        if (blackListsJWTRepository.existsByJwt(authToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: " + e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: " + e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: " + e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: " + e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: " + e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

}
