package com.example.filmview.Security.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJWTService {
    String extractUsername(String token);
    Date extractExpirationDate(String token);
    <T> T extractClaim(String token, Function<Claims,T> claim_resolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extra_claims, UserDetails user_details);
    boolean validateToken(String token, UserDetails userDetails);
    Claims extractAllClaims(String token);

}
