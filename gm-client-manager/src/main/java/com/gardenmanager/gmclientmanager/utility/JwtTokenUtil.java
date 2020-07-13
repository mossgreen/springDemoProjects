package com.gardenmanager.gmclientmanager.utility;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

//@Component
//public class JwtTokenUtil implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    public String getUsernameFromToken(String token) {
//        return (String) getAllClaimsFromToken(token).get("usr");
//    }
//
//    public String getOrgFromToken(String token) {
//        return (String) getAllClaimsFromToken(token).get("org");
//    }
//
//    public String getSubjectFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    public String getAudienceFromToken(String token) {
//        return getClaimFromToken(token, Claims::getAudience);
//    }
//
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = (Claims)getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(JWTConstants.SIGNING_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    public String generateToken(String userName, String tenantOrClientId) {
//        return doGenerateToken(userName,tenantOrClientId);
//    }
//
//    private String doGenerateToken(String username, String tenantOrClientId) {
//
////        Claims claims = Jwts.claims().setSubject(username).setAudience(tenantOrClientId);
////        claims.put("scopes", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//
//        final HashMap<String, Object> claims = new HashMap<>();
//        claims.put("usr", username);
//        claims.put("org", tenantOrClientId);
//
//        return Jwts.builder()
//                .setClaims(claims)
////                .addClaims(map)
//                .setIssuer("ihobb.example.com")
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWTConstants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
//                .signWith(SignatureAlgorithm.HS256, JWTConstants.SIGNING_KEY)
//                .compact();
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//}
